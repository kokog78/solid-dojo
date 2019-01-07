package dojo;

public class TicTacToeBoardPrinter {

	public void printBoard(TicTacToeBoard board) {
		System.out.print(' ');
		for (int x=0; x < board.getWidth(); x++) {
			System.out.print(' ');
			System.out.print(x);
		}
		System.out.println();
		for (int y=board.getHeight()-1; y >= 0; y--) {
			printLine(board.getWidth());
			System.out.println(y);
			for (int x=0; x < board.getWidth(); x++) {
				System.out.print("|");
				GameResult cell = board.getCell(x, y);
				System.out.print(getPlayerMarker(cell));
			}
			System.out.println("|");
		}
		printLine(board.getWidth());
	}
	
	private void printLine(int width) {
		for (int x=0; x < width; x++) {
			System.out.print("+-");
		}
		System.out.println("+");
	}
	
	private char getPlayerMarker(GameResult player) {
		switch (player) {
		case PLAYER_O:
			return 'O';
		case PLAYER_X:
			return 'X';
		default:
			return ' ';
		}
	}
	
}
