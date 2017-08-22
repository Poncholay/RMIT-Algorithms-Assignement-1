import java.util.Random;

public class IntegerGenerator {

	private int mStartOfRange;
	private int mEndOfRange;
	private Random mRandGen;

	public IntegerGenerator(int startOfRange, int endOfRange, long seed) throws IllegalArgumentException {
		if (startOfRange < 0 || endOfRange < 0 || startOfRange > endOfRange) {
			throw new IllegalArgumentException("startOfRange or endOfRange is invalid.");
		}
		mStartOfRange = startOfRange;
		mEndOfRange = endOfRange;
		mRandGen = new Random(seed);
	}

	public int[] sampleWithReplacement(int sampleSize) {
		int[] samples = new int[sampleSize];
		for (int i = 0; i < sampleSize; i++) {
			samples[i] = mStartOfRange + mRandGen.nextInt(mEndOfRange - mStartOfRange + 1);
		}
		return samples;
	}

	public int[] sampleWithOutReplacement(int sampleSize) throws IllegalArgumentException {
		int populationSize = mEndOfRange - mStartOfRange + 1;

		if (sampleSize > populationSize) {
			throw new IllegalArgumentException("SampleSize cannot be greater than populationSize for sampling without replacement.");
		}

		int[] samples = new int[sampleSize];
		for (int i = 0; i < sampleSize; i++) {
			samples[i] = i + mStartOfRange;
		}

		for (int j = sampleSize; j < populationSize; j++) {
			int t = mRandGen.nextInt(j + 1);
			if (t < sampleSize) {
				samples[t] = j + mStartOfRange;
			}
		}

		return samples;
	}
}
