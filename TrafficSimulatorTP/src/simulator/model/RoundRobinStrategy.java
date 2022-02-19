package simulator.model;

import java.util.List;

public class RoundRobinStrategy implements LightSwitchStrategy{
	
	private int ticks;
	
	public RoundRobinStrategy (int timeSlot){
		ticks = timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,int currTime) {
		if (roads.size() == 0) return -1;
		else if (currGreen == -1) return 0;
		else if (currTime - lastSwitchingTime < ticks) return currGreen;
		else return (currGreen + 1)%roads.size();
	}
	
	
}
