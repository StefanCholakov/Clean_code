package bg.sofia.uni.fmi.java.server;

import java.time.format.DateTimeFormatter;
import java.util.logging.SimpleFormatter;

public class Constants {

	public static final int PORT = 4444;

	public static final String COMMAND_SPLITTER = "\\s+-*";

	public static final String GET_FOOD_BY_NAME = "get-food";
	public static final String GET_FOOD_REPORT = "get-food-report";
	public static final String GET_FOOD_BY_BARCODE = "get-food-by-barcode";

	public static final String NAME_PARAMETER = "name";
	public static final String NDB_PARAMETER = "ndb";
	public static final String UPC_PARAMETER = "upc";
	public static final String IMAGE_PARAMETER = "img";

	public static final int REGISTER_PARAMETERS_NUMBER = 2;
	public static final int LOGIN_PARAMETERS_NUMBER = 2;
	public static final int LOGOUT_PARAMETERS_NUMBER = 1;
	public static final int GET_FOOD_BY_NAME_PARAMETERS = 1;
	public static final int GET_FOOD_BY_NDB_PARAMETERS = 1;
	public static final int GET_FOOD_BY_BARCODE_MAX_PARAMETERS = 2;
	public static final int GET_FOOD_BY_BARCODE_MIN_PARAMETERS = 1;

	public static final String HTTP_SPACE = "%20";

	public static final int THREAD_POOL_SIZE = 8;
	public static final int POOL_WAIT_TIME = 10;
	public static final DateTimeFormatter SESSION_EXPIRATION_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
	public static final SimpleFormatter LOGGER_HANDLER_FORMATTER = new SimpleFormatter();

	private Constants() {

	}
}