import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class TestBinarySearchTree {

	private BstMultiset.BinarySearchTree<Integer> mTree;

	@Before
	public void setupList() {
		mTree = new BstMultiset.BinarySearchTree<>();
	}

	@Test
	public void testAdd() throws IndexOutOfBoundsException {
		mTree.add(1);
	}

	@Test
	public void testDelete() {
		DataGenerator gen = new DataGenerator(0, 1000);
		int[] data = gen.sampleWithOutReplacement(25);
		for (int i = 0; i < 25; i++) {
			mTree.add(data[i]);
		}

		System.out.println(data[7] + " " + data[8] + " " + data[9] + " " + data[10]);
		mTree.remove(data[7]);
		mTree.remove(data[8]);
		mTree.remove(data[9]);
		mTree.remove(data[10]);

		int i = 0;
		for (Integer tmp : mTree) {
			System.out.println(tmp);
			i++;
		}

		Assert.assertTrue(i == 21);
	}

	@Test
	public void testPrint() throws IndexOutOfBoundsException {
		DataGenerator gen = new DataGenerator(0, 1000);
		int[] data = gen.sampleWithOutReplacement(25);
		for (int i = 0; i < 25; i++) {
			mTree.add(data[i]);
		}

		for (Integer tmp : mTree) {
			System.out.println(tmp);
		}
	}

	public class DataGenerator {

		protected int mStartOfRange;
		protected int mEndOfRange;
		Random mRandGen;

		public DataGenerator(int startOfRange, int endOfRange) throws IllegalArgumentException {
			if (startOfRange < 0 || endOfRange < 0 || startOfRange > endOfRange) {
				throw new IllegalArgumentException("startOfRange or endOfRange is invalid.");
			}
			mStartOfRange = startOfRange;
			mEndOfRange = endOfRange;
			mRandGen = new Random(System.currentTimeMillis());
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
				int t = mRandGen.nextInt(j+1);
				if (t < sampleSize) {
					samples[t] = j + mStartOfRange;
				}
			}

			return samples;
		}
	}
}
