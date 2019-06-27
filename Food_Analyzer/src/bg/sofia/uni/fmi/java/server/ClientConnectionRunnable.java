package bg.sofia.uni.fmi.java.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import bg.sofia.uni.fmi.java.client.commands.AbstractCommand;

/**
 * Represents a thread for a single client. Every client has his own thread.
 */
public class ClientConnectionRunnable implements Runnable {

	private final static Logger LOGGER = Logger.getLogger(ClientConnectionRunnable.class.getName());

	private FileHandler handler;
	private Socket socket;
	private Server server;

	public ClientConnectionRunnable(Socket socket, Server server) {
		initializeLogger();
		this.socket = socket;
		this.server = server;
	}

	@Override
	public void run() {
		try (ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);) {
			CommandProcessor processor = new CommandProcessor(writer, server);
			AbstractCommand userCommand = null;
			while (true) {
				userCommand = (AbstractCommand) reader.readObject();
				processor.processCommand(userCommand);
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Exception while trying to use client socket", e);
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.WARNING, "Exception while reading command from a user", e);
		}
	}

	private void initializeLogger() {
		try {
			handler = new FileHandler("resources\\logs.log", 0, 1, true);
			Logger rootLog = Logger.getLogger("");
			Handler[] handlers = rootLog.getHandlers();
			for (Handler handler : handlers) {
				rootLog.removeHandler(handler);
			}
			handler.setFormatter(Constants.LOGGER_HANDLER_FORMATTER);
			rootLog.addHandler(handler);
		} catch (IOException e) {
			System.err.println("Could not initialize handler");
			System.err.println(e.getMessage());
		}
	}
}