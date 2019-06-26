package bg.sofia.uni.fmi.java.suite;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import bg.sofia.uni.fmi.java.suite.TestSuite.JunitTestSuite;

public class TestRunner {

	public static void main(String[] args) {

		Result result = JUnitCore.runClasses(JunitTestSuite.class);

		System.out.printf("Total tests run: %d\n", result.getRunCount());
		System.out.printf("Tests failed: %d\n", result.getFailureCount());
	}
}