package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.json.JSONObject;


public class Vehicle extends SimulatedObject {

	private List<Junction> itinerary;
	private int maximum_speed;
	private int current_speed;
	private VehicleStatus status;
	private Road road;
	private int location;
	private int contamination_class;
	private int total_contamination;
	private int total_travelled_distance;
	private int last_junction;
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) { 
		super(id); 
		if (maxSpeed <= 0) throw new IllegalArgumentException("maxSpeed must be a positive number"); 
		if (contClass < 0 || contClass > 10) throw new IllegalArgumentException("contClass must be a number between 0 and 10");
		if (itinerary.size() < 2) throw new IllegalArgumentException("there must be at least two junctions");
			
		this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		maximum_speed = maxSpeed;
		contamination_class = contClass;
		current_speed = 0;
		status = VehicleStatus.PENDING;
		road = null;
		location = 0;
		total_contamination = 0;
		total_travelled_distance = 0;
		last_junction = 1;
	} 
	
	void setSpeed(int s) {
		if(status.equals(VehicleStatus.TRAVELING)) {
			if (s < 0) throw new IllegalArgumentException("Speed must be a postive value");
			if (maximum_speed < s) current_speed = maximum_speed;
			else current_speed = s;
		}
		
	}
	
	void setContClass(int c) {
		if (c < 0 || c > 10) throw new IllegalArgumentException("contamination must be a number between 0 and 10");
		contamination_class = c;
	}
	
	
	// Si el estado del vehiculo es traveling se actualiza la localizacion a su localizacion actual + current_speed, siempre que sea menor 
	// que la carretera. Y se actualiza la contaminacion producida por el vehiculo en ese tramo.
	// Si se llega al final de la carretera el vehiculo entra en la cola del cruce correspondiendo, modificando su estado a waiting.
	@Override
	void advance(int time) {
		if (status.equals(VehicleStatus.TRAVELING)) {
			int location_prev = location;
			if ((location + current_speed) < road.getLength()) location = location + current_speed; 
			else location = road.getLength();
			
			int c = ((location - location_prev)*contamination_class);
			total_contamination += c;
			road.addContamination(c);

			
			if (location >= road.getLength()) {
				road.getDest().enter(this);
				//itinerary.get(last_junction + 1).enter(this); 
				status = VehicleStatus.WAITING;
				current_speed = 0;
			}
			this.total_travelled_distance += location - location_prev; 
		}
	}
	
	// Mueve el vehiculo a la siguiente carretera.
	// Este proceso se realiza saliendo de la carretera actual y entrando en la proxima carretera del itinerario del vehiculo.
	// Para encontrar la siguiente carretera debe preguntar al cruce en el que el vehiculo esta esperando o el primero del itinerario 
	// en caso de que su estado sea pending. 
	// Distinguimo tres casos:
	// - Cuando el vehiculo el vehiculo no ha entrado en ningun cruce (pending), luego no puede venir de ninguna carretera (no hacemos exit, solo enter)
	// - Cuando el vehiculo esta en medio de su itinerario (waiting), hacemos enter y exit.
	// - Cuando el vehiculo esta en el ultimo cruce de su itinerario, luego solo hacemos exit, pues ya no entra en ninguna carretera.
	void moveToNextRoad() {
	    if (status.equals(VehicleStatus.ARRIVED) || status.equals(VehicleStatus.TRAVELING)) throw new IllegalArgumentException("the status must be Pending or Waiting");
		 	
	 	if(status.equals(VehicleStatus.PENDING)){
	 		Road r1 = itinerary.get(0).roadTo(itinerary.get(1));
	 		road = r1;
	 		r1.enter(this);
			status = VehicleStatus.TRAVELING;
	 	}
	 	else {
	 		if(last_junction < (itinerary.size() - 1)) {
	 			location = 0; 
	 			Road r2 = itinerary.get(last_junction).roadTo(itinerary.get(last_junction + 1)); 
		 		road.exit(this);
	 			this.road = r2;
	 			r2.enter(this);
	 			status = VehicleStatus.TRAVELING;
	 			last_junction++; 
	 		}
	 		else {
	 			this.status = VehicleStatus.ARRIVED;
	 			current_speed = 0;
	 			road.exit(this);
	 			road = null;
	 		}
	 		
	 	}
	}
	
	 public JSONObject report() {
		 JSONObject jo1 = new JSONObject();

			jo1.put("distance", total_travelled_distance);
			
			if (status.equals(VehicleStatus.WAITING) || status.equals(VehicleStatus.TRAVELING)) {
				jo1.put("road", road.getId());
			}

			jo1.put("co2", total_contamination);
			if (status.equals(VehicleStatus.WAITING) || status.equals(VehicleStatus.TRAVELING)) {
				jo1.put("location", location);
			}
			
			jo1.put("id", _id);
			jo1.put("class", contamination_class);
			jo1.put("speed", current_speed);
			jo1.put("status", status.toString());
			
			
			return jo1;
	 }

	public int getSpeed() {
		return current_speed;
	}

	public int getLocation() {
		return location;
	}

	public int getContClass() {
		return contamination_class;
	}

	public Road getRoad() {
		return road;
	}

	public List<Junction> getItinerary() {
		return itinerary;
	}
	
	public int getMaxSpeed() {
		return maximum_speed;
	}

	public VehicleStatus getStatus() {
		return status;
	}

	public int getTotalCO2() {
		return total_contamination;
	}

	public int getDistance() {
		return this.total_travelled_distance; 
	}

	public String getItineraryString() {
		StringBuilder str = new StringBuilder("[");
		for(int i = 0; i < this.itinerary.size() - 1; ++i)
			str.append(this.itinerary.get(i)._id + ",");
		str.append(this.itinerary.get(this.itinerary.size() - 1) + "]"); 
		return str.toString(); 
	}
}
