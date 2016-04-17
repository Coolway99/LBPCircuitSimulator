package main.simpleLogicGates;

import java.awt.Image;

import main.ResourceHelper;
import main.interfaces.LogicGate;

/**
 * The OR gate is one of the basic logic gates<br>
 * Digital: Turns on when either input is true<br>
 * Analog: Outputs the higher of the 2 values, ignoring sign.
 * @author Coolway99
 */
public class OR_Gate extends LogicGate{
	
	private boolean output = false;
	
	public OR_Gate(){
		super(2);
	}

	@Override
	public boolean update(long cycle){
		if(cycle == this.lastUpdated) return true;
		this.lastUpdated = cycle;
		boolean newOut = false;
		for(LogicGate gate : this.inList){
			if(gate != null && gate.getOutput(cycle)){
				newOut = true;
				break;
			}
		}
		this.output = newOut;
		return false;
	}

	@Override
	public boolean getOutput(long cycle){
		this.update(cycle);
		return this.output;
	}
	
	@Override
	public Image getForegroundImage(){
		return ResourceHelper.getImage("gateOR.png");
	}
	
}
