package pgdp.datatypes.time;

import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicConstructor;
import de.tum.in.test.api.dynamic.DynamicField;
import de.tum.in.test.api.dynamic.DynamicMethod;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.jupiter.PublicTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static de.tum.in.test.api.util.ReflectionTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Stephan Krusche (krusche@in.tum.de)
 * @version 5.1 (11.06.2021)
 */
@Public
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath("target/test-classes") // prevent access to test-related classes and resources
class BehaviorTest {

    private static final String TIME_PACKAGE = "pgdp.datatypes.time.";

    private static final DynamicClass<?> TimeOfDay = DynamicClass.toDynamic(TIME_PACKAGE + "TimeOfDay");
    private static final DynamicClass<?> Date = DynamicClass.toDynamic(TIME_PACKAGE + "Date");
    private static final DynamicClass<?> Appointment = DynamicClass.toDynamic(TIME_PACKAGE + "Appointment");

    private static final DynamicConstructor<?> newTimeOfDay = TimeOfDay.constructor(int.class, int.class, int.class);
    private static final DynamicConstructor<?> newDate = Date.constructor(int.class, int.class, int.class);
    private static final DynamicConstructor<?> newAppointment = Appointment.constructor(Date.toClass(), TimeOfDay.toClass(), TimeOfDay.toClass(), String.class);

    private static final DynamicField<?> todHour = TimeOfDay.field(int.class, "hour");
    private static final DynamicField<?> todMinute = TimeOfDay.field(int.class, "minute");
    private static final DynamicField<?> todSecond = TimeOfDay.field(int.class, "second");
    private static final DynamicField<?> dateDay = Date.field(int.class, "day");
    private static final DynamicField<?> dateMonth = Date.field(int.class, "month");
    private static final DynamicField<?> dateYear = Date.field(int.class, "year");
    private static final DynamicField<?> appDate = Appointment.field(Date.toClass(), "date");
    private static final DynamicField<?> appStart = Appointment.field(TimeOfDay.toClass(), "start");
    private static final DynamicField<?> appEnd = Appointment.field(TimeOfDay.toClass(), "end");
    private static final DynamicField<?> appDescription = Appointment.field(String.class, "description");

    private static final DynamicMethod<?> todHoursUntil = TimeOfDay.method(double.class, "hoursUntil", TimeOfDay.toClass());
    private static final DynamicMethod<?> todToString = TimeOfDay.method(String.class, "toString");
    private static final DynamicMethod<?> todToStringTwelveHourClock = TimeOfDay.method(String.class, "toStringTwelveHourClock");
    private static final DynamicMethod<?> dateToString = Date.method(String.class, "toString");
    private static final DynamicMethod<?> dateStaticIsValidDate = Date.method(boolean.class, "isValidDate", int.class, int.class, int.class);
    private static final DynamicMethod<?> appToString = Appointment.method(String.class, "toString");

    /*  ================  Test TimeOfDay  ================  */

    @PublicTest
    public void testToDConstructor() {
        var tod = newTimeOfDay.newInstance(14, 47, 22);

        assertEquals(14, (int) todHour.getOf(tod), "Bei Eingabe-Uhrzeit 14:47:22 sollte der Konstruktor von 'TimeOfDay' ein Objekt mit 'hour' 14 erstellen.");
        assertEquals(47, (int) todMinute.getOf(tod), "Bei Eingabe-Uhrzeit 14:47:22 sollte der Konstruktor von 'TimeOfDay' ein Objekt mit 'minute' 47 erstellen.");
        assertEquals(22, (int) todSecond.getOf(tod), "Bei Eingabe-Uhrzeit 14:47:22 sollte der Konstruktor von 'TimeOfDay' ein Objekt mit 'second' 22 erstellen.");
    }

    @PublicTest
    public void testToDToString() throws IllegalAccessException {
        var tod = newTimeOfDay.newInstance(0, 0, 0);

        todHour.toField().set(tod, 14);
        todMinute.toField().set(tod, 47);
        todSecond.toField().set(tod, 21);

        assertEquals("14:47:21", todToString.invokeOn(tod), "Für die Uhrzeit 14:47:22 sollte von 'toString()' der String \"14:47:22\" zurückgegeben werden.");
    }

    @PublicTest
    public void testToDToStringTwelveHourClock() throws IllegalAccessException {
        var tod = newTimeOfDay.newInstance(0, 0, 0);

        todHour.toField().set(tod, 14);
        todMinute.toField().set(tod, 47);
        todSecond.toField().set(tod, 21);

        assertEquals("2:47:21 pm", todToStringTwelveHourClock.invokeOn(tod));

        tod = newTimeOfDay.newInstance(0, 0, 0);

        todHour.toField().set(tod, 2);
        todMinute.toField().set(tod, 47);
        todSecond.toField().set(tod, 21);

        assertEquals("2:47:21 am", todToStringTwelveHourClock.invokeOn(tod));

        tod = newTimeOfDay.newInstance(0, 0, 0);

        todHour.toField().set(tod, 12);
        todMinute.toField().set(tod, 47);
        todSecond.toField().set(tod, 21);

        assertEquals("12:47:21 pm", todToStringTwelveHourClock.invokeOn(tod));

        tod = newTimeOfDay.newInstance(0, 0, 0);

        todHour.toField().set(tod, 0);
        todMinute.toField().set(tod, 47);
        todSecond.toField().set(tod, 21);

        assertEquals("12:47:21 am", todToStringTwelveHourClock.invokeOn(tod));
    }

    /*  ================  Test Date  ================  */

    @PublicTest
    public void testDateConstructor(IOTester tester) {
        var date = newDate.newInstance(15, 11, 2022);

        assertEquals(15, (int) dateDay.getOf(date), "Konstruktor von 'Date' setzt für den 15.11.2022 den Tag nicht korrekt.");
        assertEquals(11, (int) dateMonth.getOf(date), "Konstruktor von 'Date' setzt für den 15.11.2022 den Monat nicht korrekt.");
        assertEquals(2022, (int) dateYear.getOf(date), "Konstruktor von 'Date' setzt für den 15.11.2022 das Jahr nicht korrekt.");

        tester.reset();
        date = newDate.newInstance(0, 0, 2022);

        List<String> consoleOutput = tester.getOutTester().getLinesAsString();
        if(consoleOutput.size() == 0) {
            fail("Für das Datum 0.0.2022 sollte der Konstruktor von 'Date' ein Konsolen-Output erzeugen, das vor der Nutzung des eben erzeugten Objekts warnt.");
        }
        assertTrue(consoleOutput.get(0).startsWith("Not a valid date. Do NOT use this object!"), "Falsches Konsolen-Output beim Erzeugen des 0.0.2022: <" + consoleOutput.get(0) + ">");
    }

    @PublicTest
    public void testDateToString() throws IllegalAccessException {
        var date = newDate.newInstance(1, 1, 1);

        dateDay.toField().set(date, 15);
        dateMonth.toField().set(date, 11);
        dateYear.toField().set(date, 2022);

        assertEquals("15.11.2022", dateToString.invokeOn(date));
    }

    @PublicTest
    public void testDateIsValidDate() {
        assertAll(
                () -> assertTrue((boolean) dateStaticIsValidDate.invokeStatic(15, 11, 2022), "Falsches Output für den 15.11.2022"),
                () -> assertTrue((boolean) dateStaticIsValidDate.invokeStatic(29, 2, 2020), "Falsches Output für den 29.2.2020"),
                () -> assertTrue((boolean) dateStaticIsValidDate.invokeStatic(29, 2, 2400), "Falsches Output für den 29.2.2400"),

                () -> assertFalse((boolean) dateStaticIsValidDate.invokeStatic(0, 11, 2022), "Falsches Output für den 0.11.2022"),
                () -> assertFalse((boolean) dateStaticIsValidDate.invokeStatic(31, 11, 2022), "Falsches Output für den 31.11.2022"),
                () -> assertFalse((boolean) dateStaticIsValidDate.invokeStatic(15, 0, 2022), "Falsches Output für den 15.0.2022"),

                () -> assertFalse((boolean) dateStaticIsValidDate.invokeStatic(15, 13, 2022), "Falsches Output für den 15.13.2022"),
                () -> assertFalse((boolean) dateStaticIsValidDate.invokeStatic(29, 2, 2022), "Falsches Output für den 29.2.2022"),
                () -> assertFalse((boolean) dateStaticIsValidDate.invokeStatic(29, 2, 2200), "Falsches Output für den 29.2.2200")
        );
    }

    /*  ================  Test Appointment  ================  */

    @PublicTest
    public void testAppConstructor() {
        var date = newDate.newInstance(15, 11, 2022);
        var start = newTimeOfDay.newInstance(16, 15, 0);
        var end = newTimeOfDay.newInstance(17, 45, 0);

        var app = newAppointment.newInstance(date, start, end, "Ein Termin! Ein Termin!");

        assertEquals(date, appDate.getOf(app), "Konstruktor von 'Appointment' setzt das Datum nicht korrekt.");
        assertEquals(start, appStart.getOf(app), "Konstruktor von 'Appointment' setzt die Start-Uhrzeit nicht korrekt.");
        assertEquals(end, appEnd.getOf(app), "Konstruktor von 'Appointment' setzt die End-Uhrzeit nicht korrekt.");
        assertEquals("Ein Termin! Ein Termin!", appDescription.getOf(app), "Konstruktor von 'Appointment' setzt die Termin-Beschreibung nicht korrekt.");
    }

    @PublicTest
    public void testAppToString() {
        var date = newDate.newInstance(15, 11, 2022);
        var start = newTimeOfDay.newInstance(16, 15, 20);
        var end = newTimeOfDay.newInstance(17, 45, 20);

        var app = newAppointment.newInstance(date, start, end, "Ein Termin! Ein Termin!");

        assertEquals("Ein Termin! Ein Termin!\n15.11.2022\n16:15:20\n1.5 h", appToString.invokeOn(app));
    }
}
