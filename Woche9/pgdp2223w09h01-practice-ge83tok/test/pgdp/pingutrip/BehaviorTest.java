package pgdp.pingutrip;

import de.tum.in.test.api.TestUtils;
import de.tum.in.test.api.internal.PrivilegedException;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pgdp.pingutrip.BehaviorOracle.*;
import static pgdp.pingutrip.PinguTrip.*;

@TestClassAnnotation
public class BehaviorTest {

    private static boolean noForbiddenConstructs = false;

    static Random random = new Random(420);
    WayPoint someWayPoint = new WayPoint(1.0, 1.0);
    OneWay someOneWay = new OneWay(someWayPoint, someWayPoint);

    static WayPoint randomWayPoint() {
        return new WayPoint(random.nextDouble(), random.nextDouble());
    }

    static WayPoint randomBigWayPoint() {
        return new WayPoint(random.nextDouble() * 10 + 5, random.nextDouble() * 10 + 5);
    }

    static OneWay randomOneWay() {
        return new OneWay(randomWayPoint(), randomWayPoint());
    }

    static OneWay randomBigOneWay() {
        return new OneWay(randomBigWayPoint(), randomBigWayPoint());
    }

    static Stream<WayPoint> randomWayPoints() {
        return Stream.generate(BehaviorTest::randomWayPoint).limit(100);
    }

    static Stream<OneWay> randomOneWays() {
        return Stream.generate(BehaviorTest::randomOneWay).limit(100);
    }

    static Stream<WayPoint> randomWayPointsInfinit() {
        return Stream.generate(BehaviorTest::randomWayPoint);
    }

    static Stream<OneWay> randomOneWaysInfinit() {
        return Stream.generate(BehaviorTest::randomOneWay);
    }

    static double randomDouble() {
        return random.nextDouble() * (random.nextInt() % 1000) * (random.nextBoolean() ? 1 : -1);
    }

    @BeforeAll
    static void resetFiles() {
        Path pathsPath = Path.of("paths");
        Path pathsVaultPath = Path.of("paths_vault");

        // Delete all files in connections folder.
        try (Stream<Path> paths = Files.walk(pathsPath)) {
            for (Path path : paths.skip(1).toList()) {
                Files.delete(path);
            }
        } catch (IOException e) {
            TestUtils.privilegedFail("Something went wrong while cleaning up the test environment, " +
                    "please contact an instructor. Message: " + e);
        }

        // Transfer all files from connections to connections_vault folder.
        try (Stream<Path> vault = Files.walk(pathsVaultPath)) {
            for (Path path : vault.skip(1).toList()) {
                Files.copy(path, pathsPath.resolve(pathsVaultPath.relativize(path)));
            }
        } catch (IOException e) {
            TestUtils.privilegedFail("Something went wrong while cleaning up the test environment, " +
                    "please contact an instructor." + e.getMessage());
        }
    }

    static <T> void assertStreamEquals(Stream<T> expected, Stream<T> actual, String message) {
        Iterator<T> expIter = expected.iterator();
        Iterator<T> actIter = actual.iterator();
        int index = 0;
        while (expIter.hasNext() && actIter.hasNext()) {
            T exp = expIter.next();
            T act = actIter.next();
            if (!exp.equals(act)) {
                TestUtils.privilegedFail(message + " Erwartet wurde: <" + exp +
                        "> aber war <" + act + "> an Index " + index + ".");
            }
            index++;
        }

        if (expIter.hasNext()) {
            TestUtils.privilegedFail(message + " Es gibt weniger Einträge als erwartet.");
        }
        if (actIter.hasNext()) {
            TestUtils.privilegedFail(message + " Es gibt mehr Einträge als erwartet.");
        }
    }

    static void privilegedAssertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            TestUtils.privilegedFail(message + " Erwartet wurde: <" + expected + "> aber war <" + actual + ">.");
        }
    }

    static void privilegedAssertEqualsDouble(double expected, double actual, String message) {
        if (Math.abs(expected - actual) > 0.00001 || Double.isNaN(actual) || Double.isInfinite(actual)) {
            TestUtils.privilegedFail(message + " Erwartet wurde: <" + expected + "> aber war <" + actual + ">.");
        }
    }

    static void privilegedAssertTrue(boolean expected, String message) {
        if (!expected) {
            TestUtils.privilegedFail(message + " Erwartet <true> aber war <false>.");
        }
    }

    static void privilegedAssertFalse(boolean expected, String message) {
        if (expected) {
            TestUtils.privilegedFail(message + " Erwartet <false> aber war <true>.");
        }
    }

    @Order(2)
    @PublicTest
    @DisplayName("Tests public readWayPoints - IO")
    void readWayPointsIOTestPublic() {
        if (!noForbiddenConstructs) {
            TestUtils.privilegedFail("Es wurden nicht erlaubte Konstrukte verwendet.");
        }

        try {
            readWayPointsIOTest();
        } catch (PrivilegedException e) {
            TestUtils.privilegedFail("Mindestens einer der Tests ist fehlgeschlagen.");
        } catch (Exception e) {
            TestUtils.privilegedFail("Bei der Ausführung des Tests wurde eine Exception geworfen.");
        }
    }

    @HiddenTest
    @DisplayName("Tests hidden readWayPoints - IO")
    void readWayPointsIOTest() {

        // 1. Simpler Fall funktioniert.
        assertStreamEquals(Stream.of(
                        new WayPoint(6.0, 1.0),
                        new WayPoint(2.0, 4.0)),
                readWayPoints("paths/simple_path.txt"),
                "Es kann keine einfach Datei eingelesen werden.");

        // 2. Aber bei nicht existierenden Files wird ein leerer Stream zurückgegeben.
        assertStreamEquals(Stream.empty(), readWayPoints("paths/not_exists"),
                "Es wird bei nicht existenten Dateien kein leerer Stream zurück gegeben.");
    }

    @Order(3)
    @PublicTest
    @DisplayName("Tests public readWayPoints - Streams")
    void readWayPointsStreamsTestPublic() {
        if (!noForbiddenConstructs) {
            TestUtils.privilegedFail("Es wurden nicht erlaubte Konstrukte verwendet.");
        }

        try {
            readWayPointsStreamsTest();
        } catch (PrivilegedException e) {
            TestUtils.privilegedFail("Mindestens einer der Tests ist fehlgeschlagen.");
        } catch (Exception e) {
            TestUtils.privilegedFail("Bei der Ausführung des Tests wurde eine Exception geworfen.");
        }
    }

    @HiddenTest
    @DisplayName("Tests hidden readWayPoints - Streams")
    void readWayPointsStreamsTest() throws IOException {
        // 1. File simple.
        assertStreamEquals(Stream.of(
                        new WayPoint(6.0, 1.0),
                        new WayPoint(2.0, 4.0)),
                readWayPoints("paths/simple_path.txt"),
                "Die Datei 'paths/simple_path.txt' wird nicht richtig eingelesen.");

        // 2. File mit allem drum und dran.
        assertStreamEquals(Stream.of(
                        new WayPoint(1.2, 0.1),
                        new WayPoint(-10000.1111, -10.9191),
                        new WayPoint(-0.00000001, 4.23232),
                        new WayPoint(3232.32, 71.1)),
                readWayPoints("paths/complex_path.txt"),
                "Die Datei 'paths/complex_path.txt' wird nicht richtig eingelesen.");

        // 3. Leeres File.
        assertStreamEquals(Stream.empty(),
                readWayPoints("paths/empty.txt"),
                "Eine leere Datei wird nicht richtig eingelesen.");

        // 4. Nur Kommentare
        assertStreamEquals(Stream.empty(),
                readWayPoints("paths/only_comments.txt"),
                "Eine Datei mit nur Kommentaren wird nicht richtig eingelesen.");

        // 5. Direkt Schlussstrich.
        assertStreamEquals(Stream.empty(), readWayPoints("paths/instant_stop.txt"),
                "Eine Datei welche direkt einen Schlussstrich zieht wird nicht richtig eingelesen.");

        // 6. Random
        String coordinates = Stream.generate(() -> (random.nextDouble() < 0.2 ?
                "// comment " + random.nextInt() + " int" :
                randomDouble() + ";" + randomDouble())
                + (random.nextDouble() < 0.005 ? System.lineSeparator() + "---" : "")
        ).limit(1000).collect(Collectors.joining(System.lineSeparator()));
        Files.writeString(Path.of("paths/random.txt"), coordinates);

        assertStreamEquals(
                readWayPointsOracle("paths/random.txt"),
                readWayPoints("paths/random.txt"),
                "Eine Datei mit zufälligen Zeilen wird nicht richtig eingelesen."
        );
    }

    @Order(4)
    @PublicTest
    @DisplayName("Tests public transformToWays")
    void transformToWaysTestPublic() {
        if (!noForbiddenConstructs) {
            TestUtils.privilegedFail("Es wurden nicht erlaubte Konstrukte verwendet.");
        }

        try {
            transformToWaysTest();
        } catch (PrivilegedException e) {
            TestUtils.privilegedFail("Mindestens einer der Tests ist fehlgeschlagen.");
        } catch (Exception e) {
            TestUtils.privilegedFail("Bei der Ausführung des Tests wurde eine Exception geworfen.");
        }
    }

    @HiddenTest
    @DisplayName("Tests hidden transformToWays")
    void transformToWaysTest() {
        // 1. Nur zwei Wegpunkte.
        WayPoint from = new WayPoint(1.3, -6.1);
        WayPoint to = new WayPoint(2.1, 1000.2);

        List<WayPoint> wayPoints = List.of(from, to);
        Stream<OneWay> resOne = transformToWays(wayPoints);

        assertStreamEquals(Stream.of(new OneWay(from, to)), resOne,
                "Beim umwandeln zweier WayPoints zu einem OneWay ist ein Fehler aufgetreten.");

        // 2. Simpler Test.
        wayPoints = randomWayPoints().limit(20).toList();
        List<OneWay> resTwo = transformToWays(wayPoints).toList();

        for (int i = 0; i < resTwo.size(); i++) {
            OneWay calculated = resTwo.get(i);
            privilegedAssertEquals(calculated.from(), wayPoints.get(i),
                    "Bei 20 simplen Wegpunkten werden am " + i + "-ten OneWay ein anderer from Wegpunkt erwartet.");
            privilegedAssertEquals(calculated.to(), wayPoints.get(i + 1),
                    "Bei 20 simplen Wegpunkten werden am " + i + "-ten OneWay ein anderer to Wegpunkt erwartet.");
        }
        privilegedAssertEquals(wayPoints.size() - 1, resTwo.size(),
                "The berechneten OneWays haben sollten einen kürzer als die Wegpunkte sein.");

        // 3. Random Wegpunkt.
        wayPoints = randomWayPoints().toList();
        assertStreamEquals(transformToWaysOracle(wayPoints), transformToWays(wayPoints),
                "Bei zufälligen Wegen ist ein Fehler aufgetreten.");
    }

    @Order(5)
    @PublicTest
    @DisplayName("Tests public pathLength")
    void pathLengthTestPublic() {
        if (!noForbiddenConstructs) {
            TestUtils.privilegedFail("Es wurden nicht erlaubte Konstrukte verwendet.");
        }

        try {
            pathLengthTest();
        } catch (PrivilegedException e) {
            TestUtils.privilegedFail("Mindestens einer der Tests ist fehlgeschlagen.");
        } catch (Exception e) {
            TestUtils.privilegedFail("Bei der Ausführung des Tests wurde eine Exception geworfen.");
        }
    }

    @HiddenTest
    @DisplayName("Tests hidden pathLength")
    void pathLengthTest() {
        // 1. Leer.
        privilegedAssertEqualsDouble(0.0, pathLength(Stream.empty()),
                "Die Länge eines Leeren Weges sollte 0.0 sein.");

        // 2. Nur ein Path.
        privilegedAssertEqualsDouble(0.0, pathLength(Stream.of(someOneWay)),
                "Die Länge von zwei mal dem selben Wegpunkt sollte 0.0 sein.");
        OneWay randomOneWay = randomOneWay();
        privilegedAssertEqualsDouble(randomOneWay.getLength(), pathLength(Stream.of(randomOneWay)),
                "Die Länge eines zufälligen einzelnen Weges wird falsch berechnet.");

        // 3. Random.
        List<OneWay> oneWays = randomOneWays().toList();
        privilegedAssertEqualsDouble(pathLengthOracle(oneWays.stream()), pathLength(oneWays.stream()),
                "Die Länge eines zufälligen Weges wird falsch berechnet.");
    }

    @Order(6)
    @PublicTest
    @DisplayName("Tests public kidFriendlyTrip")
    void kidFriendlyTripTestPublic() {
        if (!noForbiddenConstructs) {
            TestUtils.privilegedFail("Es wurden nicht erlaubte Konstrukte verwendet.");
        }

        try {
            kidFriendlyTripTest();
        } catch (PrivilegedException e) {
            TestUtils.privilegedFail("Mindestens einer der Tests ist fehlgeschlagen.");
        } catch (Exception e) {
            TestUtils.privilegedFail("Bei der Ausführung des Tests wurde eine Exception geworfen.");
        }
    }

    @HiddenTest
    @DisplayName("Tests hidden kidFriendlyTrip")
    void kidFriendlyTripTest() {
        // 1. Leer.
        privilegedAssertEquals(List.of(), kidFriendlyTrip(List.of()),
                "Die kinderfreundliche Weg aus einem leeren Weg wird falsch berechnet.");

        // 2. Nur ein Punkt.
        OneWay oneWay = randomOneWay();
        privilegedAssertEquals(List.of(oneWay), kidFriendlyTrip(List.of(oneWay)),
                "Die kinderfreundliche Weg aus nur einem Weg wird falsch berechnet.");

        // 3. Aufsteigend.
        List<OneWay> oneWays = randomOneWays().sorted(Comparator.comparingDouble(OneWay::getLength)).toList();
        privilegedAssertEquals(kidFriendlyTripOracle(oneWays), kidFriendlyTrip(oneWays),
                "Die kinderfreundliche Weg aus aufsteigend langen Wegen wird falsch berechnet.");

        // 4. Absteigend.
        oneWays = randomOneWays().sorted((o1, o2) -> -Double.compare(o1.getLength(), o2.getLength())).toList();
        privilegedAssertEquals(kidFriendlyTripOracle(oneWays), kidFriendlyTrip(oneWays),
                "Die kinderfreundliche Weg aus absteigend langen Wegen wird falsch berechnet.");

        // 5. Random.
        oneWays = randomOneWays().toList();
        privilegedAssertEquals(kidFriendlyTripOracle(oneWays), kidFriendlyTrip(oneWays),
                "Die kinderfreundliche Weg aus zufällig langen Wegen wird falsch berechnet.");

    }

    @Order(7)
    @PublicTest
    @DisplayName("Tests public furthestAwayFromHome")
    void furthestAwayFromHomeTestPublic() {
        if (!noForbiddenConstructs) {
            TestUtils.privilegedFail("Es wurden nicht erlaubte Konstrukte verwendet.");
        }

        try {
            furthestAwayFromHomeTest();
        } catch (PrivilegedException e) {
            TestUtils.privilegedFail("Mindestens einer der Tests ist fehlgeschlagen.");
        } catch (Exception e) {
            TestUtils.privilegedFail("Bei der Ausführung des Tests wurde eine Exception geworfen.");
        }
    }

    @HiddenTest
    @DisplayName("Tests hidden furthestAwayFromHome")
    void furthestAwayFromHomeTest() {
        // 1. Leer.
        privilegedAssertEquals(someWayPoint, furthestAwayFromHome(Stream.empty(), someWayPoint),
                "Bei einem leeren Weg wird nicht home wieder gegeben.");

        // 2. Nur ein Punkt.
        WayPoint furthest = randomWayPoint();
        privilegedAssertEquals(furthest, furthestAwayFromHome(Stream.of(furthest), someWayPoint),
                "Bei nur einem Punkt wird nicht dieser wieder gegeben.");

        // 3. Nur ein Punkt welche Home ist.
        privilegedAssertEquals(someWayPoint, furthestAwayFromHome(Stream.of(someWayPoint), someWayPoint),
                "Bei nur einem Punkt welche home ist wird nicht home wieder gegeben.");

        // 4. Drei Gleiche.
        WayPoint one = new WayPoint(5.1, -4.9);
        WayPoint two = new WayPoint(5.1, -4.9);
        WayPoint three = new WayPoint(5.1, -4.9);
        privilegedAssertEquals(three, furthestAwayFromHome(Stream.of(one, two, three), someWayPoint),
                "Bei drei gleich entfernten Wegpunkten wird nicht einer der drei wieder gegeben.");

        // 5. Random.
        List<WayPoint> path = randomWayPoints().toList();
        WayPoint home = randomWayPoint();
        privilegedAssertEquals(furthestAwayFromHomeOracle(path.stream(), home), furthestAwayFromHome(path.stream(), home),
                "Bei zufälligen Wegpunkten wird nicht der am weitesten entfernte gewählt.");
    }

    @Order(8)
    @PublicTest
    @DisplayName("Tests public onTheWay")
    void onTheWayPointsTestPublic() {
        if (!noForbiddenConstructs) {
            TestUtils.privilegedFail("Es wurden nicht erlaubte Konstrukte verwendet.");
        }

        try {
            onTheWayPointsTest();
        } catch (PrivilegedException e) {
            TestUtils.privilegedFail("Mindestens einer der Tests ist fehlgeschlagen.");
        } catch (Exception e) {
            TestUtils.privilegedFail("Bei der Ausführung des Tests wurde eine Exception geworfen.");
        }
    }

    @HiddenTest
    @DisplayName("Tests hidden onTheWay")
    void onTheWayPointsTest() {
        // 1. Leer.
        WayPoint wayPoint = randomWayPoint();
        privilegedAssertFalse(onTheWayOracle(Stream.empty(), wayPoint), "Auf einem leeren Weg liegt nichts.");

        // 2. Nur ein Weg.
        WayPoint on = new WayPoint(0.8, 0.8);
        OneWay way = new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.5, 1.5));
        privilegedAssertTrue(onTheWay(Stream.of(way), on),
                "<" + on + "> sollte auf <" + way + "> liegen.");
        privilegedAssertFalse(onTheWay(Stream.of(randomBigOneWay()), on),
                "<" + on + "> sollte nicht auf <" + way + "> liegen.");

        // 3. Weg wo er drauf ist.
        on = new WayPoint(0.8, 0.8);
        way = new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.5, 1.5));
        privilegedAssertTrue(onTheWay(Stream.of(randomOneWay(), way, randomOneWay()), on),
                "<" + on + "> sollte auf <...," + way + ", ...> liegen.");
        privilegedAssertTrue(onTheWay(Stream.of(randomOneWay(), randomOneWay(), way), on),
                "<" + on + "> sollte auf <..., ..., " + way + "> liegen.");

        // 4. Weg wo er nicht drauf ist.
        on = new WayPoint(0.85, 0.8);
        way = new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.5, 1.5));
        privilegedAssertFalse(onTheWay(Stream.of(randomBigOneWay(), way, randomBigOneWay()), on),
                "<" + on + "> sollte nicht auf <..., " + way + ", ...> liegen.");
        privilegedAssertFalse(onTheWay(Stream.of(randomBigOneWay(), randomBigOneWay(), way), on),
                "<" + on + "> sollte nicht auf <..., ..., " + way + "> liegen.");

        // 5. Wo Wegpunkt einer der Wegpunkte.
        List<WayPoint> wayPoints = randomWayPoints().toList();
        List<OneWay> oneWays = transformToWaysOracle(wayPoints).toList();
        wayPoints.forEach(wp -> privilegedAssertTrue(onTheWay(oneWays.stream(), wp),
                "Wegpunkte die die Eckpunkte eines Weges sind sollten auf dem Weg liegen."));

        // 6. Random.
        List<WayPoint> points = randomWayPoints().toList();
        List<OneWay> ways = randomOneWays().toList();
        points.forEach(wp -> privilegedAssertEquals(
                onTheWayOracle(ways.stream(), wp),
                onTheWay(ways.stream(), wp),
                "Bei zufälligen Wegpunkten gibt es Unterschiede."
        ));
    }

    @Order(9)
    @PublicTest
    @DisplayName("Tests public prettyDirections")
    void prettyDirectionsPointsTestPublic() {
        if (!noForbiddenConstructs) {
            TestUtils.privilegedFail("Es wurden nicht erlaubte Konstrukte verwendet.");
        }

        try {
            prettyDirectionsPointsTest();
        } catch (PrivilegedException e) {
            TestUtils.privilegedFail("Mindestens einer der Tests ist fehlgeschlagen.");
        } catch (Exception e) {
            TestUtils.privilegedFail("Bei der Ausführung des Tests wurde eine Exception geworfen.");
        }
    }

    @HiddenTest
    @DisplayName("Tests hidden prettyDirections")
    void prettyDirectionsPointsTest() {
        // 1. Leer.
        privilegedAssertEquals("", prettyDirections(Stream.empty()),
                "Bei leeren Strecken wird ein leerer String erwartet.");

        // 2. Nur ein Weg.
        OneWay oneWay = randomOneWay();
        privilegedAssertEquals(oneWay.prettyPrint(), prettyDirections(Stream.of(oneWay)),
                "Die Umwandlung eines einzelnen Pfades ist falsch.");

        // 3. Random.
        List<OneWay> oneWays = randomOneWays().toList();
        privilegedAssertEquals(prettyDirectionsOracle(oneWays.stream()), prettyDirections(oneWays.stream()),
                "Die Umwandlung eines zufälligen Pfades ist falsch.");
    }

    @Order(1)
    @PublicTest
    @DisplayName("No forbidden constructs")
    void noForbiddenConstructs() {
        // TODO: for Artemis use ./assignment/src/
        // TODO: for local testing use ../pgdp2223w09h01-ab12cde/src where you replace ab12cde with your
        //  "TUM-Kennung"
        try {
            BehaviorTest.checkForWordsFromRoot("./assignment/src/");
        } catch (StackOverflowError e) {
            TestUtils.privilegedFail("Der Test ist fehlgeschlagen, aufgrund eines Fehler mit Regex, " +
                    "bitte wenden Sie sich an einen Anton Kluge.");
        }

        // Test for forbidden recursive calls
        try {
            transformToWays(randomWayPointsInfinit().limit(20000).toList());
            kidFriendlyTrip(randomOneWaysInfinit().limit(20000).toList());
            pathLength(randomOneWaysInfinit().limit(20000));
            furthestAwayFromHome(randomWayPointsInfinit().limit(20000), someWayPoint);
            onTheWay(randomOneWaysInfinit().limit(20000), someWayPoint);
            prettyDirections(randomOneWaysInfinit().limit(20000));
        } catch (StackOverflowError e) {
            TestUtils.privilegedFail("Es wurde eine rekursive Methode verwendet oder implementiert.");
        } catch (Exception ignored) { }

        noForbiddenConstructs = true;
    }


    /**
     * CONSTRUCT CHECK METHODS FORM HERE ON
     */

    private static final String ANY_NON_CHAR = "[^a-zA-Z_\\d]";
    private static final Pattern COMMENT_LINE = Pattern.compile("//.*");
    private static final Pattern COMMENT_BLOCK = Pattern.compile("/\\*([^/*]*)*\\*/");
    private static final Pattern STRING_LITERAL = Pattern.compile("\"(\\\\.|[^\"\n])*?\"");

    enum Patterns {
        WHILE(Pattern.compile(ANY_NON_CHAR + "while[ \n]*\\(")),
        FOR(Pattern.compile(ANY_NON_CHAR + "for[ \n]*\\(")),
        TERNARY(Pattern.compile("\\?(.| |\n)*?:")),
        IF(Pattern.compile(ANY_NON_CHAR + "if[ \n]*\\(")),
        FOREACH(Pattern.compile("\\.[ \n]*forEach[ \n]*\\("));

        final Pattern pattern;
        Patterns(Pattern pattern) { this.pattern = pattern; }
    }

    private static boolean matches(Pattern pattern, String content) {
        return pattern.matcher(content).find();
    }

    private static String replaceComments(String content) {
        content = COMMENT_LINE.matcher(content).replaceAll(" ");
        content = COMMENT_BLOCK.matcher(content).replaceAll(" ");
        return STRING_LITERAL.matcher(content).replaceAll(" ");
    }

    public static void checkForWordsFromRoot(String root) {
        try (Stream<Path> files = Files.walk(Path.of(root))) {
            files.filter(path -> Files.isRegularFile(path) && path.toString().endsWith(".java"))
                    .map(BehaviorTest::readFile).forEach(BehaviorTest::checkForKeywords);
        } catch (IOException e) {
            TestUtils.privilegedFail("Die Datei " + root + " konnte nicht iteriert werden.");
        }
    }

    public static void checkForKeywords(String content) {
        String rmComments = replaceComments(content);
        for (Patterns pattern : Patterns.values()) {
            if (matches(pattern.pattern, rmComments)) {
                TestUtils.privilegedFail("Forbidden keyword: " + pattern.name());
            }
        }
    }

    private static String readFile(Path file) {
        try {
            return Files.readString(file);
        } catch (IOException e) {
            TestUtils.privilegedFail("Die Datei " + file + " konnte nicht gelesen werden. " +
                    "Stell sicher dass sie im Ordner src/pgdp/pingutrip liegt.");
        }
        return "";
    }

}
