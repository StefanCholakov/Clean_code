package bg.sofia.uni.fmi.java.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ResponseContent {

	@SerializedName("item")
	private List<Food> items;

	@SerializedName("food")
	private Food food;

	public ResponseContent(List<Food> items, Food food) {
		this.items = items;
		this.food = food;
	}

	public List<Food> getItems() {
		return items;
	}

	public Food getFood() {
		return food;
	}

}
