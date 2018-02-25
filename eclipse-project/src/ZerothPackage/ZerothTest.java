package ZerothPackage;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ZerothTest {

	private String dirOfSampleCSV = "C:\\Users\\pheng\\Documents\\cs4222 wireless networks\\CS4222-HW2\\sample-from-pdf.csv";
	private String dir2 = "C:\\Users\\pheng\\Documents\\cs4222 wireless networks\\CS4222-HW2\\sample2.csv";
	private Double expectedVal = 2.06;
	private int expectedAlpha = 5; // alpha is label for the 'vertical' axis of the 2-d array
	private int expectedBeta = 1; // beta, that for the 'horizontal' axis;
	private CountStep c;
	private CountStep c2;
	
	@BeforeEach
	public void setUp() {
		try {
			this.c = new CountStep(dirOfSampleCSV);
			this.c2 = new CountStep(dir2);
	
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
	public void testLowerThreshold() {
		c2.setWindowSize(1);
		Double givenLowerThreshold = 1.0;
		c2.applyLowerThreshold(givenLowerThreshold);
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
	public void testUpperThreshold() {
		fail("Not implemented yet.");
	}
}
