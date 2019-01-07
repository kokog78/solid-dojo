package dojo.tictactoe;

public class Printer {

	public void printBoard(Board board) {
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
				State state = board.getCell(x, y);
				System.out.print(getPlayerMarker(state));
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
	
	private char getPlayerMarker(State state) {
		switch (state) {
		case PLAYER_O:
			return 'O';
		case PLAYER_X:
			return 'X';
		default:
			return ' ';
		}
	}
	
}
