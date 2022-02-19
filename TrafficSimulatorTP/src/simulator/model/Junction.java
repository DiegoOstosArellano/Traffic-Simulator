package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject{
	
	private List<Road> carreteras_entrantes;
	private Map<Junction, Road> carreteras_salientes;
	private List<List<Vehicle>> lista_colas;
	private  Map<Road,List<Vehicle>> carretera_cola;
	private int indice_semaforo_verde; //-1 si todas las carreteras entrantes tienen el semáforo en rojo
	private int ultimo_paso_semaforo;
	private LightSwitchStrategy estrategia_semaforo;
	private DequeuingStrategy estrategia_eliminar;
	private int x, y;

	Junction(String id, LightSwitchStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor){
		super(id);
		if (lsStrategy == null) throw new IllegalArgumentException("there must be a light switch strategy");
		if (dqStrategy == null) throw new IllegalArgumentException("there must be a dequeing strategy");
		if (xCoor < 0 || yCoor < 0) throw new IllegalArgumentException("Coords must be postive numbers");
		
		carreteras_entrantes = new ArrayList<Road>();
		carreteras_salientes = new HashMap<Junction, Road>();
		lista_colas = new ArrayList<>(); //ArrayList<List>>
		carretera_cola = new HashMap<Road, List<Vehicle>>();
		indice_semaforo_verde = -1; 
		ultimo_paso_semaforo = 0;
		estrategia_semaforo = lsStrategy;
		estrategia_eliminar = dqStrategy;
		x = xCoor;
		y = yCoor;
	}
	
	public void addIncommingRoad(Road r) {
		if (r.getDest() != this) throw new IllegalArgumentException("the road must be an entrance road");
		
		carreteras_entrantes.add(r);
		List<Vehicle> l = new LinkedList<Vehicle>();
		lista_colas.add(l);
		carretera_cola.put(r, l);
	}
	
	public void addOutGoingRoad(Road r) {
		if (r.getSrc() != this) throw new IllegalArgumentException("the road must be a source road");
		if (carreteras_salientes.containsKey(r.getDest())) throw new IllegalArgumentException("there is already one road this destination");
		
		carreteras_salientes.put(r.getDest(), r);
	}
	
	void enter(Vehicle v) {
		Road r = v.getRoad();
		List<Vehicle> q = carretera_cola.get(r);
		q.add(v);
	}
	
	Road roadTo(Junction j) {
		return carreteras_salientes.get(j);
	}

	@Override
	public void advance(int time) {
		if (indice_semaforo_verde != - 1) {
			List<Vehicle> queu = lista_colas.get(indice_semaforo_verde);
			List<Vehicle> eliminados = estrategia_eliminar.dequeue(queu);
			for (Vehicle v: eliminados) {
				v.moveToNextRoad();
				queu.remove(v);
				//como es el mismo objeto solo se elimina una vez
			}
		}
		
		int indice = estrategia_semaforo.chooseNextGreen(carreteras_entrantes, lista_colas, indice_semaforo_verde, ultimo_paso_semaforo, time);
		if (indice_semaforo_verde != indice) {
			indice_semaforo_verde = indice;
			ultimo_paso_semaforo = time;
		}
	}

	@Override
	public JSONObject report() {
		JSONObject jo1 = new JSONObject();

		jo1.put("id", _id);
		if (indice_semaforo_verde == -1) jo1.put("green", "none");
		else jo1.put("green", carreteras_entrantes.get(indice_semaforo_verde).getId());
		
		JSONArray ja = new JSONArray();
		for (int i = 0; i < lista_colas.size(); ++i) {
			JSONObject jo2 = new JSONObject();
			jo2.put("road", carreteras_entrantes.get(i).getId());
			JSONArray ja2 = new JSONArray();
			for (Vehicle v: lista_colas.get(i)) {
				ja2.put(v.getId());
			}
			jo2.put("vehicles", ja2);
			ja.put(jo2);
		}
		jo1.put("queues", ja);
		
		return jo1;
	}

	int getX() {
		return x;
	}

    int getY() {
		return y;
	}
	
}
