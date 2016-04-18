package main;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import main.interfaces.LogicGate;
import main.simpleLogicGates.AND_Gate2;
import main.simpleLogicGates.NOT_Gate;
import main.simpleLogicGates.OR_Gate;
import main.simpleLogicGates.XOR_Gate;

public class LBPLogicSimulator {
	//public static final MainFrame mainFrame = new MainFrame();
	public static final JFrame mainFrame = new JFrame("Logic Test");
	public static final MainPanel2 mainPanel = new MainPanel2();
	public static final ArrayList<LogicGate> gates = new ArrayList<>();
	public static byte cycle = 0;
	public static final Timer fpsTimer = new Timer("FPS Timer", true);
	public static final Timer updateTimer = new Timer("Update Timer", true);
	public static void main(String[] Args){
		//halfGUI();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(800, 600);
		mainFrame.add(mainPanel);
		mainFrame.setVisible(true);
		fpsTimer.schedule(new TimerTask(){
			@Override
			public void run(){
				//mainPanel.update();
				mainFrame.repaint();
			}
		}, 1000, 1000/30);
		updateTimer.schedule(new TimerTask(){
			@Override
			public void run(){
				mainPanel.update();
			}
		}, 1000, 1000/30);
	}
	
	/*@SuppressWarnings("unused")
	private static void GUI(){
		mainFrame.init();
		mainFrame.setVisible(true);
		Timer timer = new Timer("Update Timer", true);
		timer.schedule(new TimerTask(){
			@Override
			public void run(){
				cycle++;
				mainFrame.mainPanel.update();
				mainFrame.mainPanel.repaint();
				for(LogicGate gate : gates){
						gate.update(cycle);
				}
			}
		}, 1000, 1000/30);
	}*/
	
	//FOR TESTING
	private static void halfGUI(){
		JFrame frame = new JFrame("LBPLogicSimulator TEST GUI");
		Output stream = new Output();
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.add(new JScrollPane(stream.getTextArea()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		System.setOut(new PrintStream(stream));
		System.setIn(stream.input);
		while(true){
			try{
				noGUI();
			}catch(Exception e){
				e.printStackTrace();
				//WE SHALL NOT FAIL
			}
		}
	}
	
	//FOR TESTING
	private static void noGUI(){
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while(true){
			System.out.print(":>");
			String in = scanner.nextLine();
			switch(in.toUpperCase()){
				case("LIST"):{
					System.out.println("Current cycle is "+cycle);
					if(gates.isEmpty()){
						System.out.println("Array is empty");
						break;
					}
					for(int x = 0; x < gates.size(); x++){
						LogicGate gate = gates.get(x);
						if(gate == null){
							System.out.println("Gate "+x+" is NULL");
							continue;
						}
						System.out.println("Gate "+x+" has an output of "+gate.getOutput(cycle));
						System.out.println("Is currently on cycle "+gate.lastUpdated);
						for(byte y = 0; y < gate.numOfIn; y++){
							LogicGate gateIN = gate.getInputGate(y);
							if(gateIN == null){
								System.out.println("No input on port "+y);
								continue;
							}
							System.out.println("Has Gate "+gates.indexOf(gateIN)+" on input "+y);
						}
						if(gate.outList.isEmpty()){
							System.out.println("Contains no outputs");
						} else {
							for(LogicGate gateOUT : gate.outList.keySet()){
								System.out.print("Currently is outputing to gate "
										+gates.indexOf(gateOUT)+" on input(s) ");
								for(Byte port : gate.outList.get(gateOUT)){
									System.out.print(port.byteValue()+" ");
								}
								System.out.println();
							}
						}
					}
					break;
				}
				case("UPDATE"):{
					cycle++;
					for(LogicGate gate : gates){
						System.out.print("Updating gate "+gates.indexOf(gate));
						if(gate.update(cycle)){
							System.out.println(" but it was already updated");
						} else {
							System.out.println(" successfully");
						}
					}
					break;
				}
				case("CREATE"):{
					LogicGate gate;
					while(true){
						System.out.print("Which Gate:>");
						switch(scanner.nextLine().toUpperCase()){
							case("AND"):
								gate = new AND_Gate2();
								break;
							case("NOT"):
								gate = new NOT_Gate();
								break;
							case("OR"):
								gate = new OR_Gate();
								break;
							case("XOR"):
								gate = new XOR_Gate();
								break;
							default:
								System.out.println("Unrecognized gate");
								//$FALL-THROUGH$
							case("LIST"):
							case("HELP"):
								System.out.println("Avalible gates:\nAND\nNOT\nOR\nXOR");
								continue;
						}
						break;
					}
					gates.add(gate);
					System.out.println("Successfully added gate "+gates.indexOf(gate));
					break;
				}
				case("DELETE"):try{
						System.out.print("Which gate:>");
						int num = scanner.nextInt();
						System.out.println("Deleting gate "+num);
						LogicGate gate = gates.get(num);
						if(gate == null){
							System.out.println("But it didn't exist!");
							break;
						}
						gate.delete();
						gates.remove(num);
						System.out.println("Done");
					}catch(IndexOutOfBoundsException e){
						System.out.println("But it didn't exist!");
					}catch(InputMismatchException e){
						System.out.println("Not a number!");
					}
					scanner.nextLine();
					break;
				case("CONNECT"):{
					try{
						System.out.print("The output of which gate:>");
						int num = scanner.nextInt();
						LogicGate gateFrom = gates.get(num);
						System.out.print("To which gate:>");
						num = scanner.nextInt();
						LogicGate gateTo = gates.get(num);
						System.out.print("Which port:>");
						byte port = scanner.nextByte();
						gateFrom.connectOutput(port, gateTo);
						gateTo.connectInput(port, gateFrom);
						System.out.println("Done");
					}catch(InputMismatchException e){
						System.out.println("Not a number!");
					}catch(IndexOutOfBoundsException|NullPointerException e){
						System.out.println("Not a valid gate!");
					}
					scanner.nextLine();
					break;
				}
				case("DISCONNECT"):{
					try{
						System.out.print("The input of which gate:>");
						int num = scanner.nextInt();
						System.out.print("Which port:>");
						byte port = scanner.nextByte();
						LogicGate gate = gates.get(num);
						LogicGate gateOUT = gate.getInputGate(port);
						gate.breakInput(port);
						gateOUT.breakOutput(gate);
						System.out.println("Done");
					}catch(InputMismatchException e){
						System.out.println("Not a number!");
					}catch(IndexOutOfBoundsException|NullPointerException e){
						System.out.println("Not a valid gate!");
					}
					scanner.nextLine();
					break;
				}
				case("QUIT"):
				case("EXIT"):
					System.out.println("Exiting...");
					System.exit(0);
					break;
				default:
					System.out.println("UNKNOWN COMMAND");
					//$FALL-THROUGH$
				case("HELP"):
					System.out.println("KNOWN COMMANDS ARE");
					System.out.println("LIST\nUPDATE\nHELP\nCREATE\nDELETE\nCONNECT\nDISCONNECT\nEXIT");
			}
		}
	}
}