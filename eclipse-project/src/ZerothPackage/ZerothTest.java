package ZerothPackage;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;


class ZerothTest {

	private String dirOfSampleCSV = "C:\\Users\\pheng\\Documents\\cs4222 wireless networks\\CS4222-HW2\\sample-from-pdf.csv";
	private Double expectedVal = 2.06;
	private int expectedAlpha = 5; // alpha is label for the 'vertical' axis of the 2-d array
	private int expectedBeta = 1; // beta, that for the 'horizontal' axis;
	private CountStep c;
	
	@Before
	void setUp() {
		try {
			c = new CountStep(dirOfSampleCSV);
		} catch (Exception e1) {
			e1.printStackTrace();
			fail(e1.getMessage());
		}
	}
	
	@Test
	void testReadOfCSV() {
		
		
		ArrayList<ArrayList<Double>> g;
		try {
			g = c.getGroundTruth();
			String expected = Double.toString(expectedVal);
			String actual = Double.toString(g.get(expectedAlpha).get(expectedBeta)); 
			assertEquals(expected, actual);
		} catch (Exception e) {
			fail(e.getMessage());
			
		}
	
		
	}
	
	@Test
	void testMagnitudeArithmetic() {
		Double sampleX = 1.0;
		Double sampleY = 2.0;
		Double sampleZ = 3.0;
		Double actualVariance = c.calculateMagnitude(sampleX, sampleY, sampleZ);
		Double expectedVariance = 3.741657386773941;
		assertEquals(expectedVariance, actualVariance, 0.01);
	}

}
