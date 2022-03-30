package simulator.model;

public class NewInterCityRoadEvent extends NewRoadEvent{
	public NewInterCityRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) { 
		super(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}

	@Override
	Road createRoadObject() {
		return new InterCityRoad(id, js, jd, velocidad_maxima, contamination_limit, length, weather);
	}
	@Override
	public String toString() {
		return "New InterCityRoad '"+id+"'";
	}
}
