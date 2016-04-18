package main;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import main.interfaces.ColorSet;
import main.interfaces.LogicGate;
import main.simpleLogicGates.AND_Gate2;
import main.simpleLogicGates.NOT_Gate;
import main.simpleLogicGates.OR_Gate;
import main.simpleLogicGates.XOR_Gate;

/**
 * The class/component that actually handles everything. On the surface it is a
 * JPanel that implements MouseListener, MouseMotionListener, and ActionListener.
 * It internally holds a JPopupMenu for interfacing with gates and the panel itself.
 * It also holds the data for all the gates and updates them all.
 * @author Coolway99
 */
public class MainPanel2 extends JPanel implements MouseListener, MouseMotionListener, ActionListener{
	private static final long serialVersionUID = -2490678084136159574L;
	
	/**
	 * Stores the update cycle. There's no need to worry about overflowing, since all we detect
	 * is a change in the value.
	 */
	public byte cycle = 0;
	
	/**
	 * This is the map that stores the locations of all the logic gates.
	 * There can only be one gate in one grid location at any given time.
	 */
	public HashMap<Point, LogicGate> gates = new HashMap<>();
	
	/**
	 * The scale which to render everything at. This controls the grid size (how many pixels per grid area)
	 * and as consequence the render size (a normal gate takes up 1 grid space).<br>
	 * <br>
	 * This should never be below 1
	 */
	private int scale = 160;
	
	/**
	 * Stores the logic gate currently being dragged around, unless {@link #special} is set to nonzero<br>
	 * <br>
	 * This will not render unless it and {@link #currentPoint} are not null
	 */
	private LogicGate currentGate = null;
	
	/**
	 * Stores the current point for the gate currently being dragged around, unless {@link #special} 
	 * is set to nonzero<br>
	 * <br>
	 * The {@link #currentGate} will not render unless both this and {@link #currentGate} are not null.
	 */
	private Point currentPoint = null;
	
	/**
	 * This is an enum used if we are doing something special, default value is NONE
	 */
	private Special special = Special.NONE;
	
	/**
	 * This is the menu that will popup if a context menu is opened not on a gate
	 * @see #gateMenu
	 */
	private JPopupMenu panelMenu = new JPopupMenu();
	
	/**
	 * This is the menu that will popup if a context menu is opened on a gate
	 * @see #panelMenu
	 */
	private JPopupMenu gateMenu = new JPopupMenu();

	public MainPanel2(){
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.gates.put(new Point(0, 0), new AND_Gate2());//TODO for testing only
		
		this.addGateItem("Connect");
		this.addGateItem("Disconnect");
		this.addGateItem("Delete");
		
		this.addPanelItem("New AND Gate", "newANDGate");
		this.addPanelItem("New OR Gate", "newORGate");
		this.addPanelItem("New XOR Gate", "newXORGate");
		this.addPanelItem("New NOT Gate", "newNOTGate");
	}
	
	public void update(){
		synchronized(this){
			this.cycle++;
			for(LogicGate gate : this.gates.values()){
				gate.update(this.cycle);
			}
			if(this.currentGate != null){
				this.currentGate.update(this.cycle);
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g){
		synchronized(this){
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			//For drawing lines, set stroke width to 1/8th scale
			g2.setStroke(new BasicStroke(this.scale / 8));
			for(Point point : this.gates.keySet()){
				LogicGate gate = this.gates.get(point);
				ColorSet colors = gate.getColors();
				//Get the image and scale it
				g2.drawImage(gate.getImage(), point.x*this.scale,
						point.y*this.scale, gate.area.width*this.scale,
						gate.area.height*this.scale,
						//Changes color based on it's digital state
						(gate.getOutput(this.cycle) ? colors.getOn() : colors.getOff())
						//We don't have an image observer
						, null);
				//For each output, draw a line (outputs copy the color of the parent gate)
				for(LogicGate gateOut : gate.getOutputs()){
					g2.setColor((gate.getOutput(this.cycle) ? colors.getOn() : colors.getOff()));
					Point pointOut = gateOut.area.getLocation();
					//The +1 is so that it looks like it's on the other side of the gate.
					g2.drawLine((point.x+1)*this.scale, point.y*this.scale+(this.scale/2),
							pointOut.x*this.scale, pointOut.y*this.scale);
					/*
					 * TODO
					 * Idealy, I would want something that would do along the lines of...
					 * (point.y*this.scale)+((this.scale/(numOfPorts+1))*(port+1)) 
					 * Right now it draws it in the top right corner. ports are 0 based, so the +1 is needed
					 * to bring it up from 0. Taking on an extra port makes it so it wouldn't touch the corner
					 * (2 ports, 3 subsections).
					 * 
					 * An example would be 2 ports. +1 makes that 3, which the amount of subsections. Each
					 * line would be drawn from inbetween each subsection, which would be 0, 1, 2, and 3.
					 * 1 and 2 would correspond to the ports, which are normally stored as 0 and 1. This means
					 * we would have to +1 them. Diving it by scale gives us actual pixel amounts.
					 * The default code (pointOut.y*this.scale) already brings line to the top left corner, so
					 * all it would be a matter of is translating it. which would just tack on the additional
					 * pixels to the end.
					 * 
					 * This example is for inputs, but it can easily work for outputs too.
					 */
				}
			}
			if(this.currentPoint != null && this.currentGate != null){
				g2.drawImage(this.currentGate.getImage(), this.currentPoint.x*this.scale,
						this.currentPoint.y*this.scale, this.currentGate.area.width*this.scale,
						this.currentGate.area.height*this.scale,
						(this.currentGate.getOutput(this.cycle) ?
								this.currentGate.getColors().getOn() :
									this.currentGate.getColors().getOff())
						, null);
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		if(e.getButton() != MouseEvent.BUTTON1) return;
		switch(this.special){
			case NONE:
			default:{
				Point point = this.scalePoint(e.getPoint());
				LogicGate gate = this.gates.get(point);
				if(gate != null){
					this.currentGate = gate;
					this.gates.remove(point);
					this.currentPoint = point;
				} else {
					this.currentGate = null;
					this.currentPoint = null;
				}
			}
			break;
			case CONNECT:{
				LogicGate gate = this.gates.get(this.scalePoint(e.getPoint()));
				if(gate != null){
					byte port = -1;
					do{
						try{
							port = Byte.parseByte(JOptionPane.showInputDialog(
									LBPLogicSimulator.mainFrame, "Which port?", 0));
						} catch(NumberFormatException e2){
							port = -1;
						}
					}while(port < 0);
					try{
						this.currentGate.connectOutput(port, gate);
						if(gate.getInputGate(port) != null){ 
							gate.getInputGate(port).breakOutput(gate);
						}
						gate.connectInput(port, this.currentGate);
						System.out.println("Connected");
					}catch(IndexOutOfBoundsException e2){
						System.out.println("Not Valid");
					}
					this.currentGate = null;
					this.special = Special.NONE;
				}
				break;
			}
			case PLACE:{
				//if(this.currentPoint != null && this.currentGate != null){ SHOULD NOT BE NULL
				LogicGate gate = this.gates.get(this.currentPoint);
				if(gate != null){
					gate.delete(); //TODO In LBP, it's attempted to be "best-matched" port for port
				}
				this.gates.put(this.currentPoint, this.currentGate);
				this.currentGate = null;
				this.currentPoint = null;
				this.special = Special.NONE;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e){
		if(this.currentGate == null || this.special != Special.NONE) return;
		this.currentPoint = this.scalePoint(e.getPoint());
		this.currentGate.area.setLocation(this.currentPoint);
	}

	/*functionally the same as Mouse Dragged*/
	@Override
	public void mouseMoved(MouseEvent e){
		if(this.special == Special.PLACE){
			//if(this.currentGate == null) return; if we are in place mode then this should not be null.
			this.currentPoint = this.scalePoint(e.getPoint());
			this.currentGate.area.setLocation(this.currentPoint);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e){
		if(SwingUtilities.isLeftMouseButton(e)){
			if(this.currentPoint != null && this.currentGate != null){
				LogicGate gate = this.gates.get(this.currentPoint);
				if(gate != null){
					gate.delete(); //TODO In LBP, it's attempted to be "best-matched" port for port
				}
				this.gates.put(this.currentPoint, this.currentGate);
				this.currentGate = null;
				this.currentPoint = null;
				this.special = Special.NONE;
			}
		} else if(SwingUtilities.isRightMouseButton(e)){
			if(this.currentGate == null && this.special == Special.NONE){
				this.currentPoint = this.scalePoint(e.getPoint());
				LogicGate gate = this.gates.get(this.currentPoint);
				if(gate != null){
					this.currentGate = gate;
					this.gateMenu.show(this, e.getX(), e.getY());
				} else {
					this.panelMenu.show(this, e.getX(), e.getY());
				}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		String name = ((JMenuItem) e.getSource()).getName();
		try{
			switch(name){
				case("newANDGate"):
					this.special = Special.PLACE;
					this.currentGate = new AND_Gate2();
					break;
				case("newORGate"):
					this.special = Special.PLACE;
					this.currentGate = new OR_Gate();
					break;
				case("newXORGate"):
					this.special = Special.PLACE;
					this.currentGate = new XOR_Gate();
					break;
				case("newNOTGate"):
					this.special = Special.PLACE;
					this.currentGate = new NOT_Gate();
					break;
					
				case("disconnect"):{
					byte port = -1;
					do{
						try{
							port = Byte.parseByte(JOptionPane.showInputDialog("Which port?", 0));
						} catch(NumberFormatException e2){
							port = -1;
						}
					}while(port < 0);
					try{
						LogicGate gate = this.currentGate.getInputGate(port);
						if(gate != null){
							gate.breakOutput(this.currentGate);
						}
						this.currentGate.breakInput(port);

						System.out.println("Disconnected");
					}catch(IndexOutOfBoundsException e2){
						System.out.println("Not Valid");
					}
					this.currentGate = null;
					break;
				}
				case("delete"):{
					this.currentGate.delete();
					this.gates.remove(this.currentPoint);
					this.currentGate = null;
					this.currentPoint = null;
					break;
				}
				case("connect"):{
					this.special = Special.CONNECT;
					break;
				}
				default:
					this.special = Special.NONE;
			}
		}catch(IllegalArgumentException|NullPointerException e2){
			//It's not a valid argument
			System.out.println("Not a valid name, "+name);
			this.special = Special.NONE;
		}
	}
	
	private Point scalePoint(Point point){
		return new Point(point.x/this.scale, point.y/this.scale);
	}

	private void addPanelItem(String name, String identifier){
		JMenuItem item = new JMenuItem(name);
		item.setName(identifier);
		item.addActionListener(this);
		this.panelMenu.add(item);
	}

	private void addGateItem(String name){
		JMenuItem item = new JMenuItem(name);
		item.setName(name.toLowerCase());
		item.addActionListener(this);
		this.gateMenu.add(item);
	}

	@Override
	public void mouseClicked(MouseEvent e){/*UNUSED*/}

	@Override
	public void mouseEntered(MouseEvent e){/*UNUSED*/}

	@Override
	public void mouseExited(MouseEvent e){/*UNUSED*/}
}

/*/**
 * If this is doing something special, I.E. a connection, do a special thing on the next gate click
 * 0 for normal, nonzero for special
 * <br><br>
 * 1: We are connecting the gate currently stored in {@link #currentGate} to 
 * the next gate we click on.
 * <br><br>
 * 2: We are adding a new gate. The next place we click will place the gate down.
 * <br>
 * (This technically makes {@link #mousePressed(MouseEvent)} and {@link #mouseMoved(MouseEvent)} work
 * the same as how {@link #mouseReleased(MouseEvent)} and {@link #mouseDragged(MouseEvent)} work normally)
 */
/**
 * A class used to see if we are doing anything special, the names should describe
 * what we are supposed to do on the next click.  
 * @author Coolway99
 */
enum Special{
	NONE, //There is nothing special to do
	CONNECT, //We are connecting 2 gates to eachother
	DISCONNECT, //We are disconnecting 2 gates from eachother
	PLACE, //We are placing down a brand new gate
	;
}