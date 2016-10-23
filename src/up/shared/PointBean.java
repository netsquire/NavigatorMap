package up.shared;

import java.math.BigDecimal;
import java.sql.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PointBean implements IsSerializable {

	public PointBean(){}
	
	BigDecimal   deviceId;     
	Date   hwClock;	
	double latitude;
	double longitude;
	double altitude; 
	double angle;
	double speed;
	int    satellites;
	boolean  ignition;
	boolean  doorlock;
	boolean  movement;
	short  pdop;
	short  hdop;
	
	public PointBean(BigDecimal deviceId, Date hwClock, double latitude,
			double longitude, double altitude, double angle, double speed,
			int satellites, boolean ignition, boolean doorlock, boolean movement,
			short pdop, short hdop) {
		this.deviceId = deviceId;
		this.hwClock = hwClock;
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.angle = angle;
		this.speed = speed;
		this.satellites = satellites;
		this.ignition = ignition;
		this.doorlock = doorlock;
		this.movement = movement;
		this.pdop = pdop;
		this.hdop = hdop;
	}


	
	public PointBean(BigDecimal deviceId, Date hwClock, double latitude,
			double longitude, double altitude, double angle, double speed,
			int satellites) {
		super();
		this.deviceId = deviceId;
		this.hwClock = hwClock;
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.angle = angle;
		this.speed = speed;
		this.satellites = satellites;
	}
	



	public boolean getIgnition() {
		return ignition;
	}



	public void setIgnition(boolean ignition) {
		this.ignition = ignition;
	}



	public boolean getDoorlock() {
		return doorlock;
	}



	public void setDoorlock(boolean doorlock) {
		this.doorlock = doorlock;
	}



	public boolean getMovement() {
		return movement;
	}



	public void setMovement(boolean movement) {
		this.movement = movement;
	}



	public short getPdop() {
		return pdop;
	}



	public void setPdop(short pdop) {
		this.pdop = pdop;
	}



	public short getHdop() {
		return hdop;
	}



	public void setHdop(short hdop) {
		this.hdop = hdop;
	}



	public BigDecimal getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(BigDecimal deviceId) {
		this.deviceId = deviceId;
	}
	public Date getHwClock() {
		return hwClock;
	}
	public void setHwClock(Date hwClock) {
		this.hwClock = hwClock;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getAltitude() {
		return altitude;
	}
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public int getSatellites() {
		return satellites;
	}
	public void setSatellites(int satellites) {
		this.satellites = satellites;
	}
	
}
