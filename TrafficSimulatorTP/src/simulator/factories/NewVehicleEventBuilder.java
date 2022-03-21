package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {
	public NewVehicleEventBuilder() {
		super("new_vehicle");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		int maxspeed = data.getInt("maxspeed");
		int clase = data.getInt("class"); 
		JSONArray ja = data.getJSONArray("itinerary");
		List<String> l = new ArrayList<String>();
		for (int i = 0; i < ja.length(); ++i) {
			l.add(ja.getString(i));
		}
		
		return new NewVehicleEvent(time, id, maxspeed, clase, l);
	}
	
	@Override
	public String toString() {
		return "New Vehicle '"+ _type +"'";
	}
}
