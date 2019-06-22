package bg.sofia.uni.fmi.java.dto;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.annotations.SerializedName;

public class Food {

	@SerializedName("name")
	private String name;

	@SerializedName("ndbno")
	private String ndbNumber;

	@SerializedName("nutrients")
	private List<Nutrition> nutritients;

	@SerializedName("manu")
	private String manufacturer;

	public Food(String name, String ndbNumber, List<Nutrition> nutritients, String manufacturer) {
		this.name = name;
		this.ndbNumber = ndbNumber;
		this.nutritients = nutritients;
		this.manufacturer = manufacturer;
	}

	public String getUpcCode() {
		Pattern regexPattern = Pattern.compile("\\d+");
		Matcher matcher = regexPattern.matcher(name);
		matcher.find();
		String upcCode = matcher.group(0);

		return upcCode;
	}

	public String getSearchResult() {
		StringBuilder searchResult = new StringBuilder();

		searchResult.append("Full name: " + name + "\n");
		searchResult.append("NDB number: " + ndbNumber + "\n");
		searchResult.append("Manufacturer: " + manufacturer);

		return searchResult.toString();
	}

	public String getReportResult() {
		StringBuilder report = new StringBuilder();

		report.append(this.getSearchResult());
		for (Nutrition nutrition : nutritients) {
			report.append(nutrition.toString());
		}
		return report.toString();
	}

}
