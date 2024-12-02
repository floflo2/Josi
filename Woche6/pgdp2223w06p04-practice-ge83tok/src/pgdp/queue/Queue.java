package pgdp.queue;

public class Queue {
    private QueueElement first;
    private QueueElement last;
    private int size;

    public void push(int number) {
        // TODO: Implementiere diese Methode!
    }

    public int pop() {
        // TODO: Implementiere diese Methode!
        return Integer.MIN_VALUE + 1;
    }

    public int[] toArray() {
        int[] queueAsArray = new int[size];
        QueueElement current = first;
        for(int i = 0; current != null; current = current.getNext(), i++) {
            queueAsArray[i] = current.getValue();
        }
        return queueAsArray;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
