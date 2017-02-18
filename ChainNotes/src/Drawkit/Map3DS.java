package Drawkit;

import java.awt.*;
import java.awt.image.*;
import java.lang.String.*;
import javax.swing.*;

public class Map3DS extends JPanel
{

	/* catches fraction of pixels in calculations */
	float remainder = 0;
	/* margin between mapped lines and map bounds */
	int margin_m = 30;
	/* x co-ordinate of flag */
	int pointX;
	/* y co-ordinate of flag */
	int pointY;
// --Recycle Bin START (7/10/03 3:27 PM):
//	/* number of points per label */
//	int pointSet = 4;
// --Recycle Bin STOP (7/10/03 3:27 PM)
	/* distance between each marked point on mapped lines */
	int pointDist = 15;
	/* points per line to be plotted (must be a multiple of four) */
	int pointsPerLine = 20;
	/* number of points to be displayed for this page */
	int pointTotal;
	/* value of the current flag point */
	int pointInit;
	/* Image to store the flag */
	Image flagImg;
	/* Image to store the rotated labels of flag points */
	Image labelImg;

	/* necessary for label rotation */
	public static Frame offFrame = null;

	public Map3DS(int PointInit, int pointsPerLine, int pointTotal)
	{

		/* necessary for label rotation */
		if (offFrame == null)
		{
			offFrame = new Frame();
			offFrame.setBackground(Color.white);
			offFrame.addNotify();
		}

		/* store parameters passed in */
		this.pointInit = PointInit;
		this.pointsPerLine = pointsPerLine;
		this.pointTotal = pointTotal;

		/* set properties of window */
		setBackground(Color.white);
		setPreferredSize(new Dimension(955, 450));
	}

	public void paintComponent(Graphics g)
	{

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		/* width of the map */
		int width = this.getSize().width;
		/* height of the map */
		int height = this.getSize().height;

		/* new variables are made so values are initialized on redraws */
		/* value of the current point */
		int pointValue = pointInit;
		/* the number of points per row */
		int pointsPerLine2 = pointsPerLine;
		/* the number of points to be drawn on this page*/
		int pointRemain = pointTotal;

		/* get current font information */
		FontMetrics fm = g2.getFontMetrics();

		/* define the properties of the label font */
		//Font font = new Font("Arial", Font.PLAIN, 10);
		Font font = new Font("Verdana", Font.PLAIN, 10);
	    setFont(font);
		
		/* counter to keep track of current row */
		int rowIndex;
		/* arbitrary counter to keep track of current x location */
		int x;

		this.setBackground(Color.white);

		/* create a flag image */
		flagImg = createFlag(getBackground(), getForeground());

		// working version (half pointDist)
		/* algorithm to calculate the distance between each point,
		   while leaving half of this distance as each margin */
		/* store the distance between points as an integer */
		pointDist = width / pointsPerLine;
		/* store the distance between points as a float */
		remainder = (float) width / (float) pointsPerLine;
		/* calculate the fractional parts of the distance */
		remainder -= (float) pointDist;
		/* add the fractional part to the integer distance,
		   in case it is greater than 1 */
		pointDist += (int) remainder;
		/* calculate the margins to be half of one distance */
		margin_m = pointDist / 2;


		/*
		// working version (full pointDist)
		pointDist = width - margin_m;
		remainder = (float) pointDist;
		pointDist = pointDist/pointsPerLine;
		remainder = remainder/(float) pointsPerLine;
		remainder -= (float) pointDist;
		pointDist += (int) remainder;
		*/

		/* draw separator lines to divide page into three rows*/
		g2.drawLine(0, height / 3, width, height / 3);
		g2.drawLine(0, (height * 2) / 3, width, (height * 2) / 3);

		/* draw the points for the three rows */
		for (rowIndex=0; rowIndex<3; rowIndex++)
		{
			switch (rowIndex)
			{
				case 0:
					/* initialize y-axis for first row */
					pointY = (height / 3) / 2;
					break;
				case 1:
					/* initialize y-axis for second row */
					pointY = (((height * 2) / 3) + (height / 3)) / 2;
					break;
				case 2:
					/* initialize y-axis for third row */
					pointY = (height + ((height * 2) / 3)) / 2;
					break;
			}

			/* initialize x-axis for row */
			pointX = width - margin_m;

			/* check if points still need to be drawn */
			if (pointRemain > 0)
			{
				/* check if less than all points on line need to be drawn */
				if (pointRemain < pointsPerLine2)
				{
					/* only draw the remaining points */
					pointsPerLine2 = pointRemain;
				}

				/* draw points for first mapped line */
				for (x = 0; x < pointsPerLine2; x++)
				{
					/* draw the flag image */
					g2.drawImage(flagImg, pointX, pointY - 15, this);

					/* create the flag's label image */
					labelImg = rotateText(String.valueOf(pointValue), fm, getFont(), getBackground(), getForeground());
					/* draw the flag's label image */
					g2.drawImage(labelImg, pointX - 6, pointY + 10, this);
					/* increment the label counter */
					pointValue++;

					/* shift along x-axis of the mapped line */
					pointX -= pointDist;

					/* decrement the point counter */
					pointRemain--;
				}
			}
		}
	}

	/* original method created by David Risner */
	/* method to rotate the text for the labels used by the flag points, by drawing
	   the text to an Image and returning the Image */
	public Image rotateText(String s, FontMetrics fm, Font font, Color bg, Color fg)
	{
		/* width of the Image to be created */
		int width;
		/* height of the Image to be created */
		int height;
		/* counter to traverse columns of Image information */
		int x;
		/* counter to traverse rows of Image information */
		int y;

		/* array to store first Image's information */
		int[] pixels1;
		/* array to store second Image's information */
		int[] pixels2;
		Image img1;
		Image img2;
		Graphics g;
		PixelGrabber pg;
		MediaTracker mt;
		MemoryImageSource mis;

		Graphics gfx = offFrame.getGraphics();

		if (fm == null)
		{
			System.out.println("FontMetrics null.\n");
			return null;
		}

		width = fm.stringWidth(s) + 2;
		height = fm.getMaxAscent() + fm.getMaxDescent() + 2;

		pixels1 = new int[width * height];
		img1 = createImage(width, height);
		pg = new PixelGrabber(img1, 0, 0, width, height, pixels1, 0, width);

		g = img1.getGraphics();
		g.setColor(bg);
		g.fillRect(0, 0, width, height);
		g.setColor(fg);
		g.setFont(font);
		g.drawString(s, 1, fm.getMaxAscent() + 1);
		g.dispose();
		gfx.drawImage(img1, 0, 0, this);
		gfx.dispose();

		try
		{
			pg.grabPixels();
		} catch (InterruptedException ie) {}

		pixels2 = new int[width * height];
		for (y = 0; y < height; ++y)
		{
			for (x = 0; x < width; ++x)
			{
				pixels2[x * height + y] = pixels1[y * width + (width - x - 1)];
			}
		}

		mis = new MemoryImageSource(height, width, ColorModel.getRGBdefault(), pixels2, 0, height);
		img2 = createImage(mis);
		mt = new MediaTracker(this);
		mt.addImage(img2, 0);

		try
		{
			mt.waitForAll();
		} catch (InterruptedException ie) {}

		return img2;
	}

	/* method to create the generic flag Image */
	public Image createFlag(Color bg, Color fg)
	{
		/* width of the flag Image */
		int width = 7;
		/* height of the flag Image */
		int height = 17;

		/* array to store Image information */
		int[] flagMap;
		/* the actual flag Image */
		Image flagImage;
		Graphics g;
		PixelGrabber pg;

		Graphics gfx = offFrame.getGraphics();

		/* create array to store one pixel per element */
		flagMap = new int[width * height];
		/* create Image with specified dimensions */
		flagImage = createImage(width, height);
		pg = new PixelGrabber(flagImage, 0, 0, width, height, flagMap, 0, width);

		/* initialize the Image properties */
		g = flagImage.getGraphics();
		g.setColor(bg);
		g.fillRect(0, 0, width, height);
		g.setColor(fg);

		/* draw the Image */
		g.drawLine(1, 15, 1, 0);
		g.drawLine(1, 0, 7, 3);
		g.drawLine(1, 6, 7, 3);
		g.fillOval(0, 14, 3, 3);

		/*
		g.drawLine(0, 15, 0, 0);
		g.drawLine(0, 0, 6, 3);
		g.drawLine(0, 6, 6, 3);
		*/

		/* dispose of useless information */
		g.dispose();
		gfx.drawImage(flagImage, 0, 0, this);
		gfx.dispose();

		return flagImage;
	}
}