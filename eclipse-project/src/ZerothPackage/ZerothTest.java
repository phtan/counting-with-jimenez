package ZerothPackage;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ZerothTest {

	private String dirOfSampleCSV = "C:\\Users\\pheng\\Documents\\cs4222 wireless networks\\CS4222-HW2\\sample-from-pdf.csv";
	private Double expectedVal = 2.06;
	private int expectedAlpha = 5; // alpha is label for the 'vertical' axis of the 2-d array
	private int expectedBeta = 1; // beta, that for the 'horizontal' axis;
	private CountStep c;
	
	@BeforeEach
	public void setUp() {
		try {
			this.c = new CountStep(dirOfSampleCSV);
	
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
		c.setWindowSize(2);
		int givenLowerThreshold = 1;
		c.applyLowerThreshold(givenLowerThreshold);
		int expectedSizeOfList = 6;
		assertEquals(c.getLowerThresholds().length(), expectedSizeOfList);
		ArrayList<Integer> expectedIndicesOfLowerThresholds = new ArrayList<>();
		//TODO compute;
		Iterator<Integer> iter = expectedIndicesOfLowerThresholds.iterator();
		while (iter.hasNext()) {
			int index = iter.next();
			assertEquals(givenLowerThreshold, c.getLowerThresholds.get(index));
		}
	}
}
