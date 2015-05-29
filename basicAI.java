import java.util.ArrayList;
import java.util.Random;


public class basicAI implements AI{
	final static int AI = 2, HUMAN = 1;
	public int decideMove(Game currentBoard) {	
		Random generator = new Random();

		int directWin = checkInstantWinOrLose(currentBoard);
		if(directWin != Game.BADMOVE)
			return directWin;
		
		ArrayList<Integer> moves = new ArrayList<Integer>();
		for (int i = 0; i < 7; i++) {
			if (currentBoard.checkValidMove(i))
				moves.add(i);
		}
		int rand = generator.nextInt(moves.size());
		return moves.get(rand);
	}
	
	private int score(int move) {
		//TODO
		return 0;
	}

	public int checkInstantWinOrLose(Game currentBoard){
		for(int col = 0; col < Game.COL; col++){
			if(!currentBoard.checkValidMove(col))
				continue;
			int row = currentBoard.top(col);
			if(currentBoard.win(row,col,AI) || currentBoard.win(row,col,HUMAN))
				return col;
		}
		return Game.BADMOVE;
	}
	
}
