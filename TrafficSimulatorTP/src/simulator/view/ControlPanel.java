package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5342514288655490072L;
	private Controller _ctrl;
	private JFileChooser fc;
	private RoadMap mapa;
	private boolean _stopped;
	private int ticks;
	private int time;
	private final static int time0 = 1;
	private JButton loadButton;
	private JButton CO2Button;
	private JButton WeatherButton;
	private JButton RunButton;
	private JButton StopButton;
	private JButton ExitButton;
	public ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_ctrl.addObserver(this);
		_stopped = true;
		fc = new JFileChooser(); 
		initGUI();
	}
	
	private void initGUI() {
		this.setLayout(new BorderLayout());
		JToolBar barra = new JToolBar();
		add(barra, BorderLayout.NORTH);

		loadButton = new JButton();
		loadButton.setActionCommand("load");
		loadButton.setToolTipText("Load a file");
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ret = fc.showOpenDialog(ControlPanel.this);
				//seg˙n salga yo de esta ventana, "ret" tomar· un valor u otro
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
		
		loadButton.setIcon(new ImageIcon("resources/icons/open.png"));
		barra.add(loadButton);
		barra.addSeparator();// probadlo con esto y sin esto
		
		
		CO2Button = new JButton();
		CO2Button.setActionCommand("Change CO2");
		CO2Button.setToolTipText("Change CO2");
		CO2Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select_vehicle();
			}
		});
		CO2Button.setIcon(new ImageIcon("resources/icons/co2class.png"));
		barra.add(CO2Button);
		
		WeatherButton = new JButton();
		WeatherButton.setActionCommand("Change weather");
		WeatherButton.setToolTipText("Change weather");
		WeatherButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select_weather();
			}
		});
		WeatherButton.setIcon(new ImageIcon("resources/icons/weather.png"));
		barra.add(WeatherButton);
		barra.addSeparator();
		
		RunButton = new JButton();
		RunButton.setActionCommand("Run simulation");
		RunButton.setToolTipText("Run simulation with n ticks");
		RunButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped = false;
				enableToolBar(false);
				run_sim(ticks);
			}
		});
		RunButton.setIcon(new ImageIcon("resources/icons/run.png"));
		barra.add(RunButton);
		
		StopButton = new JButton();
		StopButton.setActionCommand("Stop simulation");
		StopButton.setToolTipText("Stop simulation");
		StopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		StopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		barra.add(StopButton);
		barra.addSeparator();
		
		JLabel ticksMsg = new JLabel("Ticks: ");
		ticksMsg.setAlignmentX(LEFT_ALIGNMENT);
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(time0, 1, 5000, 1));
		spinner.setMaximumSize(new Dimension(40,40));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ticks = (Integer) spinner.getValue();
			}
		});
		barra.add(ticksMsg);
		barra.add(spinner);
		barra.add(Box.createRigidArea(new Dimension(600, 20)));
		barra.addSeparator(); //TODO ver como darle m·s separaciÛn
		ExitButton = new JButton();
		ExitButton.setActionCommand("Exit simulation");
		ExitButton.setToolTipText("Exit simulation");
		ExitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showOptionDialog(ControlPanel.this,
						"Are sure you want to quit?", "Quit",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (n == 0) System.exit(0);
			}
		});
		ExitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		barra.add(ExitButton);
		
		//this.pack();
		this.setVisible(true);
		
	}
	
	private void run_sim(int n) {
        if ( n>0 && !_stopped ) {
            try {
                _ctrl.run(1); //TODO
            } catch (Exception e) {
                JOptionPane.showMessageDialog(ControlPanel.this, "An error occured. " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                _stopped = true;
                return;
            }
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run() {
                    run_sim(n-1);
                }
            });
        } else {
        	enableToolBar(true); 
            _stopped = true;
        }
    }
	private void stop() {
		_stopped = true;
	}
	
	protected void select_vehicle() {
		Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);

		ChangeCO2ClassDialog dialog = new ChangeCO2ClassDialog(parent);

		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		for (Vehicle v: mapa.getVehicles()) {
			vehicles.add(v);
		}

		int status = dialog.openV(vehicles); //Abrimos el cuadro de di√°logo 
								//con nuestros datos, sin esta l√≠nea
								//no es visible porque se hace visible en el open

		if (status == 0) {
			//System.out.println("Canceled");
		} else {
			//Poner en el action performanced
			if (dialog.getVehicle() != null) {
				List<Pair<String,Integer>> ws = new ArrayList<Pair<String,Integer>>();
				Pair<String,Integer> auxPair = new Pair<String,Integer>(dialog.getVehicle().getId(), dialog.getCO2());
				ws.add(auxPair);
				_ctrl.addEvent(new NewSetContClassEvent(dialog.getTicks() + time, ws));
			}
		}
	}
	protected void select_weather() {
		Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);

		ChangeWeatherDialog dialog = new ChangeWeatherDialog(parent);

		List<Road> roads = new ArrayList<Road>();
		for (Road r: mapa.getRoads()) {
			roads.add(r);
		}

		int status = dialog.openR(roads); 

		if (status == 0) {
			
		} else {
			if (dialog.getRoad() != null) {
				List<Pair<String,Weather>> ws = new ArrayList<Pair<String,Weather>>();
				Pair<String,Weather> auxPair = new Pair<String,Weather>(dialog.getRoad().getId(), dialog.getWeather());
				ws.add(auxPair);
				_ctrl.addEvent(new SetWeatherEvent(dialog.getTicks() + time, ws)); 
			}
		}
	}
	private void enableToolBar(boolean visible) {
		this.loadButton.setEnabled(visible);
		this.CO2Button.setEnabled(visible);
		this.WeatherButton.setEnabled(visible);
		this.RunButton.setEnabled(visible);
		this.ExitButton.setEnabled(visible);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		mapa = map;	
		this.time = time;
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		mapa = map;
		this.time = time;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		mapa = map;
		this.time = time;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		mapa = map;
		this.time = time;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		mapa = map;
		this.time = time;
	}

	@Override
	public void onError(String err) { 
	}
}
