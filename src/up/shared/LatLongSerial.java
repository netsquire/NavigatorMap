package up.shared;

import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.rpc.IsSerializable;

public class LatLongSerial  implements IsSerializable  {
	
	double latitude;
	double longitude;
	
	LatLongSerial(){}

	public LatLongSerial(LatLng point) {
		this.latitude = point.getLatitude();
		this.longitude = point.getLongitude();
	}
	
	public LatLongSerial(double latitude, double longitude) {
		this();
		this.latitude  = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/*
	public LatLng  asLatLng(){
		return LatLng.newInstance(latitude, longitude);		
	}
	*/
	
}
