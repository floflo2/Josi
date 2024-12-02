package pgdp.pools;

import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicConstructor;
import de.tum.in.test.api.dynamic.DynamicMethod;
import de.tum.in.test.api.jupiter.Hidden;
import de.tum.in.test.api.jupiter.Public;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import pgdp.W09H01;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Order(1)
public class TupleTests {

    private final DynamicClass<?> tuple = new DynamicClass<>("pgdp.pools.Tuple");
    private final DynamicConstructor<?> newTuple = tuple.constructor(Object.class, Object.class);
    private final DynamicMethod<Object> getT = tuple.method(Object.class, "getT");
    private final DynamicMethod<Object> getS = tuple.method(Object.class, "getS");
    private final DynamicMethod<Boolean> equals = tuple.method(boolean.class, "equals", Object.class);
    private final DynamicMethod<Integer> hashCode = tuple.method(int.class, "hashCode");

    @Public
    @Nested
    @W09H01
    public class PublicTests {

        @Test
        @DisplayName("Public | Konstruktor Test gleicher Typ")
        public void tupleConstructorI() {

            int t = 0;
            int s = 0;

            assertDoesNotThrow(() -> {
                newTuple.newInstance(t, s);
            }, "Beim erstellen eines Tupels mit zwei Integer Argumenten ist eine Exception aufgetreten:");

            assertDoesNotThrow(() -> {
                Object t1 = newTuple.newInstance(t, s);
                if (!getT.invokeOn(t1).equals(t) || !getS.invokeOn(t1).equals(s)) {
                    fail("Der Konstruktor setzt die Argumente nicht richtig, oder sie werden nicht richtig zurück gegeben.");
                }
            }, "Beim ausführen der Getter ist eine Exception aufgetreten");
        }

        @Test
        @DisplayName("Public | Konstruktor Test unterschiedliche Typen")
        public void tupleConstructorDifferent() {

            int t = 0;
            String s = "WHAT? You went over my helmet?";

            assertDoesNotThrow(() -> {
                newTuple.newInstance(t, s);
            }, "Beim erstellen eines Tupels mit einem Integer und einem String ist eine Exception aufgetreten:");

            assertDoesNotThrow(() -> {
                Object t1 = newTuple.newInstance(t, s);
                if (!getT.invokeOn(t1).equals(t) || !getS.invokeOn(t1).equals(s)) {
                    fail("Der Konstruktor setzt die Argumente nicht richtig, oder sie werden nicht richtig zurück gegeben.");
                }
            }, "Beim ausführen der Getter ist eine Exception aufgetreten");
        }

        @Test
        @DisplayName("Public | Equals test")
        public void tupleEquals() {

            Object t1 = newTuple.newInstance(0, 0);
            Object t2 = newTuple.newInstance(0, 1);

            assertFalse(equals.invokeOn(t1, t2) && equals.invokeOn(t2, t1), "Zwei unterschiedliche Tupel werden als gleich erkannt.");

            assertTrue(equals.invokeOn(t1, t1), "Das selbe Tupel wird nicht als gleich erkannt");
        }

        @Test
        @DisplayName("Public | Hashcode test")
        public void tupleHash() {

            Object t1 = newTuple.newInstance(0, 0);
            Object t2 = newTuple.newInstance(0, 0);

            assertEquals(hashCode.invokeOn(t1), hashCode.invokeOn(t2), "Zwei gleiche Tupel haben unterschiedliche Hashes.");
        }

    }

    @Hidden
    @Nested
    @W09H01
    public class HiddenTests {

        @Test
        @DisplayName("Hidden | Konstruktor Test")
        public void hiddenConstructor() {

            List<String> sl = new ArrayList<>();
            sl.add("My brains... are going into my feet!");
            sl.add("I bet she gives great helmet");

            double d = 4.6;

            Object t1 = newTuple.newInstance(d, sl);

            assertEquals(d, getT.invokeOn(t1), "Das im Konstruktor übergebene t entspricht nicht dem zurück gegebenen");
            assertEquals(sl, getS.invokeOn(t1), "Das im Konstruktor übergebene s entspricht nicht dem zurück gegebenen");
        }

        @Test
        @DisplayName("Hidden | Equals Test")
        public void hiddenEquals() {

            String shouldEqual = "Zwei Tupel werden als ungleich erkannt, obwohl sie gleich sind.";
            String shouldNotEqual = "Zwei Tupel werden als gleich erkannt, obwohl sie ungleich sind.";

            Set<String> hs1 = new HashSet<>();
            hs1.add("Well, I hope it's a long ceremony, 'cause it's gonna be a short honeymoon.");

            Set<String> hs1c = Set.copyOf(hs1);

            Set<String> hs2 = new HashSet<>();
            hs2.add("There's only one man who would dare give me the raspberry: Lone Star!");

            double d = 1;
            float f = 1;

            Object t1 = newTuple.newInstance(d, hs1);
            Object t2 = newTuple.newInstance(d, hs1);

            assertTrue(equals.invokeOn(t1, t2) && equals.invokeOn(t2, t1), shouldEqual);

            t2 = newTuple.newInstance(d, hs2);
            assertFalse(equals.invokeOn(t1, t2) || equals.invokeOn(t2, t1), shouldNotEqual);

            t2 = newTuple.newInstance(d, hs1);
            assertTrue(equals.invokeOn(t1, t2) && equals.invokeOn(t2, t1), shouldEqual);

            t2 = newTuple.newInstance(d, hs1c);
            assertTrue(equals.invokeOn(t1, t2) && equals.invokeOn(t2, t1), shouldEqual);

            assertTrue(equals.invokeOn(t1, t1), shouldEqual);

            Object tf = newTuple.newInstance(f, hs2);
            assertFalse(equals.invokeOn(t1, tf) || equals.invokeOn(tf, t1), shouldNotEqual);

            Object tr1 = newTuple.newInstance(hs1, d);
            Object tr2 = newTuple.newInstance(hs1, d);

            assertTrue(equals.invokeOn(tr1, tr2) && equals.invokeOn(tr2, tr1), shouldEqual);

            tr2 = newTuple.newInstance(hs2, d);
            assertFalse(equals.invokeOn(tr1, tr2) || equals.invokeOn(tr2, tr1), shouldNotEqual);

            tr2 = newTuple.newInstance(hs1, d);
            assertTrue(equals.invokeOn(tr1, tr2) && equals.invokeOn(tr2, tr1), shouldEqual);

            tr2 = newTuple.newInstance(hs1c, d);
            assertTrue(equals.invokeOn(tr1, tr2) && equals.invokeOn(tr2, tr1), shouldEqual);

            assertTrue(equals.invokeOn(tr1, tr1), shouldEqual);

            Object trf = newTuple.newInstance(f, hs2);
            assertFalse(equals.invokeOn(tr1, trf) || equals.invokeOn(trf, tr1), shouldNotEqual);

        }

        /**
         * 1P
         */
        @Test
        @DisplayName("Hidden | Hash Test")
        public void hiddenHash() {

            String shouldEqual = "Zwei Tupel hatten einen unterschiedlichen Hashcode, obwohl sie gleich sind.";

            Set<String> hs1 = new HashSet<>();
            hs1.add("Call me idiot, not you captain... I mean, you know what I mean.");

            Set<String> hs1c = Set.copyOf(hs1);

            double d = 1;

            Object t1 = newTuple.newInstance(d, hs1);
            Object t2 = newTuple.newInstance(d, hs1);

            assertEquals(hashCode.invokeOn(t1), hashCode.invokeOn(t2), shouldEqual);

            t2 = newTuple.newInstance(d, hs1);
            assertEquals(hashCode.invokeOn(t1), hashCode.invokeOn(t2), shouldEqual);

            t2 = newTuple.newInstance(d, hs1c);
            assertEquals(hashCode.invokeOn(t1), hashCode.invokeOn(t2), shouldEqual);

            Object tr1 = newTuple.newInstance(hs1, d);
            Object tr2 = newTuple.newInstance(hs1, d);

            assertEquals(hashCode.invokeOn(tr1), hashCode.invokeOn(tr2), shouldEqual);

            tr2 = newTuple.newInstance(hs1, d);
            assertEquals(hashCode.invokeOn(tr1), hashCode.invokeOn(tr2), shouldEqual);

            tr2 = newTuple.newInstance(hs1c, d);
            assertEquals(hashCode.invokeOn(tr1), hashCode.invokeOn(tr2), shouldEqual);

        }
    }
}
