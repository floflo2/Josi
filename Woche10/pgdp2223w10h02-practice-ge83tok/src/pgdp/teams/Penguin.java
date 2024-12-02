package pgdp.teams;

import java.util.HashMap;

public class Penguin {
	public final String name;
	public final int attack;
	public final int defence;
	public final int support;
	private final HashMap<Penguin, Integer> synergies;

	public Penguin(String name, int attack, int defence, int support) {
		this.name = name;
		this.attack = attack;
		this.defence = defence;
		this.support = support;
		synergies = new HashMap<>();
	}

	/**
	 * Returns the synergy of the current {@code Penguin} with the given {@code Penguin} if present,
	 * or the default value 0
	 * @param other the other {@code Penguin}
	 * @return the saved synergy or 0
	 */
	public int getSynergy(Penguin other) {
		return synergies.getOrDefault(other, 0);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(" [").append(attack).append(",").append(defence).append(",").append(support).append("]");
		return sb.toString();
	}

	/**
	 * Sets the synergy of {@code Penguin}s p1 and p2 to the given value
	 * @param p1 the first {@code Penguin}
	 * @param p2 the second {@code Penguin}
	 * @param value the new synergy value
	 */
	public static void setSynergy(Penguin p1, Penguin p2, int value) {
		p1.synergies.put(p2, value);
		p2.synergies.put(p1, value);
	}
}
