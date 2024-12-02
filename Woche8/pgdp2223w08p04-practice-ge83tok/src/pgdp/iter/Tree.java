package pgdp.iter;

import java.util.Iterator;
import java.util.Objects;

public class Tree<T extends Comparable<T>> implements Iterable<T> {
    private Element root;

    public void insert(T value) {
        Objects.requireNonNull(value);
        if (root == null) {
            root = new Element(value);
        } else {
            var node = root;
            while (true) {
                var cmp = value.compareTo(node.value);
                if (cmp == 0) {
                    break;
                } else if (cmp < 0) {
                    if (node.left == null) {
                        node.left = new Element(value);
                        break;
                    } else {
                        node = node.left;
                    }
                } else {
                    if (node.right == null) {
                        node.right = new Element(value);
                        break;
                    } else {
                        node = node.right;
                    }
                }
            }
        }
    }

    @Override
    public java.util.Iterator<T> iterator() {
		// TODO
        return null;
    }

    public class Element {
        private T value;
        private Element left;
        private Element right;

        public Element(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Element getLeft() {
            return left;
        }

        public void setLeft(Element left) {
            this.left = left;
        }

        public Element getRight() {
            return right;
        }

        public void setRight(Element right) {
            this.right = right;
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