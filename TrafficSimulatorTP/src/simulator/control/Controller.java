package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimulator;

public class Controller {
	//privates entonces ;)
	private TrafficSimulator traffic_simulator;
	private Factory<Event> events_factory;
	
	public Controller(TrafficSimulator ts, Factory<Event> fe) {
		if (ts == null) throw new IllegalArgumentException("there must be a traffic simulator");
		if (fe == null) throw new IllegalArgumentException("there must be a factory of events");
		
		traffic_simulator = ts;
		events_factory = fe;
	}
	
	//La profesora dijo que la excepción no hacía falta
	public void loadEvents(InputStream in) {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		JSONArray ja = jo.getJSONArray("events"); 
		for(int i=0; i<ja.length(); i++) {
			traffic_simulator.addEvent(events_factory.createInstance(ja.getJSONObject(i)));
		}
	}
	
	public void run (int n, OutputStream out) {
		PrintStream p = new PrintStream(out);
		p.println("{");
		p.println("  \"states\": [");
		for (int i = 0; i < n; ++i) {
			if(i == 297)
				System.out.println();
			traffic_simulator.advance();
			p.print(traffic_simulator.report().toString());
			p.println(",");
		}
		p.println("]");
		p.println("}");
	}
	
	public void reset (int n, OutputStream out) {
		traffic_simulator.reset();
	}
}
