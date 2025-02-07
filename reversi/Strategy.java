//06-02-2025, version 2, refactored in reversi package

package reversi;

public interface Strategy {
	int strategy(long me, long opponent, int total_pieces, int diff_pieces);
	String getIdentity();
}