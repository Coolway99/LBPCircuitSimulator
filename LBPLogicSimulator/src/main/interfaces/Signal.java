package main.interfaces;

public class Signal {
	boolean digital;
	double analog;
	public Signal(boolean digital, double analog){
		this.digital = digital;
		this.analog = analog;
	}
	public boolean getDigital(){
		return digital;
	}
	public double getAnalog(){
		return analog;
	}
}
