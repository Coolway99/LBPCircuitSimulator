package main.simpleLogicGates;

import java.awt.Image;

import main.ResourceHelper;
import main.interfaces.LogicGate;

/**
 * The NOT gate is one of the basic logic gates<br>
 * Digital: Is always the opposite value of it's input<br>
 * Analog: Outputs the 100 minus it's input, ignoring negatives.
 * A mathematical example would be <b>100-|x|</b>, where x is the input.
 * @author Coolway99
 */
public class NOT_Gate extends LogicGate{
	
	private boolean output = true;
	
	public NOT_Gate(){
		super(1);
	}

	@Override
	public boolean update(byte cycle){
		if(cycle == this.lastUpdated) return true;
		this.lastUpdated = cycle;
		//There can only be one possible input
		LogicGate in = this.getInputGate((byte) 0);
		if(in == null){
			this.output = true;
			return false;
		}
		if(!in.getOutput(cycle)){
			this.output = true;
		} else {
			this.output = false;
		}
		return false;
	}

	@Override
	public boolean getOutput(byte cycle){
		this.update(cycle);
		return this.output;
	}
	
	@Override
	public Image getImage(){
		return ResourceHelper.getImage("gateNOT.png");
	}
	
}
