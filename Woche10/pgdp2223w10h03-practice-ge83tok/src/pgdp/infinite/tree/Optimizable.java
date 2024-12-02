package pgdp.infinite.tree;

public interface Optimizable<T> {

    /**
     * Verarbeitet den gegebenen Wert und gibt zurück, ob der Wert der optimale Wert war.
     * @param t Der Wert, der verarbeitet werden soll.
     * @return true, wenn der Wert optimal war und die Suche beendet werden soll, false, wenn er nicht optimal war und
     *         die Suche weitergehen soll.
     */
    boolean process(T t);

    /**
     * @return Der Wert, der bis zu diesem Zeitpunkt der beste Wert war. Wenn process true zurückgegeben hat, ist
     * dieser Wert der beste Wert, der gefunden werden kann.
     */
    T getOptimum();
}
