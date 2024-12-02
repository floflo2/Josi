package pgdp.functions;

public class Functions {

    public static int square(int n) {
        int square = n*n;
        return square;
    }

    public static int sumOfSquares(int a, int b) {
        int aSquared = square(a);
        int bSquared = square(b);
        int sum = aSquared + bSquared;

        return sum;
    }

    public static int cube(int n) {
        // TODO
        return 0;
    }

    public static int average(int a, int b, int c) {
        // TODO
        return 0;
    }

    public static boolean isPythagoreanTriple(int a, int b, int c) {
        // TODO: Benutze in dieser Methode keine arithmetischen Operatoren (i.e. +, -, *, /, % etc.)!
        return false;
    }

}
