package up.shared;

import java.util.LinkedList;
import java.util.List;

import up.client.Nmap3;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

public class GeoZone implements IsSerializable {

	String name;
	int id;
	List<LatLongSerial> body = new LinkedList<LatLongSerial>();
	
	public GeoZone(){}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<LatLongSerial> getBody() {
		return body;
	}

	public void setBody(List<LatLongSerial> body) {
		this.body = body;
	}
}
