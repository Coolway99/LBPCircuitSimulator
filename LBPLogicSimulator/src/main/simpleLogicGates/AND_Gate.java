package main.simpleLogicGates;

import java.awt.Dimension;
import java.awt.Rectangle;

import main.interfaces.ILogicGateComponent;
import main.interfaces.Signal;

public class AND_Gate extends ILogicGateComponent{
	public AND_Gate(){
		this.inputs = 2;
		this.outputs = 1;
	}
	@Override
	public void update() {
		boolean analog = true;
		boolean digital = true;
		double lowestValue = 100;
		for(int x = 0; x < this.inputs; x++){
			Signal s = this.inputSignals.get(x);
			if(s.getAnalog() == 0){
				analog = false;
			} else if(Math.abs(s.getAnalog()) < lowestValue){
				lowestValue = s.getAnalog();
			}
			if(!s.getDigital()){
				digital = false;
			}
		}
		this.sendOutput(0, new Signal((digital) ? true : false, (analog) ? lowestValue : 0));
	}
	@Override
	public boolean areInputsSettable() {
		return true;
	}
	@Override
	public boolean areOutputsSettable() {
		return false;
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
	public int getInputMaxValue() {
		return 100;
	}
	@Override
	public int getInputMinValue() {
		return 2;
	}
	@Override
	public int getOutputMaxValue() {
		return 1;
	}
	@Override
	public int getOutputMinValue() {
		return 1;
	}
	@Override
	public Rectangle getGateSize() {
		return new Rectangle(new Dimension(2, inputs));
	}
}
