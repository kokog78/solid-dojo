package dojo.tictactoe;

public class Rules {

	private final static int[][] winnerPositions = new int[][] {
		{1, 0}, {0,1}, {1,1}, {-1,1}
	};
	
	public void startGame(Board board) {
		for (int y=0; y < board.getHeight(); y++) {
			for (int x=0; x < board.getWidth(); x++) {
				board.setCell(x, y, State.NONE);
			}
		}
	}

	public State getState(Board board) {
		boolean hasEmpty = false;
		for (int y=0; y < board.getHeight(); y++) {
			for (int x=0; x < board.getWidth(); x++) {
				if (board.getCell(x, y) == null) {
					hasEmpty = true;
				} else {
					for (int[] position : winnerPositions) {
						int sumOfCells = sumOfCells(board, x, y, position[0], position[1]);
						if (sumOfCells == 3) {
							return State.PLAYER_O;
						} else if (sumOfCells == -3) {
							return State.PLAYER_X;
						}
					}
					
				}
			}
		}
		if (!hasEmpty) {
			return State.DRAW;
		}
		return State.NONE;
	}
	
	private int sumOfCells(Board board, int x, int y, int deltax, int deltay) {
		return  getCellValue(board, x, y) +
				getCellValue(board, x + deltax, y + deltay) +
				getCellValue(board, x - deltax, y - deltay);
	}
	
	private int getCellValue(Board board, int x, int y) {
		if (x < 0 || x >= board.getWidth() || y < 0 || y > board.getHeight()) {
			return 0;
		}
		State cell = board.getCell(x, y);
		if (cell == State.PLAYER_O) {
			return 1;
		} else if (cell == State.PLAYER_X) {
			return -1;
		} else {
			return 0;
		}
	}
}
