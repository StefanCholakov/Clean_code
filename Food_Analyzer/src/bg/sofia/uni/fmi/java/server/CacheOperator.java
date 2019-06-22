package bg.sofia.uni.fmi.java.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import bg.sofia.uni.fmi.java.dto.Food;
import bg.sofia.uni.fmi.java.exceptions.FoodNotFoundException;

public class CacheOperator {

	private static final Type FOOD_MAP_TYPE = new TypeToken<HashMap<String, Food>>() {
	}.getType();;

	private Gson gson;
	private File usersFile;

	public CacheOperator(Path filePath) {
		gson = new GsonBuilder().setPrettyPrinting().create();
		usersFile = filePath.toFile();
	}

	public synchronized void addFoodInCache(Food food) {
		if (isFoodExisting(food)) {
			return;
		}
		HashMap<String, Food> cacheContent = getAllFoods();
		cacheContent.put(food.getUpcCode(), food);
		writeFoodsInCache(cacheContent);
	}

	public Food getFoodByUpc(String upc) throws FoodNotFoundException {
		Map<String, Food> allFoods = getAllFoods();
		if (!allFoods.containsKey(upc)) {
			throw new FoodNotFoundException("Food with given barcode does not exist");
		}
		return allFoods.get(upc);
	}

	private boolean isFoodExisting(Food food) {
		String upcCode = food.getUpcCode();
		return getAllFoods().containsKey(upcCode);
	}

	private HashMap<String, Food> getAllFoods() {
		try (JsonReader reader = new JsonReader(new FileReader(usersFile));) {
			HashMap<String, Food> allFoods = gson.fromJson(reader, FOOD_MAP_TYPE);
			if (allFoods == null) {
				return new HashMap<String, Food>();
			}
			return allFoods;
		} catch (IOException e) {
			System.err.println("Cache file was not found");
			return new HashMap<String, Food>();
		}
	}

	private synchronized void writeFoodsInCache(HashMap<String, Food> foods) {
		try (PrintWriter writer = new PrintWriter(new FileOutputStream(usersFile))) {
			gson.toJson(foods, writer);
		} catch (IOException e) {
			System.err.println("An error occured while trying to write food reports in cache");
			System.err.println(e.getMessage());
		}
	}
}
