package dojo;

public class TicTacToeBoard {
	
	private final int width;
	private final int height;
	
	private GameResult[][] cells;
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public TicTacToeBoard(int width, int height) {
		this.width = width;
		this.height = height;
		this.cells = new GameResult[width][height];
		for (int y=0; y < height; y++) {
			for (int x=0; x < width; x++) {
				this.cells[x][y] = GameResult.NONE;
			}
		}
	}
	
	public void setCell(int x, int y, GameResult player) {
		GameResult cell = cells[x][y];
		if (cell == GameResult.NONE) {
			cells[x][y] = player;
		} else {
			throw new IllegalArgumentException("Cell " + x + "," + y + " is already occupied by player " + cell);
		}
	}
	
	public GameResult getCell(int x, int y) {
		return cells[x][y];
	}
}
