package main

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Toolkit
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.util.ArrayList
import javax.swing.JPanel
import main.interfaces.LogicGate

class MainPanel extends JPanel implements MouseListener, MouseMotionListener{
	
	static val in = Toolkit.getDefaultToolkit.createImage(typeof(Main).classLoader.getResource("assets/in.png"))
	static val out = Toolkit.getDefaultToolkit.createImage(typeof(Main).classLoader.getResource("assets/out.png"))
	
	ArrayList<LogicGate> gates =  newArrayList()
	var offsetX = 0
	var offsetY = 0
	var oldX = 0
	var oldY = 0
	LogicGate gate = null
	var scale = 1d;
	
	new(){
		super()
		this.addMouseListener
		this.addMouseMotionListener
	}
	
	def void addLogicGate(LogicGate gate){
		gates.add(gate);
	}
	
	def void removeLogicGate(LogicGate gate){
		gates.remove(gate)
		gate.destory
	}
	
	def synchronized void update(){
		gates.forEach[update]
	}
	
	override paintComponent(Graphics g){
		super.paintComponent(g)
		val g2 = g as Graphics2D
		gates.forEach[
			var rect = area
			g2.scale(scale, scale)
			g2.drawImage(backgroundImage, rect.x, rect.y, rect.width, rect.height, Color.BLACK, null)
			g2.drawImage(image, rect.x+10, rect.y+10, rect.width-20, rect.height-20, null)
			if(inputs != 0){
				if(inputs == 1){
					g2.drawImage(in, rect.x-13, rect.y+34, 12, 12, null)
				} else {
					for(var x = 0; x < inputs; x++){
						g2.drawImage(in, rect.x-13, rect.y+12+(x*46), 12, 12, null)
					}
				}
			}
			if(outputs != 0){
				if(outputs == 1){
					g2.drawImage(out, rect.x+rect.width, rect.y+25, 18, 29, null)
				} else {
					for(var x = 0; x < outputs; x++){
						g2.drawImage(out, rect.x-rect.width, rect.y+12+(x*46), 18, 29, null)
					}
				}
			}
		]
	}
	
	def LogicGate getGateAt(Point p){
		System.out.println(p);
		for(LogicGate it : gates){
			if(area.contains(p)){
				return it;
			}
		}
		return null;
	}
	
	override mousePressed(MouseEvent e) {
		if(e.button != MouseEvent.BUTTON1){
			return;
		}
		gate = getGateAt(e.point)
		if(gate == null){
			return;
		}
		offsetX = e.point.x - gate.area.x
		offsetY = e.point.y - gate.area.y
		oldX = e.point.x
		oldY = e.point.y
	}
	
	override mouseDragged(MouseEvent e){
		if(gate == null){
			return;
		}
		if(Math.abs(oldX - e.point.x) > 2){
			gate.area.x = e.point.x - offsetX
			oldX = e.point.x
		}
		if(Math.abs(oldY - e.point.y) > 2){
			gate.area.y = e.point.y - offsetY
			oldY = e.point.y
		}
	}
	
	override mouseReleased(MouseEvent e){
		gate = null
	}
	
	override mouseClicked(MouseEvent e){}
	
	override mouseEntered(MouseEvent e){}
	
	override mouseExited(MouseEvent e){}
	
	override mouseMoved(MouseEvent e){}
	
}