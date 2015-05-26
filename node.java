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
	
	public int x(){ 
		return this.x;
	}
	
	public int y(){
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
	public void printNexts(){
		for(node temp = this; temp != null; temp = temp.next)
			System.out.println("x: " + temp.x + ", y: " + temp.y);
	}

	/*public int steps(node prev){
		if(prev == null)
			return 1;
		else
			return steps(prev.prev)+1;
	}
	
	public getFirstNode(){
		if(prev == null)
			return 1;
		else
			return steps(prev.prev)+1;
		
	}*/
	
	public node clone(){
		node newNode = null;
		try {
			newNode = (node) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (newNode != null){
			newNode.attach(newNode.next().clone());
		}
		return newNode;
		
	}
}