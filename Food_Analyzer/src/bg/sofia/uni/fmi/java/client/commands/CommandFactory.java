package bg.sofia.uni.fmi.java.client.commands;

import bg.sofia.uni.fmi.java.exceptions.BarcodeException;
import bg.sofia.uni.fmi.java.exceptions.CommandException;
import bg.sofia.uni.fmi.java.server.Constants;

public class CommandFactory {

	public static AbstractCommand getCommand(String command) throws CommandException, BarcodeException {

		String commandType = command.split(Constants.COMMAND_SPLITTER)[0].toLowerCase();
		switch (commandType) {

		case Constants.GET_FOOD_BY_NAME:
			return new FoodByName(command);

		case Constants.GET_FOOD_REPORT:
			return new FoodByNDBNumber(command);

		case Constants.GET_FOOD_BY_BARCODE:
			return new FoodByBarcode(command);

		default:
			throw new CommandException("Unsupported command!");
		}

	}
}
