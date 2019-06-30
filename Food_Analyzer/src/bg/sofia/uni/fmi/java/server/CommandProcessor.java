package bg.sofia.uni.fmi.java.server;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import bg.sofia.uni.fmi.java.client.commands.AbstractCommand;
import bg.sofia.uni.fmi.java.dto.Food;
import bg.sofia.uni.fmi.java.exceptions.FoodNotFoundException;

public class CommandProcessor {

	private PrintWriter writer;
	private Server server;

	public CommandProcessor(PrintWriter writer, Server server) {
		this.writer = writer;
		this.server = server;
	}

	/**
	 * Processes the user's command after it was read depending on its type.
	 * 
	 * @param command - the user's command
	 */
	public void processCommand(AbstractCommand command) {
		String commandType = command.getType();
		Map<String, String> parameters = command.getParameters();

		switch (commandType) {

		case Constants.GET_FOOD_BY_NAME:
			listFoodsByName(parameters);
			break;

		case Constants.GET_FOOD_REPORT:
			printFoodReportByNDB(parameters);
			break;

		case Constants.GET_FOOD_BY_BARCODE:
			printFoodReportByBarcode(parameters);
			break;

		default:
			printErrorMessage();
			break;
		}
	}

	/**
	 * Shows basic information about food by it's name
	 * 
	 * @param parameters - map containing entry 'name - food_name' where food_name
	 *                   is the name of the food to be searched.
	 */
	private void listFoodsByName(Map<String, String> parameters) {
		try {
			String foodName = parameters.get(Constants.NAME_PARAMETER);
			List<Food> foodList = server.getFoodsByName(foodName);
			foodList.stream().forEach(food -> writer.println(food.getSearchResult()));
		} catch (FoodNotFoundException e) {
			writer.println(e.getMessage());
		}
	}

	/**
	 * Prints report for a given food by it's NDB number
	 * 
	 * @param parameters - map containing entry 'ndb - ndb_number' where ndb_number
	 *                   is the NDB of the food to be searched.
	 */
	private void printFoodReportByNDB(Map<String, String> parameters) {
		try {
			String ndbNumber = parameters.get(Constants.NDB_PARAMETER);
			Food food = server.getFoodByNDB(ndbNumber);
			if (food != null) {
				writer.println(food.getReportResult());
			}
		} catch (FoodNotFoundException e) {
			writer.println(e.getMessage());
		}
	}

	/**
	 * Prints report for a given food by it's barcode
	 * 
	 * @param parameters - map containing at entry 'upc - upc_code' where upc_code
	 *                   is the UPC of the food to be searched.
	 */
	private void printFoodReportByBarcode(Map<String, String> parameters) {
		try {
			String upcCode = parameters.get(Constants.UPC_PARAMETER);
			Food food = server.getFoodByUpc(upcCode);
			writer.println(food.getReportResult());
		} catch (FoodNotFoundException e) {
			writer.println(e.getMessage());
		}
	}

	private void printErrorMessage() {
		writer.println("Command not found");
	}

}
