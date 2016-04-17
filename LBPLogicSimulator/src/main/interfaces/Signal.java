package main.interfaces;

/**
 * A class for outputs. Digital is a 1 or a 0, and analog is -100 to 100.
 * TODO is this really even needed?
 * @author Coolway99
 */
public class Signal {
	boolean digital;
	double analog;
	public Signal(boolean digital, double analog){
		this.digital = digital;
		this.analog = analog;
	}
	public boolean getDigital(){
		return this.digital;
	}
	public double getAnalog(){
		return this.analog;
	}
}
