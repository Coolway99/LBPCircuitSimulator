package main.interfaces;

import java.awt.Color;

/**
 * Sets used for colors on screen. Each color has it's own ON/OFF as well as an example for wires.
 * @author Coolway99
 */
public enum ColorSet{
	DEFAULT (0xA1A26B, 0xFBFEE9), //TODO This was just taken via pixel sampling
	//DEFAULT (0x008800, 0x00AA00),
	;
	
	private Color off;
	private Color on;
	
	private ColorSet(int off, int on){
		this.off = new Color(off);
		this.on = new Color(on);
	}
	
	public Color getOff(){
		return this.off;
	}
	
	public Color getOn(){
		return this.on;
	}
}
