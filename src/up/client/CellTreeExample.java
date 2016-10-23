package up.client;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

public class CellTreeExample implements EntryPoint {

	  /**
	   * The model that defines the nodes in the tree.
	   */
	  private static class CustomTreeModel implements TreeViewModel {

	    /**
	     * Get the {@link NodeInfo} that provides the children of the specified
	     * value.
	     */
	    public <T> NodeInfo<?> getNodeInfo(T value) {
	      /*
	       * Create some data in a data provider. Use the parent value as a prefix
	       * for the next level.
	       */
	      ListDataProvider<String> dataProvider = new ListDataProvider<String>();
	      for (int i = 0; i < 2; i++) {
	        dataProvider.getList().add(value + "." + String.valueOf(i));
	      }

	      // Return a node info that pairs the data with a cell.
	      return new DefaultNodeInfo<String>(dataProvider, new TextCell());
	    }

	    /**
	     * Check if the specified value represents a leaf node. Leaf nodes cannot be
	     * opened.
	     */
	    public boolean isLeaf(Object value) {
	      return value.toString().length() > 10;
	    }
	  }

	  public void onModuleLoad() {
	    TreeViewModel model = new CustomTreeModel();
	    CellTree tree = new CellTree(model, "Item 1");
	    RootLayoutPanel.get().add(tree);
	  }
	}
