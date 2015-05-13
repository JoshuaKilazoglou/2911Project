import java.awt.*;

import javax.swing.*;

public class UI extends JFrame {
	private JFrame frame;
	private Game game;
	/*
	 * Precondition:Nothing
	 * Postcondition:A frame containing new content is inserted to frame
	 */
	public UI() {
		creatConetent();
	}

	/**
	 * Preconditon:Nothing
	 * Postconditon:Meant to be invoke every time when it needs to restart or start the game 
	 */
	public void creatConetent(){
		game = new Game();
		frameSetup();
		addingPanel();
		addingMouseAdapter();
		frame.setVisible(true);
	}
	

	private void addingPanel() {
		JLayeredPane layeredPane = frame.getLayeredPane();
		layeredPane.setVisible(true);
		layeredPane.setSize(640, 480);
		
		JLabel board = new JLabel();
		board.setBounds(0, 0,640,480);
		board.setVisible(true);
		ImageIcon i = new ImageIcon(getClass().getResource("Connect4Board.png"));
		board.setIcon(i);
		board.setOpaque(true);
		
		layeredPane.add(board, new Integer(10));
	}

	private void addingMouseAdapter(){
		JLayeredPane layeredPane = frame.getLayeredPane();
		
		Layer lowerLayer = new Layer();
		lowerLayer.setBounds(0, 0,640,480);
		lowerLayer.setVisible(true);
		lowerLayer.setOpaque(false);
		
		Mouse mouse = new Mouse(lowerLayer,game);
		lowerLayer.addMouseListener(mouse);
		
		layeredPane.add(lowerLayer,new Integer(8));
	}


	
	/**
	 * This is only called when we get into the whole gaming system
	 */
	private void frameSetup() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		frame.pack();
		frame.setSize(new Dimension(640, 508));
	}
}
