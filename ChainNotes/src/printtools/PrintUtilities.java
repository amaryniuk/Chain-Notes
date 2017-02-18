package printtools;

import SmokerGI.Preview;

import javax.swing.*;
import java.awt.print.*;
import java.awt.*;
import java.util.Vector;
import java.util.Iterator;

public class PrintUtilities extends Thread
{
	static boolean isBook = false;
//   static Book bk = new Book();
	static Book bk;
	static PageManager pm;
	public static int x = 15;
	public static int y = 0;
	public static int width = 592;          // Printable width
	public static int height = 998;         // Printable height
	public static int paperWidth = 612;
	public static int paperHeight = 1008;

	private JPanel cardPanel;
	private int pageCount;
	private CardLayout cl;

	public PrintUtilities()
	{
	}

	public void print(JPanel cardPanel, int pageCount)
	{
		try
		{
			CardLayout cl = (CardLayout) cardPanel.getLayout();
			JPanel bodyPanel = cardPanel;

			PrinterJob pj = PrinterJob.getPrinterJob();
			PageFormat pf = new PageFormat();
			pf.setOrientation(PageFormat.LANDSCAPE);
			Paper sheet = pf.getPaper();
			sheet.setSize(PrintUtilities.paperWidth, PrintUtilities.paperHeight);
			sheet.setImageableArea(PrintUtilities.x, PrintUtilities.y, PrintUtilities.width, PrintUtilities.height);
			pf.setPaper(sheet);

			cl.first(bodyPanel);
			for (int i = 0; i < pageCount; i++)
			{
				this.sleep(10);
				PrintPage tempPage = new PrintPage(bodyPanel);
				pj.setPrintable(tempPage, pf);
				try
				{
					pj.print();
				} catch (PrinterException e)
				{
					e.printStackTrace();
				}
				cl.next(bodyPanel);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void print()
	{
		PrinterJob printJob = PrinterJob.getPrinterJob();

		if (bk != null)
		{
			printJob.setPageable(bk);
		}
		else
		{
			printJob.setPageable(pm);
		}

		if (printJob.printDialog())
		{
			try
			{
				printJob.print();
			} catch (PrinterException pe)
			{
				System.out.println("Error printing: " + pe);
			}
		}
	}
}
