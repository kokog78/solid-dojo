package dojo.tictactoe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Rules {

	private final Board board;

	private final static int[][] winnerPositions = new int[][] {
		{1, 0}, {0,1}, {1,1}, {-1,1}
	};

	public Rules(Board board) {
		this.board = board;
	}

	public void initializeNormalGame() {
		board.clear();
	}

	public void initializeCrossShapedGame() {
		board.clear();
		for (int y=0; y < board.getHeight(); y++) {
			for (int x=0; x < board.getWidth(); x++) {
				if ((y < 3) ^ (x < 3)) {
					board.setCell(x, y, State.BLANK);
				} else {
					board.setCell(x, y, State.WALL);
				}
			}
		}
	}

	public void giveRandomStartTo(State player, int startCount) {
		int nrOfBlankCells = board.getNumberOfBlankCells();
		Set<Integer> startIndices = getRandomIntegers(nrOfBlankCells, startCount);
		int index = 0;
		for (int y=0; y < board.getHeight(); y++) {
			for (int x=0; x < board.getWidth(); x++) {
				if (board.getCell(x, y) == State.BLANK) {
					if (startIndices.remove(index)) {
						board.setCell(x, y, player);
					}
					index++;
				}
			}
		}
	}
	
	public void startGame() {
		board.start();
	}
	
	public boolean isGameStarted() {
		return board.isStarted();
	}

	public boolean isGameFinished() {
		return board.getNumberOfBlankCells() == 0;
	}

	public State getNextPlayer() {
		int cellCountO = board.getNumberOfPlayerOCells();
		int cellCountX = board.getNumberOfPlayerXCells();
		if (cellCountO <= cellCountX) {
			return State.PLAYER_O;
		} else {
			return State.PLAYER_X;
		}
	}

	public Result getResult() {
		boolean hasBlankCell = false;
		for (int y=0; y < board.getHeight(); y++) {
			for (int x=0; x < board.getWidth(); x++) {
				State cell = board.getCell(x, y);
				switch (cell) {
				case BLANK:
					hasBlankCell = true;
					break;
				case PLAYER_O:
				case PLAYER_X:
					Result winner = getWinnerOfCell(x, y);
					if (winner != null) {
						return winner;
					}
					break;
				default:
					// wall cell, do not check
				}
			}
		}
		if (!hasBlankCell) {
			return Result.DRAW;
		}
		return null;
	}
	
	public Statistics getStatisctics() {
		Statistics stats = new Statistics();
		stats.numberOfMoves = board.getNumberOfMoves();
		stats.playerOThinkingMillis = board.getPlayerOThinkingMillis();
		stats.playerXThinkingMillis = board.getPlayerXThinkingMillis();
		stats.avgPlayerOThinkingMillis = ((double) stats.playerOThinkingMillis) / ((double)stats.numberOfMoves);
		stats.avgPlayerXThinkingMillis = ((double) stats.playerXThinkingMillis) / ((double)stats.numberOfMoves);
		stats.avgThinkingMillis = ((double) stats.playerOThinkingMillis + stats.avgPlayerXThinkingMillis) / ((double)stats.numberOfMoves);
		return stats;
	}

	public void printBoard() {
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
		case WALL:
			return '█';
		default:
			return ' ';
		}
	}

	private Result getWinnerOfCell(int x, int y) {
		for (int[] position : winnerPositions) {
			int sumOfCells = sumOfCells(x, y, position[0], position[1]);
			if (sumOfCells == 3) {
				return Result.WINNER_O;
			} else if (sumOfCells == -3) {
				return Result.WINNER_X;
			}
		}
		return null;
	}

	private int sumOfCells(int x, int y, int deltax, int deltay) {
		return  getCellValue(x, y) +
				getCellValue(x + deltax, y + deltay) +
				getCellValue(x - deltax, y - deltay);
	}

	private int getCellValue(int x, int y) {
		if (x < 0 || x >= board.getWidth() || y < 0 || y > board.getHeight()) {
			return 0;
		}
		State cell = board.getCell(x, y);
		switch (cell) {
		case PLAYER_O:
			return 1;
		case PLAYER_X:
			return -1;
		default:
			return 0;
		}
	}
	
	private Set<Integer> getRandomIntegers(int maxValue, int count) {
		List<Integer> indices = new ArrayList<>();
		for (int i=0; i<maxValue; i++) {
			indices.add(i);
		}
		shuffle(indices);
		return new HashSet<>(indices.subList(0, count));
	}

	private <T> void shuffle(List<T> list) {
		Random rnd = ThreadLocalRandom.current();
		for (int i = list.size() - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			T item = list.get(index);
			list.set(index, list.get(i));
			list.set(i, item);
		}
	}
}
