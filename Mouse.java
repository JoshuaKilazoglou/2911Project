import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Mouse implements MouseListener {
	Container target;
	Game game;
	BufferedImage[] image = new BufferedImage[2];

	public Mouse(Container target, Game game) {
		this.target = target;
		this.game = game;
		try {
			image[0] = ImageIO.read(getClass().getResource("1.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			image[1] = ImageIO.read(getClass().getResource("2.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int col = 0;
		int player = game.getCurrentPlayer();

		double Location = e.getPoint().getX();
		final double colWidth = 90.0;
		final double firstColWidth = 92.0;

		for (Double k = firstColWidth; col < 7; col++) {
			if (Location < k) {
				break;
			}
			k += colWidth;
		}
		
		System.out.println(col);
		int top = game.top(col);
		if (game.checkValidMove(col)) {
			game.makeMove(col);
			Graphics g = target.getGraphics();
			g.drawImage(image[player - 1], (int) ((col) * colWidth)+15,
					400 - top * 80 , 68, 80, null);
			target.paint(g);
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
