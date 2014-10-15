package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JPanel;

import main.interfaces.ILogicDraggable;
import main.interfaces.ILogicGate;

@SuppressWarnings("serial")
public class MainPanel extends JPanel{
	private HashMap<Integer, ILogicGate> gates =
			new HashMap<Integer, ILogicGate>();
	private int count = 0;
	private final LogicMouseListener LML = new LogicMouseListener();
	protected byte scale = 10;
	public MainPanel(){
		super();
		this.addMouseListener(LML);
		this.addMouseMotionListener(LML);
	}
	public void addLogicGate(ILogicGate gate){
		gate.setID(gates.size());
		gates.put(gate.getID(), gate);
	}
	public void removeLogicGate(int ID){
		ILogicGate gate = gates.get(ID);
		gates.remove(ID);
		gate.destroyGate();
		gate = null;
		if(++count >= 5){
			System.gc();
			count = 0;
		}
	}
	public ILogicGate getLogicGateAt(Point p){
		System.out.println("Getting logic gate at real point"+p);
		Integer[] keys = gates.keySet().toArray(new Integer[0]);
		ILogicGate gate = null;
		for(int x = 0; x < keys.length; x++){
			gate = gates.get(keys[x]);
			Rectangle size = gate.getGateSize();
			size.setLocation(gate.getDraggableLocation());
			if(size.contains(p)){
				break;
			} else {
				gate = null;
			}
		}
		gate = gates.get(keys[0]);
		System.out.println((gate != null)? "Logic gate with ID "+gate.getID()+" returned" :
			"No logic gate found at "+p);
		return gate;
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(scale, scale);
		Integer[] keys = gates.keySet().toArray(new Integer[0]);
		for(int x = 0; x < keys.length; x++){
			ILogicGate gate = gates.get(keys[x]);
			/*g2.drawImage(gate.getBackgroundImage(), gate.getDraggableLocation().x,
					gate.getDraggableLocation().y, gate.getGateSize().width, gate.getGateSize().height,null);*/
			g2.drawImage(gate.getForegroundImage(), 
					(int) (gate.getDraggableLocation().x/scale),
					(int) (gate.getDraggableLocation().y/scale),
					15, 15, Color.BLACK, null);
		}
	}
	public synchronized void doUpdate(){
		Integer[] keys = gates.keySet().toArray(new Integer[0]);
		for(int x = 0; x < keys.length; x++){
			gates.get(keys[x]).update();
		}
		this.repaint();
	}
}
class LogicMouseListener extends MouseAdapter{
	private double oldX = 0;
	private double oldY = 0;
	private ILogicGate gate = null;
	@Override
	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		Main.mainFrame.mouseRealPos.setText(p.toString());
		Main.mainFrame.mouseGridPos.setText(p.toString());
	}
	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("Mouse Pressed Event");
		this.oldX = e.getX();
		this.oldY = e.getY();
		MainPanel panel = (MainPanel) e.getSource();
		gate = panel.getLogicGateAt(e.getPoint());
		
	};
	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("Mouse Released Event");
		this.oldX = 0;
		this.oldY = 0;
		this.gate = null;
	};
	@Override
	public void mouseDragged(MouseEvent e){
		this.mouseMoved(e);
		if(e.getSource() instanceof MainPanel){
			if((gate != null && gate instanceof ILogicDraggable) &&
					(Math.abs(oldX - e.getX()) > 1) && (Math.abs(oldY - e.getY()) > 1)){
				Point p = new Point();
				gate.setDraggableLocation(e.getPoint());
				this.oldX = e.getX();
				this.oldY = e.getY();
			}
		}
	}
}