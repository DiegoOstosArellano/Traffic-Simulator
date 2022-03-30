package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Vehicle> _vehicles;
	private String[] vehiclesColumns = {"Id", "Location", "Iterinary", "CO2 Class", "Max Speed", "Speed", "Total CO2", "Distance"};
	
	public VehiclesTableModel(Controller controller) {
		this._vehicles = null; 
		controller.addObserver(this);
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this._vehicles = map.getVehicles(); 
		this.fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this._vehicles = map.getVehicles(); 
		this.fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this._vehicles = map.getVehicles(); 
		this.fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this._vehicles = map.getVehicles(); 
		this.fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this._vehicles = map.getVehicles(); 
		this.fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getColumnCount() {
		return this.vehiclesColumns.length;
	}

	@Override
	public int getRowCount() {
		return _vehicles == null ? 0 : _vehicles.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch(col) {
			case 0: return this._vehicles.get(row).getId();
			case 1: return this._vehicles.get(row).getLocation();
			case 2: return this._vehicles.get(row).getItineraryString(); //Hacerrrrr lo he creado 
			case 3: return this._vehicles.get(row).getContClass(); 
			case 4: return this._vehicles.get(row).getMaxSpeed();
			case 5: return this._vehicles.get(row).getSpeed(); 
			case 6: return this._vehicles.get(row).getTotalCO2(); 
			case 7: return this._vehicles.get(row).getDistance(); 
			default: return null; 
		}
			 
	}

	@Override
	public String getColumnName(int c) {
		return this.vehiclesColumns[c]; 
	}

}
