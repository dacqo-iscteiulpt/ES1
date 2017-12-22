package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.ComputeQualityIndicators;
import org.uma.jmetal.util.experiment.component.ExecuteAlgorithms;
import org.uma.jmetal.util.experiment.component.GenerateBoxplotsWithR;
import org.uma.jmetal.util.experiment.component.GenerateLatexTablesWithStatistics;
import org.uma.jmetal.util.experiment.component.GenerateReferenceParetoSetAndFrontFromDoubleSolutions;
import org.uma.jmetal.util.experiment.util.ExperimentAlgorithm;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;
import antiSpamFilter.AntiSpamFilterProblem;

/**
 * 
 * 
 * This class implements all the components required to build the GUI
 * 
 * 
 * @author Micael
 * @author David
 * @author Daniel
 * @author Joao 
 * @since 16-11-2017 
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
	private final String[] columnNames = { "Regras", "Pesos" };

	private JButton generateButton = new JButton("Gerar config");
	private JButton saveButton = new JButton("Gravar config");

	private JTextField labelFN = new JTextField("Falsos Negativos:\t");
	private JTextField labelFP = new JTextField("Falsos Positivos:\t");

	private JPanel bottomPanel = new JPanel();

	private JLabel automaticLabel = new JLabel("Automatic");
	private JPanel bottomLeftPanel = new JPanel();
	private DefaultTableModel automaticTableModel;
	public Object[][] dataMatrixAutomatic;
	private JTable automaticTable = new JTable();
	private JPanel bottomRightPanel = new JPanel();

	private JButton makeConfigButton = new JButton("Gerar Config");
	private JButton saveConfigButton = new JButton("Gravar Config");

	public int counterFP = 0;
	public int counterFN = 0;

	private String Path = "config/referenceFronts/AntiSpamFilterProblem.rs";

	private List<Regra> regras;

	/**
	 * This method runs all the methods and displays the GUI
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
	 * This method builds the top panel which specifies the paths for the ham,
	 * spam and rules' files 
	 */
	private void buildTop() {
		topPanel.setLayout(new GridLayout(3, 2));
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
	 * which contains three buttons (generate and save).
	 */
	private void buildMiddle() {
		middlePanel.setLayout(new BorderLayout());
		middlePanel.add(manualLabel, BorderLayout.NORTH);

		manualLabel.setHorizontalAlignment(JLabel.CENTER);

		middleRightPanel.setLayout(new GridLayout(3,1));
		middleRightPanel.add(generateButton);
		generateButton.addActionListener(new ActionListener() { 

			/**
			 * The generate button create a random number 
			 * between -5 and 5 and calls evaluateConfig method 
			*/
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < dataMatrixManual.length; i++) {
					dataMatrixManual[i][1] = ThreadLocalRandom.current().nextDouble(-5, 5 + 1);
				}
				manualTableModel.setDataVector(dataMatrixManual, columnNames);
				evaluateConfig(dataMatrixManual);
			} 
		});
		middleRightPanel.add(saveButton);
		saveButton.addActionListener(new ActionListener() { 

			/** The save button adds the weights included 
			 * in {@link GUI#dataMatrixManual} matrix to the rules.cf file. 
			 * @throws FileNotFoundException
			 * @throws UnsupportedEncodingException
			 */
			public void actionPerformed(ActionEvent e) {
				try {
					PrintWriter writer = new PrintWriter("rules.cf", "UTF-8");
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

	/**
	 * The evaluateConfig method determines the false negatives and false
	 * positives. The former increments {@link GUI#counterFP} and the
	 * latter increments {@link GUI#counterFN}.
	 * @param data is the matrix to be evaluated.
	 */
	public void evaluateConfig(Object[][] data) {
		if (!hamPath.getText().isEmpty()) {
			Scanner in;
			counterFP = 0;
			
			try {
				in = new Scanner(new FileReader(hamPath.getText()));
				while (in.hasNext()) {
					String text = in.nextLine();
					String[] line = text.split("\t");
					double count = 0;
					for(int i = 1; i < line.length; i++) {
						for(int j =  0; j < data.length; j++)  {
							if(line[i].equals((String)data[j][0])) {
								double amount = (double)data[j][1];
								count += amount;
							}
						}
					}
					if (count < 0) {
						counterFP++;
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("Path invalido");
			}
		}

		if (!spamPath.getText().isEmpty()) {
			Scanner in;
			counterFN = 0;
			try {
				in = new Scanner(new FileReader(spamPath.getText()));
				while (in.hasNext()) {
					String text = in.nextLine();
					String[] line = text.split("\t");
					double count = 0;
					for(int i = 1; i < line.length; i++) {
						for(int j =  0; j < data.length; j++)  {
							if(line[i].equals((String)data[j][0])) {
								double amount = (double)data[j][1];
								count += amount;
							}
						}
					}
					if (count < 0) {
						counterFN++;
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("Path invalido");
			}
		}
		labelFP.setText("Falsos Positivos: " + counterFP);
		labelFN.setText("Falsos Negativos: " + counterFN);
	}

	/** The method readRules reads the file rules.cf
	 * and adds all its contents to an arraylist. 
	 * Then iterates the arraylist and writes to the matrix ({@link GUI#dataMatrixManual}, {@link GUI#dataMatrixAutomatic}) the rule and 
	 * its respective weight.     
	 * 
	*/
	private void readRules() {
		if (!rulesPath.getText().isEmpty()) {
			Scanner in;
			try {
				in = new Scanner(new FileReader(rulesPath.getText()));
				regras = new ArrayList<Regra>();
				while (in.hasNext()) {
					String line = in.nextLine();
					String[] rule = line.split(" ");
					regras.add(new Regra(rule[0], Double.parseDouble(rule[1])));
				}

				dataMatrixManual = new Object[regras.size()][2];
				dataMatrixAutomatic = new Object[regras.size()][2];
				int i = 0;
				for (Regra r : regras) {
					dataMatrixManual[i][0] = r.getName();
					dataMatrixManual[i][1] = r.getWeight();
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
	 * This method builds the bottom panel which 
	 * contains {@link GUI#makeConfigButton} and {@link GUI#saveConfigButton}
	 */
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

		makeConfigButton.addActionListener(new ActionListener() {

			/** The {@link GUI#makeConfigButton} calls 
			 * two methods automaticConfig and evaluateConfig.
			 * throws IOException
			 */
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					automaticConfig();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		saveConfigButton.addActionListener(new ActionListener() {
		
			/** 
			 * The {@link GUI#saveConfigButton} adds the weights included 
			 * in {@link GUI#dataMatrixAutomatic} matrix to the rules.cf file
			 * @throws FileNotFoundException
			 * @throws UnsupportedEncodingException
			 */
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					PrintWriter writer = new PrintWriter("rules.cf", "UTF-8");
					for (int i = 0; i < dataMatrixAutomatic.length; i++) {
						writer.write(dataMatrixAutomatic[i][0] + " " + dataMatrixAutomatic[i][1] + "\n");
					}
					writer.close();
					System.out.println("saved to file");
					evaluateConfig(dataMatrixAutomatic);
				} catch (FileNotFoundException | UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			}
		});
		bottomPanel.add(bottomRightPanel, BorderLayout.EAST);
		frame.add(bottomPanel);
	}

	private void automaticConfig() throws IOException {

		final int INDEPENDENT_RUNS = 1;

		String experimentBaseDirectory = "config";

		List<ExperimentProblem<DoubleSolution>> problemList = new ArrayList<>();
		problemList.add(new ExperimentProblem<>(new AntiSpamFilterProblem(regras.size(),this)));

		List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithmList = configureAlgorithmList(
				problemList);

		Experiment<DoubleSolution, List<DoubleSolution>> experiment = new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>(
				"AntiSpamStudy").setAlgorithmList(algorithmList).setProblemList(problemList)
				.setExperimentBaseDirectory(experimentBaseDirectory).setOutputParetoFrontFileName("FUN")
				.setOutputParetoSetFileName("VAR")
				.setReferenceFrontDirectory(experimentBaseDirectory + "/referenceFronts")
				.setIndicatorList(Arrays.asList(new PISAHypervolume<DoubleSolution>()))
				.setIndependentRuns(INDEPENDENT_RUNS).setNumberOfCores(8).build();

		new ExecuteAlgorithms<>(experiment).run();
		new GenerateReferenceParetoSetAndFrontFromDoubleSolutions(experiment).run();
		new ComputeQualityIndicators<>(experiment).run();
		new GenerateLatexTablesWithStatistics(experiment).run();
		new GenerateBoxplotsWithR<>(experiment).setRows(1).setColumns(1).run();

		readResults();

	}

	/** The method readResults reads the file Path
	 * and adds the first line to the {@link GUI#dataMatrixAutomatic}
	 * @throws IOException Signals that an I/O exception of some sort has occurred. 
	 */
	private void readResults() throws IOException {
		Scanner in;
		try {
			String x = "config/referenceFronts/AntiSpamFilterProblem.rf";
			ArrayList<Double> temp = new ArrayList<Double>();
			in = new Scanner(new FileReader(x));
			while(in.hasNextLine()) {
				String[] line;
				line = in.nextLine().split(" ");
				temp.add(Double.parseDouble(line[1]));
			}
			in.close();
			int minIndex = (int)temp.indexOf(Collections.min(temp));
			String minLine = Files.readAllLines(Paths.get(Path)).get(minIndex);
			String[] weights = minLine.split(" ");
			int i = 0;
			for (Regra r : regras) {
				dataMatrixAutomatic[i][1] = Double.parseDouble(weights[i]);
				i++;
			}
			automaticTableModel = new DefaultTableModel(dataMatrixAutomatic, columnNames);
			automaticTable.setModel(automaticTableModel);
			evaluateConfig(dataMatrixAutomatic);
		} catch (FileNotFoundException e) {
			System.out.println("Path invalido");
		}
	}

	static List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> configureAlgorithmList(
			List<ExperimentProblem<DoubleSolution>> problemList) {
		List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithms = new ArrayList<>();

		for (int i = 0; i < problemList.size(); i++) {
			Algorithm<List<DoubleSolution>> algorithm = new NSGAIIBuilder<>(problemList.get(i).getProblem(),
					new SBXCrossover(1.0, 5),
					new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 10.0))
					.setMaxEvaluations(100).setPopulationSize(100).build();
			algorithms.add(new ExperimentAlgorithm<>(algorithm, "NSGAII", problemList.get(i).getTag()));
		}

		return algorithms;
	}

	private void finishFrame() {
		frame.pack();
		frame.setVisible(true);
	}

	private void buildFrame() {
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(3, 1));
	}

}