package pgdp.datatypes.rationals;

import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicConstructor;
import de.tum.in.test.api.dynamic.DynamicField;
import de.tum.in.test.api.dynamic.DynamicMethod;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.jupiter.PublicTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Stephan Krusche (krusche@in.tum.de)
 * @version 5.1 (11.06.2021)
 */
@Public
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath("target/test-classes") // prevent access to test-related classes and resources
class BehaviorTest {

    private static final String RATIONALS_PACKAGE = "pgdp.datatypes.rationals.";

    private static final DynamicClass<?> Rational = DynamicClass.toDynamic(RATIONALS_PACKAGE + "Rational");

    private static final DynamicConstructor<?> newRational = Rational.constructor(int.class, int.class);

    private static final DynamicField<?> numerator = Rational.field(int.class, "numerator");
    private static final DynamicField<?> denominator = Rational.field(int.class, "denominator");

    private static final DynamicMethod<?> multiplyBy = Rational.method(void.class, "multiplyBy", Rational.toClass());
    private static final DynamicMethod<?> divideBy = Rational.method(void.class, "divideBy", Rational.toClass());
    private static final DynamicMethod<?> add = Rational.method(void.class, "add", Rational.toClass());
    private static final DynamicMethod<?> subtract = Rational.method(void.class, "subtract", Rational.toClass());
    private static final DynamicMethod<?> toDouble = Rational.method(double.class, "toDouble");
    private static final DynamicMethod<?> toString = Rational.method(String.class, "toString");

    /*  ================  Test Rational  ================  */

    @PublicTest
    public void testRationalConstructor() {
        var rat = newRational.newInstance(22, 7);

        assertEquals(22, numerator.getOf(rat), "Konstruktor von 'Rational' erzeugt das falsche Objekt für den Bruch 22 / 7. 'numerator' ist falsch.");
        assertEquals(7, denominator.getOf(rat), "Konstruktor von 'Rational' erzeugt das falsche Objekt für den Bruch 22 / 7. 'denominator' ist falsch.");
    }

    @PublicTest
    public void testRationalMultiply() {
        var rat1 = newRational.newInstance(22, 7);
        var rat2 = newRational.newInstance(2, 3);

        multiplyBy.invokeOn(rat1, rat2);

        assertEquals(44, numerator.getOf(rat1), "'numerator' ist falsch.");
        assertEquals(21, denominator.getOf(rat1), "'denominator' ist falsch.");
    }

    @PublicTest
    public void testRationalDivide() {
        var rat1 = newRational.newInstance(22, 7);
        var rat2 = newRational.newInstance(3, 2);

        divideBy.invokeOn(rat1, rat2);

        assertEquals(44, numerator.getOf(rat1), "'numerator' ist falsch.");
        assertEquals(21, denominator.getOf(rat1), "'denominator' ist falsch.");
    }

    @PublicTest
    public void testRationalAdd() {
        var rat1 = newRational.newInstance(22, 7);
        var rat2 = newRational.newInstance(2, 3);

        add.invokeOn(rat1, rat2);

        assertEquals(80, numerator.getOf(rat1), "'numerator' ist falsch.");
        assertEquals(21, denominator.getOf(rat1), "'denominator' ist falsch.");
    }

    @PublicTest
    public void testRationalSubtract() {
        var rat1 = newRational.newInstance(22, 7);
        var rat2 = newRational.newInstance(2, 3);

        subtract.invokeOn(rat1, rat2);

        assertEquals(52, numerator.getOf(rat1), "'numerator' ist falsch.");
        assertEquals(21, denominator.getOf(rat1), "'denominator' ist falsch.");
    }

    @PublicTest
    public void testRationalToDouble() {
        var rat = newRational.newInstance(22, 7);

        assertEquals(3.142857143, (double) toDouble.invokeOn(rat), 1e-8, "Falsches Ergebnis für Input 22 / 7.");
    }

    @PublicTest
    public void testRationalToString() {
        var rat = newRational.newInstance(22, 7);

        assertEquals("22 / 7", toString.invokeOn(rat), "Falsches Ergebnis für Input 22 / 7.");
    }

}
