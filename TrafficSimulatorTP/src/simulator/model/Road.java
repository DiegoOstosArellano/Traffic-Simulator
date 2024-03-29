package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public abstract class Road extends SimulatedObject {
	protected Junction source_junction;
	protected Junction destination_junction;
	protected int length;
	protected int maximum_speed;
	protected int current_speed_limit;
	protected int contamination_alarm_limit;
	protected Weather weather_conditions;
	protected int total_contamination;
	protected List<Vehicle> vehiculos;
	protected VehicleComparator comp;
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) { 
		super(id); 
		if (maxSpeed <= 0) throw new IllegalArgumentException("maxSpeed must be a positive number"); 
		if (contLimit < 0) throw new IllegalArgumentException("contLimit must be a positive number");
		if (length <= 0) throw new IllegalArgumentException("length must be a positive number");
		if (srcJunc.equals(null)) throw new IllegalArgumentException("there must be a source Junction");
		if (destJunc.equals(null)) throw new IllegalArgumentException("there must be a destination Junction");
		if (weather.equals(null)) throw new IllegalArgumentException("there must be a weather");
		
		source_junction = srcJunc;
		destination_junction = destJunc;
		this.length = length;
		maximum_speed = maxSpeed;
		current_speed_limit = maximum_speed;
		contamination_alarm_limit = contLimit;
		weather_conditions = weather;
		total_contamination = 0;
		vehiculos = new ArrayList<Vehicle>();
		comp = new VehicleComparator();
		
		this.destination_junction.addIncommingRoad(this);
		this.source_junction.addOutGoingRoad(this);
	} 
	
	// Se a�ade el vehiculo a la lista de vehiculos de la carretera (al final) si la localizacion es 0 y la velocidad tambien.
	void enter(Vehicle v) {
		if (v.getLocation() != 0) throw new IllegalArgumentException("location must be 0"); 
		if (v.getSpeed() != 0) throw new IllegalArgumentException("current speed must be 0");
		
		vehiculos.add(v);
	}
	
	void exit(Vehicle v) {
		vehiculos.remove(v);
	}
	
	void setWeather(Weather w) {
		if (w.equals(null)) throw new IllegalArgumentException("there must be a weather"); 
		weather_conditions = w;
	}
	
	void addContamination(int c) {
		if (c < 0) throw new IllegalArgumentException("contamination must be a positive number");
		total_contamination += c;
	}
	
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
	
	// Avanza el estado de la carretera de la siguiente manera.
	// Primero se reduce la contaminacion total, despues se actualiza el limite de velocidad y finalmente se recorre la lista de vehiculos,
	// para cada uno de ellos se establece su velocidad al valor devuelto por la funcion calculateVehicleSpeed y se llama al metodo advance
	// de cada uno de estos vehiculos. Para terminar que se ordena la lista de vehiculos por su localizacion.
	void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();
		
		for (Vehicle v : vehiculos) {
			v.setSpeed(calculateVehicleSpeed(v));
			v.advance(time);
		}
		vehiculos.sort(comp);
	}
	
	public JSONObject report() {
		JSONObject jo1 = new JSONObject();

		
		jo1.put("speedlimit", current_speed_limit);
		jo1.put("co2", total_contamination);
		jo1.put("weather", weather_conditions.toString());
		
		JSONArray ja = new JSONArray();
		for (Vehicle v : vehiculos) {
			ja.put(v.getId());
		}
		
		jo1.put("vehicles", ja);
		jo1.put("id", _id);
		
		return jo1;
		
	 }
	
	public Junction getDest() {
		return destination_junction;
	}

	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(vehiculos);
	}

	public Junction getSrc() {
		return source_junction;
	}

	public int getLength() {
		return length;
	}

	public int getMaxSpeed() {
		return maximum_speed;
	}

	public int getSpeedLimit() {
		return current_speed_limit;
	}

	public int getContLimit() {
		return contamination_alarm_limit;
	}

	public Weather getWeather() {
		return weather_conditions;
	}

	public int getTotalCO2() {
		return total_contamination;
	}

	
	
}
