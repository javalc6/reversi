/*
class ReversiScenario is an helper for Reversi engine

used by ReversiBoard.java

06-02-2025, version 2, refactored in reversi package

*/
package reversi;

import java.util.Arrays;
import java.util.Random;
import static java.lang.Long.bitCount;

public class ReversiScenario {
	private final static int boardSize = 8;

    public final int checkBoard(int x0, int y0, long playeru, long opponentu, long[] result) {//bitmask64 replace by shift operations
		int pivot = (x0 << 3) + y0;
		int check = 0;
	// check increasing x
		int n_inc = 0;
		int max_inc = boardSize - (x0 + 1);
		long bitmask = 1L << (55 - pivot);//x = x0 + 1;
		while ((n_inc < max_inc) && ((opponentu & bitmask) != 0)) {//bitmask64[x][y0]
		  bitmask >>>= 8;
		  n_inc++;
		}
		if ((n_inc != 0) && (n_inc != max_inc) && ((playeru & bitmask) != 0)) {
			check += n_inc;
			for (; n_inc > 0; n_inc--) {
			  bitmask <<= 8;//x--; 
			  playeru |= bitmask; opponentu &= ~bitmask;//bitmask64[x][y0]
			}
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
			check += n_inc;
			for (; n_inc > 0; n_inc--) {
			  bitmask >>>= 8;//x++; 
			  playeru |= bitmask; opponentu &= ~bitmask;//bitmask64[x][y0]
			}
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
			check += n_inc;
			for (; n_inc > 0; n_inc--) {
			  bitmask <<= 1;//y--; 
			  playeru |= bitmask; opponentu &= ~bitmask;//bitmask64[x0][y]
			}
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
			check += n_inc;
			for (; n_inc > 0; n_inc--) {
			  bitmask >>>= 1;//y++; 
			  playeru |= bitmask; opponentu &= ~bitmask;//bitmask64[x0][y]
			}
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
			check += n_inc;
			for (; n_inc > 0; n_inc--) {
			  bitmask <<= 9;//x--; y--;
			  playeru |= bitmask; opponentu &= ~bitmask;//bitmask64[x][y]
			}
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
			check += n_inc;
			for (; n_inc > 0; n_inc--) {
  	          bitmask <<= 7;//x--; y++;
			  playeru |= bitmask; opponentu &= ~bitmask;//bitmask64[x][y]
			}
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
			check += n_inc;
			for (; n_inc > 0; n_inc--) {
		      bitmask >>>= 7;//x++; y--;		  
			  playeru |= bitmask; opponentu &= ~bitmask;//bitmask64[x][y]
			}
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
			check += n_inc;
			for (; n_inc > 0; n_inc--) {
			  bitmask >>>= 9;//x++; y++;
			  playeru |= bitmask; opponentu &= ~bitmask;//bitmask64[x][y]
			}
		}
        if (check != 0) {
			check <<= 1;//multiply by 2
			bitmask = 1L << (63 - pivot);
			playeru |= bitmask;//bitmask64[x0][y0]
			check++;//add 1
		}
		result[0] = playeru;
		result[1] = opponentu;
		return check;
    }

    private final int deepdive(long player, long opponent) {
		boolean pass = false;
		boolean flip = false;
        long[] players = new long[2];
		next:
		while (true) {
			long board = player | opponent;
			long bit = 1; //bit 0
			for (int i = boardSize - 1; i >= 0; i--) {
				for (int j = boardSize - 1; j >= 0; j--) {
					if (((board & bit) == 0) && (checkBoard(i, j, player, opponent, players) != 0)) {
                        player = players[1];
                        opponent = players[0];
                        flip = !flip;
						pass = false;
						continue next;
					}
					bit <<= 1;
				}
			}
			if (pass) {
                if (flip)
                    return bitCount(opponent) - bitCount(player);
                else return bitCount(player) - bitCount(opponent);
			} else {
			    long temp = player;
				player = opponent;
				opponent = temp;
                flip = !flip;
				pass = true;
			}
		}
    }

    public final int findAlfaBeta(int level, Strategy mStrategy, long player, long opponent, int alfa, boolean pass, boolean is_opponent) {//pass
        long board = player | opponent;
        int min = Integer.MAX_VALUE; // high value
        CandidateMove[] candidateMoves = new CandidateMove[32];//pruning

        int nplayer = bitCount(player);
        int nopponent = bitCount(opponent);
        long[] players = new long[2];

//pruning, step 1: find all candidate moves and assign a score
        int nodes = 0;//pruning
        long bit = 1; //bit 0
        for (int i = boardSize - 1; i >= 0; i--) {//pruning
            for (int j = boardSize - 1; j >= 0; j--) {
                int check;
                if (((board & bit) == 0) && ((check = checkBoard(i, j, player, opponent, players)) != 0)) {//twin
                    int np_actual = nplayer + (1 + check) / 2;
                    int no_actual = nopponent + (1 - check) / 2;
                    int score;
                    if (mStrategy != null) {
						score = - mStrategy.strategy(players[0], players[1], np_actual + no_actual, np_actual - no_actual);
                    } else score = np_actual - no_actual;
                    candidateMoves[nodes] = new CandidateMove(players[0], players[1], i, j, score);
                    nodes++;
                }
                bit <<= 1;
            }
        }
//pruning, step 2: sort candidate moves based on score
        Arrays.sort(candidateMoves, 0, nodes, (a, b) -> b.score - a.score);//pruning

//pruning, step 3: execute main logic with pruning
        for (int k = 0; k < nodes; k++) {
            int i = candidateMoves[k].i;
            int j = candidateMoves[k].j;
            int np_actual = bitCount(candidateMoves[k].playeru);
            int no_actual = bitCount(candidateMoves[k].opponentu);
            int score;
            if (np_actual == 0) {
                score = Integer.MAX_VALUE;
            } else if (no_actual == 0) {
                score = -Integer.MAX_VALUE;
			} else if (np_actual + no_actual == 64) {
				if (np_actual > no_actual)
					score = - Integer.MAX_VALUE;
				else if (np_actual == no_actual)
					score = 0;
				else score = Integer.MAX_VALUE;
            } else if (level > 1) {
                score = findAlfaBetaInternal(level - 1, mStrategy, candidateMoves[k].opponentu, candidateMoves[k].playeru, min, false, !is_opponent);
            } else {
                if (mStrategy != null) {
					score = - candidateMoves[k].score;
                } else {
					score = deepdive(candidateMoves[k].opponentu, candidateMoves[k].playeru);
                }
            }
            if (min > score)
                min = score;
            if (alfa < -min)//alfabeta pruning
                return -min;
        }
        if (nodes == 0) {
            if (pass) {
                if (nplayer > nopponent)
                    return Integer.MAX_VALUE;
                else if (nplayer == nopponent)
                    return 0;
                else return -Integer.MAX_VALUE;
            } else {
                min = findAlfaBetaInternal(level, mStrategy, opponent, player, min, true, !is_opponent);
            }
        }
        return -min;
    }

  private final int findAlfaBetaInternal(int level, Strategy mStrategy, long player, long opponent, int alfa, boolean pass, boolean is_opponent) {//pass
	long board = player | opponent;
    int min = Integer.MAX_VALUE; // high value

	int nplayer = bitCount(player);
	int nopponent = bitCount(opponent);
    long[] players = new long[2];

	int nodes = 0;
	long bit = 1; //bit 0
	for (int i = boardSize - 1; i >= 0; i--) {
		for (int j = boardSize - 1; j >= 0; j--) {
			int check, score;
			if (((board & bit) == 0) && ((check = checkBoard(i, j, player, opponent, players)) != 0)) {
				nodes++;
				int np_actual = nplayer + (1 + check) / 2;
				int no_actual = nopponent + (1 - check) / 2;
				if (np_actual == 0) {
					score = Integer.MAX_VALUE;
				} else if (no_actual == 0) {
					score = -Integer.MAX_VALUE;
				} else if (np_actual + no_actual == 64) {
					if (np_actual > no_actual)
						score = - Integer.MAX_VALUE;
					else if (np_actual == no_actual)
						score = 0;
					else score = Integer.MAX_VALUE;
				} else if (level > 1) {
					score = findAlfaBetaInternal(level - 1, mStrategy, players[1], players[0], min, false, !is_opponent);
				} else {
					if (mStrategy != null) {
						score = mStrategy.strategy(players[0], players[1], np_actual + no_actual, np_actual - no_actual);
					} else {
						score = deepdive(players[1], players[0]);
					}
				}
				if (min > score)
					min = score;
				if (alfa < -min)//alfabeta pruning
					return -min;
			}
			bit <<= 1;
		}
	}
	if (nodes == 0) {
		if (pass) {
			if (nplayer > nopponent)
				return Integer.MAX_VALUE;
			else if (nplayer == nopponent)
				return 0;
			else return -Integer.MAX_VALUE;
		} else {
			min = findAlfaBetaInternal(level, mStrategy, opponent, player, min, true, !is_opponent);
		}
    }
    return -min;
  }
}