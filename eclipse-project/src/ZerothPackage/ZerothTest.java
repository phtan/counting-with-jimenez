package ZerothPackage;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ZerothTest {

	private String dirOfSampleCSV = "C:\\Users\\pheng\\Documents\\cs4222 wireless networks\\CS4222-HW2\\sample-from-pdf.csv";
	private String dir2 = "C:\\Users\\pheng\\Documents\\cs4222 wireless networks\\CS4222-HW2\\sample2.csv";
	private String dir3 = "C:\\Users\\pheng\\git\\counting-gitlab\\data\\22_accel_data_m.csv";
	// private String dir3 = "C:\\Users\\pheng\\git\\counting-gitlab\\data\\22_accel_data_fixed.csv";
	private Double expectedVal = 2.06;
	private int expectedAlpha = 5; // alpha is label for the 'vertical' axis of the 2-d array
	private int expectedBeta = 1; // beta, that for the 'horizontal' axis;
	private CountStep c;
	private CountStep c2;
	private CountStep c3;
	
	@BeforeEach
	public void setUp() {
		try {
			this.c = new CountStep(dirOfSampleCSV);
			this.c2 = new CountStep(dir2);
			this.c3 = new CountStep(dir3);
		} catch (Exception e1) {
			e1.printStackTrace();
			fail(e1.getMessage());
		}
	}
	
	@Test
	public void testReadOfCSV() {
		
		
		ArrayList<ArrayList<Double>> g;
		try {
			g = c.getGroundTruth();
			String expected = Double.toString(expectedVal);
			String actual = Double.toString(g.get(expectedAlpha).get(expectedBeta)); 
			assertEquals(expected, actual);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			fail(e.getMessage());
			
		}
	
		
	}
	
	@Test
	public void testMagnitudeArithmetic() {
		Double sampleX = 1.0;
		Double sampleY = 2.0;
		Double sampleZ = 3.0;
		Double actualMagnitude = c.calculateMagnitude(sampleX, sampleY, sampleZ);
	
		Double expectedMagnitude = 3.741657386773941;
		assertEquals(expectedMagnitude, actualMagnitude, 0.01);
	}
	
	@Test
	public void testLocalMeanArithmetic() {
		c.setWindowSize(2);
		Double actualMean = c.calculateLocalMean(0);
		Double expectedMean = 9.759154917263177;
		assertEquals(expectedMean, actualMean, 0.01);
	}
	
	@Test
	public void testLocalVarianceArithmetic() {
		c.setWindowSize(2);
		int indexOfCentreOfAveragingWindow = 0;
		Double actualVariance = c.calculateLocalVariance(indexOfCentreOfAveragingWindow);
		Double expectedVariance = 0.049429516536621;
		assertEquals(expectedVariance, actualVariance, 0.01);
	}
	
	@Test
	public void itShallNotProceedIfVarianceIsNotComputed() {
		Double arbitraryThreshold = 1.0;
		String expectedErrorMessage = c.ERROR_VARIANCE_NOT_COMPUTED;
		try {
			c.applyLowerThreshold(arbitraryThreshold);
		} catch (Exception e) {
			assertEquals(expectedErrorMessage, e.getMessage());
		}
	}
	
	@Test
	public void varianceShallNotBeComputedMoreThanOnce() {
		boolean hasTried = false;
		String expectedErrorMessage = c.ERROR_OVERCOMPUTING_VARIANCE;
		try {
			hasTried = true;
			c.calculateSquareRootOfVariance();
			c.calculateSquareRootOfVariance();
		} catch (Exception e) {
			assertEquals(expectedErrorMessage, e.getMessage());
		}
		if(!hasTried) {
			fail("Expected execution to enter try-block but it did not.");
		}
		
	}
	@Test
	public void lowerThresholdIsSet() {
		Double arbitrary = 99.99;
		try {
			c2.calculateSquareRootOfVariance();
			c2.applyLowerThreshold(arbitrary);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		String expected = Double.toString(arbitrary);
		String actual = Double.toString(c2.lowerThreshold);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLowerThreshold() {
		c2.setWindowSize(1);
		Double givenLowerThreshold = 1.0;
		
		try {
			c2.calculateSquareRootOfVariance();
			c2.applyLowerThreshold(givenLowerThreshold);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		int expectedSizeOfList = 2;
		assertEquals(expectedSizeOfList, c2.getLowerThresholds().size());
		ArrayList<Integer> expectedIndicesOfLowerThresholds = new ArrayList<>();
		int expectedIndexNumberOne = 0;
		expectedIndicesOfLowerThresholds.add(expectedIndexNumberOne);
		int expectedIndexNumberTwo = 1;
		expectedIndicesOfLowerThresholds.add(expectedIndexNumberTwo);
		Iterator<Integer> iter = expectedIndicesOfLowerThresholds.iterator();
		while (iter.hasNext()) {
			int index = iter.next();
			// convert to string, since comparison between two Doubles that
			// have the same value will throw a strange error:
			// "AssertionFailedError: positive delta expected but was: <0.0>"
			String expected = Double.toString(givenLowerThreshold);
			String actual = Double.toString(c2.getLowerThresholds().get(index));
			assertEquals(expected, actual);
		}
	}
	
	
	@Test
	public void testAccuracy() {
		Double givenLowerThreshold = 1.0;
		Double givenUpperThreshold = 2.0;
		int permissibleError = 7; // how much the computed number of steps can differ from the ground truth
		int actualNumberOfSteps = 110;
		try {
			c3.calculateSquareRootOfVariance();
			c3.applyLowerThreshold(givenLowerThreshold);
			c3.applyUpperThreshold(givenUpperThreshold);
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
		int result = c3.countSteps();
		
		
		int error = actualNumberOfSteps - result;
		int errorSquared = error * error; //  remove negative signs if any
		int permissibleErrorSquared = permissibleError * permissibleError;
		
				
		if (errorSquared > permissibleErrorSquared) {

			String failMessage = "The computed number of steps "
			    + Integer.toString(result)
			    + " differed too much from the actual number of steps "
			    + Integer.toString(actualNumberOfSteps)
			    + "; expected error of "
			    + Integer.toString(permissibleError)
			    + " but actual error was "
			    + Integer.toString(error);
			fail(failMessage);
		}
		
		String successMessage = "I computed the number of steps to be "
			    + Integer.toString(result);
		
		System.out.println(successMessage);
	}

}
