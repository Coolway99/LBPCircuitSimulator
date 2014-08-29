package main;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {
	public static final JFrame mainFrame = new MainFrame();
	public static void main(String[] Args){
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	}
}
