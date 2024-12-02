package pgdp.minijvm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Fragment {

    public void fragment() {

        /* TODO: Schreibe die Methode, welche zum Bytecode in fragment.jvm kompiliert wurde. read() und write() sind
            bereits implementiert. lcm() ist die Methode die von CALL lcm aufgerufen wird. */

    }

    /**
     * Die nachfolgenden Methoden müssen in Eurem Programm verwendet werden.
     * Sie dürfen auf keinen Fall verändert werden!
     * Sonst kann es passieren, dass die Tests nicht mehr funktionieren!
     */

    public void write(int i) {
        System.out.println(i);
    }

    private BufferedReader br = null;

    public int read() {
        try {
            if (br == null) {
                br = new BufferedReader(new InputStreamReader(System.in));
            }
            return Integer.parseInt(br.readLine());
        } catch (IOException e) {
            System.err.println("Konnte nicht gelesen werden!");
        } catch (NumberFormatException e) {
            System.err.println("Keine Zahl! ");
        }
        return Integer.MIN_VALUE;
    }

    public int lcm(int a, int b) {
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

}
