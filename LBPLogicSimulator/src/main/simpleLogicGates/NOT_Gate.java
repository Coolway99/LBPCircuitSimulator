package main.simpleLogicGates;

import main.interfaces.LogicGate2;

public class NOT_Gate extends LogicGate2{
	
	private byte output = 1;
	
	public NOT_Gate(){
		super(1);
	}

	@Override
	public boolean update(long cycle){
		if(cycle == this.lastUpdated) return true;
		this.lastUpdated = cycle;
		//There can only be one possible input
		LogicGate2 in = this.inList.get(0);
		if(in == null || in.getOutput(cycle) == 0){
			this.output = 1;
		} else {
			this.output = 0;
		}
		return false;
	}

	@Override
	public byte getOutput(long cycle){
		this.update(cycle);
		return this.output;
	}

}
