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
	
	public void setPlayer(int player){
		this.player = player;
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

	public int getPlayer(){
		return this.player;
	}

	public move getHead(move prev){
		if(prev.prev() == null)
			return prev;
		else
			return getHead(prev.prev());
	
	}
}

public class DumbAI implements AI{
	final static int MAX_DEPTH = 3;
	final static int MAGIC_NUMBER = 5;
	final static int ALL_DIRECTION = 4;
	static final int WINNING_SCORE = 1000; //probably worth changing at some point
	final static int AI = 2, HUMAN = 1;
	PriorityQueue<move> pq;
	private int[] scores;

	private class comparator implements Comparator<move>{
		@Override
		public int compare(move a,move b){
			if(a.getDepth() != b.getDepth())
				return a.getDepth() - b.getDepth();
			
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
				copy.printGame();
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

	@Override
	public int decideMove(Game currentBoard){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		comparator cmp = new comparator();
		pq = new PriorityQueue<move>(10,cmp);
		scores = new int[Game.COL];
		move bestMove = null;
		int depth = MAX_DEPTH;

		int col = checkInstantWinOrLose(currentBoard);
		if(col != -1)
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

		
/*
		moves.add(bestMove.getHead(bestMove).col());
		printMoveDetail(bestMove);
		System.out.println();
		while(pq.peek().getHue() >= bestMove.getHue()){
			move goodMoves = pq.poll();
			printMoveDetail(goodMoves);
			System.out.println();
			moves.add(goodMoves.getHead(goodMoves).col());
		}

*/
		pq = null;
		return moves.get(generator.nextInt(moves.size()));
//		return bestMove.getHead(bestMove).col();
	}

	public int checkInstantWinOrLose(Game currentBoard){
		for(int col = 0; col < Game.COL; col++){
			if(!currentBoard.checkValidMove(col))
				continue;
			int row = currentBoard.top(col);
			if(currentBoard.win(row,col,AI) || currentBoard.win(row,col,HUMAN))
				return col;
		}
		return -1;
	}

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
			case 4: score += (player == AI ? WINNING_SCORE : -WINNING_SCORE);  break; //4 in a row i.e. win
		}

		return score;
	}

	public void printMoveDetail(move m){
		if(m == null)
			return;
		else
			printMoveDetail(m.prev());

		System.out.print("(Player: " + m.getPlayer() + " Col: " + m.col() + ", Hue: " + m.getHue() + ", Depth: " + m.getDepth() + ") -> ");
	}
}