package main.simpleLogicGates;

import java.awt.Dimension;
import java.awt.Rectangle;

import main.interfaces.ILogicGateComponent;
import main.interfaces.Signal;

public class OR_Gate extends ILogicGateComponent{
	public OR_Gate(){
		this.inputs = 2;
		this.outputs = 1;
	}
	@Override
	public boolean areInputsSettable() {
		return true;
	}
	@Override
	public int getInputMaxValue() {
		return 100;
	}
	@Override
	public int getInputMinValue() {
		return 2;
	}
	@Override
	public boolean areOutputsSettable() {
		return false;
	}
	@Override
	public int getOutputMaxValue() {
		return 0;
	}
	@Override
	public int getOutputMinValue() {
		return 0;
	}
	@Override
	public boolean hasBottomInputs() {
		return false;
	}
	@Override
	public int getBottomInputs() {
		return 0;
	}
	@Override
	public Rectangle getGateSize() {
		return new Rectangle(new Dimension(2, inputs));
	}
	@Override
	public void update() {
		boolean analog = false;
		boolean digital = false;
		double highestValue = 0;
		for(int x = 0; x < this.inputs; x++){
			Signal s = this.inputSignals.get(x);
			if(s.getAnalog() == 0){
				analog = true;
			} else if(Math.abs(s.getAnalog()) > highestValue){
				highestValue = s.getAnalog();
			}
			if(!s.getDigital()){
				digital = true;
			}
			this.sendOutput(0, new Signal((digital) ? true : false, (analog) ? highestValue : 0));
		}
	}
}
