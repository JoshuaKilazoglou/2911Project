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
	
	void setGameState(int x){
		gameState = x;
	}
	
	void attach(node next){
		this.next = next;
		next.prev = this;
	}
	
	public int row(){ 
		return this.x;
	}
	
	public int col(){
		return this.y;
	}
	
	public node prev(){ 
		return this.prev;
	}
	
	public node next(){
		return this.next;
	}
	
	public int getState(){
		return this.gameState;
	}
}