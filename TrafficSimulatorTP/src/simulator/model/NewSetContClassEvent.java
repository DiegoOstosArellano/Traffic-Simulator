package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class NewSetContClassEvent extends Event{
	private List<Pair<String,Integer>> cs;
	
	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) { 
		super(time); 
		if (cs == null) throw new IllegalArgumentException("there must be a contClass");
		this.cs = cs;
	}

	@Override
	void execute(RoadMap map) {
		for (Pair<String,Integer> c: cs) {
			Vehicle v = map.getVehicle(c.getFirst());
			if (v == null) throw new IllegalArgumentException("vehicle doesn't exist");
			v.setContClass(c.getSecond());
		}
	} 

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("Chage CO2 class: ["); 
		for(int i = 0; i < this.cs.size()-1; ++i)
			str.append("(" + this.cs.get(i).getFirst() + "," + this.cs.get(i).getSecond() + ") , ");
		str.append("(" + this.cs.get(this.cs.size() - 1).getFirst() + "," + this.cs.get(this.cs.size()-1).getSecond() + ")]"); 
		return str.toString();
		
	}
}
