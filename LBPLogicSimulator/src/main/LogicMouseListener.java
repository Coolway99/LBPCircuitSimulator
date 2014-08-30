package main;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import main.interfaces.ILogicDraggable;

public class LogicMouseListener extends MouseAdapter{
	private double oldX = 0;
	private double oldY = 0;
	@Override
	public void mousePressed(MouseEvent e) {
		this.oldX = e.getX();
		this.oldY = e.getY();
	};
	@Override
	public void mouseReleased(MouseEvent e) {
		this.oldX = 0;
		this.oldY = 0;
	};
	@Override
	public void mouseDragged(MouseEvent e){
		if(e.getSource() instanceof ILogicDraggable){
			ILogicDraggable source = (ILogicDraggable) e.getSource();
			Point p = new Point();
			p.setLocation(source.getDraggableLocation().getX() + (oldX - e.getX()),
					source.getDraggableLocation().getY() + (oldY - e.getY()));
			source.setDraggableLocation(p);
			this.oldX = e.getX();
			this.oldY = e.getY();
		}
	}
}