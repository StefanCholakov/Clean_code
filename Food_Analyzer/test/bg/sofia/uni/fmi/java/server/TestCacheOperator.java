package bg.sofia.uni.fmi.java.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import bg.sofia.uni.fmi.java.dto.Food;
import bg.sofia.uni.fmi.java.dto.Nutrition;
import bg.sofia.uni.fmi.java.exceptions.FoodNotFoundException;

public class TestCacheOperator {

	private static final Type FOOD_MAP_TYPE = new TypeToken<HashMap<String, Food>>() {
	}.getType();;
	private static CacheOperator cache;
	private static Path pathToCacheFile;
	private static Gson gson;

	@BeforeClass
	public static void setUp() {
		pathToCacheFile = Paths.get("resources\\cacheTest.json");
		cache = new CacheOperator(pathToCacheFile);
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	@Test
	public void foodIsAddedInCacheSuccessfully() throws FileNotFoundException {
		try {
			Food food = new Food("RAFFAELLO, UPC: 009800146130", "123456", new ArrayList<Nutrition>(), "raffy");
			cache.addFood(food);
			Map<String, Food> cacheContent = getCacheContent();
			assertNotNull(cacheContent.get("009800146130"));
		} finally {
			deleteFileContent();
		}
	}

	@Test
	public void foodCannotBeAddedTwiceInCache() throws FileNotFoundException {
		try {
			Food firstFood = new Food("RAFFAELLO, UPC: 009800146130", "123456", new ArrayList<Nutrition>(), "raffy");
			Food secondFood = new Food("RAFFAELLO, UPC: 009800146130", "123456", new ArrayList<Nutrition>(), "raffy");
			cache.addFood(firstFood);
			cache.addFood(secondFood);
			Map<String, Food> cacheContent = getCacheContent();
			assertEquals(1, cacheContent.size());
		} finally {
			deleteFileContent();
		}
	}

	@Test(expected = FoodNotFoundException.class)
	public void getFoodByInvalidUpcThrowsException() throws FoodNotFoundException {
		cache.getFoodByUpc("invalid_upc");
	}

	@Test
	public void foodWithExistingUpcIsReturnedSuccessfully() throws FileNotFoundException, FoodNotFoundException {
		try {
			Food food = new Food("RAFFAELLO, UPC: 009800146130", "123456", new ArrayList<Nutrition>(), "raffy");
			cache.addFood(food);
			assertNotNull(cache.getFoodByUpc("009800146130"));
		} finally {
			deleteFileContent();
		}
	}

	private void deleteFileContent() throws FileNotFoundException {
		try (PrintWriter writer = new PrintWriter(pathToCacheFile.toFile())) {
			writer.print("");
		}
	}

	private Map<String, Food> getCacheContent() throws FileNotFoundException {
		JsonReader reader = new JsonReader(new FileReader(pathToCacheFile.toFile()));
		Map<String, Food> cacheContent = gson.fromJson(reader, FOOD_MAP_TYPE);
		if (cacheContent == null) {
			return new HashMap<String, Food>();
		}
		return cacheContent;
	}
}
