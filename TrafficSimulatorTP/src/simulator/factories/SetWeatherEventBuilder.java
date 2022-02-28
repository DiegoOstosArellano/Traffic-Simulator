package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {
	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		JSONArray ja = data.getJSONArray("info");
		List<JSONObject> l = new ArrayList<JSONObject>();
		List<Pair<String,Weather>> ws = new ArrayList<Pair<String,Weather>>();
		Pair<String,Weather> aux;
		for (int i = 0; i < ja.length(); ++i) {
			l.add(ja.getJSONObject(i));
			aux = new Pair<String,Weather>(l.get(i).getString("road"), Weather.valueOf(l.get(i).getString("weather").toUpperCase()));
			ws.add(aux);
		}
		
		return new SetWeatherEvent(time, ws);
	}
}
