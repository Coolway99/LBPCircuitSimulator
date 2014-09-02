package main.interfaces;

import java.util.EventListener;

/***/
public interface LogicInputListener extends EventListener{
	public void inputBroken(int receivingPort, LogicOutputListener listener);
}
