package simulator.model;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
public class Vehicle extends SimulatedObject {

	private List<Junction> itinerary;
	private int maxSpeed;
	private int contClass; 
	private int CurrentSpeed; 
	
	
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		super(id);
		this.itinerary = itinerary; 
		this.maxSpeed = maxSpeed;
		
	} 
	@Override
	void advance(int time) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public JSONObject report() {
		// TODO Auto-generated method stub
		return null;
	}

}

/*
� 	itinerary (de tipo List<Junction>): una lista de cruces que representa
	el itinerario del veh�culo.
� 	maximum speed (de tipo int): la velocidad m�xima a la cual puede
	viajar el veh�culo.
� 	current speed (de tipo int): la velocidad actual a la que est�
	circulando el veh�culo.
� 	status (de tipo enumerado VehicleStatus � ver paquete
	�simulator.model�): el estado del veh�culo que puede ser Pending
	(todav�a no ha entrado a la primera carretera de su itinerario),
	Traveling (circulando sobre una carretera), Waiting (esperando en un
	cruce), or Arrived (ha completado su itinerario). 
�	road (de tipo Road): la carretera sobre la que el coche est�
	circulando; debe ser null en caso de que no est� en ninguna
	carretera.
� 	location (de tipo int): la posici�n del veh�culo sobre la carretera
	sobre la que est� circulando (la distancia desde el comienzo de la
	carretera; el comienzo de la carretera es la localizaci�n 0).
� 	contamination class (de tipo int): un n�mero entre 0 y 10 (ambos
	inclusive) que se usa para calcular el CO2 emitido por el veh�culo en
	cada paso de la simulaci�n. Es equivalente a los distintivos
	medioambientales que se llevan los veh�culos en el mundo real.
� 	total contamination (de tipo int): el total de CO2 emitido por el
	veh�culo durante toda su trayectoria recorrida.
� 	total travelled distance (de tipo int): la distancia total recorrida por el
	veh�culo.
 * */
