package up.client;

import java.text.DateFormat;
import java.util.List;

import up.shared.GeoZone;
import up.shared.LatLongSerial;
import up.shared.MoveTrack;
import up.shared.Vehicle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.PolygonMouseOutHandler;
import com.google.gwt.maps.client.event.MapClickHandler.MapClickEvent;
import com.google.gwt.maps.client.event.PolygonMouseOutHandler.PolygonMouseOutEvent;
import com.google.gwt.maps.client.event.PolygonMouseOverHandler.PolygonMouseOverEvent;
import com.google.gwt.maps.client.event.PolygonMouseOverHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class EditGeoZones  extends PopupPanel {
	
	HorizontalPanel holder = new HorizontalPanel();
    boolean stopGeoEdit = true;
    Polygon poly;
	Polyline pline;
	LatLng[] corners = new LatLng[20];
	int k = 0;
	
	public EditGeoZones() {		
		holder.add(new Label(" geoZone "));
		holder.setSpacing(4);
		
		final TextBox zone = new TextBox();
		zone.setName("newGeoZone");
		zone.setValue(" ");
		holder.add(zone);
		holder.add(new Label("  click on map (3 - 20), then press ->  " ));				
		final GeoZone gz = new GeoZone(); 
		
		final MapClickHandler handler = new MapClickHandler() {			
			@Override
			public void onClick(MapClickEvent event) {
					LatLng p = event.getLatLng();
					System.out.println("() Clicked on map: " + p.getLatitude() + " : " + p.getLongitude());
					LatLongSerial click = new LatLongSerial(p.getLatitude(), p.getLongitude());
					LatLng llclick = LatLng.newInstance(p.getLatitude(), p.getLongitude());
					gz.getBody().add(click);
					int len = gz.getBody().size();
					corners[k] = llclick;
					// ***
					Icon icon = Icon.newInstance(Icon.DEFAULT_ICON);
					icon.setImageURL("http://maps.google.com/intl/en_us/mapfiles/ms/micons/blue-dot.png");
					MarkerOptions ops = MarkerOptions.newInstance(icon);
					ops.setIcon(icon);
					Marker marker = new Marker(llclick, ops);
					Nmap3.getMap().addOverlay(marker);
					Nmap3.gzList.add((Overlay) marker);
					// ***
					System.out.println("gz.getBody().size(): " + k + " : " + len);					
					if (k == 1)	{						
						pline = new Polyline(corners);
						Nmap3.getMap().addOverlay(pline);
						Nmap3.gzList.add((Overlay) pline);
						}
					if (k == 2){ 
						poly = new Polygon(corners);
						Nmap3.getMap().addOverlay(poly);
						Nmap3.gzList.add((Overlay) poly);
						}
					if (k > 1)
							pline.insertVertex(k+1, LatLng.newInstance(p.getLatitude(), p.getLongitude()));
					if (k > 2) 
							poly.insertVertex(k+1, LatLng.newInstance(p.getLatitude(), p.getLongitude()));
					k++;						
					}
				};
				
		Nmap3.getMap().addMapClickHandler(handler);			
		Button stopButton = new Button (" Add GeoZone ");
		stopButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				gz.setName(zone.getValue());
				int len = gz.getBody().size();
				LatLongSerial p = gz.getBody().get(0);
				pline.insertVertex(len+1, LatLng.newInstance(p.getLatitude(), p.getLongitude()));
				
				//System.out.println("GeoZone: storing...");
				Nmap3.dbService.storeGZ(gz, new AsyncCallback<Integer>() {
					
					@Override
					public void onSuccess(Integer result) {
						System.out.println("(add GeoZone): GeoZone added");
						Nmap3.gzList.add((Overlay)pline);
						holder.add(new Label(" GeoZone added: " + result));
					}
					
					@Override
					public void onFailure(Throwable caught) {
						System.out.println("(add GeoZone): Something happend");
						}
					});
				}
			});
					
		Button bclose = new Button (" Close window ");
		bclose.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				hide();
				Nmap3.getMap().removeMapClickHandler(handler);
				removeFromParent();
				}
			});
		
		Button baction = new Button (" Action ");
		Button showAll = new Button (" Show all geozones ");
		showAll.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				Nmap3.dbService.getGeoZones(new AsyncCallback<List<GeoZone>>() {
					
					@Override
					public void onSuccess(List<GeoZone> result) {
						for (final GeoZone gz: result) {
							Nmap3.ta.setText(gz.getName());
							int i = 0;
							int len = gz.getBody().size();
							LatLng[] corners = new LatLng[len + 1];
							for (LatLongSerial z: gz.getBody()) {
								LatLng corner = LatLng.newInstance(z.getLatitude(), z.getLongitude());
								corners[i++] = corner;
								}
							corners[i] = corners[0];
							Polygon p = new Polygon(corners);
							p.addPolygonMouseOverHandler(new PolygonMouseOverHandler() {
								
								@Override
								public void onMouseOver(PolygonMouseOverEvent event) {
									Nmap3.taTemp = Nmap3.ta.getText();																		
									String s = "GZone: " + gz.getName() + "\nPoints: " + gz.getBody().size();
									Nmap3.ta.setText(s);
								}
							});
							p.addPolygonMouseOutHandler(new PolygonMouseOutHandler() {
								
								@Override
								public void onMouseOut(PolygonMouseOutEvent event) {
									Nmap3.ta.setText(Nmap3.taTemp);
								}
							});
							
							Nmap3.getMap().addOverlay(p);
							Nmap3.gzList.add(p);
							}
						}
					
					@Override
					public void onFailure(Throwable caught) {
						System.out.println("(get GeoZones): Something happend");
						}
				});
				}
			});
		
		Button hideAll = new Button (" Hide all geozones ");
		hideAll.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				for (Overlay gz: Nmap3.gzList) {
					Nmap3.getMap().removeOverlay(gz);
					}				
				}
			});
				
		holder.add(stopButton);		
		holder.add(baction);
		holder.add(bclose);
		holder.add(showAll);
		holder.add(hideAll);
		setWidget(holder);
		show();
		}
	
	/*
	public void store(GeoZone gz) {
		Nmap3.dbService.storeGZ(gz, new AsyncCallback<Integer>() {
			
			@Override
			public void onSuccess(Integer result) {
				System.out.println("(add GeoZone): GeoZone added");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("(add GeoZone): Something happend");
				}
			});
	}
	
	
	private GeoZone collectPoints(String selected) {
		
		final GeoZone gz = new GeoZone(selected);
		
		Nmap3.getMap().addMapClickHandler(new MapClickHandler() {			
			@Override
			public void onClick(MapClickEvent event) {
				if(stopGeoEdit){
					LatLng p = event.getLatLng();
					gz.getBody().add(
							new LatLongSerial(p.getLatitude(), p.getLongitude()));
					System.out.println("GeoZone: pressed " + p.getLatitude() + " : "+ p.getLongitude());
						}				
				}
			});
	    
		stopButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				setStopGeoEdit(true);
				System.out.println("Stop GeoZone pressed ");
				}
			});
	    
	    final Timer timer = new Timer() {			
			@Override
			public void run() {
				System.out.println("    waiting... ");
				if (isStopGeoEdit())
					this.cancel();
				}
			};
		timer.scheduleRepeating(500);
		System.out.println("Timer stopped.");
		return gz;
		}
	*/

	
	public boolean isStopGeoEdit() {
		return stopGeoEdit;
	}
	public void setStopGeoEdit(boolean stopGeo) {
		stopGeoEdit = stopGeo;
	}
	
	
}
