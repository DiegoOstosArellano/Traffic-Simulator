package simulator.factories;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;

public class NewInterCityRoadEventBuilder extends NewRoadEventBuilder{
	public NewInterCityRoadEventBuilder(){
		super("new_inter_city_road");
	}

	@Override
	protected Event createTheRoad() {
		return new NewInterCityRoadEvent(time, id, src, dest, length, co2limit, maxspeed, weather);
	}
	
	@Override
	public String toString() {
		return "New Intercity Road '"+ _type +"'";
	}
}