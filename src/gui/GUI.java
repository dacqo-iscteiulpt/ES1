package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GUI {

	private JFrame frame = new JFrame("GUI");

	private JPanel top = new JPanel();
	private JLabel rules = new JLabel("rules.cf path: ");
	private JLabel ham = new JLabel("ham.log path: ");
	private JLabel spam = new JLabel("spam.log path: ");
	private JTextField rulesPath = new JTextField("");
	private JTextField hamPath = new JTextField("");
	private JTextField spamPath = new JTextField("");
	
	private JPanel middle = new JPanel();
	
	private JPanel middleBottom = new JPanel();
	private JPanel middleRight = new JPanel();
	private JPanel middleLeft = new JPanel();
	
	private JLabel manual = new JLabel("Manual");
	private JLabel rulesWeights = new JLabel("Regras            Pesos");
	
	private JTable middleTable = new JTable(5, 2);
	
	private JButton generate = new JButton("Gerar config");
	private JButton evaluate = new JButton("Avaliar config");
	private JButton save = new JButton("Gravar config");
	
	private JCheckBox fakeNegatives = new JCheckBox("Falsos Negativos");
	private JCheckBox fakePositives = new JCheckBox("Falsos Positivos");
	
	private JPanel bottom = new JPanel();

	private JLabel automatic = new JLabel("                             Automatic");
	private JPanel bottomPanel = new JPanel();
	private JPanel bottomLeft = new JPanel();
	private JLabel rulesWeight = new JLabel("Rules            Weights");
	private JTable bottomTable = new JTable(5,2);
	private JPanel bottomRight = new JPanel();
	private JButton makeConfig = new JButton("Make Config");
	private JButton saveConfig = new JButton("Save Config");
	
	public void go() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				buildFrame();
				buildTop();
				buildMiddle();
				buildBottom();
				finishFrame();
			}
		});
	}


	private void buildTop() {
		top.setLayout(new GridLayout(3,2));
		top.add(rules);
		top.add(rulesPath);
		top.add(ham);
		top.add(hamPath);
		top.add(spam);
		top.add(spamPath);
		frame.add(top);
	}

	private void buildMiddle() {
		middle.setLayout(new BorderLayout());
		middle.add(manual, BorderLayout.NORTH);
		
		manual.setHorizontalAlignment(JLabel.CENTER);
		
		middleRight.setLayout(new GridLayout(3,1));
		middleRight.add(generate);
		middleRight.add(evaluate);
		middleRight.add(save);
		middle.add(middleRight, BorderLayout.EAST);
		
		middleLeft.setLayout(new BorderLayout());
		middleLeft.add(rulesWeights, BorderLayout.NORTH);
		middleLeft.add(middleTable, BorderLayout.CENTER);
		middle.add(middleLeft, BorderLayout.WEST);
	    		
		middleBottom.setLayout(new FlowLayout());
		middleBottom.add(fakeNegatives);
		middleBottom.add(fakePositives);
		middle.add(middleBottom, BorderLayout.SOUTH);
		
		frame.add(middle);
	}


	private void buildBottom() {
		bottom.setLayout(new BorderLayout());
		bottom.add(automatic, BorderLayout.NORTH);		
		bottom.add(bottomPanel);
		
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.add(bottomLeft);
		
		bottomLeft.setLayout(new BorderLayout());
		bottomLeft.add(rulesWeight, BorderLayout.NORTH);
		bottomLeft.add(bottomTable);
		
		bottomRight.setLayout(new GridLayout(2,1));
		bottomRight.add(makeConfig);
		bottomRight.add(saveConfig);
		
		bottomPanel.add(bottomRight);
		frame.add(bottom);
		
	}


	private void finishFrame() {
		frame.pack();
		frame.setVisible(true);		
	}


	private void buildFrame() {
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(3,1));
	}

}