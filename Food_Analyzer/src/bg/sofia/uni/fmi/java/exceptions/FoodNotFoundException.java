package bg.sofia.uni.fmi.java.exceptions;

public class FoodNotFoundException extends Exception {

	private static final long serialVersionUID = 2879674184923122909L;

	public FoodNotFoundException(String message) {
		super(message);
	}
}
