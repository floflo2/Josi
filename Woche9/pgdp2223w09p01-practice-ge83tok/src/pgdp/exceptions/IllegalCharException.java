package pgdp.exceptions;

public class IllegalCharException {
    private String escapeSpecial(char used) {
        return switch (used) {
            case '\n' -> "line break (\\n)";
            case '\t' -> "tab (\\t)";
            case '\r' -> "carriage return (\\r)";
            case ' ' -> "whitespace";
            case '\b' -> "backspace (\\b)";
            case '\f' -> "formfeed (\\f)";
            default -> Character.toString(used);
        };
    }
}
