package main;

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
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		for(Point point : this.gates.keySet()){
			LogicGate gate = this.gates.get(point);
			g2.drawImage(gate.getImage(), point.x*this.scale,
					point.y*this.scale, gate.area.width*this.scale,
					gate.area.height*this.scale, null);
		}
		if(this.currentPoint != null && this.currentGate != null){
			g2.drawImage(this.currentGate.getImage(), this.currentPoint.x*this.scale,
					this.currentPoint.y*this.scale, this.currentGate.area.width*this.scale,
					this.currentGate.area.height*this.scale, null);
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
							port = Byte.parseByte(JOptionPane.showInputDialog("Which port?", 0));
						} catch(NumberFormatException e2){
							port = -1;
						}
					}while(port < 0);
					try{
						this.currentGate.connectOutput(port, gate);
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
	}

	/*functionally the same as Mouse Dragged*/
	@Override
	public void mouseMoved(MouseEvent e){
		if(this.special == Special.PLACE){
			//if(this.currentGate == null) return; if we are in place mode then this should not be null.
			this.currentPoint = this.scalePoint(e.getPoint());
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
						this.currentGate.getInputGate(port).breakOutput(this.currentGate);
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
				default:
					this.special = Special.valueOf(name);
			}
		}catch(IllegalArgumentException|NullPointerException e2){
			//It's not a valid argument
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
	NONE ("none"), //There is nothing special to do
	CONNECT ("connect"), //We are connecting 2 gates to eachother
	DISCONNECT ("disconnect"), //We are disconnecting 2 gates from eachother
	PLACE ("create"), //We are placing down a brand new gate
	;
	private final String name;
	
	private Special(){
		this.name = "none";
	}
	
	private Special(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}