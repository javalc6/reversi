import static java.lang.Long.bitCount;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import reversi.AbstractTest;
import reversi.ReversiBoard;
import reversi.ReversiScenario;
import reversi.SymmetryChecker;

/**
 * The {@code GameScanner} class is designed to explore and find solutions in a Reversi game.
 * It can be configured to search for solutions up to a certain number of moves and can optionally
 * restrict the search to only symmetric solutions.
 * <p>
 * <b>Usage:</b>
 * <p>
 * The program is run from the command line with the following parameters:
 * <pre>
 *   java GameScanner [-s] &lt;max number of moves to explore&gt; [output filename]
 * </pre>
 * <ul>
 *   <li>{@code -s}: Optional. If present, the search is restricted to only symmetric solutions.</li>
 *   <li>{@code <max number of moves to explore>}: Required. Specifies the maximum number of moves to explore.</li>
 *   <li>{@code [output filename]}: Optional. Specifies the file to write the found solutions to. If not provided, solutions are printed to the console.</li>
 * </ul>
 * <p>
 * <b>Example:</b>
 * <pre>
 *   java GameScanner 10 solutions.txt
 *   java GameScanner -s 9
 * </pre>
 * <p>
 * The first example searches for solutions up to 10 moves and writes them to "solutions.txt".
 * The second example searches for symmetric solutions up to 6 moves and prints them to the console.
 */
public class GameScanner extends AbstractTest {
    public static final int BOARD_SIZE = 8;
    private static final ReversiScenario REVERSI_SCENARIO = new ReversiScenario();
    private static PrintWriter output = null;

    public static void main(String[] args) throws FileNotFoundException {
        boolean onlySymmetric = false;
        Integer maxMoves = null;
        String filename = null;

        for (String arg : args) {
            if ("-s".equals(arg)) {
                onlySymmetric = true;
            } else if (maxMoves == null) {
                try {
                    maxMoves = Integer.parseInt(arg);
                } catch (NumberFormatException e) {
                    filename = arg;
                }
            } else {
                filename = arg; // If we already have maxMoves, this must be the filename
            }
        }

        if (maxMoves != null) {
            if (filename != null) {
                output = new PrintWriter(filename);
            }
            ReversiBoard reversiBoard = new ReversiBoard();
            long[] board = reversiBoard.getBoard();
            long startTime = System.nanoTime();
            StringBuilder moveSequence = new StringBuilder();
            int numberOfSolutions = findSolutions(board[0], board[1], moveSequence, maxMoves, false, onlySymmetric);
            if (output != null) {
                output.close();
            }
            System.out.println("Number of found solutions: " + numberOfSolutions);
            System.out.println("Execution time: " + (System.nanoTime() - startTime) / 1e6 + " ms");
        } else {
            System.out.println("usage: java GameScanner [-s] <max number of moves to explore> [output filename]");
        }
    }

    public static int findSolutions(long player, long opponent, StringBuilder moveSequence, int maxMoves, boolean pass, boolean onlySymmetric) {
        int numberOfSolutions = 0;
        long board = player | opponent;
        int numberOfPlayerPieces = bitCount(player);
        int numberOfOpponentPieces = bitCount(opponent);
        int numberOfNodes = 0;
        long bit = 1; // bit 0
        long[] nextPlayers = new long[2];

        for (int row = BOARD_SIZE - 1; row >= 0; row--) {
            for (int col = BOARD_SIZE - 1; col >= 0; col--) {
                int capturedPieces;
                if (((board & bit) == 0) && ((capturedPieces = REVERSI_SCENARIO.checkBoard(row, col, player, opponent, nextPlayers)) != 0)) {
                    numberOfNodes++;
                    moveSequence.append((char) ('A' + row)).append((char) ('1' + col));

                    int actualPlayerPieces = numberOfPlayerPieces + (1 + capturedPieces) / 2;
                    int actualOpponentPieces = numberOfOpponentPieces + (1 - capturedPieces) / 2;

                    if (actualPlayerPieces == 0 || actualOpponentPieces == 0) { // end of game
                        if (!onlySymmetric || SymmetryChecker.checkSymmetry(nextPlayers)) {
                            printSolution(moveSequence);
                            numberOfSolutions++;
                        }
                    } else if (maxMoves > 1) {
                        numberOfSolutions += findSolutions(nextPlayers[1], nextPlayers[0], moveSequence, maxMoves - 1, false, onlySymmetric);
                    }
                    moveSequence.setLength(moveSequence.length() - 2);
                }
                bit <<= 1;
            }
        }

        if (numberOfNodes == 0) {
            if (pass) { // end of game
                if (!onlySymmetric || SymmetryChecker.checkSymmetry(nextPlayers)) {
                    printSolution(moveSequence);
                    numberOfSolutions++;
                }
            } else {
                moveSequence.append("--"); // pass
                numberOfSolutions += findSolutions(opponent, player, moveSequence, maxMoves - 1, true, onlySymmetric);
                moveSequence.setLength(moveSequence.length() - 2);
            }
        }
        return numberOfSolutions;
    }

    private static void printSolution(StringBuilder moveSequence) {
        if (output != null) {
            output.println(moveSequence);
        } else {
            System.out.println(moveSequence);
        }
    }

    public static String export(String lineSeparator, long black, long white) {
        StringBuilder boardString = new StringBuilder(64);
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                long bitmask = (1L << (63 - (col << 3) - row));
                if ((black & bitmask) != 0) {
                    boardString.append('X');
                } else if ((white & bitmask) != 0) {
                    boardString.append('O');
                } else {
                    boardString.append('-');
                }
            }
            if (lineSeparator != null) {
                boardString.append(lineSeparator);
            }
        }
        return boardString.toString();
    }
}