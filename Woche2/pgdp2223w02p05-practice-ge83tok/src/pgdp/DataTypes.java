package pgdp;

public class DataTypes {

	public static void calculateBitsForLong() {
		// TODO
		System.out.println("Einem long stehen ?? Bits zur Verfügung.");
	}

	public static void smallestTypePossible(double in) {
		// TODO
		System.out.println("TODO");
	}

	public static void main(String[] args) {
		smallestTypePossible(Double.MAX_VALUE);
		smallestTypePossible(Long.MAX_VALUE);
		smallestTypePossible(Integer.MAX_VALUE);
		smallestTypePossible(Short.MAX_VALUE);
		smallestTypePossible(Byte.MAX_VALUE);
		System.out.println();
		calculateBitsForLong();
	}

}
