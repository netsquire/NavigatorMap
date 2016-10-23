package up.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Login {

	  public Widget show() {
	    
		Widget w = new Auth().show();
		DockLayoutPanel p = new DockLayoutPanel(Unit.PCT);   
		
	    p.addNorth(new HTML(""), 40);
	    p.addWest(new HTML(""), 40);
	    p.addEast(new HTML(""), 40);
	    p.addSouth(new HTML(""), 40);
	    p.add( w );
	    
	    //GWT.log("Auth form is set.");
		//System.out.println("Auth form is set.");
	    /*
	    
	    HorizontalPanel panel = new HorizontalPanel();
	    panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_JUSTIFY);	    
	    p.add(w);
	    */
	    
	    return p;
	  }
	}