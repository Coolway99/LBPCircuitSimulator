package main;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class Main {
	public static final MainFrame mainFrame = new MainFrame();
	public static void main(String[] Args){
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				mainFrame.mainPanel1.doUpdate();
			}
		}, 1000/30);
	}
}