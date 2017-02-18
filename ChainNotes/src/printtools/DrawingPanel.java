package printtools;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

public class DrawingPanel extends JPanel
{
	private int fontSize = 90;
	private String message = "Java 2D";
	private int messageWidth;

	public DrawingPanel()
	{
		setBackground(Color.white);
		Font font = new Font("Serif", Font.PLAIN, fontSize);
		setFont(font);
		FontMetrics metrics = getFontMetrics(font);
		messageWidth = metrics.stringWidth(message);
		int width = messageWidth * 5 / 3;
		int height = fontSize * 3;
		setPreferredSize(new Dimension(width, height));
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		int x = messageWidth / 10;
		int y = fontSize * 5 / 2;
		g2d.translate(x, y);
		g2d.setPaint(Color.lightGray);
		AffineTransform origTransform = g2d.getTransform();
		g2d.shear(-0.95, 0);
		g2d.scale(1, 3);
		g2d.drawString(message, 0, 0);
		g2d.setTransform(origTransform);
		g2d.setPaint(Color.black);
		g2d.drawString(message, 0, 0);
	}
}
