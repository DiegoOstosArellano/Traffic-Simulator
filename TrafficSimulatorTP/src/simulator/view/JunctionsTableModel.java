package simulator.view;


import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Junction> _junctions;
	private String[] junctionsColumns = {"Id", "Green", "Queues"};
	
	public JunctionsTableModel(Controller controller) {
		this._junctions = null; 
		controller.addObserver(this);
	}
	@Override
	public int getColumnCount() {
		return this.junctionsColumns.length;
	}

	@Override
	public int getRowCount() {
		return _junctions == null ? 0 : _junctions.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
		case 0: return this._junctions.get(rowIndex).getId();
		case 1: int i =  this._junctions.get(rowIndex).getGreenLightIndex();
				if(i == -1)
					return "NONE";
				else return i; 
		case 2: return  this._junctions.get(rowIndex).queuesToString();  
		
		default: return null; 
		}
	}
	@Override
	public String getColumnName(int c) {
		return this.junctionsColumns[c]; 
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this._junctions = map.getJunctions(); 
		this.fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this._junctions = map.getJunctions(); 
		this.fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this._junctions = map.getJunctions(); 
		this.fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this._junctions = map.getJunctions(); 
		this.fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this._junctions = map.getJunctions(); 
		this.fireTableDataChanged();
	}

	@Override
	public void onError(String err) {}

}
