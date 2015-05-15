import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class EntrancePanel extends JPanel {
	private MenuUI myFrame;

	public EntrancePanel(MenuUI myFrame) {
		super();
		setVisible(true);
		setSize(250, 400);
		addingPanel();
		this.myFrame= myFrame;
		setVisible(true);
	}

	private void addingPanel() {
		JPanel Menu = new JPanel();
		Menu.setVisible(true);
		addingButton(Menu);
		add(Menu);
	}

	private void addingButton(JPanel target) {
		JPanel thisClass = this;
		
		JButton newGame = new JButton("NewGame");
		newGame.setVisible(true);
		newGame.setPreferredSize(new Dimension(40, 30));
		newGame.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel mode = myFrame.getTheOtherPanel(thisClass);
				mode.setVisible(true);
				myFrame.add(mode);
				setVisible(false);
			}

		});

		JButton help = new JButton("Help");
		help.setVisible(true);
		help.setPreferredSize(new Dimension(40, 30));
		help.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = "Help";
				JOptionPane.showMessageDialog(myFrame, message,
						"Help Information", JOptionPane.INFORMATION_MESSAGE,
						null);
			}
		});

		JButton exit = new JButton("Exit");
		exit.setVisible(true);
		exit.setPreferredSize(new Dimension(40, 30));
		exit.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}

		});

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints cst = new GridBagConstraints();
		cst.fill = GridBagConstraints.HORIZONTAL;

		target.setLayout(layout);
		cst.insets = new Insets(15, 0, 30, 0);// top padding,left padding,bottom
												// padding,right padding for
												// every grid block
		cst.ipadx = 80;// width of the grid block
		cst.ipady = 30;// height of the grid block
		cst.gridx = 0;
		cst.gridy = 0;
		target.add(newGame, cst);
		cst.gridy = 1;
		target.add(help, cst);
		cst.gridy = 2;
		target.add(exit, cst);
	}

}
