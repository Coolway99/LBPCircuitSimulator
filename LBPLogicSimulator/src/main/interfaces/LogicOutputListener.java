package main.interfaces;

import java.util.EventListener;

public interface LogicOutputListener extends EventListener{
	public void outputBroken(int receivingPort, LogicInputListener listener);
	public void outputSignal(int port, Signal s);
}
