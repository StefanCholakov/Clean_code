package bg.sofia.uni.fmi.java.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

import bg.sofia.uni.fmi.java.dto.ApiResponse;
import bg.sofia.uni.fmi.java.dto.Food;
import bg.sofia.uni.fmi.java.exceptions.FoodNotFoundException;

public class Server {

	private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
	private static final String API_KEY = "DEMO_KEY";

	private FileHandler handler;
	private ServerSocket serverSocket;
	private HttpClient client;
	private Gson gson;
	private CacheOperator cache;
	private ExecutorService clientsThreadPool;

	public Server() {
		initializeLogger();
		client = HttpClient.newHttpClient();
		gson = new Gson();
		Path serverCacheFile = Paths.get("resources\\ServerCache.json");
		cache = new CacheOperator(serverCacheFile);
		clientsThreadPool = Executors.newFixedThreadPool(Constants.THREAD_POOL_SIZE);
	}

	public static void main(String[] args) {
		new Server().start();
	}

	public void start() {
		try {
			serverSocket = new ServerSocket(Constants.PORT);
			System.out.printf("Server is running on localhost:%d%n", Constants.PORT);
			acceptClients();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "The server was not started properly on port" + Constants.PORT, e);
		} finally {
			shutdownThreadPool();
			closeSocket();
		}
	}

	public List<Food> getFoodByName(String name) throws FoodNotFoundException {
		name = name.replace(" ", Constants.HTTP_SPACE);
		final String requestUri = "https://api.nal.usda.gov/ndb/search/?q=" + name + "&api_key=" + API_KEY;
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(requestUri)).build();
		try {
			HttpResponse<String> httpResponse = client.send(request, BodyHandlers.ofString());
			String jsonResponse = httpResponse.body();
			ApiResponse apiResponse = gson.fromJson(jsonResponse, ApiResponse.class);

			if (apiResponse.containsError()) {
				throw new FoodNotFoundException("Food with name specified does not exist");
			}

			return apiResponse.getSearchResult();
		} catch (IOException | InterruptedException e) {
			LOGGER.log(Level.SEVERE, "An error occured while sending request to the food api", e);
			return new ArrayList<Food>();
		}
	}

	public Food getFoodByNDB(String ndb) throws FoodNotFoundException {
		final String requestUri = "https://api.nal.usda.gov/ndb/reports/?ndbno=" + ndb + "&api_key=" + API_KEY;
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(requestUri)).build();
		try {
			HttpResponse<String> httpResponse = client.send(request, BodyHandlers.ofString());
			String jsonResponse = httpResponse.body();
			ApiResponse apiResponse = gson.fromJson(jsonResponse, ApiResponse.class);

			if (apiResponse.containsError()) {
				throw new FoodNotFoundException("Food with NDB number specified does not exist");
			}

			Food foodReport = apiResponse.getFoodReport();
			cache.addFood(foodReport);
			return foodReport;
		} catch (IOException | InterruptedException e) {
			LOGGER.log(Level.SEVERE, "An error occured while sending request to the food api", e);
			return null;
		}
	}

	public Food getFoodByUpc(String upcCode) throws FoodNotFoundException {
		return cache.getFoodByUpc(upcCode);
	}

	private void acceptClients() {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				ClientConnectionRunnable runnable = new ClientConnectionRunnable(socket, this);
				clientsThreadPool.execute(runnable);
			} catch (IOException e) {
				LOGGER.warning("An error occured while trying to establish connection with client");
			}

		}
	}

	private void shutdownThreadPool() {
		clientsThreadPool.shutdown();
		try {
			while (!clientsThreadPool.awaitTermination(Constants.POOL_WAIT_TIME, TimeUnit.SECONDS)) {
				LOGGER.info("Waiting for completion of threads in thread pool...");
			}
		} catch (InterruptedException e) {
			LOGGER.severe("Interruped while awaiting completion of threads");
			clientsThreadPool.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}

	private void closeSocket() {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "An error occured while trying to stop the server", e);
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
			System.err.println("Could not initialize handler");
			System.err.println(e.getMessage());
		}
	}
}