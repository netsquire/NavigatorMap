package up.shared;

import java.util.LinkedList;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.rpc.IsSerializable;

public class MoveTrack implements IsSerializable {

	//private String deviceId;
	//private String deviceName;
	private Device dev;
	private List<PointBean> path = new LinkedList<PointBean>();
	private List<PointBean> kalmanPath = new LinkedList<PointBean>();
	private List<PointBean> trimPath = new LinkedList<PointBean>();
	private PathLegend pathLegend = new PathLegend();
	
	private double maxLat = 0;
	private double minLat = 0;
	private double maxLng = 0;
	private double minLng = 0;
	
	private LatLongSerial center = null;
	private int       isReady = 0;
	private boolean  isLoaded = false;
	private boolean    isOpen = false;
	private boolean dbFetched = false;
	private int     numPoints = 0;	
	private String     status = "nil";
	
	public MoveTrack() {}

	/*
	public MoveTrack newInstance(MoveTrack mt){		
		setCenter(mt.getCenter());
		setPath(mt.getPath());
		setNumPoints(mt.getPath().size());		
		setReady(200);		
		setLoaded(true);
		setStatus("init_ok");
		GWT.log(status);
		GWT.log(" Exactly placed points: " + this.getNumPoints());		
		return this;
		}
	*/
	
	public PointBean nearest(double latitude, double longitude){
		double len = 0;
		double min = 10e25;
		int ind = 0, j = 0;
		for (PointBean pb: getPath()) {
			len = Math.sqrt( Math.pow(latitude - pb.getLatitude(),2) + Math.pow(longitude - pb.getLongitude(),2));
			if (min > len) {
				ind = j;
				min = len;
				}			
			j++;
			}
		return getPath().get(ind);
		}
	
	public Device getDev() {
		return dev;
	}

	public void setDev(Device dev) {
		this.dev = dev;
	}

	public PathLegend getPathLegend() {
		return pathLegend;
	}

	public void setPathLegend(PathLegend pathLegend) {
		this.pathLegend = pathLegend;
	}

	/*
	public String getDeviceName() {
		return deviceName;
		}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
		}
	*/
	public LatLng[] getLaLoPath(){
		return getLaLoSinglePath(path);
		}
		
	public LatLng[] getLatLongKalmanPath(){		
		return getLaLoSinglePath(kalmanPath);
		}
	
	public LatLng[] getLaLoTrimmedPath(){
		return getLaLoSinglePath(trimPath);
		}
	
	public LatLng[] getLaLoSinglePath(List<PointBean> inPath){
		int len = getPath().size();
		int i = 0;
		LatLng[] latlong = new LatLng[len];
		for (PointBean pb: inPath){
			try {
				latlong[i++] = LatLng.newInstance(pb.getLatitude(), pb.getLongitude());
			} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				System.out.println(" AIOBE: " + e.getMessage());
				System.out.println(" Index = " + i + " MAX = " + getNumPoints());
				e.printStackTrace();
			}
			}
		return latlong;
		}
	
	// Getters & setters
	
	/*
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	*/
	public int getIsReady() {
		return isReady;
	}

	public void setIsReady(int isReady) {
		this.isReady = isReady;
	}

	public List<PointBean> getTrimPath() {
		return trimPath;
	}

	public void setTrimPath(List<PointBean> trimPath) {
		this.trimPath = trimPath;
	}

	public List<PointBean> getKalmanPath() {
		return kalmanPath;
	}

	public void setKalmanPath(List<PointBean> kalmanPath) {
		this.kalmanPath = kalmanPath;
	}

	public String getStatus() {
		return status;
	}

	// potentially long operation - think about changing
	public void setStatus(String status) {
		this.status = getStatus() + ":" + status;
	}

/*
	public void setTrack(LatLongSerial[] track) {
		this.track = track;
	}

	public LatLongSerial[] getTrack() {
		return track;
	}
*/
	public void setCenter(LatLongSerial center) {
		this.center = center;
	}

	public boolean isDbFetched() {
		return dbFetched;
	}

	public void setDbFetched(boolean dbFetched) {
		this.dbFetched = dbFetched;
	}

	public int getNumPoints() {
		return numPoints;
	}

	public void setNumPoints(int numPoints) {
		this.numPoints = numPoints;
	}

	public boolean isLoaded() {
		return isLoaded;
	}

	public void setLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	public boolean isOpen() {
		return isOpen;
	}
	public int isReady() {
		return isReady;
	}

	public void setReady(int isReady) {
		this.isReady = isReady;
	}

	public LatLongSerial getCenter() {
		return center;
	}
	
	public List<PointBean> getPath(){
		return this.path;
	}
	
	public void setPath(List<PointBean> in){
		this.path = in;
	}

	public double getMaxLat() {
		return maxLat;
	}

	public void setMaxLat(double maxLat) {
		this.maxLat = maxLat;
	}

	public double getMinLat() {
		return minLat;
	}

	public void setMinLat(double minLat) {
		this.minLat = minLat;
	}

	public double getMaxLng() {
		return maxLng;
	}

	public void setMaxLng(double maxLng) {
		this.maxLng = maxLng;
	}

	public double getMinLng() {
		return minLng;
	}

	public void setMinLng(double minLng) {
		this.minLng = minLng;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public double getCenterLat(){
		double maxLat = getMaxLat();
		double minLat = getMinLat();
		double aveLat = (maxLat + minLat)/2;
		//System.out.println(" Latitude: " + maxLat + " / " + minLat + " = " + aveLat );
		return aveLat;
	}

	public double getCenterLong(){
		double maxLong = getMaxLng();
		double minLong = getMinLng();
		double aveLong = (maxLng + minLng)/2;
		//System.out.println(" Longitude: " + maxLong + " / " + minLong + " = " + aveLong );
		return aveLong;
	}
	
	
	public void animate() {
		// TODO Auto-generated method stub
		
	}


	
	/*
	private LatLng[] path = {
		     LatLng.newInstance(55.665580469670985 , 36.53949737548828)
			};
	
	
		     LatLng.newInstance(55.658802424711446 , 36.53984069824219),
		     LatLng.newInstance(55.652991737612965 , 36.54327392578125),
		     LatLng.newInstance(55.64679272072754 , 36.54430389404297),
		     LatLng.newInstance(55.64020518996826 , 36.541900634765625),
		     LatLng.newInstance(55.63632965430247 , 36.540870666503906),
		     LatLng.newInstance(55.62896508022178 , 36.53709411621094),
		     LatLng.newInstance(55.62411921078643 , 36.529541015625),
		     LatLng.newInstance(55.621017539900606 , 36.5130615234375),
		     LatLng.newInstance(55.61888499879606 , 36.508941650390625),
		     LatLng.newInstance(55.61539513584262 , 36.50585174560547),
		     LatLng.newInstance(55.610159758935964 , 36.49829864501953),
		     LatLng.newInstance(55.603953962610454 , 36.49005889892578),
		     LatLng.newInstance(55.60337211886132 , 36.481475830078125),
		     LatLng.newInstance(55.60026880643609 , 36.47083282470703),
		     LatLng.newInstance(55.601044657557956 , 36.45195007324219),
		     LatLng.newInstance(55.60298421823381 , 36.438560485839844),
		     LatLng.newInstance(55.604535797729035 , 36.42860412597656),
		     LatLng.newInstance(55.60783270038267 , 36.42139434814453),
		     LatLng.newInstance(55.611711054594984 , 36.42345428466797),
		     LatLng.newInstance(55.61558902526748 , 36.43341064453125),
		     LatLng.newInstance(55.61946661242041 , 36.43684387207031),
		     LatLng.newInstance(55.625476114623986 , 36.438560485839844),
		     LatLng.newInstance(55.625088432606624 , 36.447486877441406),
		     LatLng.newInstance(55.62489459015995 , 36.455726623535156),
		     LatLng.newInstance(55.62314996499902 , 36.46705627441406),
		     LatLng.newInstance(55.62276225997315 , 36.47735595703125),
		     LatLng.newInstance(55.62121140152132 , 36.484222412109375),
		     LatLng.newInstance(55.62431305706783 , 36.49040222167969),
		     LatLng.newInstance(55.62431305706783 , 36.501731872558594),
		     LatLng.newInstance(55.61946661242041 , 36.507225036621094),
		     LatLng.newInstance(55.61539513584262 , 36.504478454589844),
		     LatLng.newInstance(55.611323236433364 , 36.49864196777344),
		     LatLng.newInstance(55.60686305189916 , 36.49314880371094),
		     LatLng.newInstance(55.601820493336014 , 36.48456573486328),
		     LatLng.newInstance(55.60085069621599 , 36.474952697753906),
		     LatLng.newInstance(55.600656733915 , 36.466026306152344),
		     LatLng.newInstance(55.60143257736496 , 36.449546813964844),
		     LatLng.newInstance(55.60240236010027 , 36.43890380859375),
		     LatLng.newInstance(55.604923683013354 , 36.42585754394531),
		     LatLng.newInstance(55.60763877260378 , 36.418304443359375),
		     LatLng.newInstance(55.609965842663705 , 36.40663146972656),
		     LatLng.newInstance(55.609965842663705 , 36.39564514160156),
		     LatLng.newInstance(55.610159758935964 , 36.384315490722656),
		     LatLng.newInstance(55.61209886892118 , 36.374359130859375),
		     LatLng.newInstance(55.61481346181522 , 36.368865966796875),
		     LatLng.newInstance(55.616558458009905 , 36.361656188964844),
		     LatLng.newInstance(55.619272742171034 , 36.353759765625),
		     LatLng.newInstance(55.62082367732115 , 36.34757995605469),
		     LatLng.newInstance(55.62392536354633 , 36.34174346923828),
		     LatLng.newInstance(55.629352723893206 , 36.334877014160156),
		     LatLng.newInstance(55.635360710478764 , 36.32972717285156),
		     LatLng.newInstance(55.637686135397544 , 36.32972717285156),
		     LatLng.newInstance(55.642724082556086 , 36.32560729980469),
		     LatLng.newInstance(55.64466158201747 , 36.32354736328125),
		     LatLng.newInstance(55.645436554966125 , 36.32526397705078),
		     LatLng.newInstance(55.64756765150652 , 36.32354736328125),
		     LatLng.newInstance(55.64911746706226 , 36.32354736328125),
		     LatLng.newInstance(55.65318544107807 , 36.32354736328125),
		     LatLng.newInstance(55.65454133850118 , 36.327667236328125),
		     LatLng.newInstance(55.65764035628693 , 36.327667236328125),
		     LatLng.newInstance(55.66015812760702 , 36.32698059082031),
		     LatLng.newInstance(55.66286939254092 , 36.32457733154297),
		     LatLng.newInstance(55.66577411085124 , 36.32457733154297),
		     LatLng.newInstance(55.66771046995609 , 36.329383850097656),
		     LatLng.newInstance(55.66887223942912 , 36.334190368652344),
		     LatLng.newInstance(55.670227593553925 , 36.338653564453125),
		     LatLng.newInstance(55.671389288295465 , 36.34105682373047),
		     LatLng.newInstance(55.670614828966755 , 36.3372802734375),
		     LatLng.newInstance(55.6682913590041 , 36.33247375488281),
		     LatLng.newInstance(55.66693593781173 , 36.32801055908203),
		     LatLng.newInstance(55.66538682753259 , 36.323890686035156),
		     LatLng.newInstance(55.66132012128982 , 36.32251739501953),
		     LatLng.newInstance(55.6582213948113 , 36.32011413574219),
		     LatLng.newInstance(55.65318544107807 , 36.323890686035156),
		     LatLng.newInstance(55.65027978847778 , 36.327667236328125),
		     LatLng.newInstance(55.64485532669226 , 36.32801055908203),
		     LatLng.newInstance(55.642724082556086 , 36.32698059082031),
		     LatLng.newInstance(55.63807369279805 , 36.32835388183594),
		     LatLng.newInstance(55.63361655120771 , 36.331443786621094),
		     LatLng.newInstance(55.63032181629597 , 36.33384704589844),
		     LatLng.newInstance(55.62799595426723 , 36.33831024169922),
		     LatLng.newInstance(55.624700746754606 , 36.342430114746094),
		     LatLng.newInstance(55.62237455111249 , 36.344146728515625),
		     LatLng.newInstance(55.62218069524408 , 36.35032653808594),
		     LatLng.newInstance(55.619660481711016 , 36.358909606933594),
		     LatLng.newInstance(55.616558458009905 , 36.362342834472656),
		     LatLng.newInstance(55.61442567433621 , 36.368865966796875),
		     LatLng.newInstance(55.612680583219166 , 36.379852294921875),
		     LatLng.newInstance(55.60841447796599 , 36.389122009277344),
		     LatLng.newInstance(55.60822055306379 , 36.40113830566406),
		     LatLng.newInstance(55.609771925432575 , 36.41040802001953),
		     LatLng.newInstance(55.60957800724254 , 36.417274475097656),
		     LatLng.newInstance(55.60725091416929 , 36.4251708984375),
		     LatLng.newInstance(55.60356606773666 , 36.433753967285156),
		     LatLng.newInstance(55.60279026648159 , 36.44542694091797),
		     LatLng.newInstance(55.60162653582997 , 36.45641326904297),
		     LatLng.newInstance(55.600656733915 , 36.46465301513672),
		     LatLng.newInstance(55.600656733915 , 36.475982666015625),
		     LatLng.newInstance(55.60240236010027 , 36.485595703125),
		     LatLng.newInstance(55.598911030051326 , 36.48216247558594),
		     LatLng.newInstance(55.59483741894291 , 36.482505798339844),
		     LatLng.newInstance(55.58979331443427 , 36.485252380371094),
		     LatLng.newInstance(55.58630086226771 , 36.48902893066406),
		     LatLng.newInstance(55.58513664248536 , 36.49314880371094)
		  };
			*/
}
