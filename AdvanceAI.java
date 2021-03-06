
public class AdvanceAI implements AI{
	private int[] result;
	AdvanceAI(){
		result = new int [7];
	}


	/**
	 * decided move 
	 * precondition:NULL
	 * postcondition: return maximum number of the steps
	 * invariant:Null
	 * @param g game
	 * @return move
	 */
	public int decideMove(Game g) {
		result = new int[7];
		//evaluateMoves(g);
		//if one move win
		for( int i = 0; i < 7;i++){
			Game temp = g.clone();
			if ( !temp.checkValidMove(i)){
				continue;
			}
			temp.makeMove(i);
			if (temp.getState() == temp.GAMESET){
				return i;
			}
			result[i] = boardEval(temp,temp.getCurrentPlayer());
		}
		//eval(g, 1);
		int firstValid = 0,max = 0;
		for(;firstValid < Game.COL; firstValid++)
			if(g.checkValidMove(firstValid)){
				max = firstValid;
				break;
			}

		for( int i = firstValid; i < Game.COL;i++){
			if( g.checkValidMove(i) && result[i] > result[max]){
				max = i;
			}
		}
		return max;
	}
	

	/**
	 * Evaluate row 
	 * @param g game
	 * precondition:NULL
	 * postcondition: return x | 0<x<row ||　0< top(x) <col
	 * invariant:Null
	 * @param x row no
	 * @param y column no
	 * @param player player
	 * @return return evaluation of row
	 */
	private int rowEval(Game g,int x, int y,int player) {
		boolean sideA = true,sideB = true;
		int count = 1;
		//int x = g.top(y);
		int countL = 1;
		int countR = 1;
		//System.out.println(x+" "+y);
		for(int i = 1; sideA || sideB; i++){

			if(sideA){

				if(y+i >= Game.COL){
					sideA = false;
				}else if (countR == 4){
					sideA = false;
				}else if(g.whatsHere(x,y+i) == player||g.whatsHere(x,y+i) == g.NOP ){
					countR++;
				}else{
					sideA = false;
				}
			}
			if(sideB){
				if(y-i < 0){
					sideB = false;
				}else if (countL == 4){
					sideB = false;
				}else if(g.whatsHere(x,y-i) == player||g.whatsHere(x,y-i) == g.NOP){
					countL++;
				}else{
					sideB = false;
				}
			}
		}
		count = countL + countR -4;
		if ((count)<0){
			count = 0;
		}
		if ( count != 0){
			sideA = true;
			sideB = true;
			//int x = g.top(y);
			countL = 1;
			countR = 1;
			//System.out.println(x+" "+y);
			for(int i = 1; sideA || sideB; i++){

				if(sideA){

					if(y+i >= Game.COL){
						sideA = false;
					}else if (countR == 4){
						sideA = false;
					}else if(g.whatsHere(x,y+i) == player){
						countR+=30;
					}else{
						sideA = false;
					}
				}
				if(sideB){
					if(y-i < 0){
						sideB = false;
					}else if (countL == 4){
						sideB = false;
					}else if(g.whatsHere(x,y-i) == player){
						countL+=30;
					}else{
						sideB = false;
					}
				}
			}
		}
		count += countL + countR;
		return count;
	}
	
	/**
	 * Evaluate column
	 * @param g game
	 * precondition:NULL
	 * postcondition: return x | 0<x<row ||　0< top(x) <col
	 * invariant:Null
	 * @param x row no
	 * @param y column no
	 * @param player player
	 * @return return evaluation of column
	 */
	private int colEval(Game g,int x, int y,int player) {
		boolean sideA = true,sideB = true;
		int count = 1;
		//int x = g.top(y);
		int countU = 1;
		int countD = 1;
		//System.out.println(x+" "+y);
		for(int i = 1; sideA || sideB; i++){

			if(sideA){

				if(x+i >= Game.ROW){
					sideA = false;
				}else if (countU == 4){
					sideA = false;
				}else if(g.whatsHere(x+i,y) == player||g.whatsHere(x+i,y) == g.NOP ){
					countU++;
				}else{
					sideA = false;
				}
			}
			if(sideB){
				if(x-i < 0){
					sideB = false;
				}else if (countD == 4){
					sideB = false;
				}else if(g.whatsHere(x-i,y) == player||g.whatsHere(x-i,y) == g.NOP){
					countD++;
				}else{
					sideB = false;
				}
			}
		}
		count = countU + countD -4;
		if ((count)<0){
			count = 0;
		}
		if ( count != 0){
			sideA = true;
			sideB = true;
			//int x = g.top(y);
			countU = 1;
			countD = 1;
			//System.out.println(x+" "+y);
			for(int i = 1; sideA || sideB; i++){

				if(sideA){

					if(x+i >= Game.ROW){
						sideA = false;
					}else if (countU == 4){
						sideA = false;
					}else if(g.whatsHere(x+i,y) == player){
						countU+=10;
					}else{
						sideA = false;
					}
				}
				if(sideB){
					if(x-i < 0){
						sideB = false;
					}else if (countD == 4){
						sideB = false;
					}else if(g.whatsHere(x-i,y) == player){
						countD+=10;
					}else{
						sideB = false;
					}
				}
			}
		}
		count += countU + countD;
		return count;
	}
	
	/**
	 * Evaluate left diagonal 
	 * @param g game
	 * precondition:NULL
	 * postcondition: return x | 0<x<row ||　0< top(x) <col
	 * invariant:Null
	 * @param x row no
	 * @param y column no
	 * @param player player
	 * @return return evaluation of left diagonal
	 */
	private int LdiagEval(Game g,int x, int y, int player) {
		boolean sideA = true,sideB = true;
		int count = 1;
		//int x = g.top(y);
		int countU = 1;
		int countD = 1;
		//System.out.println(x+" "+y);
		for(int i = 1; sideA || sideB; i++){

			if(sideA){

				if(x+i >= Game.ROW || y-i < 0){
					sideA = false;
				}else if (countU == 4){
					sideA = false;
				}else if(g.whatsHere(x+i,y-i) == player||g.whatsHere(x+i,y-i) == g.NOP ){
					countU++;
				}else{
					sideA = false;
				}
			}
			if(sideB){
				if(x-i < 0 || y+i >= Game.COL){
					sideB = false;
				}else if (countD == 4){
					sideB = false;
				}else if(g.whatsHere(x-i,y+i) == player||g.whatsHere(x-i,y+i) == g.NOP){
					countD++;
				}else{
					sideB = false;
				}
			}
		}
		count = countU + countD -4;
		if ((count)<0){
			count = 0;
		}
		if ((count)<0){
			count = 0;
		}
		if ( count != 0){
			sideA = true;
			sideB = true;
			countU = 1;
			countD = 1;
			for(int i = 1; sideA || sideB; i++){

				if(sideA){

					if(x+i >= Game.ROW || y-i < 0){
						sideA = false;
					}else if (countU == 4){
						sideA = false;
					}else if(g.whatsHere(x+i,y-i) == player ){
						countU+=10;
					}else{
						sideA = false;
					}
				}
				if(sideB){
					if(x-i < 0 || y+i >= Game.COL){
						sideB = false;
					}else if (countD == 4){
						sideB = false;
					}else if(g.whatsHere(x-i,y+i) == player){
						countD+=10;
					}else{
						sideB = false;
					}
				}
			}
		}
		return count;
	}
	
	/**
	 * Evaluate right diagonal 
	 * @param g game
	 * precondition:NULL
	 * postcondition: return x | 0<x<row ||　0< top(x) <col
	 * invariant:Null
	 * @param x row no
	 * @param y column no
	 * @param player player
	 * @return return evaluation of right diagonal
	 */
	private int RdiagEval(Game g,int x, int y,int player) {
		boolean sideA = true,sideB = true;
		int count = 1;
		//int x = g.top(y);
		int countU = 1;
		int countD = 1;
		//System.out.println(x+" "+y);
		for(int i = 1; sideA || sideB; i++){

			if(sideA){

				if(x+i >= Game.ROW || y+i >= Game.COL){
					sideA = false;
				}else if (countU == 4){
					sideA = false;
				}else if(g.whatsHere(x+i,y+i) == player||g.whatsHere(x+i,y+i) == g.NOP ){
					countU++;
				}else{
					sideA = false;
				}
			}
			if(sideB){
				if(x-i < 0 || y-i < 0){
					sideB = false;
				}else if (countD == 4){
					sideB = false;
				}else if(g.whatsHere(x-i,y-i) == player||g.whatsHere(x-i,y-i) == g.NOP){
					countD++;
				}else{
					sideB = false;
				}
			}
		}
		count = countU + countD -4;
		if ((count)<0){
			count = 0;
		}
		if ( count != 0){
			sideA = true;
			sideB = true;
			countU = 1;
			countD = 1;
			for(int i = 1; sideA || sideB; i++){

				if(sideA){

					if(x+i >= Game.ROW || y+i >= Game.COL){
						sideA = false;
					}else if (countU == 4){
						sideA = false;
					}else if(g.whatsHere(x+i,y+i) == player ){
						countU+=10;
					}else{
						sideA = false;
					}
				}
				if(sideB){
					if(x-i < 0 || y-i < 0){
						sideB = false;
					}else if (countD == 4){
						sideB = false;
					}else if(g.whatsHere(x-i,y-i) == player){
						countD+=10;
					}else{
						sideB = false;
					}
				}
			}
		}
		return count;
	}

	/**
	 * return board evaluation
	 * precondition:NULL
	 * postcondition: return x | 0<x<row ||　0< top(x) <col
	 * invariant:Null
	 * @param g game
	 * @param player player
	 * @return evaluation
	 */
	private int boardEval(Game g, int player){
		int re =0;
		for (int i = 0; i < 7; i++){
			for(int j =0; j < 6; j++){
				if (g.whatsHere(j, i) == player)
				re +=pointEval(g,j,i,player);
			}
		}
		return re;
	}

	/**
	 * return point evaluation
	 * precondition:NULL
	 * postcondition: return x | 0<x<row ||　0< top(x) <col
	 * invariant:Null
	 * @param g game
	 * @param i row no
	 * @param j column no
	 * @param player player
	 * @return evaluation
	 */
	private int pointEval(Game g,int i, int j, int player) {
		int opponent = g.switchPlayer();
		return  -rowEval(g,i,j,player) - colEval(g,i,j,player) 
				- LdiagEval(g,i,j,player)-RdiagEval(g,i,j,player)
				+ rowEval(g,i,j,opponent) + colEval(g,i,j,opponent) 
				+ LdiagEval(g,i,j,opponent)+RdiagEval(g,i,j,opponent);
		
	}
}
