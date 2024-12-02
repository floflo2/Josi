package pgdp.trains;

import de.tum.in.test.api.TestUtils;
import de.tum.in.test.api.jupiter.Hidden;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pgdp.trains.connections.Station;
import pgdp.trains.connections.TrainConnection;
import pgdp.trains.connections.TrainStop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static pgdp.trains.BehaviorOracle.*;
import static pgdp.trains.processing.DataProcessing.*;

@TestClassAnnotation
public class BehaviorTest {

    private static final int[] summary = new int[6];
    private static boolean noForbiddenKeywords = true;

    static List<String> testFiles = List.of(
            "simple_connections.json",
            "empty_connections.json",
            "advanced_connections.json",
            "edgecase_connections.json",
            "real_world_connections.json");

    // So the data is only accessed after the files have been cleaned.
    static List<Arguments> testData = null;
    static Station testStationOne = new Station(8000261, "München Hbf");
    static Station testStationTwo = new Station(8000013, "Augsburg Hbf");

    static List<Arguments> testConnections() {
        if (testData == null) {
            testData = testFiles.stream().map(path ->
                    Arguments.of(loadFileOracle("connections/" + path).toList())).toList();
        }
        return testData;
    }

    @BeforeAll
    static void resetFiles() {
        Path connectionsPath = Path.of("connections");
        Path connectionsVaultPath = Path.of("connections_vault");

        // Create test directory if not exists
        if (Files.notExists(connectionsPath)) {
            try {
                Files.createDirectory(connectionsPath);
            } catch (IOException e) {
                TestUtils.privilegedFail("Something went wrong while cleaning up the test environment, "
                        + "please contact an instructor.");
            }
        }

        // Delete all files in connections folder.
        try (Stream<Path> paths = Files.walk(connectionsPath)) {
            for (Path path : paths.skip(1).toList()) {
                Files.delete(path);
            }
        } catch (IOException e) {
            TestUtils.privilegedFail("Something went wrong while cleaning up the test environment, "
                    + "please contact an instructor.");
        }

        // Transfer all files from connections to connections_vault folder.
        try (Stream<Path> vault = Files.walk(connectionsVaultPath)) {
            for (Path path : vault.skip(1).toList()) {
                Files.copy(path, connectionsPath.resolve(connectionsVaultPath.relativize(path)));
            }
        } catch (IOException e) {
            TestUtils.privilegedFail("Something went wrong while cleaning up the test environment, "
                    + "please contact an instructor.");
        }
    }

    private static <T> void assertStreamEquals(Stream<T> expected, Stream<T> actual, String message) {
        if (actual == null) {
            TestUtils.privilegedFail(message + " Der zurück gegeben Stream war null.");
        }
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

    private static void privilegedAssertEqualsMapXToDouble(Map<?, Double> expected, Map<?, Double> actual, String message) {
        for(Map.Entry<?, Double> entry : expected.entrySet()) {
            if(Math.abs(actual.get(entry.getKey()) - entry.getValue()) > 0.001 ||
                    Double.isNaN(actual.get(entry.getKey())) != Double.isNaN(entry.getValue())) {
                TestUtils.privilegedFail(message + " Erwartet wurde <" + expected + "> aber war <" + actual + ">.");
            }
        }

        for(Map.Entry<?, Double> entry : actual.entrySet()) {
            if(Math.abs(expected.get(entry.getKey()) - entry.getValue()) > 0.001 ||
                    Double.isNaN(expected.get(entry.getKey())) != Double.isNaN(entry.getValue())) {
                TestUtils.privilegedFail(message + " Erwartet wurde <" + expected + "> aber war <" + actual + ">.");
            }
        }
    }

    private static void privilegedAssertEquals(double expected, double actual, String message) {
        if (Math.abs(expected - actual) > 0.001 || Double.isNaN(expected) != Double.isNaN(actual)) {
            TestUtils.privilegedFail(message + " Erwartet wurde <" + expected + "> aber war <" + actual + ">.");
        }
    }

    /**
     * CLEAN DATASET TESTING
     */

    @Order(1)
    @PublicTest
    @DisplayName("Tests public cleanDataset(Stream<TrainConnection>)")
    void cleanDatasetTestPublic() {
        cleanDatasetTest((List<TrainConnection>) testConnections().get(0).get()[0]);
    }

    @Order(2)
    @Hidden
    @DisplayName("Tests hidden cleanDataset(Stream<TrainConnection>)")
    @ParameterizedTest(name = "[{index}] - cleanDataset(Stream<TrainConnection>)")
    @MethodSource("testConnections")
    void cleanDatasetTest(List<TrainConnection> trainConnections) {
        Stream<TrainConnection> exp = cleanDatasetOracle(trainConnections.stream());
        Stream<TrainConnection> act = cleanDataset(trainConnections.stream());
        assertStreamEquals(exp, act, "Für die Verbindungen wurden nicht richtig gesäubert.");
        summary[0]++;
    }

    @Order(3)
    @HiddenTest
    @DisplayName("Tests summary cleanDataset(Stream<TrainConnection>)")
    void cleanDatasetTestSummary() {
        if (!noForbiddenKeywords) {
            TestUtils.privilegedFail("Es wurden nicht erlaubte Konstrukte verwendet.");
        }
        privilegedAssertEquals(testFiles.size() + 1, summary[0],
                "Es wurden nicht alle Tests für cleanDataset bestanden.");
    }

    /**
     * WORST DELAYED TRAIN TESTING
     */

    @Order(1)
    @PublicTest
    @DisplayName("Tests public worstDelayedTrain(Stream<TrainConnection>)")
    void worstDelayedTrainTestPublic() {
        worstDelayedTrainTest((List<TrainConnection>) testConnections().get(0).get()[0]);
    }

    @Order(2)
    @Hidden
    @DisplayName("Tests hidden worstDelayedTrain(Stream<TrainConnection>)")
    @ParameterizedTest(name = "[{index}] - worstDelayedTrain(Stream<TrainConnection>)")
    @MethodSource("testConnections")
    void worstDelayedTrainTest(List<TrainConnection> trainConnections) {
        TrainConnection exp = worstDelayedTrainOracle(trainConnections.stream());
        TrainConnection act = worstDelayedTrain(trainConnections.stream());

        if (exp == null && act == null) {
            summary[1]++;
            return;
        } else if (exp == null) {
            TestUtils.privilegedFail("Es wird ein null Wert erwartet.");
        } else if (act == null) {
            TestUtils.privilegedFail("Die Methode hat unerwartet einen null Wert zurück gegeben.");
        }

        TrainStop expMax = exp.stops().stream().max(Comparator.comparingInt(TrainStop::getDelay)).orElse(null);
        TrainStop actMax = act.stops().stream().max(Comparator.comparingInt(TrainStop::getDelay)).orElse(null);

        if (expMax == null || actMax == null) {
            throw new RuntimeException("The test seems to have a problem, please contact an instructor.");
        }

        privilegedAssertEquals(expMax.getDelay(), actMax.getDelay(),
                "Es wurde die falsche maximale Verspätung gefunden.");
        summary[1]++;
    }

    @Order(3)
    @HiddenTest
    @DisplayName("Tests summary worstDelayedTrain(Stream<TrainConnection>)")
    void worstDelayedTrainTestSummary() {
        if (!noForbiddenKeywords) {
            TestUtils.privilegedFail("Es wurden nicht erlaubte Konstrukte verwendet.");
        }
        privilegedAssertEquals(testFiles.size() + 1, summary[1],
                "Es wurden nicht alle Tests für worstDelayedTrain bestanden.");
    }

    /**
     * PERCENT OF KIND STOPS TESTING
     */

    @Order(1)
    @PublicTest
    @DisplayName("Tests public percentOfKindStops(Stream<TrainConnection>)")
    void percentOfKindStopsTestPublic() {
        percentOfKindStopsTest((List<TrainConnection>) testConnections().get(0).get()[0]);
    }

    @Order(2)
    @Hidden
    @DisplayName("Tests hidden percentOfKindStops(Stream<TrainConnection>, Kind)")
    @ParameterizedTest(name = "[{index}] - percentOfKindStops(Stream<TrainConnection>)")
    @MethodSource("testConnections")
    void percentOfKindStopsTest(List<TrainConnection> trainConnections) {
        double expReg = percentOfKindStopsOracle(trainConnections.stream(), TrainStop.Kind.REGULAR);
        double expAct = percentOfKindStops(trainConnections.stream(), TrainStop.Kind.REGULAR);
        privilegedAssertEquals(expReg, expAct, "Die Prozent der regulären Stops wird falsch berechnet");

        expReg = percentOfKindStopsOracle(trainConnections.stream(), TrainStop.Kind.CANCELLED);
        expAct = percentOfKindStops(trainConnections.stream(), TrainStop.Kind.CANCELLED);
        privilegedAssertEquals(expReg, expAct, "Die Prozent der gecancelten Stops wird falsch berechnet");

        expReg = percentOfKindStopsOracle(trainConnections.stream(), TrainStop.Kind.ADDITIONAL);
        expAct = percentOfKindStops(trainConnections.stream(), TrainStop.Kind.ADDITIONAL);
        privilegedAssertEquals(expReg, expAct, "Die Prozent der zugefügten Stops wird falsch berechnet");
        summary[2]++;
    }

    @Order(3)
    @HiddenTest
    @DisplayName("Tests summary percentOfKindStops(Stream<TrainConnection>)")
    void percentOfKindStopsTestSummary() {
        if (!noForbiddenKeywords) {
            TestUtils.privilegedFail("Es wurden nicht erlaubte Konstrukte verwendet.");
        }
        privilegedAssertEquals(testFiles.size() + 1, summary[2],
                "Es wurden nicht alle Tests für percentOfKindStops bestanden.");
    }

    /**
     * AVERAGE DELAY AT TESTING
     */

    @Order(1)
    @PublicTest
    @DisplayName("Tests public averageDelayAt(Stream<TrainConnection>)")
    void averageDelayAtTestPublic() {
        averageDelayAtTest((List<TrainConnection>) testConnections().get(0).get()[0]);
    }

    @Order(2)
    @Hidden
    @DisplayName("Tests hidden averageDelayAt(Stream<TrainConnection>, Station)")
    @ParameterizedTest(name = "[{index}] - averageDelayAt(Stream<TrainConnection>)")
    @MethodSource("testConnections")
    void averageDelayAtTest(List<TrainConnection> trainConnections) {
        double expDel = averageDelayAtOracle(trainConnections.stream(), testStationOne);
        double actDel = averageDelayAt(trainConnections.stream(), testStationOne);
        privilegedAssertEquals(expDel, actDel, "Die durchschnittliche Verspätung wird falsch berechnet");

        expDel = averageDelayAtOracle(trainConnections.stream(), testStationTwo);
        actDel = averageDelayAt(trainConnections.stream(), testStationTwo);
        privilegedAssertEquals(expDel, actDel, "Die durchschnittliche Verspätung wird falsch berechnet");
        summary[3]++;
    }

    @Order(3)
    @HiddenTest
    @DisplayName("Tests summary averageDelayAt(Stream<TrainConnection>)")
    void averageDelayAtTestSummary() {
        if (!noForbiddenKeywords) {
            TestUtils.privilegedFail("Es wurden nicht erlaubte Konstrukte verwendet.");
        }
        privilegedAssertEquals(testFiles.size() + 1, summary[3],
                "Es wurden nicht alle Tests für averageDelayAt bestanden.");
    }

    /**
     * DELAY COMPARED TO TOTAL TRAVEL TIME BY TRANSPORT TESTING
     */

    @Order(1)
    @PublicTest
    @DisplayName("Tests public delayComparedToTotalTravelTimeByTransport(Stream<TrainConnection>)")
    void delayComparedToTotalTravelTimeByTransportTestPublic() {
        delayComparedToTotalTravelTimeByTransportTest((List<TrainConnection>) testConnections().get(0).get()[0]);
    }

    @Order(2)
    @Hidden
    @DisplayName("Tests hidden delayComparedToTotalTravelTimeByTransport(Stream<TrainConnection>)")
    @ParameterizedTest(name = "[{index}] - delayComparedToTotalTravelTimeByTransport(Stream<TrainConnection>)")
    @MethodSource("testConnections")
    void delayComparedToTotalTravelTimeByTransportTest(List<TrainConnection> trainConnections) {
        Map<String, Double> expMap = delayComparedToTotalTravelTimeByTransportOracle(trainConnections.stream());
        Map<String, Double> actMap = delayComparedToTotalTravelTimeByTransport(trainConnections.stream());
        privilegedAssertEqualsMapXToDouble(expMap, actMap, "Die Verspätung pro Fahrzeug wird falsch berechnet.");
        summary[4]++;
    }

    @Order(3)
    @HiddenTest
    @DisplayName("Tests summary delayComparedToTotalTravelTimeByTransport(Stream<TrainConnection>)")
    void delayComparedToTotalTravelTimeByTransportTestSummary() {
        if (!noForbiddenKeywords) {
            TestUtils.privilegedFail("Es wurden nicht erlaubte Konstrukte verwendet.");
        }
        privilegedAssertEquals(testFiles.size() + 1, summary[4],
                "Es wurden nicht alle Tests für delayComparedToTotalTravelTimeByTransport bestanden.");
    }

    /**
     * AVERAGE DELAY BY HOUR OF DAY TESTING
     */

    @Order(1)
    @PublicTest
    @DisplayName("Tests public averageDelayByHour(Stream<TrainConnection>)")
    void averageDelayByHourTestPublic() {
        averageDelayByHourTest((List<TrainConnection>) testConnections().get(0).get()[0]);
    }

    @Order(2)
    @Hidden
    @DisplayName("Tests hidden averageDelayByHour(Stream<TrainConnection>)")
    @ParameterizedTest(name = "[{index}] - averageDelayByHour(Stream<TrainConnection>)")
    @MethodSource("testConnections")
    void averageDelayByHourTest(List<TrainConnection> trainConnections) {
        Map<Integer, Double> expMap = averageDelayByHourOracle(trainConnections.stream());
        Map<Integer, Double> actMap = averageDelayByHour(trainConnections.stream());
        privilegedAssertEqualsMapXToDouble(expMap, actMap, "Die Verspätung pro Stunde wird falsch berechnet.");
        summary[5]++;
    }

    @Order(3)
    @HiddenTest
    @DisplayName("Tests summary averageDelayByHour(Stream<TrainConnection>)")
    void averageDelayByHourTestSummary() {
        if (!noForbiddenKeywords) {
            TestUtils.privilegedFail("Es wurden nicht erlaubte Konstrukte verwendet.");
        }
        privilegedAssertEquals(testFiles.size() + 1, summary[5],
                "Es wurden nicht alle Tests für averageDelayByHour bestanden.");
    }

    @Order(1)
    @PublicTest
    @DisplayName("No forbidden constructs")
    void noForbiddenConstructs() {
        // TODO: for Artemis use ./assignment/src/
        // TODO: for local testing use ../pgdp2223w09h02-ab123cde/src where you replace ab123cde with your
        // matrikel number
        BehaviorTest.checkForWordsFromRoot("./assignment/src/");
    }


    /**
     * CONSTRUCT CHECK METHODS FORM HERE ON
     */

    private static final String ANY_NON_CHAR = "[^a-zA-Z_\\d]";
    private static final Pattern COMMENT_LINE = Pattern.compile("//.*");
    private static final Pattern COMMENT_BLOCK = Pattern.compile("/\\*([^/*]*)*\\*/");

    enum Patterns {
        WHILE(Pattern.compile(ANY_NON_CHAR + "while[ \n]*\\(")),
        FOR(Pattern.compile(ANY_NON_CHAR + "for[ \n]*\\(")),
        // IF(Pattern.compile(ANY_NON_CHAR + "if[ \n]*\\(")),
        FOREACH(Pattern.compile( "\\.[ \n]*forEach[ \n]*\\("));

        final Pattern pattern;
        Patterns(Pattern pattern) { this.pattern = pattern; }
    }

    private static boolean matches(Pattern pattern, String content) {
        return pattern.matcher(content).find();
    }

    private static String replaceComments(String content) {
        String withOutLineComment = COMMENT_LINE.matcher(content).replaceAll("");
        return COMMENT_BLOCK.matcher(withOutLineComment).replaceAll("");
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
                noForbiddenKeywords = false;
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
