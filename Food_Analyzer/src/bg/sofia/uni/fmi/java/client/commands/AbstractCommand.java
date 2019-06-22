package bg.sofia.uni.fmi.java.client.commands;

import java.io.Serializable;
import java.util.Map;

public abstract class AbstractCommand implements Serializable {

	private static final long serialVersionUID = 7228698613930447083L;

	protected Map<String, String> parameters;

	public abstract String getType();

	public abstract boolean isValid();

	public Map<String, String> getParameters() {
		return parameters;
	}
}
