package pgdp.ds;

import java.util.HashSet;
import java.util.Set;

public class SimpleSet {

	private Set<Integer> members;

	SimpleSet() {
		members = new HashSet<>();
	}

	public void add(int value) {
		members.add(value);
	}

	public boolean contains(int value) {
		return members.contains(value);
	}

	public int[] toArray() {
		return members.stream().mapToInt(x -> x.intValue()).toArray();
	}

}
