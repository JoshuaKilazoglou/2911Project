import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuFrame extends JFrame {
	private MenuFrame thisClass = this;
	private BoardFrame boardFrame;
	private JPanel entrance;//exitButton0
	private JPanel mode;//backToMenuButton1,exitButton1
	private JPanel AI;//backToMenuButton2,exitButton2
	private JButton newGameButton, helpButton, exitButton0, exitButton1, exitButton2, hardButton,
			normalButton, easyButton, pvpButton, pvAIButton, backToMenuButton1,
			backToMenuButton2;
	private AbstractAction specialListener;//this is for back to Menu

	public MenuFrame() {
		super("Menu");
		
		specialListener = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				boardFrame.setVisible(false);
				boardFrame.dispose();
				mode.setVisible(false);
				AI.setVisible(false);
				add(entrance);
				entrance.setVisible(true);
				setVisible(true);
			}
		};
		
		setSize(400, 500);
		setLocationRelativeTo(null);
		buildEntrancePanel();
		buildModePanel();
		buildAIPanel();
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void buildEntrancePanel() {
		entrance = new JPanel();
		newGameButton = new JButton("NewGame");
		helpButton = new JButton("Help");
		exitButton0 = new JButton("Exit");

		ListenerForEntrance lis = new ListenerForEntrance();
		newGameButton.addActionListener(lis);
		helpButton.addActionListener(lis);
		exitButton0.addActionListener(lis);

		entrance.add(newGameButton);
		entrance.add(helpButton);
		entrance.add(exitButton0);

		add(entrance);
	}

	private void buildModePanel() {
		mode = new JPanel();
		pvpButton = new JButton("V.S. Friends");
		pvAIButton = new JButton("V.S. AI");
		backToMenuButton1 = new JButton("Back To Menu");
		exitButton1 = new JButton("Exit");

		ListenerForMode lis = new ListenerForMode();
		pvpButton.addActionListener(lis);
		pvAIButton.addActionListener(lis);
		backToMenuButton1.addActionListener(lis);
		exitButton1.addActionListener(lis);

		mode.add(pvpButton);
		mode.add(pvAIButton);
		mode.add(backToMenuButton1);
		mode.add(exitButton1);
	}

	private void buildAIPanel() {
		AI = new JPanel();
		hardButton = new JButton("Hard");
		normalButton = new JButton("Normal");
		easyButton = new JButton("Easy");
		backToMenuButton2 = new JButton("Back To Menu");
		exitButton2 = new JButton("Exit");
		

		ListenerForAI lis = new ListenerForAI();
		hardButton.addActionListener(lis);
		normalButton.addActionListener(lis);
		easyButton.addActionListener(lis);
		backToMenuButton2.addActionListener(lis);
		exitButton2.addActionListener(lis);
		
		AI.add(easyButton);
		AI.add(normalButton);
		AI.add(hardButton);
		AI.add(backToMenuButton2);
		AI.add(exitButton2);
	}

	private class ListenerForEntrance implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object object = e.getSource();
			if (object == newGameButton) {
				entrance.setVisible(false);
				mode.setVisible(true);
				add(mode);
			} else if (object == helpButton) {
				String message = "help";
				JOptionPane.showMessageDialog(thisClass, message,
						"Help Information", JOptionPane.INFORMATION_MESSAGE,
						null);
			} else if (object == exitButton0) {
				System.exit(0);
			}
		}
	}
	
	private class ListenerForMode implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object object = e.getSource();
			if (object == backToMenuButton1) {
				mode.setVisible(false);
				entrance.setVisible(true);
				add(entrance);
			} else if (object == pvpButton) {
				boardFrame = new BoardFrame();
				//boardFrame.setButton(specialListener);
				setVisible(false);
				dispose();
				boardFrame.setVisible(true);
			} else if (object == pvAIButton) {
				mode.setVisible(false);
				AI.setVisible(true);
				add(AI);
			} else if (object == exitButton1) {
				System.exit(0);
			}
		}
	}


	private class ListenerForAI implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object object = e.getSource();
			if (object == backToMenuButton2) {
				AI.setVisible(false);
				entrance.setVisible(true);
				add(entrance);
			} else if (object == hardButton) {
				boardFrame = new BoardFrame();
				//boardFrame.setButton(specialListener);
				setVisible(false);
				dispose();
				boardFrame.setVisible(true);
			} else if (object == normalButton) {
				boardFrame = new BoardFrame();
				//boardFrame.setButton(specialListener);
				setVisible(false);
				dispose();
				boardFrame.setVisible(true);
			} else if (object == easyButton) {
				boardFrame = new BoardFrame();
				//boardFrame.setButton(specialListener);
				setVisible(false);
				dispose();
				boardFrame.setVisible(true);
			} else if (object == exitButton2){
				System.exit(0);
			}
		}
	}
}
