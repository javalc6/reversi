package reversi;
/**
 * The SymmetryChecker class provides methods to check for various types of symmetry in a 2D board represented as an array of long integers.
 * It determines if the board exhibits symmetry across its horizontal axis, if each row is a palindrome, or if it's symmetric across either of its main diagonals.
 * The board is interpreted as two 8x8 boards packed into a long array, where each long represents a board for black and white pegs, and each byte within a long represents a row of the board.
 */
public class SymmetryChecker {
    private static final boolean[] PalindromeTable = new boolean[256];

    static {
        for (int i = 0; i < 256; i++) {
			byte x = (byte) i;
			byte reverseBits = 0;
			for (int j = 0; j < 8; j++) {
				reverseBits <<= 1;
				reverseBits |= (byte) (x & 1);
				x >>= 1;
			}
			PalindromeTable[i] = reverseBits == (byte) i;
        }
    }

	public static boolean checkSymmetry(long[] board) {
		return checkboard(board[0]) && checkboard(board[1]);
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