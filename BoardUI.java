import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BoardUI extends JFrame {
	private Game game;
	private Layer chessPiecePanel;
	private JPanel toolbar;
	private JButton startButton,undoButton,redoButton,exitButton;
	/*
	 * Precondition:Nothing Postcondition:A frame containing new content is
	 * inserted to frame
	 */
	public BoardUI() {
		super();
		creatConetent();
	}

	public void addButtons() {
		toolbar = new JPanel();
		startButton = new JButton("Restart");
		undoButton = new JButton("Undo");
		redoButton = new JButton("Redo");
		exitButton = new JButton("Exit");
		toolbar.setLayout(new FlowLayout());
		toolbar.add(startButton);
		toolbar.add(undoButton);
		toolbar.add(redoButton);
		toolbar.add(exitButton);
		listener lis = new listener();
		startButton.addActionListener(lis);
		undoButton.addActionListener(lis);
		redoButton.addActionListener(lis);
		exitButton.addActionListener(lis);
		add(toolbar,BorderLayout.SOUTH);
	}

	private class listener implements ActionListener{
	@Override
		public void actionPerformed(ActionEvent e){
			Object obj = e.getSource();
			if(obj==startButton){
				System.out.println("Restart");
				game.restartGame();
			}else if(obj==undoButton){
				System.out.println("Undo");
				game.undo();
			}else if(obj==redoButton){
				System.out.println("Redo");
				game.redo();
			}else if(obj==exitButton){
				System.out.println("Exit");
				System.exit(0);
			}
		}
	}
	/**
	 * Preconditon:Nothing Postconditon:Meant to be invoke every time when it
	 * needs to restart or start the game
	 */
	public void creatConetent() {
		game = new Game();
		chessPiecePanel = new Layer();
		frameSetup();
		addButtons();
		addingBoardPanel();
		addingChessListener();
	}

	private void addingBoardPanel() {
		JLayeredPane layeredPane = getLayeredPane();
//		layeredPane.setSize(640, 480);

		JLabel board = new JLabel();
		board.setBounds(0, 0, 640, 480);
		ImageIcon i = new ImageIcon(getClass().getResource("Connect4Board.png"));
		board.setIcon(i);
		board.setOpaque(true);

		layeredPane.add(board, new Integer(10));
	}

	private void addingChessListener() {
		JLayeredPane layeredPane = getLayeredPane();

		chessPiecePanel.setBounds(0, 0, 640, 480);
//		chessPiecePanel.setVisible(true);
		chessPiecePanel.setOpaque(false);

		ChessMouse mouse = new ChessMouse(chessPiecePanel, game, this);
		chessPiecePanel.addMouseListener(mouse);

		layeredPane.add(chessPiecePanel, new Integer(8));
	}

	/**
	 * This is only called when we get into the whole gaming system
	 */
	private void frameSetup() {
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setLayout(new FlowLayout());
		pack();
		setSize(new Dimension(640, 540));
//		setLocationRelativeTo(null);
	}
}