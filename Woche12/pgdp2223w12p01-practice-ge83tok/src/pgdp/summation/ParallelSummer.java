package pgdp.summation;

// TODO: Vervollständige diese Klasse, sodass sie einen Thread repräsentiert, der das übergebene Array von lowerBound
//  (inklusive) bis upperBound (exklusive) summiert und das Ergebnis in result speichert.
public class ParallelSummer {
    private int[] array;
    private int lowerBound;
    private int upperBound;

    private long result = 0;

    public ParallelSummer(int[] array, int lowerBound, int upperBound) {
        this.array = array;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public long getResult() {
        return result;
    }
}
