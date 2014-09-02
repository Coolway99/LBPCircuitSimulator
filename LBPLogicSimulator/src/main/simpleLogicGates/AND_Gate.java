package main.simpleLogicGates;

import java.awt.Dimension;
import java.awt.Rectangle;

import main.interfaces.ILogicGateComponent;

public class AND_Gate extends ILogicGateComponent{
	public AND_Gate(){
		this.inputs = 2;
		this.outputs = 1;
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
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
