import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoardGUI extends JPanel implements MouseListener{
	Game game;
	Image img;
	boolean isGameOver = false;

	final static int ROW = 6;
	final static int COL = 7;

	public void restartGame(){
		game.restartGame();
		repaint();
	}

	public void undo(){
		if(game.undo())
			repaint();
	}

	public void redo(){
		if(game.redo())
			repaint();
	}

	public BoardGUI(){
		game = new Game();
		setBackground(Color.white);
		img = Toolkit.getDefaultToolkit().getImage("Connect4Board.png");
		addMouseListener(this);
	}

	public int getDisImgToBorder(){
		int imgWidth = img.getWidth(this);
		int imgHeight = img.getHeight(this);
		int WinWidth = getWidth();
		int WinHeight = getHeight();
		return (WinWidth-imgWidth)/2;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);

		int imgWidth = img.getWidth(this);
		int imgHeight = img.getHeight(this);
		int WinWidth = getWidth();
		int WinHeight = getHeight();


//		System.out.println("imgWidth: " + imgWidth + ", imgHeight: " + imgHeight);
//		System.out.println("WinWidth: " + WinWidth + ", WinHeight: " + WinHeight);

		int x = (WinWidth-imgWidth)/2; // to keep the picture in the middle
		int y = (WinHeight-imgHeight)/2;
		g.drawImage(img,x,y,null);

		for(int i = ROW-1; i >= 0; i--)
			for(int j = 0; j < COL; j++){
				g.setColor(Color.BLACK);
				int col = Game.getX(j);
				int row = Game.getY(ROW-1-i);
				g.drawOval(x+col,y+row,Connect4Board.CRADUIS*2,Connect4Board.CRADUIS*2);
				if(game.whatsHere(i,j) != 0){
					g.setColor((game.whatsHere(i,j) == 1)? Color.RED:Color.YELLOW);
					g.fillOval(x+col,y+row,Connect4Board.CRADUIS*2,Connect4Board.CRADUIS*2);
				}
				
			}

		

	}

	public void mousePressed(MouseEvent e) {
    	//
    }  
   
   	public void mouseClicked(MouseEvent e) {  
   		double location = e.getPoint().getX();
   		int borderDis = getDisImgToBorder();
   		int col = Game.getCol(borderDis,location);
   		
   		if(col == -1)
   			return;

   		if(!game.checkValidMove(col))
   			return;

   		int row = game.makeMove(col);
   		/*
			What happens when won? game.getState() = 2;
			what happens when the board is full? game.getState() = 1;
   		*/
   		repaint();
   	}  
     
   	public void mouseEntered(MouseEvent e) {  
   		//
   	}  
   	
   	public void mouseExited(MouseEvent e) {  
   		//
   	}  
   	public void mouseReleased(MouseEvent e) {  
   		//
   	}
}