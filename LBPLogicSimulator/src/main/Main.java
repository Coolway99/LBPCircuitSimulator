package main;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class Main {
	public static final String synchronizationKeyA = "bleh";
	public static final MainFrame mainFrame = new MainFrame();
	public static void main(String[] Args){
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Main.update();
			}
		}, 1000/30);
	}
	public static void update(){
		
	}
}