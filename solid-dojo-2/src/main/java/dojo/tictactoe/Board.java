package dojo.tictactoe;

public class Board {
	
	private final int width;
	private final int height;
	private State[][] cells;
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.cells = new State[width][height];
	}
	
	public void setCell(int x, int y, State state) {
		if (state == State.DRAW) {
			throw new IllegalArgumentException("Invalid cell value: " + state);
		}
		if (state != State.NONE) {
			State cell = cells[x][y];
			if (cell != State.NONE) {
				throw new IllegalArgumentException("Cell " + x + "," + y + " is already occupied by player " + cell);
			}
		}
		cells[x][y] = state;
	}
	
	public State getCell(int x, int y) {
		return cells[x][y];
	}
}
