package up.client;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class CellListExample implements EntryPoint {

	/**
	   * The list of data to display.
	   */
	  private static final List<String> DAYS = Arrays.asList("Sunday", "Monday",
	      "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

	  public void onModuleLoad() {
	    // Create a cell that will interact with a value updater.
	    TextInputCell inputCell = new TextInputCell();

	    // Create a CellList that uses the cell.
	    CellList<String> cellList = new CellList<String>(inputCell);

	    // Create a value updater that will be called when the value in a cell
	    // changes.
	    ValueUpdater<String> valueUpdater = new ValueUpdater<String>() {
	      public void update(String newValue) {
	        Window.alert("You typed: " + newValue);
	      }
	    };

	    // Add the value updater to the cellList.
	    cellList.setValueUpdater(valueUpdater);

	    // Set the total row count. This isn't strictly necessary, but it affects
	    // paging calculations, so its good habit to keep the row count up to date.
	    cellList.setRowCount(DAYS.size(), true);

	    // Push the data into the widget.
	    cellList.setRowData(0, DAYS);

	    // Add it to the root panel.
	    RootPanel.get().add(cellList);
	  }
	}