package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

import javax.swing.JPanel;

import main.interfaces.LogicGate;
import main.simpleLogicGates.AND_Gate2;

public class MainPanel2 extends JPanel implements MouseListener, MouseMotionListener{
	private static final long serialVersionUID = -2490678084136159574L;
	
	public HashMap<Point, LogicGate> gates = new HashMap<>();
	private int scale = 80;
	private LogicGate currentGate = null;
	private Point currentPoint = null;

	public MainPanel2(){
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.gates.put(new Point(0, 0), new AND_Gate2());
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		for(Point point : this.gates.keySet()){
			LogicGate gate = this.gates.get(point);
			g2.drawImage(gate.getForegroundImage(), point.x*this.scale,
					point.y*this.scale, gate.area.width*this.scale,
					gate.area.height*this.scale, null);
		}
		if(this.currentGate != null){
			g2.drawImage(this.currentGate.getForegroundImage(), this.currentPoint.x*this.scale,
					this.currentPoint.y*this.scale, this.currentGate.area.width*this.scale,
					this.currentGate.area.height*this.scale, null);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		if(e.getButton() != MouseEvent.BUTTON1) return;
		if(this.currentGate != null){
			this.gates.put(this.currentPoint, this.currentGate);
			this.currentGate = null;
			this.currentPoint = null;
			return;
		}
		Point point = new Point(e.getX()/this.scale, e.getY()/this.scale);
		LogicGate gate = this.gates.get(point);
		if(gate != null){
			System.out.println("Picked up gate");
			this.currentGate = gate;
			this.gates.remove(point);
			this.currentPoint = point;
		}
		System.out.println(e.getPoint());
		System.out.println(point);
	}

	@Override
	public void mouseDragged(MouseEvent e){
		if(this.currentGate == null) return;
		this.currentPoint.setLocation(e.getX()/this.scale, e.getY()/this.scale);
	}

	@Override
	public void mouseReleased(MouseEvent e){
		if(e.getButton() != MouseEvent.BUTTON1) return;
		if(this.currentGate != null){
			this.gates.put(this.currentPoint, this.currentGate);
			this.currentGate = null;
			this.currentPoint = null;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e){
		if(this.currentGate == null) return;
		this.currentPoint.setLocation(e.getX()/this.scale, e.getY()/this.scale);
	}

	@Override
	public void mouseClicked(MouseEvent e){/*UNUSED*/}

	@Override
	public void mouseEntered(MouseEvent e){/*UNUSED*/}

	@Override
	public void mouseExited(MouseEvent e){/*UNUSED*/}
	
}
