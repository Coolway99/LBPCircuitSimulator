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
	protected byte scale = 100;
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
		System.out.println("Getting logic gate at "+p);
		Integer[] keys = gates.keySet().toArray(new Integer[0]);
		ILogicGate gate = null;
		for(int x = 0; x < keys.length; x++){
			System.out.println("Testing gate "+x);
			gate = gates.get(keys[x]);
			Rectangle size = gate.getGateSize();
			size.setBounds(0, 0, size.width*scale, size.height*scale);
			size.setLocation(gate.getDraggableLocation());
			System.out.println("Size is " + size);
			if(size.contains(p)){
				break;
			} else {
				gate = null;
			}
		}
		System.out.println((gate != null)? "Logic gate with ID "+gate.getID()+" returned" :
			"No logic gate found at "+p);
		return gate;
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Integer[] keys = gates.keySet().toArray(new Integer[0]);
		for(int x = 0; x < keys.length; x++){
			ILogicGate gate = gates.get(keys[x]);
			g2.drawImage(gate.getBackgroundImage(), gate.getDraggableLocation().x,
					gate.getDraggableLocation().y, gate.getGateSize().width*scale,
					gate.getGateSize().height*scale, null);
			g2.drawImage(gate.getForegroundImage(), 
					(int) (gate.getDraggableLocation().x +  .15 * scale),
					(int) (gate.getDraggableLocation().y +  .15 * scale),
					(int) (1.7 * scale), (int) (1.7 * scale), Color.BLACK, null);
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
	private double offsetX = 0;
	private double offsetY = 0;
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
		if(gate != null){
			this.offsetX = e.getX() - gate.getDraggableLocation().getX();
			this.offsetY = e.getY() - gate.getDraggableLocation().getY();
			System.out.println("Offset X = "+this.offsetX+", Offset Y = "+this.offsetY);
		}
	};
	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("Mouse Released Event");
		this.oldX = 0;
		this.oldY = 0;
		this.offsetX = 0;
		this.offsetY = 0;
		this.gate = null;
	};
	@Override
	public void mouseDragged(MouseEvent e){
		this.mouseMoved(e);
		if(e.getSource() instanceof MainPanel){
			if((gate != null && gate instanceof ILogicDraggable) &&
					(Math.abs(oldX - e.getX()) > 1) && (Math.abs(oldY - e.getY()) > 1)){
				gate.setDraggableLocation(new Point((int) (e.getX() - this.offsetX), (int) (e.getY() - this.offsetY)));
				this.oldX = e.getX();
				this.oldY = e.getY();
			}
		}
	}
}