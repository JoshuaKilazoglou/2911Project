import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;


class move extends node{
	private int depth = 0,heu = 0;
	private move prev;

	public move(int col,move prev){
		super(0,col,null,null);
		this.prev = prev;
	}

	public move(int col,move prev, int heu){
		super(0,col,null,null);
		this.prev = prev;
		this.heu = heu;
	}

	public move(int col,move prev, int heu, int depth){
		super(0,col,null,null);
		this.prev = prev;
		this.heu = heu;
		this.depth = depth;
	}
	
	public void setDepth(int d){
		this.depth = d;
	}

	public void setHeu(int h){
		heu = h;
	}

	public int getDepth(){
		return depth;
	}

	public move prev(){
		return this.prev;
	}

	public int getHue(){
		return heu;
	}

	public move getHead(move prev){
		if(prev.prev() == null)
			return prev;
		else
			return getHead(prev.prev());
	
	}
}

public class DumbAI implements AI{
	final static int MAX_DEPTH = 5;
	final static int MAGIC_NUMBER = 5;
	final static int ALL_DIRECTION = 4;
	static final int WINNING_SCORE = 1000; //probably worth changing at some point
	final static int AI = 2, HUMAN = 1;
	PriorityQueue<move> pq;

	private class comparator implements Comparator<move>{
		@Override
		public int compare(move a,move b){
//			if(a.getDepth() != b.getDepth())
//				return a.getDepth() - b.getDepth();
			
			return b.getHue() - a.getHue();
		}
	}

	public void reFill(Game g, node lastMove){
		if(lastMove == null)
			return;
		else
			reFill(g,lastMove.prev());

		g.makeMove(lastMove.col());
		lastMove = lastMove.prev();
	}

	public move getBestMove(Game currentBoard,int maxDepth){
		int initialDepth = 0;
		Game g = currentBoard.clone();
		
		for(int col = 0; col < Game.COL; col++){
			if(g.checkValidMove(col)){
				int score = (MAGIC_NUMBER) * eval(g,col);
				move m = new move(col,null,score);
				pq.add(m);
			}
		}

		if(maxDepth == 0)
			return pq.poll();

		while(!pq.isEmpty() && initialDepth < maxDepth){
			move bestMove = pq.poll();
			
			if(initialDepth < bestMove.getDepth())
				initialDepth = bestMove.getDepth();

			for(int col = 0; col < Game.COL; col++){	
				Game copy = currentBoard.clone();
				reFill(copy,bestMove);
				copy.printGame();
				if(!copy.checkValidMove(col))
					continue;

				int score = (MAGIC_NUMBER - bestMove.getDepth()+1) * eval(g,col) + bestMove.getHue();
				move m = new move(col,bestMove,score,(bestMove.getDepth()+1));
				pq.add(m);
			}
		}

		return pq.poll();
	}

	@Override
	public int decideMove(Game currentBoard){
		comparator cmp = new comparator();
		pq = new PriorityQueue<move>(10,cmp);
		move bestMove = null;
		int depth = MAX_DEPTH;

		while((bestMove = getBestMove(currentBoard,depth--)) == null);
		return bestMove.getHead(bestMove).col();
	}

	public int eval(Game g, int col){
		int row = g.top(col),score = 0,maxConnect = 0, maxDir = 0;
		int player = g.getCurrentPlayer(), opponent = g.switchPlayer();

		for(int direction = 0; direction < ALL_DIRECTION; direction++) 
			maxConnect = Math.max(g.howManyConnect(row,col, player, direction), maxConnect);
		
		switch (maxConnect) { //assign scores (tentative values)
			case 0: score = 0; 		break; //shouldnt occur
			case 1: score = 0; 		break; //no other chips
			case 2: score = 50;		break; //2 in a row
			case 3: score = 100;	break;  //3 in a row
			case 4: score = WINNING_SCORE; break; //4 in a row i.e. win
		}

		for(int direction = 0; direction < ALL_DIRECTION; direction++) 
			maxConnect = Math.max(g.howManyConnect(row,col, opponent, direction), maxConnect);
		
		switch (maxConnect) { //assign scores (tentative values)
			case 0: score += 0; 		break; //shouldnt occur
			case 1: score += 0; 		break; //no other chips
			case 2: score += 100;		break; //2 in a row
			case 3: score += 250;	break;  //3 in a row
			case 4: score += 2*WINNING_SCORE; break; //4 in a row i.e. win
		}

		return score;
	}
}