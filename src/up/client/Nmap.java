package up.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import up.shared.Device;
import up.shared.LatLongSerial;
import up.shared.MoveTrack;
import up.shared.PointBean;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.impl.PolylineImpl;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.maps.client.overlay.PolylineOptions;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class Nmap implements EntryPoint {
	
	public static NmapServiceAsync dbService = GWT.create(NmapService.class);	
	MapWidget             map = null;
	List<String>       idList = new ArrayList<String>();
	List<Device>    deviceList; 
	LatLng          mapCenter;
	Map<String, String>         idMap = new HashMap<String, String>();
	Map<String, MoveTrack> moveTracks = new HashMap<String, MoveTrack>();
	//Map<String, Polyline> mtPolyLines = new HashMap<String, Polyline>();
	Map<String, TrackView>    mtViews = new HashMap<String, TrackView>();
	
	List<String> colorSet = new LinkedList<String>(
			Arrays.asList("#0000FF", "#FF0000", "#000000", "#00FF00"));
	Iterator<String> colorIter;
	
	public void onModuleLoad() {
		colorIter = colorSet.iterator();	
		Maps.loadMapsApi("ABQIAAAA9-YgywipIImzKN-B0iV1QxR0hDzQZbDglWyOJMg2OfSg-myJwBS3Kg9ANStKKZbDJnTr5y9wtxcv_g", "2", false, 
			   new Runnable() {
		   			public void run() {
		   				fetchDB();
		   				}
	    		});
		}
	
	private void fetchDB(){	
		dbService.getDevices(new AsyncCallback<List<Device>>() {						
			int numDevices;
			@Override
			public void onSuccess(List<Device> result) {
				deviceList = result;
				numDevices = deviceList.size();
				for (final Device dev: result) {
					
					dbService.getTodayTrack(dev, new AsyncCallback<MoveTrack>(){
						@Override
						public void onFailure(Throwable caught) {
							System.out.println("WRONG: <inside getTodayTrack()>");		
							}
						@Override
								public void onSuccess(MoveTrack result) {
									if (result != null /* && result.getStatus().contains("init_ok") */ ) {
										String vid = null;
										String dName = dev.getName();
										String comment = dev.getComment();
										String dId = dev.getDeviceId() + "";
										vid = dId;
										if (dName != null && !dName.equals("") && !dName.equals(null)) {
											vid = dName;
											}
										idList.add(vid);
										idMap.put(dId, vid);
										//MoveTrack mt = null;
										MoveTrack mt = result;
										//mt.newInstance(result);
										mt.setStatus(dId);
										for (PointBean pb : result.getPath()) {
											GWT.log(" (on the fly): "
													+ pb.getLatitude() 
													+ " : "
													+ pb.getLongitude());
											}
										moveTracks.put(dId, mt);
										GWT.log(" status: <" + mt.getStatus() + ">");
										GWT.log(" num freshly of points: " + mt.getNumPoints());
									}
									if (--numDevices <= 0) {// is last
										workPane();
										//System.out.println("Starting workPane()");
										}

								}
						});
					
					}
				}			
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Something goes wrong - inside getDevices()!");
				}
		});
				
		dbService.getNum(new AsyncCallback<Integer>() {			
			@Override
			public void onSuccess(Integer result) {
				System.out.println("Ok! rows: " + result);
				}			
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Something goes wrong!");
				}
			});  
	}
	
	private void workPane() {
		//System.out.println("   ...working workPane()");
		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
		dock.addNorth(new HTML("<h2>Navigator Pane</h2>"), 20);
		
		TextCell textCell = new TextCell();
	    final CellList<String> cellList = new CellList<String>(textCell);
		cellList.setRowCount(idList.size(), true);
	    cellList.setRowData(0, idList);
	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
	    cellList.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	        final String selected = selectionModel.getSelectedObject();
	        if (selected != null) {
	          Window.alert("You selected: " + selected);
	          // get and draw today track for dId = idMap.get(selected)	        	          
	        	}
	      	}
	    });
		dock.addWest(cellList, 140);
		
		String footer = "<h4>Navigator Footer</h4> " 
						+ "<br> #0000FF - all points"
						+ "<br> #00FF00 - points (trimmed)"
						+ "<br> #FF0000 - points (kalmanized)"
						;
	    dock.addSouth(new HTML("<h4>Navigator Footer</h4>"), 40);
	    
		LatLng mapCenter = LatLng.newInstance(55.7395072, 37.6482976);
	    map = new MapWidget(mapCenter, 13);	   
	    map.setSize("100%", "100%");
	    map.addControl(new LargeMapControl());
	    //map.setSize("500px", "300px");
	    //map.setUIToDefault();
	    //map.addMapType(MapType.getHybridMap());
	    //map.setCurrentMapType(MapType.getHybridMap());
	    
	    map.addOverlay(new Marker(mapCenter));
	    //map.getInfoWindow().open(map.getCenter(), new InfoWindowContent("Centre of Russia! 55.67, 36.34"));	    

		for (String mtid : moveTracks.keySet()) {
			System.out.println(" ===(" + mtid + ")=== ");			
			mtViews.put(mtid, 
						new TrackView(mtid, 
									  visualTrack(moveTracks.get(mtid))));
			}
		
	    dock.add(map);
	    RootLayoutPanel.get().add(dock);
	}
	
	private Polyline visualTrack(/* String deviceId */ MoveTrack mt) {
	
		
		Polyline tLine = new Polyline(				
				mt.getLaLoTrimmedPath(), 
				//colorIter.next(),
				"#000000",
				2,
				1,
				PolylineOptions.newInstance(true, true)
				);	
		//pline.insertVertex(index, latlng);
		tLine.setVisible(true);
	    map.addOverlay(tLine);
		
	    return tLine;
		}
		
}

