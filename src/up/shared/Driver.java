package up.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Driver  implements IsSerializable {
	
	private static int nextId = 0;	
	public  int id;	
	public String name;
	private Vehicle car;
	
	public Driver() {
		this.id = 0;
		}
	
	public Driver(String name) {
		nextId++;
		this.id = nextId;
		this.name = name;
		}
	
	public Driver(String name, Vehicle car) {
		this.name = name;
		this.car = car;
		}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vehicle getCar() {
		return car;
	}

	public void setCar(Vehicle car) {
		this.car = car;
	}
}
