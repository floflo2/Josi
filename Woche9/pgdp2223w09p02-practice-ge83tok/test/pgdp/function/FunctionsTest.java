package pgdp.function;

import de.tum.in.test.api.dynamic.Check;
import de.tum.in.test.api.dynamic.DynamicClass;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class FunctionsTest {

    static DynamicClass<?> functions;

    @BeforeAll
    static void init() {
        functions = DynamicClass.toDynamic("pgdp.function.Functions");
    }

    @W09P
    void testMap() {
        var map = functions.method(List.class, "map", List.class, Function.class);
        map.check(Check.PUBLIC, Check.STATIC);
        var tmp = map.invokeStatic(List.of(), Function.identity());
        assertNotNull(tmp, "return value is null");
        assertTrue(tmp.isEmpty(), "empty list results in non empty list");
        var original = IntStream.range(0, 100).boxed().toList();
        var squared = map.invokeStatic(original, (Function<Integer, Integer>) i -> i * i);
        assertNotNull(squared, "mapped list is null");
        assertEquals(100, squared.size(), "mapped list has wrong size");
        for (int i = 0; i < 100; i++) {
            assertEquals(i * i, squared.get(i));
        }
    }

    @W09P
    void testSquare() {
        var square = functions.method(List.class, "square", List.class);
        square.check(Check.PUBLIC, Check.STATIC);
        var tmp = square.invokeStatic(List.of());
        assertNotNull(tmp, "return value is null");
        assertTrue(tmp.isEmpty(), "empty list results in non empty list");
        var original = IntStream.range(0, 100).boxed().toList();
        var squared = square.invokeStatic(original);
        assertNotNull(squared, "result is null");
        assertEquals(100, squared.size(), "result has wrong size");
        for (int i = 0; i < 100; i++) {
            assertEquals(i * i, squared.get(i));
        }
    }

    @W09P
    void testToString() {
        var toString = functions.method(List.class, "toString", List.class);
        toString.check(Check.PUBLIC, Check.STATIC);
        var tmp = toString.invokeStatic(List.of());
        assertNotNull(tmp, "result is null");
        assertTrue(tmp.isEmpty(), "empty list results in non empty list");
        var original = IntStream.range(0, 100).boxed().toList();
        var strings = toString.invokeStatic(original);
        assertNotNull(strings, "result null");
        assertEquals(100, strings.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(Integer.toString(i), strings.get(i));
        }
    }

    @W09P
    void testFilter() {
        var filter = functions.method(List.class, "filter", List.class, Predicate.class);
        filter.check(Check.PUBLIC, Check.STATIC);
        var tmp = filter.invokeStatic(List.of(), (Predicate<Integer>) i -> i == 1);
        assertNotNull(tmp, "result is null");
        assertTrue(
                tmp.isEmpty(),
                "empty list results in non empty list"
        );
        var original = IntStream.range(0, 100).boxed().toList();
        var filtered = filter.invokeStatic(original, (Predicate<Integer>) i -> i == 1);
        assertNotNull(filtered, "result is null");
        assertEquals(1, filtered.size(), "list has wrong size");
        assertEquals(1, filtered.get(0), "wrong element remains");
    }

    @W09P
    void testFilterAny() {
        var filter = functions.method(List.class, "filterAny", List.class, Predicate.class, Predicate.class);
        filter.check(Check.PUBLIC, Check.STATIC);
        var tmp = filter.invokeStatic(List.of(), (Predicate<Integer>) i -> i == 1, (Predicate<Integer>) i -> i == 4);
        assertNotNull(tmp, "result null");
        assertTrue(tmp
                .isEmpty(), "empty list results in non empty list");
        var original = IntStream.range(0, 100).boxed().toList();
        var filtered =
                filter.invokeStatic(original, (Predicate<Integer>) i -> i == 1, (Predicate<Integer>) i -> i == 3);
        assertNotNull(filtered, "result is null");
        assertEquals(2, filtered.size(), "list has wrong size");
        assertEquals(1, filtered.get(0), "wrong element remains");
        assertEquals(3, filtered.get(1), "wrong element remains");
    }

    @W09P
    void testMultiple2or7() {
        var multiple = functions.method(List.class, "multiple2or7", List.class);
        multiple.check(Check.PUBLIC, Check.STATIC);
        var tmp = multiple.invokeStatic(List.of());
        assertNotNull(tmp, "result is null");
        assertTrue(tmp.isEmpty(), "empty list results in non empty list");
        var original = IntStream.range(0, 100).boxed().toList();
        var result = multiple.invokeStatic(original);
        assertNotNull(result, "result is null");
        var should = IntStream.range(0, 100).filter(((IntPredicate) i -> (i & 1) == 0).or(i -> (i % 7) == 0)).toArray();
        assertEquals(should.length, result.size(), "result has wrong size");
        for (int i = 0; i < should.length; i++) {
            assertEquals(should[i], result.get(i));
        }
    }
}
