package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {
	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		JSONArray ja = data.getJSONArray("info");
		List<JSONObject> l = new ArrayList<JSONObject>();
		List<Pair<String,Integer>> ws = new ArrayList<Pair<String,Integer>>();
		Pair<String,Integer> aux;
		for (int i = 0; i < ja.length(); ++i) {
			l.add(ja.getJSONObject(i));
			aux = new Pair<String,Integer>(l.get(i).getString("vehicle"), l.get(i).getInt("class"));
			ws.add(aux);
		}
		
		return new NewSetContClassEvent(time, ws);
	}
	
	@Override
	public String toString() {
		return "New setContClass '"+ _type +"'";
	}
}