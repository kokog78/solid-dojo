package dojo.tictactoe;

public class Board {
	
	private final int width;
	private final int height;
	private State[][] cells;
	private boolean started;
	private long playerOThinkingMillis;
	private long playerXThinkingMillis;
	private long lastMoveTime = 0;
	private int numberOfMoves = 0;
	
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.cells = new State[height][width];
		clear();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public void start() {
		this.started = true;
		playerOThinkingMillis = 0;
		playerXThinkingMillis = 0;
		lastMoveTime = System.currentTimeMillis();
		numberOfMoves = 0;
	}
	
	public void clear() {
		started = false;
		for (int y=0; y < height; y++) {
			for (int x=0; x < width; x++) {
				setCell(x, y, State.BLANK);
			}
		}
	}
	
	public long getPlayerOThinkingMillis() {
		return playerOThinkingMillis;
	}
	
	public long getPlayerXThinkingMillis() {
		return playerXThinkingMillis;
	}
	
	public int getNumberOfMoves() {
		return numberOfMoves;
	}
	
	public int getNumberOfBlankCells() {
		return countCells(State.BLANK);
	}
	
	public int getNumberOfPlayerOCells() {
		return countCells(State.PLAYER_O);
	}
	
	public int getNumberOfPlayerXCells() {
		return countCells(State.PLAYER_X);
	}
	
	public void setCell(int x, int y, State state) {
		if (started) {
			switch (state) {
			case BLANK:
			case WALL:
				throw new IllegalArgumentException("Game is already started, it is not allowed to place " + state + " cell");
			default:
				expectCellIsBlank(x, y);
			}
			incPlayerThinkingMillis(state);
			numberOfMoves++;
		}
		cells[y][x] = state;
	}
	
	public State getCell(int x, int y) {
		return cells[y][x];
	}
	
	private void incPlayerThinkingMillis(State player) {
		long millis = System.currentTimeMillis() - lastMoveTime;
		if (player == State.PLAYER_O) {
			playerOThinkingMillis += millis;
		} else {
			playerXThinkingMillis += millis;
		}
	}

	private int countCells(State state) {
		int count = 0;
		for (int y=0; y < height; y++) {
			for (int x=0; x < width; x++) {
				State cell = getCell(x, y);
				if (state == cell) {
					count++;
				}
			}
		}
		return count;
	}
	
	private void expectCellIsBlank(int x, int y) {
		State cell = getCell(x, y);
		if (cell != State.BLANK) {
			throw new IllegalArgumentException("Cell " + x + "," + y + " is not blank");
		}
	}
	
}
