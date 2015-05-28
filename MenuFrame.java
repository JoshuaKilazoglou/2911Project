import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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

import java.awt.*;
import javax.swing.*;
/**
 * Public class of the Frame of the menu
 * @author Alan
 *		extends JFrame
 */
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

	/**
	 * Constructor of the class
	 * Postcondtion:creat an instance of the class
	 */
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

	/**
	 * Private method for building one of the panels to be switched in MenuFrame. This is the EntrancPanel
	 * Postcondition:An initialized instance of Entrance Panel is built and put in the field of entrance
	 */
	private void buildEntrancePanel() {
		entrance = new JPanel();
		entrance.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		entrance.setLayout(new GridLayout(3,1,15,20));
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

	/**
	 * Private method for building one of the panels to be switched in MenuFrame. This is the ModePanel
	 * Postcondition:An initialized instance of ModePanel is built and put in the field of mode
	 */
	private void buildModePanel() {
		mode = new JPanel();
		mode.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		mode.setLayout(new GridLayout(4,1,15,20));

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

	
	/**
	 * Private method for building one of the panels to be switched in MenuFrame. This is the AIPanel
	 * Postcondition:An initialized instance of AIPanel is built and put in the field of AI
	 */
	private void buildAIPanel() {
		AI = new JPanel();
		AI.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		AI.setLayout(new GridLayout(5,1,15,20));

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

	/**
	 * A general private Listener class that deals with the Buttons in the EntrancePanel
	 * @author Alan
	 * implements ActionListener
	 */
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
	
	/**
	 * A general private Listener class that deals with the Buttons in the ModePanel
	 * @author Alan
	 * implements ActionListener
	 */
	private class ListenerForMode implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object object = e.getSource();
			if (object == backToMenuButton1) {
				mode.setVisible(false);
				entrance.setVisible(true);
				add(entrance);
			} else if (object == pvpButton) {
				boardFrame = new BoardFrame(0);
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


	/**
	 * A general private Listener class that deals with the Buttons in the AIPanel
	 * @author Alan
	 * implements ActionListener
	 */
	private class ListenerForAI implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object object = e.getSource();
			if (object == backToMenuButton2) {
				AI.setVisible(false);
				entrance.setVisible(true);
				add(entrance);
			} else if (object == hardButton) {
				boardFrame = new BoardFrame(3);
				//boardFrame.setButton(specialListener);
				setVisible(false);
				dispose();
				boardFrame.setVisible(true);
			} else if (object == normalButton) {
				boardFrame = new BoardFrame(2);
				//boardFrame.setButton(specialListener);
				setVisible(false);
				dispose();
				boardFrame.setVisible(true);
			} else if (object == easyButton) {
				boardFrame = new BoardFrame(1);
				//boardFrame.setButton(specialListener);
				setVisible(false);
				dispose();
				boardFrame.setVisible(true);
			} else if (object == exitButton2){
				System.exit(0);
			}
		}
	}
	
	/**
	 * Getting the EntrancePanel
	 * @return JPanel
	 */
	public JPanel getEntrancePanel(){
		return entrance;
	}
	
	/**
	 * Getting the ModePanel
	 * @return JPanel
	 */
	public JPanel getModePanel(){
		return mode;
	}
}
