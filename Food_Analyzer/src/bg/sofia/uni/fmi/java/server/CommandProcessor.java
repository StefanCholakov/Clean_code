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

	public void processCommand(AbstractCommand command) {
		String commandType = command.getType();
		Map<String, String> parameters = command.getParameters();

		switch (commandType) {

		case Constants.GET_FOOD_BY_NAME:
			getFoodByName(parameters);
			break;

		case Constants.GET_FOOD_REPORT:
			getFoodReportByNdb(parameters);
			break;

		case Constants.GET_FOOD_BY_BARCODE:
			getFoodByBarcode(parameters);
			break;

		default:
			printErrorMessage();
			break;
		}
	}

	private void getFoodByName(Map<String, String> parameters) {
		try {
			String foodName = parameters.get(Constants.NAME_PARAMETER);
			List<Food> foodList = server.getFoodByName(foodName);
			foodList.stream().forEach(food -> writer.println(food.getSearchResult()));
		} catch (FoodNotFoundException e) {
			writer.println(e.getMessage());
		}
	}

	private void getFoodReportByNdb(Map<String, String> parameters) {
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

	private void getFoodByBarcode(Map<String, String> parameters) {
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
