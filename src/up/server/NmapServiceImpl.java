package up.server;

import static java.lang.Math.pow;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import up.client.NmapService;
import up.client.TrackView;
import up.shared.Device;
import up.shared.Driver;
import up.shared.GeoZone;
import up.shared.LatLongSerial;
import up.shared.MoveTrack;
import up.shared.PathLegend;
import up.shared.PointBean;
import up.shared.TrackInfo;
import up.shared.Vehicle;
import Jama.Matrix;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class NmapServiceImpl 
		extends RemoteServiceServlet 
		implements NmapService {
	
	// ---
	// Globs -> Properties
	//double distLimit = 7.040251402564411E-5;
	int pointLimit = 200;
	static Map<String, MockTrack>    moveTracks = new HashMap<String, MockTrack>();
	static Connection  conn = getConn();
	//static ResultSet rs = null;
	// ---

	
//	select car_id from drivers,BINDED_DRIVER where BINDED_DRIVER.driver_id = drivers.driver_id and drivers.name = 'Nikifor'";
	
	public Vehicle getCarByDriverName(String drname){
		String sql = " select vehicles.car_id, vehicles.name from drivers,BINDED_DRIVER,vehicles " 
							+ " where BINDED_DRIVER.driver_id = drivers.driver_id " 
							+ " and drivers.name = '" + drname + "'";
		Vehicle res = null;
		ResultSet rs = null;
		try {
		rs = conn.prepareStatement(sql).executeQuery();	
		while(rs.next()){	
			res = new Vehicle(rs.getDouble(1), rs.getString(2));
			}
		rs.close();
		} catch (SQLException e) {
			System.out.println("(getCarByDriverName) SQL Exception.");
			e.printStackTrace();
		}		
		return res;
	}

	
	
	public String getDriverByCarName(String carname){
		String sql = " select DRIVERS.NAME, VEHICLES.CAR_ID from VEHICLES,BINDED_DRIVER,DRIVERS " 
							+ "where VEHICLES.name='" + carname + "' and BINDED_DRIVER.CAR_ID = VEHICLES.CAR_ID " 
							+ " and BINDED_DRIVER.DRIVER_ID = DRIVERS.DRIVER_ID";
		String res = null;
		ResultSet rs = null;
		try {
		rs = conn.prepareStatement(sql).executeQuery();	
		while(rs.next()){	
			res = rs.getString(1);
			}
		rs.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception.");
			e.printStackTrace();
		}		
		return res;
	}
	
	public List<GeoZone> getGeoZones(){
		List<GeoZone> lgz = new LinkedList<GeoZone>();		
		try {
			String sql_gzids = "select gzid, count(gzid) from GZ_POINTS group by gzid having count(gzid)>2";
			ResultSet rs = conn.prepareStatement(sql_gzids).executeQuery();	
			while(rs.next()){		
				GeoZone gz = new GeoZone();
				int gzid = rs.getInt(1);	
				gz.setId(gzid);
				
				String sql_gz_p = "select latitude, longitude from GZ_POINTS where gzid = " + gzid;
				ResultSet rs2 = conn.prepareStatement(sql_gz_p).executeQuery();	
				while(rs2.next()){	
					gz.getBody().add(new LatLongSerial(rs2.getDouble(1), rs2.getDouble(2)));
					}
				
				String sql_gz_name = "select name from GEOZONES where gzid = " + gzid;
				ResultSet rs3 = conn.prepareStatement(sql_gz_name).executeQuery();
				while(rs3.next()){	
					gz.setName(rs3.getString(1));
					}
				lgz.add(gz);
				}		
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception.");
			e.printStackTrace();
		}		
		return lgz;
	}
	
	public int storeGZ(GeoZone gz){
		int gzid = 0;
		boolean rs = false;	
		int cnt = 0;
		try {
			String sql_gz_id = "select MAX(gzid) from geozones";		
			ResultSet ret = conn.prepareStatement(sql_gz_id).executeQuery();
			while (ret.next()) {
				gzid = ret.getInt(1);
				}
			System.out.println("Max gzId (add GeoZone): " + gzid);
			gzid++;
			
			String sql_gz_name = "insert into geozones (gzid, name) values (" + gzid + ", '" + gz.getName() + "')";
			System.out.println("SQL (add GeoZone): " + sql_gz_name);
			rs = conn.prepareStatement(sql_gz_name).execute();	
			
			String sql_gz_points = "insert into gz_points (gzid, latitude, longitude) values (?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql_gz_points);
			for (LatLongSerial  point: gz.getBody()) {
				ps.setInt(1, gzid);
				ps.setDouble(2, point.getLatitude());
				ps.setDouble(3, point.getLongitude());
				rs = ps.execute(); 
				cnt++;	
				}
			ret.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception.");
			e.printStackTrace();
		}		
		return cnt;		
	}
	
	public int addNewDriver(String drName){
		//Connection conn = getConn();				
		String sql = "select MAX(driver_id) from DRIVERS";
		int id = 0;
		boolean rs = false;
		try {			
			ResultSet ret = conn.prepareStatement(sql).executeQuery();
			while (ret.next()) {
				id = ret.getInt(1);
				}
			System.out.println("Max Id (add New Driver): " + id);
			id++;
			sql = " insert into DRIVERS (NAME, DRIVER_ID) values ('" + drName + "', " + id + ")";
			System.out.println("SQL (add New Driver): " + sql);
			rs = conn.prepareStatement(sql).execute();	
			
			sql = " insert into BINDED_DRIVER (DRIVER_ID) values (" + id + ")";
			System.out.println("SQL (add New Driver): " + sql);
			rs = conn.prepareStatement(sql).execute();	
			
			ret.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception.");
			e.printStackTrace();
		}		
		//if(rs)
			return id;
		//else return 0;
	}
	
	public boolean bindDriver(String dname, String carName){
		//Connection conn = getConn();		
		int car_id = 0;
		int driver_id = 0;
		boolean rs = false;
		
		try {
			String sql_car_id = "select car_id from vehicles where NAME = '" + carName + "'";
			//System.out.println("SQL ( get car_id): " + sql_car_id);
			ResultSet ret = conn.prepareStatement(sql_car_id).executeQuery();	
			while (ret.next()) {
				car_id = ret.getInt(1);
				}
			
			String sql_driver_id = "select driver_id from drivers where NAME = '" + dname + "'";
			//System.out.println("SQL ( get driver_id): " + sql_driver_id);
			ret = conn.prepareStatement(sql_driver_id).executeQuery();	
			while (ret.next()) {
				driver_id = ret.getInt(1);
				}
			
			String sql_unbind = "update BINDED_DRIVER set car_id = NULL where car_id = " + car_id;
			//System.out.println("SQL ( unbind driver_id of previous car_id ): " + sql_unbind);
			rs = conn.prepareStatement(sql_unbind).execute();
			
			String sql_bind = "update BINDED_DRIVER set car_id = " + car_id + " where driver_id = " + driver_id;
			//System.out.println("SQL ( bind driver_id to car_id ): " + sql_bind);
			rs = conn.prepareStatement(sql_bind).execute();
		
			ret.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception.");
			e.printStackTrace();
		}		
		return rs;
	}
	
	public List<Driver> getDrivers() {
		//Connection conn = getConn();		
		String sql = " select name, driver_id from DRIVERS ";
		//System.out.println("SQL (getDrivers): " + sql);
		ResultSet rs = null;
		List<Driver> rc = new ArrayList<Driver>();
		try {
			rs = conn.prepareStatement(sql).executeQuery();	
			while(rs.next()){			
				rc.add(	new Driver(	rs.getString(1)));	
				//System.out.println("  -- Driver Name: " + rs.getString(1));
				}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception.");
			e.printStackTrace();
		}		
		return rc;
	}

	
	
	public boolean checkLoggedIn(String user, String pass) {
		//System.out.println("Access granted");
		return true;
	}
	
	public TrackInfo initMockTrack(String mtName, Device dev){		
		MockTrack mt = new MockTrack(mtName, getTodayTrack(dev));
		moveTracks.put(mtName, mt);		
		String status = mt.getStatus();
		System.out.println("Mock Track status: " + status);
		TrackInfo ti = new TrackInfo();
		ti.setSize(mt.getPath().size());
		PointBean pb = mt.getPath().get(0);
		LatLongSerial[] lls = {new LatLongSerial(pb.getLatitude(), pb.getLongitude())};
		ti.setLalo( lls );
		return ti;
	}
	
	public PointBean nextPoint(String mtName){		
		return moveTracks.get(mtName).nextPoint();
		}
	
	static Connection getConn()
	{	
		Connection conn = null;		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e1) {
			System.out.println("Class Not Found Exception.");
			e1.printStackTrace();
		}
		try {
			//conn = DriverManager.getConnection("jdbc:oracle:thin:geo/geo@localhost:1522/orcl","geo","geo");
			conn = DriverManager.getConnection("jdbc:oracle:thin:geo/geo@195.210.135.24:1522/orcl","geo","geo");
			//System.out.println(" connection good");
		} catch (SQLException e) {
			System.out.println("SQL Exception.");
			System.out.println(" connection failed");
			e.printStackTrace();
		} 	 		
		return conn;
	}

	
	
	public List<MoveTrack> getTracks(){
		List<MoveTrack> tracks = new LinkedList<MoveTrack>();
		List<Device> devList = getDevices();		
		for (Device d: devList) {
			MoveTrack dt = getTodayTrack(d);
			//dt.setDeviceName(d.getName());
			dt.setDev(d);
			int len = dt.getPath().size();
			System.out.println("Length of MoveTrack: " + len + " (" + d.getName() + ")");
			//if (len > 0) 
				tracks.add(dt);
			}
		return tracks;
	}

	public MoveTrack getTodayTrack(Device dev) {
		//Connection conn = getConn();
		String devName = dev.getName();
		String sql = "SELECT DEVICE_ID, HW_CLOCK, LATITUDE, LONGITUDE, ALTITUDE, ANGLE, SPEED, SATELLITES"
			+ " IGNITION, DOORLOCK, MOVEMENT, PDOP, HDOP"
			+ " FROM tracks WHERE DEVICE_ID = " 
			+ dev.getDeviceId() 
			+ " AND HW_CLOCK > sysdate-1 "
			+ " ORDER BY HW_CLOCK";		
		//System.out.println(" getTodayTrack() SQL: " + sql);
		
		MoveTrack mt = new MoveTrack();
		mt.setDev(dev);
		ResultSet rs = null;
		List<PointBean>       path = new LinkedList<PointBean>();
		mt.setPathLegend(new PathLegend());

		double maxLat =  200;
		double minLat = -200;
		double maxLng =  200;
		double minLng = -200;		
		boolean succesful = false;
		LatLongSerial center = null;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			try {
				rs = ps.executeQuery();
			} catch (SQLSyntaxErrorException e) {
				System.out.println(" SQLSyntaxErrorException: " + e.getErrorCode());
				System.out.println("  message: " + e.getMessage());
				System.out.println(" SQLState: " + e.getSQLState());
				System.out.println(" getTodayTrack() -> SQL: " + sql);
				e.printStackTrace();
			}					
			
			while(rs.next()){				
				BigDecimal deviceId = rs.getBigDecimal(1);
				Date    hwClock   =  rs.getDate(2);
				double  latitude  =  rs.getDouble(3);
				double  longitude =  rs.getDouble(4);
				double  altitude  =  rs.getDouble(5);
				double     angle  =  rs.getDouble(6);
				double     speed  =  rs.getDouble(7);
				int   satellites  =  rs.getInt(8);
				boolean    ignition =  rs.getBoolean(9);
				boolean    doorlock =  rs.getBoolean(10);
				boolean    movement =  rs.getBoolean(11);
				short        pdop =  rs.getShort(12);
				short        hdop =  rs.getShort("HDOP");
				path.add( new PointBean(deviceId,  hwClock,    latitude,
										longitude, altitude,   angle, 
										speed,     satellites, ignition, 
										doorlock,  movement,   pdop,       hdop));
				succesful = true;
				if (maxLat > latitude)	maxLat = latitude;
				if (minLat < latitude)	minLat = latitude;
				if (maxLng > longitude)	maxLng = longitude;
				if (minLng < longitude)	minLng = longitude;
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception.");
			e.printStackTrace();
		}
		System.out.println("Num of raw points: " + path.size());
		if (succesful) {
			mt.setMaxLat(maxLat);
			mt.setMaxLng(maxLng);
			mt.setMinLat(minLat);
			mt.setMinLng(minLng);
			int devType = dev.getDeviceType();
			//System.out.println("Device type: " + devType + "(" + devName + ")");
			switch (devType) {				
				case 0: mt.setPath( path ); break;			
				//case 1:	mt.setPath( trimDist(  path, 7.040251402564411E-5 )); break;
				case 1:	mt.setPath( trimDist( path, 7.040251402564411E-6 )); break;
				//mt.setPath(  trimDist( kalmanize(path), 7.040251402564411E-5 ));
				//mt.setPath(path);
				//mt.setKalmanPath(kalmanize( path ));
				//mt.setTrimPath( trimDist( path, 7.040251402564411E-5 ));
				default: mt.setPath( path );
				}
			
			mt.getDev().setDeviceId(dev.getDeviceId());
			mt.getDev().setName(devName);
			double  cLat = (maxLat + minLat) / 2;
			double cLong = (maxLng + minLng) / 2;
			center = new LatLongSerial(cLat, cLong);
			mt.setCenter( center );
			mt.setStatus("db_fetched");
		}			
		return mt;
	}
	
	
	List<PointBean> trimDist (List<PointBean> inPath, double distLimit){
		List<PointBean> outPath = new LinkedList<PointBean>();
		outPath.add(inPath.get(0));
		double prevLat = inPath.get(0).getLatitude();
		double prevLong = inPath.get(0).getLongitude();
	
		for (PointBean pb : inPath) {
			if (geoDistance(pb.getLatitude(), pb.getLongitude(), prevLat, prevLong) > distLimit) {
				outPath.add(pb);
				prevLat = pb.getLatitude();
				prevLong = pb.getLongitude();	
				}							
			}
		System.out.println("<trimDist>    input points: "  + inPath.size() 
								   + "    output points: " + outPath.size());
		return outPath;
	}
	
	List<PointBean> kalmanize (List<PointBean> inPath) {
		System.out.println("Starting kalmanize: " + inPath.size() + " points.");
		List<PointBean> outPath = new LinkedList<PointBean>();
		double startLat = inPath.get(0).getLatitude();
		double startLng = inPath.get(0).getLongitude();
		double dt = 50d;
		double processNoiseStdev = 3;
		double measurementNoiseStdev = 5000;
		KalmanFilter KF;
		double kLat = -1000, kLong = -1000;
		KF = KalmanFilter.buildKF(0, 0, 
								  dt, 
								  pow(processNoiseStdev, 2) / 2,
								  pow(measurementNoiseStdev, 2));
		KF.setX(new Matrix(new double[][] { { startLat }, { 0 }, { startLng }, { 0 } }));

		for (PointBean pb : inPath) {			
				KF.predict();
				KF.correct(new Matrix(new double[][] { 
										{ pb.getLatitude() }, 
										{ pb.getLongitude() } 
										}));
				kLat  = (double) KF.getX().get(0, 0);
				kLong = (double) KF.getX().get(1, 0);
				/*
				System.out.println(" FROM: (" 
					+ pb.getLatitude() + " : " + pb.getLongitude() + ")"
					+ ") kalmanized TO: ("										
					+  kLat + " : " +  kLong + ")");
				*/
				outPath.add(
						new PointBean(pb.getDeviceId(), pb.getHwClock(),
									  kLat, kLong, 
									  pb.getAltitude(), pb.getAngle(), 
									  pb.getSpeed(),    pb.getSatellites(),
									  pb.getIgnition(), pb.getDoorlock(), 
									  pb.getMovement(), pb.getPdop(), pb.getHdop()));									
				}
		System.out.println("<kalmanize>    input points: " +   inPath.size() 
									+ "    output points: " + outPath.size());
		return outPath;
	}
	
	
	
	private double geoDistance(double lat1, double lon1, double lat2, double lon2) {
		  double theta = lon1 - lon2;
		  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		  dist = Math.acos(dist);
		  dist = rad2deg(dist);
		  dist = dist * 60 * 1.1515;		  
		  dist = dist * 0.001609344;
		  
		  return (dist);
		}

	
		private double deg2rad(double deg) {
			/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
			/*::  This function converts decimal degrees to radians             :*/
			/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
		  return (deg * Math.PI / 180.0);
		}

		
		private double rad2deg(double rad) {
			/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
			/*::  This function converts radians to decimal degrees             :*/
			/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
		  return (rad * 180.0 / Math.PI);
		}


	
	
	public int pingDB() throws NullPointerException {
		//Connection conn = getConn();
		String sql = "select COUNT(*) from tracks";
		int rc = 0;
		try {
			rc = conn.prepareStatement(sql).executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL Exception.");
			e.printStackTrace();
		}
		return rc;
	}
	
	public int getNum() throws NullPointerException {
		//Connection conn = getConn();
		String sql = "select COUNT(*) from tracks";
		ResultSet rs = null;
		int rc = 0;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();	
			rs.next();
			rc = rs.getInt(1);
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception.");
			e.printStackTrace();
		}
		return rc;
	}

	
	@Override
	public List<String> getGroups() {
		//Connection conn = getConn();
		int rows = 5;
		//String sql = "select description from DEVICE_GROUPS";
		String sql = "select DEVICE_ID, to_char(HW_CLOCK, 'DD-MON-YYYY HH:MI:SS')," 
						+ " LONGITUDE,LATITUDE,ALTITUDE,ANGLE,SPEED,SATELLITES, " 
						+ " IGNITION, DOORLOCK, MOVEMENT, PDOP, HDOP"
						+ " to_char(UNI_TIME, 'DD-MON-YYYY HH:MI:SS') " 
						+ " from ( select * from tracks order by HW_CLOCK desc) " 
						+ "where rownum < " + (rows+1) + ";";
		ResultSet rs = null;
		List<String> rc = new ArrayList<String>();
		try {
			rs = conn.prepareStatement(sql).executeQuery();	
		while(rs.next()){
			rc.add(rs.getString(1));
		}
		rs.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception.");
			e.printStackTrace();
		}
		return rc;
	}
	
	public LatLongSerial getLastPoint(String dId) {
		//Connection conn = getConn();
		String sql = "select LATITUDE, LONGITUDE from LAST_VALUES where DEVICE_ID = " + dId + " ";
		PreparedStatement ps = null;
		LatLongSerial pb = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();	
		while(rs.next()){
			pb =	new LatLongSerial(  rs.getDouble(1), rs.getDouble(2));
			System.out.println("getLastPoint(serverSide): " + rs.getDouble(1) + " : " + rs.getDouble(2));
					}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e.getErrorCode());
			System.out.println("SQLE: " + e.getSQLState());
			System.out.println("SQLE: " + e.getMessage());
			System.out.println("SQL: " + sql);
			e.printStackTrace();
		}
		return pb;
	}

	/*
	static PointBean pb = null;
	static PreparedStatement ps = null;
	static String sql;
	*/
	public PointBean getLastPB(String dId) {
		//Connection conn = getConn();
		String sql = "select DEVICE_ID, HW_CLOCK, LATITUDE, LONGITUDE,ALTITUDE,ANGLE,SPEED,SATELLITES " 
			// + " ,IGNITION, DOORLOCK, MOVEMENT, PDOP, HDOP "
			+ " from LAST_VALUES where DEVICE_ID = " + dId + " ";
		PreparedStatement ps = null;
		PointBean pb = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();	
		while(rs.next()){
			pb =	new PointBean(
					  rs.getBigDecimal(1), 
					  rs.getDate(2), 
					  rs.getDouble(3),
					  rs.getDouble(4),
					  rs.getDouble(5), 
					  rs.getDouble(6), 
					  rs.getDouble(7),
					  rs.getInt(8)
					  );
					/*
					  rs.getBoolean(9),
					  rs.getBoolean(10),
					  rs.getBoolean(11),
					  rs.getByte(12),
					  rs.getByte(13));
					*/
			//System.out.println("getLastPoint(serverSide) Date: " + rs.getDate(2));
					}
		rs.close();
		ps.close();	
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e.getErrorCode());
			System.out.println("SQLE: " + e.getSQLState());
			System.out.println("SQLE: " + e.getMessage());
			System.out.println("SQL: " + sql);
			e.printStackTrace();
			}		
		return pb;
	}


	public PointBean lastPoint(long devId) {
		Connection lconn = getConn();
		//int rows = 5;
		String sql = "select DEVICE_ID, HW_CLOCK, " 
						+ " LATITUDE,LONGITUDE,ALTITUDE,ANGLE,SPEED,SATELLITES, " 
						+ " IGNITION, DOORLOCK, MOVEMENT, PDOP, HDOP "
						+ " from ( select * from tracks where DEVICE_ID = " + devId + " order by HW_CLOCK DESC) " 
						+ " where rownum = 1 ";
		//System.out.println("SQL: " + sql);
		ResultSet rs = null;
		PointBean rc = new PointBean();
		try {
			rs = lconn.prepareStatement(sql).executeQuery();	
		while(rs.next()){
			rc = new PointBean(rs.getBigDecimal(1), 
							  rs.getDate(2), 
							  rs.getDouble(3),
							  rs.getDouble(4),
							  rs.getDouble(5), 
							  rs.getDouble(6), 
							  rs.getDouble(7),
							  rs.getInt(8),
							  rs.getBoolean(9),
							  rs.getBoolean(10),
							  rs.getBoolean(11),
							  rs.getByte(12),
							  rs.getByte(13));
					}
			rs.close();
			lconn.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception.");
			e.printStackTrace();
		}
		return rc;
	}
	
	
	public List<PointBean> lastPoints(int points) {
		Connection lconn = getConn();
		//int rows = 5;
		String sql = "select DEVICE_ID, to_char(HW_CLOCK, 'DD-MON-YYYY HH:MI:SS')," 
						+ " LATITUDE,LONGITUDE,ALTITUDE,ANGLE,SPEED,SATELLITES, " 
						+ " IGNITION, DOORLOCK, MOVEMENT, PDOP, HDOP"
						+ " to_char(UNI_TIME, 'DD-MON-YYYY HH:MI:SS') " 
						+ " from ( select * from tracks order by HW_CLOCK desc) " 
						+ "where rownum < " + (points+1) ;
		System.out.println("SQL: " + sql);
		ResultSet rs = null;
		List<PointBean> rc = new ArrayList<PointBean>();
		try {
			rs = lconn.prepareStatement(sql).executeQuery();	
		while(rs.next()){
			rc.add(	new PointBean(rs.getBigDecimal(1), 
							  rs.getDate(2), 
							  rs.getDouble(3),
							  rs.getDouble(4),
							  rs.getDouble(5), 
							  rs.getDouble(6), 
							  rs.getDouble(7),
							  rs.getInt(8),
							  rs.getBoolean(9),
							  rs.getBoolean(10),
							  rs.getBoolean(11),
							  rs.getByte(12),
							  rs.getByte(13)));
					}
		rs.close();
		lconn.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception.");
			e.printStackTrace();
		}
		return rc;
	}

	public List<Device> getDevices() {
		Connection conn = getConn();		
		String sql = "select device_id, " 
			+ "NAME, "
			+ "IS_BLOCKED, " 
			+ "TECH_COMMENT, "
			+ "DEVICE_TYPE, "
			+ "DEVICE_GROUP "
			+ "from DEVICES " 
			+ " where IS_ON='Y'"
			;	
		//System.out.println("SQL (getDevices): " + sql);
		ResultSet rs = null;
		List<Device> rc = new ArrayList<Device>();
		try {
			rs = conn.prepareStatement(sql).executeQuery();	
		while(rs.next()){			
			rc.add(	new Device(
					rs.getLong(1), 
					rs.getBoolean(3), 
					rs.getString(4),
					rs.getInt(5),
					rs.getInt(6),
					rs.getString(2)));	
			//System.out.println("  -- Device Name: " + rs.getString(2));
					}
		rs.close();
		conn.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception.");
			e.printStackTrace();
		}
		
		return rc;
	}

	





	
	
}
