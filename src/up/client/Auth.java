package up.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Auth {
	
	public Widget show() {
		final FormPanel form = new FormPanel();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.addStyleName("table-center");
		form.addStyleName("demo-FormPanel");
		
		VerticalPanel holder = new VerticalPanel();
		holder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		holder.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		holder.add(new Label("User ID"));
		final TextBox userid = new TextBox();
		userid.setName("userid");
		userid.setValue("admin");
		holder.add(userid);
		holder.add(new Label("Password"));
		final PasswordTextBox passwd = new PasswordTextBox();
		passwd.setName("passwd");
		passwd.setValue("success");
		holder.add(passwd);
		
		Button b = new Button("Submit");		
		b.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				//System.out.println("(ClickHandler) UserId Text: " + userid.getText());
				//System.out.println("(ClickHandler) UserId Value: " + userid.getValue());
				//System.out.println("(ClickHandler) Password Text: " + passwd.getText());
				//System.out.println("(ClickHandler) Password Value: " + passwd.getValue());
				validate(userid.getValue(), passwd.getValue());
				/*
				String url = GWT.getModuleBaseURL();
				System.out.println("URL: " + url);
				form.setAction(url + "Admin/");
				form.submit();
				*/
				}
			});
		holder.add((Widget) b);		
		form.add(holder);
		return form;
		}
	
	
	public void validate(String uname, String pass) {
		  // loginService is a GWT-RPC service that checks if the user is logged in
		  Nmap3.dbService.checkLoggedIn(uname, pass, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					GWT.log("Access granted.");
					System.out.println("Access granted.");
			    	startApp();
			      } else {
			    	  GWT.log("Access denied.");
			    	  System.out.println("Access denied.");
			    	  Window.Location.assign("/login");
			      }
			}
			
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Something goes wrong!");
			}
		});
	}	  
			  
			  			  		
	
	void startApp(){
	  	  (new Nmap3()).onModuleLoad();
	}
	
}
