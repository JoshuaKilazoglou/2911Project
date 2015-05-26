import java.util.ArrayList;


public class MinMaxAI {
	public static final int START_DEPTH = 5;
	public static final int MIN_COL = 0, MAX_COL = 6;
	public static final int DEFAULT_MOVE = 0, DEFAULT_SCORE = 0;
	public static final int HUMAN = 1, AI = 2;
	public static final int WINNING_SCORE = 1000000, LOSING_SCORE = -1000000; //probably worth changing at some point
	
	public MinMaxAI() {
		//not sure if needed?
	}

	public int decideMove(Game currentBoard) {
		
		int depth = START_DEPTH;
		int move = DEFAULT_MOVE;
		int score = DEFAULT_MOVE;
		int maxScore = DEFAULT_MOVE;
		
		//this loop is a special case of the loop within MinMax
		for(int i = MIN_COL; i < MAX_COL; i++) {
			Game child = generateChild(currentBoard);
			score = minMax(child, depth, i, AI);
			if (score > maxScore) maxScore = score; move = i;
		}

		return move;
	}
	
	private int minMax(Game currentBoard, int depth, int move, int player) {
		//If move is invalid, return losing score
		if(!currentBoard.checkValidMove(move)) return LOSING_SCORE;
		//Otherwise move is actually made
		currentBoard.makeMove(move); 
		//Then move is scored
		int score = score(currentBoard, move, player);
		//return if max depth reached OR winning score reached
		if (depth == 0 || (score >= WINNING_SCORE) ) {
			return score;
		}
		
		int bestScore = 0;//?
		
		if (player == AI) {
			bestScore = LOSING_SCORE;
			for (int newMove = MIN_COL; newMove < MAX_COL; newMove++) {//for each possible move
				Game child = generateChild(currentBoard);//maybe should be clone
				//MinMax with next player
				score = minMax(child, depth, newMove, HUMAN);
				//BestScore is maximising score for AI
				bestScore = Math.max(score, bestScore);
			}	
		} else {
			bestScore = WINNING_SCORE;
			for (int newMove = MIN_COL; newMove < MAX_COL; newMove++) {//for each possible move
				Game child = generateChild(currentBoard);//maybe should be clone
				//MinMax with next player
				score = -minMax(child, depth, newMove, AI); //MinMax with opposite player
				//BestScore is minimising score for Human
				bestScore = Math.min(score, bestScore);
			}	
		
		}
			
		return bestScore;
	}
	
	/**
	 * Scores the move based on how useful it is determined to be
	 * @param currentBoard Current gamestate to be scored
	 * @param move The potential move being scored
	 * @param player The player whose turn it is
	 * @return The score
	 */
	private int score(Game currentBoard, int move, int player) {
		int score = 0;
		int y = currentBoard.top(move); //Determine y value of coordinate
		int maxConnect = 0;
		for(int direction = 0; direction <= 3; direction++) {
			maxConnect = Math.max(currentBoard.howManyConnect(y,  move, player, direction), maxConnect);
		}
		switch (maxConnect) { //assign scores (tentative values)
		case 0: score = 0; 		break; //shouldnt occur
		case 1: score = 0; 		break; //no other chips
		case 2: score = 50;		break; //2 in a row
		case 3: score = 100;	break;  //3 in a row
		case 4: score = WINNING_SCORE; break; //4 in a row i.e. win
		}
		return score;
	}
	
	/**
	 * Generates a clone of the current board
	 * @param currentBoard The current gamestate
	 * @return the clone
	 */
	//NOTE: due to changes this is possibly a redundant function
	private Game generateChild(Game currentBoard) {
		Game child = new Game(); //needs some sort of clone
		return child;
	}
}
