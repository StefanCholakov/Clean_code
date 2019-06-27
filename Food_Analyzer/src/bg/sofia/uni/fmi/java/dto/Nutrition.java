package bg.sofia.uni.fmi.java.dto;

public class Nutrition {

	private String name;
	private String unit;
	private String value;

	public Nutrition(String name, String unit, String value) {
		this.name = name;
		this.unit = unit;
		this.value = value;
	}

	@Override
	public String toString() {
		return "Nutrition: " + name + " " + value + unit + "\n";
	}
}
