package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;
import simulator.model.Weather;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver {
	
	private static final int _JRADIUS = 10;
	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _ROAD_COLOR = Color.BLACK;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	private List<Road> rl;
	private Image _car;

	MapByRoadComponent(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
		setPreferredSize (new Dimension (300, 200));
	}
	
	private void initGUI() {
		_car = loadImage("car");
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());
		
		if (rl == null || rl.size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			//updatePrefferedSize(); //copiamos esto igual menos esta linea pq nunca se nos va a salir
			drawMap(g);
		}
	}
	
	private void drawMap(Graphics g) {
		for (int i = 0; i < rl.size(); ++i) {
			// the road goes from (x1,y) to (x2,y)
			Road r = rl.get(i);
			int x1 = 50;
			int x2 = getWidth()-100;
			int y = (i+1)*50;
			
			//id of the road
			g.setColor(_ROAD_COLOR);
			g.drawString(r.getId(), 15, y);
			
			//road
			g.drawLine(x1, y, x2, y);
			//junctions
			drawJunctions(g, x1, x2, y, r);
			//vehicles
			drawVehicles(g, x1, x2, y, r);
			
			//imagen weather
			Image weather = chooseWeather(r.getWeather());
			g.drawImage(weather, getWidth() - 91, y - 20, 32, 32, this);
			
			//imagen contaminacion
			int C = (int) Math.floor(Math.min((double) r.getTotalCO2()/(1.0 + (double) r.getContLimit()),1.0) / 0.19);
			Image contclass = loadImage("cont_" + C);
			g.drawImage(contclass,  getWidth() - 54, y - 20, 32, 32, this);
		}
	}
	
	private Image chooseWeather(Weather weather) {
		switch (weather) {
			case SUNNY: 
				return loadImage("sun");
			case CLOUDY:
				return loadImage("cloud");
			case RAINY:
				return loadImage("rain");
			case STORM:
				return loadImage("storm");
			default:
				return loadImage("wind");
		}
	}
	
	private void drawJunctions(Graphics g, int x1, int x2, int y, Road r) {
		// draw a circle with center at (x,y) with radius _JRADIUS
		// for the source junction
		g.setColor(_JUNCTION_COLOR);
		g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

		// draw the junctions identifiers at (x1,y), (x2, y)
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(r.getSrc().getId(), x1, y - 7);
		g.drawString(r.getSrc().getId(), x2, y - 7);
		
		//representa el cruce destino, dicho color dependerá
		//del color del semáforo de la carretera
		g.setColor(_RED_LIGHT_COLOR);
		int idx = r.getDest().getGreenLightIndex();
		if (idx != -1 && r.equals(r.getDest().getInRoads().get(idx))) {
			g.setColor(_GREEN_LIGHT_COLOR);
		}
		g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
	}
	
	private void drawVehicles(Graphics g, int x1, int x2, int y, Road r) {
		for (Vehicle v : r.getVehicles()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {
				int x = x1 + (int) ((x2 - x1) * ((double) v.getLocation() / (double) r.getLength()));
				
				// Choose a color for the vehcile's label and background, depending on its
				// contamination class
				int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContClass()));
				g.setColor(new Color(0, vLabelColor, 0));
				g.drawImage(_car, x, y - 13, 22, 22, this); 
				g.drawString(v.getId(), x, y - 23);
				
			}
		}
	}
			
	
	// loads an image from a file
		private Image loadImage(String img) {
			Image i = null;
			try {
				return ImageIO.read(new File("resources/icons/" + img + ".png"));
			} catch (IOException e) {
			}
			return i;
	}
	
	public void update(RoadMap map) {
		rl = map.getRoads();
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {}
	
}
