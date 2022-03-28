package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog{
	
	private static final long serialVersionUID = 1L;

	private int _status; //para controlar la salida de nuestro cuadro de diÃ¡logo
	private JComboBox<Vehicle> _vehicles;
	private DefaultComboBoxModel<Vehicle> _vehiclesModel;
	private JComboBox<Integer> CO2Combo;
	private Integer CO2;
	private int Time0 = 1;
	private int ticks = 1; 
	
	public ChangeCO2ClassDialog(Frame parent) {
		super(parent, true); 
		initGUI();
	}
	
	private void initGUI() {

		_status = 0; //Por si cierro la ventana sin hacer clic en un botÃ³n

		setTitle("Change CO2 Class");
		
		//Creamos un panel principal donde meter cada uno de los paneles secundarios
		JPanel mainPanel = new JPanel(new BorderLayout()); 
		
		
		JPanel messagePanel = new JPanel(); 
		messagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		messagePanel.setPreferredSize(new Dimension(180, 40));
		
		//Configuramos las etiquetas del messagePanel
		JLabel upLabel = new JLabel("Schedule an event to change the CO2 class of a vehicle");
		JLabel downLabel = new JLabel("after a given number of simulation ticks from now.");
		messagePanel.add(upLabel);
		messagePanel.add(downLabel); 
		setContentPane(messagePanel); 
		
		//Ponemos el panel de mensaje en la parte norte del mainPanel
		mainPanel.add(messagePanel, BorderLayout.NORTH); 
		
		//Creamos el viewsPanel para poner la linea de enmedio
		JPanel viewsPanel = new JPanel();
		viewsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		 
		//Creamos un vehiclesPanel para incluir el label y el comboBox
		JPanel vehiclesPanel = new JPanel();
		vehiclesPanel.setLayout(new BoxLayout(vehiclesPanel,BoxLayout.X_AXIS));
		 
		//Creamos el label y la lista de vehiculos para el vehiclePanel
		JLabel vehicleLable = new JLabel("Vehicle: ");
		vehiclesPanel.add(vehicleLable); 
		this._vehiclesModel = new DefaultComboBoxModel<>();
		_vehicles = new JComboBox<>(_vehiclesModel);
		//TODO como hacer para que se seleccionen los vehicles
		vehiclesPanel.add(_vehicles);
		
		//Añadimos el vehiclePanel al panel de viewsPanel
		viewsPanel.add(vehiclesPanel);
		
		//Creamos el panel de contClass
		JPanel contPanel = new JPanel(); 
		contPanel.setLayout(new BoxLayout(contPanel,BoxLayout.X_AXIS));
		
		//Creamos etiqueta y array de contClass(1-10)
		JLabel contLabel = new JLabel("CO2 Class: ");
		contPanel.add(contLabel); 
		Integer[] valores = new Integer[11];
		for (int i = 0; i <= 10; ++i) {
			valores[i] = i; 
		}
		CO2Combo = new JComboBox<Integer>(valores);
		CO2Combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CO2 = (Integer) CO2Combo.getSelectedItem();
			}
		});
		contPanel.add(CO2Combo);
		
		//Añadimos el contPanel al panel de viewsPanel
		viewsPanel.add(contPanel);
		
		//Creamos ticksPanel 
		JPanel ticksPanel = new JPanel(); 
		ticksPanel.setLayout(new BoxLayout(ticksPanel,BoxLayout.X_AXIS));
		
		//Añadimos etiqueta y spinner para los ticks
		JLabel ticksLabel = new JLabel("Ticks: ");
		ticksPanel.add(ticksLabel); 
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(Time0, 1, Integer.MAX_VALUE, 1));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ticks = (Integer) spinner.getValue();
			}
		});
		ticksPanel.add(spinner);
		spinner.setPreferredSize(new Dimension(60, 20));
		
		//Añadimos el ticksPanel al viesPanel
		viewsPanel.add(ticksPanel);
		
		//Añadimos el panel que se localiza en el center al mainPanel
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		
		//Creamos okCancelPanel para los dos botones de la parte inferior (South) de la consola
		JPanel okCancelPanel = new JPanel(); 
		okCancelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		 
		//Creamos boton para cancel 
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeCO2ClassDialog.this.dispose();
			}
		});
		
		//Lo añadimos al panel okCancelPanel
		okCancelPanel.add(cancel);
		
		//Creamos boton para ok
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Faltaria diferencia si se ha seleccionado algo 
				_status = 1;
				setVisible(false);
			}
		});
		//Lo añadimos al panel okCancelPanel
		okCancelPanel.add(btnOk);
		
		//Añadimos okCancelPanel al mainPanel
		mainPanel.add(okCancelPanel, BorderLayout.SOUTH);
		
		setContentPane(mainPanel);
		setMinimumSize(new Dimension(400, 200)); 
		setPreferredSize(new Dimension(400, 200)); 
		setVisible(false); 
		/*
		JPanel mainPanel = new JPanel();
		//mainPanel.setLayout(new BorderLayout());
		setContentPane(mainPanel);

		JLabel helpMsg = new JLabel("Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.");
		helpMsg.setAlignmentX(LEFT_ALIGNMENT);
		mainPanel.add(helpMsg, BorderLayout.NORTH);
		mainPanel.add(Box.createRigidArea(new Dimension(10, 20)));//para que no salga 
											//pegado al botÃ³n de arriba

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
		});
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
		
		//no se si aquí hay que indicar que hemos metido un nuevo observador o keloke
	*/
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
