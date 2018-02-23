package ZerothPackage;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class ZerothTest {

	private String dirOfSampleCSV = "C:\\Users\\pheng\\Documents\\cs4222 wireless networks\\CS4222-HW2\\sample-from-pdf.csv";
	private Double expectedVal = 2.06;
	private int expectedAlpha = 5; // alpha is label for the 'vertical' axis of the 2-d array
	private int expectedBeta = 1; // beta, that for the 'horizontal' axis;
	@Test
	void test() {
		CountStep c = null;
		try {
			c = new CountStep(dirOfSampleCSV);
		} catch (Exception e1) {
			e1.printStackTrace();
			fail(e1.getMessage());
		}
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

}
