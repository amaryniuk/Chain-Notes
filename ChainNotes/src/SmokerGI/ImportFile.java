package SmokerGI;

import printtools.PrintUtilities;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.lang.*;
import java.util.Vector;

public class ImportFile extends JFrame
{
	public ImportFile(String client, String logoPath, String prospect, int shotPointSpacing, int receiverInterval,
	                  String pattern, String comments, String date, String compiledBy, String lineDirection,
	                  int pointsPerLine, int typeSelected, final Vector lineInfoVector)

	{
		// Some variables *************************************************************************
		int totalPoints = 0;
		int pagePoints = 0;
		int currentPage = 0;
		int numPages = 0;
		int thisPage = 0;

		Line line;
		String lineNumber = null;
		int bol = 0;
		int eol = 0;

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

		// Make all the Lines *********************************************************************

		// we need a (vector of) pagePanels for each line
		// that is; a complete line iss broken into several (vector) pages
		for (int i = 0; i < lineInfoVector.size(); i++)
		{
			// reset all values
			totalPoints = pagePoints = currentPage = numPages = thisPage = 0;

			// extract the lineNumber, bol, and eol from each entry in the vector
			line = (Line) lineInfoVector.elementAt(i);
			lineNumber = line.lineNumber();
			bol = line.bol();
			eol = line.eol();

			// Preview Panel Stuff ********************************************************************
			totalPoints = eol - bol + 1;

			numPages = totalPoints / (pointsPerLine * 3);
			if (totalPoints % (pointsPerLine * 3) != 0)
			{
				numPages++;
			}

			for (int j = 0; j < numPages; j++)
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

				currentPage = pageCount + 1;
				thisPage = j + 1;

				Preview pagePanel = new Preview(client, logoPath, prospect, shotPointSpacing, receiverInterval, pattern,
				                                comments, date, compiledBy, lineNumber, lineDirection, pointsPerLine,
				                                bol, eol, pagePoints, typeSelected, thisPage, numPages, i * pointsPerLine * 3);

				pagePanel.setVisible(true);

				bodyPanel.add(String.valueOf(pageCount), pagePanel);
				pageCount++;

				pageVector.addElement(pagePanel);
				bol += pointsPerLine * 3;
			}
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
	private JButton prevLine = new JButton("<<");
	private JButton nextLine = new JButton(">>");
}