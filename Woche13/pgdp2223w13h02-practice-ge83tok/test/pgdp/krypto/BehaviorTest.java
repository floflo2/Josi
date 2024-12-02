package pgdp.krypto;

import de.tum.in.test.api.TestUtils;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.fail;

@TestClassAnnotation
public class BehaviorTest {

    private static final List<Long> primNumbers = LongStream.range(3, 100000).filter(BehaviorTest::isPrime).boxed().toList();
    private static ByteCodeInterpreter eea = null;
    private static ByteCodeInterpreter cryption = null;

    private static boolean isPrime(long p) {
        return p == 2 || IntStream.range(2, (int) p).noneMatch(i -> p % i == 0);
    }

    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private static long lcm(long a, long b) {
        return a * b / gcd(a, b);
    }

    private static List<Long> calcCryption(long[] xs) {

        int index = 2;
        long y = xs[0];
        long z = xs[1];
        List<Long> result = new java.util.ArrayList<>();

        while (xs[index] != 0) {
            long x = xs[index++];

            long res = 1;
            long tempY = y;
            while (0 < tempY) {
                res *= x;
                tempY -= 1;
            }
            result.add(Math.floorMod(res, z));
        }

        return result;
    }

    private static List<String> readFile(String file) {
        try {
            // TODO für Artemis: "./assignment/src/pgdp/krypto/"
            // TODO für Assigment: "../pgdp2223w13h02-ab123cd/src/pgdp/krypto/" und ab123cd für eigene Kennung.
            // Sonst Projekt wie immer einbinden.
            return Files.readAllLines(Path.of("./assignment/src/pgdp/krypto/" + file));
        } catch (IOException e) {
            TestUtils.privilegedFail("Die Datei " + file + " konnte nicht gelesen werden. " +
                    "Stell sicher dass sie im Ordner src/pgdp/krypto liegt.");
        }
        return List.of();
    }

    @Order(1)
    @PublicTest
    @DisplayName("ob die Datei eea.jvm richtig geparsed werden kann:")
    void parseEea() {
        List<String> code = readFile("eea.jvm");
        try {
            eea = new ByteCodeInterpreter(code, List.of());
        } catch (Exception e) {
            TestUtils.privilegedFail("Die Datei eea.jvm konnte nicht geparsed werden. " +
                    "Stell sicher, dass sie syntaktisch korrekt ist.");
        }
    }

    @Order(2)
    @HiddenTest
    @DisplayName("ob das Programm in eea.jvm richtige d's für simplen Input berechnet:")
    void testEeaSimple() {
        Random random = new Random();

        testEea(23, 11, 3L, random);
    }

    @Order(3)
    @HiddenTest
    @DisplayName("ob das Programm in eea.jvm richtige d's für mehrere Inputs berechnet:")
    void testEeaMedium() {
        Random random = new Random();

        testEea(11, 31, 29L, random);
        testEea(5, 5, 3L, random);
        testEea(19, 19, 17L, random);
        testEea(7, 3, 5L, random);
        testEea(13, 3, 11L, random);
        testEea(13, 7, 11L, random);
    }

    @Order(4)
    @HiddenTest
    @DisplayName("ob das Programm in eea.jvm richtige d's für beliebigen Input berechnet:")
    void testEeaAll() {
        Random random = new Random(111);

        for (int i = 0; i < 2000; i++) {
            // generate two random prime numbers p and q, calculate n, phi and e.
            long p = primNumbers.get(random.nextInt(primNumbers.size()));
            long q = primNumbers.get(random.nextInt(primNumbers.size()));
            testEea(p, q, null, random);
        }
    }

    @Order(1)
    @PublicTest
    @DisplayName("ob die Datei cryption.jvm richtig geparsed werden kann:")
    void parseCryption() {
        List<String> code = readFile("cryption.jvm");
        try {
            cryption = new ByteCodeInterpreter(code, List.of("MOD"));
        } catch (Exception e) {
            TestUtils.privilegedFail("Die Datei cryption.jvm konnte nicht geparsed werden. " +
                    "Stell sicher, dass sie syntaktisch korrekt ist und kein MOD verwendet wird.");
        }
    }

    @Order(2)
    @HiddenTest
    @DisplayName("ob das Programm in cryption.jvm eine simple Nachricht richtig ver- und entschlüsselt:")
    void testCryptionSimple() {
        testCryption(new long[]{2}, 2, 10);
    }

    @Order(3)
    @HiddenTest
    @DisplayName("ob das Programm in cryption.jvm eine mehrere Nachrichten richtig ver- und entschlüsselt:")
    void testCryptionMedium() {
        testCryption(new long[]{4, 2, 20, 30}, 2, 10);
        testCryption(new long[]{11, 12, 13, 14}, 3, 20);
        testCryption(new long[]{11, 1000, 1}, 1, 20);
        testCryption(new long[]{11, 1241, 1, 0, 10}, 1, 1);
    }

    @Order(4)
    @HiddenTest
    @DisplayName("ob das Programm in cryption.jvm beliebige Nachrichten richtig ver- und entschlüsselt:")
    void testCryptionAll() {
        // every test gets his on random instance to make sure that the tests are independent
        Random random = new Random(111);

        for (int i = 0; i < 10000; i++) {
            // generate the values x to en/decrypt and y and N
            long[] xs = LongStream.generate(() -> random.nextInt(20)).limit(random.nextInt(20)).toArray();
            long y = random.nextInt(5) + 1;
            long N = random.nextInt(2147483640) + 1;

            testCryption(xs, y, N);
        }
    }

    private void testEea(long p, long q, Long eo, Random random) {
        if (eea == null) {
            parseEea();
        }
        // generate two random prime numbers p and q, calculate n, phi and e.
        long phi = lcm(p - 1, q - 1);
        long e;
        if (eo == null) {
            do {
                e = primNumbers.get(random.nextInt(primNumbers.size()));
            } while (e > phi || gcd(e, phi) != 1);
        } else {
            e = eo;
        }


        List<Long> ds = new ArrayList<>();
        try {
            eea.reset();
            ds = eea.run(e, phi);
        } catch (EmptyStackException ex) {
            fail("Es wurde versucht auf einen leeren Stack zuzugreifen bei Eingabe: a " + e + ", b: " + phi);
        } catch (NullPointerException ex) {
            fail("Das Programm hat keine Ausgabe ausgegeben bei Eingabe: a " + e + ", b: " + phi);
        } catch (Exception ex) {
            fail("Es wurde eine Exception geworfen bei Eingabe a: " + e + ", b: " + phi +
                    ", Nachricht:" + ex.getMessage());
        }

        if (ds.size() != 1) {
            fail("Das Programm hat nicht genau eine Ausgabe ausgegeben bei Eingabe a: "
                    + e + ", b: " + phi);
        }

        long d = ds.get(0);
        if (Math.floorMod(d * e, phi) != 1) {
            fail("Die Implementierung in eea.jvm ist nicht richtig. Für p: " + p + ", q: " + q
                    + ", e: " + e + ", phi: " + phi + " wurde: " + d + " berechnet, was die Gleichung "
                    + " d * e mod phi == 1 nicht erfüllt.");
        }
    }

    private void testCryption(long[] xs, long y, long N) {
        if (eea == null) {
            parseCryption();
        }

        long[] input = new long[xs.length + 3];
        System.arraycopy(xs, 0, input, 2, xs.length);

        input[0] = y;
        input[1] = N;
        input[input.length - 1] = 0;

        List<Long> exp = calcCryption(input);
        List<Long> act = new ArrayList<>();
        try {
            cryption.reset();
            act = cryption.run(Arrays.stream(input).boxed().toArray(Long[]::new));
        } catch (EmptyStackException e) {
            fail("Tried to access empty stack while running cryption.jvm with input: " + Arrays.toString(input));
        } catch (Exception e) {
            fail("Exception thrown while running cryption.jvm with input: " + Arrays.toString(input) +
                    ", with message: " + e.getMessage());
        }

        if (!Objects.equals(exp, act)) {
            fail("Die Implementation in cryption.jvm ist nicht richtig. Für input: " + Arrays.toString(input) +
                    " war das Ergebnis: " + act + " aber sollte eigentlich: " + exp + " sein.");
        }
    }
}
