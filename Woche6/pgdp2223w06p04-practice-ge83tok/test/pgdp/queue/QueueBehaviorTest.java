package pgdp.queue;

import org.junit.jupiter.api.*;


import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.PathType;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.Public;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Stephan Krusche (krusche@in.tum.de)
 * @version 5.1 (11.06.2021)
 */
@Public
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath("target/test-classes") // prevent access to test-related classes and resources
class QueueBehaviorTest {

    @Test
    void testPush() {
        Queue queue = new Queue();
        queue.push(1);
        queue.push(2);
        queue.push(3);

        assertArrayEquals(new int[] { 1, 2, 3 }, queue.toArray(), "Nach Pushen von 1, dann 2, dann 3 sollte die Queue folgendermaßen aussehen: [1, 2, 3]."
                + " Sie sieht aber so aus: " + Arrays.toString(queue.toArray()));
    }

    @Test
    void testPushThenPop() {
        Queue queue = new Queue();
        queue.push(1);
        queue.push(2);
        queue.push(3);
        queue.push(4);
        queue.push(5);

        assertEquals(1, queue.pop(), "Nach Pushen von 1, dann 2, dann 3, dann 4, dann 5 sollte die erste Pop-Operation eine 1 liefern!");
        assertEquals(2, queue.pop(), "Nach Pushen von 1, dann 2, dann 3, dann 4, dann 5 sollte die zweite Pop-Operation eine 2 liefern!");
        assertEquals(3, queue.pop(), "Nach Pushen von 1, dann 2, dann 3, dann 4, dann 5 sollte die dritte Pop-Operation eine 3 liefern!");
        assertEquals(4, queue.pop(), "Nach Pushen von 1, dann 2, dann 3, dann 4, dann 5 sollte die vierte Pop-Operation eine 4 liefern!");
        assertEquals(5, queue.pop(), "Nach Pushen von 1, dann 2, dann 3, dann 4, dann 5 sollte die fünfte Pop-Operation eine 5 liefern!");
    }

}
