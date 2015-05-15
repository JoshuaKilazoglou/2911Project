

public class Game{
	public static final int P1 = 1, P2 = 2, NOP = 0;  
	private int player; // 1 for player 1 and 2 for player 2.
	private Board board;
	private node lastmove; // undo redo element
	private int state; // 0 for normal state, 2 for someone won

	Game(){
		player = 1;
		state = 0;
		board = new Board(6,7);
		lastmove = new node(); // undo redo element
	}

	// returns the available row to input. e.g. if the 1st row has no checker return this row
	public int top(int x){
		int i = 0;
		while(i < 6 && board.whatsHere(i,x) != NOP)
			i++;
		return i;
	}

	// switch the player
	public int switchPlayer(){
		return ((player == P1) ? P2 : P1);
	}

	// check if someone won the game, if someone won, the state is 2, if the board is full the state is 1
	// else the state is 0
	public int getState(){
		return state;
	}

	// restarts the game by setting everything to start state
	public void restartGame(){
		board.clearBoard();
		state = 0;
		lastmove = new node(); // we might want to clear the record for the actual undo, redo
		player = 1;
	}

	/**
	 * make the move and check if the game has been won, if it has the game state is set to 2
	 * also switch the player and returns the top row(the row where the checker is added)
	 * precondition:
	 * postcondition:return 1 or (return 0,2 and move add to the board)
	 * @param x
	 * @return
	 */
	public int makeMove(int x){
			//x out of bound or the column is full thus no move is made or there's already another player
			int y = top(x);
			board.addChecker(y,x,player);
			if(win(y,x,player))
				state = 2;
			if(board.isBoardFull())
				state = 1;

			updateLastMove(y,x);
			player = switchPlayer();
			return y;
	}

	// check if the move is legal, so the column is not out of bound, the column is not full
	// and the top element is not occupied
	public boolean checkValidMove(int x){
		if(x < 0 || x >= 7 || board.whatsHere(5,x) != NOP 
			|| board.whatsHere(top(x),x) != NOP)
			return false;
		return true;
	}

	public void printGame(){
		board.printBoard();
	}

	public int getCurrentPlayer(){
		return player;
	}

	// undo redo function
	// add a move to the linked list
	private void updateLastMove(int x,int y){
		node newmove = new node(x,y,player,null,null);
		lastmove.attach(newmove);
		lastmove = newmove;
	}

	// check if a checker inserted in row x, col y by player would win the game
	private boolean win(int x, int y, int player) {
		return board.rowCheck(x,y,player) || board.colCheck(x,y,player) || board.LdiagCheck(x,y,player) || board.RdiagCheck(x,y,player);
	}

	// undo redo function
	// undo the move, the player is also changed, the checker on the board is deleted
	// if undo success return true, if not(the player nevered player a move, the player have undoed all his move)
	public boolean undo(){
		if(lastmove.x() == -1)
			return false;

		board.deleteChecker(lastmove.x(),lastmove.y());
		player = switchPlayer();
		lastmove = lastmove.prev();
		return true;
	}

	// undo redo function
	// redo the move, the player is changed, the checker is added to the board
	// return true for success and false for not
	public boolean redo(){
		if(lastmove == null || lastmove.next() == null)
			return false;

		lastmove = lastmove.next();
		board.addChecker(lastmove.x(),lastmove.y(),player);
		player = switchPlayer();
		return true;
	}

	public Board getBoard(){
		return board;
	}

	// returns the checker at row x, col y, 0 for nothing, 1 for p1 and 2 for p2
	public int whatsHere(int x,int y){
		return board.whatsHere(x,y);
	}
}