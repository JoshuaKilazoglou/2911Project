import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuUI extends JFrame {
	private ModePanel modePanel;
	private EntrancePanel entrancePanel;

	public MenuUI() {
		super();
		modePanel = new ModePanel(this);
		entrancePanel = new EntrancePanel(this);
		add(entrancePanel);
		setSize(400,500);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	public JPanel getTheOtherPanel(JPanel myself){
		if (myself.equals(modePanel)){
			return entrancePanel;
		}	else{
			return modePanel;
		}
	}
}
