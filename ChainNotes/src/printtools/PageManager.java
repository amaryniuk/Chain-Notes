package printtools;

import SmokerGI.Preview;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;

public class PageManager implements Pageable
{
	PrintPage[] pages;
	PageFormat pf = new PageFormat();

	PageManager(Component[] comps)
	{
		pages = new PrintPage[comps.length];
		for (int i = 0; i < comps.length; i++)
		{
			pages[i] = new PrintPage(comps[i]);
		}
	}

	public void pagePrint()
	{
		PrinterJob printJob = PrinterJob.getPrinterJob();

		try
		{
			for (int i = 0; i < pages.length; i++)
			{
				printJob.setPrintable(pages[i], pf);
				printJob.print();
			}
		} catch (PrinterException pe)
		{
			System.out.println("Error printing: " + pe);
		}

	}

	public void pagePrint(int numb)
	{
		PrinterJob printJob = PrinterJob.getPrinterJob();

		try
		{
			printJob.setPrintable(pages[numb], pf);
			printJob.print();
		} catch (PrinterException pe)
		{
			System.out.println("Error printing: " + pe);
		}
	}

	public int getNumberOfPages()
	{
		return pages.length;
	}

	public void setPageFormat(PageFormat pf)
	{
		this.pf = pf;
	}

	public PageFormat getPageFormat(int pageIndex)
	        throws IndexOutOfBoundsException
	{
		return pf;
	}

	public Printable getPrintable(int pageIndex)
	        throws IndexOutOfBoundsException
	{
		return pages[pageIndex];
	}

}
