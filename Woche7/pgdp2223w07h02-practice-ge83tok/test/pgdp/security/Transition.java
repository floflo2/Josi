package pgdp.security;

public record Transition(String state, int level, String type) {
	public String toString() {
		return "[" + state + " " + level + " " + type + "]";
	}
}
