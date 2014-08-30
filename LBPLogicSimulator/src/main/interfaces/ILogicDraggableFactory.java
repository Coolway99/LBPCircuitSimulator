package main.interfaces;

import java.awt.Point;

@SuppressWarnings("static-access")
public class ILogicDraggableFactory implements ILogicDraggable{
	@Override
	public void setDraggableLocation(Point p) {
		this.location.setLocation(p);
	}
	@Override
	public Point getDraggableLocation() {
		return this.location;
	}
	
}
