package main.simpleLogicGates;

import java.awt.Image;

import main.ResourceHelper;
import main.interfaces.Invertable;
import main.interfaces.LogicGate;

/**
 * The AND gate is one of the basic logic gates<br>
 * Digital: Only turns one when both inputs are true<br>
 * Analog: Outputs the lower of the 2 values. The signs of the 2 values are multiplied together.
 * If there's 2 negative or 2 positive inputs, the output is positive, otherwise negative.
 * @author Coolway99
 */
public class AND_Gate extends LogicGate implements Invertable{
	
	private boolean output = false;
	/**
	 * Is this a NAND gate?
	 */
	private boolean inverted = false;
	//private double analog = 0;
	
	public AND_Gate(){
		super(2);
	}
	
	public AND_Gate(boolean inverted){
		super(2);
		this.inverted = inverted;
	}
	
	@Override
	public boolean update(byte cycle){
		if(cycle == this.lastUpdated) return true;
		this.lastUpdated = cycle;
		boolean newOut = true;
		//this.analog = 100;
		for(byte port = 0; port < this.numOfIn; port++){
			LogicGate gate = this.getInputGate(port);
			if(gate == null){
				newOut = false;
				//this.analog = 0;
				//Copied from the old AND_Gate file. Might be useful
				/*(!analog || getAnalog() == 0){
						analog = false
					} else if(Math.abs(getAnalog()) < lowestValue){
						lowestValue = getAnalog();
					}
					if(!getDigital()){
						digital = false;
					}*/
				break;
			}
			if(!gate.getOutput(cycle)){
				newOut = false;
				break;
			}
		}
		this.output = (this.inverted ? !newOut : newOut);
		return false;
	}
	
	@Override
	public boolean getOutput(byte cycle){
		this.update(cycle);
		return this.output;
	}

	@Override
	public Image getImage(){
		return ResourceHelper.getImage((this.inverted ? "gateNAND.png" :"gateAND.png"));
	}
	
	@Override
	public void invert(){
		this.inverted = !this.inverted;
	}
	
	@Override
	public boolean getInverted(){
		return this.inverted;
	}
}
