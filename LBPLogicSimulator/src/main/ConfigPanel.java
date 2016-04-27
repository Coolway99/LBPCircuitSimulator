package main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import main.interfaces.Invertable;
import main.interfaces.LogicGate;

public class ConfigPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = -7006981127517693985L;

	private final GridBagLayout layout = new GridBagLayout();
	private final GridBagConstraints c = new GridBagConstraints();
	private final JButton invert = new JButton("Invert");
	private LogicGate gate = null;
	
	public ConfigPanel(){
		this.initC();
		this.setLayout(this.layout);
		this.layout.columnWeights = new double[5];
		this.layout.rowWeights = new double[10];
		
		this.c.gridx = 1;
		this.c.gridy = 4;
		this.c.gridwidth = 3;
		this.add(this.invert, this.c);
		this.invert.addActionListener(this);
	}
	
	private void initC(){
		this.c.gridx = 0;
		this.c.gridy = 0;
		this.c.fill = GridBagConstraints.BOTH;
		this.c.gridheight = 1;
		this.c.gridwidth = 1;
	}
	
	/**
	 * Update the config panel with settings for the specified gate
	 * @param gate The gate to update the panel with, or null for the background panel
	 */
	public void update(LogicGate gate){
		this.gate = gate;
		if(gate == null){
			this.invert.setEnabled(false);
		} else {
			if(gate instanceof Invertable){
				this.invert.setEnabled(true);
			} else {
				this.invert.setEnabled(false);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e){
		switch(((JButton) e.getSource()).getText()){
			case "Invert":
				((Invertable) this.gate).invert();
				break;
			default:
				break;
		}
	}
}
