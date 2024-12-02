package pgdp.collections;

import java.util.HashMap;
import java.util.List;

public class CountingObjectsCollection<K> {
	private HashMap<K, Integer> map;

	public CountingObjectsCollection() {
		map = new HashMap<>();
	}

	public CountingObjectsCollection(CountingObjectsCollection<K> old) {
		// implement in one line of code
		// TODO 1
	}

	public void insert(K obj) {
		// implement in one line of code
		// TODO 2
	}

	public int getObjectCount(K obj) {
		// implement in one line of code
		// TODO 3
		return -1;
	}

	public int getTotalObjectCount() {
		// implement using for-each-loop
		// TODO 4
		return -1;
	}

	public List<K> getKeyList() {
		// implement in one line of code, list should be modifiable
		// TODO 5
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CountingObjectsCollection<K> other = (CountingObjectsCollection<K>) obj;
		// Assure that both maps are equal without using map.equals
		// TODO 6
		return false;
	}
}
