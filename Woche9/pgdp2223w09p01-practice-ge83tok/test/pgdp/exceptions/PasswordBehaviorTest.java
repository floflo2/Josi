package pgdp.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import de.tum.in.test.api.*;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.jupiter.PublicTest;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.ExceptionMethod;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@MirrorOutput
@StrictTimeout(2)
@BlacklistPath(value = "**Test.{java,class}", type = PathType.GLOB)
@WhitelistPath("")
@AddTrustedPackage("net.bytebuddy**")
public class PasswordBehaviorTest {
    @PublicTest
    public void testArgumentCheck1() {
        try {
            new NotEnoughException(3, 5);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("NotEnoughException macht keine Eingabeprüfung!");
    }

    @PublicTest
    public void testArgumentCheck2() {
        try {
            new NotEnoughLetterException(3, 5);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("NotEnoughException macht keine Eingabeprüfung!");
    }

    @PublicTest
    public void testArgumentCheck3() {
        try {
            new NotLongEnoughException(3, 5);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("NotEnoughException macht keine Eingabeprüfung!");
    }

    @PublicTest
    public void testToString1() {
        assertTrue(
                new NotEnoughLowerCaseException(5, 3).toString().contains("5"), "Ausgabe enthält should nicht!");
    }

    @PublicTest
    public void testToString2() {
        assertTrue(
                new NotEnoughUpperCaseException(5, 3).toString().contains("3"), "Ausgabe enthält is nicht!");
    }

    @PublicTest
    public void testToString3() {
        assertTrue(
                new IllegalCharException('x').toString().contains("x"), "Ausgabe enthält used nicht!");
    }

    @PublicTest
    public void testPasswordCheck1() {
        Password p = new Password(2, 2, 2, new char[]{'x'});
        try {
            p.checkFormat("abAB12'*");
        } catch (Exception e) {
            fail("Korrektes Passwort abgelehnt mit " + e);
        }
    }

    @PublicTest
    public void testPasswordCheck2() throws Exception {
        Password p = new Password(2, 2, 2, new char[]{'x'});
        try {
            p.checkFormat("abA12'*");
        } catch (NotEnoughUpperCaseException e) {
            return;
        } catch (IllegalCharException | NotEnoughException e) {
            fail("Unerwartete Exception");
        }
        fail("UpperCase nicht geworfen");
    }

    @PublicTest
    public void testPasswordCheck3() throws Exception {
        Password p = new Password(2, 2, 2, new char[]{'x'});
        try {
            p.checkFormat("aAB12'*");
        } catch (NotEnoughLowerCaseException e) {
            return;
        } catch (IllegalCharException | NotEnoughException e) {
            fail("Unerwartete Exception");
        }
        fail("LowerCase nicht geworfen");
    }

    @PublicTest
    public void testPasswordCheck6() throws Exception {
        Password p = new Password(2, 2, 2, new char[]{'x'});
        try {
            p.checkFormat("abAB12*'x");
        } catch (IllegalCharException e) {
            return;
        } catch (NotEnoughException e) {
            fail("Unerwartete Exception");
        }
        fail("IllegalChar nicht geworfen");
    }

    @PublicTest
    public void testPasswordCheck7() throws Exception {
        Password p = new Password(2, 2, 2, new char[]{'x'});
        try {
            p.checkFormat("a");
        } catch (NotLongEnoughException e) {
            return;
        } catch (IllegalCharException | NotEnoughException e) {
            fail("Unerwartete Exception");
        }
        fail("LongEnough nicht geworfen");
    }

    @Public
    @ParameterizedTest
    @ValueSource(strings = {"aaa", "AAA", "aA"})
    public void testLoggingNotEnough(String check, IOTester tester) throws Exception {
        Password p = new Password(1, 1, 3, new char[]{'x'});
        try {
            p.checkFormatWithLogging(check);
        } catch (NotEnoughException e) {
            assertFalse(tester.out().getLines().isEmpty(), "no output on stderr");
            return;
        } catch (IllegalCharException e) {
            fail("Unerwartete Exception");
        }
        fail("keine Exception geworfen");
    }

    @PublicTest
    public void testLoggingInvalid(IOTester tester) throws Exception {
        Password p = new Password(1, 1, 1, new char[]{'x'});
        try {
            p.checkFormatWithLogging("aAx");
        } catch (IllegalCharException e) {
            assertFalse(tester.out().getLines().isEmpty(), "no output on stderr");
            return;
        } catch (NotEnoughException e) {
            fail("Unerwartete Exception");
        }
        fail("keine Exception geworfen");
    }

    @PublicTest
    public void testLoggingOtherExceptions(IOTester tester) throws Exception {
        Password p =
                new ByteBuddy().subclass(Password.class).define(Password.class.getMethod("checkFormat", String.class))
                        .intercept(new ExceptionMethod(new ExceptionMethod.ConstructionDelegate.ForDefaultConstructor(
                                TypeDescription.ForLoadedType.of(IllegalArgumentException.class)))).make()
                        .load(PasswordBehaviorTest.class.getClassLoader()).getLoaded()
                        .getConstructor(int.class, int.class, int.class, char[].class)
                        .newInstance(1, 1, 1, new char[0]);
        try {
            p.checkFormatWithLogging("aAx");
        } catch (IllegalArgumentException e) {
            assertTrue(tester.out().getLines().isEmpty(), "Other exceptions logged");
            return;
        } catch (Exception e) {
            fail("Unerwartete Exception");
        }
        fail("keine Exception geworfen");
    }
}
