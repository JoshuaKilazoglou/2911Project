import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;


class move extends node{
	private int depth = 0,heu = 0,player = 0;
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
	
	/**
	 * sets the player who did the move
	 * precondition: player = 1,2
	 * @param player the player who did the move
	 */
	public void setPlayer(int player){
		this.player = player;
	}

	/**
	 * sets the depth of the move
	 * precondition: d = 0..MAX_DEPTH
	 * @param d the depth of the move
	 */
	public void setDepth(int d){
		this.depth = d;
	}

	/**
	 * Sets the score value of the move
	 * @param h the score value
	 */
	public void setHeu(int h){
		heu = h;
	}

	/**
	 * get the depth
	 * @return the depth
	 */
	public int getDepth(){
		return depth;
	}

	/**
	 * get the previous move
	 * @return the previous move node
	 */
	public move prev(){
		return this.prev;
	}

	/**
	 * get the hueristic
	 * @return the heuristic
	 */
	public int getHue(){
		return heu;
	}

	/**
	 * get the player
	 * @return the player who did the move
	 */
	public int getPlayer(){
		return this.player;
	}

	/**
	 * get the head of the move
	 * @param prev
	 * @return the starting move
	 */
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
	private int[] scores;

	private class comparator implements Comparator<move>{
		@Override
		/**
		 * an implementation of the compare function
		 */
		public int compare(move a,move b){
			if(a.getDepth() != b.getDepth())
				return a.getDepth() - b.getDepth();
			
			return b.getHue() - a.getHue();
		}
	}

	/**
	 * refills the game with the moves inside the node
	 * precondition: game ! = NULL
	 * postcondition: game is fulled
	 * @param g the game object
	 * @param lastMove the last move
	 */
	public void reFill(Game g, node lastMove){
		if(lastMove == null)
			return;
		else
			reFill(g,lastMove.prev());

		g.makeMove(lastMove.col());
		lastMove = lastMove.prev();
	}
	
	/**
	 * Use dijsktra to search for all the possible moves and return the best move
	 * it updates and aggregate the score
	 * precondition: currentBoard != null,0 < maxDepth <= MAX_DEPTH
	 * postcondition: the score is aggregated
	 * invariant: the game board 
	 * @param currentBoard the game
	 * @param maxDepth the max depth to search
	 * @return a move object consisting the most optimal set of moves that could happen
	 */
	public move getBestMove(Game currentBoard,int maxDepth){
		int initialDepth = 0;
		Game g = currentBoard.clone();
		
		for(int col = 0; col < Game.COL; col++){
			if(g.checkValidMove(col)){
				int score = (MAGIC_NUMBER) * eval(g,col);
				move m = new move(col,null,score);
				m.setPlayer(g.getCurrentPlayer());
				pq.add(m);
				scores[m.getHead(m).col()] += score;
			}
		}

		if(maxDepth == 0)
			return pq.poll();

		while(!pq.isEmpty() && pq.peek().getDepth() < maxDepth){
			move bestMove = pq.poll();
			
			for(int col = 0; col < Game.COL; col++){	
				Game copy = currentBoard.clone();
				reFill(copy,bestMove);
//				copy.printGame();
				if(!copy.checkValidMove(col))
					continue;

				int score = (MAGIC_NUMBER - bestMove.getDepth()+1) * eval(copy,col) + bestMove.getHue();
				move m = new move(col,bestMove,score,(bestMove.getDepth()+1));
				m.setPlayer(copy.getCurrentPlayer());
				pq.add(m);

				scores[m.getHead(m).col()] += score;
			}
		}

		return pq.poll();
	}
	/**
	 * An implementation of decide Move for the AI
	 * precondition: currentBoard != NULL
	 * postcondition: a move is given
	 * invariant: currentBoard
	 * @param currentBoard the game 
	 * @return the best move
	 */
	@Override
	public int decideMove(Game currentBoard){
	/*	
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/
		comparator cmp = new comparator();
		pq = new PriorityQueue<move>(10,cmp);
		scores = new int[Game.COL];
		move bestMove = null;
		int depth = MAX_DEPTH;

		int col = checkInstantWinOrLose(currentBoard);
		if(col != Game.BADMOVE)
			return col;

		while((bestMove = getBestMove(currentBoard,depth--)) == null);

		Random generator = new Random();
		ArrayList<Integer> moves = new ArrayList<Integer>();

		int firstValid = 0,max = 0;
		for(;firstValid < Game.COL; firstValid++){
			if(currentBoard.checkValidMove(firstValid)){
				max = scores[firstValid];
				break;
			}
		}

		for(col = firstValid; col < Game.COL; col++)
			if(scores[col] > max && currentBoard.checkValidMove(col))
				max = scores[col];

		for(col = firstValid; col < Game.COL; col++)
			if(scores[col] == max && currentBoard.checkValidMove(col))
				moves.add(col);

		pq = null;
		return moves.get(generator.nextInt(moves.size()));
	}
	
	/**
	 * check if we can win or lose in one move
	 * precondition: currrentBoard != NULL
	 * postcondition: null
	 * invariant: currentBoard
	 * @param currentBoard the game
	 * @return the row if we can win or lose, or -1 is we can't
	 */
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

	/**
	 * evaluate the game state 
	 * @param g the game
	 * @param col the column to simlulate
	 * @return the evaluation score
	 */
	public int eval(Game g, int col){
		int row = g.top(col),score = 0,maxConnect = 0, maxDir = 0;
		int player = g.getCurrentPlayer(), opponent = g.switchPlayer();

		for(int direction = 0; direction < ALL_DIRECTION; direction++) 
			maxConnect = Math.max(g.howManyConnect(row,col, player, direction), maxConnect);
		
		switch (maxConnect) { //assign scores (tentative values)
			case 0: score = 0; 		break; //shouldnt occur
			case 1: score = 0; 		break; //no other chips
			case 2: score = (player == AI ? 50 : -50);		break; //2 in a row
			case 3: score = (player == AI ? 100 : -100); 	break;  //3 in a row
			case 4: score = (player == AI ? WINNING_SCORE : -WINNING_SCORE);  break; //4 in a row i.e. win
		}
		
		for(int direction = 0; direction < ALL_DIRECTION; direction++) 
			maxConnect = Math.max(g.howManyConnect(row,col, opponent, direction), maxConnect);
		
		switch (maxConnect) { //assign scores (tentative values)
			case 0: score += 0; 		break; //shouldnt occur
			case 1: score += 0; 		break; //no other chips
			case 2: score += (player == AI ? 100 : -100);		break; //2 in a row
			case 3: score += (player == AI ? 250 : -250); 	break;  //3 in a row
			case 4: score += (player == AI ? 2*WINNING_SCORE : -2*WINNING_SCORE);  break; //4 in a row i.e. win
		}

		return score;
	}
}