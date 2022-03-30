package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event {
	private List<Pair<String,Weather>> ws;
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) { 
		super(time); 
		if (ws == null) throw new IllegalArgumentException("there must be a weather");
		this.ws = ws;
		}

	@Override
	void execute(RoadMap map) {
		for (Pair<String,Weather> w: ws) {
			Road r = map.getRoad(w.getFirst());
			if (r == null) throw new IllegalArgumentException("the road doesn't exist");
			r.setWeather(w.getSecond());
		}
	} 
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("Chage weather class: ["); 
		for(int i = 0; i < this.ws.size()-1; ++i)
			str.append("(" + this.ws.get(i).getFirst() + "," + this.ws.get(i).getSecond() + ") , ");
		str.append("(" + this.ws.get(this.ws.size() - 1).getFirst() + "," + this.ws.get(this.ws.size()-1).getSecond() + ")]"); 
		return str.toString();
		
	}
}
