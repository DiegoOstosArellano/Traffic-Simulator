package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int time;
	private JLabel timeLabel;
	private JLabel eventLabel;

	public StatusBar(Controller ctrl) {
		ctrl.addObserver(this);
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		timeLabel = new JLabel("Time: " + time);
		add(timeLabel);
		
		add(Box.createRigidArea(new Dimension(50, 20)));
		
		eventLabel = new JLabel();
		add(eventLabel);
		
		add(Box.createRigidArea(new Dimension(50, 20)));

		setVisible(true);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.time = time;
		this.timeLabel.setText("Time: " + time);
		this.eventLabel.setText("");
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.time = time;
		this.timeLabel.setText("Time: " + time);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.time = time;
		this.timeLabel.setText("Time: " + time);
		this.eventLabel.setText("Event added: " + e.toString());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.time = time;
		this.timeLabel.setText("Time: " + time);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.time = time;

	}

	@Override
	public void onError(String err) {
		this.eventLabel.setText("An error occur: " + err); 
	}

}
