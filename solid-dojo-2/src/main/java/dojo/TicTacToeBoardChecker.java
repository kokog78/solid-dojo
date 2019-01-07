package dojo;

public class TicTacToeBoardChecker {

	private final static int[][] winnerPositions = new int[][] {
		{1, 0}, {0,1}, {1,1}, {-1,1}
	};

	public GameResult getResult(TicTacToeBoard board) {
		boolean hasEmpty = false;
		for (int y=0; y < board.getHeight(); y++) {
			for (int x=0; x < board.getWidth(); x++) {
				if (board.getCell(x, y) == null) {
					hasEmpty = true;
				} else {
					for (int[] position : winnerPositions) {
						int sumOfCells = sumOfCells(board, x, y, position[0], position[1]);
						if (sumOfCells == 3) {
							return GameResult.PLAYER_O;
						} else if (sumOfCells == -3) {
							return GameResult.PLAYER_X;
						}
					}
					
				}
			}
		}
		if (!hasEmpty) {
			return GameResult.DRAW;
		}
		return GameResult.NONE;
	}
	
	private int sumOfCells(TicTacToeBoard board, int x, int y, int deltax, int deltay) {
		return  getCellValue(board, x, y) +
				getCellValue(board, x + deltax, y + deltay) +
				getCellValue(board, x - deltax, y - deltay);
	}
	
	private int getCellValue(TicTacToeBoard board, int x, int y) {
		if (x < 0 || x >= board.getWidth() || y < 0 || y > board.getHeight()) {
			return 0;
		}
		GameResult cell = board.getCell(x, y);
		if (cell == GameResult.PLAYER_O) {
			return 1;
		} else if (cell == GameResult.PLAYER_X) {
			return -1;
		} else {
			return 0;
		}
	}
}
