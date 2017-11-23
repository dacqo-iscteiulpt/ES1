package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
	
//	private JCheckBox
	
	private JPanel bottom = new JPanel();


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
		middleBottom.setLayout(new FlowLayout());
		middle.add(middleBottom, BorderLayout.SOUTH);
	}


	private void buildBottom() {
		// TODO Auto-generated method stub
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