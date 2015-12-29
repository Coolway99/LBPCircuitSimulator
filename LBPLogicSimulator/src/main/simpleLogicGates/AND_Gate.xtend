package main.simpleLogicGates

import java.awt.Toolkit
import main.interfaces.LogicGate
import main.interfaces.Signal
import java.awt.Rectangle
import main.LBPLogicSimulator

class AND_Gate extends LogicGate{
	
	static val image = Toolkit.getDefaultToolkit.createImage(typeof(LBPLogicSimulator).classLoader.getResource("assets/gateAND.png"))
	
	new(){
		this.inputs = 2;
		this.outputs = 1;
		this.area = new Rectangle(0, 0, 80, 80)
	}
	
	override update() {
		inputList.forEach[
			var digital = true
			var analog = true
			var lowestValue = 100d
				try{
					if(!analog || getAnalog() == 0){
						analog = false
					} else if(Math.abs(getAnalog()) < lowestValue){
						lowestValue = getAnalog();
					}
					if(!getDigital()){
						digital = false;
					}
				} catch(NullPointerException e){
					//No input found
				}
			this.setOutput(0, new Signal(digital, if(analog){lowestValue}else{0}))
		]
	}
	
	override getImage() {
		image
	}
}