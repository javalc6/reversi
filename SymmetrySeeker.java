/*
Perform automatic reversi games to find symmetric boards

Usage:  java SymmetrySeeker [-v] [number of solutions]

-v	verbose
*/
import java.util.Random;

import reversi.AbstractTest;
import reversi.Move;
import reversi.ReversiScenario;
import reversi.Strategy;
import reversi.TKind;

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

		ReversiScenario revScenario = board.getReversiScenario();

		final Strategy strategy = normal;
		int n_games = 0;
		int win = 0;
		int drawn = 0;
		for (int i = 0; i < n;) {
			Move move = new Move();
			board.clear();
			while (board.userCanMove(TKind.black) || board.userCanMove(TKind.white)) {
				if (board.findMove(TKind.black, DEFAULT_LEVEL, move, strategy, null, false, random)) {
					board.move(move.i, move.j, TKind.black);
				}
				if (board.findMove(TKind.white, DEFAULT_LEVEL, move, strategy, null, false, random)) {
					board.move(move.i, move.j, TKind.white);
				}
			}
			if (board.getCounter(TKind.black) < board.getCounter(TKind.white)) win++;
			if (board.getCounter(TKind.black) == board.getCounter(TKind.white)) drawn++;
			n_games++;

			long[] myboard = board.getBoard();
			if (checkboard(myboard[0]) && checkboard(myboard[1])) {
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

    private static final boolean[] PalindromeTable = new boolean[256];

    static {
        for (int i = 0; i < 256; i++) {
			byte x = (byte) i;
			byte reverseBits = 0;
			for (int j = 0; j < 8; j++) {
				reverseBits <<= 1;
				reverseBits |= (x & 1);
				x >>= 1;
			}
			PalindromeTable[i] = reverseBits == (byte) i;
        }
    }

	private static boolean checkboard(long aboard) {
		byte[] board = new byte[8];
		for (int i = 7; i >= 0; i--) {
			board[i] = (byte)(aboard & 0xFF);
			aboard >>= 8;
		}
		boolean test = true;
//first test
		for (int i = 0; i < 4; i++) {
			if (board[i] != board[7 - i]) {
				test = false;
				break;
			}
		}
		if (test) return true;
	
//second test
		test = true;
		for (int i = 0; i < 8; i++) {
			if (!PalindromeTable[board[i] & 0xFF]) {
				test = false;
				break;
			}
		}
		if (test) return true;

//third test - mirror board diagonal 1
		test = true;
        byte[] mirrored = new byte[8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int bit = (board[i] >> (7 - j)) & 1;
                mirrored[j] |= (byte) (bit << (7 - i));
            }
        }
		for (int i = 0; i < 8; i++) {
			if (board[i] != mirrored[i]) {
				test = false;
				break;
			}
		}
		if (test) return true;

//fourth test - mirror board diagonal 2
		test = true;
        mirrored = new byte[8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int bit = (board[i] >> j) & 1;
                mirrored[j] |= (byte) (bit << i);
            }
        }
		for (int i = 0; i < 8; i++) {
			if (board[i] != mirrored[i]) {
				test = false;
				break;
			}
		}
		return test;
	}

}