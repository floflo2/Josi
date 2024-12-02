package pgdp.sim;

public class Simulation {
	private Cell[] cells;
	private int width;
	private int height;

	public Simulation(Cell[] cells, int width, int height) {
		this.width = width;
		this.height = height;
		this.cells = cells;
	}

	/** Simuliert einen Tick des Spiels:
	 *  Erst nehmen alle MovingCells Nahrung zu sich,
	 *  dann wird auf allen Cells die tick()-Methode aufgerufen.
	 */
	public void tick() {
		// TODO: Diese Methode implementieren
	}
}
