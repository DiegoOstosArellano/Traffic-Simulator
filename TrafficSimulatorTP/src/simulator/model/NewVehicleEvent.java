package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event {
	private String id;
	private int velocidad_maxima;
	private int clase_contaminacion;
	private List<String> itinerary_strings;
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) { 
			super(time); 
			this.id = id;
			this.velocidad_maxima = maxSpeed;
			this.clase_contaminacion = contClass;
			this.itinerary_strings = itinerary;
	}

	// Crea un vehiculo en funcion de sus argumentos y se añade al mapa de carreteras
	@Override
	void execute(RoadMap map) {
		List<Junction> itinerary_junctions = new ArrayList<Junction>();
		for(String s: itinerary_strings) {
			itinerary_junctions.add(map.getJunction(s));
		}
		Vehicle v = new Vehicle(id, velocidad_maxima, clase_contaminacion, itinerary_junctions);
		map.addVehicle(v);
		v.moveToNextRoad();
	}
	@Override
	public String toString() {
		return "New Vehicle '"+id+"'";
	}
}
