package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.*;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver{
	private Controller _ctrl;
	private JFileChooser fc;
	
	public ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_ctrl.addObserver(this);
		initGUI();
	}
	
	private void initGUI() {
		JToolBar barra = new JToolBar();
		add(barra, BorderLayout.NORTH);

		JButton load = new JButton();
		load.setActionCommand("load");
		load.setToolTipText("Load a file");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ret = fc.showOpenDialog(ControlPanel.this);
				//según salga yo de esta ventana, "ret" tomará un valor u otro
				if (ret == JFileChooser.APPROVE_OPTION) {
					try {
						_ctrl.reset();
						_ctrl.loadEvents(new FileInputStream(fc.getSelectedFile()));
					}catch (Exception ex) { //hay que llamar a onerror???
						JOptionPane.showMessageDialog(ControlPanel.this,
								"Ha ocurrido un error.");
					}
				}
		}});
		load.setIcon(new ImageIcon("icons/open.png"));
		barra.add(load);
		barra.addSeparator();// probadlo con esto y sin esto
		
		/*
		JButton save = new JButton();
		save.setActionCommand("save");
		save.setToolTipText("Save a file");
		save.addActionListener(this);
		save.setIcon(new ImageIcon("icons/save.png"));
		barra.add(save);
		barra.addSeparator();

		String[] names = { "Times Roman", "Arial", "Comic" };
		JComboBox<String> combo = new JComboBox<String>(names);
		combo.setSelectedIndex(1);
		combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				textArea.append("Has seleccionado la opción: " + combo.getSelectedItem() + "\n");
				
			}
		});
		combo.setMaximumSize(new Dimension(150, 50));
		barra.add(combo);
		
		//Lo que ponga después de esta línea sale desplazado a la derecha
		barra.add(Box.createGlue());

		// con lo que metemos aquí, en este ejemplo no hacemos nada en concreto
		JTextField text = new JTextField(8);
		text.setMaximumSize(new Dimension(60, 50));
		
		barra.add(text);
		
		textArea = new JTextArea(10,60);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		mainPanel.add(scrollPane, BorderLayout.CENTER); // observad que cuando se llena la zona,
									// salen las barras


		this.setContentPane(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);*/
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
}
