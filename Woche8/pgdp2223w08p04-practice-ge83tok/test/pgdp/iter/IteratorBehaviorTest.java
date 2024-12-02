package pgdp.iter;

import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.jupiter.Public;
import org.junit.jupiter.api.Test;

import de.tum.in.test.api.dynamic.DynamicClass;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@MirrorOutput
@StrictTimeout(5)
@Public
public class IteratorBehaviorTest {

    @Test
    void testListIterator() {
        assertTrue(Arrays.asList(List.Iterator.class.getInterfaces()).contains(Iterator.class),
                "List Iterotor does not implement Iterator");
        var l = new List<String>();
        var element = l.new Element("");
        Iterator iter = (Iterator) l.new Iterator(null);
        assertFalse(iter.hasNext(), "Iterator should be empty");
        iter = (Iterator) l.new Iterator(element);
        assertTrue(iter.hasNext(), "Iterator should not be empty");
        assertEquals("", iter.next());
        assertFalse(iter.hasNext(), "Iterator should be empty by now");

        var e2 = l.new Element("b");
        e2.setNext(element);
        iter = (Iterator) l.new Iterator(e2);
        assertTrue(iter.hasNext(), "Iterator should not be empty");
        assertEquals("b", iter.next());
        assertTrue(iter.hasNext(), "Iterator should not be empty yet");
        assertEquals("", iter.next());
        assertFalse(iter.hasNext(), "Iterator should be empty by now");
    }

    @Test
    void testList() {
        assertTrue(Arrays.asList(List.class.getInterfaces()).contains(Iterable.class),
                "List does not implement Iteratable");
        var l = new List<String>();
        assertNotNull(l.iterator());
        assertFalse(l.iterator().hasNext());
        l.insert("69");
        var i = l.iterator();
        assertTrue(i.hasNext());
        assertEquals("69", i.next());
        assertFalse(i.hasNext());
    }

    @Test
    void testTreeIterator() {
        assertTrue(Arrays.asList(Tree.Iterator.class.getInterfaces()).contains(Iterator.class),
                "Tree iterator does not implement Iterator");
        var t = new Tree<String>();
        var e1 = t.new Element("a");
        var iter = (Iterator<String>) t.new Iterator(null);
        assertFalse(iter.hasNext(), "Iterator should be empty");
        iter = (Iterator<String>) t.new Iterator(e1);
        assertTrue(iter.hasNext(), "Iter should have content");
        assertEquals("a", iter.next());
        assertFalse(iter.hasNext(), "Iterator should be empty");
        var e2 = t.new Element("b");
        var e3 = t.new Element("🐧");
        e1.setRight(e2);
        e1.setLeft(e3);
        iter = (Iterator<String>) t.new Iterator(e1);

        assertTrue(iter.hasNext(), "Iter should have content for left");
        assertEquals("🐧", iter.next());
        assertTrue(iter.hasNext(), "Iter should have content for middle");
        assertEquals("a", iter.next());
        assertTrue(iter.hasNext(), "Iter should have content for right");
        assertEquals("b", iter.next());
        assertFalse(iter.hasNext(), "Iterator should be empty");

    }

    @Test
    void testTree() {
        assertTrue(Arrays.asList(Tree.class.getInterfaces()).contains(Iterable.class),
                "Tree does not implement Iteratable");
        var t = new Tree<Integer>();
        assertNotNull(t.iterator());
        assertFalse(t.iterator().hasNext());
        t.insert(69);
        t.insert(42);
        t.insert(420);
        var i = t.iterator();
        assertTrue(i.hasNext());
        assertEquals(42, i.next());
        assertTrue(i.hasNext());
        assertEquals(69, i.next());
        assertTrue(i.hasNext());
        assertEquals(420, i.next());
        assertFalse(i.hasNext());
    }
}

