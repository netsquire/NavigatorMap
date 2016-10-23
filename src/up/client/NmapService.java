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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greet")
public interface NmapService extends RemoteService {
	public int getNum();
	
	public List<String> getGroups();
	
	public List<PointBean> lastPoints(int rows);
	
	public PointBean lastPoint(long devId);
	
	public List<Device> getDevices();

	public MoveTrack getTodayTrack(Device dev);
	
	public LatLongSerial getLastPoint(String dId);
	
	public PointBean getLastPB(String dId);
	
	public List<MoveTrack> getTracks();

	public TrackInfo initMockTrack(String mtName, Device dev);
	
	public PointBean nextPoint(String mtName);
	
	public boolean checkLoggedIn(String user, String pass);

	public List<Driver> getDrivers();
	
	public boolean bindDriver(String dname, String carName);
	
	public int addNewDriver(String drName);
	
	public int storeGZ(GeoZone gz);
	
	public List<GeoZone> getGeoZones();
	
	public String getDriverByCarName(String carname);
	
	public Vehicle getCarByDriverName(String drname);
}
