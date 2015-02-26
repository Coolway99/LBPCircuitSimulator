package main.interfaces;

/**
 * Listens for a change on the input, this goes on the output
 * @author Coolway99
 */
public interface ILogicOutput{
	public void inputBroken(int port, ILogicInput listener);
}
