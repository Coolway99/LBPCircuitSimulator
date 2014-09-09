package main.interfaces;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.event.EventListenerList;



/**The base class for all logic. All logic gates MUST be extend this class<br />
 * Signal I/O and connections is handled in this class automatically, can be overridden to take control of
 * the Signal I/O, or the connections (i.e, randomizer), however, it is probably not necessary
 * @category LogicGates
 * @see ILogicDraggable
 * @see ILogicGate*/
@SuppressWarnings("static-access")
public abstract class ILogicGate implements ILogicDraggable, LogicInputListener, LogicOutputListener{
	protected HashMap<Integer, EventListenerList> inputListenerList = 
			new HashMap<Integer, EventListenerList>();
	protected HashMap<Integer, EventListenerList> outputListenerList = 
			new HashMap<Integer, EventListenerList>();
	protected HashMap<Integer, Signal> inputSignals = 
			new HashMap<Integer, Signal>();
	protected HashMap<Integer, Signal> lastOutput =
			new HashMap<Integer, Signal>();
	protected int inputs;
	protected int outputs;
	private int ID = -1;
	public void addInputListener(int port, LogicInputListener l){
		inputListenerList.get(port).add(LogicInputListener.class, l);
	}
	public void removeInputListener(int port, LogicInputListener l){
		inputListenerList.get(port).remove(LogicInputListener.class, l);
	}
	public void addOutputListener(int port, LogicOutputListener l){
		outputListenerList.get(port).add(LogicOutputListener.class, l);
	}
	public void removeOutputListener(int port, LogicOutputListener l){
		outputListenerList.get(port).add(LogicOutputListener.class, l);
	}
	protected void sendOutput(int port, Signal s){
		if(!s.equals(lastOutput.get(port))){
			LogicOutputListener[] listeners = outputListenerList.get(port).getListeners(LogicOutputListener.class);
			for(int x = 0; x < listeners.length; x++){
				listeners[x].outputSignal(x, s);
			}
		}
	}
	@Override
	public void outputSignal(int port, Signal s) {
		this.inputSignals.put(port, s);
	};
	/** @return The number of inputs*/
	public int getInputs() {
		return this.inputs;
	}
	/**Sets the number of inputs (shouldn't be called if areInputsSettable returns false
	 *@see this.areInputsSettable()
	 *@param value The new amount of inputs*/
	public void setInputs(int value) {
		if(this.areInputsSettable() && 
				(value < this.getInputMaxValue() && value > this.getInputMinValue())){
			this.inputs = value;
		}
	}
	/**Called when another gate makes a connection to this one*/
	public void connectInput(int port, ILogicGate fromGate) {
		this.removeInputListener(port, this.inputListenerList.get(port).getListeners(LogicInputListener.class)[0]);
		this.addInputListener(port, fromGate);
	}
	/**Returns the number of outputs
	 * @return The number of outputs*/
	public int getOutputs() {
		return this.outputs;
	}
	/**Sets the number of outputs (shouldn't be called if areInputsSettable returns false)
	 *@see this.areInputsSettable()
	 *@param value The new amount of outputs*/
	public void setOutputs(int value) {
		if(this.areOutputsSettable() && 
				(value < this.getOutputMaxValue() && value > this.getOutputMinValue())){
			this.outputs = value;
		}
	}
	/**Called when this gate makes a connection to another*/
	public void connectOutput(int port, ILogicGate toGate) {
		this.addOutputListener(port, toGate);
	}
	/**Called when the gate is going to get deleted*/
	public void destroyGate() {
		for(int x = 0; x < inputs; x++){
			LogicInputListener[] listeners = inputListenerList.get(x).getListeners(LogicInputListener.class);
			for(int y = 0; y < listeners.length; y++){
				listeners[y].inputBroken(x, this);
			}
		}
		for(int x = 0; x < outputs; x++){
			LogicOutputListener[] listeners = outputListenerList.get(x).getListeners(LogicOutputListener.class);
			for(int y = 0; y < listeners.length; y++){
				listeners[y].outputBroken(x, this);
			}
		}
	};
	@Override
	public void inputBroken(int receivingPort, LogicOutputListener listener) {
		this.removeOutputListener(receivingPort, listener);
	};
	@Override
	public void outputBroken(int receivingPort, LogicInputListener listener) {
		this.removeInputListener(receivingPort, listener);	
	}
	@Override
	public void setDraggableLocation(Point p) {
		this.location.setLocation(p);
	}
	@Override
	public Point getDraggableLocation() {
		return this.location;
	}
	/**Used for getting the image that is stretched on the background of the gate (the outline),
	 * usually a set default image<br />
	 * The resolution is 20x20 pixels, however make it as big as you wish, it will be
	 * rendered at runtime at the correct stretched size
	 * @return The icon used for the back
	 * @see getForegroundIcon
	 * */
	public Icon getBackgroundIcon() {
		return new ImageIcon(this.getClass().getResource("assets/logicBackground.png"));
	};
	public int getID(){
		return ID;
	}
	public void setID(int newID){
		this.ID = newID;
	}
	public abstract void update();
	/**If the number of inputs are adjustable, this should return true, otherwise false
	 * @return Are the number of inputs changeable*/
	public abstract boolean areInputsSettable();
	/**@return The maximum number of inputs*/
	public abstract int getInputMaxValue();
	/**Gets the minimum number of inputs
	 * @return The minimum number of inputs*/
	public abstract int getInputMinValue();
	/**If the number of outputs are adjustable, this should return true, otherwise false
	 * @return Are the number of outputs changeable*/
	public abstract boolean areOutputsSettable();
	/**Gets the maximum number of outputs
	 * @return The maximum number of outputs*/
	public abstract int getOutputMaxValue();
	/**Gets the minimum number of outputs
	 * @return The minimum number of outputs*/
	public abstract int getOutputMinValue();
		/**If there are inputs in the bottom (like the microchip and selector), this should return true
		 * @return If it has bottom inputs are not*/
	public abstract boolean hasBottomInputs();
	/**@return The number of bottom inputs*/
	public abstract int getBottomInputs();
	/**Used to get the size for the object
	 * @return A rectangle representing the size*/
	public abstract Rectangle getGateSize();
	/**Used for getting the image that is static in the middle of the object. Usually a symbol
	 * representing it.
	 * The resolution it is rendered at is 15x15 pixels, however if it is bigger/smaller it will be resized
	 * The image should be white as it will be hue changed to the colors set
	 * @return The icon used for the front
	 * @see getBackgroundIcon
	 */
	public abstract Icon getForegroundIcon();
}
