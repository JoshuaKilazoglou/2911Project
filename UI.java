import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UI extends JFrame{
	private JPanel toolbar;
	private JButton startButton,undoButton,redoButton,exitButton,backToMenu;
	private BoardGUI board;

	public UI(){
		super("UI");
		board = new BoardGUI();
		add(board);
		addToolBar();
		board.setOpaque(true);
		setSize(640,560);
		setVisible(true);
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
				// What to do if back to menu;
			}
		}
	}

	public static void main(String args[]){
		UI ui = new UI();
	}
}