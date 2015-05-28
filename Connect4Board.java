import java.util.ArrayList;
import java.util.List;

public class Connect4Board{
	public static final int CRADUIS = 35;
	public static final int INITIAL_SIDE_MARGIN = 15;
	public static final int TOP_MARGIN = 5;
	public static final int SIDE_MARGIN = 10;

	private int[][] board;
	private int X,Y;

	Connect4Board(int row,int col){
		this.X = row;
		this.Y = col;

		board = new int[row][col]; // Java automatically fills the matrix with 0
	}

	/**
	 * precondition: NULL
	 * postcondition: all
	 * invariant:board
	 * return the board in a matrix(array of array of int)
	 * @return board
	 */
	public int[][] getBoard() {
		return board;
	}
	
	/**
	 * add checker to the board
	 * precondition: 0<=row < g.row, 0<=col < g.column, player == 1,2
	 * postcondition: board[x][y] = player
	 * invariant:NULL
	 * @param row the row no.
	 * @param col the column no.
	 * @param player the player who add the checker
	 */
	public void addChecker(int row, int col, int player){
		if(row >= X || col >= Y || row < 0 || col < 0)
			return;
		board[row][col] = player;
	}

	/**
	 * return what is on the selected location(x,y) of the board
	 * (ie: player1's checker, player2's checker or nothing
	 * precondition: 0<=row < g.row, 0<=col < g.column
	 * postcondition: all
	 * invariant: NULL
	 * @param row the row no.
	 * @param col the column no.
	 * @return what is on the selected location of the board
	 */
	public int whatsHere(int row, int col){
		return board[row][col];
	}

	/**
	 * remove the checker from the board of the selected loaction(x,y)
	 * precondition: 0<=row < g.row, 0<=col < g.column
	 * postcondition: board[x][y] = 0
	 * invariant:NULL
	 * @param row the row no.
	 * @param col the column no.
	 */
	public void deleteChecker(int row, int col){
		addChecker(row,col,0);
	}


	/**
	 * print the board
	 * precondition:NULL
	 * postcondition:NULL
	 * invariant:board
	 */
	public void printBoard() {
		System.out.println("board");
		for(int i = X-1; i >= 0 ; i--){
			for(int j = 0; j < Y; j++)
				System.out.print(" " + board[i][j]);
			System.out.println();
		}
	}

	/**
	 * clear all the checker on the board
	 * precondition:null
	 * postcondition:board[x][y] =0 | 0<=x<row, 0<=y<column
	 */
	public void clearBoard(){
		board = new int[this.X][this.Y];
	}

	/**
	 * return true if the board is full, false otherwise
	 * precondition:NULL
	 * postcondition:NULL
	 * invariant:board
	 * @return true if the board is full, false otherwise
	 */
	public boolean isBoardFull(){
		for(int[] row : this.board)
			for(int i : row)
				if(i == 0) 
					return false;
		return true;
	}

	/**
	 * check the number of checkers for the row
	 * precondition: 0<=row < g.row, 0<=col < g.column, player == 1,2
	 * postcondition: board[x][y] = player
	 * invariant:NULL
	 * @param row the row no.
	 * @param col the column no.
	 * @param player the player who add the checker
	 * @return the number of connected checker on the row
	 */
	public int connectRow(int row,int col,int player){
		boolean sideA = true,sideB = true;
		int count = 1;

		for(int i = 1; sideA || sideB; i++){
			if(sideA)
			 	if(col+i >= Y)
			 		sideA = false;
				else if(whatsHere(row,col+i) == player)
					count++;
				else
					sideA = false;

			if(sideB)
				if(col-i < 0)
					sideB = false;
				else if(whatsHere(row,col-i) == player)
					count++;
				else
					sideB = false;
		}

		return count;
	}
	/**
	 * check the number of checkers for the column
	 * precondition: 0<=row < g.row, 0<=col < g.column, player == 1,2
	 * postcondition: board[x][y] = player
	 * invariant:NULL
	 * @param row the row no.
	 * @param col the column no.
	 * @param player the player who add the checker
	 * @return the number of connected checker on the col
	 */
	public int connectCol(int row,int col,int player){
		boolean sideA = true,sideB = true;
		int count = 1;

		for(int i = 1; sideA || sideB; i++){
			if(sideA)
				if(row+i >= X)
					sideA = false;
				else if(whatsHere(row+i,col) == player)
					count++;
				else
					sideA = false;

			if(sideB)
				if(row-i < 0)
					sideB = false;
				else if(whatsHere(row-i,col) == player)
					count++;
				else
					sideB = false;
		}

		return count;
	}
	
	/**
	 * check the number of checkers for left diagnal
	 * precondition: 0<=row < g.row, 0<=col < g.column, player == 1,2
	 * postcondition: board[x][y] = player
	 * invariant:NULL
	 * @param row the row no.
	 * @param col the column no.
	 * @param player the player who add the checker
	 * @return the number of connected checker on the l diagnal
	 */
	public int connectLDiag(int row,int col,int player){
		boolean sideA = true,sideB = true;
		int count = 1;

		for(int i = 1; sideA || sideB; i++){
			if(sideA)
				if (row+i >= X || col-i < 0)
					sideA = false;
				else if(whatsHere(row+i,col-i) == player)
					count++;
				else
					sideA = false;

			if(sideB)
				if(row-i < 0 || col+i >= Y)
					sideB = false;
				else if(whatsHere(row-i,col+i) == player)
					count++;
				else
					sideB = false;
		}

		return count;
	}
	
	/**
	 * check the number of checkers for right diagnal
	 * precondition: 0<=row < g.row, 0<=col < g.column, player == 1,2
	 * postcondition: board[x][y] = player
	 * invariant:NULL
	 * @param row the row no.
	 * @param col the column no.
	 * @param player the player who add the checker
	 * @return the number of connected checker on the r diagnal
	 */
	public int connectRDiag(int row,int col,int player){
		boolean sideA = true,sideB = true;
		int count = 1;

		for(int i = 1; sideA || sideB; i++){
			if(sideA)
				if (row+i >= X || col+i >= Y)
					sideA = false;
				else if(whatsHere(row+i,col+i) == player)
					count++;
				else
					sideA = false;

			if(sideB)
				if(row-i < 0 || col-i < 0)
					sideB = false;
				else if(whatsHere(row-i,col-i) == player)
					count++;
				else
					sideB = false;
		}

		return count;
	}

	/**
	 * Check if the number of checkers on the row results winning condition
	 * precondition: 0<=row < g.row, 0<=col < g.column, player == 1,2
	 * postcondition: board[x][y] = player
	 * invariant:NULL
	 * @param row the row no.
	 * @param col the column no.
	 * @param player the player who add the checker
	 * @return true for we can win, and false for we cant
	 */
	public boolean rowCheck(int row,int col, int player){
		if(connectRow(row,col,player) >= 4)
			return true;

		return false;
	}

	/**
	 * Check if the number of checkers on the col results winning condition
	 * precondition: 0<=row < g.row, 0<=col < g.column, player == 1,2
	 * postcondition: board[x][y] = player
	 * invariant:NULL
	 * @param row the row no.
	 * @param col the column no.
	 * @param player the player who add the checker
	 * @return true for we can win, and false for we cant
	 */
	public boolean colCheck(int row,int col, int player){
		if(connectCol(row,col,player) >= 4)
			return true;

		return false;
	}

	/**
	 * Check if the number of checkers on the left diagnal results winning condition
	 * precondition: 0<=row < g.row, 0<=col < g.column, player == 1,2
	 * postcondition: board[x][y] = player
	 * invariant:NULL
	 * @param row the row no.
	 * @param col the column no.
	 * @param player the player who add the checker
	 * @return true for we can win, and false for we cant
	 */
	public boolean LdiagCheck(int row,int col, int player){
		if(connectLDiag(row,col,player) >= 4)
			return true;

		return false;
	}

	/**
	 * Check if the number of checkers on the right diagnal results winning condition
	 * precondition: 0<=row < g.row, 0<=col < g.column, player == 1,2
	 * postcondition: board[x][y] = player
	 * invariant:NULL
	 * @param row the row no.
	 * @param col the column no.
	 * @param player the player who add the checker
	 * @return true for we can win, and false for we cant
	 */
	public boolean RdiagCheck(int row,int col, int player){
		if(connectRDiag(row,col,player) >= 4)
			return true;

		return false;
	}
	
	/**
	 * check the number of checkers connected in all directions
	 * precondition: 0<=row < g.row, 0<=col < g.column, player == 1,2. direction = 0,1,2,3
	 * postcondition: board[x][y] = player
	 * invariant:NULL
	 * @param row the row no.
	 * @param col the column no.
	 * @param player the player
	 * @param direction the direction
	 * @return the number connected on direction
	 */
	public int howManyConnect(int row,int col, int player, int direction){
		int connected = 0;
		switch(direction){
			case 0: 
				connected = this.connectRow(row,col,player);
				break;
			case 1:
				connected = this.connectCol(row,col,player);
				break;
			case 2:
				connected = this.connectLDiag(row,col,player);
				break;
			case 3:
				connected = this.connectRDiag(row,col,player);
				break;
		}

		return connected;
	}
}