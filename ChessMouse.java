import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;

public class ChessMouse implements MouseListener {
	BoardUI ui;
	Game game;
	Layer target;
	JDialog dialog;
	Color[] color = new Color[2];

	public ChessMouse(Layer target, Game game,BoardUI ui) {
		this.target = target;
		this.game = game;
		this.ui = ui;
		this.dialog = new JDialog();
		color[0] = Color.RED;
		color[1] = Color.YELLOW;
		
		JButton reStart = new JButton("Restart");
		reStart.setVisible(true);
		reStart.setPreferredSize(new Dimension(100,62));
		reStart.addActionListener(new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				game.restartGame();	
				ui.creatConetent();
				dialog.setVisible(false);
			}
			
		});
		
		JButton exit = new JButton("exit");
		exit.setVisible(true);
		exit.setPreferredSize(new Dimension(100,62));
		exit.addActionListener(new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);	
			}
			
		});
		
		
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setHgap(20);
		flowLayout.setVgap(30);
		dialog.setSize(new Dimension(260,160));
		dialog.setLayout(flowLayout);
		dialog.setResizable(false);
		dialog.add(reStart);
		dialog.setLocation(target.getWidth()/3, target.getHeight()/3);
		dialog.add(exit);	
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int col = 0;
		int player = game.getCurrentPlayer();

		double LocationX = e.getPoint().getX();
		//double LocationY = e.getPoint().getY();
		final double colWidth = 90.0;
		final double firstColWidth = 92.0;

		for (Double k = firstColWidth; col < 7; col++) {
			if (LocationX < k) {
				break;
			}
			k += colWidth;
		}

		int top = game.top(col);
		if (game.checkValidMove(col)) {
			game.makeMove(col);
			Graphics g = target.getGraphics();
			g.setColor(color[player-1]);
			g.fillOval((int) ((col) * colWidth) + 13, 405 - top * 80, 75, 75);
			target.paintComponent(g);
		}
		
		if (game.getState() == 2){
			dialog.setModal(true);
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
		}	
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
