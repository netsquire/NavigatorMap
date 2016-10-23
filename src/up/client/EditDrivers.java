package up.client;

import java.util.LinkedList;
import java.util.List;

import up.shared.Device;
import up.shared.Driver;
import up.shared.Vehicle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class EditDrivers  extends PopupPanel {

	List<Vehicle> vl = new LinkedList<Vehicle>();
	List<Driver>  dl = new LinkedList<Driver>();
	
	public EditDrivers(){
		super(true);
		
		Nmap3.dbService.getDevices(new AsyncCallback<List<Device>>() {			
			@Override
			public void onSuccess(List<Device> result) {
				for(Device d: result){
					vl.add(new Vehicle(d.getName()));
					}
				Nmap3.dbService.getDrivers(new AsyncCallback<List<Driver>>() {					
					@Override
					public void onSuccess(List<Driver> result) {
						for(Driver d: result){
							dl.add(d);
							}
						{
						setWidget(mkForm(vl,dl));
						show();
						}
						}					
					@Override
					public void onFailure(Throwable caught) {}
					});								
				}			
			@Override
			public void onFailure(Throwable caught) {}
			});			    
		}
	
	public Widget mkForm(List<Vehicle> carList, List<Driver> driverList) {
		//System.out.println("start show()");
		
		System.out.println("(mkForm) Cars: " + carList.size());
		System.out.println("(mkForm) Drivers: " + driverList.size());
		/*
		for (Vehicle car: carList){
			System.out.println("(mkForm) Car: " + car.getName());
			}
		for (Driver d: driverList){
			System.out.println("(mkForm) Driver: " + d.getName());
			}
		*/
		
		FormPanel form = new FormPanel();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.addStyleName("table-center");
		form.addStyleName("demo-FormPanel");
		
		VerticalPanel outer = new VerticalPanel();
		final HorizontalPanel holder = new HorizontalPanel();
		holder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		holder.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		holder.add(new Label("Driver Name: "));
		final ListBox username = new ListBox();
		username.setName("username");
		for(Driver dn: driverList){
			username.addItem(dn.getName(), dn.getName());
			}
		holder.add(username);
		
		holder.add(new Label("Car (Vehicle): "));
		final ListBox car = new ListBox();
		car.setName("car");
		for (Vehicle v: carList){
			car.addItem(v.getName(), v.getName());
			}
		car.setVisibleItemCount(1);
		holder.add(car);
		
		final Button bind = new Button("Bind it!");		
		bind.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				bind.setEnabled(false);
				final String drName = username.getValue(username.getSelectedIndex());
				final String carName = car.getValue(car.getSelectedIndex());
				System.out.println("BIND " +  drName + " to car: " + carName );
				Nmap3.dbService.bindDriver(drName, carName, 
										   new AsyncCallback<Boolean>() {

											@Override
											public void onFailure(Throwable caught) {}

											@Override
											public void onSuccess(Boolean result) {
												holder.add(new Label(drName + " on " + carName ));
												bind.setEnabled(true);
											}
										});
				}
			});		
		holder.add(bind);		
		holder.setBorderWidth(1);
		
		HorizontalPanel holder2 = new HorizontalPanel();
		Button bclose = new Button ("Close window");
		bclose.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				hide();
				removeFromParent();
				}
			});		
		holder2.add(bclose);
		
		final HorizontalPanel newd = new HorizontalPanel();
		final Button newb = new Button ("Add new driver");
		final TextBox tb = new TextBox();
		newb.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				newb.setEnabled(false);
				Nmap3.dbService.addNewDriver(tb.getValue(), new AsyncCallback<Integer>() {
					
					@Override
					public void onSuccess(Integer result) {
						if (result > 0) {
							newd.add(new HTML("New driver is on cage: " + tb.getValue() + " (" + result + ")"));
							System.out.println("New driver is on cage: " + tb.getValue() + " (" + result + ")");
							}
						else if (result == 0){
							newd.add(new HTML("   -- Something happend: " + result ));
							}
						newb.setEnabled(true);
						}
					
					@Override
					public void onFailure(Throwable caught) {
						System.out.println("  -- Something happend: " + caught.getMessage());
					}
				});
				
			}
		});
		newd.add(newb);
		newd.add(tb);
		
		outer.add(holder);
		outer.add(newd);
		outer.add(holder2);
		
		form.setHeight("20em");
		form.setWidth("60em");
		//form.add(holder);
		form.add(outer);
		return form;
		}
	
}
