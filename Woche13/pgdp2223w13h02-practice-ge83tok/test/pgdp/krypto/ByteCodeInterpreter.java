package pgdp.krypto;

import de.tum.in.test.api.TestUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

public class ByteCodeInterpreter {

    public final Stack<Long> stack = new Stack<>();
    private final Command[] code;
    private final List<Long> in = new LinkedList<>();
    public long pc = 0;
    List<Long> out = new ArrayList<>();
    private final List<String> forbidden;

    public static List<String> cleanCode(List<String> code) {

        if (usesDirectJumps(code)) {
            code = resolveDirectJumps(code);
        }

        return code.stream().map(String::trim)
                .map(s -> s.contains("//") ? s.substring(0, s.indexOf("//")).trim() : s)
                .flatMap(s -> {
                    String[] split = s.split(":");
                    String label = split[0].trim() + ":";
                    return split.length == 2 ? Stream.of(label, split[1].trim()) : Stream.of(s);
                })
                .map(String::trim)
                .map(s -> s.endsWith(":") ? s.substring(0, s.length() - 1).trim() + ":" : s)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());
    }

    private static boolean isDirectJump(String code) {
        return code.trim().matches("[Ff]?[Jj][Uu][Mm][Pp] +\\d+");
    }

    private static boolean usesDirectJumps(List<String> code) {
        return code.stream().anyMatch(ByteCodeInterpreter::isDirectJump);
    }

    private static List<String> resolveDirectJumps(List<String> code) {

        char currentLabel = 'a';

        List<String> newCode = new ArrayList<>(List.copyOf(code));

        for (int i = 0; i < code.size(); i++) {

            String line = code.get(i).trim();

            if (isDirectJump(line)) {

                String[] split = line.trim().split(" +");
                int lineJumpTo = Integer.parseInt(split[1]);
                String codeJumpTo = newCode.get(lineJumpTo).trim();
                String label;

                if (codeJumpTo.contains(":")) {
                    label = codeJumpTo.split(":")[0].trim();
                } else {
                    label = (currentLabel++) + "";
                    newCode.set(lineJumpTo, label + ": " + codeJumpTo);
                }

                newCode.set(i, split[0] + " " + label);
            }
        }

        return newCode;
    }

    public ByteCodeInterpreter(List<String> file, List<String> forbidden) {

        this.forbidden = forbidden;

        List<String> codeString = cleanCode(file);

        Map<String, Integer> labels = new HashMap<>();

        int labelCount = 0;
        for (int i = 0; i < codeString.size(); i++) {
            if (codeString.get(i).contains(":")) {
                String label = codeString.get(i).split(":")[0].trim();
                labels.put(label, i - (labelCount++));
                codeString.set(i, null);
            }
        }

        code = codeString.stream().filter(Objects::nonNull)
                .map(line -> getInstruction(line, labels))
                .toArray(Command[]::new);
    }

    private static Long translateConst(String s) {
        if (s.equalsIgnoreCase("TRUE")) {
            return 1L;
        } else if (s.equalsIgnoreCase("FALSE")) {
            return 0L;
        } else {
            return Long.parseLong(s);
        }
    }

    private static Long secondTop(Stack<Long> stack) {
        return stack.get(stack.size() - 2);
    }

    private static Long topAndClear(Stack<Long> stack) {
        Long res = stack.pop();
        stack.pop();
        return res;
    }

    interface Command {
        void execute();
    }

    public void reset() {
        stack.clear();
        out.clear();
        in.clear();
        pc = 0;
    }

    public Command getInstruction(String instruction, Map<String, Integer> labels) {
        String[] parts = instruction.split(" +");

        if (forbidden.contains(parts[0])) {
            TestUtils.privilegedFail("Die Instruktion : " + parts[0] + " ist nicht erlaubt!");
        }

        return switch (parts[0].toUpperCase()) {
            case "ADD" -> () -> stack.push(secondTop(stack) + topAndClear(stack));
            case "SUB" -> () -> stack.push(secondTop(stack) - topAndClear(stack));
            case "MUL" -> () -> stack.push(secondTop(stack) * topAndClear(stack));
            case "DIV" -> () -> stack.push(secondTop(stack) / topAndClear(stack));
            case "MOD" -> () -> stack.push(secondTop(stack) % topAndClear(stack));
            case "NEG" -> () -> stack.push(-stack.pop());
            case "AND" -> () -> stack.push(stack.pop() & stack.pop());
            case "OR" -> () -> stack.push(stack.pop() | stack.pop());
            case "NOT" -> () -> stack.push(stack.pop() == 0 ? 1L : 0L);
            case "LESS" -> () -> stack.push(secondTop(stack) < topAndClear(stack) ? 1L : 0L);
            case "LEQ" -> () -> stack.push(secondTop(stack) <= topAndClear(stack) ? 1L : 0L);
            case "EQ" -> () -> stack.push(secondTop(stack).equals(topAndClear(stack)) ? 1L : 0L);
            case "NEQ" -> () -> stack.push(!secondTop(stack).equals(topAndClear(stack)) ? 1L : 0L);
            case "TRUE" -> () -> stack.push(1L);
            case "FALSE" -> () -> stack.push(0L);
            case "CONST" -> () -> stack.push(translateConst(parts[1]));
            case "LOAD" -> () -> stack.push(stack.get(Integer.parseInt(parts[1])));
            case "STORE" -> () -> {
                int index = Integer.parseInt(parts[1]);
                stack.set(index, index == stack.size() - 1 ? stack.peek() : stack.pop());
            };
            case "JUMP" -> () -> pc = labels.get(parts[1]);
            case "FJUMP" -> () -> pc = stack.pop() == 0 ? labels.get(parts[1]) : pc;
            case "ALLOC" -> () -> IntStream.range(0, Integer.parseInt(parts[1])).forEach(i -> stack.push(0L));
            case "READ" -> () -> stack.push(in.remove(0));
            case "WRITE" -> () -> out.add(stack.pop());
            case "HALT" -> () -> pc = -1;
            default -> {
                TestUtils.privilegedFail("Unknown instruction: " + instruction);
                yield null;
            }
        };
    }

    public List<Long> run(Long... input) {
        in.addAll(Arrays.asList(input));
        while (pc >= 0 && pc < code.length) {
            if (pc >= code.length) {
                fail("Der Programm Zähler hat über das Ende hinweg gelesen.");
            }
            code[(int) pc++].execute();
        }
        return out;
    }

}
