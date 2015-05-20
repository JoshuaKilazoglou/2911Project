import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Board extends JPanel implements MouseListener,ActionListener,MouseMotionListener{
	Game game;
	basicAI AI = null;
	Image img;
	Dialog dialog;
	
	private final static int DURATION = 5;
	Timer t = null;
	private boolean isFalling = false; // is a checker falling
	private int fallingRow = 0,fallSpeed = 12,terminate = 0;
	private int fallingCol = 0,col=0;
	
	private int cursorCol = -1;
	private boolean isAIMove = false;
	

	final static int ROW = 6;
	final static int COL = 7;

	public int getCurrentPlayer(){
		return game.switchPlayer();
	}
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

	public Board(Dialog dialog){
		game = new Game();
		this.dialog = dialog;
		setBackground(Color.white);
		img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("Connect4Board.png"));
		addMouseListener(this);
		addMouseMotionListener(this);
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
			g2d.setColor((game.getCurrentPlayer()==1)?Color.RED:Color.YELLOW);
			g2d.fillOval(fallingCol,fallingRow,Connect4Board.CRADUIS*2,Connect4Board.CRADUIS*2);
		}

		if(cursorCol != -1){
			int cursorRow = y-Connect4Board.CRADUIS*2;
			g2d.setColor((game.getCurrentPlayer()==1)?Color.RED:Color.YELLOW);
			g2d.fillOval(cursorCol-Connect4Board.CRADUIS,cursorRow,Connect4Board.CRADUIS*2,Connect4Board.CRADUIS*2);	
		}
		
		g.drawImage(img,x,y,null);
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
		
		
		node lastmove = game.getLastMove();
		if(lastmove != null){		
			int lastRow = Game.getY(ROW-lastmove.x()-1);
			int lastCol = Game.getX(lastmove.y());
		
			g2d.setColor(Color.GREEN);
			g2d.setStroke(new BasicStroke(5));
			g2d.drawOval(x+lastCol, y+lastRow, Connect4Board.CRADUIS*2, Connect4Board.CRADUIS*2);
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
   			if (game.getState() == 2){
   				dialog.setTitle("Player " + game.switchPlayer() + " Win");
				dialog.setModal(true);
				dialog.setVisible(true);
			} else if (game.getState() == 1){
   				dialog.setTitle("The Board is full");
				dialog.setModal(true);
				dialog.setVisible(true);
			}
   			t = null;
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
   		int borderDis = getDisImgToBorder();
   		int borderDisWidth = getDisImgToBorder();
   		col = Game.getCol(borderDis,location);
   		
   		if(col == -1)
   			return;
   		
   		if(!game.checkValidMove(col))
   			return;
   		
		
		t = new Timer(DURATION,this);

		fallingCol = Game.getX(col)+borderDisWidth;
		fallingRow = borderDis;
		terminate = Game.getY(ROW-game.top(col)-1);
		isFalling = true;

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
	private JButton startButton,undoButton,redoButton,exitButton,backToMenu,b1,b2,b3;
	private Board board;
	
	public BoardFrame(){
		super("BoardFrame");
		JDialog dialog = buildDialog();
		board = new Board(dialog);
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
				MenuFrame current = new MenuFrame();
				current.getModePanel().setVisible(true);
				current.getEntrancePanel().setVisible(false);
				current.add(current.getModePanel());
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
