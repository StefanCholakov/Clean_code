package bg.sofia.uni.fmi.java.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {

	@SerializedName("list")
	private ResponseContent list;
	@SerializedName("report")
	private ResponseContent report;

	public ApiResponse(ResponseContent list, ResponseContent report) {
		this.list = list;
		this.report = report;
	}

	public List<Food> getSearchResult() {
		return list.getItems();
	}

	public Food getFoodReport() {
		return report.getFood();
	}

	public boolean containsError() {
		return list == null && report == null;
	}
}
