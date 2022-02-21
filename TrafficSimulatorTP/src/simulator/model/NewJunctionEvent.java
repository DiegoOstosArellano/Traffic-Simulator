package simulator.model;

public class NewJunctionEvent extends Event {
	private String id;
	private LightSwitchingStrategy light_switching_strategy;
	private DequeuingStrategy dequeueing_strategy;
	int x, y;

	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) { 
			super(time); 
			this.id = id;
			this.light_switching_strategy = lsStrategy;
			this.dequeueing_strategy = dqStrategy;
			this.x = xCoor;
			this.y = yCoor;
}

	@Override
	void execute(RoadMap map) {
		// TODO Auto-generated method stub
		Junction j = new Junction(id, light_switching_strategy, dequeueing_strategy, x, y);
		map.addJunction(j);
	}
}