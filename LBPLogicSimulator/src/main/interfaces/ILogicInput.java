package main.interfaces;

/**
 * Interface for change on an output. This goes onto the input
 * @author Coolway99
 */
public interface ILogicInput{
	public void outputBroken(int port, ILogicOutput listener);
	public void inputSignal(int port, Signal s);
}
