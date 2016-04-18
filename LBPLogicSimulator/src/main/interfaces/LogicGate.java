package main.interfaces;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The base class for all logic gates 
 * @author Coolway99
 */
public abstract class LogicGate{
	/**
	 * This list is for storing the inputs of other gates
	 */
	protected ArrayList<LogicGate> inList = new ArrayList<>(2);
	/**
	 * This list is for storing what gates are expecting this one's output
	 * <b>PUBLIC FOR TESTING</b>
	 */
	public HashMap<LogicGate, ArrayList<Byte>> outList = new HashMap<>();
	
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
	 * A constructor that converts inputs to <b>byte</b> and then calls {@link #LogicGate2(byte)}
	 * @param inputs How many inputs the gate has
	 */
	public LogicGate(int inputs){
		this((byte) inputs);
	}

	/**
	 * Calls {@link #LogicGate2(byte, Rectangle)} with default size (1x1)
	 * @param inputs How many inputs the gate has
	 */
	public LogicGate(byte inputs){
		this(inputs, new Rectangle(1, 1));
	}
	
	/**
	 * Calls {@link #LogicGate2(byte, Rectangle)}, converting "inputs" to a byte
	 * @param inputs How many inputs the gate has
	 * @param area The size the gate takes up
	 */
	public LogicGate(int inputs, Rectangle area){
		this((byte) inputs, area);
	}
	
	/**
	 * The main constructor. It creates a logic gate with x many "inputs", and an area specified by the
	 * rectangle. The area is based off of 1, as the program scales it up automatically. 1x1 is the default
	 * square size.
	 * @param inputs How many inputs the gate has
	 * @param area The size the gates takes up
	 */
	public LogicGate(byte inputs, Rectangle area){
		this.numOfIn = inputs;
		this.area = area;
		for(int x = 0; x < this.numOfIn; x++){
			this.inList.add(null);
		}
	}
	
	/**
	 * Connect the output of another gate to an input port of this gate
	 * @param port The port to connect to
	 * @param gate The other gate
	 */
	public void connectInput(byte port, LogicGate gate){
		this.inList.set(port, gate);
	}
	
	/**
	 * Connect the output of this gate to the input of another gate
	 * @param port The input of the other gate
	 * @param gate The other gate
	 */
	public void connectOutput(byte port, LogicGate gate){
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
	public void breakOutput(LogicGate gate){
		this.outList.remove(gate);
	}
	
	/**
	 * Automatically unsubscribes itself from all gates on it's input, and
	 * removes itself from outputting to other gates.
	 * <br><br>
	 * Call before deleting this object, as it doesn't clear it's own lists.
	 * You cannot expect garbage collection to automatically call {@link #finalize()}
	 */
	public void delete(){
		for(LogicGate gate : this.inList){
			if(gate == null) continue;
			gate.breakOutput(this);
		}
		for(LogicGate gate : this.outList.keySet()){
			if(gate == null) continue;
			ArrayList<Byte> ports = this.getOutputPorts(gate);
			for(Byte port : ports){
				gate.breakInput(port);
			}
		}
	}

	/**
	 * Returns the gate who's output is connect this gate's input
	 * @param port The port to look at
	 * @return The gate connected, or null if nonexistent
	 */
	public LogicGate getInputGate(byte port){
		return this.inList.get(port);
	}

	/**
	 * Returns an ArrayList of all the ports this gate's output is connect to the other gate's input
	 * @param gate The gate to get all the ports for
	 * @return An ArrayList containing all the ports
	 */
	public ArrayList<Byte> getOutputPorts(LogicGate gate){
		return this.outList.get(gate);
	}

	/**
	 * Updates the gate. If the cycle matches the previous one, do not update.
	 * If it needs to update it's inputs, it will do so.
	 * It can be a byte because all it detects is change, so if the byte overflows then it's still a change
	 * @param cycle The "cycle" of the program, to prevent it updating more than once per tick
	 * @return Was it already updated?
	 */
	public abstract boolean update(byte cycle);

	/**
	 * Returns the output of this gate, updating it if it need be<br>
	 * @param cycle The cycle, if not the previous one, update the gate first
	 * @return The output of the gate.
	 */
	public abstract boolean getOutput(byte cycle);
	
	/**
	 * Fetches the image used to render this logic gate.
	 * @return The image that should be used for this gate.
	 */
	public abstract Image getImage();
	
	/**
	 * If this logic gate is being deleted, then run {@link #delete()}
	 * <br><br>
	 * Technically speaking, if this logic gate "still exists" then it's unable
	 * for it to be garbage collected. This is due to it still being reference-able
	 * inside other logic gates due to how it's stored.
	 * <br><br>
	 * TODO This really should need to do this. Provided how everything works,
	 * this logic gate shouldn't be in the memory of others if it's being deleted.
	 */
	@Override
	protected void finalize() throws Throwable{
		this.delete();
	}
}
