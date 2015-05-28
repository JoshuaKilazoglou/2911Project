import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * The panel used to store the content of the connect4 board
 * 		implments MouseListener,ActionListener,MouseMotionListener
 */
class Board extends JPanel implements MouseListener,ActionListener,MouseMotionListener{
	Game game;
	AI AI = null; // change basicAI to something your trying to test
	Image img;
	Dialog dialog;
	JLabel wait;
	Thread hint;

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
	
	private boolean displayHint = false;
	private boolean waitingHint = false;
	private boolean interuptHint = false;
	private int hintCol = -1;
	
	final static int ROW = Game.ROW;
	final static int COL = Game.COL;

	/**
	 * Constructor to build the panel for the connect4 panel
	 * PostCondition: BUidling the panel for the connect4 board
	 * @param mode:The mode chosen to play.0 means pvp;1 means easy;2 measns normal;3 means hard
	 * @param dialog:The dialog that would pop up when game is fnished to remind user to make action.
	 * 				 This dialog should be initialized in the Frame of the board
	 */
	public Board(Dialog dialog,int mode){
		game = new Game();
		this.dialog = dialog;
		this.AIMode = mode;
		this.AI = new DumbAI(); // for testing, change the AI object for which ever your using
		
		setBackground(Color.white);
		img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("Connect4Board.png"));
		addMouseListener(this);
		addMouseMotionListener(this);
		displayHint = false;
	}

	/**
	 * Initialize wait label,which would be displayed while AI is generating steps
	 * Postcondition:The label for displaying the label is generated
	 * @param wait:The label displaying the text
	 */
	public void setWaitLabel(JLabel wait){
		this.wait = wait;
		wait.setVisible(false);
	}

	
	/**
	 * The method for restart the game in the logic backend and setting up some parameters to its begining state
	 * Preconditon:Game has started
	 * Postcondition: The game is reset
	 */
	public void restartGame(){
		if(isAIMove)
			return;

		if(waitingHint){
			interuptHint = true;
			wait.setVisible(false);
		}

		game.restartGame();
		isAIMove = false;
		playerWon = false;
		aIWon = false;
		setDisplayHint(false);
		repaint();
	}
	
	/**
	 * The method for displaying the hint
	 * Precondition: null
	 * Postcondtion: hint flag turned on
	 */
	public void displayHint(){
		if(isAIMove)
			return;
		if(waitingHint)
			return;

		wait.setText("Generating Hint...");

   		hint = new Thread(new Runnable() {
   			public void run(){
   				waitingHint = true;
   				wait.setVisible(true);
				hintCol = AI.decideMove(game);
				if(interuptHint){
					interuptHint = false;
					return;	
				}
				displayHint = true;
				repaint();
				if(!isAIMove)
					wait.setVisible(false);
				waitingHint = false;			
			}
   		});

   		hint.start();
	}

	/**
	 * undo the move
	 * postcondition: the display hint flag is turned off, chekers are deleted in the game
	 */
	public void undo(){
		if(waitingHint)
			return;

	   	setDisplayHint(false);
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
	
	/**
	 * redo the move
	 * postcondition: the display hint flag is turned off, chekers are added in the game
	 */
	public void redo(){
		if(waitingHint)
			return;
		
		setDisplayHint(false);
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

	/**
	 * display the hint
	 * @param i true for yes, false for no
	 */
	public void setDisplayHint(boolean i){
		this.displayHint = i;
	}

	/**
	 * get the distance betweent the image and the let border
	 * @return the distance
	 */
	public int getDisImgToBorder(){
		int imgWidth = img.getWidth(this);
		int WinWidth = getWidth();
		return (WinWidth-imgWidth)/2;
	}
	/**
	 * get the distance between the image and the top border
	 * @return the distance
	 */
	public int getDisImgToTop(){
		int imgHeight = img.getHeight(this);
		int WinHeight = getHeight();
		return (WinHeight-imgHeight)/2+Connect4Board.CRADUIS;
	}
	/**
	 * draw the game
	 * invariant the game
	 * precondition: g != null
	 * postcondition: the graphic is changed
	 */
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
			int Row = Game.getY(ROW-game.top(hintCol)-1);
			int Col = Game.getX(hintCol);
		
			g2d.setColor(Color.pink);
			g2d.setStroke(new BasicStroke(5));
			g2d.drawOval(x+Col, y+Row, Connect4Board.CRADUIS*2, Connect4Board.CRADUIS*2);
		}
	}

	/**
	 * prepare the animation
	 * precondition: null
	 * postcondition: create new timer, set the fallingCol and Row, set a terminate row and set falling to true
	 */
	public void prepareAnimation(){
		t = new Timer(DURATION,this);
		fallingCol = Game.getX(this.col)+getDisImgToBorder();
		fallingRow = getDisImgToTop();
		terminate = Game.getY(ROW-game.top(this.col)-1);
		isFalling = true;
	}

	/**
	 * check the state and pop message
	 * precondition: null
	 * postcondition: a message pop up or nothing happens
	 */
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
	
	/**
	 * performe the animation
	 * precondition: e != null
	 * postcondtion: the falling animation variables are set to the default state and the AI is called if pvAI
	 */
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
						wait.setText("AI is thinking...");
						wait.setVisible(true);
						col = AI.decideMove(game);
						try{
							Thread.sleep(100);
						}catch(Exception e){}
						wait.setVisible(false);	
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

	/**
	 * deals with clicking on the board
	 * precondition: e!=null
	 */
   	public void mouseClicked(MouseEvent e) { 
   		if(waitingHint){
   			interuptHint = true;
   			waitingHint = false;
   			new Thread(new Runnable(){
   				public void run(){
   					wait.setText("Hint interupted!");
   					try{
   						Thread.sleep(100);
   					}catch(Exception e){

   					}
   					if(!isAIMove)
   						wait.setVisible(false);

   				}
   			}).start();
   		}
		
   		setDisplayHint(false);
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
   		
		prepareAnimation();
		t.start();
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
	
	/**
	 * draw the ball on top when the mouse is moved
	 * precondition: e != null
	 * postcondition: the cursorCol is set to the position of the cursor
	 */
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

/**
 * The class for storing the frame of the menu
 * 		extends JFrame
 * @author Alan
 *
 */
public class BoardFrame extends JFrame{
	private JPanel toolbar;
	private JButton startButton,undoButton,redoButton,exitButton,backToMenu,hintButton,b1,b2,b3;//the lass3 button is for dialog
	private Board board;
	private JLabel wait; 
	
	/**
	 * Constructor for the frame of the game board
	 * Precondtion:null
	 * postCondtion:Creat an instance of the BoardFrame for users to operate
	 * @param mode
	 */
	public BoardFrame(int mode){
		super("BoardFrame");
		JDialog dialog = buildDialog();
		board = new Board(dialog,mode);
		add(board);
		addToolBar();
		board.setWaitLabel(wait);
		board.setOpaque(true);
		setSize(680,630);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	/**
	 * PreCondtion:null
	 * PostCondition:Creating the dialog to pop out when the game is finished
	 * @return JDialog
	 */
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

	/**
	 * A method creating toolbar with functional buttons
	 * PreCondtion:null
	 * PostCondition:Creating a JPanel as the game board with Buttons needed for the game board interface and add it to the frame
	 */
	public void addToolBar(){
		toolbar = new JPanel();
		startButton = new JButton("Restart");
		hintButton = new JButton("Hint");
		undoButton = new JButton("Undo");
		redoButton = new JButton("Redo");
		exitButton = new JButton("Exit");
		backToMenu = new JButton("Back");
		wait = new JLabel("Ai thinking...",JLabel.CENTER);

		toolbar.add(wait,BorderLayout.WEST);
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

		toolbar.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		toolbar.setLayout(new GridLayout(1,7));
		add(toolbar,BorderLayout.SOUTH);

	}
	
	/**
	 * A general private Listener class that deals with the Buttons in the game board Panel.(See addtoolBar method)
	 * @author Alan
	 *		implements ActionListener
	 */
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
				MenuFrame current = new MenuFrame();
				current.getModePanel().setVisible(true);
				current.getEntrancePanel().setVisible(false);
				current.add(current.getModePanel());
			}else if(obj == hintButton){
				System.out.println("Hint");
				board.displayHint();
			}
		}
	}
	
	/**
	 * Adding Buttons to the game finished dialog
	 * Precondtion:Passed in the game finished dialog into the method
	 * Postcondition:The dialog has all the buttons it need in it
	 * @param dialog the target dialog for the buttons to be put in
	 */
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
	
	/**
	 * A general private Listener class that deals with the Buttons in the game finished dialog.
	 * @author Alan
	 *		implements ActionListener
	 */
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