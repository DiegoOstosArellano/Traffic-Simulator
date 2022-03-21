package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {
	private RoadMap mapa_carreteras;
	private List<Event> lista_eventos;
	private int time;
	private List<TrafficSimObserver> lista_observadores;
	
	public TrafficSimulator() {
		mapa_carreteras = new RoadMap();
		lista_eventos = new SortedArrayList<Event>();
		time = 0;
		lista_observadores = new ArrayList<TrafficSimObserver>();
	}
	
	public void addEvent(Event e) {
		lista_eventos.add(e);
		for (TrafficSimObserver to: lista_observadores) {
			to.onEventAdded(mapa_carreteras, lista_eventos, e, time);
		}
	}
	
	// Avanza el estado de la simulacion de la siguiente forma.
	// Incrementa el tiempo de la simulacion en 1, ejecuta todos los eventos cuyo tiempo sea igual al actual y los elimina. Posteriormente llama
	// al metodo advance de todos los cruces y finalmente al de todas las carreteras.
	public void advance() {
		time++;
		for (TrafficSimObserver to: lista_observadores) {
			to.onAdvanceStart(mapa_carreteras, lista_eventos, time);
		}
		
		while (lista_eventos.size() > 0 && lista_eventos.get(0).getTime() == time) {
			lista_eventos.remove(0).execute(mapa_carreteras);
		}
		try {
			for (Junction j: mapa_carreteras.getJunctions()) {
				j.advance(time);
			}
			
			for(Road r: mapa_carreteras.getRoads()) {
				r.advance(time);
			}
		}catch(Exception e) {
			for (TrafficSimObserver to: lista_observadores) {
				to.onError(e.getMessage());
			}
			throw e;
		}
		for (TrafficSimObserver to: lista_observadores) {
			to.onAdvanceEnd(mapa_carreteras, lista_eventos, time);
		}
	}
	
	// Limpia el mapa de carretras y la lista de eventos y pone el tiempo a 0.
	public void reset() {
		mapa_carreteras.reset();
		lista_eventos.clear();
		time = 0;
		for (TrafficSimObserver to: lista_observadores) {
			to.onReset(mapa_carreteras, lista_eventos, time);
		}
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("time", time);
		jo.put("state", mapa_carreteras.report());
		
		return jo;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		lista_observadores.add(o);
		for (TrafficSimObserver to: lista_observadores) {
			to.onRegister(mapa_carreteras, lista_eventos, time);
		}
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		lista_observadores.remove(o);
	}
}
