package gui;

import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.go();
		
		//generate HV.Boxplot.eps
		String[] params = new String [2];

		params[0] = "C:/Program Files/R/R-3.4.3/bin/x64/Rscript.exe";

		params[1] = "C:/Users/Micael/git/ES1-2017-IC1-65/config/AntiSpamStudy/R/HV.Boxplot.R";

		String[] envp = new String [1];

		envp[0] = "Path=C:/Program Files/R/R-3.4.3/bin/x64";

		try {
			Process p1 = Runtime.getRuntime().exec(params, envp, new File("C:/Users/Micael/git/ES1-2017-IC1-65/config/AntiSpamStudy/R"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//generate AntiSpamStudy.pdf 
		params[0] = "C:/Users/Micael/AppData/Local/Programs/MiKTeX 2.9/miktex/bin/x64/pdflatex.exe";

		params[1] = "C:/Users/Micael/git/ES1-2017-IC1-65/config/AntiSpamStudy/latex/AntiSpamStudy.tex";

		envp[0] = "Path=C:/Users/Micael/AppData/Local/Programs/MiKTeX 2.9/miktex/bin/x64";
		
		try {
			Process p2 = Runtime.getRuntime().exec(params, envp, new File("C:/Users/Micael/git/ES1-2017-IC1-65/config/AntiSpamStudy/latex"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
