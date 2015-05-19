public class Game{
	public static final int P1 = 1, P2 = 2, NOP = 0;  
	public static final int ROW = 6,COL = 7;
	private int player; // 1 for player 1 and 2 for player 2.
	private Connect4Board board;
	private node lastmove; // undo redo element
	private int state; // 0 for normal state, 2 for someone won

	Game(){
		player = 1;
		state = 0;
		board = new Connect4Board(ROW,COL);
		lastmove = new node(); // undo redo element
	}

	// returns the available row to input. e.g. if the 1st row has no checker return this row
	public int top(int x){
		int i = 0;
		while(i < ROW && board.whatsHere(i,x) != NOP)
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
		if(x < 0 || x >= COL || board.whatsHere(ROW-1,x) != NOP 
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
		node newmove = new node(x,y,null,null);
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

	public Connect4Board getBoard(){
		return board;
	}

	// returns the checker at row x, col y, 0 for nothing, 1 for p1 and 2 for p2
	public int whatsHere(int x,int y){
		return board.whatsHere(x,y);
	}

	// returns how many checkers of "player" are connected, "direction" is the direction you want to check
	// 0 for row, 1 for col, 2 for left diagnal, 3 for right diagnal
	public int howManyConnect(int row,int col, int player, int direction){
		int connected = 0;
		switch(direction){
			case 0: 
				connected = board.connectRow(row,col,player);
				break;
			case 1:
				connected = board.connectCol(row,col,player);
				break;
			case 2:
				connected = board.connectLDiag(row,col,player);
				break;
			case 3:
				connected = board.connectRDiag(row,col,player);
				break;
		}

		return connected;
	}

	// get UI column axis in pixel without margin to the side of the window
	public static int getX(int x){
		return x*Connect4Board.CRADUIS*2 + x*Connect4Board.SIDE_MARGIN*2 + Connect4Board.INITIAL_SIDE_MARGIN;
	}

	// get UI row axis in pixel without margin to the top of the window
	public static int getY(int y){
		return y*Connect4Board.CRADUIS*2 + y*Connect4Board.TOP_MARGIN*2 + Connect4Board.TOP_MARGIN;
	}

	// gets the virtual/backend board col index
	// if the click was not in a circle return -1;
	public static int getCol(int width, double x){
		for(int i = 0; i < COL; i++){
			int col = getX(i)+width;
			if(x >= col && x <= col+Connect4Board.CRADUIS*2)
//				return (int)Math.floor((x-Connect4Board.INITIAL_SIDE_MARGIN)/(Connect4Board.CRADUIS*2+Connect4Board.SIDE_MARGIN*2));
				return i;
		}
		return -1;
	}
}