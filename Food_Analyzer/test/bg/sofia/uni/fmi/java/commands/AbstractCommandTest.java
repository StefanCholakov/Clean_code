package bg.sofia.uni.fmi.java.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import bg.sofia.uni.fmi.java.client.commands.AbstractCommand;
import bg.sofia.uni.fmi.java.client.commands.CommandFactory;
import bg.sofia.uni.fmi.java.exceptions.BarcodeException;
import bg.sofia.uni.fmi.java.exceptions.CommandException;
import bg.sofia.uni.fmi.java.server.Constants;

public class AbstractCommandTest {

	@Test(expected = CommandException.class)
	public void invalidCommandThrowsException() throws CommandException, BarcodeException {
		CommandFactory.getCommand("invalid-command");
	}

	@Test
	public void getFoodByNameCommandReturnsCorrectType() throws CommandException, BarcodeException {
		AbstractCommand command = CommandFactory.getCommand("get-food raffaello");
		assertEquals(Constants.GET_FOOD_BY_NAME, command.getType());
	}

	@Test
	public void getFoodByNameCommandIsNotValid() throws CommandException, BarcodeException {
		AbstractCommand command = CommandFactory.getCommand("get-food");
		assertFalse(command.isValid());
	}

	@Test
	public void getFoodByNameCommandIsValid() throws CommandException, BarcodeException {
		AbstractCommand command = CommandFactory.getCommand("get-food fruit juice");
		assertTrue(command.isValid());
	}

	@Test
	public void getFoodByNameCommandReturnsCorrectParameters() throws CommandException, BarcodeException {
		AbstractCommand command = CommandFactory.getCommand("get-food vanilia ice cream");
		Map<String, String> parameters = command.getParameters();
		assertTrue(parameters.containsKey(Constants.NAME_PARAMETER));
		assertEquals("vanilia ice cream", parameters.get(Constants.NAME_PARAMETER));
	}

	@Test
	public void getFoodByNDBCommandReturnsCorrectType() throws CommandException, BarcodeException {
		AbstractCommand command = CommandFactory.getCommand("get-food-report 45142036");
		assertEquals(Constants.GET_FOOD_REPORT, command.getType());
	}

	@Test
	public void ndbNumberCannotBeString() throws CommandException, BarcodeException {
		AbstractCommand command = CommandFactory.getCommand("get-food-report ndbNum");
		assertFalse(command.isValid());
	}

	@Test
	public void getFoodReportWithoutParametersIsInvalid() throws CommandException, BarcodeException {
		AbstractCommand command = CommandFactory.getCommand("get-food-report");
		assertFalse(command.isValid());
	}

	@Test
	public void getFoodReportCommandIsValid() throws CommandException, BarcodeException {
		AbstractCommand command = CommandFactory.getCommand("get-food-report 45142036");
		assertTrue(command.isValid());
	}

	@Test
	public void getFoodReportCommandReturnsCorrectParameters() throws CommandException, BarcodeException {
		AbstractCommand command = CommandFactory.getCommand("get-food-report 45142036");
		Map<String, String> parameters = command.getParameters();
		assertTrue(parameters.containsKey(Constants.NDB_PARAMETER));
		assertEquals("45142036", parameters.get(Constants.NDB_PARAMETER));
	}

	@Test
	public void getFoodByBarcodeCommandReturnsCorrectType() throws CommandException, BarcodeException {
		AbstractCommand command = CommandFactory.getCommand("get-food-by-barcode --upc 009800146130");
		assertEquals(Constants.GET_FOOD_BY_BARCODE, command.getType());
	}

	@Test
	public void getFoodByBarcodeCommandIsInvalidWithoutArguments() throws CommandException, BarcodeException {
		AbstractCommand command = CommandFactory.getCommand("get-food-by-barcode");
		assertFalse(command.isValid());
	}

	@Test
	public void getFoodByBarcodeCommandWithUpcSpecifiedIsValid() throws CommandException, BarcodeException {
		AbstractCommand command = CommandFactory.getCommand("get-food-by-barcode --upc 009800146130");
		assertTrue(command.isValid());
	}

	@Test
	public void getFoodByBarcodeCommandWithImageSpecifiedIsValid() throws CommandException, BarcodeException {
		AbstractCommand command = CommandFactory.getCommand("get-food-by-barcode --img resources\\raffaello.jpg");
		assertTrue(command.isValid());
	}
}
