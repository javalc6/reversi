/*
Perform automatic reversi games to find symmetric boards

Usage:  java SymmetrySeeker [-v] [number of solutions]

-v	verbose
*/
import java.util.Random;

import reversi.AbstractTest;
import reversi.Move;
import reversi.SymmetryChecker;
import reversi.Strategy;
import reversi.TKind;

/**
 * The SymmetrySeeker class is designed to find and display symmetric game board states in a game.
 * It simulates game play by making moves for two players (black and white) until no more moves are possible.
 * It then checks if the resulting board configuration is symmetric and prints the board's transcript if it is.
 * The class can be run with command-line arguments to control the number of symmetric solutions to find and the verbosity of the output.
 */
public class SymmetrySeeker extends AbstractTest {
	private final static int DEFAULT_NUMBER_OF_SOLUTIONS = 10;
	private final static int DEFAULT_LEVEL = 1;

	public static void main(String[] args) {
		try	{
			int n = DEFAULT_NUMBER_OF_SOLUTIONS; boolean verbose = false;		
			if (args.length > 0) {
				int idx = 0;
				if (args[idx].equals("-v")) {
					verbose = true;
					idx++;
				}
				if (idx < args.length) {
					n = Integer.parseInt(args[idx]);
//					idx++;
				}
			}
			execute(n, verbose);
		} catch (NumberFormatException e)	{
			System.out.println("Usage:  java SymmetrySeeker [-v] [number of solutions]");
			System.out.println("-v	verbose");
		}
	}

	public static void execute(int n, boolean verbose) {
		long t = System.nanoTime();
		Random random = new Random();

		final Strategy strategy = normal;
		int n_games = 0;
		int win = 0;
		int drawn = 0;
		for (int i = 0; i < n;) {
			Move move = new Move();
			board.clear();
			while (board.userCanMove(TKind.black) || board.userCanMove(TKind.white)) {
				if (board.findMove(TKind.black, DEFAULT_LEVEL, move, strategy, null, null, false, random, true)) {
					board.move(move.i, move.j, TKind.black);
				}
				if (board.findMove(TKind.white, DEFAULT_LEVEL, move, strategy, null, null, false, random, false)) {
					board.move(move.i, move.j, TKind.white);
				}
			}
			if (board.getCounter(TKind.black) < board.getCounter(TKind.white)) win++;
			if (board.getCounter(TKind.black) == board.getCounter(TKind.white)) drawn++;
			n_games++;

			long[] myboard = board.getBoard();
			if (SymmetryChecker.checkSymmetry(myboard)) {
				System.out.println("Symmetric board with transcript = " + board.getTranscript());
				if (verbose)
					board.println();
				i++;
			}
		}
		if (verbose)
			System.out.println("Total#=" + n_games + "  Win#=" + win + "  Drawn#=" + drawn);

		long delta = System.nanoTime()-t;
		if (verbose)
			System.out.println("elapsed time:"+delta/1e9+" s");
	}

}