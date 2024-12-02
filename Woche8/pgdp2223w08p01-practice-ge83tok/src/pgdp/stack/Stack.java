package pgdp.stack;

import java.util.ArrayList;
import java.util.List;

public class Stack <T> {

    public void push(T t) {
	//TODO: Füge übergebenes Objekt oben auf dem Stack ein.
    }

    public T pop() {

	//TODO: Entferne oberstes Objekt vom Stack und gib es zurück.
	return null;
    }

    public static void main(String[] args) {

        Stack<String> stack = new Stack<>();

        stack.push("PGDP W07P04");
        System.out.println("Pushed: " + "PGDP W07P04");
        stack.push("PGDP W07H01");
        System.out.println("Pushed: " + "PGDP W07H01");
        stack.push("PGDP W07H02");
        System.out.println("Pushed: " + "PGDP W07H02");
        stack.push("DS HA");
        System.out.println("Pushed: " + "DS HA");

        System.out.println("Popped: " + stack.pop());
        System.out.println("Popped: " + stack.pop());

        stack.push("ERA HA");
        System.out.println("Pushed: " + "ERA HA");

        System.out.println("Popped: " + stack.pop());
        System.out.println("Popped: " + stack.pop());

        stack.push("PGDP W07P01");
        System.out.println("Pushed: " + "PGDP W07P01");
        stack.push("PGDP W07P02");
        System.out.println("Pushed: " + "PGDP W07P02");

        System.out.println("Popped: " + stack.pop());
        System.out.println("Popped: " + stack.pop());

        stack.push("PGDP W07P03");
        System.out.println("Pushed: " + "PGDP W07P03");

        System.out.println("Popped: " + stack.pop());
        System.out.println("Popped: " + stack.pop());
    }
}