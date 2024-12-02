package pgdp.threads;

import java.util.LinkedList;
import java.util.List;

public class SynchronizedList<T> {
    private List<T> innerList = new LinkedList<>();

    public void add(int index, T e) throws InterruptedException {
        innerList.add(index, e);
    }

    public T remove(int index) throws InterruptedException {
        return innerList.remove(index);
    }

    public T get(int index) throws InterruptedException {
        return innerList.get(index);
    }

    public boolean contains(T e) throws InterruptedException {
        return innerList.contains(e);
    }
}