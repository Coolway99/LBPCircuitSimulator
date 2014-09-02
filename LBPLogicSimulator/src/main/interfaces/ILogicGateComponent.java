package main.interfaces;

import java.awt.Point;
import java.util.HashMap;

import javax.swing.event.EventListenerList;



/**The base class for all logic.<br />
 * Signal I/O and connections is handled in this class automatically, can be overridden to take control of
 * the Signal I/O, or the connections, however, it is probably not necessary
 * @see ILogicDraggable
 * @see ILogicGate*/
@SuppressWarnings("static-access")
public abstract class ILogicGateComponent implements ILogicGate, ILogicDraggable, LogicInputListener, LogicOutputListener{
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
	@Override
	public int getInputs() {
		return this.inputs;
	}
	@Override
	public void setInputs(int value) {
		if(this.areInputsSettable() && 
				(value < this.getInputMaxValue() && value > this.getInputMinValue())){
			this.inputs = value;
		}
	}
	@Override
	public void connectInput(int port, ILogicGateComponent fromGate) {
		this.removeInputListener(port, this.inputListenerList.get(port).getListeners(LogicInputListener.class)[0]);
		this.addInputListener(port, fromGate);
	}
	@Override
	public int getOutputs() {
		return this.outputs;
	}
	@Override
	public void setOutputs(int value) {
		if(this.areOutputsSettable() && 
				(value < this.getOutputMaxValue() && value > this.getOutputMinValue())){
			this.outputs = value;
		}
	}
	@Override
	public void connectOutput(int port, ILogicGateComponent toGate) {
		this.addOutputListener(port, toGate);
	}
	@Override
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
	public abstract void update();
}
