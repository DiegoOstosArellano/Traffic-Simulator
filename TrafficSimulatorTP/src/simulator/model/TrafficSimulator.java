package simulator.model;

import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator {
	private RoadMap mapa_carreteras;
	private List<Event> lista_eventos;
	private int time;
	
	public TrafficSimulator() {
		mapa_carreteras = new RoadMap();
		lista_eventos = new SortedArrayList<Event>();
		time = 0;
	}
	
	public void addEvent(Event e) {
		lista_eventos.add(e);
	}
	
	// Avanza el estado de la simulacion de la siguiente forma.
	// Incrementa el tiempo de la simulacion en 1, ejecuta todos los eventos cuyo tiempo sea igual al actual y los elimina. Posteriormente llama
	// al metodo advance de todos los cruces y finalmente al de todas las carreteras.
	public void advance() {
		time++;
		
		while (lista_eventos.size() > 0 && lista_eventos.get(0).getTime() == time) {
			lista_eventos.remove(0).execute(mapa_carreteras);
		}
		
		for (Junction j: mapa_carreteras.getJunctions()) {
			j.advance(time);
		}
		
		for(Road r: mapa_carreteras.getRoads()) {
			r.advance(time);
		}
	}
	
	// Limpia el mapa de carretras y la lista de eventos y pone el tiempo a 0.
	public void reset() {
		mapa_carreteras.reset();
		lista_eventos.clear();
		time = 0;
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("time", time);
		jo.put("state", mapa_carreteras.report());
		
		return jo;
	}
}
