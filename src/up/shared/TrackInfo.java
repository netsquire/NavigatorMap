package up.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TrackInfo  implements IsSerializable {

	public TrackInfo(){}
	
	int size;
	LatLongSerial[] lalo;
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public LatLongSerial[] getLalo() {
		return lalo;
	}
	public void setLalo(LatLongSerial[] lalo) {
		this.lalo = lalo;
	}

}
