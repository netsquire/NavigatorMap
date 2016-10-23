package up.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class Main implements EntryPoint {

	RootLayoutPanel rp = RootLayoutPanel.get();
	
	@Override
	public void onModuleLoad() {
		//new Nmap3().onModuleLoad();
		//new PopupPanelExample().onModuleLoad();
		//new Pane().onModuleLoad();
		Widget w = (new Login()).show();
		rp.add(w);
		//new CellTreeExample().onModuleLoad();
		//new CellListExample().onModuleLoad();
		//new KeyProviderExample().onModuleLoad();
		
		//new VehicleList().ListLoad();
	}

}
