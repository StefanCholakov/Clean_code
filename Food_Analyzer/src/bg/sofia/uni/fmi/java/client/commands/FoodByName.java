package bg.sofia.uni.fmi.java.client.commands;

import java.util.HashMap;

import bg.sofia.uni.fmi.java.server.Constants;

/*
 * Represents client's command 'get-food <name>'
 */
public class FoodByName extends AbstractCommand {

	private static final long serialVersionUID = 2153171668111571678L;

	public FoodByName(String command) {
		parameters = new HashMap<>();
		fillParametersMap(command);
	}

	@Override
	public String getType() {
		return Constants.GET_FOOD_BY_NAME;
	}

	@Override
	public boolean isValid() {
		return parameters.get(Constants.NAME_PARAMETER) != null;
	}

	private void fillParametersMap(String command) {
		String[] commandTokens = command.split(Constants.COMMAND_SPLITTER, 2);
		if (commandTokens.length == Constants.GET_FOOD_BY_NAME_PARAMETERS + 1) {
			parameters.put(Constants.NAME_PARAMETER, commandTokens[1]);
		} else {
			parameters.put(Constants.NAME_PARAMETER, null);
		}
	}

}
