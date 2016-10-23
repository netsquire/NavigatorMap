package up.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Vehicle /* extends Device */ implements IsSerializable {	
	
	private static int nextId = 0;
	public  int id;		
	public String name;
	private double carId;
	private Driver driver;
	private Device device;

	public Vehicle() {
		this.id = 0;
	}	
	
	public Vehicle(String name) {
		nextId++;
		this.id = nextId;
		this.name = name;
		}

	public Vehicle(double carid, String name) {
		this(name);
		setCarId(carid);
		}
	
	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCarId() {
		return carId;
	}

	public void setCarId(double carId) {
		this.carId = carId;
	}   
}
