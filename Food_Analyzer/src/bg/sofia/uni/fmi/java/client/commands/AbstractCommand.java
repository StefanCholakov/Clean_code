package bg.sofia.uni.fmi.java.client.commands;

import java.io.Serializable;
import java.util.Map;

public abstract class AbstractCommand implements Serializable {

	private static final long serialVersionUID = 7228698613930447083L;

	protected Map<String, String> parameters;

	/**
	 * @return the type of the given command
	 */
	public abstract String getType();

	/**
	 * Checks whether the given command has a valid syntax or not
	 * 
	 * @return true - if the command has a valid syntax
	 *         <p>
	 *         false - otherwise
	 */
	public abstract boolean isValid();

	public Map<String, String> getParameters() {
		return parameters;
	}
}
