/*
base class for Test and Tester

contains different game strategies to be used in tests

06-02-2025, version 2, refactored in reversi package
*/
package reversi;

import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;

import static java.lang.Long.bitCount;

public class AbstractTest {
    public final static int boardSize = 8;
	public final static byte[] bitmask = {(byte)0x80,0x40,0x20,0x10,8,4,2,1};
    public static ReversiBoard board = new ReversiBoard();

	private final static long[][] bitmask64 = new long[8][8];
	{//static
		long bit = 1; //bit 0
		for (int i = boardSize - 1; i >= 0; i--) {
			for (int j = boardSize - 1; j >= 0; j--) {
				bitmask64[i][j] = bit;
				bit <<= 1;
			}
		}
	}

    public final static Strategy none =
        new Strategy() {
          public int strategy(long me, long opponent, int total_pieces, int diff_pieces) {
            return 0;
          }
		  public String getIdentity() {
			  return "none";
		  }
        };
    public final static Strategy normal =
        new Strategy() {
          public int strategy(long me, long opponent, int total_pieces, int diff_pieces) {
            int tstrat = 0;
            for (int i = 0; i < 8; i++) {
              if ((opponent & bitmask64[i][0]) != 0) tstrat++;//opponent[i][0]
              else if ((me & bitmask64[i][0]) != 0) tstrat--;//me[i][0]
              if ((opponent & bitmask64[i][7]) != 0) tstrat++;//opponent[i][7]
              else if ((me & bitmask64[i][7]) != 0) tstrat--;//me[i][7]
              if ((opponent & bitmask64[0][i]) != 0) tstrat++;//opponent[0][i]
              else if ((me & bitmask64[0][i]) != 0) tstrat--;//me[0][i]
              if ((opponent & bitmask64[7][i]) != 0) tstrat++;//opponent[7][i]
              else if ((me & bitmask64[7][i]) != 0) tstrat--;//me[7][i]
			}
			return tstrat + bitCount(opponent) - bitCount(me);
          }
		  public String getIdentity() {
			  return "normal";
		  }
        };
}
