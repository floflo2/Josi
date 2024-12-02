package pgdp.sim;

import java.util.random.RandomGeneratorFactory;

public class RandomGenerator {

	private RandomGenerator() {
	}

	private static final RandomGeneratorFactory<? extends RandomGenerator> factory = RandomGeneratorFactory
			.of("Xoshiro256PlusPlus");
	private static java.util.random.RandomGenerator gen = factory.create(new byte[] { 13, 37, 4, 20, 69 });

	public static void reseed(byte[] seed) {
		gen = factory.create(seed);
	}

	/**
	 * 
	 * @return random integer
	 */
	public static int nextInt() {
		return gen.nextInt();
	}

	/**
	 * 
	 * @param upperBound upper bound of the returned int
	 * @return number between 0 (inclusive) and upperBound (exclusive)
	 */
	public static int nextInt(int upperBound) {
		return gen.nextInt(upperBound);
	}

	/**
	 * @param lowerBound lower bound of the returned integer
	 * @param upperBound upper bound of the returned integer
	 * @return number between lowerBound (inclusive) and upperBound (exclusive)
	 */
	public static int nextInt(int lowerBound, int upperBound) {
		return gen.nextInt(lowerBound, upperBound);
	}
}
