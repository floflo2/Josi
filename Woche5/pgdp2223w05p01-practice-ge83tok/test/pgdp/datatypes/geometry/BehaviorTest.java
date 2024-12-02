package pgdp.datatypes.geometry;

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

    private static final String GEOMETRY_PACKAGE = "pgdp.datatypes.geometry.";

    private static final DynamicClass<?> Point = DynamicClass.toDynamic(GEOMETRY_PACKAGE + "Point");
    private static final DynamicClass<?> Vector = DynamicClass.toDynamic(GEOMETRY_PACKAGE + "Vector");
    private static final DynamicClass<?> Circle = DynamicClass.toDynamic(GEOMETRY_PACKAGE + "Circle");
    private static final DynamicClass<?> Rectangle = DynamicClass.toDynamic(GEOMETRY_PACKAGE + "Rectangle");

    private static final DynamicConstructor<?> newPoint = Point.constructor(int.class, int.class);
    private static final DynamicConstructor<?> newVector = Vector.constructor(int.class, int.class);
    private static final DynamicConstructor<?> newUnitCircle = Circle.constructor();
    private static final DynamicConstructor<?> newCircle = Circle.constructor(Point.toClass(), int.class);
    private static final DynamicConstructor<?> newRectangle = Rectangle.constructor(Point.toClass(), int.class, int.class);

    private static final DynamicField<?> pointX = Point.field(int.class, "x");
    private static final DynamicField<?> pointY = Point.field(int.class, "y");
    private static final DynamicField<?> vectorX = Vector.field(int.class, "x");
    private static final DynamicField<?> vectorY = Vector.field(int.class, "y");
    private static final DynamicField<?> circleCenter = Circle.field(Point.toClass(), "center");
    private static final DynamicField<?> circleRadius = Circle.field(int.class, "radius");
    private static final DynamicField<?> rectBottomLeftCorner = Rectangle.field(Point.toClass(), "bottomLeftCorner");
    private static final DynamicField<?> rectWidth = Rectangle.field(int.class, "width");
    private static final DynamicField<?> rectHeight = Rectangle.field(int.class, "height");

    private static final DynamicMethod<?> pointGetDistanceToOrigin = Point.method(double.class, "getDistanceToOrigin");
    private static final DynamicMethod<?> pointGetVectorTo = Point.method(Vector.toClass(), "getVectorTo", Point.toClass());
    private static final DynamicMethod<?> vectorGetLength = Vector.method(double.class, "getLength");
    private static final DynamicMethod<?> circleGetCircumference = Circle.method(double.class, "getCircumference");
    private static final DynamicMethod<?> circleGetArea = Circle.method(double.class, "getArea");
    private static final DynamicMethod<?> rectGetCircumference = Rectangle.method(int.class, "getCircumference");
    private static final DynamicMethod<?> rectGetArea = Rectangle.method(int.class, "getArea");

    /*  ================  Test Point  ================  */

    @PublicTest
    public void testPointConstructor() {
        var point = newPoint.newInstance(4, -3);

        assertEquals(4, (int) pointX.getOf(point), "Konstruktor von 'Point' setzt für den Punkt (4, -3) die x-Koordinate nicht korrekt.");
        assertEquals(-3, (int) pointY.getOf(point), "Konstruktor von 'Point' setzt für den Punkt (4, -3) die y-Koordinate nicht korrekt.");
    }

    @PublicTest
    public void testPointDistanceToOrigin() {
        var point = newPoint.newInstance(4, -3);
        var dist = pointGetDistanceToOrigin.invokeOn(point);
        assertEquals(5.0, (double) dist, 1e-10, "Falsche Distanz zu Ursprung bei Punkt (4, -3) berechnet.");
    }

    @PublicTest
    public void testPointVectorTo() {
        var point = newPoint.newInstance(4, -3);
        var otherPoint = newPoint.newInstance(2, 5);
        var vec = pointGetVectorTo.invokeOn(point, otherPoint);

        assertEquals(-2, vectorX.getOf(vec), "x- Koordinate des berechneten Vektors von (4, -3) nach (2, 5) falsch");
        assertEquals(8, vectorY.getOf(vec), "y- Koordinate des berechneten Vektors von (4, -3) nach (2, 5) falsch");
    }

    /*  ================  Test Vector  ================  */

    @PublicTest
    public void testVectorConstructor() {
        var vec = newVector.newInstance(4, -3);

        assertEquals(4, (int) vectorX.getOf(vec), "Konstruktor von 'Vector' setzt für den Vektor (4, -3) die x-Koordinate nicht korrekt.");
        assertEquals(-3, (int) vectorY.getOf(vec), "Konstruktor von 'Vector' setzt für den Vektor (4, -3) die y-Koordinate nicht korrekt.");
    }

    @PublicTest
    public void testVectorLength() {
        var vec = newVector.newInstance(4, -3);
        var len = vectorGetLength.invokeOn(vec);
        assertEquals(5.0, (double) len, 1e-10, "Falsche Länge bei Vektor (4, -3) berechnet.");
    }

    /*  ================  Test Circle  ================  */

    @PublicTest
    public void testCircleConstructor() {
        var point = newPoint.newInstance(4, -3);
        var circle = newCircle.newInstance(point, 7);

        assertEquals(point, circleCenter.getOf(circle), "Konstruktor von 'Circle' setzt für den Kreis mit Mittelpunkt (4, -3) und Radius 7 den Mittelpunkt nicht korrekt.");
        assertEquals(7, circleRadius.getOf(circle), "Konstruktor von 'Circle' setzt für den Kreis mit Mittelpunkt (4, -3) und Radius 7 den Radius nicht korrekt.");
    }

    @PublicTest
    public void testCircleCircumference() {
        var point = newPoint.newInstance(4, -3);
        var circle = newCircle.newInstance(point, 7);

        assertEquals(43.9822971503, (double) circleGetCircumference.invokeOn(circle), 1e-8, "Falscher Umfang für Kreis mit Mittelpunkt (4, -3) und Radius 7");
    }

    @PublicTest
    public void testCircleArea() {
        var point = newPoint.newInstance(4, -3);
        var circle = newCircle.newInstance(point, 7);

        assertEquals(153.93804002589985, (double) circleGetArea.invokeOn(circle), 1e-8, "Falsche Fläche für Kreis mit Mittelpunkt (4, -3) und Radius 7");
    }

    /*  ================  Test Rectangle  ================  */

    @PublicTest
    public void testRectangleConstructor() {
        var point = newPoint.newInstance(4, -3);
        var rect = newRectangle.newInstance(point, 5, 3);

        assertEquals(point, rectBottomLeftCorner.getOf(rect), "Konstruktor von 'Rectangle' setzt für das Rechteck mit Ecke (4, -3), Breite 5 und Höhe 3 den Eckpunkt nicht korrekt.");
        assertEquals(5, rectWidth.getOf(rect), "Konstruktor von 'Rectangle' setzt für das Rechteck mit Ecke (4, -3), Breite 5 und Höhe 3 die Breite nicht korrekt.");
        assertEquals(3, rectHeight.getOf(rect), "Konstruktor von 'Rectangle' setzt für das Rechteck mit Ecke (4, -3), Breite 5 und Höhe 3 die Höhe nicht korrekt.");
    }

    @PublicTest
    public void testRectangleCircumference() {
        var point = newPoint.newInstance(4, -3);
        var rect = newRectangle.newInstance(point, 5, 3);

        assertEquals(16, rectGetCircumference.invokeOn(rect), "Falscher Umfang für Rechteck mit Ecke (4, -3), Breite 5 und Höhe 3");
    }

    @PublicTest
    public void testRectangleArea() {
        var point = newPoint.newInstance(4, -3);
        var rect = newRectangle.newInstance(point, 5, 3);

        assertEquals(15, rectGetArea.invokeOn(rect), "Falsche Fläche für Rechteck mit Ecke (4, -3), Breite 5 und Höhe 3");
    }

}
