package up.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class PopupPanelExample implements EntryPoint {

	  public static class MyPopup extends PopupPanel {

	    public MyPopup() {
	      super(true);
	      setWidget(new Label("Click outside of this popup to close it"));
	      System.out.println(" Start MyPopup() ");
	    }
	  }

	  public void onModuleLoad() {
		  System.out.println(" Start onModuleLoad() ");
	    Button b1 = new Button("Click me to show popup");
	    b1.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	    	  System.out.println(" Start MyPopup().show() ");
	    	  new MyPopup().show();
	      	}
	      });

	    RootPanel.get().add(b1);

	    Button b2 = new Button("Click me to show popup partway across the screen");

	    b2.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        final MyPopup popup = new MyPopup();
	        // Position the popup 1/3rd of the way down and across the screen, and
	        // show the popup. Since the position calculation is based on the
	        // offsetWidth and offsetHeight of the popup, you have to use the
	        // setPopupPositionAndShow(callback) method. The alternative would
	        // be to call show(), calculate the left and top positions, and
	        // call setPopupPosition(left, top). This would have the ugly side
	        // effect of the popup jumping from its original position to its
	        // new position.
	        popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
	          public void setPosition(int offsetWidth, int offsetHeight) {
	            int left = (Window.getClientWidth() - offsetWidth) / 3;
	            int top = (Window.getClientHeight() - offsetHeight) / 3;
	            popup.setPopupPosition(left, top);
	          }
	        });
	      }
	    });

	    RootPanel.get().add(b2);
	  }
	}