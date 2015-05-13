import java.util.ArrayList;
import java.util.List;

public class Board {
	private int[][] board;
	private int X,Y;

	Board(int x,int y){
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
}


