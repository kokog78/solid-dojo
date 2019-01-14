package dojo.tictactoe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class TicTacToeIT {

	private Board board;
	private Rules rules;
	private State player;
	
	@Before
	public void initPlayer() {
		player = State.PLAYER_O;
	}
	
	@Test
	public void initializes_a_normal_game() throws Exception {
		board = new Board(3, 3);
		rules = new Rules(board);
		assertGameStatus(false, false, State.PLAYER_O);
		
		rules.initializeNormalGame();
		assertGameStatus(false, false, State.PLAYER_O);
		
		rules.startGame();
		assertGameStatus(true, false, State.PLAYER_O);
		printGameBoard("Normal game, initial board");
	}
	
	@Test
	public void plays_a_draw_game() throws Exception {
		board = new Board(3, 3);
		rules = new Rules(board);
		rules.initializeNormalGame();
		rules.startGame();
		
		normalMove(1, 1, false);
		normalMove(1, 2, false);
		normalMove(0, 1, false);
		normalMove(2, 1, false);
		normalMove(0, 2, false);
		normalMove(0, 0, false);
		normalMove(2, 2, false);
		normalMove(2, 0, false);
		normalMove(1, 0, true);
		
		assertResults(Result.DRAW);
		assertStatistics(9);
		printGameBoard("Normal game, draw results");
	}
	
	@Test
	public void plays_a_won_game() throws Exception {
		board = new Board(3, 3);
		rules = new Rules(board);
		rules.initializeNormalGame();
		rules.startGame();
		
		normalMove(1, 1, false);
		normalMove(1, 2, false);
		normalMove(0, 1, false);
		normalMove(2, 1, false);
		normalMove(0, 2, false);
		normalMove(0, 0, false);
		normalMove(2, 2, false);
		normalMove(1, 0, false);
		normalMove(2, 0, true);
		
		assertResults(Result.WINNER_O);
		assertStatistics(9);
		printGameBoard("Normal game, player O won");
	}
	
	@Test
	public void plays_a_cross_shaped_game() throws Exception {
		board = new Board(6, 6);
		rules = new Rules(board);
		assertGameStatus(false, false, State.PLAYER_O);
		
		rules.initializeCrossShapedGame();
		assertGameStatus(false, false, State.PLAYER_O);
		
		rules.startGame();
		assertGameStatus(true, false, State.PLAYER_O);		
		printGameBoard("Cross-shaped game, initial board");

		normalMove(1, 4, false);
		normalMove(2, 3, false);
		normalMove(4, 1, false);
		normalMove(3, 2, false);
		normalMove(2, 4, false);
		normalMove(0, 4, false);
		normalMove(2, 5, false);
		normalMove(0, 3, false);
		normalMove(0, 5, false);
		normalMove(1, 5, false);
		normalMove(1, 3, false);
		normalMove(4, 2, false);
		normalMove(5, 2, false);
		normalMove(3, 0, false);
		normalMove(5, 1, false);
		normalMove(5, 0, false);
		normalMove(4, 0, false);
		normalMove(3, 1, true);
		
		assertResults(Result.WINNER_X);
		assertStatistics(18);
		printGameBoard("Cross-shaped game, results");
	}
	
	@Test
	public void gives_random_start_on_normal_game() throws Exception {
		board = new Board(3, 3);
		rules = new Rules(board);
		assertGameStatus(false, false, State.PLAYER_O);
		
		rules.initializeNormalGame();
		rules.giveRandomStartTo(State.PLAYER_O, 1);
		assertGameStatus(false, false, State.PLAYER_X);
	}
	
	@Test
	public void gives_random_start_on_cross_shaped_game() throws Exception {
		board = new Board(6, 6);
		rules = new Rules(board);
		assertGameStatus(false, false, State.PLAYER_O);
		
		rules.initializeCrossShapedGame();
		rules.giveRandomStartTo(State.PLAYER_O, 1);
		assertGameStatus(false, false, State.PLAYER_X);
	}
	
	private void normalMove(int x, int y, boolean finished) {
		board.setCell(x, y, player);
		if (player == State.PLAYER_O) {
			player = State.PLAYER_X;
		} else {
			player = State.PLAYER_O;
		}
		assertGameStatus(true, finished, player);
	}

	private void assertGameStatus(boolean started, boolean finished, State nextPlayer) {
		assertThat(rules.isGameStarted()).isEqualTo(started);
		assertThat(rules.isGameFinished()).isEqualTo(finished);
		assertThat(rules.getNextPlayer()).isEqualTo(nextPlayer);
		if (!finished) {
			assertThat(rules.getResult()).isNull();
		} else {
			assertThat(rules.getResult()).isNotNull();
		}
	}
	
	private void assertResults(Result result) {
		assertThat(rules.getResult()).isEqualTo(result);
	}
	
	private void assertStatistics(int numberOfMoves) {
		Statistics stats = rules.getStatisctics();
		assertThat(stats).isNotNull();
		assertThat(stats.numberOfMoves).isEqualTo(numberOfMoves);
	}
	
	private void printGameBoard(String title) {
		System.out.println(title);
		rules.printBoard();
		System.out.println();
		System.out.println();
	}
	
}
