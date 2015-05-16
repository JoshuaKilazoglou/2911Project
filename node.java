public class node {
	private int x,y;
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

	public void printNexts(){
		for(node temp = this; temp != null; temp = temp.next)
			System.out.println("x: " + temp.x + ", y: " + temp.y);
	}

	public void printPrev(node prev){
		if(prev == null)
			return;
		else
			printPrev(prev.prev);

		System.out.println("x: " + prev.x + ", y: " + prev.y);		
	}
}