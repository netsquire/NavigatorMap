package up.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import up.shared.Device;
import up.shared.MoveTrack;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class Nmap2 implements EntryPoint {
	public static NmapServiceAsync dbService = GWT.create(NmapService.class);	
	static MapWidget             map = null;
	List<String>       idList = new ArrayList<String>();
	List<Device>    deviceList; 
	//LatLng          mapCenter;
	//Map<String, String>         idMap = new HashMap<String, String>();
	//Map<String, MoveTrack> moveTracks = new HashMap<String, MoveTrack>();
	Map<String, TrackView>    mtViews = new HashMap<String, TrackView>();
	//Map<String, Integer>      command = new HashMap<String, Integer>();
	Command todo;
	DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
	
	List<String> colorSet = new LinkedList<String>(
			Arrays.asList("#0000FF", "#FF0000", "#000000", "#00FF00"));
	Iterator<String> colorIter;
	
	
	// ================================================
	@Override
	public void onModuleLoad() {
		colorIter = colorSet.iterator();	
		/*
		todo.order("map", makeMap());
		todo.order("db_fetch", fetchDb());
		todo.order("workpane", workPane());		
		todo.action(linkAll());
		*/
		checkCommand();
	}

	// ================================================
	
	private void checkCommand() {
		// TODO Auto-generated method stub
		
	}


	void linkAll(){
		// add everything to RootLayoutPanel 
		RootLayoutPanel.get().add(dock);
		}
	
	void fetchDb(){
		dbService.getTracks(new AsyncCallback<List<MoveTrack>>() {			
			@Override
			public void onSuccess(List<MoveTrack> result) {
				for(MoveTrack mt : result) {
					TrackView tv = new TrackView(mt);
					mtViews.put( mt.getDev().getDeviceId() + "", tv);
					tv.visualize(map);
					//if(mt.isOpen())
						//tv.animate();
					//report("db_fetch", 1);
					}				
				}			
			@Override
			public void onFailure(Throwable caught) {}
		});
	}

	void makeMap(){
		Maps.loadMapsApi("ABQIAAAA9-YgywipIImzKN-B0iV1QxR0hDzQZbDglWyOJMg2OfSg-myJwBS3Kg9ANStKKZbDJnTr5y9wtxcv_g", "2", false, 
			   new Runnable() {
		   			public void run() {
		   				todo.report();
		   				}
	    		});
	}

	private void workPane() {
		//System.out.println("   ...working workPane()");
		
		dock.addNorth(new HTML("<h2>Navigator Pane</h2>"), 20);
		
		TextCell textCell = new TextCell();
	    final CellList<String> cellList = new CellList<String>(textCell);
		cellList.setRowCount(idList.size(), true);
	    cellList.setRowData(0, idList);
	    final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
	    cellList.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	        final String selected = selectionModel.getSelectedObject();
	        if (selected != null) {
	        	Window.alert("You selected: " + selected);       	          
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

	    /*
		for (String mtid : mtViews.keySet()) {
			System.out.println(" ===(" + mtid + ")=== ");			
			mtViews.put(mtid, 
						new TrackView(mtid, 
									  visualTrack(moveTracks.get(mtid))));
			}
		*/
		
	    dock.add(map);
	    
	}
	
	
}
