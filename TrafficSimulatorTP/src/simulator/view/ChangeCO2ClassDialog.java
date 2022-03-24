package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog{
	
	private static final long serialVersionUID = 1L;

	private int _status; //para controlar la salida de nuestro cuadro de diálogo
	private JComboBox<Vehicle> _vehicles;
	private DefaultComboBoxModel<Vehicle> _vehiclesModel;
	
	public ChangeCO2ClassDialog(Frame parent) {
		super(parent, true); //el segundo argumento indica que la ventana es modal,e.d.,
							//que hasta que no acepte o cancele no me deja interactuar
							//con la anterior.
		initGUI();
	}
	
	private void initGUI() {

		_status = 0; //Por si cierro la ventana sin hacer clic en un botón

		setTitle("Change CO2 Class");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		JLabel helpMsg = new JLabel("Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.");
		helpMsg.setAlignmentX(LEFT_ALIGNMENT);
		mainPanel.add(helpMsg);
		mainPanel.add(Box.createRigidArea(new Dimension(10, 20)));//para que no salga 
											//pegado al botón de arriba

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel viewspanel = new JPanel(); 
		 
		JComboBox boton1 = new JComboBox();
		JButton boton2 = new JButton("Pulsa para cerrar la aplicacci�n");

		viewspanel.getContentPane().setLayout(new FlowLayout());

		viewspanel.getContentPane().add(new JLabel("Vehicle: "));
		this.getContentPane().add(boton1);
		
		this.getContentPane().add(new JLabel("Etiqueta 2"));
		this.getContentPane().add(boton2);
		
		
		
		
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(viewsPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(10, 20)));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);

		//Creamos el modelo y luego se lo paso al constructor del JComboBox
		//análogo al JTable, recordáis?.
		//Pero en este caso el modelo se encarga de refrescar los datos del JCombo
		//si los datos cambian
		_dishesModel = new DefaultComboBoxModel<>();
		_dishes = new JComboBox<>(_dishesModel);

		viewsPanel.add(_dishes);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0; 
				MyDialogWindow.this.setVisible(false);//ya no se ve el cuadro 
													//de diálogo
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Observad que le pido las cosas al modelo, es quien realmente tiene 
				//los datos, el JComboBox es una representación visual del modelo 
				//que hemos creado.
				System.out.println("Has hecho clic en ok");
				if (_dishesModel.getSelectedItem() != null) {
					_status = 1;
					MyDialogWindow.this.setVisible(false);
					//ya no es visible;
				}
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);//no permite al usuario agrandar esta ventana
		setVisible(false);//en el constructor no se hace visible todavía
	}
	
}
