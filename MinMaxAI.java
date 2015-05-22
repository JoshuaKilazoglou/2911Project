import java.util.ArrayList;


public class MinMaxAI {
	public static final int START_DEPTH = 5;
	public static final int MIN_COL = 0, MAX_COL = 6;
	public static final int DEFAULT_MOVE = 0, DEFAULT_SCORE = 0;
	//public static final boolean AI = true;
	public static final int WINNING_SCORE = 1000000, LOSING_SCORE = -1000000; //probably worth changing at some point
	
	public MinMaxAI() {
		
	}

	public int decideMove(Game currentBoard) {
		
		int depth = START_DEPTH;
		int move = DEFAULT_MOVE;
		int score = DEFAULT_MOVE;
		int maxScore = DEFAULT_MOVE;
		
		
		for(int i = MIN_COL; i < MAX_COL; i++) {
			score = minMax(currentBoard, depth, i, true);
			if (score > maxScore) maxScore = score; move = i;
		}

		return move;
	}
	
	private int minMax(Game currentBoard, int depth, int move, boolean isAI) {
		
		int score = score(move);
		int bestScore = 0;
		
		if ((depth == 0 || (score >= WINNING_SCORE) || (currentBoard.checkValidMove(move)) )) {
			return score;
		}
		
		if (isAI == true) {
			bestScore = LOSING_SCORE;
			for(Game child: generateChildren(currentBoard)) {
				//TODO
				
			}
			
		}
		return score;
	}
	
	private int score(int move) {
		//TODO
		return 0;
	}
	
	/**
	 * Generates 7 boards from each possible move on the current board
	 * @param currentBoard The current gamestate
	 * @return ArrayList of the resulting child gamestates
	 */
	private ArrayList<Game> generateChildren(Game currentBoard) {
		ArrayList<Game> children = new ArrayList<Game>(7);
		for (int i = MIN_COL; i < MAX_COL; i++) {
			//TODO
		} 
		return children;
	}
}
