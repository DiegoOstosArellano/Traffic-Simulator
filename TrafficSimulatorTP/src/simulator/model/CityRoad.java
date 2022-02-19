package simulator.model;

public class CityRoad extends Road{
	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather){
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x = 0;
		
		if(weather_conditions.equals(Weather.WINDY) || weather_conditions.equals(Weather.STORM)) 
			x = 10;
		else 
			x = 2;
		
		if (total_contamination - x < 0) 
			total_contamination = 0;
		else 
			total_contamination -= x;
	}

	@Override
	void updateSpeedLimit() {}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return (int)(((11.0 - v.getContClass())/11.0)*current_speed_limit);
	}
}
