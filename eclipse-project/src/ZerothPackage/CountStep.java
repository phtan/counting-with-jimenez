package ZerothPackage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

public class CountStep {
	
	private ArrayList<ArrayList<Double>> groundTruth;
	private ArrayList<Double> withLowerThreshold;
	private int windowSize;
	
	private final int DEFAULT_WINDOW_SIZE = 15;
	

	public CountStep(String dir) throws Exception {
		
		windowSize = DEFAULT_WINDOW_SIZE;
		
		groundTruth = new ArrayList<ArrayList<Double>>();
		withLowerThreshold = new ArrayList<Double>();
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

		/*
		 * sum = 0;
		 * divisors = 2w - 1; // w is size of the averaging window
		 * looping from (centre - window) to (centre + window)
		 *     if (centre - window) < 0 // reached the first entry in list
		 *          divisors--;
		 *          continue to next iteration of loop;
		 *     else
		 *         v = list[centre-window]
		 *         sum = addVToTheSum(v, sum);
		 */
		int divisorMinusOne = 2 * this.windowSize;
		int divisor = divisorMinusOne + 1;
		double sum = 0.0;
		for (int k=centre-this.windowSize; k<=centre+this.windowSize; k++) {
			int maxIndex = this.groundTruth.size()-1;
			if (k<0) {
				divisor--;
				continue;
			} else if (k>maxIndex) {
				divisor--;
				continue;
			}
			else {
				ArrayList<Double> listForSampleK = this.groundTruth.get(k);
				int xIndex = 0;
				int yIndex = 1;
				int zIndex = 2;
				Double magnitude = this.calculateMagnitude(listForSampleK.get(xIndex),
						listForSampleK.get(yIndex),
						listForSampleK.get(zIndex));
				sum += magnitude;
			}
		}
		Double result;
		result = sum / divisor;
		return result;
	}

	public Double calculateLocalVariance(int indexOfCentreOfAveragingWindow) {
		
		int centre = indexOfCentreOfAveragingWindow;	
		Double localMean = this.calculateLocalMean(centre);

		
		int divisorMinusOne = 2 * this.windowSize;
		int divisor = divisorMinusOne + 1;
		double sum = 0.0;
		
		for (int k=centre-this.windowSize; k<=centre+this.windowSize; k++) {
			int maxIndex = this.groundTruth.size()-1;
			if (k<0) {
				divisor--;
				continue;
			} else if(k>maxIndex) {
				divisor--;
				continue;
			} else {
				ArrayList<Double> listForSampleK = this.groundTruth.get(k);
				int xIndex = 0;
				int yIndex = 1;
				int zIndex = 2;
				Double magnitude = this.calculateMagnitude(listForSampleK.get(xIndex),
						listForSampleK.get(yIndex),
						listForSampleK.get(zIndex));
				
				double squareRootOfAddend = magnitude - localMean;
				Double addend = squareRootOfAddend * squareRootOfAddend;
				sum += addend;
			}
		}
		Double result;
		result = sum / divisor;
		return result;
	}

	public void applyLowerThreshold(Double givenLowerThreshold) {
		Double variance;
		Double squareRootOfVariance;
		for (int k=0; k<this.groundTruth.size(); k++) {
			variance = this.calculateLocalVariance(k);
			squareRootOfVariance = Math.sqrt(variance);
			if (squareRootOfVariance<givenLowerThreshold) {
				this.withLowerThreshold.add(givenLowerThreshold);
			} else {
				this.withLowerThreshold.add(squareRootOfVariance);
			}
		}
		
	}

	public ArrayList<Double> getLowerThresholds() {
	
		return withLowerThreshold;
	}

}
