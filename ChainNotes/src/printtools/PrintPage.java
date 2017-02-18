package printtools;

import SmokerGI.Preview;

import javax.swing.*;
import java.awt.print.*;
import java.awt.*;
import java.awt.image.VolatileImage;

public class PrintPage implements Printable
{
	Component toBePrinted;
	protected static JFrame printFrame = null;

	public PrintPage(Component componentToBePrinted)
	{
		if (printFrame == null)
		{
			printFrame = new JFrame();
		}
		this.toBePrinted = componentToBePrinted;
//      JFrame testFrame = new JFrame();
//      testFrame.getContentPane().add(toBePrinted);
//      toBePrinted.setVisible(true);
//      toBePrinted.validate();
//      testFrame.pack();
//      testFrame.setVisible(true);
	}

	public int print(Graphics g, PageFormat pageFormat, int pageIndex)
	{
		if (pageIndex > 0)
		{
			return NO_SUCH_PAGE;
		}
		else
		{
//         JFrame testFrame = new JFrame();
//         testFrame.getContentPane().add(toBePrinted);
//         toBePrinted.setVisible(true);
//         toBePrinted.validate();
//         testFrame.pack();
//         testFrame.setVisible(true);

			Graphics2D g2d = (Graphics2D) g;

			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			disableDoubleBuffering(toBePrinted);
			toBePrinted.setVisible(true);
			toBePrinted.validate();
			toBePrinted.paint(g2d);

			enableDoubleBuffering(toBePrinted);

			return PAGE_EXISTS;
		}
	}

	public static void disableDoubleBuffering(Component c)
	{
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(false);
	}

	public static void enableDoubleBuffering(Component c)
	{
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(true);
	}
}
