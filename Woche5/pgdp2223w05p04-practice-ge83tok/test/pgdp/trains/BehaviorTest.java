package pgdp.trains;

import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicConstructor;
import de.tum.in.test.api.dynamic.DynamicField;
import de.tum.in.test.api.dynamic.DynamicMethod;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
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

    private static final String PACKAGE = "pgdp.trains.";

    // Classes
    private static final DynamicClass<?> Train = DynamicClass.toDynamic(PACKAGE + "Train");
    private static final DynamicClass<?> Carriage = DynamicClass.toDynamic(PACKAGE + "Carriage");
    private static final DynamicClass<?> Locomotive = DynamicClass.toDynamic(PACKAGE + "Locomotive");
    private static final DynamicClass<?> Person = DynamicClass.toDynamic(PACKAGE + "Person");

    // Constructors
    private static final DynamicConstructor<?> newTrain = Train.constructor(Locomotive.toClass());
    private static final DynamicConstructor<?> newCarriage = Carriage.constructor(boolean.class, int.class);
    private static final DynamicConstructor<?> newLocomotive = Locomotive.constructor(int.class, Person.toClass());
    private static final DynamicConstructor<?> newPerson = Person.constructor(int.class, String.class);

    // Fields
    private static final DynamicField<?> trLocomotive = Train.field(Locomotive.toClass(), "locomotive");
    private static final DynamicField<Object[]> trCarriages = Train.field(Object[].class, "carriages");
    private static final DynamicField<?> carIsRestaurantCarriage = Carriage.field(boolean.class, "isRestaurantCarriage");
    private static final DynamicField<?> carEmptyWeight = Carriage.field(int.class, "emptyWeight");
    private static final DynamicField<Object[]> carPassengers = Carriage.field(Object[].class, "passengers");
    private static final DynamicField<?> locoEmptyWeight = Locomotive.field(int.class, "emptyWeight");
    private static final DynamicField<?> locoDriver = Locomotive.field(Person.toClass(), "driver");
    private static final DynamicField<?> perWeight = Person.field(int.class, "weight");
    private static final DynamicField<?> perName = Person.field(String.class, "name");

    // Train Methods
    private static final DynamicMethod<?> trGetLocomotive = Train.method(Locomotive.toClass(), "getLocomotive");
    private static final DynamicMethod<Object[]> trGetCarriages = Train.method(Object[].class, "getCarriages");
    private static final DynamicMethod<?> trSetLocomotive = Train.method(void.class, "setLocomotive", Locomotive.toClass());
    private static final DynamicMethod<?> trAppendCarriage = Train.method(void.class, "appendCarriage", Carriage.toClass());
    private static final DynamicMethod<?> trGetNumberOfPassengers = Train.method(int.class, "getNumberOfPassengers");
    private static final DynamicMethod<?> trGetTotalWeight = Train.method(int.class, "getTotalWeight");

    // Carriage Methods
    private static final DynamicMethod<?> carIsRestaurantCarriageMethod = Carriage.method(boolean.class, "isRestaurantCarriage");
    private static final DynamicMethod<?> carGetEmptyWeight = Carriage.method(int.class, "getEmptyWeight");
    private static final DynamicMethod<Object[]> carGetPassengers = Carriage.method(Object[].class, "getPassengers");
    private static final DynamicMethod<?> carBoardPeople = Carriage.method(void.class, "boardPeople", (Array.newInstance(Person.toClass(), 0)).getClass());

    // Locomotive Methods
    private static final DynamicMethod<?> locoGetEmptyWeight = Locomotive.method(int.class, "getEmptyWeight");
    private static final DynamicMethod<?> locoGetDriver = Locomotive.method(Person.toClass(), "getDriver");
    private static final DynamicMethod<?> locoSetDriver = Locomotive.method(void.class, "setDriver", Person.toClass());

    // Person Methods
    private static final DynamicMethod<?> perGetWeight = Person.method(int.class, "getWeight");
    private static final DynamicMethod<?> perGetName = Person.method(String.class, "getName");

    // ================ Test Train ================ //

    @Public
    @Test
    public void testTrainConstructor() {
        var loco = newLocomotive.newInstance(0, null);
        var train = newTrain.newInstance(loco);

        assertEquals(loco, trLocomotive.getOf(train), "'locomotive' falsch gesetzt. Ist nicht das dem Konstruktor übergebene Objekt.");
        assertNotNull(trCarriages.getOf(train), "'carriages' sollte mit leerem Array initialisiert werden.");
        assertEquals(0, trCarriages.getOf(train).length, "'carriages' sollte mit leerem Array initialisiert werden.");
    }

    @Public
    @Test
    public void testTrainGetLocomotive() {
        var loco = newLocomotive.newInstance(0, null);
        var train = newTrain.newInstance(loco);

        assertEquals(loco, trGetLocomotive.invokeOn(train), "Gibt nicht die gleiche Lokomotive zurück, wie sie in die Variable 'locomotive' gesetzt wurde.");
    }

    @Public
    @Test
    public void testTrainGetCarriages() throws IllegalAccessException {
        var loco = newLocomotive.newInstance(0, null);
        var train = newTrain.newInstance(loco);

        var car1 = newCarriage.newInstance(true, 50);
        var car2 = newCarriage.newInstance(true, 70);
        var car3 = newCarriage.newInstance(false, 35);

        var cars = Array.newInstance(Carriage.toClass(), 3);
        Array.set(cars, 0, car1);
        Array.set(cars, 1, car2);
        Array.set(cars, 2, car3);

        trCarriages.toField().set(train, cars);

        assertArrayEquals((Object[]) cars, trGetCarriages.invokeOn(train), "Gibt nicht das gleiche Carriage-Array zurück, wie es in der Variable 'carriages' steht.");
    }

    @Public
    @Test
    public void testTrainSetLocomotive() {
        var loco = newLocomotive.newInstance(0, null);
        var train = newTrain.newInstance(loco);

        var newLoco = newLocomotive.newInstance(50, null);
        trSetLocomotive.invokeOn(train, newLoco);

        assertEquals(newLoco, trLocomotive.getOf(train), "Setzen der Lokomotive funktioniert nicht korrekt!");
    }

    @Public
    @Test
    public void testTrainAppendCarriage() throws IllegalAccessException {
        var loco = newLocomotive.newInstance(0, null);
        var train = newTrain.newInstance(loco);

        var car1 = newCarriage.newInstance(true, 50);
        var car2 = newCarriage.newInstance(true, 70);
        var car3 = newCarriage.newInstance(false, 35);

        var cars = Array.newInstance(Carriage.toClass(), 3);
        Array.set(cars, 0, car1);
        Array.set(cars, 1, car2);
        Array.set(cars, 2, car3);

        trCarriages.toField().set(train, cars);

        var car4 = newCarriage.newInstance(true, 103);
        var car5 = newCarriage.newInstance(true, 99);

        trAppendCarriage.invokeOn(train, car4);
        trAppendCarriage.invokeOn(train, car5);

        var expCars = Array.newInstance(Carriage.toClass(), 5);
        Array.set(expCars, 0, car1);
        Array.set(expCars, 1, car2);
        Array.set(expCars, 2, car3);
        Array.set(expCars, 3, car4);
        Array.set(expCars, 4, car5);

        assertArrayEquals((Object[]) expCars, trGetCarriages.invokeOn(train));
    }

    @Public
    @Test
    public void testTrainGetNumberOfPassengers() throws IllegalAccessException {
        var loco = newLocomotive.newInstance(0, null);
        var train = newTrain.newInstance(loco);

        var car1 = newCarriage.newInstance(true, 50);
        var car2 = newCarriage.newInstance(true, 70);
        var car3 = newCarriage.newInstance(false, 35);

        var p11 = newPerson.newInstance(10, "Hans");
        var p12 = newPerson.newInstance(8, "Peter");
        var p13 = newPerson.newInstance(12, "Günther");
        var p14 = newPerson.newInstance(10, "Rudolf");
        var p31 = newPerson.newInstance(5, "Hermann");
        var p32 = newPerson.newInstance(5, "Damfrau");

        var cars = Array.newInstance(Carriage.toClass(), 3);
        Array.set(cars, 0, car1);
        Array.set(cars, 1, car2);
        Array.set(cars, 2, car3);

        var p1 = Array.newInstance(Person.toClass(), 4);
        Array.set(p1, 0, p11);
        Array.set(p1, 1, p12);
        Array.set(p1, 2, p13);
        Array.set(p1, 3, p14);

        var p2 = Array.newInstance(Person.toClass(), 0);

        var p3 = Array.newInstance(Person.toClass(), 2);
        Array.set(p3, 0, p31);
        Array.set(p3, 1, p32);

        carPassengers.toField().set(car1, p1);
        carPassengers.toField().set(car2, p2);
        carPassengers.toField().set(car3, p3);

        trCarriages.toField().set(train, cars);

        assertEquals(6, trGetNumberOfPassengers.invokeOn(train), "Falsche Gesamt-Passagierzahl bei drei Wagen mit 4, 0 und 2 Passagieren.");
    }

    @Public
    @Test
    public void testTrainGetTotalWeight() throws IllegalAccessException {
        var driver = newPerson.newInstance(15, "Sepp");
        var loco = newLocomotive.newInstance(100, driver);
        var train = newTrain.newInstance(loco);

        var car1 = newCarriage.newInstance(true, 50);
        var car2 = newCarriage.newInstance(true, 70);
        var car3 = newCarriage.newInstance(false, 35);

        var p11 = newPerson.newInstance(10, "Hans");
        var p12 = newPerson.newInstance(8, "Peter");
        var p13 = newPerson.newInstance(12, "Günther");
        var p14 = newPerson.newInstance(10, "Rudolf");
        var p31 = newPerson.newInstance(5, "Hermann");
        var p32 = newPerson.newInstance(5, "Damfrau");

        var cars = Array.newInstance(Carriage.toClass(), 3);
        Array.set(cars, 0, car1);
        Array.set(cars, 1, car2);
        Array.set(cars, 2, car3);

        var p1 = Array.newInstance(Person.toClass(), 4);
        Array.set(p1, 0, p11);
        Array.set(p1, 1, p12);
        Array.set(p1, 2, p13);
        Array.set(p1, 3, p14);

        var p2 = Array.newInstance(Person.toClass(), 0);

        var p3 = Array.newInstance(Person.toClass(), 2);
        Array.set(p3, 0, p31);
        Array.set(p3, 1, p32);

        carPassengers.toField().set(car1, p1);
        carPassengers.toField().set(car2, p2);
        carPassengers.toField().set(car3, p3);

        trCarriages.toField().set(train, cars);

        assertEquals(320, trGetTotalWeight.invokeOn(train), "Falsche Gesamt-Passagierzahl bei drei Wagen mit Gewicht 50, 70 und 35 und Passagieren mit Gesamtgewicht 50, sowie einer Lok mit Gewicht 100 und einem Fahrer mit Gewicht 15.");
    }

    // ================ Test Carriage ================ //

    @Public
    @Test
    public void testCarriageConstructor() {
        var carriage = newCarriage.newInstance(true, 50);

        assertEquals(true, carIsRestaurantCarriage.getOf(carriage), "Setzen des Flags 'isRestaurantCarriage' funktioniert nicht korrekt!");
        assertEquals(50, carEmptyWeight.getOf(carriage), "Setzen der 'emptyWeight' funktioniert nicht korrekt!");
        assertArrayEquals((Object[]) Array.newInstance(Person.toClass(), 0), carPassengers.getOf(carriage), "Setzen der 'passenger' auf ein leeres Array funktioniert nicht korrekt!");
    }

    @Public
    @Test
    public void testCarriageIsRestaurantCarriage() throws IllegalAccessException {
        var carriage = newCarriage.newInstance(true, 0);
        carIsRestaurantCarriage.toField().set(carriage, false);
        assertFalse((boolean) carIsRestaurantCarriageMethod.invokeOn(carriage), "Variable 'isRestaurantCarriage' wird von entsprechendem Getter nicht korrekt ausgelesen.");
    }

    @Public
    @Test
    public void testCarriageGetEmptyWeight() throws IllegalAccessException {
        var carriage = newCarriage.newInstance(true, 0);
        carEmptyWeight.toField().set(carriage, 314);
        assertEquals(314, carGetEmptyWeight.invokeOn(carriage), "Variable 'emptyWeight' wird von entsprechendem Getter nicht korrekt ausgelesen.");
    }

    @Public
    @Test
    public void testCarriageGetPassengers() throws IllegalAccessException {
        var carriage = newCarriage.newInstance(true, 0);

        var p1 = newPerson.newInstance(10, "Hans");
        var p2 = newPerson.newInstance(8, "Peter");
        var p3 = newPerson.newInstance(12, "Günther");
        var p4 = newPerson.newInstance(10, "Rudolf");

        var p = Array.newInstance(Person.toClass(), 4);
        Array.set(p, 0, p1);
        Array.set(p, 1, p2);
        Array.set(p, 2, p3);
        Array.set(p, 3, p4);

        carPassengers.toField().set(carriage, p);

        assertArrayEquals((Object[]) p, carGetPassengers.invokeOn(carriage), "Variable 'passengers' wird von entsprechendem Getter nicht korrekt ausgelesen.");
    }

    @Public
    @Test
    public void testCarriageBoardPeople() throws IllegalAccessException {
        var carriage = newCarriage.newInstance(true, 0);

        var p1 = newPerson.newInstance(10, "Hans");
        var p2 = newPerson.newInstance(8, "Peter");
        var p3 = newPerson.newInstance(12, "Günther");
        var p4 = newPerson.newInstance(10, "Rudolf");

        var p = Array.newInstance(Person.toClass(), 4);
        Array.set(p, 0, p1);
        Array.set(p, 1, p2);
        Array.set(p, 2, p3);
        Array.set(p, 3, p4);

        carPassengers.toField().set(carriage, p);

        var p5 = newPerson.newInstance(5, "Hermann");
        var p6 = newPerson.newInstance(5, "Damfrau");

        var newP = Array.newInstance(Person.toClass(), 2);
        Array.set(newP, 0, p5);
        Array.set(newP, 1, p6);

        carBoardPeople.invokeOn(carriage, newP);

        var allP = Array.newInstance(Person.toClass(), 6);
        Array.set(allP, 0, p1);
        Array.set(allP, 1, p2);
        Array.set(allP, 2, p3);
        Array.set(allP, 3, p4);
        Array.set(allP, 4, p5);
        Array.set(allP, 5, p6);

        assertArrayEquals((Object[]) allP, carPassengers.getOf(carriage), "Zwei Leute in einen Waggon zu boarden, in dem bereits vier sind, hat nicht korrekt funktioniert.");
    }

    // ================ Test Locomotive ================ //

    @Public
    @Test
    public void testLocomotiveConstructor() {
        var per = newPerson.newInstance(5, "Heinz-Linde");
        var loco = newLocomotive.newInstance(99, per);

        assertEquals(99, locoEmptyWeight.getOf(loco), "'emptyWeight' nicht korrekt gesetzt.");
        assertEquals(per, locoDriver.getOf(loco), "'driver' nicht korrekt gesetzt.");
    }

    @Public
    @Test
    public void testLocomotiveGetEmptyWeight() throws IllegalAccessException {
        var per = newPerson.newInstance(5, "Heinz-Linde");
        var loco = newLocomotive.newInstance(99, per);

        locoEmptyWeight.toField().set(loco, 111);
        assertEquals(111, locoGetEmptyWeight.invokeOn(loco), "'emptyWeight' wurde nicht korrekt ausgelesen.");
    }

    @Public
    @Test
    public void testLocomotiveGetDriver() throws IllegalAccessException {
        var per = newPerson.newInstance(5, "Heinz-Linde");
        var loco = newLocomotive.newInstance(99, null);

        locoDriver.toField().set(loco, per);
        assertEquals(per, locoGetDriver.invokeOn(loco), "'driver' wurde nicht korrekt ausgelesen.");
    }

    @Public
    @Test
    public void testLocomotiveSetDriver() {
        var per = newPerson.newInstance(5, "Heinz-Linde");
        var loco = newLocomotive.newInstance(99, null);

        locoSetDriver.invokeOn(loco, per);
        assertEquals(per, locoDriver.getOf(loco), "'driver' wurde nicht korrekt gesetzt!");
    }

    // ================ Test Person ================ //

    @Public
    @Test
    public void testPersonConstructor() {
        var person = newPerson.newInstance(10, "Karl");
        assertEquals(10, perWeight.getOf(person), "'weight' wurde nicht korrekt gesetzt.");
        assertEquals("Karl", perName.getOf(person), "'name' wurde nicht korrekt gesetzt.");
    }

    @Public
    @Test
    public void testPersonGetWeight() throws IllegalAccessException {
        var person = newPerson.newInstance(10, "Karl");
        perWeight.toField().set(person, 15);
        assertEquals(15, perGetWeight.invokeOn(person), "'weight' wurde nicht korrekt ausgelesen.");
    }

    @Public
    @Test
    public void testPersonGetName() throws IllegalAccessException {
        var person = newPerson.newInstance(10, "Karl");
        perName.toField().set(person, "Marie-Luise");
        assertEquals("Marie-Luise", perGetName.invokeOn(person), "'name' wurde nicht korrekt ausgelesen.");
    }
}
