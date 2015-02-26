package main.simpleLogicGates;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import main.Main;
import main.interfaces.LegacyILogicGate;
import main.interfaces.Signal;

public class XOR_Gate extends LegacyILogicGate{
	private static Image foregroundImage = new ImageIcon(Main.class.getResource("assets/gateXOR.png")).getImage();
	public XOR_Gate() {
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
		return 1;
	}
	@Override
	public int getOutputMinValue() {
		return 1;
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
		boolean analogLocked = false;
		double analogSignal = 0;
		boolean digital = false;
		boolean digitalLocked = false;
		for(int x = 0; x < this.inputs; x++){
			Signal s = this.inputSignals.get(x);
			if(s.getAnalog() != 0 && !analogLocked){
				analog = true;
				analogSignal = this.inputSignals.get(x).getAnalog();
			} else if(s.getAnalog() != 0 && analog){
				analog = false;
				analogLocked = true;
			}
			if(s.getDigital() && !digitalLocked){
				digital = true;
			} else if(s.getDigital() && digital){
				digital = false;
				digitalLocked = true;
			}
		}
		this.sendOutput(0, new Signal(digital, (analog) ? analogSignal : 0));
	}
	@Override
	public Image getForegroundImage() {
		return XOR_Gate.foregroundImage;
	}
}
