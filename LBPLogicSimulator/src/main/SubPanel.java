package main;

import java.awt.GridLayout;
import java.io.PrintStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SubPanel extends JPanel{
	private static final long serialVersionUID = -6882300119515941241L;
	
	private final Output out = new Output();
	public final ConfigPanel configPanel = new ConfigPanel();

	public SubPanel(){
		this.setLayout(new GridLayout(2, 1));
		this.add(this.configPanel);
		this.add(new JScrollPane(this.out.getTextArea()));
		
		this.out.getTextArea().setEditable(false);
		System.setOut(new PrintStream(this.out));
	}
}
