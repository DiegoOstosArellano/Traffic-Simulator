package simulator.model;

import java.util.List;

public class RoundRobinStrategy implements LightSwitchingStrategy{
	
	private int ticks;
	
	//tenemos que comprobar el timeSlot???
	public RoundRobinStrategy (int timeSlot){
		ticks = timeSlot;
	}

	// Si la lista de carreteras entrantes es vacia, trivialmente ningun semaforo se pondra en verde, por lo que devolvemos -1.
	// Si todos los semaforos estan en rojo, pone en verde el primer semaforo lista de carretras.
	// si currTime - lastSwitchingTime < ticks no cambia el semaforo en verde.
	// En otro caso pone en verde el semaforo de la carretera (currGreen + 1)%roads.size().
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,int currTime) {
		if (roads.size() == 0) return -1;
		else if (currGreen == -1) return 0;
		else if (currTime - lastSwitchingTime < ticks) return currGreen;
		else return (currGreen + 1)%roads.size();
	}
	
	
}
