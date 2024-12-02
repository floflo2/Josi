package pgdp.stack;

import de.tum.in.test.api.*;
import de.tum.in.test.api.jupiter.Public;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Public
@MirrorOutput
@StrictTimeout(1)
@WhitelistPath(value = "../pgdp2122*w07p04**", type = PathType.GLOB) // for manual assessment and development
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath(value = "{test,target/test}**Test*.{java,class}", type = PathType.GLOB)
@WhitelistPackage("java.util.Stack")
public class StackBehaviorTest {

    @StrictTimeout(60)
    @Test
    public void maxInt() {

        Stack<Integer> stack = new Stack<>();

        int increment = 1000;

        for (int i = 0; i >= 0; i+=increment) {
            stack.push(i);
        }

        for (int i = Integer.MAX_VALUE - Integer.MAX_VALUE%increment; i >= 0; i-=increment) {
            int test = stack.pop();
            if (test != i) {
                fail("Dein stack hat ein Element nicht wie erwartet gespeichert");
            }
        }
    }

    @Test
    public void oneToTen() {

        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < 10; i++) {
            stack.push(i);
        }

        for (int i = 9; i >= 0; i--) {
            int test = stack.pop();
            if (test != i) {
                fail("Beim pushen und poppen der Zahlen 0-9 wurde nicht die erwartete Zahl zurück gegeben. %d statt %d".formatted(test, i));
            }
        }
    }

    @Test
    public void removeEmpty() {

        Stack<Object> stack = new Stack<>();

        final Object[] test = new Object[1];

        assertDoesNotThrow(() -> {
            test[0] = stack.pop();
        });

        if (test[0] != null) {
            fail("Ein leerer Stack soll null beim pop() zurück geben.");
        }
    }

    @Test
    public void simplePushPop() {

        Stack<String> stack = new Stack<>();

        String test = "Pinguine sind toll!🐧";

        stack.push(test);

        assertEquals(test, stack.pop() , "String vor dem push und nach dem pop stimmen nicht überein.");
    }

    @Test
    public void randomInsertions() {

        Stack<Integer> stack = new Stack<>();
        List<Integer> state = new ArrayList<>();

        Random random = new Random(187);

        for (int i = 0; i < 10; i++) {

            stack.push(i);
            state.add(i);

            while (true) {

                if (state.size() == 0) {
                    break;
                }

                if (random.nextInt(2) == 0) {
                    int test = stack.pop();
                    if (test != state.get(state.size()-1)) {

                        fail("Es wurde das falsche element vom stack zurück gegeben. Erwartet: %d, erhalten: %d, Der restliche Stack: %s".formatted(state.get(state.size()-1), test, stackDUmp(stack)));

                    }
                    state.remove(state.size()-1);
                } else {
                    break;
                }
            }
        }
    }

    @Test
    void testExample() {

        Stack<String> stack = new Stack<>();

        stack.push("PGDP W07P04");
        stack.push("PGDP W07H01");
        stack.push("PGDP W07H02");
        stack.push("DS HA");

        String result = stack.pop();
        if (!result.equals("DS HA")) {
            fail("Es wurde das falsche element vom stack zurück gegeben. Erwartet: %s, erhalten: %s, Der restliche Stack: %s".formatted("DS HA", result, stackDUmp(stack)));
        }

        result = stack.pop();
        if (!result.equals("PGDP W07H02")) {
            fail("Es wurde das falsche element vom stack zurück gegeben. Erwartet: %s, erhalten: %s, Der restliche Stack: %s".formatted("W07H02", result, stackDUmp(stack)));
        }

        stack.push("ERA HA");


        result = stack.pop();
        if (!result.equals("ERA HA")) {
            fail("Es wurde das falsche element vom stack zurück gegeben. Erwartet: %s, erhalten: %s, Der restliche Stack: %s".formatted("ERA HA", result, stackDUmp(stack)));
        }


        result = stack.pop();
        if (!result.equals("PGDP W07H01")) {
            fail("Es wurde das falsche element vom stack zurück gegeben. Erwartet: %s, erhalten: %s, Der restliche Stack: %s".formatted("W07H02", result, stackDUmp(stack)));
        }

        stack.push("PGDP W07P01");
        stack.push("PGDP W07P02");

        result = stack.pop();
        if (!result.equals("PGDP W07P02")) {
            fail("Es wurde das falsche element vom stack zurück gegeben. Erwartet: %s, erhalten: %s, Der restliche Stack: %s".formatted("W07P02", result, stackDUmp(stack)));
        }

        result = stack.pop();
        if (!result.equals("PGDP W07P01")) {
            fail("Es wurde das falsche element vom stack zurück gegeben. Erwartet: %s, erhalten: %s, Der restliche Stack: %s".formatted("W07P01", result, stackDUmp(stack)));
        }

        stack.push("PGDP W07P03");

        result = stack.pop();
        if (!result.equals("PGDP W07P03")) {
            fail("Es wurde das falsche element vom stack zurück gegeben. Erwartet: %s, erhalten: %s, Der restliche Stack: %s".formatted("W07P03", result, stackDUmp(stack)));
        }

        result = stack.pop();
        if (!result.equals("PGDP W07P04")) {
            fail("Es wurde das falsche element vom stack zurück gegeben. Erwartet: %s, erhalten: %s, Der restliche Stack: %s".formatted("W07P04", result, stackDUmp(stack)));
        }
    }


    String stackDUmp(Stack<?> stack) {

        List<Object> state = new ArrayList<>();

        Object test;
        while((test = stack.pop()) != null) {

            state.add(test);
        }

        Collections.reverse(state);

        return state.toString();
    }
}