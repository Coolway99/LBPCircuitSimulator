package main.simpleLogicGates;

import java.awt.Image;

import main.ResourceHelper;
import main.interfaces.LogicGate;

/**
 * The XOR gate is one of the basic logic gates<br>
 * Digital: Turns on when either input is on, except when both are on.<br>
 * Analog: Outputs the Input, outputs 0 if it has 2 inputs.
 * @author Coolway99
 */
public class XOR_Gate extends LogicGate{
	
	private boolean output = false;
	
	public XOR_Gate(){
		super(2);
	}

	@Override
	public boolean update(byte cycle){
		if(cycle == this.lastUpdated) return true;
		this.lastUpdated = cycle;
		boolean newOut = false;
		for(LogicGate gate : this.inList){
			if(gate != null && gate.getOutput(cycle)){
				newOut = !newOut;
			}
		}
		this.output = newOut;
		return false;
	}
	
	@Override
	public boolean getOutput(byte cycle){
		this.update(cycle);
		return this.output;
	}
	
	@Override
	public Image getImage(){
		return ResourceHelper.getImage("gateXOR.png");
	}
	
}
