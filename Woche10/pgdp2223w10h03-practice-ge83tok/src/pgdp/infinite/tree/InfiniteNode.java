package pgdp.infinite.tree;

import java.util.List;

public class InfiniteNode<T> {

    private final InfiniteTree<T> tree;
    private final InfiniteNode<T> parent;
    private final T value;

    public InfiniteNode(InfiniteTree<T> tree, T value, InfiniteNode<T> parent) {
        this.parent = parent;
        this.tree = tree;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public InfiniteNode<T> getParent() {
        return parent;
    }

    /**
     * @return Gibt alle bisher berechneten Kinder des Knotens zurück.
     */
    public List<InfiniteNode<T>> getChildren() {
        // TODO: Implementieren.
        return null;
    }

    /**
     * Berechnet das nächste Kind des Knotens und gibt es zurück.
     * @return das neue Kind oder null, wenn es keine weiteren Kinder gibt.
     */
    public InfiniteNode<T> calculateNextChild() {
        // TODO: Implementieren.
        return null;
    }

    /**
     * Berechnet alle Kinder des Knotens.
     */
    public void calculateAllChildren() {
        // TODO: Implementieren.
    }

    /**
     * @return true, wenn alle Kinder berechnet wurden, false sonst.
     */
    public boolean isFullyCalculated() {
        // TODO: Implementieren.
        return false;
    }

    /**
     * Setzt die gesamte Berechnung des Knotens zurück.
     */
    public void resetChildren() {
        // TODO: Implementieren.
    }

}
