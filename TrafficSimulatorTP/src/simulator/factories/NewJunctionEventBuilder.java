package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;

public class NewJunctionEventBuilder extends Builder<Event> {
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> fl, Factory<DequeuingStrategy> fd) {
		super("new_junction");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		JSONArray coor = data.getJSONArray("coor");
		int x = coor.getInt(0);
		int y = coor.getInt(1);
		
		return null;
	}
}
