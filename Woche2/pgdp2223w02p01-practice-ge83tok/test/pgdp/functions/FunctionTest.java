package pgdp.functions;

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
@StrictTimeout(2)
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath("target/test-classes") // prevent access to test-related classes and resources
class FunctionTest {

    /* ================================ Test cube() ================================ */

    @Public
    @Test
    public void testCube() {
        assertAll(
                () -> testCube(0, 0),
                () -> testCube(1, 1),
                () -> testCube(-1, -1),
                () -> testCube(3, 27),
                () -> testCube(-8, -512),
                () -> testCube(1_000, 1_000_000_000),
                () -> testCube(-1_211, -1_775_956_931)
        );
    }

    private void testCube(int n, int expectedCube) {
        int actualCube = Functions.cube(n);
        assertEquals(expectedCube, actualCube, "Die Zahl " + n + " wurde nicht korrekt hoch 3 genommen.");
    }

    /* ================================ Test average() ================================ */

    @Public
    @Test
    public void testAverage() {
        assertAll(
                () -> testAverage(0, 0, 0, 0),
                () -> testAverage(5, 5, 5, 5),
                () -> testAverage(3, 4, 5, 4),
                () -> testAverage(2, 4, 12, 6),
                () -> testAverage(1, 4, 22, 9),
                () -> testAverage(-7, -4, 8, -1)
        );
    }

    private void testAverage(int a, int b, int c, int expectedAverage) {
        int actualAverage = Functions.average(a, b, c);
        assertEquals(expectedAverage, actualAverage,
                "Der Durchschnitt der drei Zahlen " + a + ", " + b + " und " + c +
                        " wurde nicht korrekt berechnet.");
    }

    /* ================================ Test isPythagoreanTriple() ================================ */

    @Public
    @Test
    public void testIsPythagoreanTriple() {
        assertAll(
                () -> testIsPythagoreanTriple(0, 0, 0, true),
                () -> testIsPythagoreanTriple(5, 5, 5, false),
                () -> testIsPythagoreanTriple(3, 4, 5, true),
                () -> testIsPythagoreanTriple(2, 4, 12, false),
                () -> testIsPythagoreanTriple(5, 12, 13, true),
                () -> testIsPythagoreanTriple(5, 13, 14, false),
                () -> testIsPythagoreanTriple(8, 15, 17, true),
                () -> testIsPythagoreanTriple(115, 252, 277, true)
        );
    }

    private void testIsPythagoreanTriple(int a, int b, int c, boolean expectedResult) {
        boolean actualResult = Functions.isPythagoreanTriple(a, b, c);
        assertEquals(expectedResult, actualResult,
                expectedResult
                        ? "Die drei Zahlen " + a + ", " + b + " und " + c + " wurden nicht korrekt als 'Pythagoreisches Tripel' erkannt."
                        : "Die drei Zahlen " + a + ", " + b + " und " + c + " wurden fälschlicherweise als 'Pythagoreisches Tripel' erkannt."
        );
    }

}
