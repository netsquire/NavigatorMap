package up.client;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import up.shared.Device;
import up.shared.Driver;
import up.shared.GeoZone;
import up.shared.LatLongSerial;
import up.shared.MoveTrack;
import up.shared.PointBean;
import up.shared.TrackInfo;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MarkerMouseOutHandler;
import com.google.gwt.maps.client.event.MarkerMouseOverHandler;
import com.google.gwt.maps.client.event.PolylineClickHandler;
import com.google.gwt.maps.client.event.MarkerMouseOutHandler.MarkerMouseOutEvent;
import com.google.gwt.maps.client.event.MarkerMouseOverHandler.MarkerMouseOverEvent;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class Nmap3 implements EntryPoint {
	
	String version = "<h4>06.05.2011 - v0.25</h4>";
	
	public static NmapServiceAsync dbService = GWT.create(NmapService.class);	
	private static MapWidget map = null;
	List<String>     idList = new ArrayList<String>();
	static List<Overlay>     gzList = new ArrayList<Overlay>();
	List<Device>     deviceList; 
	static Map<String,  TrackView>    mtViews = new HashMap<String, TrackView>();
	Map<String, Device> devices = new HashMap<String, Device>();
	DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
	List<String> colorSet = new LinkedList<String>(Arrays.asList("#FF0000", "#0000FF", "#000000", "#00FF00"));
	Iterator<String> colorIter = colorSet.iterator();
	TextCell textCell = new TextCell();
	Button button;
	CellList<String> cellList = new CellList<String>(textCell);
	static TextArea ta = new TextArea();
	static String taTemp = "temporary storage";
	boolean stopAnimation = false;
	
	@Override
	public void onModuleLoad() {	
		makeMap();		
		}

	void makeMap(){
		String key = "ABQIAAAA9-YgywipIImzKN-B0iV1QxQinXTYBEa2hfAVt7uyEC7RskR_UxSHAafZ_c_K-AyA2bwrEe-p6W_Kmg";
		Maps.loadMapsApi(key, "2", false, 
			   new Runnable() {
		   			public void run() {
		   				LatLng mapCenter = LatLng.newInstance(55.7395072, 37.6482976);
		   			    setMap(new MapWidget(mapCenter, 13));	   
		   			    getMap().setSize("100%", "100%");
		   			    getMap().addControl(new LargeMapControl());
		   			    //map.addOverlay(new Marker(mapCenter));
		   				workPane();
		   				}
	    		});
		}

	private void workPane() {					
	
	    final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
	    cellList.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	        final String selected = selectionModel.getSelectedObject();
	        MoveTrack mt = mtViews.get(selected).getMoveTrack();
	        if (selected != null) {
	        	// this code isn't working
	        	LatLng panTo;
	        	if(mt.getPath().size() > 0){
	        		panTo = LatLng.newInstance(mt.getCenterLat(), mt.getCenterLong());
	        		}
	        	else { /* if(mt.getPath().size() == 0) */
	        		panTo = LatLng.newInstance(mt.getCenter().getLatitude(), mt.getCenter().getLongitude());
	        		}
	        	getMap().panTo(panTo);
	        	System.out.println(" Pan to selected: " + selected + " -> (" + mt.getPath().size() + ") "
	        							+ panTo.getLatitude() + " : " + panTo.getLongitude());	        		
	        	}
	      		}
	    	});
	    
	    HorizontalPanel hp = new HorizontalPanel();
	    hp.setTitle("Toolbar");
	    Button driversButton = new Button("Drivers");
	    driversButton.addClickHandler(new ClickHandler() {			
			@Override		
			public void onClick(ClickEvent event) {
			        final PopupPanel popup = new PopupPanel();
			        popup.setWidget(new EditDrivers());		
			        popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			          public void setPosition(int offsetWidth, int offsetHeight) {
			            int left = (Window.getClientWidth() - offsetWidth) / 3;
			            int top = (Window.getClientHeight() - offsetHeight) / 3;
			            popup.setPopupPosition(left, top);
			          }
			        });
			      }
		});
	    
	    Button vehiclesButton = new Button("Vehicles");
	    vehiclesButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				new EditVehicles();
			}
		});
	    
	    Button geozButton = new Button("Geo Zones");
	    geozButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				new EditGeoZones();
			}
		});
	    
	    /*
	    Button stopAnim = new Button("Stop animation");
	    geozButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				stopAnimation();
				}
			});
	    */
	    
	    //stopButton.setEnabled(false);
	    hp.setHeight("160");
	    hp.setBorderWidth(2);
		hp.add( driversButton  );
		hp.add( vehiclesButton );
		hp.add( geozButton     );
		//hp.add( stopAnim     );
	    dock.addNorth(hp, 32);	
	    
	    //LatLng mapCenter = LatLng.newInstance(55.7395072, 37.6482976);
	    //getMap().getInfoWindow().open(mapCenter, new InfoWindowContent("Map Center"));
	    fetchDb();	
	}
		
	void fetchDb(){
		dbService.getTracks(new AsyncCallback<List<MoveTrack>>() {			
			@Override
			public void onSuccess(List<MoveTrack> result) {
				for (MoveTrack mt : result) {					 
						final TrackView tv = new TrackView(mt);
						final String devName = mt.getDev().getName();
						mtViews.put(devName, tv);
						devices.put(devName, mt.getDev());
						idList.add( devName);
						System.out.println("  Processed device: " + mt.getDev().getName() 
														   + " (" + mt.getDev().getDeviceId() 
														   + ") -> Path size: " + mt.getPath().size());
						if (mt.getPath().size() > 0){
							tv.setPline(new Polyline(mt.getLaLoPath(), colorIter.next(), 2));
							tv.getPline().addPolylineClickHandler(new PolylineClickHandler() {
								
							@Override
							public void onClick(PolylineClickEvent event) {								
								LatLng cl = event.getLatLng();
								PointBean npb = tv.getMoveTrack().nearest(cl.getLatitude(), cl.getLongitude() );
								LatLng nll = LatLng.newInstance(npb.getLatitude(), npb.getLongitude());
								Geocoder gc = new Geocoder();
								gc.getLocations(nll, new LocationCallback() {
									
									@Override
									public void onSuccess(JsArray<Placemark> locations) {
										System.out.println(" Return locations: " + locations.length());
										StringBuilder value = new StringBuilder();
								            for (int i = 0; i < locations.length(); ++i) {
								              Placemark location = locations.get(i);								              
								              if (location.getAddress() != null) {
								                value.append(location.getAddress());
								              } else {
								                if (location.getCity() != null) {
								                  value.append(location.getCity());
								                }
								                if (location.getAdministrativeArea() != null) {
								                  value.append(location.getAdministrativeArea() + ", ");
								                }
								                if (location.getCountry() != null) {
								                  value.append(location.getCountry());
								                }
								              }								             
								            }
								            ta.setText(" \n " + value.toString());
								            System.out.println(" \n " + value.toString());										
									}								
									@Override
									public void onFailure(int statusCode) {										
										}
								});								
								ta.setValue(npb.getLatitude() + " : " + npb.getLongitude());
								ta.setEnabled(false);
								ta.setText(npb.getLatitude() + " : " + npb.getLongitude());
								getMap().addOverlay(new Marker(cl));
								getMap().addOverlay(new Marker(nll));
							}
							});
							tv.setLastIndex( tv.getPline().getVertexCount() );
							getMap().addOverlay( tv.getPline() );
						}
						else  {	
							final long devId = mt.getDev().getDeviceId();
							System.out.println(" Set death mark for " + devId);							
							dbService.lastPoint(devId, new AsyncCallback<PointBean>() {								
								@Override
								public void onSuccess(final PointBean result) {
									LatLng point = LatLng.newInstance(result.getLatitude(), result.getLongitude()); 									
									Icon icon = Icon.newInstance(Icon.DEFAULT_ICON);									
									icon.setImageURL("cars/niva-gray.png");
									icon.setIconSize(Size.newInstance(32, 32));
									icon.setShadowURL(null);									
									MarkerOptions ops = MarkerOptions.newInstance(icon);
									ops.setIcon(icon);
									Marker marker = new Marker(point, ops);
									
									marker.addMarkerMouseOverHandler(new MarkerMouseOverHandler() {									
										@Override
										public void onMouseOver(MarkerMouseOverEvent event) {
													taTemp = ta.getText();																		
													String s = "Car: " + devName 										 
															+ "  Something happend "
															+ "  Last time <" + result.getHwClock() + ">";
													ta.setText(s);																							
											}
										});
									marker.addMarkerMouseOutHandler(new MarkerMouseOutHandler() {									
										@Override
										public void onMouseOut(MarkerMouseOutEvent event) {
											ta.setText(taTemp);
											}
										});																		
									// ***
									
									Nmap3.getMap().addOverlay(marker);									
									map.addOverlay(marker);
									tv.getMoveTrack().setCenter(new LatLongSerial(point));									
									}								
								@Override
								public void onFailure(Throwable caught) {}
								});							
						}						
					}
				System.out.println(" Start showAll()");
				showAll();				
				}			
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Somebody isn't right!..");
				}
		});
	}
	
	
	
	final String did = "352848023763960";
	final String mtName = "fake";
	LatLng startPoint;
	Marker prevMarker = null;
	int ind = 0;
	int pathLen = 0;
	Timer todo;
	double gasMileage = .00012;
	
	void showAll(){ 	
		for (final String tv: mtViews.keySet()) {
			MoveTrack mt = mtViews.get(tv).getMoveTrack();			
			final int size = mtViews.get(tv).getMoveTrack().getPath().size();
			if (mtViews.get(tv).getMoveTrack().getPath().size() > 0) {
				final Polyline pline = mtViews.get(tv).getPline();
				final double polylen = pline.getLength();
				ind = pline.getVertexCount();
				System.out.println("Launch timer (showAll): " + ind);
				todo = new Timer(){
					@Override
					public void run() {
						dbService.getLastPB(did, new AsyncCallback<PointBean>() {
							
							public void onSuccess(final PointBean result) {
								LatLng point = LatLng.newInstance(result.getLatitude(), result.getLongitude());
								pline.insertVertex(ind++, point);
								Icon icon = Icon.newInstance(Icon.DEFAULT_ICON);								
								icon.setImageURL("cars/gazel.png");
								icon.setIconSize(Size.newInstance(32, 32));
								icon.setShadowURL(null);
								MarkerOptions ops = MarkerOptions.newInstance(icon);
								ops.setIcon(icon);
								Marker marker = new Marker(point, ops);
								marker.addMarkerMouseOverHandler(new MarkerMouseOverHandler() {									
									@Override
									public void onMouseOver(MarkerMouseOverEvent event) {
										taTemp = ta.getText();										
										Nmap3.dbService.getDriverByCarName(tv, new AsyncCallback<String>() {											
											@Override
											public void onSuccess(String driverName) {
												@SuppressWarnings("deprecation")
												String s = "Car: " + tv 
														//+ "\n Size: "  + size 
														+ "   Path: " + Math.round(polylen)
														+ "   Gas Mileage: " + Math.round( polylen * gasMileage ) 
														+ "   Driver: " + driverName
														+ "   Speed: " + result.getSpeed()
														//+ "\n" + (result.getMovement() ? " moving " : " not moving ")
														//+ "\nIgnition: " + (result.getIgnition() ? " on " : " non ")
														+ "   Satellites: " + result.getSatellites()
														//+ "\nPDOP: " + result.getPdop()
														//+ "\nHDOP: " + result.getHdop()
														//+ "\nTime: " + result.getHwClock().getTime() + " mc"
														+ "  <" + result.getHwClock().toGMTString().replace(" GMT", "")
														+ ">  Angle: " + result.getAngle();
												ta.setText(s);		
												}											
											@Override
											public void onFailure(Throwable caught) {
												System.out.println("(getDriverByCarName): Something happend");
											}
										});										
										}
									});
								marker.addMarkerMouseOutHandler(new MarkerMouseOutHandler() {									
									@Override
									public void onMouseOut(MarkerMouseOutEvent event) {
										ta.setText(taTemp);
										}
									});
								if (prevMarker != null)
									Nmap3.getMap().removeOverlay(prevMarker);
								Nmap3.getMap().addOverlay(marker);						
								prevMarker = marker;								
								}							
							@Override
							public void onFailure(Throwable caught) {
								System.out.println("(getLastPoint): Something happend");
								}
						});						
					}};
					todo.scheduleRepeating(500);
				}
			}
		
		cellList.setRowCount(idList.size(), true);
	    cellList.setRowData(0, idList);		    
	   
	    final StackLayoutPanel panel = new StackLayoutPanel(Unit.EM);
	    panel.add((new VehicleList(idList)).mkList(), new HTML(" Vehicles "), 4); 
	    
	    final List<Driver> drList = new LinkedList<Driver>();
		dbService.getDrivers(new AsyncCallback<List<Driver>>() {			
			@Override
			public void onSuccess(List<Driver> result) {				
				for(Driver driver: result){					
					drList.add(new Driver(driver.getName()));
					}	
				panel.add((new DriverList(drList)).mkList(), new HTML(" Drivers "), 4);		
				}			
			@Override
			public void onFailure(Throwable caught) {}
			});
	    	 
	    dock.addWest(panel, 100);
	    
	    ta.setCharacterWidth(200);
	    ta.setVisibleLines(20);
	    dock.addSouth(ta, 60);	    	    
	    
		dock.add(getMap()); 
		RootLayoutPanel.get().add(dock);
		}

	public static void setMap(MapWidget map) {
		Nmap3.map = map;
	}

	public static MapWidget getMap() {
		return map;
	}

	public boolean animationFinish() {
		return stopAnimation;
	}

	public void stopAnimation() {
		this.stopAnimation = true;
	}

	public List<Overlay> getGzList() {
		return gzList;
	}

	public void setGzList(List<Overlay> gzList) {
		this.gzList = gzList;
	}	
}
