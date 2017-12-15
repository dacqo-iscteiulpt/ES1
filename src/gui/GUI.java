package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * This class implements all the components
 * required to build the GUI
 * 
 * @author Micael
 * @author David
 * @author Daniel
 * @author João (Javadoc)
 * @since 16-11-2017
 *
 */

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

	private DefaultTableModel model1;
	private Object[][] data1;
	private JTable middleTable = new JTable();

	private JPanel middleBottom = new JPanel();
	private JPanel middleRight = new JPanel();
	private JPanel middleLeft = new JPanel();

	private JLabel manual = new JLabel("Manual");
	private final String[] columnNames = {"Regras", "Pesos"};

	private JButton generate = new JButton("Gerar config");
	private JButton evaluate = new JButton("Avaliar config");
	private JButton save = new JButton("Gravar config");

	private JTextField fakeNegatives = new JTextField("Falsos Negativos:\t");
	private JTextField fakePositives = new JTextField("Falsos Positivos:\t");

	private JPanel bottom = new JPanel();

	private JLabel automatic = new JLabel("Automatic");
	private JPanel bottomLeft = new JPanel();
	private DefaultTableModel model2;
	private Object[][] data2;
	private JTable bottomTable = new JTable();
	private JPanel bottomRight = new JPanel();

	private JButton makeConfig = new JButton("Gerar Config");
	private JButton saveConfig = new JButton("Gravar Config");

	private int falsosPositivos = 0;
	private int falsosNegativos = 0;

/**
 * This method runs all 
 * the methods and displays the GUI
 */
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

/**
 * This method builds the top panel 
 * which specifies the paths for the ham, spam and rules' files */
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
	
/**
 * This method builds the middle panel
 * which contains three buttons (generate, evaluate and save).*/
	private void buildMiddle() {
		middle.setLayout(new BorderLayout());
		middle.add(manual, BorderLayout.NORTH);

		manual.setHorizontalAlignment(JLabel.CENTER);

		middleRight.setLayout(new GridLayout(3,1));
		middleRight.add(generate);
		generate.addActionListener(new ActionListener() { 
			
/**The generate button create a random number between -5 and 5. */
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < data1.length; i++) {
					data1[i][1] = ThreadLocalRandom.current().nextDouble(-5, 5 + 1);
				}
				model1.setDataVector(data1, columnNames);
			} 
		});
		middleRight.add(evaluate);
		evaluate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				evaluateConfig();
			} 
		});
		middleRight.add(save);
		save.addActionListener(new ActionListener() { 
			
/** The save button adds the weights included 
* in {@link GUI#data1} matrix to the rules.txt file. */
			public void actionPerformed(ActionEvent e) {
				try {
					PrintWriter writer = new PrintWriter("rules.txt", "UTF-8");
					for(int i = 0; i < data1.length; i++) {
						writer.write(data1[i][0] + " " + data1[i][1] +"\n");
					}
					writer.close();
					System.out.println("saved to file");
				} catch (FileNotFoundException | UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} 
		});
		middle.add(middleRight, BorderLayout.EAST);

		middleLeft.setLayout(new BorderLayout());
		middleLeft.add(new JScrollPane(middleTable), BorderLayout.CENTER);
		middle.add(middleLeft, BorderLayout.WEST);

		middleBottom.setLayout(new FlowLayout());
		middleBottom.add(fakeNegatives);
		middleBottom.add(fakePositives);
		middle.add(middleBottom, BorderLayout.SOUTH);

		frame.add(middle);
	}

/** The evaluateConfig method determines the false negatives and false positives.
*The former increments {@link GUI#falsosPositivos} 
*and the latter increments {@link GUI#falsosNegativos}. */
	protected void evaluateConfig() {
		if(!hamPath.getText().isEmpty()) {
			Scanner in;
			try {
				in = new Scanner(new FileReader(hamPath.getText()));
				while(in.hasNext()) {
					String text = in.nextLine();
					String[] line = text.split("\t");
					double count = 0;
					for(int i = 1; i < line.length; i++) {
						for(int j =  0; j < data1.length; j++)  {
							if(line[i].equals((String)data1[j][0])) {
								double amount = (double)data1[j][1];
								count += amount;
							}
						}
					}
					if(count < 0) {
						falsosPositivos++;
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("Path invalido");
			}
		}

		if(!spamPath.getText().isEmpty()) {
			Scanner in;
			try {
				in = new Scanner(new FileReader(spamPath.getText()));
				while(in.hasNext()) {
					String text = in.nextLine();
					String[] line = text.split("\t");
					double count = 0;
					for(int i = 1; i < line.length; i++) {
						for(int j =  0; j < data1.length; j++)  {
							if(line[i].equals((String)data1[j][0])) {
								double amount = (double)data1[j][1];
								count += amount;
							}
						}
					}
					if(count < 0) {
						falsosNegativos++;
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("Path invalido");
			}
		}
		fakePositives.setText("Falsos Positivos: " + falsosPositivos);
		fakeNegatives.setText("Falsos Negativos: " + falsosNegativos);
		falsosNegativos = 0;
		falsosPositivos = 0;
	}

/** The method readRules reads the file rules.txt
 * and adds all its contents to an arraylist. 
 * Then iterates the arraylist and writes to the matrix ({@link GUI#data1}, {@link GUI#data2}) the rule and 
 * its respective weight.     */
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

				data1 = new Object[Regras.size()][2];
				data2 = new Object[Regras.size()][2];
				int i = 0;
				for(Regra r: Regras) {
					data1[i][0] = r.getName();
					data1[i][1] = 0.0;
					data2[i][0] = r.getName();
					data2[i][1] = 0.0;
					i++;
				}

				model1 = new DefaultTableModel(data1, columnNames);
				model2 = new DefaultTableModel(data2, columnNames);
				middleTable.setModel(model1);
				bottomTable.setModel(model2);
			} catch (FileNotFoundException e) {
				System.out.println("Path invalido");
			}
		}
	}

/**
* This method builds the bottom panel 
* */
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