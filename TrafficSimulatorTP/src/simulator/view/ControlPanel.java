package simulator.view;

import java.awt.BorderLayout;
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
	private final static int time0 = 1;
	
	public ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_ctrl.addObserver(this);
		_stopped = true;
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
		
		load.setIcon(new ImageIcon("icons/open.png"));
		barra.add(load);
		barra.addSeparator();// probadlo con esto y sin esto
		
		
		JButton CO2Botton = new JButton();
		CO2Botton.setActionCommand("Change CO2");
		CO2Botton.setToolTipText("Change CO2");
		CO2Botton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select_vehicle();
			}
		});
		CO2Botton.setIcon(new ImageIcon("icons/co2class.png"));
		barra.add(CO2Botton);
		
		JButton WeatherBotton = new JButton();
		WeatherBotton.setActionCommand("Change weather");
		WeatherBotton.setToolTipText("Change weather");
		WeatherBotton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select_weather();
			}
		});
		WeatherBotton.setIcon(new ImageIcon("icons/weather.png"));
		barra.add(WeatherBotton);
		barra.addSeparator();
		
		JButton RunBotton = new JButton();
		RunBotton.setActionCommand("Run simulation");
		RunBotton.setToolTipText("Run simulation with n ticks");
		RunBotton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped = false;
				run_sim(ticks);
			}
		});
		RunBotton.setIcon(new ImageIcon("icons/run.png"));
		barra.add(RunBotton);
		
		JButton StopBotton = new JButton();
		StopBotton.setActionCommand("Stop simulation");
		StopBotton.setToolTipText("Stop simulation");
		StopBotton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		StopBotton.setIcon(new ImageIcon("icons/stop.png"));
		barra.add(StopBotton);
		barra.addSeparator();
		
		JLabel ticksMsg = new JLabel("Ticks: ");
		ticksMsg.setAlignmentX(LEFT_ALIGNMENT);
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(time0, 1, 5000, 10));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ticks = (Integer) spinner.getValue();
			}
		});
		barra.add(ticksMsg);
		
		JButton ExitBotton = new JButton();
		ExitBotton.setActionCommand("Exit simulation");
		ExitBotton.setToolTipText("Exit simulation");
		ExitBotton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showOptionDialog(ControlPanel.this,
						"Are sure you want to quit?", "Quit",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (n == 0) System.exit(0);
			}
		});
		ExitBotton.setIcon(new ImageIcon("icons/stop.png"));
		barra.add(ExitBotton, BorderLayout.EAST);
		
		//this.pack();
		this.setVisible(true);
	}
	
	private void run_sim(int n) {
        if ( n>0 && !_stopped ) {
            try {
                _ctrl.run(1, null); //TODO whtahafa
            } catch (Exception e) {
                JOptionPane.showMessageDialog(ControlPanel.this, "An error occured. " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                //setButtonsEnabled(true);
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
        	//setButtonsEnabled(true);
        	//enableToolBar(true); //TODO preguntar
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
			System.out.println("Canceled");
		} else {
			if (dialog.getVehicle() != null) {
				List<Pair<String,Integer>> ws = new ArrayList<Pair<String,Integer>>();
				Pair<String,Integer> auxPair = new Pair<String,Integer>(dialog.getVehicle().getId(), dialog.getCO2());
				ws.add(auxPair);
				_ctrl.addEvent(new NewSetContClassEvent(dialog.getTicks(), ws));
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
			System.out.println("Canceled");
		} else {
			if (dialog.getRoad() != null) {
				List<Pair<String,Weather>> ws = new ArrayList<Pair<String,Weather>>();
				Pair<String,Weather> auxPair = new Pair<String,Weather>(dialog.getRoad().getId(), dialog.getWeather());
				ws.add(auxPair);
				_ctrl.addEvent(new SetWeatherEvent(dialog.getTicks(), ws));
			}
		}
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		mapa = map;	
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		mapa = map;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		mapa = map;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		mapa = map;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		mapa = map;
	}

	@Override
	public void onError(String err) { //TODO enterarnos bien
		System.out.println(err);
	}
}
