package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

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