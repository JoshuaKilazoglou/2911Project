import java.util.ArrayList;
import java.util.Random;


public class basicAI implements AI{

	public int decideMove(Game currentBoard) {	
		Random generator = new Random();
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

	
	
}
