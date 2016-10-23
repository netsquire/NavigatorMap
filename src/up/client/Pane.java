package up.client;

import java.util.Arrays;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.maps.client.overlay.PolylineOptions;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Pane implements EntryPoint {

	
	MapWidget map;
	
	@Override
	public void onModuleLoad() {
		
	    // Attach five widgets to a DockLayoutPanel, one in each direction. Lay
	    // them out in 'em' units.
	    final DockLayoutPanel p = new DockLayoutPanel(Unit.EM);
	    p.addNorth(new HTML("north"), 2);
	    p.addSouth(new HTML("south"), 2);
	    p.addEast(new HTML("east"), 2);
	    p.addWest(new HTML("west"), 2);
	    //p.add(new HTML("center"));

	    final RootLayoutPanel rp = RootLayoutPanel.get();
	    Maps.loadMapsApi("ABQIAAAA9-YgywipIImzKN-B0iV1QxR0hDzQZbDglWyOJMg2OfSg-myJwBS3Kg9ANStKKZbDJnTr5y9wtxcv_g", "2", false, 
				   new Runnable() {
			   			public void run() {
			   				LatLng mapCenter = LatLng.newInstance(55.7395072, 37.6482976);
			   			    map = new MapWidget(mapCenter, 13);	   
			   			    map.setSize("100%", "100%");
			   			    map.addControl(new LargeMapControl());
			   			    
			   			    map.addOverlay(new Marker(mapCenter));
			   			    
			   			    LatLng[] path = {
			   					LatLng.newInstance(55.73,  37.641),
			   					LatLng.newInstance(55.734, 37.643),
			   					LatLng.newInstance(55.736, 37.645),
			   					LatLng.newInstance(55.738, 37.647),
			   					LatLng.newInstance(55.74,  37.649),
			   			 		};
			   			    
			   			    final Polyline kLine = new Polyline(				
			   					path, 
			   					"#FF0000",				
			   					3,
			   					1,
			   					PolylineOptions.newInstance(true, true)
			   					);	
			   			    //pline.insertVertex(index, latlng);
			   			    kLine.setVisible(true);
			   			    map.addOverlay(kLine);
			   			    

			   			    p.add(map);
			   			    rp.add(p);
			   			    
			   			final double dx = .001;
						final double dy = .002;
			   			int i = 0;
			   			for (final int j: Arrays.asList( 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 )) {
			   				Timer t = new Timer() {
			   					public void run() {	
			   						/*
			   						final Marker m = new Marker(
			   								LatLng.newInstance(55.75 + j*dx, 37.65 + j*dy)); 
			   						map.addOverlay(m);
			   						System.out.println("Marker added to map");
			   						*/
			   						kLine.insertVertex(0, LatLng.newInstance(55.75 + j*dx, 37.65 + j*dy));
			   						/*
			   						Timer r = new Timer() {
			   							@Override
			   							public void run() {
			   								//map.removeOverlay(m);
			   								//System.out.println("Marker removed from map");
			   							}
			   						};			   						
			   						r.schedule(1000);
			   						*/
			   					}
			   				};
			   				t.schedule(++i * 1000);			   				
			   			}
			   			    
			   			    
			   			    /*
			   			    for(int i: Arrays.asList( 0, 1, 2, 3 )) {
			   			    try {
								Thread.sleep(1000);
								kLine.insertVertex(0, LatLng.newInstance(55.75 + i*dx, 37.65 + i*dy));
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			   			    }
			   			    */
			   				}
		    		});
	    
	}

	/*
	 Coords: ()
 Coords: (55.7399232 : 37.648224)
 Coords: (55.7399232 : 37.648224)
 Coords: (55.7399232 : 37.648224)
 Coords: (55.7399232 : 37.648224)
 Coords: (55.7399296 : 37.6482208)
 Coords: (55.739936 : 37.6482208)
 Coords: (55.739936 : 37.6482208)
 Coords: (55.739936 : 37.6482208)
 Coords: (55.739936 : 37.6482208)
 Coords: (55.739936 : 37.648224)
 Coords: (55.739936 : 37.648224)
	
	*/
}
