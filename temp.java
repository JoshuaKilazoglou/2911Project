import java.util.Scanner;

public class temp {
	public static void test1(){
		Game g = new Game();
		System.out.println(g.makeMove(1));
		System.out.println(g.makeMove(1));
		System.out.println(g.makeMove(1));
		System.out.println(g.makeMove(1));
		System.out.println(g.makeMove(1));
		System.out.println(g.makeMove(2));
		System.out.println(g.makeMove(3));
		System.out.println(g.makeMove(5));
		System.out.println(g.makeMove(4));
		System.out.println(g.makeMove(4));
		System.out.println(g.makeMove(3));
		System.out.println(g.makeMove(3));
		System.out.println(g.makeMove(2));
		//System.out.println(g.makeMove(2,2));
		//System.out.println(g.makeMove(1,2));
		System.out.println(g.makeMove(6));
		System.out.println(g.makeMove(6));
		System.out.println(g.makeMove(6));

		System.out.println(g.makeMove(5));
		System.out.println(g.makeMove(6));
		System.out.println(g.makeMove(5));
		g.printGame();			
	}

	public static void main(String[] args){
		System.out.println("Enter the column that you want to insert the checker\n" +
						   "Starting from the left is 0 and ends at 7.\n" +
						   "After insertion You will be given the state of the game and the new layout\n" + 
						   "0 for nothing particular, 1 for failed insertion and 2 for someone won\n" +
						   "input -1 to exit\n");

		Scanner input = new Scanner(System.in);
		Game g = new Game();
		g.printGame();
		int x = Integer.parseInt(input.next());
		while(x != -1){
			if(x == 7)
				System.out.println("The game state is " + g.undo());
			else if(x == 8)
				System.out.println("The game state is " + g.redo());
			else
				System.out.println("The game state is " + g.makeMove(x));
			
			g.printGame();
			x = Integer.parseInt(input.next());
		} 		
	}
}
