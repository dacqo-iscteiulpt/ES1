package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

public class GUI {

	private JFrame frame = new JFrame("GUI");

	private JPanel top = new JPanel();
	private JLabel rules = new JLabel("rules.cf path: ");
	private JLabel ham = new JLabel("ham.log path: ");
	private JLabel spam = new JLabel("spam.log path: ");
	private JTextField rulesPath = new JTextField("/Users/Ltfx/Documents/EngenheriaSoftware/rules.cf");
	private JTextField hamPath = new JTextField("/Users/Ltfx/Documents/EngenheriaSoftware/ham.log");
	private JTextField spamPath = new JTextField("/Users/Ltfx/Documents/EngenheriaSoftware/spam.log");

	private JPanel middle = new JPanel();

	private TableModel model1;
	private JTable middleTable = new JTable();

	private JPanel middleBottom = new JPanel();
	private JPanel middleRight = new JPanel();
	private JPanel middleLeft = new JPanel();

	private JLabel manual = new JLabel("Manual");


	private JButton generate = new JButton("Gerar config");
	private JButton evaluate = new JButton("Avaliar config");
	private JButton save = new JButton("Gravar config");

	private JCheckBox fakeNegatives = new JCheckBox("Falsos Negativos");
	private JCheckBox fakePositives = new JCheckBox("Falsos Positivos");

	private JPanel bottom = new JPanel();

	private JLabel automatic = new JLabel("Automatic");
	private JPanel bottomLeft = new JPanel();
	private JTable bottomTable = new JTable();
	private TableModel model2;
	private JPanel bottomRight = new JPanel();

	private JButton makeConfig = new JButton("Gerar Config");
	private JButton saveConfig = new JButton("Gravar Config");

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
		rulesPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readRules();
			}
		});
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
		middleTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					int row = middleTable.getSelectedRow();
					int column = middleTable.getSelectedColumn();
					System.out.println("Row: " + row + ", Column: " + column);
//					double temp = (double) model1.getValueAt(row, column);
//					middleTable.setValueAt(temp, row, column);

				}
			}
		});
		middleLeft.add(new JScrollPane(middleTable), BorderLayout.CENTER);
		middle.add(middleLeft, BorderLayout.WEST);

		middleBottom.setLayout(new FlowLayout());
		middleBottom.add(fakeNegatives);
		middleBottom.add(fakePositives);
		middle.add(middleBottom, BorderLayout.SOUTH);

		frame.add(middle);
	}


	private void readRules() {
		if(!rulesPath.getText().isEmpty()) {
			Scanner in;
			try {
				in = new Scanner(new FileReader(rulesPath.getText()));
				List<Regra> Regras = new ArrayList<Regra>();
				while(in.hasNext()) {
					String text = in.next();
					Regras.add(new Regra(text, 0));
				}
				model1 = new CustomTableModel(Regras);
				model2 = new CustomTableModel(Regras);
				middleTable.setModel(model1);
				bottomTable.setModel(model2);
			} catch (FileNotFoundException e) {
				System.out.println("Path invalido");
			}
		}
	}


	private void buildBottom() {
		bottom.setLayout(new BorderLayout());
		bottom.add(automatic, BorderLayout.NORTH);
		automatic.setHorizontalAlignment(JLabel.CENTER);
		
		bottom.add(bottomLeft, BorderLayout.WEST);

		bottomLeft.setLayout(new BorderLayout());
		bottomLeft.add(new JScrollPane(bottomTable), BorderLayout.CENTER);
		bottomTable.setEnabled(false);

		bottomRight.setLayout(new GridLayout(2,1));
		bottomRight.add(makeConfig);
		bottomRight.add(saveConfig);

		bottom.add(bottomRight, BorderLayout.EAST);
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