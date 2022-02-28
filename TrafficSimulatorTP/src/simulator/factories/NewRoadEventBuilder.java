package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event>{
	int time;
	String id;
	String src;
	String dest;
	int length;
	int co2limit;
	int maxspeed;
	Weather weather;
	
	NewRoadEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		time = data.getInt("time");
		id = data.getString("id");
		src = data.getString("src");
		dest = data.getString("dest");
		length = data.getInt("length");
		co2limit = data.getInt("co2limit");
		maxspeed = data.getInt("maxspeed");
		String string_weather = data.getString("weather");
		weather = Weather.valueOf(string_weather.toUpperCase());
		
		return createTheRoad();
	}
	
	protected abstract Event createTheRoad();
}
