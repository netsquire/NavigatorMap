package up.shared;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PathLegend  implements IsSerializable {

	public PathLegend(){}
	
	private List<Note> notes = new LinkedList<Note>();

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}
	
}
