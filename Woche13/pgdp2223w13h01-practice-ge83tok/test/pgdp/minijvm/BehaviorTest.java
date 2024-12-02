package pgdp.minijvm;

import de.tum.in.test.api.TestUtils;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pgdp.minijvm.ByteCodeInterpreter.cleanCode;

import static org.junit.jupiter.api.Assertions.fail;

@TestClassAnnotation
public class BehaviorTest {

    private static ByteCodeInterpreter lcm = null;
    private static List<String> fragment = null;

    @Order(1)
    @PublicTest
    @DisplayName("ob der ByteCode in lcm.jvm interpretiert werden könnte:")
    void parseLcm() {
        List<String> code = readFile("lcm.jvm");
        try {
            lcm = new ByteCodeInterpreter(code);
        } catch (Exception e) {
            TestUtils.privilegedFail("Die Datei lcm.jvm konnte nicht geparsed werden. " +
                    "Stell sicher, dass sie syntaktisch korrekt ist.");
        }
    }

    @PublicTest
    @DisplayName("ob der ByteCode in lcm.jvm das erwartete Ergebnis liefert:")
    void testRunLcm() {
        if (lcm == null) {
            TestUtils.privilegedFail("Um diesen Test auszuführen muss lcm.jvm interpretierbaren ByteCode enthalten.");
        }

        for (int i = 1; i < 100; i++) {
            for (int j = 1; j < 100; j++) {

                long exp = lcm(i, j);
                List<Long> act = new ArrayList<>();
                try {
                    lcm.reset();
                    act = lcm.run((long) i, (long) j);
                } catch (EmptyStackException e) {
                    fail("Es wurde versucht auf einen leeren Stack zuzugreifen mit Input: " + i);
                } catch (NullPointerException e) {
                    fail("Es wurde kein Ergebnis ausgegeben mit Input: " + i);
                } catch (Exception e) {
                    fail("Es wurde eine Exception geworfen mit Input: " + i + ": " + e.getMessage());
                }

                if (act.size() != 1) {
                    fail("Es wurde nicht genau ein Ergebnis ausgegeben mit Input: " + i);
                }

                if (act.get(0) != exp) {
                    fail("Die Implementierung in lcm.jvm ist nicht korrekt. lcm("
                            + i + ", " + j + ") sollte folgendes Ergebnis liefern: " + exp + " aber war: " + act);
                }
            }
        }
    }

    @Order(3)
    @HiddenTest
    @DisplayName("ob der ByteCode in Block A in lcm.jvm syntaktisch korrekt ist:")
    void testSyntaxBlockALcm() {
        if (testSyntaxBlockLcm("a_init.jvm")) {
            fail("Der Block A in lcm.jvm ist syntaktisch nicht korrekt.");
        }
    }

    @Order(4)
    @HiddenTest
    @DisplayName("ob der ByteCode in Block B in lcm.jvm syntaktisch korrekt ist:")
    void testSyntaxBlockBLcm() {
        if (testSyntaxBlockLcm("b_if.jvm")) {
            fail("Der Block B in lcm.jvm ist syntaktisch nicht korrekt.");
        }
    }

    @Order(5)
    @HiddenTest
    @DisplayName("ob der ByteCode in Block C in lcm.jvm syntaktisch korrekt ist:")
    void testSyntaxBlockCLcm() {
        if (testSyntaxBlockLcm("c_while.jvm")) {
            fail("Der Block C in lcm.jvm ist syntaktisch nicht korrekt.");
        }
    }

    @Order(6)
    @HiddenTest
    @DisplayName("ob der ByteCode in Block D in lcm.jvm syntaktisch korrekt ist:")
    void testSyntaxBlockDLcm() {
        if (testSyntaxBlockLcm("d_if_inner.jvm")) {
            fail("Der Block D in lcm.jvm ist syntaktisch nicht korrekt.");
        }
    }

    @Order(7)
    @HiddenTest
    @DisplayName("ob der ByteCode in Block E in lcm.jvm syntaktisch korrekt ist:")
    void testSyntaxBlockELcm() {
        if (testSyntaxBlockLcm("e_else_inner.jvm")) {
            fail("Der Block E in lcm.jvm ist syntaktisch nicht korrekt.");
        }
    }

    @Order(8)
    @HiddenTest
    @DisplayName("ob der ByteCode in Block F in lcm.jvm syntaktisch korrekt ist:")
    void testSyntaxBlockFLcm() {
        if (testSyntaxBlockLcm("f_write.jvm")) {
            fail("Der Block F in lcm.jvm ist syntaktisch nicht korrekt.");
        }
    }

    @Order(9)
    @HiddenTest
    @DisplayName("ob der ByteCode in lcm.jvm insgesamt syntaktisch korrekt ist:")
    void testSyntaxLcm() throws IOException {
        if (lcm == null) {
            TestUtils.privilegedFail("Um diesen Test auszuführen muss lcm.jvm interpretierbaren ByteCode enthalten.");
        }

        List<String> actCode = cleanCode(readFile("lcm.jvm"), true);
        List<String> expCode = cleanCode(Files.readAllLines(Path.of("test/pgdp/minijvm/oracles/lcm_oracle.jvm")), true);

        Optional<String> error = testOracleAgainstAssigmentFrom(expCode, actCode, 0, null);
        error.ifPresent(Assertions::fail);
    }

    @Order(1)
    @PublicTest
    @DisplayName("ob die Methode fragment in Klasse Fragment.java interpretiert werden kann:")
    void parseFragment() {
        try {
            List<String> fragmentCode = readFile("Fragment.java");

            fragmentCode = fragmentCode.stream().dropWhile(s -> !s.contains("public void fragment()")).skip(1)
                    .map(String::trim)
                    .map(s -> s.contains("//") ? s.substring(0, s.indexOf("//")).trim() : s)
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .collect(Collectors.toList());

            fragmentCode = removeMultilineComment(fragmentCode).stream()
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .collect(Collectors.toList());

            int bracketCounter = 1;
            for (int i = 0; i < fragmentCode.size(); i++) {
                String line = fragmentCode.get(i);
                if (line.contains("{")) {
                    bracketCounter++;
                }
                if (line.contains("}")) {
                    bracketCounter--;
                }
                if (bracketCounter == 0) {
                    fragment = fragmentCode.subList(0, i);
                    break;
                }
            }

        } catch (Exception e) {
            TestUtils.privilegedFail("Die Datei Fragment.java konnte nicht geparsed werden. " +
                    "Stell sicher, dass Du die Methodensignatur von fragment nicht verändert hast und kein " +
                    "abenteuerliches Formatting verwendest. Entferne ggf. alle Kommentare aus der Datei.");
        }
    }

    @HiddenTest
    @DisplayName("ob die Methode fragment in Klasse Fragment.java die richtigen Ergebnisse berechnet:")
    void testRunFragment() {

        Fragment fm;

        ByteArrayOutputStream output;
        ByteArrayInputStream input;
        output = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(output);
        System.setOut(ps);

        int[] is = new int[]{1, 2, 3, -4, 5, 6, 100, -111, 1023, -4121, 31231, -12221, 231546, -62342, 123134};
        int[] js = new int[]{1, -2, 3, 4, 5, 6, -893, 98, 3421, 2516, 61232, -156234, 3231, -4231, 657723, -1234};

        for (int i : is) {
            for (int j : js) {

                long exp = gcd(i, j);
                long act = Integer.MIN_VALUE;

                fm = new Fragment();
                input = new ByteArrayInputStream((i + System.lineSeparator() + j).getBytes());
                System.setIn(input);

                try {
                    output.reset();
                    fm.fragment();
                    act = Integer.parseInt(output.toString().replaceAll(System.lineSeparator(), ""));
                } catch (NumberFormatException e) {
                    fail("Es wurde kein Ergebnis zurück gegeben.");
                } catch (Exception e) {
                    fail("Es wurde eine Exception geworfen mit Input i:" + i + ", j: " + j + ": " + e.getMessage());
                }

                if (act != exp) {
                    fail("Die Implementierung ist nicht korrekt. fragment("
                            + i + ", " + j + ") sollte: " + exp + " berechnen aber war: " + act);
                }
            }
        }
    }

    @HiddenTest
    @DisplayName("ob die Methode fragment in Klasse Fragment.java die richtige Syntax hat:")
    void testSyntaxFragment() {

        if (fragment == null) {
            TestUtils.privilegedFail("Um diesen Test auszuführen muss fragment interpretierbar sein.");
        }

        String firstVar;
        String secondVar;
        try {
            String[] split = Arrays.stream(fragment.get(0).split("[, ;]"))
                    .filter(s -> !s.isBlank()).toArray(String[]::new);
            firstVar = split[1];
            secondVar = split[2];
        } catch (Exception e) {
            fail("Die Implementierung in fragment hat nicht die korrekte Syntax. " +
                    "Die erste Zeile sollte zwei Variablen deklarieren im Format: int a, b;");
            return;
        }

        fragment = fragment.stream().skip(1)
                .map(s -> s.replaceAll(" ", ""))
                .flatMap(s -> Arrays.stream(s.split("[{}]")))
                .filter(s -> !s.isBlank()).toList();

        List<String> exp = List.of(
                "%s=read();".formatted(firstVar),
                "%s=read();".formatted(secondVar),
                "%s=%s*%s;".formatted(firstVar, firstVar, secondVar),
                "if(%s<0)".formatted(firstVar),
                "%s=-%s;".formatted(firstVar, firstVar),
                "%s=lcm(%s,%s);".formatted(secondVar, firstVar, secondVar),
                "%s=%s/%s;".formatted(secondVar, firstVar, secondVar),
                "write(%s);".formatted(secondVar)
        );

        for (int i = 0; i < exp.size(); i++) {
            if (i >= fragment.size()) {
                fail("The implementation in fragment is not correct. Line "
                        + i + " is missing. There should be the line " + exp.get(i));
            }

            String actLine = fragment.get(i);
            String expLine = exp.get(i);

            if (!actLine.equals(expLine)) {
                fail("Die Implementierung in fragment hat nicht die korrekte Syntax. " +
                        "Zeile " + i + " sollte " + expLine + " sein aber ist " + actLine + ".");
            }
        }
    }


    /** HELPER METHODS FROM HERE */
    private boolean testSyntaxBlockLcm(String block) {
        if (lcm == null) {
            TestUtils.privilegedFail("Um diesen Test auszuführen muss lcm.jvm interpretierbar sein.");
        }

        List<String> actCode = cleanCode(readFile("lcm.jvm"), true);
        List<String> expCode = null;
        try {
            expCode = cleanCode(Files.readAllLines(Path.of("test/pgdp/minijvm/oracles/" + block)), false);
        } catch (IOException e) {
            fail("Die Datei " + block + " konnte nicht gelesen werden.");
        }

        return !oracleFragmentMatchesAssignment(expCode, actCode, null);
    }

    private boolean oracleFragmentMatchesAssignment(List<String> oracleFragment, List<String> assignment,
                                                    Map<String, String> labels) {
        for (int i = 0; i <= assignment.size() - oracleFragment.size(); i++) {
            Optional<String> tested = testOracleAgainstAssigmentFrom(oracleFragment, assignment, i, labels);
            if (tested.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Matches the oracle fragment against the assignment, starting in line start in the assignment.
     * @param oracle the correct code fragment.
     * @param assignment the assignment code.
     * @param start the line in the assignment to start matching.
     * @return an Optional String of the error message if the match failed, None otherwise.
     */
    private Optional<String> testOracleAgainstAssigmentFrom(List<String> oracle, List<String> assignment,
                                                            int start, Map<String, String> labels) {

        if (oracle.size() > assignment.size() - start) {
            return Optional.of("Der Code ist zu kurz. Er sollte mindestens " + oracle.size() + " Zeilen lang sein.");
        }

        Map<String, String> correspondingLabels = labels == null ? new HashMap<>() : labels;
        List<String> assignmentFragment = assignment.subList(start, assignment.size());

        for (int i = 0; i < oracle.size(); i++) {

            // 1. Case: we are out of bounds
            if (i >= assignmentFragment.size()) {
                return Optional.of("Die Implementation in lcm.jvm ist nicht richtig. Zeile "
                        + i + " fehlt. Dort sollte die Instruktion " + oracle.get(i) + " sein.");
            }

            String actLine = assignmentFragment.get(i);
            String expLine = oracle.get(i);

            // 2. Case: block of untested code
            if (expLine.equals("...")) {
                List<String> remainingOracle = oracle.subList(i + 1, oracle.size());
                List<String> remainingAssignment = assignmentFragment.subList(i, assignmentFragment.size());
                if (oracleFragmentMatchesAssignment(remainingOracle, remainingAssignment, correspondingLabels)) {
                    return Optional.empty();
                } else {
                    return Optional.of("Die Implementation in lcm.jvm ist nicht richtig.");
                }
            }

            // 3. Case: label
            if (isInstruction(expLine, "JUMP") || isInstruction(expLine, "FJUMP") || isLabel(expLine)) {

                if (isInstruction(expLine, "JUMP") && !isInstruction(actLine, "JUMP")) {
                    return Optional.of("Die Implementation in lcm.jvm ist nicht richtig. " +
                            "Zeile " + (start + i) + " sollte eine JUMP Instruktion sein.");
                }

                if (isInstruction(expLine, "FJUMP") && !isInstruction(actLine, "FJUMP")) {
                    return Optional.of("Die Implementation in lcm.jvm ist nicht richtig. " +
                            "Zeile " + (start + i) + " sollte eine FJUMP Instruktion sein.");
                }

                if (isLabel(expLine) && !isLabel(actLine)) {
                    return Optional.of("Die Implementation in lcm.jvm ist nicht richtig. " +
                            "Zeile " + (start + i) + " sollte ein Label sein.");
                }

                String expLabel = getLabel(expLine);
                String actLabel = getLabel(actLine);

                if (correspondingLabels.containsKey(expLabel)) {
                    if (!correspondingLabels.get(expLabel).equals(actLabel)) {
                        return Optional.of("Die Implementation in lcm.jvm ist nicht richtig. " +
                                "Zeile " + (start + i) + " sollte ein Label mit dem Namen: " + expLabel + " enthalten.");
                    }
                } else {
                    correspondingLabels.put(expLabel, actLabel);
                }
            }

            // 4. Case: instruction
            else {
                if (!expLine.equalsIgnoreCase(actLine.replaceAll(" +", " "))) {
                    return Optional.of("Die Implementation in lcm.jvm ist nicht richtig. " +
                            "Zeile " + i + " sollte folgende Instruktion sein " + expLine + " aber war " + actLine + ".");
                }
            }
        }
        return Optional.empty();
    }
    private static List<String> readFile(String file) {
        try {
            // TODO für Artemis: "./assignment/src/pgdp/minijvm/"
            // TODO für Assigment: "../pgdp2223w11h01-ab12cde/src/pgdp/minijvm/" und ab12cde für die eigene TUM Kennung.
            // Sonst Projekt wie immer einbinden.
            return Files.readAllLines(Path.of("./assignment/src/pgdp/minijvm/" + file));
        } catch (IOException e) {
            TestUtils.privilegedFail("Die Datei " + file + " konnte nicht gelesen werden. " +
                    "Stell sicher dass sie im Ordner src/pgdp/minijvm liegt.");
        }
        return List.of();
    }

    public static boolean isInstruction(String line, String instruction) {
        return line.trim().toUpperCase().startsWith(instruction.toUpperCase()) && !line.trim().endsWith(":");
    }

    public static boolean isLabel(String line) {
        return line.trim().endsWith(":");
    }

    public static String getLabel(String line) {
        if (isLabel(line)) {
            return line.trim().substring(0, line.length() - 1).trim();
        } else if (isInstruction(line, "JUMP") || isInstruction(line, "FJUMP")) {
            return line.trim().substring(line.trim().indexOf(" ") + 1).trim();
        }
        throw new IllegalArgumentException("The given line does not contain a label.");
    }

    private static List<String> removeMultilineComment(List<String> code) {
        String line = String.join("\n", code);
        int commentStart = 0;
        boolean inComment = false;
        for (int i = 0; i < line.length() - 1; i++) {
            if (line.charAt(i) == '/' && line.charAt(i + 1) == '*' && !inComment) {
                commentStart = i;
                inComment = true;
            }
            if (line.charAt(i) == '*' && line.charAt(i + 1) == '/' && inComment) {
                line = line.substring(0, commentStart) + line.substring(i + 2);
                i = commentStart;
                inComment = false;
            }
        }
        return Arrays.asList(line.split("\n"));
    }

    public static int lcm(int a, int b) {
        int r;
        if (a <= 0) {
            a = -a;
        }
        if (b <= 0) {
            b = -b;
        }
        r = a * b;
        while (a != b) {
            if (b < a) {
                a = a - b;
            } else {
                b = b - a;
            }
        }

        r = r / a;
        return r;
    }

    public static int gcd(int a, int b) {
        a = a * b;
        if (a < 0) {
            a = -a;
        }
        b = lcm(a, b);
        b = a / b;
        return b;
    }

}
