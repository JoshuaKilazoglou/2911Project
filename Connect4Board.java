import java.util.ArrayList;
import java.util.List;

public class Connect4Board{
	public static final int CRADUIS = 35;
	public static final int INITIAL_SIDE_MARGIN = 15;
	public static final int TOP_MARGIN = 5;
	public static final int SIDE_MARGIN = 10;

	private int[][] board;
	private int X,Y;

	Connect4Board(int x,int y){
		this.X = x;
		this.Y = y;

		board = new int[x][y]; // Java automatically fills the matrix with 0
	}

	public int[][] getBoard() {
		return board;
	}
	
	public void addChecker(int x, int y, int player){
		if(x >= X || y >= Y || x < 0 || y < 0)
			return;
		board[x][y] = player;
	}

	public int whatsHere(int x, int y){
		return board[x][y];
	}

	public void deleteChecker(int x, int y){
		addChecker(x,y,0);
	}

	public void printBoard() {
		System.out.println("board");
		for(int i = X-1; i >= 0 ; i--){
			for(int j = 0; j < Y; j++)
				System.out.print(" " + board[i][j]);
			System.out.println();
		}
	}

	public void clearBoard(){
		board = new int[this.X][this.Y];
	}

	public boolean isBoardFull(){
		for(int[] row : this.board)
			for(int i : row)
				if(i == 0) 
					return false;
		return true;
	}

	public int connectRow(int x,int y,int player){
		boolean sideA = true,sideB = true;
		int count = 1;

		for(int i = 1; sideA || sideB; i++){
			if(sideA)
			 	if(y+i >= Y)
			 		sideA = false;
				else if(whatsHere(x,y+i) == player)
					count++;
				else
					sideA = false;

			if(sideB)
				if(y-i < 0)
					sideB = false;
				else if(whatsHere(x,y-i) == player)
					count++;
				else
					sideB = false;
		}

		return count;
	}

	public int connectCol(int x,int y,int player){
		boolean sideA = true,sideB = true;
		int count = 1;

		for(int i = 1; sideA || sideB; i++){
			if(sideA)
				if(x+i >= X)
					sideA = false;
				else if(whatsHere(x+i,y) == player)
					count++;
				else
					sideA = false;

			if(sideB)
				if(x-i < 0)
					sideB = false;
				else if(whatsHere(x-i,y) == player)
					count++;
				else
					sideB = false;
		}

		return count;
	}

	public int connectLDiag(int x,int y,int player){
		boolean sideA = true,sideB = true;
		int count = 1;

		for(int i = 1; sideA || sideB; i++){
			if(sideA)
				if (x+i >= X || y-i < 0)
					sideA = false;
				else if(whatsHere(x+i,y-i) == player)
					count++;
				else
					sideA = false;

			if(sideB)
				if(x-i < 0 || y+i >= Y)
					sideB = false;
				else if(whatsHere(x-i,y+i) == player)
					count++;
				else
					sideB = false;
		}

		return count;
	}

	public int connectRDiag(int x,int y,int player){
		boolean sideA = true,sideB = true;
		int count = 1;

		for(int i = 1; sideA || sideB; i++){
			if(sideA)
				if (x+i >= X || y+i >= Y)
					sideA = false;
				else if(whatsHere(x+i,y+i) == player)
					count++;
				else
					sideA = false;

			if(sideB)
				if(x-i < 0 || y-i < 0)
					sideB = false;
				else if(whatsHere(x-i,y-i) == player)
					count++;
				else
					sideB = false;
		}

		return count;
	}

	public boolean rowCheck(int x,int y, int player){
		if(connectRow(x,y,player) >= 4)
			return true;

		return false;
	}
	public boolean colCheck(int x,int y, int player){
		if(connectCol(x,y,player) >= 4)
			return true;

		return false;
	}

	// these are a bit mind fuck since we start from the bottom
	public boolean LdiagCheck(int x,int y, int player){
		if(connectLDiag(x,y,player) >= 4)
			return true;

		return false;
	}

	public boolean RdiagCheck(int x,int y, int player){
		if(connectRDiag(x,y,player) >= 4)
			return true;

		return false;
	}

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