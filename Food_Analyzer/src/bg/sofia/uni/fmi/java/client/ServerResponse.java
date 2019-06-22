package bg.sofia.uni.fmi.java.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A thread for every client. Represents the server's responses, which are
 * broadcast to the client's standard output.
 */
public class ServerResponse implements Runnable {

	private final static Logger LOGGER = Logger.getLogger(ServerResponse.class.getName());
	private Socket socket;

	public ServerResponse(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "An error occurred while receiving data from the server", e);
		}
	}
}