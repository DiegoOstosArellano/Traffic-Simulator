package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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

	private int _status; // para controlar la salida de nuestro cuadro de diÃ¡logo
	private JComboBox<Road> _roads;
	private DefaultComboBoxModel<Road> _RoadModel;
	private JComboBox<Weather> weatherCombo;
	private Weather weather = Weather.SUNNY;
	private static final int Time0 = 1;
	private int ticks = 1;

	public ChangeWeatherDialog(Frame parent) {
		super(parent, true);
		initGUI();
	}

	private void initGUI() {

		_status = 0; // Por si cierro la ventana sin hacer clic en un botÃ³n

		setTitle("Change Road Weather");

		// Creamos un panel principal donde meter cada uno de los paneles secundarios
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		messagePanel.setPreferredSize(new Dimension(180, 40));

		// Configuramos las etiquetas del messagePanel
		JLabel upLabel = new JLabel("Schedule an event to change the weather of a road");
		JLabel downLabel = new JLabel("after a given number of simulation ticks from now.");
		messagePanel.add(upLabel);
		messagePanel.add(downLabel);
		setContentPane(messagePanel);

		// Ponemos el panel de mensaje en la parte norte del mainPanel
		mainPanel.add(messagePanel, BorderLayout.NORTH);

		// Creamos el viewsPanel para poner la linea de enmedio
		JPanel viewsPanel = new JPanel();
		viewsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		// Creamos el label y la lista de vehiculos para el vehiclePanel
		JLabel roadsLabel = new JLabel("Road: ");
		viewsPanel.add(roadsLabel);
		this._RoadModel = new DefaultComboBoxModel<>();
		_roads = new JComboBox<>(_RoadModel);

		viewsPanel.add(_roads);

		// Creamos etiqueta y array de contClass(1-10)
		JLabel weatherLabel = new JLabel("Weather: ");
		viewsPanel.add(weatherLabel);
		Weather[] valores = Weather.values();
		weatherCombo = new JComboBox<Weather>(valores);
		weatherCombo.addActionListener(new ActionListener() {
		  @Override 
		  public void actionPerformed(ActionEvent e) { 
			  weather = (Weather)weatherCombo.getSelectedItem(); 
			  } 
		  });
		 

		viewsPanel.add(weatherCombo);

		// Añadimos etiqueta y spinner para los ticks
		JLabel ticksLabel = new JLabel("Ticks: ");
		viewsPanel.add(ticksLabel);
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(Time0, 1, Integer.MAX_VALUE, 1));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ticks = (Integer) spinner.getValue();
			}
		});
		viewsPanel.add(spinner);
		spinner.setPreferredSize(new Dimension(60, 20));

		// Añadimos el panel que se localiza en el center al mainPanel
		mainPanel.add(viewsPanel, BorderLayout.CENTER);

		// Creamos okCancelPanel para los dos botones de la parte inferior (South) de la
		// consola
		JPanel okCancelPanel = new JPanel();
		okCancelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		// Creamos boton para cancel
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeWeatherDialog.this.dispose();
			}
		});

		// Lo añadimos al panel okCancelPanel
		okCancelPanel.add(cancel);

		// Creamos boton para ok
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Faltaria diferencia si se ha seleccionado algo
				_status = 1;
				setVisible(false);
			}
		});
		// Lo añadimos al panel okCancelPanel
		okCancelPanel.add(btnOk);

		// Añadimos okCancelPanel al mainPanel
		mainPanel.add(okCancelPanel, BorderLayout.SOUTH);

		setContentPane(mainPanel);
		setMinimumSize(new Dimension(400, 200));
		setPreferredSize(new Dimension(400, 200));
		setVisible(false);
	}

	public int openR(List<Road> roads) { 
		_RoadModel.removeAllElements();
		for (Road r : roads)
			_RoadModel.addElement(r);

		setLocation(getParent().getLocation().x + 100, getParent().getLocation().y + 100);

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
