package pgdp.pools;

import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicConstructor;
import de.tum.in.test.api.dynamic.DynamicField;
import de.tum.in.test.api.dynamic.DynamicMethod;
import de.tum.in.test.api.jupiter.Hidden;
import de.tum.in.test.api.jupiter.Public;
import org.junit.jupiter.api.*;
import pgdp.W09H01;

import java.lang.reflect.Field;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@Order(3)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TupleFactoryTest {

    private final String packageString = "pgdp.pools.";

    private final DynamicClass<?> tuple = new DynamicClass<>(packageString + "Tuple");
    private final DynamicConstructor<?> newTuple = tuple.constructor(Object.class, Object.class);
    private final DynamicMethod<?> getT = tuple.method(Object.class, "getT");
    private final DynamicMethod<?> getS = tuple.method(Object.class, "getS");

    private final DynamicClass<?> tuplePool = new DynamicClass<>(packageString + "TuplePool");
    private final DynamicConstructor<?> newTuplePool = tuplePool.constructor();
    private final DynamicMethod<Tuple> insert = tuplePool.method(Tuple.class, "insert", Tuple.class);
    private final DynamicMethod<Tuple> getByValue = tuplePool.method(Tuple.class, "getByValue", Object.class, Object.class);
    private final DynamicMethod<Integer> getNumberOfTuples = tuplePool.method(int.class, "getNumberOfTuples");

    private final DynamicClass<?> tupleFactory = new DynamicClass<>(packageString + "TupleFactory");
    private final DynamicConstructor<?> newTupleFactory = tupleFactory.constructor();
    private final DynamicMethod<?> create = tupleFactory.method(tuple.toClass(), "create", Object.class, Object.class);
    private final DynamicMethod<?> intern = tupleFactory.method(tuple.toClass(), "intern", tuple.toClass());
    private DynamicField<?> poolField;

    private Object tf;

    @BeforeAll
    public void setup() {
        Field[] fields = TupleFactory.class.getDeclaredFields();

        for (Field f : fields) {
            if (f.getType().equals(TuplePool.class)) {
                poolField = tupleFactory.field(f.getType(), f.getName());
                return;
            }
        }
    }

    @BeforeEach
    public void initialize() {

        tf = newTupleFactory.newInstance();
    }

    @Public
    @Nested
    @W09H01
    public class PublicTests {

        @Test
        @DisplayName("Public | simple new Tuple Test")
        public void simpleTupleTest() {

            int t = 1;
            int s = 1;

            Object tuple = create.invokeOn(tf, t, s);
            assertEquals(t, getT.invokeOn(tuple), "Das zurückgegebene Tupel hat nicht die richtigen Werte.");
            assertEquals(s, getS.invokeOn(tuple), "Das zurückgegebene Tupel hat nicht die richtigen Werte.");
        }

        @Test
        @DisplayName("Public | Simple intern Test")
        public void simpleIntern() {

            int t = 1;
            int s = 1;
            Object testTuple = newTuple.newInstance(t, s);

            Object interned = intern.invokeOn(tf, testTuple);

            assertSame(testTuple, interned, "Das in einen leeren TuplePool einzufügende Tupel wurde nicht zurückgegeben");

        }

    }

    @Hidden
    @Nested
    @W09H01
    public class HiddenTests {

        @Test
        @DisplayName("Hidden | Background create")
        public void backgroundCreateTest() throws IllegalAccessException {

            HashSet<String> qt = new HashSet<>();
            qt.add("I see your schwartz is as big as mine.");
            HashSet<String> qtc = new HashSet<>(qt);

            HashSet<String> qs = new HashSet<>();
            qs.add("Oooh, that's gonna leave a mark.");
            HashSet<String> qsc = new HashSet<>(qs);

            Object pool = newTuplePool.newInstance();
            poolField.toField().set(tf, pool);

            Object testTuple = newTuple.newInstance(qt, qs);

            Object created = create.invokeOn(tf, qt, qs);

            assertEquals(testTuple, created, "Das erstellte Tupel hat nicht die übergebenen Argumente");
            assertSame(created, getByValue.invokeOn(pool, qt, qs), "Erstelltes Tupel ist nicht im TuplePool.");

            assertSame(created, create.invokeOn(tf, qtc, qsc), "Neues Tupel mit bekannten werten wird neu erstellt.");

            assertEquals(1, getNumberOfTuples.invokeOn(pool), "Es wurden mehr Tuple in den Pool eingefügt, als einzigartige erstellt wurden.");

        }

        @Test
        @DisplayName("Hidden | Indirect create Test")
        public void indirectCreate() throws IllegalAccessException {

            HashSet<String> qt = new HashSet<>();
            qt.add("Prepare for Metamorphosis, are you ready Kafka?");
            HashSet<String> qtc = new HashSet<>(qt);

            HashSet<String> qs = new HashSet<>();
            qs.add("It's not that we're afraid of death, far from it, it's just that we've got this thing about death... It's not us!");
            HashSet<String> qsc = new HashSet<>(qs);

            Object pool = newTuplePool.newInstance();
            poolField.toField().set(tf, pool);

            Object testTuple = newTuple.newInstance(qt, qs);

            insert.invokeOn(pool, testTuple);

            assertSame(testTuple, create.invokeOn(tf, qtc, qsc), "Ein bereits im Pool befindliches Tupel wird neu erstellt.");

        }

        @Test
        @DisplayName("Hidden | Indirekt intern Test")
        public void indirectIntern() throws IllegalAccessException {

            HashSet<String> qt = new HashSet<>();
            qt.add("I'm a mog: half man, half dog. I'm my own best friend!");
            HashSet<String> qtc = new HashSet<>(qt);

            HashSet<String> qs = new HashSet<>();
            qs.add("So, Lord Helmet, at last we meet again for the first time for the last time.");
            HashSet<String> qsc = new HashSet<>(qs);



            Object pool = newTuplePool.newInstance();
            poolField.toField().set(tf, pool);

            Object testTuple = newTuple.newInstance(qt, qs);

            insert.invokeOn(pool, testTuple);
            assertSame(testTuple, intern.invokeOn(tf, newTuple.newInstance(qtc, qsc)), "Ein sich bereits im Pool befindliches Tupel wird bei intern nicht zurück gegeben.");
        }

    }
}