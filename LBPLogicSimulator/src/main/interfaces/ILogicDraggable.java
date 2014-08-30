package main.interfaces;

import java.awt.Point;

public interface ILogicDraggable{
	public Point location = new Point();
	public void setDraggableLocation(Point p);
	public Point getDraggableLocation();
}
