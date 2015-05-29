/**
 * a data structure to hold the record of steps
 */
public class node implements Cloneable{
	private int x,y,gameState=0;
	private node prev,next;
	
	node(){
		this.x = -1;
		this.y = -1;
		this.prev = null;
		this.next = null;
	}
	
	node(int x,int y,node prev, node next){
		this.x = x;
		this.y = y;
		this.prev = prev;
		this.next = next;
	}
	/**
	 * set the game state of the node
	 * @param x the game state to be assigned
	 */
	void setGameState(int x){
		gameState = x;
	}
	
	/**
	 * attach the next move
	 * @param next the next move
	 */
	void attach(node next){
		this.next = next;
		next.prev = this;
	}
	
	/**
	 * get the row of this move
	 * @return int
	 */
	public int row(){ 
		return this.x;
	}
	/**
	 * get the col of this move
	 * @return int
	 */
	public int col(){
		return this.y;
	}
	/**
	 * get the previous move
	 * @return int
	 */
	public node prev(){ 
		return this.prev;
	}
	
	/**
	 * get the next move
	 * @return int
	 */
	public node next(){
		return this.next;
	}
	
	/**
	 * get the game state
	 * @return int
	 */
	public int getState(){
		return this.gameState;
	}
}