package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import main.simpleLogicGates.AND_Gate;

public class MainFrame extends javax.swing.JFrame {
	private static final long serialVersionUID = 443248416294991918L;
	
	public MainPanel mainPanel = new MainPanel();
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuFile = new JMenu("File");
	private JMenuItem menuItemOpen = new JMenuItem("Open...");
	private JMenuItem menuItemSave = new JMenuItem("Save");
	private JMenuItem menuItemSaveAs = new JMenuItem("Save as...");
	private JMenu menuEdit = new JMenu("Edit");
	private JMenu subMenuAddNew = new JMenu("Add New");
	private JMenu subMenuSimpleLogic = new JMenu("Simple Logic");
	private JMenuItem menuOptionANDGate = new JMenuItem("AND Gate");
	private JMenuItem menuOptionORGate = new JMenuItem("OR Gate");
	private JMenuItem menuOptionNOTGate = new JMenuItem("NOT Gate");
	private JMenuItem menuOptionXORGate = new JMenuItem("XOR Gate");
	private JMenu subMenuAdvancedLogic = new JMenu("Advanced Logic");
	public JFileChooser fc = new JFileChooser();
	
	public static void main(String args[]) {
		new MainFrame().setVisible(true);
	}

	public MainFrame(){
		super();
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("TestFrame");
		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		setName("mainFrame"); // NOI18N
		
		menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		menuItemOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = fc.showOpenDialog(Main.mainFrame);
				if(value == JFileChooser.APPROVE_OPTION){
					//TODO Open Dialog
				}
			}
		});
		menuFile.add(menuItemOpen);
		
		menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		menuItemSave.setText("Save...");
		menuItemSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(fc.getSelectedFile() == null){
					menuItemSaveAs.doClick();
				} else {
					//TODO Saving
				}
			}
		});
		menuFile.add(menuItemSave);
		
		menuItemSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
		menuItemSaveAs.setText("Save As...");
		menuItemSaveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = fc.showSaveDialog(Main.mainFrame);
				if(value == JFileChooser.APPROVE_OPTION){
					//TODO Saving
				}
			}
		});
		menuFile.add(menuItemSaveAs);
		
		menuBar.add(menuFile);
		
		menuBar.add(menuEdit);
		
		menuOptionANDGate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.addLogicGate(new AND_Gate());
			}
		});
		subMenuSimpleLogic.add(menuOptionANDGate);
		
		subMenuSimpleLogic.add(menuOptionORGate);
		
		subMenuSimpleLogic.add(menuOptionNOTGate);
		
		subMenuSimpleLogic.add(menuOptionXORGate);
		
		subMenuAddNew.add(subMenuSimpleLogic);
		
		subMenuAddNew.add(subMenuAdvancedLogic);
		
		menuBar.add(subMenuAddNew);
		
		setJMenuBar(menuBar);
		add(mainPanel);
		pack();
	}
}