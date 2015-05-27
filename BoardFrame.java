import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Board extends JPanel implements MouseListener,ActionListener,MouseMotionListener{
	Game game;
	AI AI = null; // change basicAI to something your trying to test
	Image img;
	Dialog dialog;
	
	private final static int DURATION = 5;
	Timer t = null;

	private boolean playerWon = false;
	private boolean aIWon = false;

	private int player = Game.P1;
	private int ai = Game.P2;

	private boolean isFalling = false; // is a checker falling
	private int fallingRow = 0,fallSpeed = 10,terminate = 0;
	private int fallingCol = 0,col=0;
	
	private int cursorCol = -1;
	private boolean isAIMove = false;
	private int AIMode = 0;
	
	
	final static int ROW = Game.ROW;
	final static int COL = Game.COL;
	
	private boolean displayHint;

	public Board(Dialog dialog,int mode){
		game = new Game();
		this.dialog = dialog;
		this.AIMode = mode;
		if(mode != 0)
			this.AI = new DumbAI(); // for testing, change the AI object for which ever your using
		
		setBackground(Color.white);
		img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("Connect4Board.png"));
		addMouseListener(this);
		addMouseMotionListener(this);
		displayHint = false;
	}

	public void restartGame(){
		game.restartGame();
		isAIMove = false;
		playerWon = false;
		aIWon = false;
		repaint();
	}
	
	public void setDisplayHint(boolean i){
		this.displayHint = i;
	}

	public void undo(){
		if(isAIMove)
			return;

		if(AIMode != 0) {
			if(playerWon){
				if(game.undo())
					repaint();
				playerWon = false;
			} else {
				if(game.undo()) 
					repaint();
				if(game.undo())
					repaint();
			}
		} else if(game.undo())
			repaint();
	}

	public void redo(){
		if(isAIMove)
			return;

		if(AIMode != 0) {
			if(game.redo())
				repaint();
			if(game.redo())
				repaint();

			if(game.getState() == Game.GAMESET)
				if(game.switchPlayer() == player)
					playerWon = true;
				else
					aIWon = true;
		
		} else if(game.redo())
			repaint();
	}



	public int getDisImgToBorder(){
		int imgWidth = img.getWidth(this);
		int WinWidth = getWidth();
		return (WinWidth-imgWidth)/2;
	}

	public int getDisImgToTop(){
		int imgHeight = img.getHeight(this);
		int WinHeight = getHeight();
		return (WinHeight-imgHeight)/2+Connect4Board.CRADUIS;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int x = getDisImgToBorder(); // to keep the picture in the middle
		int y = getDisImgToTop();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(isFalling){
			g2d.setColor((game.getCurrentPlayer()==Game.P1)?Color.RED:Color.YELLOW);
			g2d.fillOval(fallingCol,fallingRow,Connect4Board.CRADUIS*2,Connect4Board.CRADUIS*2);
		}

		if(cursorCol != -1){
			int cursorRow = y-Connect4Board.CRADUIS*2;
			g2d.setColor((game.getCurrentPlayer()==Game.P1)?Color.RED:Color.YELLOW);
			g2d.fillOval(cursorCol-Connect4Board.CRADUIS,cursorRow,Connect4Board.CRADUIS*2,Connect4Board.CRADUIS*2);	
		}
		
		g.drawImage(img,x,y,null);
		for(int i = ROW-1; i >= 0; i--)
			for(int j = 0; j < COL; j++){
				int col = Game.getX(j);
				int row = Game.getY(ROW-1-i);
				if(game.whatsHere(i,j) != Game.NOP){
					g2d.setColor((game.whatsHere(i,j) == Game.P1)? Color.RED:Color.YELLOW);
					g2d.fillOval(x+col,y+row,Connect4Board.CRADUIS*2,Connect4Board.CRADUIS*2);
				}
				g2d.setColor(Color.BLACK);
				g2d.drawOval(x+col,y+row,Connect4Board.CRADUIS*2,Connect4Board.CRADUIS*2);	
			}
		
		
		node lastmove = game.getLastMove();
		if(lastmove != null){		
			int lastRow = Game.getY(ROW-lastmove.row()-1);
			int lastCol = Game.getX(lastmove.col());
		
			g2d.setColor(Color.GREEN);
			g2d.setStroke(new BasicStroke(5));
			g2d.drawOval(x+lastCol, y+lastRow, Connect4Board.CRADUIS*2, Connect4Board.CRADUIS*2);
		}
		
		if (displayHint){	
			int Row = Game.getY(ROW-game.getHint().row()-1);
			int Col = Game.getX(game.getHint().col());
		
			g2d.setColor(Color.pink);
			g2d.setStroke(new BasicStroke(5));
			g2d.drawOval(x+Col, y+Row, Connect4Board.CRADUIS*2, Connect4Board.CRADUIS*2);
		}
	}

	public void prepareAnimation(){
		t = new Timer(DURATION,this);
		fallingCol = Game.getX(this.col)+getDisImgToBorder();
		fallingRow = getDisImgToTop();
		terminate = Game.getY(ROW-game.top(this.col)-1);
		isFalling = true;
	}

	public void checkState(){
		if (game.getState() == Game.GAMESET){
   			dialog.setTitle("Player " + game.switchPlayer() + " Win");
			dialog.setModal(true);
			dialog.setVisible(true);
		} else if (game.getState() == Game.BOARDFULL){
   			dialog.setTitle("The Board is full");
			dialog.setModal(true);
			dialog.setVisible(true);
		}
	}

	public void actionPerformed(ActionEvent e){
		fallingRow += fallSpeed;
		repaint();
		if(fallingRow+Connect4Board.TOP_MARGIN > terminate){
			t.stop();
			isFalling = false;
			
			game.makeMove(this.col);	   		
			repaint();
			t = null;
   			
   			if(game.getState() == Game.GAMESET && game.switchPlayer() == player)
   				playerWon = true;
   			if(game.getState() == Game.GAMESET && game.switchPlayer() == ai)
   				aIWon = true;
   			
   			if (AIMode != 0 && isAIMove == false && !playerWon && game.getState() != Game.BOARDFULL){
   				new Thread(new Runnable() {
   					public void run(){
						isAIMove = true;
						col = AI.decideMove(game);
						prepareAnimation();
						t.start();					
					}
   				}).start();
			}
			
			if(isAIMove == true)
				isAIMove = false;
			
			checkState();
   		}
	}
	
	public void mousePressed(MouseEvent e) {
    	//
    }

   	public void mouseClicked(MouseEvent e) { 
   		if(game.getState() == 2)
   			return;
   		
   		if(isAIMove)
			return;
   		
   		if(t != null)
			return;
		
   		
   		double location = e.getPoint().getX();
   		col = Game.getCol(getDisImgToBorder(),location);
   		
   		if(col == -1)
   			return;
   		
   		if(!game.checkValidMove(col))
   			return;
   		
   		setDisplayHint(false);
   		Thread thread = new Thread(game);
   		thread.start();
		prepareAnimation();
		t.start();
		/*
			What happens when won? game.getState() = 2;
			what happens when the board is full? game.getState() = 1;
   		*/
		/*
			I moved the dialog pop up to actionPerformed because timer runs asynchronous
			and i can't find a way to access the thread componenet of the timer so I can't stop
			it running at the same time with the dialog pop up.
		*/
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
   	
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseMoved(MouseEvent e) {
		int cursorPos = e.getX();
		if(cursorPos < getDisImgToBorder()+Connect4Board.INITIAL_SIDE_MARGIN+Connect4Board.CRADUIS)
			cursorCol = getDisImgToBorder()+Connect4Board.INITIAL_SIDE_MARGIN+Connect4Board.CRADUIS;
		else if(cursorPos > getDisImgToBorder()+Game.getX(COL-1)+Connect4Board.CRADUIS)
			cursorCol = getDisImgToBorder()+Game.getX(COL-1)+Connect4Board.CRADUIS;
		else
			cursorCol = cursorPos;
		
		repaint();
	}
}

public class BoardFrame extends JFrame{
	private JPanel toolbar;
	private JButton startButton,undoButton,redoButton,exitButton,backToMenu,hintButton,b1,b2,b3;//the lass3 button is for dialog
	private Board board;
	
	public BoardFrame(int mode){
		super("BoardFrame");
		JDialog dialog = buildDialog();
		board = new Board(dialog,mode);
		add(board);
		addToolBar();
		board.setOpaque(true);
		setSize(680,630);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private JDialog buildDialog() {
		JDialog dialog = new JDialog(this);
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = (screenSize.width - 300) / 2;
		final int y = (screenSize.height - 200) / 2;
		dialog.setLocation(x, y);
		dialog.setSize(300,200);
		addButtontoDialog(dialog);
		return dialog;
	}

	public void addToolBar(){
		toolbar = new JPanel();
		startButton = new JButton("Restart");
		hintButton = new JButton("Hint");
		undoButton = new JButton("Undo");
		redoButton = new JButton("Redo");
		exitButton = new JButton("Exit");
		backToMenu = new JButton("Back");

		toolbar.add(startButton);
		toolbar.add(hintButton);
		toolbar.add(undoButton);
		toolbar.add(redoButton);
		toolbar.add(exitButton);
		toolbar.add(backToMenu);

		listener lis = new listener();
		hintButton.addActionListener(lis);
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
				board.setDisplayHint(false);
				repaint();
				System.out.println("Undo");
				board.undo();
			}else if(obj==redoButton){
				board.setDisplayHint(false);
				repaint();
				System.out.println("Redo");
				board.redo();
			}else if(obj==exitButton){
				System.out.println("Exit");
				System.exit(1);
			}else if(obj==backToMenu){
				System.out.println("BackToMenu");
				setVisible(false);
				dispose();
				MenuFrame current = new MenuFrame();
				current.getModePanel().setVisible(true);
				current.getEntrancePanel().setVisible(false);
				current.add(current.getModePanel());
			}else if(obj == hintButton){
				System.out.println("Hint");
				board.setDisplayHint(true);
				board.repaint();
			}
		}
	}
	
	private void addButtontoDialog(Dialog dialog){
		b1 = new JButton("ReStart");
		b2 = new JButton("Back to menu");
		b3 = new JButton("Exit");
		JPanel jpanel = new JPanel();
		jpanel.setBorder(BorderFactory.createEmptyBorder(20,50,20,50));
		jpanel.setLayout(new GridLayout(3,1,10,10));
		jpanel.add(b1);
		jpanel.add(b2);
		jpanel.add(b3);
		ActionListenerForDialog lis = new ActionListenerForDialog();
		b1.addActionListener(lis);
		b2.addActionListener(lis);
		b3.addActionListener(lis);
		b1.setVisible(true);
		b2.setVisible(true);
		b3.setVisible(true);
		dialog.add(jpanel);
	}
	
	private class ActionListenerForDialog implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == b1){
				System.out.println("Restart");
				board.restartGame();
				JDialog dialog = (JDialog) SwingUtilities.windowForComponent(b1);
				dialog.setVisible(false);
				dialog.dispose();
			}	else if (e.getSource() == b2){
				System.out.println("BackToMenu");
				setVisible(false);
				dispose();
				JFrame current = new MenuFrame();
			}	else if (e.getSource() == b3){
				System.exit(0);
			}		
		}		
	}
}
