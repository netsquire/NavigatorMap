package up.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import up.shared.Device;
import up.shared.MoveTrack;
import up.shared.Vehicle;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class VehicleList  {
	List<Vehicle> ids = new ArrayList<Vehicle>();
	
	public VehicleList() {}
	
	public VehicleList(List<String> ls) {
		for(String devName: ls){
			ids.add(new Vehicle(devName));
			}	
		}
	
	private static class VehicleCell extends AbstractCell<Vehicle> {
		    @Override
		    public void render(Context context, Vehicle value, SafeHtmlBuilder sb) {
		      if (value != null) {
		        sb.appendEscaped(value.name);
		      	}
		      }
		  	}
	  
	public Widget mkList() {
		ProvidesKey<Vehicle> keyProvider = new ProvidesKey<Vehicle>() {
			public Object getKey(Vehicle item) {
				return (item == null) ? null : item.id;
				}
			};
			
		CellList<Vehicle> cellList = new CellList<Vehicle>(new VehicleCell(), keyProvider);
		cellList.setRowCount(ids.size(), true);
		cellList.setRowData(0, ids);
		final SelectionModel<Vehicle> selectionModel = new SingleSelectionModel<Vehicle>(keyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						final Vehicle selected = ((SingleSelectionModel<Vehicle>) selectionModel).getSelectedObject();
						if (selected != null) {
							MoveTrack mt = Nmap3.mtViews.get(selected.name).getMoveTrack();							
							LatLng panTo;
				        	if(mt.getPath().size() > 0){
				        		panTo = LatLng.newInstance(mt.getCenterLat(), mt.getCenterLong());
				        		}
				        	else { /* if(mt.getPath().size() == 0) */
				        		panTo = LatLng.newInstance(mt.getCenter().getLatitude(), mt.getCenter().getLongitude());
				        		}
				        	Nmap3.getMap().panTo(panTo);				        
							}						
					}					
				});
		cellList.setSelectionModel(selectionModel);
		return cellList;
		}
	}
