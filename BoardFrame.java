import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Board extends JPanel implements MouseListener{
	Game game;
	Image img;
	boolean isGameOver = false;
	private JFrame myFrame = BoardFrame.this;

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

	public Board(){
		game = new Game();
		setBackground(Color.white);
		myFrame = frame;
		img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("Connect4Board.png"));
		addMouseListener(this);
	}

	public int getDisImgToBorder(){
		int imgWidth = img.getWidth(this);
		int WinWidth = getWidth();
		return (WinWidth-imgWidth)/2;
	}

	public int getDisImgToTop(){
		int imgHeight = img.getHeight(this);
		int WinHeight = getHeight();
		return (WinHeight-imgHeight)/2;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int x = getDisImgToBorder(); // to keep the picture in the middle
		int y = getDisImgToTop();
		g.drawImage(img,x,y,null);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		for(int i = ROW-1; i >= 0; i--)
			for(int j = 0; j < COL; j++){
				int col = Game.getX(j);
				int row = Game.getY(ROW-1-i);
				if(game.whatsHere(i,j) != 0){
					g2d.setColor((game.whatsHere(i,j) == 1)? Color.RED:Color.YELLOW);
					g2d.fillOval(x+col,y+row,Connect4Board.CRADUIS*2,Connect4Board.CRADUIS*2);
				}
				g2d.setColor(Color.BLACK);
				g2d.drawOval(x+col,y+row,Connect4Board.CRADUIS*2,Connect4Board.CRADUIS*2);	
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

public class BoardFrame extends JFrame{
	private JPanel toolbar;
	private JButton startButton,undoButton,redoButton,exitButton,backToMenu;
	private Board board;

	public BoardFrame(){
		super("BoardFrame");
		board = new Board();
		add(board);
		addToolBar();
		board.setOpaque(true);
		setSize(640,560);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void addToolBar(){
		toolbar = new JPanel();
		startButton = new JButton("Restart");
		undoButton = new JButton("Undo");
		redoButton = new JButton("Redo");
		exitButton = new JButton("Exit");
		backToMenu = new JButton("Back");

		toolbar.add(startButton);
		toolbar.add(undoButton);
		toolbar.add(redoButton);
		toolbar.add(exitButton);
		toolbar.add(backToMenu);

		listener lis = new listener();
		startButton.addActionListener(lis);
		undoButton.addActionListener(lis);
		redoButton.addActionListener(lis);
		exitButton.addActionListener(lis);
		backToMenu.addActionListener(lis);

		toolbar.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		add(toolbar,BorderLayout.SOUTH);

	}

	private class listener implements ActionListener{
	@Override
		public void actionPerformed(ActionEvent e){
			Object obj = e.getSource();
			if(obj==startButton){
				System.out.println("Restart");
				board.restartGame();
			}else if(obj==undoButton){
				System.out.println("Undo");
				board.undo();
			}else if(obj==redoButton){
				System.out.println("Redo");
				board.redo();
			}else if(obj==exitButton){
				System.out.println("Exit");
				System.exit(1);
			}else if(obj==backToMenu){
				System.out.println("BackToMenu");
				setVisible(false);
				dispose();
				JFrame current = new MenuFrame();
			}
		}
	}
}
