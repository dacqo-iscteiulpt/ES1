package gui;

import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.go();
		
		String[] params = new String [2];

		params[0] = "C:/Program Files/R/R-3.4.3/bin/x64/Rscript.exe";

		params[1] = "C:/Users/Micael/git/ES1-2017-IC1-65/config/AntiSpamStudy/R/HV.Boxplot.R";

		String[] envp = new String [1];

		envp[0] = "Path=C:/Program Files/R/R-3.4.3/bin/x64";
		
		try {
			Process p = Runtime.getRuntime().exec(params, envp, new File("C:/Users/Micael/git/ES1-2017-IC1-65/config/AntiSpamStudy/R"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ProcessBuilder pb = new ProcessBuilder("pdflatex", "-shell-escape", "AntiSpamStudy.tex");
        pb.directory(new File("C:/Users/Micael/git/ES1-2017-IC1-65/config/AntiSpamStudy/latex"));
        try {
            Process p = pb.start();
            p.waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

	}

}
