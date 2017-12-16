package gui;

public class Regra {
	private String name;
	private double weight;
	
	public Regra(String name, double weight) {
		this.setName(name);
		this.setWeight(weight);
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
