package main.interfaces;

public class Signal {
	int port;
	boolean digital;
	double analog;
	public Signal(int port, boolean digital, double analog){
		this.port = port;
		this.digital = digital;
		this.analog = analog;
	}
	public int getPort(){
		return port;
	}
	public boolean getDigital(){
		return digital;
	}
	public double getAnalog(){
		return analog;
	}
}
