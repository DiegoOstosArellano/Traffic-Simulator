package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Event> _events;
	private String[] EventsColumns = {"Time", "Desc"};
	
	public EventsTableModel(Controller controller) {
		this._events = null;
		controller.addObserver(this);
		
	}


	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this._events = events;
		this.fireTableStructureChanged();
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this._events = events;
		this.fireTableStructureChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		this._events = events; 
		this.fireTableStructureChanged();
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this._events = events;
		this.fireTableStructureChanged();
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getColumnCount() {
		return this.EventsColumns.length;
	}

	@Override
	public int getRowCount() {
		return _events == null ? 0 : _events.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		
		if(col == 0)
			return this._events.get(row).getTime(); 
		else
			return this._events.get(row).toString(); 
		 
		
	}
	
	
	@Override
	public String getColumnName(int c) {
		return this.EventsColumns[c]; 
	}
}
