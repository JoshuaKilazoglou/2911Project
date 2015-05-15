import java.awt.*;

import javax.swing.*;

public class BoardUI extends JFrame {
	private Game game;
	private Layer chessPiecePanel;

	/*
	 * Precondition:Nothing Postcondition:A frame containing new content is
	 * inserted to frame
	 */
	public BoardUI() {
		super();
		creatConetent();
	}

	/**
	 * Preconditon:Nothing Postconditon:Meant to be invoke every time when it
	 * needs to restart or start the game
	 */
	public void creatConetent() {
		game = new Game();
		chessPiecePanel = new Layer();
		frameSetup();
		addingBoardPanel();
		addingChessListener();
	}

	private void addingBoardPanel() {
		JLayeredPane layeredPane = getLayeredPane();
		layeredPane.setVisible(true);
		layeredPane.setSize(640, 480);

		JLabel board = new JLabel();
		board.setBounds(0, 0, 640, 480);
		board.setVisible(true);
		ImageIcon i = new ImageIcon(getClass().getResource("Connect4Board.png"));
		board.setIcon(i);
		board.setOpaque(true);

		layeredPane.add(board, new Integer(10));
	}

	private void addingChessListener() {
		JLayeredPane layeredPane = getLayeredPane();

		chessPiecePanel.setBounds(0, 0, 640, 480);
		chessPiecePanel.setVisible(true);
		chessPiecePanel.setOpaque(false);

		ChessMouse mouse = new ChessMouse(chessPiecePanel, game, this);
		chessPiecePanel.addMouseListener(mouse);

		layeredPane.add(chessPiecePanel, new Integer(8));
	}

	/**
	 * This is only called when we get into the whole gaming system
	 */
	private void frameSetup() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		pack();
		setSize(new Dimension(640, 508));
		setLocationRelativeTo(null);
	}
}