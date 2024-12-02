package pgdp.pools;

import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicConstructor;
import de.tum.in.test.api.dynamic.DynamicField;
import de.tum.in.test.api.dynamic.DynamicMethod;
import de.tum.in.test.api.jupiter.Hidden;
import de.tum.in.test.api.jupiter.Public;
import org.junit.jupiter.api.*;
import pgdp.W09H01;
import pgdp.hashset.TupleHashSet;

import java.lang.reflect.Field;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@Order(2)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TuplePoolTest {

    private static final String packageString = "pgdp.pools.";

    private final DynamicClass<?> tuple = new DynamicClass<>(packageString + "Tuple");
    private final DynamicConstructor<?> newTuple = tuple.constructor(Object.class, Object.class);

    private final DynamicClass<?> tuplePool = new DynamicClass<>(packageString + "TuplePool");
    private final DynamicConstructor<?> newTuplePool = tuplePool.constructor();
    private final DynamicMethod<?> insert = tuplePool.method(Object.class, "insert", tuple.toClass());
    private final DynamicMethod<?> getByValue = tuplePool.method(Object.class, "getByValue", Object.class, Object.class);
    private final DynamicMethod<Integer> getNumberOfTuples = tuplePool.method(int.class, "getNumberOfTuples");
    private DynamicField<?> hashSet;

    @BeforeAll
    public void setup() {

        Field[] fields = TuplePool.class.getDeclaredFields();

        for (Field f : fields) {
            if (f.getType().equals(TupleHashSet.class)) {
                hashSet = tuplePool.field(f.getType(), f.getName());
                return;
            }
        }
    }

    @Public
    @Nested
    @W09H01
    public class PublicTests {

        @Test
        @DisplayName("Public | Test simple insert")
        public void simpleInsert() {

            Object tp = newTuplePool.newInstance();
            TupleHashSet<Integer, Integer> tupleSet = (TupleHashSet<Integer, Integer>) hashSet.getOf(tp);

            Object testTuple = newTuple.newInstance(2,2);

            insert.invokeOn(tp, testTuple);

            assertEquals(1, tupleSet.insertedTuples(), "Ein in den Pool eingefügtes Tuple wird nicht ins HashSet eingefügt.");
            assertEquals(1, getNumberOfTuples.invokeOn(tp), "Dein TuplePool gibt nicht die richtige Anzahl an gespeicherten Tupeln zurück");
        }

        @Test
        @DisplayName("Public | Test simple find")
        public void simpleFind() {

            Object tp = newTuplePool.newInstance();
            TupleHashSet<Integer, Integer> testSet = (TupleHashSet<Integer, Integer>) hashSet.getOf(tp);

            int s = 2;
            int t = 2;

            Object testTuple = newTuple.newInstance(t, s);

            insert.invokeOn(tp, testTuple);

            assertEquals(1, testSet.insertedTuples(), "Ein in den Pool eingefügtes Tuple wird nicht ins HashSet eingefügt.");
            assertEquals(1, getNumberOfTuples.invokeOn(tp), "Dein TuplePool gibt nicht die richtige Anzahl an gespeicherten Tupeln zurück");

            assertEquals(testTuple, getByValue.invokeOn(tp, t, s), "Ein eingefügtes Tupel wurde nicht gefunden.");
        }


    }
    @Hidden
    @Nested
    @W09H01
    public class HiddenTests {

        @Test
        @DisplayName("Hidden | Background Test")
        public void backgroundTest() throws IllegalAccessException {

            HashSet<String> q = new HashSet<>();
            q.add("What's the matter, Colonel Sandurz? Chicken?");
            q.add("Out of order? F***! Even in the future, nothing works!");

            HashSet<String> qc = new HashSet<>(q);

            HashSet<Object> empty = new HashSet<>();

            int t = 0;

            Object tq = newTuple.newInstance(t, q);
            Object tqc = newTuple.newInstance(t, qc);
            Object tEmpty = newTuple.newInstance(t, empty);

            Object pool = newTuplePool.newInstance();
            TupleHashSet<Integer, HashSet<String>> testSet = (TupleHashSet<Integer, HashSet<String>>) hashSet.getOf(pool);
            hashSet.toField().set(pool, testSet);

            assertEquals(tEmpty, insert.invokeOn(pool, tEmpty), "Tupel kann nicht erfolgreich in Pool eingefügt werden.");

            assertEquals(1, testSet.insertedTuples(), "Ein in den Pool eingefügtes Tuple wird nicht ins HashSet eingefügt.");
            assertEquals(1, getNumberOfTuples.invokeOn(pool), "Dein TuplePool gibt nicht die richtige Anzahl an gespeicherten Tupeln zurück");

            assertEquals(tEmpty, getByValue.invokeOn(pool, t, empty), "Ein eingefügtes Tupel wurde nicht gefunden.");

            Object inserted = testSet.removeFirstElement();

            assertSame(tEmpty, inserted, "Das in das HashSet eingefügt Tupel ist nicht das in den Pool eingefügte Tupel.");

            testSet.insert((Tuple<Integer, HashSet<String>>) tq);

            assertEquals(tq, getByValue.invokeOn(pool, t, q), "Pool testet das Hashset nicht richtig auf beinhaltete Tupel");

            assertEquals(tq, insert.invokeOn(pool, tqc), "Bestehende Referenz von gleichem Element beim einfügen nicht zurück gegeben.");

            assertEquals(1, testSet.insertedTuples(), "Es sind nicht genau ein Tupel im HashSet, obwohl nur ein einzigartiges eingefügt wurde.");
            assertEquals(1, getNumberOfTuples.invokeOn(pool), "Die Anzahl an einzigartigen Tupeln im Pool stimmt nicht.");

        }

        @Test
        @DisplayName("Hidden | Background Test reversed")
        public void backgroundTestReversed() {

            HashSet<String> q = new HashSet<>();
            q.add("No, it's not what you think. It's much, much worse!");
            q.add("Yogurt! Yogurt! I hate Yogurt! Even with Strawberries.");

            HashSet<String> qc = new HashSet<>(q);

            HashSet<Object> empty = new HashSet<>();

            int s = 0;

            Object tq = newTuple.newInstance(q, s);
            Object tqc = newTuple.newInstance(qc, s);
            Object tEmpty = newTuple.newInstance(empty, s);

            Object pool = newTuplePool.newInstance();
            TupleHashSet<HashSet<String>, Integer> testSet = (TupleHashSet<HashSet<String>, Integer>) hashSet.getOf(pool);

                    assertEquals(tEmpty, insert.invokeOn(pool, tEmpty), "Tupel kann nicht erfolgreich in Pool eingefügt werden.");

            assertEquals(1, testSet.insertedTuples(), "Ein in den Pool eingefügtes Tuple wird nicht ins HashSet eingefügt.");
            assertEquals(1, getNumberOfTuples.invokeOn(pool), "Dein TuplePool gibt nicht die richtige Anzahl an gespeicherten Tupeln zurück");

            assertEquals(tEmpty, getByValue.invokeOn(pool, empty, s), "Ein eingefügtes Tupel wurde nicht gefunden.");

            Object inserted = testSet.removeFirstElement();

            assertSame(tEmpty, inserted, "Das in das HashSet eingefügt Tupel ist nicht das in den Pool eingefügte Tupel.");

            testSet.insert((Tuple<HashSet<String>, Integer>) tq);

            assertEquals(tq, getByValue.invokeOn(pool, q, s), "Pool testet das Hashset nicht richtig auf beinhaltete Tupel");

            assertEquals(tq, insert.invokeOn(pool, tqc), "Bestehende Referenz von gleichem Element beim einfügen nicht zurück gegeben.");

            assertEquals(1, testSet.insertedTuples(), "Es sind nicht genau ein Tupel im HashSet, obwohl nur ein einzigartiges eingefügt wurde.");
            assertEquals(1, getNumberOfTuples.invokeOn(pool), "Die Anzahl an einzigartigen Tupeln im Pool stimmt nicht.");

        }
    }

}
