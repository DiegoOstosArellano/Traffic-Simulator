package simulator.model;

import java.awt.RenderingHints;
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
	private int indice_semaforo_verde; //-1 si todas las carreteras entrantes tienen el semÃ¡foro en rojo
	private int ultimo_paso_semaforo;
	private LightSwitchingStrategy estrategia_semaforo;
	private DequeuingStrategy estrategia_eliminar;
	private int x, y;

	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor){
		super(id);
		if (lsStrategy == null) throw new IllegalArgumentException("there must be a light switch strategy");
		if (dqStrategy == null) throw new IllegalArgumentException("there must be a dequeing strategy");
		if (xCoor < 0 || yCoor < 0) throw new IllegalArgumentException("Coords must be postive numbers");
		
		carreteras_entrantes = new ArrayList<Road>();
		carreteras_salientes = new HashMap<Junction, Road>();
		lista_colas = new ArrayList<List<Vehicle>>(); //ArrayList<List>>
		carretera_cola = new HashMap<Road, List<Vehicle>>();
		indice_semaforo_verde = -1; 
		ultimo_paso_semaforo = 0;
		estrategia_semaforo = lsStrategy;
		estrategia_eliminar = dqStrategy;
		x = xCoor;
		y = yCoor;
	}
	
	// primero comprobamos que el cruce destino de la carretera r es igual al cruce actual, en caso contrario lanzamos una excepcion.
	// En caso de no lanzarla añadimos la carretera r al final de la lista de carreteras entrantes. Se crea una cola para r, la cual es añadidida
	// al final de la lista de colas, a su vez, añadimos l con la llave r al mapa carretera_cola.
	void addIncommingRoad(Road r) { //r carretera que estoy creando 
		if (!r.getDest().equals(this)) throw new IllegalArgumentException("the road must be an entrance road"); 
		carreteras_entrantes.add(r);
		List<Vehicle> l = new LinkedList<Vehicle>();
		lista_colas.add(l);
		carretera_cola.put(r, l);
		
	}
	// primero comprobamos que el cruce de salida de la carretera r es igual al cruce actual, en caso contrario lanzamos una excepcion.
	// Tambien comprobamos que ninguna otra carretera va a este cruce. Si todo va bien se añade el par j,r al mapa de carreteras salientes.
	void addOutGoingRoad(Road r) {
		if (r.getSrc() != this) throw new IllegalArgumentException("the road must be a source road");
		if (carreteras_salientes.containsKey(r.getDest())) throw new IllegalArgumentException("there is already one road this destination");
		
		carreteras_salientes.put(r.getDest(), r);
	}
	
	// añadimos el vehiculo v a la cola de carretera r, siendo r la carretera actual de v
	void enter(Vehicle v) {
		Road r = v.getRoad();
		List<Vehicle> q = carretera_cola.get(r);
		q.add(v);
	}
	
	Road roadTo(Junction j) {
		return carreteras_salientes.get(j);
	}

	
	// Provoca el avance del cruce dependiendo de la estrategia.
	// si ningun semaforo esta en verde y la lista de colas no es vacia, movemos hacia la siguiente carretera el primer coche o todos los coches,
	// dependiendo de la estrategia dequeuing. Finalmente elegimos el proximo semaforo verde, consecuentemente cambiamos el indice del semaforo.
	@Override
	void advance(int time) {
		if (indice_semaforo_verde != - 1 && !lista_colas.isEmpty()) {
			List<Vehicle> queu = lista_colas.get(indice_semaforo_verde);
			List<Vehicle> eliminados = estrategia_eliminar.dequeue(queu);
			
			while (!eliminados.isEmpty()) {
				eliminados.get(0).moveToNextRoad();
				eliminados.remove(eliminados.get(0));
				this.lista_colas.get(indice_semaforo_verde).remove(0);
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

	public int getX() {
		return x;
	}

    public int getY() {
		return y;
	}

	public int getGreenLightIndex() {
		return indice_semaforo_verde;
	}

	public List<Road> getInRoads() {
		return carreteras_entrantes;
	}
	
}
