package main.simpleLogicGates;

import java.awt.Image;

import main.ResourceHelper;
import main.interfaces.LogicGate;

/**
 * The AND gate is one of the basic logic gates<br>
 * Digital: Only turns one when both inputs are true<br>
 * Analog: Outputs the lower of the 2 values. The signs of the 2 values are multiplied together.
 * If there's 2 negative or 2 positive inputs, the output is positive, otherwise negative.
 * @author Coolway99
 */
public class AND_Gate2 extends LogicGate{
	
	private boolean output = false;
	//private double analog = 0;
	
	public AND_Gate2(){
		super(2);
	}
	
	@Override
	public boolean update(byte cycle){
		if(cycle == this.lastUpdated) return true;
		this.lastUpdated = cycle;
		boolean newOut = true;
		//this.analog = 100;
		for(LogicGate gate : this.inList){
			if(gate == null){
				newOut = false;
				//this.analog = 0;
				break;
			}
			if(!gate.getOutput(cycle)){
				newOut = false;
				break;
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
		return ResourceHelper.getImage("gateAND.png");
	}
	
}
