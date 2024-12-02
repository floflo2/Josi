package pgdp.iter;

import java.util.Iterator;

public class List<T> implements Iterable<T> {

    private Element first;
    private Element last;

    void insert(T value) {
        if (first == null) {
            first = new Element(value);
            last = first;
        } else {
            var n = new Element(value);
            last.setNext(n);
            last = n;
        }
    }

    @Override
    public java.util.Iterator<T> iterator() {
		// TODO
        return null;
    }

    public class Element {
        private T value;
        private Element next;

        Element(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Element getNext() {
            return next;
        }

        public void setNext(Element next) {
            this.next = next;
        }
    }

    public class Iterator implements java.util.Iterator<T> {
		// TODO
		
        public Iterator(Element e) {
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            return null;
        }
    }

}