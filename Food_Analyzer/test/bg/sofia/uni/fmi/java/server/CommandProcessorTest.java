package bg.sofia.uni.fmi.java.server;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import bg.sofia.uni.fmi.java.client.commands.AbstractCommand;
import bg.sofia.uni.fmi.java.client.commands.CommandFactory;
import bg.sofia.uni.fmi.java.dto.Food;
import bg.sofia.uni.fmi.java.dto.Nutrition;
import bg.sofia.uni.fmi.java.exceptions.BarcodeException;
import bg.sofia.uni.fmi.java.exceptions.CommandException;
import bg.sofia.uni.fmi.java.exceptions.FoodNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class CommandProcessorTest {

	@Mock
	private PrintWriter writerMock;

	@Mock
	private Server serverMock;

	private CommandProcessor commandProcessor;

	@Before
	public void setUp() {
		commandProcessor = new CommandProcessor(writerMock, serverMock);
	}

	@Test
	public void cannotDisplayFoodWithNotExistingName()
			throws FoodNotFoundException, CommandException, BarcodeException, InterruptedException {

		final String exceptionMessage = "Food with name specified does not exist";
		doThrow(new FoodNotFoundException(exceptionMessage)).when(serverMock).getFoodByName(anyString());
		AbstractCommand userCommand = CommandFactory.getCommand("get-food invalid_name");
		commandProcessor.processCommand(userCommand);
		verify(writerMock).println(exceptionMessage);
	}

	@Test
	public void foodWithValidNameIsDisplayedSuccessfully()
			throws CommandException, BarcodeException, InterruptedException, FoodNotFoundException {

		Food food = new Food("chocolate", "123456", null, "9848918998");
		List<Food> foodsByGivenName = new ArrayList<>();
		foodsByGivenName.add(food);

		when(serverMock.getFoodByName(anyString())).thenReturn(foodsByGivenName);
		AbstractCommand userCommand = CommandFactory.getCommand("get-food invalid_name");
		commandProcessor.processCommand(userCommand);
		verify(writerMock).println(food.getSearchResult());
	}

	@Test
	public void cannotGetFoodByInvalidNdbNumber()
			throws CommandException, BarcodeException, InterruptedException, FoodNotFoundException {

		final String exceptionMessage = "Food with NDB number specified does not exist";
		doThrow(new FoodNotFoundException(exceptionMessage)).when(serverMock).getFoodByNDB(anyString());
		AbstractCommand userCommand = CommandFactory.getCommand("get-food-report invalid_ndb");
		commandProcessor.processCommand(userCommand);
		verify(writerMock).println(exceptionMessage);
	}

	@Test
	public void foodReportIsDisplayedForValiNdb()
			throws CommandException, BarcodeException, InterruptedException, FoodNotFoundException {

		List<Nutrition> nutritions = new ArrayList<>();
		nutritions.add(new Nutrition("Energy", "50", "kcal"));
		nutritions.add(new Nutrition("Protein", "0.71", "g"));
		Food food = new Food("orange", "123456", nutritions, "1884498884");

		when(serverMock.getFoodByNDB(anyString())).thenReturn(food);
		AbstractCommand userCommand = CommandFactory.getCommand("get-food-report 123456");
		commandProcessor.processCommand(userCommand);
		verify(writerMock).println(food.getReportResult());
	}

	@Test
	public void cannotGetFoodreportForInvalidUpc()
			throws CommandException, BarcodeException, InterruptedException, FoodNotFoundException {

		final String exceptionMessage = "Food with given barcode does not exist";
		doThrow(new FoodNotFoundException(exceptionMessage)).when(serverMock).getFoodByUpc(anyString());
		AbstractCommand userCommand = CommandFactory.getCommand("get-food-by-barcode  --upc invalid_upc");
		commandProcessor.processCommand(userCommand);
		verify(writerMock).println(exceptionMessage);
	}

	@Test
	public void foodReportIsDisplayedForValidUpc()
			throws CommandException, BarcodeException, InterruptedException, FoodNotFoundException {

		List<Nutrition> nutritions = new ArrayList<>();
		nutritions.add(new Nutrition("Energy", "50", "kcal"));
		nutritions.add(new Nutrition("Protein", "0.71", "g"));
		Food food = new Food("orange", "123456", nutritions, "1884498884");

		when(serverMock.getFoodByUpc(anyString())).thenReturn(food);
		AbstractCommand userCommand = CommandFactory.getCommand("get-food-by-barcode --upc 1884498884");
		commandProcessor.processCommand(userCommand);
		verify(writerMock).println(food.getReportResult());
	}
}
