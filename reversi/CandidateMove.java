//06-02-2025, version 2, refactored in reversi package

package reversi;

public class CandidateMove {
	final long playeru, opponentu;
	final int i, j;
	final int score;

	CandidateMove(long playeru, long opponentu, int i, int j, int score) {
		this.playeru = playeru;
		this.opponentu = opponentu;
		this.i = i;
		this.j = j;
		this.score = score;
	}
}
