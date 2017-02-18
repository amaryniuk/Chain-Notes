package SmokerGI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.lang.*;

import Drawkit.*;

public class Preview extends JPanel
{
	public Preview(String aClient, String aLogoPath, String aProspect, int aShotPointSpacing,
	               int aReceiverInterval, String aPattern, String aComments,
	               String aDate, String aCompiledBy, String aLineNumber, String aLineDirection,
	               int pointsPerLine, int bol, int eol, int pagePoints, int typeSelected, int _currentPage,
	               int numPages, int pointType)
	{
		aProspect = aProspect.toUpperCase();
		clientLogo = new JLabel(new ImageIcon(aLogoPath));
		currentPage = _currentPage;

		infoTitle.setText(
		        "<html>" +
		        "<table cellspacing=0 cellpadding=0>" +
		        "<tr>" +
		        "<td width=" + titlewidth + " align=center><font size=" + headerText + "><b>" + aProspect + "</b></font>" +
		        "<br>" +
		        "<font size=" + titletext + "><b>LINE CHAINING NOTES</b></font>" +
		        "</td>" +
		        "</tr>" +
		        "</table>" +
		        "</html>"
		);

		/* checks to leave certain fields blank */
		if (aShotPointSpacing == 0)
			bShotPointSpacing = "";
		else
		{
			bShotPointSpacing = String.valueOf(aShotPointSpacing);
			bShotPointSpacing = bShotPointSpacing + " m";
		}

		if (aReceiverInterval == 0)
			bReceiverInterval = "";
		else
		{
			bReceiverInterval = String.valueOf(aReceiverInterval);
			bReceiverInterval = bReceiverInterval + " m";
		}

		infoHeader.setText(
		        /* Column One */
		        "<html>" +
		        "<table cellspacing=0 cellpadding=0>" +
		        "<tr>" +
		        "<td width=" + buffersize0 + "></td>" +
		        "<td width=" + col1labelsize + "><font size=" + labeltext + "><b>Client :</b></font></td>" +
		        "<td width=" + buffersize1 + "></td>" +
		        "<td width=" + col1blanksize + " align=left><font size=" + blanktext + ">" + aClient + "</font></td>" +
		        "<td width=" + buffersize2 + "></td>" +
		        "<td width=" + col2labelsize + "><font size=" + labeltext + "><b>Line Number :</b></font></td>" +
		        "<td width=" + buffersize1 + "></td>" +
		        "<td width=" + col2blanksize + " align=left><font size=" + blanktext + ">" + aLineNumber + "</font></td>" +
		        "<td width=" + buffersize2 + "></td>" +
		        "<td width=" + col3labelsize + "></td>" +
		        "<td width=" + buffersize1 + "></td>" +
		        "<td width=" + col3blanksize + "></td>" +
		        "<td width=" + buffersize2 + "></td>" +
		        "<td width=" + col4labelsize + "><font size=" + labeltext + "><b>Date :</b></font></td>" +
		        "<td width=" + buffersize1 + "></td>" +
		        "<td width=" + col4blanksize + " align=left><font size=" + blanktext + ">" + aDate + "</font></td>" +
		        "<td width=" + buffersize0 + "></td>" +
		        "</tr>" +

		        /* Column Two */
		        "</table>" +
		        "<table cellspacing=0 cellpadding=0>" +
		        "<tr>" +
		        "<td width=" + buffersize0 + "></td>" +
		        "<td width=" + col1labelsize + "><font size=" + labeltext + "><b>Shot Pattern :</b></font></td>" +
		        "<td width=" + buffersize1 + "></td>" +
		        "<td width=" + col1blanksize + " align=left><font size=" + blanktext + ">" + aPattern + "</font></td>" +
		        "<td width=" + buffersize2 + "></td>" +
		        "<td width=" + col2labelsize + "><font size=" + labeltext + ">Line Direction :</font></td>" +
		        "<td width=" + buffersize1 + "></td>" +
		        "<td width=" + col2blanksize + " align=left><font size=" + blanktext + ">" + aLineDirection + "</font></td>" +
		        "<td width=" + buffersize2 + "></td>" +
		        "<td width=" + col3labelsize + "><font size=" + labeltext + ">SP Spacing :</font></td>" +
		        "<td width=" + buffersize1 + "></td>" +
		        "<td width=" + col3blanksize + " align=left><font size=" + blanktext + ">" + bShotPointSpacing + "</font></td>" +
		        "<td width=" + buffersize2 + "></td>" +
		        "<td width=" + col4labelsize + "><font size=" + labeltext + ">Receiver Interval :</font></td>" +
		        "<td width=" + buffersize1 + "></td>" +
		        "<td width=" + col4blanksize + " align=left><font size=" + blanktext + ">" + bReceiverInterval + "</font></td>" +
		        "<td width=" + buffersize0 + "></td>" +
		        "</tr>" +

		        /* Column Three */
		        "</table>" +
		        "<table cellspacing=0 cellpadding=0>" +
		        "<tr>" +
		        "<td width=" + buffersize0 + "></td>" +
		        "<td width=" + col1labelsize + "><font size=" + labeltext + ">Comments :</font></td>" +
		        "<td width=" + buffersize1 + "></td>" +
		        "<td width=" + commentblanksize + "  align=left><font size=" + blanktext + ">" + aComments + "</td>" +
		        "<td width=" + buffersize2 + "></td>" +
		        "<td width=" + col3labelsize + "><font size=" + labeltext + ">Page :</font></td>" +
		        "<td width=" + buffersize1 + "></td>" +
		        "<td width=" + col3blanksize + " align=left><font size=" + blanktext + ">" + currentPage + " of " + numPages + "</font></td>" +
		        "<td width=" + buffersize2 + "></td>" +
		        "<td width=" + col4labelsize + "><font size=" + labeltext + ">Compiled By :</font></td>" +
		        "<td width=" + buffersize1 + "></td>" +
		        "<td width=" + col4blanksize + " align=left><font size=" + blanktext + ">" + aCompiledBy + "</font></td>" +
		        "<td width=" + buffersize0 + "></td>" +
		        "</tr>" +
		        "</table>" +
		        "</html>"
		);

		// Logo Panel Stuff ***********************************************************************
		/* initialize the logo panel properties */
		logoPanel.setLayout(new GridBagLayout());
		logoPanel.setBackground(Color.white);
		logoPanel.setBorder(new LineBorder(Color.black, 1));
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridwidth = 1;
		constraints.gridheight = 3;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.insets = new Insets(5, 15, 5, 15);
//
//		// Add the Client Logo
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridx = 0;
		constraints.gridy = 0;
		logoPanel.add(clientLogo, constraints);
//
//		// Add the Wolf Logo
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridx = 2;
		constraints.gridy = 0;
		logoPanel.add(wolfLogo, constraints);

//		// Add the Prospect Name, and a title
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 100;
		constraints.weighty = 0;
//
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 1;
		constraints.gridy = 0;
		logoPanel.add(infoTitle, constraints);

//		constraints.gridx = 1;
//		constraints.gridy = 0;
//		prospect.setPreferredSize(new Dimension(400, 20));
//		logoPanel.add(prospect, constraints);
//
//		constraints.gridx = 1;
//		constraints.gridy = 1;
//		logoPanel.add(title, constraints);

		// Column One Stuff ***********************************************************************
		infoPanel.setLayout(new BorderLayout());
		infoPanel.setBorder(new LineBorder(Color.black, 1));
		infoPanel.setBackground(Color.white);
		infoPanel.add(infoHeader, BorderLayout.CENTER);

		// Header Panel Stuff *********************************************************************
		/* initialize the header panel properties */
		this.setBackground(Color.white);
		headerBox = Box.createVerticalBox();
		headerBox.setBackground(Color.white);
		headerBox.setBorder(new LineBorder(Color.black, 1));
		headerBox.add(logoPanel, BorderLayout.NORTH);
		headerBox.add(infoPanel, BorderLayout.CENTER);

		this.add(headerBox, BorderLayout.NORTH);

		/* determines which type of map to display */
		/* Megabin map selected */
		if (typeSelected == 0)
		{
			/* spawn a Megabin map */
			megabin = new MegaBin(bol, pointsPerLine, pagePoints, aShotPointSpacing, aReceiverInterval, pointType);
			megabin.setBorder(new LineBorder(Color.black, 2));
			this.add(megabin, BorderLayout.CENTER);
		}
		/* 2D map selected */
		else if (typeSelected == 1)
		{
			/* spawn a 2D map */
			map2d = new Map2D(bol, pointsPerLine, pagePoints, aShotPointSpacing, aReceiverInterval, pointType);
			map2d.setBorder(new LineBorder(Color.black, 2));
			this.add(map2d, BorderLayout.CENTER);
		}
		/* 2D X-Station map selected */
		else if (typeSelected == 2)
		{
			/* spawn a 2D X-Station map */
			mapxs = new MapXS(bol, pointsPerLine, pagePoints, aShotPointSpacing, aReceiverInterval, pointType);
			mapxs.setBorder(new LineBorder(Color.black, 2));
			this.add(mapxs, BorderLayout.CENTER);
		}
		/* 3D Source map selected */
		else if (typeSelected == 3)
		{
			/* spawn a 3D Source map */
			map3ds = new Map3DS(bol, pointsPerLine, pagePoints);
			map3ds.setBorder(new LineBorder(Color.black, 2));
			this.add(map3ds, BorderLayout.CENTER);
		}
		/* 3D Receiver map selected */
		else
		{
			/* spawn a 3D Receiver map */
			map3dr = new Map3DR(bol, pointsPerLine, pagePoints);
			map3dr.setBorder(new LineBorder(Color.black, 2));
			this.add(map3dr, BorderLayout.CENTER);
		}
	}

	public int currentPage()
	{
		return currentPage;
	}

	// Header Info Stuff **************************************************************************

	/* the JLabel to contain the HTML */
	private JLabel infoHeader = new JLabel();
	/* distance between borders and content */
	private int buffersize0 = 10;
	/* distance from end of labels to start of blanks */
	private int buffersize1 = 10;
	/* distance from end of blanks to start of labels */
	private int buffersize2 = 20;
	/* size of the header font */
	private int headerText = 4;
	/* size of the label font */
	private int labeltext = 3;
	/* size of the blank font */
	private int blanktext = 2;
	/* width of the first label column */
	private int col1labelsize = 80;
	/* width of the second label column */
	private int col2labelsize = 100;
	/* width of the third label column */
	private int col3labelsize = 80;
	/* width of the fourth label column */
	private int col4labelsize = 120;
	/* width of the first blank column */
	private int col1blanksize = 138;
	/* width of the second blank column */
	private int col2blanksize = 128;
	/* width of the third blank column */
	private int col3blanksize = 58;
	/* width of the fourth blank column */
	private int col4blanksize = 128;
	/* width of the comment blank column */
	private int commentblanksize = col1blanksize + buffersize2 + col2labelsize + buffersize1 + col2blanksize;
	/* the JLabel to contain the title HTML */
	private JLabel infoTitle = new JLabel();
	/* width of the title column */
	private int titlewidth = 200;
	/* size of title font */
	private int titletext = 3;
	/* for checking to see if SPS and RI are null */
	private String bShotPointSpacing = null;
	private String bReceiverInterval = null;


	// Header Panel Stuff *************************************************************************
	private Box headerBox;

	// Logo Panel Stuff ***************************************************************************
	private JPanel logoPanel = new JPanel();

	private JLabel clientLogo;
	/* use default path for Wolf logo */
	private JLabel wolfLogo = new JLabel(new ImageIcon("SmokerGI/2ww.adm"));

	private JLabel prospect = new JLabel();
	private JLabel title = new JLabel("LINE CHAINING NOTES");

	// Info Panel Stuff : Row 1 *******************************************************************
	private JPanel infoPanel = new JPanel();

	private JLabel clientLabel = new JLabel("Client");
	private JLabel lineNumberLabel = new JLabel("Line Number");
	private JLabel dateLabel = new JLabel("Date");

	private JLabel client = new JLabel();
	private JLabel lineNumber = new JLabel();
	private JLabel date = new JLabel();

	// Info Panel Stuff : Row 2 *******************************************************************

	private JLabel patternLabel = new JLabel("Shot Pattern");
	private JLabel lineDirectionLabel = new JLabel("Line Direction");
	private JLabel shotPointSpacingLabel = new JLabel("Shot Point Spacing");
	private JLabel receiverIntervalLabel = new JLabel("Receiver Interval");

	private JLabel pattern = new JLabel();
	private JLabel lineDirection = new JLabel();
	private JLabel shotPointSpacing = new JLabel();
	private JLabel receiverInterval = new JLabel();

	// Info Panel Stuff : Row 3 *******************************************************************

	private JLabel commentsLabel = new JLabel("Comments");
	private JLabel pageLabel = new JLabel("Page");
	private int currentPage;
	private JLabel compiledByLabel = new JLabel("Compiled By");

	private JLabel comments = new JLabel();
	private JLabel page = new JLabel();
	private JLabel compiledBy = new JLabel();

	//  Spawn Map Stuff ***************************************************************************
	/* instances of all the map types */
	private MegaBin megabin;
	private Map2D map2d;
	private MapXS mapxs;
	private Map3DS map3ds;
	private Map3DR map3dr;
}
