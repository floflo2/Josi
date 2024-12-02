package pgdp.sim;

public class CellSymbol {

	private final String name;
	private final int id;

	private CellSymbol(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public final static CellSymbol PLANT = new CellSymbol("PLANT", 0), HAMSTER = new CellSymbol("HAMSTER", 1),
			PINGU = new CellSymbol("PINGU", 2), WOLF = new CellSymbol("WOLF", 3);

	public int id() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return name;
	}

}
