package pgdp.summation;

import de.tum.in.test.api.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.text.*;
import java.util.*;

import static de.tum.in.test.api.util.ReflectionTestUtils.*;

import de.tum.in.test.api.jupiter.Public;

/**
 * @author Stephan Krusche (krusche@in.tum.de)
 * @version 5.1 (11.06.2021)
 */
@Public
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath("target/test-classes") // prevent access to test-related classes and resources
@AllowThreads
class BehaviorTest {

    @Test
    void testSummation() {
        assertAll(
                () -> testSummation( new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, 1, 55 ),
                () -> testSummation( new int[] { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20 }, 1, 110 ),
                () -> testSummation( new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, 2, 55 ),
                () -> testSummation( new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, 10, 55 ),
                () -> testSummation( new int[] { -42 }, 1, -42 ),
                () -> testSummation( new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, 100, 55 ),
                () -> testSummation( new int[] {}, 1, 0 )
        );
    }

    void testSummation(int[] array, int threadCount, long expected) {
        long actual = Main.sumParallel(array, threadCount);
        assertEquals(expected, actual, "Das Array " + Arrays.toString(array) + " wird mit " + threadCount + " Threads nicht korrekt summiert.");
    }

}
