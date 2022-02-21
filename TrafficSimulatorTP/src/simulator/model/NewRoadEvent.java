package simulator.model;

public abstract class NewRoadEvent extends Event {
	protected String id;
	protected String source_junction;
	protected String destination_junction;
	protected int length;
	protected int contamination_limit;
	protected int velocidad_maxima;
	protected Weather weather;
	protected Junction js;
	protected Junction jd;
	
	public NewRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) { 
			super(time); 
			this.id = id;
			this.source_junction = srcJun;
			this.destination_junction = destJunc;
			this.length = length;
			this.contamination_limit = co2Limit;
			this.velocidad_maxima = maxSpeed;
			this.weather = weather;
	}

	@Override
	void execute(RoadMap map) {
		js = map.getJunction(source_junction);
		jd = map.getJunction(destination_junction);
		
		map.addRoad(createRoadObject());
	}
	
	abstract Road createRoadObject();
}
