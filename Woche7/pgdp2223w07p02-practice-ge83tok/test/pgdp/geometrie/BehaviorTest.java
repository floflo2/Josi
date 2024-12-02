package pgdp.geometrie;

import de.tum.in.test.api.jupiter.PublicTest;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.text.*;
import java.util.*;

import static de.tum.in.test.api.util.ReflectionTestUtils.*;

import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.PathType;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.Public;

/**
 * @author Stephan Krusche (krusche@in.tum.de)
 * @version 5.1 (11.06.2021)
 */
@Public
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath("target/test-classes") // prevent access to test-related classes and resources
class BehaviorTest {
    @PublicTest
    public void testCircumferenceCircle() {
        BaseArea a = new Circle(5);

        assertEquals(10 * Math.PI, a.circumference(), 0.0001);
    }

    @PublicTest
    public void testCircumferenceRecangle() {
        BaseArea a = new Rectangle(10, 5);

        assertEquals(30, a.circumference(), 0.0001);
    }

    @PublicTest
    public void testCircumferenceRegularPolygon() {
        BaseArea a = new RegularPolygon(4, 10);

        assertEquals(40, a.circumference(), 0.0001);
    }

    @PublicTest
    public void testCircumferenceSquare() {
        BaseArea a = new Square(5);

        assertEquals(20, a.circumference(), 0.0001);
    }

    @PublicTest
    public void testAreaCircle() {
        BaseArea a = new Circle(5);

        assertEquals(25 * Math.PI, a.area(), 0.0001);
    }

    @PublicTest
    public void testAreaRecangle() {
        BaseArea a = new Rectangle(10, 5);

        assertEquals(50, a.area(), 0.0001);
    }

    @PublicTest
    public void testAreaRegularPolygon() {
        BaseArea a = new RegularPolygon(4, 10);

        assertEquals(100, a.area(), 0.0001);
    }

    @PublicTest
    public void testAreaSquare() {
        BaseArea a = new Square(5);

        assertEquals(25, a.area(), 0.0001);
    }

    @PublicTest
    public void testIsSquareCircle() {
        BaseArea a = new Circle(5);

        assertFalse(a.isSquare());
    }

    @PublicTest
    public void testIsSquareRecangle() {
        BaseArea a = new Rectangle(10, 5);
        assertFalse(a.isSquare());

        a = new Rectangle(5, 5);
        assertTrue(a.isSquare());
    }

    @PublicTest
    public void testIsSquareRegularPolygon() {
        BaseArea a = new RegularPolygon(4, 10);
        assertTrue(a.isSquare());

        a = new RegularPolygon(3, 10);
        assertFalse(a.isSquare());
    }

    @PublicTest
    public void testIsSquareSquare() {
        BaseArea a = new Square(5);
        assertTrue(a.isSquare());
    }

    @PublicTest
    public void testToSquareCircle() {
        BaseArea a = new Circle(5);

        assertNull(a.toSquare());
    }

    @PublicTest
    public void testToSquareRecangle() {
        BaseArea a = new Rectangle(10, 5);
        assertNull(a.toSquare());

        a = new Rectangle(5, 5);
        assertEquals(5, a.toSquare().getLength(), 0.0001);
    }

    @PublicTest
    public void testToSquareRegularPolygon() {
        BaseArea a = new RegularPolygon(4, 10);
        assertEquals(10, a.toSquare().getLength(), 0.0001);

        a = new RegularPolygon(3, 10);
        assertNull(a.toSquare());
    }

    @PublicTest
    public void testToSquareSquare() {
        BaseArea a = new Square(5);
        assertEquals(5, a.toSquare().getLength(), 0.0001);
    }

    @PublicTest
    public void testPrismSurface1() {
        Prism p = new Prism(new Square(5), 5);
        assertEquals(150, p.surface(), 0.0001);
    }

    @PublicTest
    public void testPrismSurface2() {
        Prism p = new Prism(new RegularPolygon(3, 10), 5);
        assertEquals(236.6025403784439, p.surface(), 0.1);
    }

    @PublicTest
    public void testPrismVolume1() {
        Prism p = new Prism(new RegularPolygon(3, 10), 5);
        assertEquals(216.50635094610973, p.volume(), 0.1);
    }

    @PublicTest
    public void testPrismVolume2() {
        Prism p = new Prism(new Rectangle(5, 10), 5);
        assertEquals(250, p.volume(), 0.0001);
    }

    @PublicTest
    public void testPrismIsCube1() {
        Prism p = new Prism(new Rectangle(5, 10), 5);
        assertFalse(p.isCube());
    }

    @PublicTest
    public void testPrismIsCube2() {
        Prism p = new Prism(new RegularPolygon(4, 5), 5);
        assertTrue(p.isCube());
    }
}
