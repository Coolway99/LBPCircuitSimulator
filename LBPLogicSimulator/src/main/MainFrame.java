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
	
	public MainFrame(){
		super();
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("TestFrame");
		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		setName("mainFrame"); // NOI18N
		
		this.menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		this.menuItemOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = MainFrame.this.fc.showOpenDialog(LBPLogicSimulator.mainFrame);
				if(value == JFileChooser.APPROVE_OPTION){
					//TODO Open Dialog
				}
			}
		});
		this.menuFile.add(this.menuItemOpen);
		
		this.menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		this.menuItemSave.setText("Save...");
		this.menuItemSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(MainFrame.this.fc.getSelectedFile() == null){
					MainFrame.this.menuItemSaveAs.doClick();
				} else {
					//TODO Saving
				}
			}
		});
		this.menuFile.add(this.menuItemSave);
		
		this.menuItemSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
		this.menuItemSaveAs.setText("Save As...");
		this.menuItemSaveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = MainFrame.this.fc.showSaveDialog(LBPLogicSimulator.mainFrame);
				if(value == JFileChooser.APPROVE_OPTION){
					//TODO Saving
				}
			}
		});
		this.menuFile.add(this.menuItemSaveAs);
		
		this.menuBar.add(this.menuFile);
		
		this.menuBar.add(this.menuEdit);
		
		this.menuOptionANDGate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.this.mainPanel.addLogicGate(new AND_Gate());
			}
		});
		this.subMenuSimpleLogic.add(this.menuOptionANDGate);
		
		this.subMenuSimpleLogic.add(this.menuOptionORGate);
		
		this.subMenuSimpleLogic.add(this.menuOptionNOTGate);
		
		this.subMenuSimpleLogic.add(this.menuOptionXORGate);
		
		this.subMenuAddNew.add(this.subMenuSimpleLogic);
		
		this.subMenuAddNew.add(this.subMenuAdvancedLogic);
		
		this.menuBar.add(this.subMenuAddNew);
		
		setJMenuBar(this.menuBar);
		add(this.mainPanel);
		pack();
	}
}