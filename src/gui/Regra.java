package gui;

public class Regra {
	private String name;
	private double peso;
	
	Regra(String name, double peso) {
		this.setName(name);
		this.setPeso(peso);
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
