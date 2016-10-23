package up.client;

import java.util.List;

import up.server.MockTrack;
import up.shared.Device;
import up.shared.Driver;
import up.shared.GeoZone;
import up.shared.LatLongSerial;
import up.shared.MoveTrack;
import up.shared.PointBean;
import up.shared.TrackInfo;
import up.shared.Vehicle;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NmapServiceAsync {
	void getNum(AsyncCallback<Integer> callback)
			throws IllegalArgumentException;

	void getGroups(                AsyncCallback<List<String>>    callback);
	
	void lastPoints(        int i, AsyncCallback<List<PointBean>> callback);

	void getTodayTrack(Device dev, AsyncCallback<MoveTrack>       callback);

	void getLastPoint(String dId,  AsyncCallback<LatLongSerial>       callback);

	void getTracks(                AsyncCallback<List<MoveTrack>> callback);

	void initMockTrack(String mtName, Device dev, AsyncCallback<TrackInfo> asyncCallback);

	void nextPoint(String mtName,  AsyncCallback<PointBean> 		callback);

	void checkLoggedIn(String user, String pass, AsyncCallback<Boolean> callback);

	void getDrivers(				AsyncCallback<List<Driver>> 	callback);
	
	void getDevices(                AsyncCallback<List<Device>>    	callback);

	void bindDriver(String dname, String carName, AsyncCallback<Boolean> callback);

	void addNewDriver(String drName, AsyncCallback<Integer> callback);

	void storeGZ(GeoZone gz, 		AsyncCallback<Integer> callback);

	void getGeoZones(				AsyncCallback<List<GeoZone>> callback);

	void getDriverByCarName(String carname, AsyncCallback<String> callback);

	void getLastPB(String dId, 		AsyncCallback<PointBean> callback);

	void lastPoint(long devId,    AsyncCallback<PointBean> callback);

	void getCarByDriverName(String drname, AsyncCallback<Vehicle> callback);
	
}
