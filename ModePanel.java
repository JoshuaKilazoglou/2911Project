import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class ModePanel extends JPanel{
	private BoardUI boardUI;
	private MenuUI myFrame;
	public ModePanel(MenuUI myFrame){
		super();
		setVisible(true);
		setSize(250,400);
		addingPanel();
		this.myFrame = myFrame;
		this.boardUI = new BoardUI();
	}
	
	private void addingPanel(){
		JPanel mode = new JPanel();
		mode.setVisible(true);
		addingButton(mode);	
		add(mode);
	}
	
	private void addingButton(JPanel target){
		JButton AIButton = new JButton("V.S. AI");
		AIButton.setVisible(true);
		AIButton.setPreferredSize(new Dimension(40,30));
		AIButton.addActionListener(new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				boardUI.setVisible(true);
				myFrame.setVisible(false);
				setVisible(false);
			}
			
		});
									
		JButton personButton = new JButton("V.S. Friends");
		personButton.setVisible(true);
		personButton.setPreferredSize(new Dimension(40,30));
		personButton.addActionListener(new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				boardUI.setVisible(true);
				myFrame.setVisible(false);
				setVisible(false);				
			}
		});
		
		ModePanel thisClass = this;
		JButton back = new JButton("Back to menu");
		back.setVisible(true);
		back.setPreferredSize(new Dimension(40,30));
		back.addActionListener(new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);	
				JPanel menu = myFrame.getTheOtherPanel(thisClass);
				menu.setVisible(true);
				myFrame.add(menu);
			}
			
		});
			
		JButton exit = new JButton("Exit");
		exit.setVisible(true);
		exit.setPreferredSize(new Dimension(40,30));
		exit.addActionListener(new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);			
			}
			
		});
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints cst = new GridBagConstraints();
		cst.fill = GridBagConstraints.HORIZONTAL;
			
		target.setLayout(layout);
		cst.insets = new Insets(15, 0, 15, 0);//top padding,left padding,bottom padding,right padding for every grid block
		cst.ipadx = 80;//width of the grid block
		cst.ipady = 30;//height of the grid block
		cst.gridx = 0;
		cst.gridy = 0;
		target.add(AIButton,cst);
		cst.gridy = 1;
		target.add(personButton,cst);
		cst.gridy = 2;
		target.add(back,cst);
		cst.gridy = 3;
		target.add(exit,cst);
	}
}
