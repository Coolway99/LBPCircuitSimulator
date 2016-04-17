package main.simpleLogicGates;

import main.interfaces.LogicGate2;

public class OR_Gate2 extends LogicGate2{
	
	private byte output = 0;
	
	public OR_Gate2(){
		super(2);
	}

	@Override
	public boolean update(long cycle){
		if(cycle == this.lastUpdated) return true;
		this.lastUpdated = cycle;
		byte newOut = 0;
		for(LogicGate2 gate : this.inList){
			if(gate != null && gate.getOutput(cycle) == 1){
				newOut = 1;
				break;
			}
		}
		this.output = newOut;
		return false;
	}

	@Override
	public byte getOutput(long cycle){
		this.update(cycle);
		return this.output;
	}
}
