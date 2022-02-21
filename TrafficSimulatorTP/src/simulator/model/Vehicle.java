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
		last_junction = -1;
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
	
	@Override
	void advance(int time) {
		if (status.equals(VehicleStatus.TRAVELING)) {
			int location_prev = location;
			if ((location + current_speed) < road.getLength()) location = location + current_speed; 
			else location = road.getLength();
			
			int c = ((location - location_prev)*contamination_class);
			total_contamination += c;
			road.addContamination(c);

			
			if (location == road.getLength()) {
				itinerary.get(last_junction + 1).enter(this); 
				status = VehicleStatus.WAITING;
				current_speed = 0;
			}
			this.total_travelled_distance += location - location_prev; 
		}
	}
	
	void moveToNextRoad() {
	    if (status.equals(VehicleStatus.ARRIVED) || status.equals(VehicleStatus.TRAVELING)) throw new IllegalArgumentException("the status must be Pending or Waiting");
		 	
	 	if(status.equals(VehicleStatus.PENDING)){
	 		Road r1 = itinerary.get(0).roadTo(itinerary.get(1));
	 		r1.enter(this);
	 		road = r1;
	 	}
	 	else {
	 		Road r2 = itinerary.get(last_junction).roadTo(itinerary.get(last_junction + 1)); 
	 		road.exit(this);
	 		location = 0; 
	 		if(last_junction < itinerary.size())  {
	 			r2.enter(this);
	 			road = r2; 
	 		}
	 		
	 	}
		last_junction++; 
		status = VehicleStatus.TRAVELING;
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
}
