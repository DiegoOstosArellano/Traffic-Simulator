package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.model.Road;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;

	private int _status; //para controlar la salida de nuestro cuadro de diálogo
	private JComboBox<Road> _roads;
	private DefaultComboBoxModel<Road> _RoadModel;
	private JComboBox<Weather> weatherCombo;
	private Weather weather;
	private static final int Time0 = 1;
	private int ticks; 
	
	public ChangeWeatherDialog(Frame parent) {
		super(parent, true); 
		initGUI();
	}
	
	private void initGUI() {

		_status = 0; //Por si cierro la ventana sin hacer clic en un botón

		setTitle("Change Road Weather");
		JPanel mainPanel = new JPanel();
		//mainPanel.setLayout(new BorderLayout());
		setContentPane(mainPanel);

		JLabel helpMsg = new JLabel("Schedule an event to change the weather of a road after a given number of simulation ticks from now.");
		helpMsg.setAlignmentX(LEFT_ALIGNMENT);
		mainPanel.add(helpMsg, BorderLayout.NORTH);
		mainPanel.add(Box.createRigidArea(new Dimension(10, 20)));//para que no salga 
											//pegado al botón de arriba

		JPanel viewsPanel = new JPanel();
		viewsPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		setContentPane(viewsPanel);
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		
		JLabel RoadMsg = new JLabel("Road: ");
		RoadMsg.setAlignmentX(LEFT_ALIGNMENT);
		viewsPanel.add(RoadMsg);
		_RoadModel = new DefaultComboBoxModel<>();
		_roads = new JComboBox<>(_RoadModel);
		/*_roads.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Road> roads = new ArrayList<Road>();
				for (Road r: mapa.getRoads()) {
					roads.add(r);
				}
				road = openR(roads);
			}
		});*/
		viewsPanel.add(_roads);
		
		JLabel weatherMsg = new JLabel("Weather: ");
		weatherMsg.setAlignmentX(LEFT_ALIGNMENT);
		viewsPanel.add(weatherMsg);
		Weather[] valores = Weather.values(); //TODO optimizar 
		weatherCombo = new JComboBox<Weather>(valores);
		weatherCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				weather = (Weather) weatherCombo.getSelectedItem();
			}
		});
		viewsPanel.add(weatherCombo);
		
		JLabel ticksMsg = new JLabel("Ticks: ");
		ticksMsg.setAlignmentX(LEFT_ALIGNMENT);
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(Time0, 1, 5000, 10));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ticks = (Integer) spinner.getValue();
			}
		});
		viewsPanel.add(ticksMsg);
		
		JPanel cancelokPanel = new JPanel();
		cancelokPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		setContentPane(cancelokPanel);
		mainPanel.add(viewsPanel, BorderLayout.SOUTH);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeWeatherDialog.this.dispose();
			}
		});
		cancelokPanel.add(btnCancel);
		cancelokPanel.setBorder(BorderFactory.createEmptyBorder(500, 25, 25, 25)); //TODO
		
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 1;
				mainPanel.setVisible(false);
			}
		});
		cancelokPanel.add(btnOk);
		
		//no se si aqu� hay que indicar que hemos metido un nuevo observador o keloke
	}
	
	public int openR(List<Road> roads) { //si se ve raro cambiarlo xd
		_RoadModel.removeAllElements();
		for (Road r : roads)
			_RoadModel.addElement(r);
		
		setLocation(getParent().getLocation().x + 100, getParent().getLocation().y + 100);
		// Probad por ejemplo con +50

		setVisible(true); 

		return _status;
	}

	public Weather getWeather() {
		return weather;
	}

	public int getTicks() {
		return ticks;
	}

	Road getRoad() {
		return (Road) _RoadModel.getSelectedItem();// se lo pido al modelo
	}
	
}
