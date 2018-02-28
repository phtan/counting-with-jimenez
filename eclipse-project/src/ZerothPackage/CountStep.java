package ZerothPackage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

public class CountStep {
	
	private ArrayList<ArrayList<Double>> groundTruth;
	private ArrayList<Double> squareRootsOfVariance;
	private ArrayList<Double> withLowerThreshold;
	private ArrayList<Double> withUpperThreshold;
	private int windowSize;
	
	private boolean haveCalculatedSquareRootOfVariance;
	
	private final int DEFAULT_WINDOW_SIZE = 15;
	private final Double NOT_A_VALUE = Double.NaN;
	
	public final String ERROR_VARIANCE_NOT_COMPUTED = "sigma has not been computed, vis-a-vis Jimenez-algorithm.";
	public final String ERROR_OVERCOMPUTING_VARIANCE = "Why are you calculating square-root-of-variance more than one time?";
	public Double lowerThreshold;
	
	public CountStep(String dir) throws Exception {
		
		windowSize = DEFAULT_WINDOW_SIZE;
		haveCalculatedSquareRootOfVariance = false;
		
		groundTruth = new ArrayList<ArrayList<Double>>();
		withLowerThreshold = new ArrayList<Double>();
		withUpperThreshold = new ArrayList<Double>();
		squareRootsOfVariance = new ArrayList<Double>();
		
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
		int expectedNumberOfArguments = 1;
		if (args.length != expectedNumberOfArguments) {
			String msg = "expected " + expectedNumberOfArguments 
					+ " number of arguments but received "
					+ args.length
					+ " number of arguments.";
			System.out.println(msg);
		}
		String userDir = args[0];
		
		Double givenLowerThreshold = 1.0;
		Double givenUpperThreshold = 2.0;
		CountStep c3 = null;
		try {
			c3 = new CountStep(userDir);
			c3.calculateSquareRootOfVariance();
			c3.applyLowerThreshold(givenLowerThreshold);
			c3.applyUpperThreshold(givenUpperThreshold);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		int result = c3.countSteps();
		System.out.println(result);
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
	
	public void calculateSquareRootOfVariance() throws Exception {
		
		if (haveCalculatedSquareRootOfVariance) {
			throw new Exception(ERROR_OVERCOMPUTING_VARIANCE );
		}
		Double variance;
		Double squareRootOfVariance;

		for (int k=0; k<this.groundTruth.size(); k++) {
			variance = this.calculateLocalVariance(k);
			squareRootOfVariance = Math.sqrt(variance);
			this.squareRootsOfVariance.add(squareRootOfVariance);
		}


		haveCalculatedSquareRootOfVariance = true;

	}

	public void applyLowerThreshold(Double givenLowerThreshold) throws Exception {
		if (!haveCalculatedSquareRootOfVariance) {
		    throw new Exception(ERROR_VARIANCE_NOT_COMPUTED);
		}
		Double squareRootOfVariance;
		this.lowerThreshold = givenLowerThreshold;
		for (int k=0; k<this.groundTruth.size(); k++) {
			 
			squareRootOfVariance = squareRootsOfVariance.get(k);
			
			if (squareRootOfVariance<givenLowerThreshold) {
				this.withLowerThreshold.add(givenLowerThreshold);
			} else {
				this.withLowerThreshold.add(NOT_A_VALUE);
			}
		}
		
	}

	public ArrayList<Double> getLowerThresholds() {
	
		return withLowerThreshold;
	}

	public void applyUpperThreshold(Double givenUpperThreshold) throws Exception {
		if (!haveCalculatedSquareRootOfVariance) {
		    throw new Exception(ERROR_VARIANCE_NOT_COMPUTED);
		}
		Double squareRootOfVariance;
		Double zeroOtherwiseAsWrittenInAlgorithm = 0.0;
		for (int k=0; k<this.groundTruth.size(); k++) {
			 
			squareRootOfVariance = squareRootsOfVariance.get(k);
			
			if (squareRootOfVariance>givenUpperThreshold) {
				this.withUpperThreshold.add(givenUpperThreshold);
			} else {
				this.withUpperThreshold.add(zeroOtherwiseAsWrittenInAlgorithm);
			}
		}
		
	}

	public int countSteps() {
		// TODO test that exceptions get thrown if upper and lower thresholds have not been computed.
		ArrayList<Integer> transitionsFromHighToLow = new ArrayList<>();
		
		int result = 0;
		for (int n=0; n<withUpperThreshold.size()-1; n++) {
			if (n + 1 == withUpperThreshold.size()) { // we want the (n+1)th element to still be within the bounds of the list
				continue;
			}
			Double whatsNext = withUpperThreshold.get(n);
			Double whatsAfterIt = withUpperThreshold.get(n+1); // look to the next value

			if (whatsAfterIt < whatsNext) { // ideally, whatsAfterIt would be 0, and whatsNext 1
				transitionsFromHighToLow.add(n); // we want to track the index of these, since they 'point' to a possible step, vis-a-vis Jimenez algorithm.
			}


		}
		Iterator<Integer> that = transitionsFromHighToLow.iterator();
		int indexOfPossibleStep;
		boolean hasReachedEndOfWindow = false;
		while (that.hasNext()) {
			indexOfPossibleStep = that.next();
			int indexAtEndOfWindow = indexOfPossibleStep + this.windowSize;
			
			for (int k = indexOfPossibleStep; k <= indexAtEndOfWindow; k++) {
				if (k > this.withLowerThreshold.size() - 1) {
					break;
				} else if (withLowerThreshold.get(k) > this.lowerThreshold) {
					break;
				} else if (withLowerThreshold.get(k) == this.lowerThreshold){
					if (k==indexAtEndOfWindow) {
						hasReachedEndOfWindow = true;
					}
					if (hasReachedEndOfWindow) {
						result++;
					}
					
				} else {
					// TODO i don't know what to do.
				}
			}
		}
		return result;
	}

}
