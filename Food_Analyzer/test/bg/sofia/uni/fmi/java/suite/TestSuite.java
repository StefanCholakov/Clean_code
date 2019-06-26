package bg.sofia.uni.fmi.java.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import bg.sofia.uni.fmi.java.client.BarcodeDecoderTest;
import bg.sofia.uni.fmi.java.commands.AbstractCommandTest;
import bg.sofia.uni.fmi.java.server.CommandProcessorTest;
import bg.sofia.uni.fmi.java.server.TestCacheOperator;

public class TestSuite {

	@RunWith(Suite.class)

	@Suite.SuiteClasses({ BarcodeDecoderTest.class, AbstractCommandTest.class, TestCacheOperator.class,
			CommandProcessorTest.class })

	public class JunitTestSuite {

	}
}