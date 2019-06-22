package bg.sofia.uni.fmi.java.exceptions;

public class BarcodeException extends Exception {

	private static final long serialVersionUID = -7048024674952351551L;

	public BarcodeException(String message, Throwable exception) {
		super(message, exception);
	}

}
