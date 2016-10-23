package up.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Device implements IsSerializable  {

	public Device(){}
	
	long deviceId;
	boolean isBlocked;
	String comment;
	int deviceType;
	int deviceGroup;
	String name;
	
	public Device(long deviceId, boolean isBlocked, String comment,
			       int deviceType, int deviceGroup, String name) {
		this.deviceId = deviceId;
		this.isBlocked = isBlocked;
		this.comment = comment;
		this.deviceType = deviceType;
		this.deviceGroup = deviceGroup;
		this.name = name;
	}
	public long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}
	public boolean isBlocked() {
		return isBlocked;
	}
	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
	public int getDeviceGroup() {
		return deviceGroup;
	}
	public void setDeviceGroup(int deviceGroup) {
		this.deviceGroup = deviceGroup;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
