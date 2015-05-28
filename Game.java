public class Game{
	public static final int P1 = 1, P2 = 2, NOP = 0;  
	public static final int ROW = 6,COL = 7;
	public static int GAMESET = 2, BOARDFULL = 1, NAD = 0; // nothing abnormal deteced
	public static int LEFT = 0, RIGHT = 1;

	private int player; // 1 for player 1 and 2 for player 2.
	private Connect4Board board;
	private node lastmove; // undo redo element
	private int state; // 0 for normal state, 2 for someone won
	
	Game(){
		player = P1;
		state = NAD;
		board = new Connect4Board(ROW,COL);
		lastmove = new node(); // undo redo element
	}

	// returns the available row to input. e.g. if the 1st row has no checker return this row
	/**
	 * returns the available row to input. e.g. if the 1st row is the first row 
	 * has no checker return this row
	 * precondition: 0<col<7
	 * postcondition:0<i<6
	 * invariant: NULL
	 * @param col column no.
	 * @return top value
	 */
	public int top(int col){
		int i = 0;
		while(i < ROW && board.whatsHere(i,col) != NOP)
			i++;
		return i;
	}

	// switch the player
	/**
	 * get the non active player
	 * precondition:NULL
	 * postcondition:return player != current player
	 * invariant:NULL
	 * @return non active player
	 */
	public int switchPlayer(){
		return ((player == P1) ? P2 : P1);
	}

	/**check if someone won the game, if someone won, the state is 2, if the board is full the state is 1
	 * else the state is 0
	 * precondition: NULL
	 * postcondition: NULL
	 * invariant:NULL
	 * @return state
	 */
	public int getState(){
		return state;
	}

	// restarts the game by setting everything to start state
	/**
	 * restart game
	 * precondition: NULL
	 * postcondition: board[x][y] =0,player = P1,lastmove[z] =0,state = NAD | 0 <= x < 7 , 0<=y<7 ,0<=z
	 * invariant: NULL
	 */
	public void restartGame(){
		board.clearBoard();
		state = NAD;
		lastmove = new node(); // we might want to clear the record for the actual undo, redo
		player = P1;
	}

	/**
	 * add checker to the board
	 * precondition:0<=col < g.column, player == 1,2
	 * postcondition: board[top(column)][column] = current player
	 * invariant:NULL
	 * @param col the column no.
	 */
	public int makeMove(int col){
			//x out of bound or the column is full thus no move is made or there's already another player
			int row = top(col);
			board.addChecker(row,col,player);
			if(win(row,col,player))
				state = GAMESET;
			if(board.isBoardFull())
				state = BOARDFULL;

			updateLastMove(row,col);
			player = switchPlayer();
			return row;
	}

	// check if the move is legal, so the column is not out of bound, the column is not full
	// and the top element is not occupied
	/**
	 * check if the move is legal, so the column is not out of bound, the column is not full
	 * and the top element is not occupied
	 * precondition:0<=col < g.column, player == 1,2
	 * postcondition: null
	 * invariant:NULL
	 * @param col column no.
	 * @return if legal
	 */
	public boolean checkValidMove(int col){
		if(col < 0 || col >= COL || board.whatsHere(ROW-1,col) != NOP 
			|| board.whatsHere(top(col),col) != NOP)
			return false;
		return true;
	}

	/**
	 * print game board
	 * precondition:NULL
	 * postcondition:NULL
	 * invariant: NULL
	 */
	public void printGame(){
		board.printBoard();
	}

	/**
	 * return current active player
	 * precondition:NULL
	 * postcondition:NULL
	 * invariant: NULL
	 * @return player
	 */
	public int getCurrentPlayer(){
		return player;
	}

	// undo redo function
	/** 
	 * add a move to the linked list
	 * precondition:0<=col < g.column, player == 1,2
	 * postcondition: node add to the lastmove
	 * invariant:NULL
	 * @param row row no.
	 * @param col column no.
	 */
	private void updateLastMove(int row,int col){
		node newmove = new node(row,col,null,null);
		newmove.setGameState(state);
		lastmove.attach(newmove);
		lastmove = newmove;
	}

	// check if a checker inserted in row x, col y by player would win the game
	/**
	 * check if a checker inserted in row x, col y by player would win the game
	 * precondition:0<=col < g.column, player == 1,2
	 * postcondition: all
	 * invariant:NULL
	 * @param row row no
	 * @param col col no
	 * @param player player
	 * @return win or not
	 */
	public boolean win(int row, int col, int player) {
		return board.rowCheck(row,col,player) || board.colCheck(row,col,player) || board.LdiagCheck(row,col,player) || board.RdiagCheck(row,col,player);
	}

	// undo redo function
	// undo the move, the player is also changed, the checker on the board is deleted
	// if undo success return true, if not(the player nevered player a move, the player have undoed all his move)
	/**
	 * undo the last move
	 * precondition:null
	 * postcondition:last move undo
	 * invariant: lastmove
	 * @return return true if undo success
	 */
	public boolean undo(){
		if(lastmove.row() == -1)
			return false;

		board.deleteChecker(lastmove.row(),lastmove.col());
		player = switchPlayer();
		lastmove = lastmove.prev();
		state = lastmove.getState();
		return true;
	}

	/**
	 * redo the last move
	 * redo the move, the player is changed, the checker is added to the board
	 * return true for success and false for not
	 * precondition:null
	 * postcondition:last move undo
	 * invariant: lastmove
	 * @return return true if redo success
	 */
	public boolean redo(){
		if(lastmove == null || lastmove.next() == null)
			return false;

		lastmove = lastmove.next();
		board.addChecker(lastmove.row(),lastmove.col(),player);
		state = lastmove.getState();
		player = switchPlayer();
		return true;
	}

	public Connect4Board getBoard(){
		return board;
	}

	/**
	 * returns the checker at row x, col y, 0 for nothing, 1 for p1 and 2 for p2
	 * precondition: 0<=row < g.row, 0<=col < g.column
	 * postcondition: NULL
	 * invariant: NULL
	 * @param row row no.
	 * @param col column no.
	 * @return player
	 */
	// 
	public int whatsHere(int row,int col){
		return board.whatsHere(row,col);
	}

	/**
	 * returns how many checkers of "player" are connected, "direction" is the direction you want to check
	 * 0 for row, 1 for col, 2 for left diagnal, 3 for right diagnal
	 * precondition: 0<=row < g.row, 0<=col < g.column,direction = 1,2,3,4
	 * postcondition: NULL
	 * invariant: NULL
	 * @param row row no.
	 * @param col column no.
	 * @param player player
	 * @param direction direction no.
	 * @return Connected number
	 */
	public int howManyConnect(int row,int col, int player, int direction){
		if(row >= ROW || col >= COL)
			return 0;

		return board.howManyConnect(row,col,player,direction);
	}
	
	/**
	 * get UI column axis in pixel without margin to the side of the window
	 * precondition: 0<=col < g.col
	 * postcondition: NULL
	 * invariant:NULL
	 * @param col column no.
	 * @return UI column axis in pixel without margin to the side of the window
	 */
	public static int getX(int col){
		return col*Connect4Board.CRADUIS*2 + col*Connect4Board.SIDE_MARGIN*2 + Connect4Board.INITIAL_SIDE_MARGIN;
	}

	// get UI row axis in pixel without margin to the top of the window
	/**
	 * get UI row axis in pixel without margin to the side of the window
	 * precondition: 0<=col < g.rcol
	 * postcondition: NULL
	 * invariant:NULL
	 * @param row row no.
	 * @return UI column axis in pixel without margin to the side of the window
	 */
	public static int getY(int row){
		return row*Connect4Board.CRADUIS*2 + row*Connect4Board.TOP_MARGIN*2 + Connect4Board.TOP_MARGIN;
	}

	// gets the virtual/backend board col index
	// if the click was not in a circle return -1;
	/**
	 * gets the virtual/backend board col index
	 * if the click was not in a circle return -1;
	 * precondition: 0<=col < g.rcol
	 * postcondition: NULL
	 * invariant: width,col
	 * @param width width no.
	 * @param col col no.
	 * @return column no
	 */
	public static int getCol(int width, double xcol){
		for(int i = 0; i < COL; i++){
			int col = getX(i)+width;
			if(xcol >= col && xcol <= col+Connect4Board.CRADUIS*2)
//				return (int)Math.floor((x-Connect4Board.INITIAL_SIDE_MARGIN)/(Connect4Board.CRADUIS*2+Connect4Board.SIDE_MARGIN*2));
				return i;
		}
		return -1;
	}
	
	/**
	 * return last move
	 * precondition: NULL
	 * postcondition:NULL
	 * invariant: NULL
	 * @return last move
	 */
	public node getLastMove(){
		if(lastmove.row() == -1)
			return null;
		return lastmove;
	}
	
	/**
	 * set active player to player
	 * precondition: player = 1,2
	 * postcondition:g.player = player
	 * invariant: NULL
	 */
	public void setPlayer(int player){
		this.player = player;
	}
	
	/**
	 * set active state to state
	 * precondition: state = 0,1,2
	 * postcondition:g.state = state
	 * invariant: NULL
	 */
	public void setState(int state){
		this.state = state;
	}
	
	/**
	 * clone the game
	 * precondition: NULL
	 * postcondition: copy of game
	 * Invariant: game
	 * return: copy of game
	 */
	public Game clone(){
		Game g = new Game();
		Connect4Board bd = g.getBoard();
		for(int i = 0; i < ROW; i++)
			for(int j = 0; j < COL; j++)
				bd.addChecker(i, j, this.whatsHere(i, j));
		g.setPlayer(this.player);
		g.setState(this.state);
		return g;
	}
	
	@Override
	/**
	 * if it's equal
	 * return if eual return true else false
	 */
	public boolean equals(Object o){		
		for(int i = 0; i < ROW; i++)
			for(int j = 0; j < COL; j++)
				if(this.board.whatsHere(i, j) != ((Game) o).whatsHere(i,j))
					return false;
		return true;
	}
}