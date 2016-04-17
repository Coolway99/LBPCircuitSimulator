package main.simpleLogicGates;

import main.interfaces.LogicGate2;

public class XOR_Gate2 extends LogicGate2{
	
	private byte output = 0;
	
	public XOR_Gate2(){
		super(2);
	}

	@Override
	public boolean update(long cycle){
		if(cycle == this.lastUpdated) return true;
		this.lastUpdated = cycle;
		boolean newOut = false;
		for(LogicGate2 gate : this.inList){
			if(gate != null && gate.getOutput(cycle) == 1){
				newOut = !newOut;
			}
		}
		this.output = (byte) (newOut ? 1 : 0);
		return false;
	}
	
	@Override
	public byte getOutput(long cycle){
		this.update(cycle);
		return this.output;
	}
}
