package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel  extends AbstractTableModel implements TrafficSimObserver{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Road> _roads;
	private String[] roadsColumns = {"Id", "Length", "Weather","Max Speed", "Speed Limit", "Total CO2", "Distance"};
	
	public RoadsTableModel(Controller controller) {
		this._roads = null; 
		controller.addObserver(this);
	}
	@Override
	public int getColumnCount() {
		return this.roadsColumns.length;
	}

	@Override
	public int getRowCount() {
		return _roads == null ? 0 : _roads.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
			case 0: return this._roads.get(rowIndex).getId();
			case 1: return this._roads.get(rowIndex).getLength();
			case 2: return this._roads.get(rowIndex).getWeather().toString(); //Hacerrrrr lo he creado 
			case 3: return this._roads.get(rowIndex).getMaxSpeed(); 
			case 4: return this._roads.get(rowIndex).getSpeedLimit();
			case 5: return this._roads.get(rowIndex).getTotalCO2();
			case 6: return this._roads.get(rowIndex).getContLimit();
			default: return null; 
		}
	}
	@Override
	public String getColumnName(int c) {
		return this.roadsColumns[c]; 
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this._roads = map.getRoads(); 
		this.fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this._roads = map.getRoads(); 
		this.fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this._roads = map.getRoads(); 
		this.fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this._roads = map.getRoads(); 
		this.fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this._roads = map.getRoads(); 
		this.fireTableDataChanged();
	}

	@Override
	public void onError(String err) {}

}
