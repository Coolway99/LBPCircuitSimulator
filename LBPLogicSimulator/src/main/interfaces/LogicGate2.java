package main.interfaces;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The base class for all logic gates 
 * @author Coolway99
 */
public abstract class LogicGate2{
	/**
	 * This list is for storing the inputs of other gates
	 */
	protected ArrayList<LogicGate2> inList = new ArrayList<>(2);
	/**
	 * This list is for storing what gates are expecting this one's output
	 * <b>PUBLIC FOR TESTING</b>
	 */
	public HashMap<LogicGate2, ArrayList<Byte>> outList = new HashMap<>();
	
	/**
	 * The number of inputs this gate has
	 */
	public byte numOfIn;
	
	/**
	 * The area of this gate
	 */
	public Rectangle area;
	
	/**
	 * The previous cycle it was updated on
	 * TODO <b>PUBLIC FOR TESTING</b>
	 */
	public long lastUpdated = 0;
	
	/**
	 * Updates the gate. If the cycle matches the previous one, do not update.
	 * If it needs to update it's inputs, it will do so.
	 * @param cycle The "cycle" of the program, to prevent it updating more than once per tick
	 * @return Was it already updated?
	 */
	public abstract boolean update(long cycle);
	
	/**
	 * Returns the output of this gate, updating it if it need be
	 * @param cycle The cycle, if not the previous one, update the gate first
	 * @return The output of the gate.
	 */
	public abstract byte getOutput(long cycle);
	
	public LogicGate2(int inputs){
		this((byte) inputs);
	}
	
	public LogicGate2(byte inputs){
		this(inputs, new Rectangle(80, 80));
	}
	
	public LogicGate2(int inputs, Rectangle area){
		this((byte) inputs, area);
	}
	
	public LogicGate2(byte inputs, Rectangle area){
		this.numOfIn = inputs;
		this.area = area;
		for(int x = 0; x < this.numOfIn; x++){
			this.inList.add(null);
		}
	}
	
	/**
	 * Returns the gate who's output is connect this gate's input
	 * @param port The port to look at
	 * @return The gate connected, or null if nonexistent
	 */
	public LogicGate2 getInputGate(byte port){
		return this.inList.get(port);
	}
	
	/**
	 * Returns an ArrayList of all the ports this gate's output is connect to the other gate's input
	 * @param gate The gate to get all the ports for
	 * @return An ArrayList containing all the ports
	 */
	public ArrayList<Byte> getOutputPorts(LogicGate2 gate){
		return this.outList.get(gate);
	}
	
	/**
	 * Connect the output of another gate to an input port of this gate
	 * @param port The port to connect to
	 * @param gate The other gate
	 */
	public void connectInput(byte port, LogicGate2 gate){
		this.inList.set(port, gate);
	}
	
	/**
	 * Connect the output of this gate to the input of another gate
	 * @param port The input of the other gate
	 * @param gate The other gate
	 */
	public void connectOutput(byte port, LogicGate2 gate){
		if(this.outList.get(gate) == null){
			this.outList.put(gate, new ArrayList<Byte>());
		}
		this.outList.get(gate).add(port);
	}
	
	/**
	 * Clears the specified port, "removing" any gates from it 
	 * @param port The port to clear
	 */
	public void breakInput(byte port){
		this.inList.set(port, null);
	}
	
	/**
	 * Removes the gate, preventing it from receiving signals anymore
	 * @param gate The gate to remove
	 */
	public void breakOutput(LogicGate2 gate){
		this.outList.remove(gate);
	}
	
	/**
	 * Automatically unsubscribes itself from all gates on it's input, and
	 * removes itself from outputting to other gates.
	 * 
	 * Call before deleting this object, as it doesn't clear it's own lists.
	 */
	public void delete(){
		for(LogicGate2 gate : this.inList){
			if(gate == null) continue;
			gate.breakOutput(this);
		}
		for(LogicGate2 gate : this.outList.keySet()){
			if(gate == null) continue;
			ArrayList<Byte> ports = this.getOutputPorts(gate);
			for(Byte port : ports){
				gate.breakInput(port);
			}
		}
	}
}
