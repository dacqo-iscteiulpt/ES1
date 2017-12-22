package antiSpamFilter;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

import gui.GUI;

public class AntiSpamFilterProblem extends AbstractDoubleProblem {

	GUI gui;
	
	  public AntiSpamFilterProblem(Integer numberOfVariables, GUI gui) {
		this.gui = gui;
	    setNumberOfVariables(numberOfVariables);
	    setNumberOfObjectives(2);
	    setName("AntiSpamFilterProblem");

	    List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
	    List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

	    for (int i = 0; i < getNumberOfVariables(); i++) {
	      lowerLimit.add(-5.0);
	      upperLimit.add(5.0);
	    }

	    setLowerLimit(lowerLimit);
	    setUpperLimit(upperLimit);
	  }

	  public void evaluate(DoubleSolution solution){
	    double[] x = new double[getNumberOfVariables()];
	    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
	      x[i] = solution.getVariableValue(i) ;
	    }
	    
	    for(int i = 0; i < gui.dataMatrixAutomatic.length; i++) {
	    	gui.dataMatrixAutomatic[i][1] = x[i];
	    }
	    
	    gui.evaluateConfig(gui.dataMatrixAutomatic);
	    solution.setObjective(0, gui.counterFP);
	    solution.setObjective(1, gui.counterFN);
	  }
	}
