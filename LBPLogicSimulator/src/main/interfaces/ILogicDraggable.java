package main.interfaces;

import java.awt.Point;

public interface ILogicDraggable{
	public Point location = new Point();
	public void setLocation(Point p);
	public Point getLocation();
}
