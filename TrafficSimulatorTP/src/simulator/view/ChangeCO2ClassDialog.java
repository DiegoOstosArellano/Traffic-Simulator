package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog{
	
	private static final long serialVersionUID = 1L;

	private int _status; //para controlar la salida de nuestro cuadro de diálogo
	private JComboBox<Vehicle> _vehicles;
	private DefaultComboBoxModel<Vehicle> _vehiclesModel;
	private JComboBox<Integer> CO2Combo;
	private Integer CO2;
	private int Time0 = 1;
	private int ticks; 
	
	public ChangeCO2ClassDialog(Frame parent) {
		super(parent, true); 
		initGUI();
	}
	
	private void initGUI() {

		_status = 0; //Por si cierro la ventana sin hacer clic en un botón

		setTitle("Change CO2 Class");
		JPanel mainPanel = new JPanel();
		//mainPanel.setLayout(new BorderLayout());
		setContentPane(mainPanel);

		JLabel helpMsg = new JLabel("Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.");
		helpMsg.setAlignmentX(LEFT_ALIGNMENT);
		mainPanel.add(helpMsg, BorderLayout.NORTH);
		mainPanel.add(Box.createRigidArea(new Dimension(10, 20)));//para que no salga 
											//pegado al botón de arriba

		JPanel viewsPanel = new JPanel();
		viewsPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		setContentPane(viewsPanel);
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		
		JLabel VehicleMsg = new JLabel("Vehicle: ");
		VehicleMsg.setAlignmentX(LEFT_ALIGNMENT);
		viewsPanel.add(VehicleMsg);
		_vehiclesModel = new DefaultComboBoxModel<>();
		_vehicles = new JComboBox<>(_vehiclesModel);
		/*_vehicles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Vehicle> vehicles = new ArrayList<Vehicle>();
				for (Vehicle v: mapa.getVehicles()) {
					vehicles.add(v);
				}
				vehicle = openV(vehicles);
			}
		});*/
		viewsPanel.add(_vehicles);
		
		JLabel CO2Msg = new JLabel("CO2 Class: ");
		CO2Msg.setAlignmentX(LEFT_ALIGNMENT);
		viewsPanel.add(CO2Msg);
		Integer[] valores = new Integer[11];
		for (int i = 0; i <= 10; ++i) {
			valores[i] = i; 
		}
		CO2Combo = new JComboBox<Integer>(valores);
		CO2Combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//lblTexto.setText((Integer) CO2Combo.getSelectedItem());
				CO2 = (Integer) CO2Combo.getSelectedItem();
			}
		});
		viewsPanel.add(CO2Combo);
		
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
				ChangeCO2ClassDialog.this.dispose();
			}
		});
		cancelokPanel.add(btnCancel);
		cancelokPanel.setBorder(BorderFactory.createEmptyBorder(500, 25, 25, 25));
		
		
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
	
	public int openV(List<Vehicle> vehicles) { //si se ve raro cambiarlo xd
		_vehiclesModel.removeAllElements();
		for (Vehicle v : vehicles)
			_vehiclesModel.addElement(v);
		
		setLocation(getParent().getLocation().x + 100, getParent().getLocation().y + 100);
		// Probad por ejemplo con +50

		setVisible(true); 

		return _status;
	}

	public Integer getCO2() {
		return CO2;
	}

	public int getTicks() {
		return ticks;
	}

	Vehicle getVehicle() {
		return (Vehicle) _vehiclesModel.getSelectedItem();// se lo pido al modelo
	}
	
}
