package simulator.control;
//test
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	private TrafficSimulator traffic_simulator;
	private Factory<Event> events_factory;
	
	public Controller(TrafficSimulator ts, Factory<Event> fe) {
		if (ts == null) throw new IllegalArgumentException("there must be a traffic simulator");
		if (fe == null) throw new IllegalArgumentException("there must be a factory of events");
		
		traffic_simulator = ts;
		events_factory = fe;
	}
	
	// Carga los eventos (La profesora dijo que la excepción no hacía falta).
	public void loadEvents(InputStream in) {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		JSONArray ja = jo.getJSONArray("events"); 
		for(int i=0; i<ja.length(); i++) {
			traffic_simulator.addEvent(events_factory.createInstance(ja.getJSONObject(i)));
		}
	}
	
	// Ejecuta el simulador n ticks, llamando al metodo advance n veces y escribe los diferentes estados en out, utilizando el formato JSON.
	public void run (int n, OutputStream out) {
		PrintStream p = new PrintStream(out);
		p.println("{");
		p.println("  \"states\": [");
		for (int i = 0; i < n - 1; ++i) {
			traffic_simulator.advance();
			p.print(traffic_simulator.report().toString());
			p.println(",");
		}
		traffic_simulator.advance();
		p.println(traffic_simulator.report().toString());
		p.println("]");
		p.println("}");
	}
	
	public void reset (int n, OutputStream out) {
		traffic_simulator.reset();
	}
	
	public void addObserver(TrafficSimObserver o) {
		traffic_simulator.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		traffic_simulator.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		traffic_simulator.addEvent(e);
	}
}
