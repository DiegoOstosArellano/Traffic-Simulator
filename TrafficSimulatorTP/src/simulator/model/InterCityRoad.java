package simulator.model;

public class InterCityRoad extends Road {
	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather){
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x = 0;
		
		switch(weather_conditions) {
		case SUNNY:
			x = 2;
			break;
		case CLOUDY:
			x = 3;
			break;
		case RAINY:
			x = 10;
			break;
		case WINDY:
			x = 15;
			break;
		case STORM:
			x = 20;
		};
		
		total_contamination = (int)(((100.0 - x)/100.0)*total_contamination);
	}

	@Override
	void updateSpeedLimit() {
		if (total_contamination > contamination_alarm_limit) 
			current_speed_limit = (int)(maximum_speed*0.5);
		else 
			current_speed_limit = maximum_speed;
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		if(weather_conditions.equals(Weather.STORM)) 
			current_speed_limit = (int)(current_speed_limit*0.8);
			
		return current_speed_limit;
	}
	
}
