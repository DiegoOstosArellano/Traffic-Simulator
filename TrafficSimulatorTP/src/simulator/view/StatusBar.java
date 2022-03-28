package simulator.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller controller;
	private int time;
	private Event eventoActual;

	public StatusBar(Controller ctrl) {
		controller = ctrl;
		initGUI();
	}

	private void initGUI() {
		JToolBar barra = new JToolBar();
		add(barra, FlowLayout.LEFT); 
		
		JLabel timeLabel = new JLabel("Time: " + time); 
		barra.add(timeLabel);
		barra.addSeparator();
		if(eventoActual != null) {
			JLabel eventLabel = new JLabel("event added: " + eventoActual.toString());
			barra.add(eventLabel); 
		}
		this.setVisible(true);
		
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
