package SmokerGI;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.lang.*;
import java.util.Vector;

import printtools.PrintUtilities;

public class Intermediate extends JFrame
{
	public static void main(String[] args)
	{
		Intermediate frame = new Intermediate("Client", "", "Prospect", 30, 10,
		                                      "Pattern", "Comments", "Date", "Compiled By", "Line Number",
		                                      "Line Direction", 12, 101, 300, 1);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.show();
	}

	/**************************************************************************************************
	 * Intermediate(...)
	 *  Purpose:
	 *	    Take in the information entered inside of SmokerGI, and convert it into each page of
	 *      the chain notes.  Then add each page to a vector so we can easily access all of them.
	 *      Also, create a small button panel so we can easily navigate through the vector
	 *	Use:
	 *	    Either run Intermediate () via the SmokerGI class, or independantly with the stub
	 *      call to the constructor inside of main.
	 **************************************************************************************************/
	public Intermediate(String client, String logoPath, String prospect, int shotPointSpacing, int receiverInterval,
	                    String pattern, String comments, String date, String compiledBy, String lineNumber,
	                    String lineDirection, int pointsPerLine, int bol, int eol, int typeSelected)
	{
		// Some variables *************************************************************************
		int totalPoints = 0;
		int pagePoints = 0;
		int currentPage = 0;
		int numPages = 0;

		if (logoPath.length() < 2)
			logoPath = "SmokerGI/1ws.adm";

		// Button Panel Stuff *********************************************************************
		buttonPanel.setBackground(Color.white);

		prev.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				c1.previous(bodyPanel);
			}
		});

		next.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				c1.next(bodyPanel);
			}
		});

		close.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				setVisible(false);
			}
		});

		print.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				PrintUtilities pu = new PrintUtilities();
				pu.start();
				pu.print(bodyPanel, pageCount);
			}
		});

		buttonPanel.add(prev);
		buttonPanel.add(print);
		buttonPanel.add(close);
		buttonPanel.add(next);

		// Preview Panel Stuff ********************************************************************
		totalPoints = eol - bol + 1;

		numPages = totalPoints / (pointsPerLine * 3);
		if (totalPoints % (pointsPerLine * 3) != 0)
		{
			numPages++;
		}

		for (int i = 0; i < numPages; i++)
		{
			if (totalPoints - (pointsPerLine * 3) >= 0)
			{
				totalPoints -= (pointsPerLine * 3);
				pagePoints = pointsPerLine * 3;
			}
			else
			{
				pagePoints = totalPoints;
			}

			currentPage = i + 1;

			Preview pagePanel = new Preview(client, logoPath, prospect, shotPointSpacing, receiverInterval, pattern,
			                                comments, date, compiledBy, lineNumber, lineDirection, pointsPerLine,
			                                bol, eol, pagePoints, typeSelected, currentPage, numPages, i * pointsPerLine * 3);

			pagePanel.setVisible(true);

			bodyPanel.add(String.valueOf(i), pagePanel);
			pageCount++;

			pageVector.addElement(pagePanel);
			bol += pointsPerLine * 3;
		}
		c1.first(bodyPanel);

		// Frame Stuff ****************************************************************************
		getContentPane().add(bodyPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		this.setTitle(client + " : " + prospect);
		this.setSize(1000, 650);
		this.setResizable(false);
	}

	// Preview Panel Stuff ************************************************************************
	private Vector pageVector = new Vector();

	// CardLayout Manager for bodyPanel ***********************************************************
	private JPanel bodyPanel = new JPanel(new CardLayout(), true);
	private CardLayout c1 = (CardLayout) bodyPanel.getLayout();
	private int pageCount = 0;

	// Button Panel Stuff *************************************************************************
	private JPanel buttonPanel = new JPanel();
	private JButton close = new JButton("Close");
	private JButton print = new JButton("Print");
	private JButton prev = new JButton("<");
	private JButton next = new JButton(">");
}
