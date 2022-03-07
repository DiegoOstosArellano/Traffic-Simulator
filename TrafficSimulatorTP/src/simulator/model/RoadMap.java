package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	private List<Junction> lista_cruces;
	private List<Road> lista_carreteras;
	private List<Vehicle> lista_vehiculos;
	private Map<String, Junction> mapa_cruces;
	private Map<String, Road> mapa_carreteras;
	private Map<String, Vehicle> mapa_vehiculos;
	
	RoadMap(){
		lista_cruces = new ArrayList<Junction>();
		lista_carreteras = new ArrayList<Road>();
		lista_vehiculos = new ArrayList<Vehicle>();
		mapa_cruces = new HashMap<String, Junction>();
		mapa_carreteras = new HashMap<String, Road>();
		mapa_vehiculos = new HashMap<String, Vehicle>();
	}
	
	// Comprueba que este cruce no exista mediante el identificador. 
	// Si todo va bien añade el cruce j al final de la lista de cruces y modifica el mapa de cruces.
	void addJunction(Junction j) { 
		if(mapa_cruces.containsKey(j.getId())) throw new IllegalArgumentException("this junction is already in the map"); 
		
		mapa_cruces.put(j.getId(), j);
		lista_cruces.add(j);
	}
	
	// Comprueba que esta carretera no exista mediante el identificador y que los cruces que conecta estan en el mapa de cruces. 
	// Si todo va bien añade la carretra r al final de la lista de carreteras y modifica el mapa de carreteras.
	void addRoad(Road r) {
		if(mapa_carreteras.containsKey(r.getId())) throw new IllegalArgumentException("this road is already in the map"); 
		if(!mapa_cruces.containsKey(r.getSrc().getId()) || !mapa_cruces.containsKey(r.getDest().getId())) throw new IllegalArgumentException("this road is incorrect");
			
		mapa_carreteras.put(r.getId(), r);
		lista_carreteras.add(r);
	}
	
	// Comprueba que este vehiculo no exista mediante el identificador y que su itinerario es valido.
	// Si todo va bien añade v al final de la lista de vehiculos y modifica el mapa de vehiculos.
	void addVehicle(Vehicle v) {
		if(mapa_vehiculos.containsKey(v.getId())) throw new IllegalArgumentException("this road is already in the map"); 
		for (int i = 0; i < v.getItinerary().size() - 1; ++i) {
			if(v.getItinerary().get(i).roadTo(v.getItinerary().get(i + 1)) == null) throw new IllegalArgumentException("this road isnÂ´t in the itinerary of this vehicle"); 
		}
			
		mapa_vehiculos.put(v.getId(), v);
		lista_vehiculos.add(v);
	}
	
	public Junction getJunction(String id) {
		return mapa_cruces.getOrDefault(id, null); 
		 
	}
	
	public Road getRoad(String id) {
		return mapa_carreteras.getOrDefault(id, null); 
	}

	public Vehicle getVehicle(String id) {
		return mapa_vehiculos.getOrDefault(id, null); 
	}
	
	public List<Junction>getJunctions(){
		return Collections.unmodifiableList(lista_cruces);
	}
	
	public List<Road>getRoads(){
		return Collections.unmodifiableList(lista_carreteras);
	}
	
	public List<Vehicle>getVehicles(){
		return Collections.unmodifiableList(lista_vehiculos);
	}
	
	// Limpia todas las listas y mapas.
	void reset() { 
		lista_cruces.clear();
		lista_carreteras.clear();
		lista_vehiculos.clear();
		mapa_cruces.clear();
		mapa_carreteras.clear();
		mapa_vehiculos.clear();
	}
	
	public JSONObject report() {
		
		JSONObject jo1 = new JSONObject();
		
		JSONArray ja2 = new JSONArray();
		for (Road r : lista_carreteras) {
			ja2.put(r.report());
		}
		
		jo1.put("roads", ja2);
		
		JSONArray ja = new JSONArray();
		for (Junction j : lista_cruces) {
			ja.put(j.report());
		}
		
		jo1.put("junctions", ja);
		
		JSONArray ja3 = new JSONArray();
		for (Vehicle v : lista_vehiculos) {
			ja3.put(v.report());
		}
		jo1.put("vehicles", ja3);
		
		return jo1;
	}
}
