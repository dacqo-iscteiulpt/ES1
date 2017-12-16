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
 * @author Micael
 * @author David
 * @author Daniel 
 * @author João (Javadoc)
 * @since 16-11-2017
 * This class implements all the components
 * required to build the GUI
*/

public class GUI {

	private JFrame frame = new JFrame("GUI");

	private JPanel topPanel = new JPanel();
	private JLabel rulesLabel = new JLabel("rules.cf path: ");
	private JLabel hamLabel = new JLabel("ham.log path: ");
	private JLabel spamLabel = new JLabel("spam.log path: ");
	private JTextField rulesPath = new JTextField("rules.cf");
	private JTextField hamPath = new JTextField("ham.log");
	private JTextField spamPath = new JTextField("spam.log");

	private JPanel middlePanel = new JPanel();

	private DefaultTableModel manualTableModel;
	private Object[][] dataMatrixManual;
	private JTable manualTable = new JTable();

	private JPanel middleBottomPanel = new JPanel();
	private JPanel middleRightPanel = new JPanel();
	private JPanel middleLeftPanel = new JPanel();

	private JLabel manualLabel = new JLabel("Manual");
	private final String[] columnNames = {"Regras", "Pesos"};

	private JButton generateButton = new JButton("Gerar config");
	private JButton evaluateButton = new JButton("Avaliar config");
	private JButton saveButton = new JButton("Gravar config");

	private JTextField labelFN = new JTextField("Falsos Negativos:\t");
	private JTextField labelFP = new JTextField("Falsos Positivos:\t");

	private JPanel bottomPanel = new JPanel();

	private JLabel automaticLabel = new JLabel("Automatic");
	private JPanel bottomLeftPanel = new JPanel();
	private DefaultTableModel automaticTableModel;
	private Object[][] dataMatrixAutomatic;
	private JTable automaticTable = new JTable();
	private JPanel bottomRightPanel = new JPanel();

	private JButton makeConfigButton = new JButton("Gerar Config");
	private JButton saveConfigButton = new JButton("Gravar Config");

	private int counterFP = 0;
	private int counterFN = 0;

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
		topPanel.setLayout(new GridLayout(3,2));
		topPanel.add(rulesLabel);
		topPanel.add(rulesPath);
		rulesPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readRules();
			}
		});
		topPanel.add(hamLabel);
		topPanel.add(hamPath);
		topPanel.add(spamLabel);
		topPanel.add(spamPath);
		frame.add(topPanel);
	}
	
/**
 * This method builds the middle panel
 * which contains three buttons (generate, evaluate and save).*/
	private void buildMiddle() {
		middlePanel.setLayout(new BorderLayout());
		middlePanel.add(manualLabel, BorderLayout.NORTH);

		manualLabel.setHorizontalAlignment(JLabel.CENTER);

		middleRightPanel.setLayout(new GridLayout(3,1));
		middleRightPanel.add(generateButton);
		generateButton.addActionListener(new ActionListener() { 
			
/**The generate button create a random number between -5 and 5. */
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < dataMatrixManual.length; i++) {
					dataMatrixManual[i][1] = ThreadLocalRandom.current().nextDouble(-5, 5 + 1);
				}
				manualTableModel.setDataVector(dataMatrixManual, columnNames);
			} 
		});
		middleRightPanel.add(evaluateButton);
		evaluateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				evaluateConfig();
			} 
		});
		middleRightPanel.add(saveButton);
		saveButton.addActionListener(new ActionListener() { 
			
/** The save button adds the weights included 
* in {@link GUI#dataMatrixManual} matrix to the rules.txt file. */
			public void actionPerformed(ActionEvent e) {
				try {
					PrintWriter writer = new PrintWriter("rules.txt", "UTF-8");
					for(int i = 0; i < dataMatrixManual.length; i++) {
						writer.write(dataMatrixManual[i][0] + " " + dataMatrixManual[i][1] +"\n");
					}
					writer.close();
					System.out.println("saved to file");
				} catch (FileNotFoundException | UnsupportedEncodingException e1) {
					System.out.println("File not found");
				}
			} 
		});
		middlePanel.add(middleRightPanel, BorderLayout.EAST);

		middleLeftPanel.setLayout(new BorderLayout());
		middleLeftPanel.add(new JScrollPane(manualTable), BorderLayout.CENTER);
		middlePanel.add(middleLeftPanel, BorderLayout.WEST);

		middleBottomPanel.setLayout(new FlowLayout());
		middleBottomPanel.add(labelFN);
		middleBottomPanel.add(labelFP);
		middlePanel.add(middleBottomPanel, BorderLayout.SOUTH);

		frame.add(middlePanel);
	}

/** The evaluateConfig method determines the false negatives and false positives.
*The former increments {@link GUI#counterFP} 
*and the latter increments {@link GUI#counterFN}. */
	private void evaluateConfig() {
		if(!hamPath.getText().isEmpty()) {
			Scanner in;
			try {
				in = new Scanner(new FileReader(hamPath.getText()));
				while(in.hasNext()) {
					String text = in.nextLine();
					String[] line = text.split("\t");
					double count = 0;
					for(int i = 1; i < line.length; i++) {
						for(int j =  0; j < dataMatrixManual.length; j++)  {
							if(line[i].equals((String)dataMatrixManual[j][0])) {
								double amount = (double)dataMatrixManual[j][1];
								count += amount;
							}
						}
					}
					if(count < 0) {
						counterFP++;
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
						for(int j =  0; j < dataMatrixManual.length; j++)  {
							if(line[i].equals((String)dataMatrixManual[j][0])) {
								double amount = (double)dataMatrixManual[j][1];
								count += amount;
							}
						}
					}
					if(count < 0) {
						counterFN++;
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("Path invalido");
			}
		}
		labelFP.setText("Falsos Positivos: " + counterFP);
		labelFN.setText("Falsos Negativos: " + counterFN);
		counterFN = 0;
		counterFP = 0;
	}

/** The method readRules reads the file rules.txt
 * and adds all its contents to an arraylist. 
 * Then iterates the arraylist and writes to the matrix ({@link GUI#dataMatrixManual}, {@link GUI#dataMatrixAutomatic}) the rule and 
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

				dataMatrixManual = new Object[Regras.size()][2];
				dataMatrixAutomatic = new Object[Regras.size()][2];
				int i = 0;
				for(Regra r: Regras) {
					dataMatrixManual[i][0] = r.getName();
					dataMatrixManual[i][1] = 0.0;
					dataMatrixAutomatic[i][0] = r.getName();
					dataMatrixAutomatic[i][1] = 0.0;
					i++;
				}

				manualTableModel = new DefaultTableModel(dataMatrixManual, columnNames);
				automaticTableModel = new DefaultTableModel(dataMatrixAutomatic, columnNames);
				manualTable.setModel(manualTableModel);
				automaticTable.setModel(automaticTableModel);
			} catch (FileNotFoundException e) {
				System.out.println("Path invalido");
			}
		}
	}

/**
* This method builds the bottom panel 
* */
	private void buildBottom() {
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(automaticLabel, BorderLayout.NORTH);
		automaticLabel.setHorizontalAlignment(JLabel.CENTER);

		bottomPanel.add(bottomLeftPanel, BorderLayout.WEST);

		bottomLeftPanel.setLayout(new BorderLayout());
		bottomLeftPanel.add(new JScrollPane(automaticTable), BorderLayout.CENTER);
		automaticTable.setEnabled(false);

		bottomRightPanel.setLayout(new GridLayout(2,1));
		bottomRightPanel.add(makeConfigButton);
		bottomRightPanel.add(saveConfigButton);

		bottomPanel.add(bottomRightPanel, BorderLayout.EAST);
		frame.add(bottomPanel);

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