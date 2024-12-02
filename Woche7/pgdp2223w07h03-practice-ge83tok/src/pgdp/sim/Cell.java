package pgdp.sim;

public interface Cell {

	int priority();
	CellSymbol getSymbol();
	void tick(Cell[] cells, Cell[] newCells, int width, int height, int x, int y);

	default boolean place(Cell[] cells, Cell[] newCells, int width, int height, int x, int y) {
		// get available space
		int[] available = new int[48];
		int len = 0;
		int upperY = Math.min(height, y + 3);
		int upperX = Math.min(width, x + 3);
		for (int ny = Math.max(0, y - 2); ny < upperY; ny++) {
			for (int nx = Math.max(0, x - 2); nx < upperX; nx++) {
				if (ny != y | nx != x) {
					if (cells[nx + ny * width] == null) {
						available[len << 1] = nx;
						available[(len << 1) + 1] = ny;
						len++;
					}
				}
			}
		}
		if (len == 0) {
			return false;
		}
		int used = RandomGenerator.nextInt(len);
		int nx = available[used << 1];
		int ny = available[(used << 1) + 1];
		int position = nx + ny * width;
		if (newCells[position] == null || newCells[position].priority() < this.priority())
			newCells[position] = this;
		return true;
	}
}
