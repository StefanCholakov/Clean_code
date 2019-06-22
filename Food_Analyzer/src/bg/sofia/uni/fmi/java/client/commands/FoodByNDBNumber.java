package bg.sofia.uni.fmi.java.client.commands;

import java.util.HashMap;

import bg.sofia.uni.fmi.java.server.Constants;

public class FoodByNDBNumber extends AbstractCommand {

	private static final long serialVersionUID = 3530426780260545508L;

	public FoodByNDBNumber(String command) {
		parameters = new HashMap<>();
		fillParametersMap(command);
	}

	@Override
	public String getType() {
		return Constants.GET_FOOD_REPORT;
	}

	@Override
	public boolean isValid() {
		String ndbNumber = parameters.get(Constants.NDB_PARAMETER);
		return ndbNumber != null && isNumeric(ndbNumber);
	}

	private void fillParametersMap(String command) {
		String[] commandTokens = command.split(Constants.COMMAND_SPLITTER);
		if (commandTokens.length == Constants.GET_FOOD_BY_NDB_PARAMETERS + 1) {
			parameters.put(Constants.NDB_PARAMETER, commandTokens[1]);
		} else {
			parameters.put(Constants.NDB_PARAMETER, null);
		}
	}

	private boolean isNumeric(String num) {
		try {
			Integer.parseInt(num);
			return true;
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}
	}

}
