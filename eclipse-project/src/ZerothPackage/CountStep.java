package ZerothPackage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class CountStep {
	
	private ArrayList<ArrayList<Double>> groundTruth;
	private int windowSize;
	
	private final int DEFAULT_WINDOW_SIZE = 15;
	

	public CountStep(String dir) throws Exception {
		
		windowSize = DEFAULT_WINDOW_SIZE;
		
		groundTruth = new ArrayList<ArrayList<Double>>();
		BufferedReader br = null;
		String line = "";
		String separator = ",";
		
		try {
			br = new BufferedReader(new FileReader(dir));
			while ((line = br.readLine()) != null) {
				String[] single = line.split(separator);
				ArrayList<Double> listOfSingle = new ArrayList<>();
				for (int j=0; j<single.length; j++) {
					listOfSingle.add(Double.parseDouble(single[j]));
					
				}
				groundTruth.add(listOfSingle);
				
			}
		} catch (FileNotFoundException e) {
			throw e;
			
		}
		br.close();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public ArrayList<ArrayList<Double>> getGroundTruth() throws Exception {
		if (groundTruth != null) {
			return groundTruth;
		} else {
			throw new Exception("groundTruth should not be null but it is");
		}
	}
	
	public Double calculateMagnitude(Double x, Double y, Double z) {
		Double result = 0.0;
		Double xSquared = x * x;
		Double ySquared = y * y;
		Double zSquared = z * z;
		result = Math.sqrt(xSquared + ySquared + zSquared);
		return result;
	}

	

	public void setWindowSize(int i) {
		windowSize = i;
		
	}

	public Double calculateLocalMean(int centre) {

		return null;
	}

}
