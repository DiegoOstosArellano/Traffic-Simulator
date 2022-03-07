package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy{
	private int ticks;
	
	public MostCrowdedStrategy (int timeSlot){
		ticks = timeSlot;
	}

	// Si la lista de colas es vacia, trivialmente ningun semaforo se pondra en verde, por lo que devolvemos -1.
	// Si todos los semaforos estan en rojo, pone en verde el semaforo de la carretera entrante con la cola mas larga.
	// si currTime - lastSwitchingTime < ticks no cambia el semaforo en verde.
	// En otro caso pone en verde el semaforo de la carretera entrante con la cola mas larga, realizando una busqueda circular
	// desde la posicion (currGreen + 1) % qs.size()
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,int currTime) {
		if (roads.size() == 0) return -1;
		else if (currGreen == -1) {
			int indice_mayor = 0;
			for (int i = 0; i < qs.size(); ++i) {
				if (qs.get(i).size() > qs.get(indice_mayor).size()) {
					indice_mayor = i;
				}
			}
			return indice_mayor;
		}
		else if (currTime - lastSwitchingTime < ticks) return currGreen;
		else {
			int indice_mayor = (currGreen + 1) % qs.size(); 
			for (int i = (currGreen + 1) % qs.size() ; i < (qs.size() + currGreen + 1); ++i) { 
				if (qs.get(i % qs.size()).size() > qs.get(indice_mayor).size()) {
					indice_mayor = i;
				}
			}
			return indice_mayor;
		}
	}
}
