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

/**
 * A class representing the server's cache. Provides methods for adding food in
 * the cache and extracting food information from it by a given criteria.
 */
public class CacheOperator {

	private static final Type FOOD_MAP_TYPE = new TypeToken<HashMap<String, Food>>() {
	}.getType();;

	private Gson gson;
	private File cache;

	public CacheOperator(Path filePath) {
		gson = new GsonBuilder().setPrettyPrinting().create();
		cache = filePath.toFile();
	}

	/**
	 * Adds a food in the server's cache.
	 * 
	 * @param food to be added
	 */
	public synchronized void addFood(Food food) {
		if (isFoodExisting(food)) {
			return;
		}
		HashMap<String, Food> cacheContent = getAllFoods();
		cacheContent.put(food.getUpcCode(), food);
		writeFoodsInCache(cacheContent);
	}

	/**
	 * @param upc code of the food.
	 * @return Food with the specified UPC code if it exists in the cache.
	 * @throws FoodNotFoundException if there is no food with the UPC code
	 *                               specified.
	 */
	public Food getFoodByUpc(String upc) throws FoodNotFoundException {
		Map<String, Food> allFoods = getAllFoods();
		if (!allFoods.containsKey(upc)) {
			throw new FoodNotFoundException("Food with given barcode does not exist");
		}
		return allFoods.get(upc);
	}

	/**
	 * Checks whether certain food exists in the cache. Comparison is made by an UPC
	 * code.
	 * 
	 * @param food to be checked
	 */
	private boolean isFoodExisting(Food food) {
		String upcCode = food.getUpcCode();
		return getAllFoods().containsKey(upcCode);
	}

	/**
	 * Returns the current content of the cache. It is represented by a map, in
	 * which the key is the UPC code of a certain food and the value is the food
	 * itself.
	 */
	private HashMap<String, Food> getAllFoods() {
		try (JsonReader reader = new JsonReader(new FileReader(cache));) {
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

	/**
	 * Writes the cache content in the file.
	 * 
	 * @param foods - map in which the key is the UPC code of a certain food and the
	 *              value is the food itself.
	 */
	private synchronized void writeFoodsInCache(HashMap<String, Food> foods) {
		try (PrintWriter writer = new PrintWriter(new FileOutputStream(cache))) {
			gson.toJson(foods, writer);
		} catch (IOException e) {
			System.err.println("An error occured while trying to write food reports in cache");
			System.err.println(e.getMessage());
		}
	}
}
