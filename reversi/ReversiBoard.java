/*

class ReversiBoard implements the board of the game Reversi

06-02-2025, version 2, refactored in reversi package

*/
package reversi;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import static java.lang.Long.bitCount;

import tools.Duplet;

public class ReversiBoard {
  private final static int boardSize = 8;
  private final ReversiScenario revScenario = new ReversiScenario();

  private final StringBuilder transcript = new StringBuilder();

  final long[] board = new long[2];
  private boolean PassCounter;

  public ReversiBoard() {
    clear();
  }

	public ReversiScenario getReversiScenario() {
		return revScenario;
	}

    public TKind get(int i, int j) {
        long bitmask = 1L << (63 - (i << 3) - j);
        if ((board[0] & bitmask) != 0)
            return TKind.black;
        else if ((board[1] & bitmask) != 0)
            return TKind.white;
        else return TKind.nil;
    }

	public String getTranscript() {
        return transcript.toString();
    }

	public static int countMoves(String transcript, int length) {
		int count = 0;
		for (int j = 0; j < length - 1; j += 2)
			if (transcript.charAt(j) != '-')
				count++;
		return count;
	}

    public long[] getBoard() {//return board as long[], long[0] contains black and long[1] contains white
        return board;
    }

    public static long[] packBoardAsLongArray(TKind[][] board) {//return board as long[], long[0] contains black and long[1] contains white
        long bit = 1; //bit 0
        long[] result = new long[2];
        for (int p = boardSize - 1; p >= 0; p--) {//converter
            for (int q = boardSize - 1; q >= 0; q--) {
                if (board[p][q] == TKind.black)
                    result[0] += bit;
                else if (board[p][q] == TKind.white)
                    result[1] += bit;
                bit <<= 1;
            }
        }
        return result;
    }

    public int getCounter(TKind player) {
        if (player == TKind.black)
            return bitCount(board[0]);
        else if (player == TKind.white)
            return bitCount(board[1]);
        else return boardSize * boardSize - bitCount(board[0]) - bitCount(board[1]); //nil
    }


  public void clear() {
    board[0] = 0x810000000L;//initial black pieces in E4 and D5
    board[1] = 0x1008000000L;//initial white pieces in D4 and E5
    PassCounter = false;
	transcript.setLength(0);//clear transcript
  }


  public void println() {
	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 8; j++) {
			switch (get(i, j)) {
			  case white:
				System.out.print("W");
				break;
			  case black:
				System.out.print("B");
				break;
			  default:
				System.out.print(" ");
				break;
			}

		}
		System.out.println();
	}
  }

  public static void println(TKind[][] board) {
    System.out.print("[");
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) System.out.print(board[i][j] + ",");
      System.out.println((i == 7 ? "]" : ""));
    }
  }

  public static void println(boolean[][] black, boolean[][] white) {
    System.out.print("[");
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
		  String piece = "nil";
		  if (black[i][j])
			  piece = "black";
		  else if (white[i][j])
			  piece = "white";
		  System.out.print(piece + ",");
	  }
      System.out.println((i == 7 ? "]" : ""));
    }
  }

    public TKind getWinner() {
        int cdiff = getCounter(TKind.black) - getCounter(TKind.white);
        if (cdiff > 0)
            return TKind.black;
        else if (cdiff < 0)
            return TKind.white;
        else return TKind.nil;
    }

    public int move(int x, int y, TKind turn) {
        long[] players = new long[2];
        int check = revScenario.checkBoard(x, y, board[turn == TKind.black ? 0 : 1], board[turn == TKind.black ? 1 : 0], players);
        if (check != 0) {
            board[0] = turn == TKind.black ? players[0] : players[1];
            board[1] = turn == TKind.black ? players[1] : players[0];
            transcript.append((char) (x + 'A'));
            transcript.append((char) (y + '1'));
        }
        return check;
    }

    public void store_pass() {
        transcript.append("--");
    }

    public static boolean isValid(long playeru, long opponentu, int x0, int y0) {//bitmask64 replace by shift operations
		int pivot = (x0 << 3) + y0;
	// check increasing x
		int n_inc = 0;
		int max_inc = boardSize - (x0 + 1);
		long bitmask = 1L << (55 - pivot);//x = x0 + 1;
		while ((n_inc < max_inc) && ((opponentu & bitmask) != 0)) {//bitmask64[x][y0]
		  bitmask >>>= 8;
		  n_inc++;
		}
		if ((n_inc != 0) && (n_inc != max_inc) && ((playeru & bitmask) != 0)) {
			return true;
		}
    // check decreasing x
		n_inc = 0;
		max_inc = x0;
		bitmask = 1L << (71 - pivot);//x = x0 - 1;
		while ((n_inc < max_inc) && ((opponentu & bitmask) != 0)) {//bitmask64[x][y0]
		  bitmask <<= 8;//x--
		  n_inc++;
		}
		if ((n_inc != 0) && (n_inc != max_inc) && ((playeru & bitmask) != 0)) {
			return true;
		}
    // check increasing y
		n_inc = 0;
		max_inc = boardSize - (y0 + 1);
		bitmask = 1L << (62 - pivot);//y = y0 + 1;
		while ((n_inc < max_inc) && ((opponentu & bitmask) != 0)) {//bitmask64[x0][y]
		  bitmask >>>= 1;//y++
		  n_inc++;
		}
		if ((n_inc != 0) && (n_inc != max_inc) && ((playeru & bitmask) != 0)) {
			return true;
		}
    // check decreasing y
		n_inc = 0;
		max_inc = y0;
		bitmask = 1L << (64 - pivot);//y = y0 - 1;
		while ((n_inc < max_inc) && ((opponentu & bitmask) != 0)) {//bitmask64[x0][y]
		  bitmask <<= 1;//y--;
		  n_inc++;
		}
		if ((n_inc != 0) && (n_inc != max_inc) && ((playeru & bitmask) != 0)) {
			return true;
		}
    // check diagonal (+1, +1)
		n_inc = 0;
		max_inc = Math.min(boardSize - (x0 + 1), boardSize - (y0 + 1));
		bitmask = 1L << (54 - pivot);//x = x0 + 1; y = y0 + 1
		while ((n_inc < max_inc) && ((opponentu & bitmask) != 0)) {//bitmask64[x][y]
		  bitmask >>>= 9;//x++;y++;
		  n_inc++;
		}
		if ((n_inc != 0) && (n_inc != max_inc) && ((playeru & bitmask) != 0)) {//bitmask64[x][y]
			return true;
		}
    // check diagonal (+1, -1)
		n_inc = 0;
		max_inc = Math.min(boardSize - (x0 + 1), y0);
		bitmask = 1L << (56 - pivot);//x = x0 + 1; y = y0 - 1	
		while ((n_inc < max_inc) && ((opponentu & bitmask) != 0)) {//bitmask64[x][y]
		  bitmask >>>= 7;//x++;y--;
		  n_inc++;
		}
		if ((n_inc != 0) && (n_inc != max_inc) && ((playeru & bitmask) != 0)) {//bitmask64[x][y]
			return true;
		}
    // check diagonal (-1, +1)
		n_inc = 0;
		max_inc = Math.min(x0, boardSize - (y0 + 1));
		bitmask = 1L << (70 - pivot);//x = x0 - 1; y = y0 + 1
		while ((n_inc < max_inc) && ((opponentu & bitmask) != 0)) {//bitmask64[x][y]
	      bitmask <<= 7;//x--;y++;
		  n_inc++;
		}
		if ((n_inc != 0) && (n_inc != max_inc) && ((playeru & bitmask) != 0)) {//bitmask64[x][y]
			return true;
		}
    // check diagonal (-1, -1)
		n_inc = 0;
		max_inc = Math.min(x0, y0);
		bitmask = 1L << (72 - pivot);//x = x0 - 1; y = y0 - 1	
		while ((n_inc < max_inc) && ((opponentu & bitmask) != 0)) {//bitmask64[x][y]
		  bitmask <<= 9;//x--;y--;
		  n_inc++;
		}
		if ((n_inc != 0) && (n_inc != max_inc) && ((playeru & bitmask) != 0)) {//bitmask64[x][y]
			return true;
		}
		return false;
    }

	public boolean isValid(int x, int y, TKind kind) {
        long player = board[kind == TKind.black ? 0 : 1];
        long opponent = board[kind == TKind.black ? 1 : 0];
		return isValid(player, opponent, x, y);
    }


	final static Random rnd = new Random(0);//used to select opening
	public boolean findMove(TKind player, int llevel, Move move, Strategy mStrategy, HashMap<Duplet, String> openings,
			boolean print_result, Random random) {
		if (openings != null) {
			Duplet boardu = new Duplet(board[0], board[1]);
			String open = openings.get(boardu);
			if (open != null) {
				int rand = rnd.nextInt(open.length() / 2) * 2;
				int i = open.charAt(rand) - 'A';
				int j = open.charAt(rand + 1) - '1';
				long bitmask = 1L << (63 - (i << 3) - j);
				if ((((board[0] | board[1]) & bitmask) == 0) && isValid(i, j, player)) {//twin
					move.i = i;
					move.j = j;
					return true;
				}
			}// else Log.d(tag, "opening not found");
		}
		CandidateMove[] candidateMoves = new CandidateMove[32];//pruning

		Strategy strategy;
		if (9 + llevel <= getCounter(TKind.nil))
			strategy = mStrategy;
		else strategy = null;

//pruning, step 1: find all candidate moves and assign a score
        long[] players = new long[2];
		int nodes = 0;//pruning
		long bit = 1; //bit 0
		for (int i = boardSize - 1; i >= 0; i--) {//pruning
		  for (int j = boardSize - 1; j >= 0; j--) {
			if ((((board[0] | board[1]) & bit) == 0) && (revScenario.checkBoard(i, j, board[player == TKind.black ? 0 : 1], board[player == TKind.black ? 1 : 0], players) != 0)) {//twin
				long playeru = players[0]; long opponentu = players[1];
				int np_actual = bitCount(playeru);
				int no_actual = bitCount(opponentu);
				int score;
				if (mStrategy != null) {
					score = mStrategy.strategy(opponentu, playeru, np_actual + no_actual, np_actual - no_actual);
				} else score = np_actual - no_actual;
				candidateMoves[nodes] = new CandidateMove(playeru, opponentu, i, j, score);
				nodes++;
			}
			bit <<= 1;
		  }
		}
//pruning, step 2: sort candidate moves based on score
		Arrays.sort(candidateMoves, 0, nodes, (a, b) -> b.score - a.score);//pruning
		int min = Integer.MAX_VALUE; // high value
		int n_min = 0;
//pruning, step 3: execute main logic with pruning
		for (int k = 0; k < nodes; k++) {
				int i = candidateMoves[k].i;
				int j = candidateMoves[k].j;
	            long playeru = candidateMoves[k].playeru, opponentu = candidateMoves[k].opponentu;
				int max;
				int np_actual = bitCount(playeru);
				int no_actual = bitCount(opponentu);
				if (np_actual == 0) {
					max = Integer.MAX_VALUE;
				} else if (no_actual == 0) {
					max = -Integer.MAX_VALUE;
				} else if (llevel == 1)	{//level, basic choice
					if (mStrategy != null) {
						max = mStrategy.strategy(playeru, opponentu, np_actual + no_actual, np_actual - no_actual);
					} else {
						max = no_actual - np_actual;
					}
				} else if (llevel == 0)	{//level 0, random choice
					max = 0;
				} else {
					max = revScenario.findAlfaBeta(llevel - 1, strategy, opponentu, playeru, min, false, true);//pass
				}
			  if (print_result) {
				  if (n_min > 0)
					  System.out.print(", ");
				  System.out.print((char) (i + 'A')); System.out.print((char) (j + '1')); System.out.print(":"+max);
			  }
			  if ((n_min == 0) || (min > max)) {
				min = max;
				move.i = i;
				move.j = j;
				n_min = 1;
			  } else if (min == max) { // random move
				n_min++;
				if (random.nextInt(n_min) == 0) {
					move.i = i;
					move.j = j;
				}
			  }
		}
		if (print_result) {
			System.out.println("--> min = "+min);
		}
		return n_min > 0;
	}

    public static boolean userCanMove(long playeru, long opponentu) {
        long bit = 1; //bit 0
        for (int i = boardSize - 1; i >= 0; i--) {
            for (int j = boardSize - 1; j >= 0; j--) {
                if ((((playeru | opponentu) & bit) == 0) && isValid(playeru, opponentu, i, j)) return true;
                bit <<= 1;
            }
        }
        return false;
    }
    public boolean userCanMove(TKind player) {
        long playeru = board[player == TKind.black ? 0 : 1];
        long opponentu = board[player == TKind.black ? 1 : 0];
		return userCanMove(playeru, opponentu);
    }

    public static int mobility(long playeru, long opponentu) {
        int result = 0;
		long bit = 1; //bit 0
        for (int i = boardSize - 1; i >= 0; i--) {
            for (int j = boardSize - 1; j >= 0; j--) {
                if ((((playeru | opponentu) & bit) == 0) && isValid(playeru, opponentu, i, j))
					result++;
                bit <<= 1;
            }
        }
		return result;
	}
    public int mobility(TKind player) {
        long playeru = board[player == TKind.black ? 0 : 1];
        long opponentu = board[player == TKind.black ? 1 : 0];
		return mobility(playeru, opponentu);
    }

	public ArrayList<Move> getPossibleMoves(TKind player) {
        long playeru = board[player == TKind.black ? 0 : 1];
        long opponentu = board[player == TKind.black ? 1 : 0];
        ArrayList<Move> result = new ArrayList<>();
		long bit = 1; //bit 0
        for (int i = boardSize - 1; i >= 0; i--) {
            for (int j = boardSize - 1; j >= 0; j--) {
                if ((((playeru | opponentu) & bit) == 0) && isValid(playeru, opponentu, i, j)) {
					result.add(new Move(i, j));
				}
                bit <<= 1;
            }
        }
		return result;
	} 

	public static ArrayList<Move> getPossibleMoves(long playeru, long opponentu) {//general purpose
        ArrayList<Move> result = new ArrayList<>();
		long bit = 1; //bit 0
        for (int i = boardSize - 1; i >= 0; i--) {
            for (int j = boardSize - 1; j >= 0; j--) {
                if ((((playeru | opponentu) & bit) == 0) && isValid(playeru, opponentu, i, j)) {
					result.add(new Move(i, j));
				}
                bit <<= 1;
            }
        }
		return result;
	} 

}
