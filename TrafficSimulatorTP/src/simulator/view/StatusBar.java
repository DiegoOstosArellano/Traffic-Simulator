package simulator.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver{
	private Controller controller;
	private int time;
	private Event eventoActual;
	
	public StatusBar(Controller ctrl) {
		controller = ctrl;
		initGUI();
	}
	
	private void initGUI() {
		JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel timeLabel = new JLabel("Time: ");
		timePanel.add(timeLabel);
		
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.time = time;
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.time = time;
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.time = time;
		eventoActual = e;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.time = time;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.time = time;
		
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
