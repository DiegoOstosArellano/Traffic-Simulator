package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {
	Factory<LightSwitchingStrategy> fl;
	Factory<DequeuingStrategy> fd;
	
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> fl, Factory<DequeuingStrategy> fd) {
		super("new_junction");
		this.fl = fl;
		this.fd = fd;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		JSONArray coor = data.getJSONArray("coor");
		int x = coor.getInt(0);
		int y = coor.getInt(1);
		JSONObject lsStrategy = data.getJSONObject("ls_strategy");
		LightSwitchingStrategy ls = fl.createInstance(lsStrategy);
		JSONObject dqStrategy = data.getJSONObject("dq_strategy");
		DequeuingStrategy dq = fd.createInstance(dqStrategy);
		
		return new NewJunctionEvent(time, id, ls, dq, x, y);
	}
}
