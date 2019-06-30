package bg.sofia.uni.fmi.java.client.commands;

import java.io.File;
import java.util.HashMap;

import bg.sofia.uni.fmi.java.client.BarcodeDecoder;
import bg.sofia.uni.fmi.java.exceptions.BarcodeException;
import bg.sofia.uni.fmi.java.server.Constants;

/*
 * Represents client's command 'get-food-by-barcode'
 */
public class FoodByBarcode extends AbstractCommand {

	private static final long serialVersionUID = -6111066208056732403L;

	public FoodByBarcode(String command) throws BarcodeException {
		parameters = new HashMap<>();
		fillParametersMap(command);
	}

	@Override
	public String getType() {
		return Constants.GET_FOOD_BY_BARCODE;
	}

	@Override
	public boolean isValid() {

		if (parameters.size() < Constants.GET_FOOD_BY_BARCODE_MIN_PARAMETERS
				|| parameters.size() > Constants.GET_FOOD_BY_BARCODE_MAX_PARAMETERS) {
			return false;
		} else if (!parameters.containsKey(Constants.UPC_PARAMETER)
				&& !parameters.containsKey(Constants.IMAGE_PARAMETER)) {
			return false;
		}

		return true;
	}

	private void fillParametersMap(String command) throws BarcodeException {
		String[] commandTokens = command.split(Constants.COMMAND_SPLITTER);
		if (hasOddParametersNumber(commandTokens)) {
			return;
		}
		for (int i = 1; i < commandTokens.length; i += 2) {
			parameters.put(commandTokens[i], commandTokens[i + 1]);
		}
		if (parameters.containsKey(Constants.IMAGE_PARAMETER) && !parameters.containsKey(Constants.UPC_PARAMETER)) {
			File image = new File(parameters.get(Constants.IMAGE_PARAMETER));
			String upcCode = BarcodeDecoder.decodeQRCode(image);
			parameters.put(Constants.UPC_PARAMETER, upcCode);
		}
	}

	private boolean hasOddParametersNumber(String[] commandTokens) {
		return (commandTokens.length - 1) % 2 == 1;
	}

}
