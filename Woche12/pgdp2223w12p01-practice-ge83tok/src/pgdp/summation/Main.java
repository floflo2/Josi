package pgdp.summation;

import java.util.stream.IntStream;
import org.apache.commons.lang3.NotImplementedException;

public class Main {
    public static long sumParallel(int[] array, int threadCount) {
        // TODO: Vervollständige diese Methode
        throw new NotImplementedException();
    }

    public static void main(String[] args) {
        int[] toSum = IntStream.range(0, 1_000_000_000).toArray();

        long startTime = System.currentTimeMillis();

        // TODO: Setze threadCount auf verschiedene Werte und ermittele so
        //  1. ob du "korrekt" parallelisierst, also mit mehreren Threads die Berechnung tatsächlich schneller ist
        //  2. mit wie vielen Threads genau die Berechnung am schnellsten ist
        long result = sumParallel(toSum, 1);

        long finishTime = System.currentTimeMillis();

        System.out.println("Ergebnis der Berechnung: " + result);
        System.out.println("Dauer der Berechnung: " + ( finishTime - startTime ) + "ms");
    }
}
