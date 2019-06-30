package bg.sofia.uni.fmi.java.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import bg.sofia.uni.fmi.java.client.commands.AbstractCommand;
import bg.sofia.uni.fmi.java.client.commands.CommandFactory;
import bg.sofia.uni.fmi.java.exceptions.BarcodeException;
import bg.sofia.uni.fmi.java.exceptions.CommandException;
import bg.sofia.uni.fmi.java.server.Constants;

/**
 * Represents a single client. Provides methods for connecting to the server and
 * processing user input.
 */
public class Client {

	private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

	private static FileHandler handler;
	private Socket socket;

	public static void main(String[] args) throws IOException {
		try {
			new Client().run();
		} catch (UnknownHostException e) {
			LOGGER.log(Level.SEVERE, "Invalid server host name!", e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "An error occurred while trying to connect to the server", e);
		}
	}

	public Client() {
		initializeLogger();
	}

	public void run() throws UnknownHostException, IOException {
		connectToServer();
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "An error occurred while closing the connection", e);
		}
	}

	private void connectToServer() throws UnknownHostException, IOException {
		socket = new Socket("localhost", Constants.PORT);
		try (ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream())) {
			ServerResponse clientRunnable = new ServerResponse(socket);
			new Thread(clientRunnable).start();
			processInput(writer);
		}
	}

	private void processInput(ObjectOutputStream writer) throws IOException {
		try (Scanner inputReader = new Scanner(System.in)) {
			String input;
			while ((input = inputReader.nextLine()) != null) {
				try {
					AbstractCommand command = CommandFactory.getCommand(input);
					if (command.isValid()) {
						writer.writeObject(command);
					} else {
						System.out.println("Invalid command!");
					}
				} catch (CommandException e) {
					System.out.println(e.getMessage());
				} catch (BarcodeException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	private void initializeLogger() {
		try {
			handler = new FileHandler("resources\\logs.log", 0, 1, true);
			Logger rootLog = Logger.getLogger("");
			Handler[] handlers = rootLog.getHandlers();
			for (Handler handler : handlers) {
				handler.close();
				rootLog.removeHandler(handler);
			}
			handler.setFormatter(Constants.LOGGER_HANDLER_FORMATTER);
			rootLog.addHandler(handler);
		} catch (IOException e) {
			System.err.println("The log file handler was not initialized!");
		}
	}
}
