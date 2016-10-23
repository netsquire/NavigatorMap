package up.client;

import java.util.ArrayList;
import java.util.List;

import up.shared.Device;
import up.shared.Driver;
import up.shared.MoveTrack;
import up.shared.Vehicle;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

public class DriverList {
	
	Widget w = new Widget();
	List<Driver> ids = new ArrayList<Driver>();
	
	public DriverList() {}
	
	/*
	public Widget getW(){		
		return Composite.asWidgetOrNull(w);
		}
	*/
	
	public DriverList(List<Driver> ls) {
		ids = ls;
		}
	
	private static class DriverCell extends AbstractCell<Driver> {
		    @Override
		    public void render(Context context, Driver value, SafeHtmlBuilder sb) {
		      if (value != null) {
		        sb.appendEscaped(value.getName());
		      	}
		      }
		  	}
	  
	public Widget mkList() {
		ProvidesKey<Driver> keyProvider = new ProvidesKey<Driver>() {
			public Object getKey(Driver item) {
				return (item == null) ? null : item.id;
				}
			};
			
		CellList<Driver> cellList = new CellList<Driver>(new DriverCell(), keyProvider);
		cellList.setRowCount(ids.size(), true);
		cellList.setRowData(0, ids);
		final SelectionModel<Driver> selectionModel = new SingleSelectionModel<Driver>(keyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {						
						final Driver selected = ((SingleSelectionModel<Driver>) selectionModel).getSelectedObject();						
						if (selected != null) {
							System.out.println("Selected: " + selected.getName());
							Nmap3.dbService.getCarByDriverName(selected.getName(), new AsyncCallback<Vehicle>() {
								
								@Override
								public void onSuccess(Vehicle result) {
									System.out.println(" on car: " + result.getCarId() + " name: " + result.getName() );
									
									}
								
								@Override
								public void onFailure(Throwable caught) {}
								});
							
							/*
							MoveTrack mt = Nmap3.mtViews.get(selected.getName()).getMoveTrack();							
							LatLng panTo;
				        	if(mt.getPath().size() > 0){
				        		panTo = LatLng.newInstance(mt.getCenterLat(), mt.getCenterLong());
				        		}
				        	else {panTo = LatLng.newInstance(mt.getCenter().getLatitude(), mt.getCenter().getLongitude());	}
				        	Nmap3.getMap().panTo(panTo);
				        	*/
							}	
						
						}					
				});
		cellList.setSelectionModel(selectionModel);
		return cellList;
		}
		}

