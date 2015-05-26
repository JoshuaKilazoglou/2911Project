
public class AdvanceAI implements AI{
	private int[] result;
	AdvanceAI(){
		result = new int [7];
	}
	
	
	public int decideMove(Game g) {
		result = new int[7];
		evaluateMoves(g);
		int max = 0;
		for( int i = 0; i < 7;i++){
			if( result[i] > result[max]){
				max = i;
			}
		}
		return max;
	}

	public void evaluateMoves(Game g){
		for( int i = 0; i < g.COL; i++){
			//if the move is not valid, dont' concern it any more
			if ( !g.checkValidMove(i)){
				continue;
			}
			//if the move is valid than...do
			int top = g.top(i);
			//eval of advantage gained
			result[i] += rowEval(g,i) + colEval(g,i) + LdiagEval(g,i)+RdiagEval(g,i);
			//result[i] += dRowEval(g,i) + dCowEval(g,i) + dLdiagEval(g,i)+dRdiagEval(g,i);
			//eval of dis
			System.out.println(result[i]);
		}
	}
	private int rowEval(Game g, int y) {
		boolean sideA = true,sideB = true;
		int count = 1;
		int x = g.top(y);
		int countL = 1;
		int countR = 1;
		//System.out.println(x+" "+y);
		for(int i = 1; sideA || sideB; i++){
			
			if(sideA){
				
				if(y+i >= g.COL){
					sideA = false;
				}else if (countR == 4){
					sideA = false;
				}else if(g.whatsHere(x,y+i) == g.getCurrentPlayer()||g.whatsHere(x,y+i) == g.NOP ){
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
				}else if(g.whatsHere(x,y-i) == g.getCurrentPlayer()||g.whatsHere(x,y-i) == g.NOP){
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
		return count;
	}
	private int colEval(Game g, int y) {
		boolean sideA = true,sideB = true;
		int count = 1;
		int x = g.top(y);
		int countU = 1;
		int countD = 1;
		//System.out.println(x+" "+y);
		for(int i = 1; sideA || sideB; i++){
			
			if(sideA){
				
				if(x+i >= g.ROW){
					sideA = false;
				}else if (countU == 4){
					sideA = false;
				}else if(g.whatsHere(x+i,y) == g.getCurrentPlayer()||g.whatsHere(x+i,y) == g.NOP ){
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
				}else if(g.whatsHere(x-i,y) == g.getCurrentPlayer()||g.whatsHere(x-i,y) == g.NOP){
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
		return count;
	}
	private int LdiagEval(Game g, int y) {
		boolean sideA = true,sideB = true;
		int count = 1;
		int x = g.top(y);
		int countU = 1;
		int countD = 1;
		//System.out.println(x+" "+y);
		for(int i = 1; sideA || sideB; i++){
			
			if(sideA){
				
				if(x+i >= g.ROW || y-i < 0){
					sideA = false;
				}else if (countU == 4){
					sideA = false;
				}else if(g.whatsHere(x+i,y-i) == g.getCurrentPlayer()||g.whatsHere(x+i,y-i) == g.NOP ){
					countU++;
				}else{
					sideA = false;
				}
			}
			if(sideB){
				if(x-i < 0 || y+i >= g.COL){
					sideB = false;
				}else if (countD == 4){
					sideB = false;
				}else if(g.whatsHere(x-i,y+i) == g.getCurrentPlayer()||g.whatsHere(x-i,y+i) == g.NOP){
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
		return count;
	}
	
	private int RdiagEval(Game g, int y) {
		boolean sideA = true,sideB = true;
		int count = 1;
		int x = g.top(y);
		int countU = 1;
		int countD = 1;
		//System.out.println(x+" "+y);
		for(int i = 1; sideA || sideB; i++){
			
			if(sideA){
				
				if(x+i >= g.ROW || y+i >= g.COL){
					sideA = false;
				}else if (countU == 4){
					sideA = false;
				}else if(g.whatsHere(x+i,y+i) == g.getCurrentPlayer()||g.whatsHere(x+i,y+i) == g.NOP ){
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
				}else if(g.whatsHere(x-i,y-i) == g.getCurrentPlayer()||g.whatsHere(x-i,y-i) == g.NOP){
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
		return count;
	}
	
}
