import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import java.util.StringTokenizer;

public class DEM
{
	public static void main(String[] args)
	{
		DEMFrame frame = new DEMFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.show();
	}
}

class DEMFrame extends JFrame
{
	public DEMFrame()
	{
		// create a file dialog to import the DEM file
		FileDialog fd = new FileDialog(DEMFrame.this);
		fd.setLocation(250, 250);
		fd.setDirectory("");
		fd.setVisible(true);

		// once the file is imported, call the readDEM function to read the data
		if (fd.getFile() != null)
		{
			try
			{
				BufferedReader in = new BufferedReader(new FileReader(fd.getDirectory() + new String(fd.getFile())));
				readDEM(in);
				in.close();
			} catch (IOException exception)
			{
				System.out.println("IOException Thrown: 1");
				exception.printStackTrace();
			}
		}

		// provide a small jframe to input the spacing
		Container contentPane = getContentPane();
		panel.setLayout(new GridLayout(2,2));

		panel.add(intervalLabel);
		panel.add(intervalField);
		panel.add(startAtLabel);
		panel.add(startAtField);

		contentPane.add(panel, BorderLayout.CENTER);
		contentPane.add(okButton, BorderLayout.SOUTH);

		this.pack();
//		this.setResizable(false);
		this.setLocation(200, 200);

		// once the user picks his spacing, do all this crap
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				int i = 0;
				double distance = 0;
				double overshoot = 0;
				Point p1 = null, p2 = null, receiver = null;

				// convert what the user types into a usable integer
				try
				{
					interval = Integer.parseInt(intervalField.getText().trim());
				} catch (NumberFormatException nfe)
				{
				}
				try
				{
					startAt = Integer.parseInt(startAtField.getText().trim());
				} catch (NumberFormatException nfe)
				{
				}

				setVisible(false);      // you can close the dialog now

				// make sure we dont go out of bounds on the DEM Vector
				while (i < DEMVector.size() - 1)
				{
					// check to make sure that the distance doesnt exceed the alloted interval.
					while ((distance < interval) && (i < DEMVector.size() - 1))
					{
						p1 = (Point) DEMVector.elementAt(i);
						p2 = (Point) DEMVector.elementAt(i + 1);

						distance += hypotenues(p1, p2);

						i++;
					}

					// find out how far past the interval we went by adding that last point
					if(distance > interval)
					{
						overshoot = distance - interval;

						// now we have to interpolate where the last point would be if we hadn't overshot it
						// The point this call returns is the point that we want to save.
						ReceiverVector.addElement(zeroBack(p1, p2, overshoot));
						distance = overshoot;
					}
					receiver = null;
					overshoot = 0;
				}

				// now open up a file dialog to save the file to.
				FileDialog fd = new FileDialog(DEMFrame.this, "Save file...");
				fd.setDirectory("");
				fd.setLocation(100, 100);
				fd.setVisible(true);

				if (fd.getFile() != null)
				{
					try
					{
						// save data to file by calling the writeDEM function
						PrintWriter out = new PrintWriter(new FileWriter(fd.getDirectory() + new String(fd.getFile())));
						writeDEM(out);
						out.close();
					} catch (IOException exception)
					{
						System.out.println("IOException Thrown: 2");
						exception.printStackTrace();
					}
				}
				System.exit(0);
			}
		});
	}

	// pretty simple - just read in each line, then store the data as a Point object
	private void readDEM(BufferedReader in) throws IOException
	{
		double easting = 0;
		double northing = 0;
		double elevation = 0;
		Point point;

		String s = in.readLine();           // read in first line

		while (s != null)                   // eof returns a null
		{
			StringTokenizer t = new StringTokenizer(s, ",");
			if (t.countTokens() == 3)
			{
				// convert the text fields into integers
				try
				{
					easting = Double.parseDouble(t.nextToken().trim());

				} catch (NumberFormatException nfe)
				{
				}

				try
				{
					northing = Double.parseDouble(t.nextToken().trim());
				} catch (NumberFormatException nfe)
				{
				}

				try
				{
					elevation = Double.parseDouble(t.nextToken().trim());
				} catch (NumberFormatException nfe)
				{
				}

				// make your point, and then put it into our vector
				point = new Point(easting, northing, elevation);
				DEMVector.addElement(point);
			}
			s = in.readLine();          // now repeat
		}
		// add the first element of the file to the Receiver Vector
		point = (Point) DEMVector.firstElement();
		ReceiverVector.addElement(point);
	}

	void writeDEM(PrintWriter out) throws IOException
	{
		Point writePoint = null;
		double E, N, V;

		out.println("Line Number, Easting, Northing, Elevation");
		for (int i = 0; i < ReceiverVector.size(); i++)
		{
			// first get the point and save it in writePoint, then to clean up the output a little bit,
			// use Math.rint() to set the precision to two decimal places.
			writePoint = (Point) ReceiverVector.elementAt(i);
			E = Math.rint(writePoint.Easting() * 100) / 100 ;
			N = Math.rint(writePoint.Northing() * 100) / 100;
			V = Math.rint(writePoint.Elevation() * 100) / 100;

			out.println(startAt + ", " + E + ", " + N + ", " + V);
			startAt++;
		}
	}

	// check your highschool mathbooks, 'cause this just calculates the hypotenues of two points in three dimensional space.
	private double hypotenues(Point p1, Point p2)
	{
		double deltaE = Math.abs(p2.Easting() - p1.Easting());
		double deltaN = Math.abs(p2.Northing() - p1.Northing());
		double deltaV = Math.abs(p2.Elevation() - p1.Elevation());

		return Math.sqrt(deltaE * deltaE + deltaN * deltaN + deltaV * deltaV);
	}

	private Point zeroBack(Point p1, Point p2, double overshoot)
	{
		// need the distance between p1 and p2
		double distance = hypotenues(p1, p2);

		// and the ratio (or percentage) of how far from p1 the point at overshoot is
		double ratio = 1- (overshoot / distance);

		// find (deltaEasing, deltaNorthing, deltaElevation) between Point p1 and p2,
		// then multiply by our ratio above to inerpolate where the point of overshoot is (relative to p1)
		// then add back p1(E,N,V) to get the absolute value for the easting, northing, and elevation.
		double E = p1.Easting   () + (ratio * (Math.abs(p2.Easting   () - p1.Easting   () )));
		double N = p1.Northing  () + (ratio * (Math.abs(p2.Northing  () - p1.Northing  () )));
		double V = p1.Elevation () + (ratio * (Math.abs(p2.Elevation () - p1.Elevation () )));

		return new Point(E, N, V);

	}

	// some attributes
	private JPanel panel = new JPanel();

	private JLabel intervalLabel = new JLabel("Interval : Default = 70  ");
	private JTextField intervalField = new JTextField(5);
	private int interval = 70;

	private JLabel startAtLabel = new JLabel("Start At : Default = 101");
	private JTextField startAtField = new JTextField(5);
	private int startAt = 101;

	private JButton okButton = new JButton("OK");

	private Vector DEMVector = new Vector();
	private Vector ReceiverVector = new Vector();
}

// here is our point class - pretty self explanitory...
// make a point by calling the constructor, some gets, and a handy print function
class Point
{
	public Point(double e, double n, double v)
	{
		Easting = e;
		Northing = n;
		Elevation = v;
	}

	public double Easting()
	{
		return Easting;
	}

	public double Northing()
	{
		return Northing;
	}

	public double Elevation()
	{
		return Elevation;
	}

	public void print()
	{
		System.out.println(Easting() + ", " + Northing() + ", " + Elevation());
	}

	private double Easting;
	private double Northing;
	private double Elevation;
}
