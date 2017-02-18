package SmokerGI;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.Date;
import java.lang.String;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.*;

public class SmokerGI
{
	public static void main(String[] args)
	{
		SmokerGI_Frame frame = new SmokerGI_Frame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.show();
	}
}

class SmokerGI_Frame extends JFrame
{
	public SmokerGI_Frame()
	{
		// MenuBar Stuff **************************************************************************
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');

		newItem = fileMenu.add(new MenuAction("New"));
		newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		newItem.setMnemonic('N');

		saveItem = fileMenu.add(new MenuAction("Save"));
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		saveItem.setMnemonic('S');

		openItem = fileMenu.add(new MenuAction("Load"));
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		openItem.setMnemonic('L');

		fileMenu.addSeparator();

		importItem = fileMenu.add(new MenuAction("Import File"));
		importItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
		importItem.setMnemonic('I');

		fileMenu.addSeparator();

		// Exit the program
		fileMenu.add(new AbstractAction("Exit")
		{
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		});

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');

		helpItem = helpMenu.add(new MenuAction("Help Topics"));
		helpItem.setMnemonic('T');
		helpMenu.add(helpItem);

		helpMenu.addSeparator();

		licenceItem = helpMenu.add(new MenuAction("Licence"));
		licenceItem.setMnemonic('L');
		helpMenu.add(licenceItem);

		// you can also add the mnemonic key to an action
		Action aboutAction = new MenuAction("About");
		aboutAction.putValue(Action.MNEMONIC_KEY, new Integer('A'));
		helpMenu.add(aboutAction);

		// add all top-level menus to menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

		// Mouse listener for menubar
		getContentPane().addMouseListener(new MouseAdapter()
		{
			public void mouseReleased(MouseEvent event)
			{
				if (event.isPopupTrigger())
					popup.show(event.getComponent(), event.getX(), event.getY());
			}
		});

		// Restore previous session data at beggining of session **********************************
		try
		{
			BufferedReader in = new BufferedReader(new FileReader("restore.dat"));
			readData(in);
			in.close();

		} catch (IOException exception)
		{
			System.out.println("IOException Thrown: 1");
			exception.printStackTrace();
		}

		// CONSTRUCT THE COMPONENTS : SORTED BY PANEL *********************************************

		// North Panel Stuff **********************************************************************
		northPanel.add(wolfLogo);

		// South Panel Stuff **********************************************************************
		southPanel = new JPanel();
		quitButton = new JButton("Quit");
		quitButton.addActionListener(new QuitAction());
		createButton = new JButton("Create");
		createButton.addActionListener(new CreateAction());

		southPanel.add(createButton);
		southPanel.add(quitButton);

		// Left Panel Stuff ***********************************************************************
		GridBagConstraints constraints = new GridBagConstraints();
		leftPanel.setLayout(new GridBagLayout());
		leftPanel.setBorder(new EtchedBorder());

		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 100;
		constraints.insets = new Insets(5, 5, 5, 5);

		constraints.gridx = 0;
		constraints.gridy = 0;
		leftPanel.add(clientLabel, constraints);

		constraints.gridy = 1;
		leftPanel.add(clientLogoLabel, constraints);

		constraints.gridy = 2;
		leftPanel.add(prospectLabel, constraints);

		constraints.gridy = 3;
		leftPanel.add(shotPointSpacingLabel, constraints);

		constraints.gridy = 4;
		leftPanel.add(receiverIntervalLabel, constraints);

		constraints.gridy = 5;
		leftPanel.add(patternLabel, constraints);

		constraints.gridy = 6;
		leftPanel.add(commentsLabel, constraints);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weightx = 100;
		constraints.weighty = 100;

		constraints.gridx = 1;
		constraints.gridy = 0;
		leftPanel.add(client, constraints);

		constraints.gridy = 1;
		constraints.gridwidth = 2;
		leftPanel.add(clientLogoPath, constraints);

		// load a saved file
		openFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				FileDialog fd = new FileDialog(SmokerGI_Frame.this, "Open File...");
				fd.setDirectory("");
				fd.setLocation(100, 100);
				fd.setVisible(true);

				if (fd.getFile() != null)
				{
					clientLogoPath.setText(fd.getDirectory() + new String(fd.getFile()));
				}
			}
		});

		constraints.gridx = 3;
		constraints.gridwidth = 1;
		constraints.weightx = 0;
		constraints.ipadx = 0;
		constraints.insets = new Insets(5, 0, 5, 5);
		leftPanel.add(openFile, constraints);
		constraints.gridx = 1;
		constraints.gridwidth = 3;
		constraints.weightx = 100;
		constraints.insets = new Insets(5, 5, 5, 5);

		constraints.gridy = 2;
		leftPanel.add(prospect, constraints);

		constraints.gridy = 3;
		leftPanel.add(shotPointSpacing, constraints);

		constraints.gridy = 4;
		leftPanel.add(receiverInterval, constraints);

		constraints.gridy = 5;
		leftPanel.add(pattern, constraints);

		constraints.gridy = 6;
		leftPanel.add(comments, constraints);

		// Right Panel Stuff **********************************************************************
		constraints = new GridBagConstraints();
		rightPanel.setLayout(new GridBagLayout());
		rightPanel.setBorder(new EtchedBorder());

		// Format the current time.
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", java.util.Locale.CANADA);
		Date MoYr = new Date();
		date.setText(formatter.format(MoYr));

		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 100;
		constraints.insets = new Insets(5, 5, 5, 5);

		constraints.gridx = 0;
		constraints.gridy = 0;
		rightPanel.add(dateLabel, constraints);

		constraints.gridy = 1;
		rightPanel.add(compiledByLabel, constraints);

		constraints.gridy = 2;
		rightPanel.add(lineNumberLabel, constraints);

		constraints.gridy = 3;
		rightPanel.add(lineDirectionLabel, constraints);

		constraints.gridy = 4;
		rightPanel.add(pointsPerLineLabel, constraints);

		constraints.gridy = 5;
		rightPanel.add(bolLabel, constraints);

		constraints.gridy = 6;
		rightPanel.add(eolLabel, constraints);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 100;
		constraints.weighty = 100;

		constraints.gridx = 1;
		constraints.gridy = 0;
		rightPanel.add(date, constraints);

		constraints.gridy = 1;
		rightPanel.add(compiledBy, constraints);

		constraints.gridy = 2;
		rightPanel.add(lineNumber, constraints);

		constraints.gridy = 3;
		rightPanel.add(lineDirection, constraints);

		constraints.gridy = 4;
		rightPanel.add(pointsPerLine, constraints);

		constraints.gridy = 5;
		rightPanel.add(bol, constraints);

		constraints.gridy = 6;
		rightPanel.add(eol, constraints);

		// Type Panel Stuff ***********************************************************************
		constraints = new GridBagConstraints();
		typePanel.setLayout(new GridBagLayout());
		typePanel.setBorder(new TitledBorder(" Line Type "));

//		typeButtonGroup.add(megaBin);
		typeButtonGroup.add(twoD);
		typeButtonGroup.add(xStat);
		typeButtonGroup.add(source);
		typeButtonGroup.add(receiver);

		twoD.setSelected(true);

		// action listeners just set the line type
		megaBin.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				typeSelected = 0;
			}
		});

		twoD.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				typeSelected = 1;
			}
		});

		xStat.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				typeSelected = 2;
			}
		});

		source.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				typeSelected = 3;
			}
		});

		receiver.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				typeSelected = 4;
			}
		});

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 100;
		constraints.weighty = 100;

//		constraints.gridy = 0;
//		constraints.gridx = 0;
//		typePanel.add(megaBin, constraints);

		constraints.gridx = 1;
		typePanel.add(twoD, constraints);

		constraints.gridx = 2;
		typePanel.add(xStat, constraints);

		constraints.gridx = 3;
		typePanel.add(source, constraints);

		constraints.gridx = 4;
		typePanel.add(receiver, constraints);

		// Center Panel Stuff *********************************************************************
		constraints = new GridBagConstraints();
		centerPanel.setLayout(new GridBagLayout());

		// Left and Right Panel Stuff
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 100;
		constraints.weighty = 100;

		// Add Left Panel
		constraints.insets = new Insets(10, 10, 10, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;
		centerPanel.add(leftPanel, constraints);

		// Add Right Panel
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.insets = new Insets(10, 5, 10, 10);
		centerPanel.add(rightPanel, constraints);

		// Type Panel Stuff
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 100;
		constraints.weighty = 10;

		// Add Type Panel
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 50, 10, 50);
		centerPanel.add(typePanel, constraints);

		// Add Panels to JFrame BorderLayout ******************************************************
		Container contentPane = getContentPane();

		contentPane.add(northPanel, BorderLayout.NORTH);
		contentPane.add(southPanel, BorderLayout.SOUTH);
		contentPane.add(centerPanel, BorderLayout.CENTER);

		this.setTitle("Wolf Survey and Mapping : Chain Notes v0.9");
		this.setLocation(100, 100);
		this.pack();
		this.setResizable(false);
	}

	/**********************************************************************************************
	 * QuitAction implements ActionListener
	 *  Purpose:
	 *      The Quit button automatically saves information currently inside the text field
	 *      inside a text file called restore.dat
	 *  Use:
	 *      Called when the "quit" button is clicked.  Output information currently inside
	 *      text fields to an output file called 'restore.dat'
	 **********************************************************************************************/
	private class QuitAction implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			try
			{
				// save current data to file for autoloading before quiting program
				PrintWriter out = new PrintWriter(new FileWriter("restore.dat"));
				writeData(out);
				out.close();
			} catch (IOException exception)
			{
				System.out.println("IOException Thrown: 2");
				exception.printStackTrace();
			}
			System.exit(0);
		}
	}

	/**********************************************************************************************
	 * CreateAction implements ActionListener
	 *  Purpose:
	 *      Creates a new set of chaining notes and displays them on screen
	 * Use:
	 *      Called when "create" button is clicked.  Take information currently inside
	 *      text fields, test for errors: if none, create notes
	 *                                    else, provide warning
	 ***********************************************************************************************/
	private class CreateAction implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			int ppl = 0; // points per line
			int sps = 0; // shot point spacing
			int ri = 0;  // receiver interval
			int b = 0;   // bol
			int e = 0;   // eol

			// convert the text fields into integers
			try
			{
				ppl = Integer.parseInt(pointsPerLine.getText().trim());
			} catch (NumberFormatException nfe)
			{
			}

			try
			{
				sps = Integer.parseInt(shotPointSpacing.getText().trim());
			} catch (NumberFormatException nfe)
			{
			}

			try
			{
				ri = Integer.parseInt(receiverInterval.getText().trim());
			} catch (NumberFormatException nfe)
			{
			}

			try
			{
				b = Integer.parseInt(bol.getText().trim());
			} catch (NumberFormatException nfe)
			{
			}

			try
			{
				e = Integer.parseInt(eol.getText().trim());
			} catch (NumberFormatException nfe)
			{
			}

			if (!((typeSelected <= 2) && ((sps < 1) || (ri < 1))))      // if map type is 2D or 2D-X-station, sps and ri must be positive
			{
				if (!((typeSelected <= 2) && (sps % ri != 0)))          // as well, sp spacing must be divisible by receiver interval
				{
					if ((ppl > 0) && (b > 0) && (e >= b))               // in all cases, ppl, bol, eol, all must be positive
					{
						Intermediate frame = new Intermediate(client.getText(), clientLogoPath.getText(), prospect.getText(),
						                                      sps, ri, pattern.getText(), comments.getText(), date.getText(),
						                                      compiledBy.getText(), lineNumber.getText(), lineDirection.getText(),
						                                      ppl, b, e, typeSelected);
						frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
						frame.show();
					}
					else
					{
						String message = "Unknown Error";
						if (ppl <= 0)
							message = "Points Per Line must be a positive integer";
						if (b <= 0)
							message = "BOL must be a positive integer";
						if (e < b)
							message = "EOL must be a poitive integer larger than BOL";

						JOptionPane.showMessageDialog(null, message, null, JOptionPane.WARNING_MESSAGE);
					}
				}
				else
				{
					String message = "Shot Point Spacing must be evenly divisible by Receiver Interval";
					JOptionPane.showMessageDialog(null, message, null, JOptionPane.WARNING_MESSAGE);
				}
			}
			else
			{
				String message = "Unknown Error";
				if (sps < 1)
					message = "Shot Point Spacing must be a positive integer";
				if (ri < 1)
					message = "Receiver Interval must be a positive integer";

				JOptionPane.showMessageDialog(null, message, null, JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	/**********************************************************************************************
	 * void writeData(PrintWriter) throws IOException
	 *  Purpose:
	 *      Call this method to save the current data.  Data is written to the printwriter object
	 *	Use:
	 *      Called automatically when the program terminates
	 **********************************************************************************************/
	void writeData(PrintWriter out) throws IOException
	{
		out.println(client.getText().trim() + " |"
		            + clientLogoPath.getText().trim() + " |"
		            + prospect.getText().trim() + " |"
		            + shotPointSpacing.getText().trim() + " |"
		            + receiverInterval.getText().trim() + " |"
		            + pattern.getText().trim() + " |"
		            + comments.getText().trim() + " |"
		            + compiledBy.getText().trim() + " |"
		            + lineNumber.getText().trim() + " |"
		            + lineDirection.getText().trim() + " |"
		            + pointsPerLine.getText().trim() + " |"
		            + bol.getText().trim() + " |"
		            + eol.getText().trim() + " |"
		);
	}

	/**********************************************************************************************
	 * void readData(BufferedReader) throws IOException
	 *  Purpose:
	 *      Call this method to read in saved data.  Data is read from a bufferedreader object
	 *  Use:
	 *      Called automatically when the program starts.
	 ***********************************************************************************************/
	void readData(BufferedReader in) throws IOException
	{
		String s = in.readLine();
		StringTokenizer t = new StringTokenizer(s, "|");

		if ((t.hasMoreElements()) && (t.countTokens() == 13))
		{
			client.setText(t.nextToken().trim());
			clientLogoPath.setText(t.nextToken().trim());
			prospect.setText(t.nextToken().trim());
			shotPointSpacing.setText(t.nextToken().trim());
			receiverInterval.setText(t.nextToken().trim());
			pattern.setText(t.nextToken().trim());
			comments.setText(t.nextToken().trim());
			compiledBy.setText(t.nextToken().trim());
			lineNumber.setText(t.nextToken().trim());
			lineDirection.setText(t.nextToken().trim());
			pointsPerLine.setText(t.nextToken().trim());
			bol.setText(t.nextToken().trim());
			eol.setText(t.nextToken().trim());
		}
	}

	/**********************************************************************************************
	 * void readSeg(BufferedReader, Vector) throws IOException
	 *  Purpose:
	 *      Read in a bol-eol list in order to create multiple chain notes.
	 *  Use:
	 *      Each line in file should represent one Line object (Form: lineNumber, bol, eol)
	 *      Read each line and store the line inside the vector passed in by reference.
	 ***********************************************************************************************/
	void readSeg(BufferedReader in, Vector lineVector) throws IOException
	{
		String s = in.readLine();           // read in first line

		String lineNumber = null;
		int bol = 0;
		int eol = 0;
		Line newLine;

		while (s != null)                   // eof returns a null
		{
			StringTokenizer t = new StringTokenizer(s, ",");
			if (t.countTokens() == 3)
			{
				lineNumber = t.nextToken().trim();

				// convert the text fields into integers
				try
				{
					bol = Integer.parseInt(t.nextToken().trim());
				} catch (NumberFormatException nfe)
				{
				}

				try
				{
					eol = Integer.parseInt(t.nextToken().trim());
				} catch (NumberFormatException nfe)
				{
				}

				newLine = new Line(lineNumber, bol, eol);
				lineVector.addElement(newLine);
			}
			s = in.readLine();          // now repeat
		}
	}

	/**********************************************************************************************
	 * MenuAction extends AbstractAction
	 *  Purpose:
	 *	    Determine which menu item was pressed, and take appropriate action.
	 *	Use:
	 *	    If - Else Ifs determine which menu item was selected via a passed in String argument.
	 ***********************************************************************************************/
	class MenuAction extends AbstractAction
	{
		public MenuAction(String name)
		{
			super(name);
		}

		public void actionPerformed(ActionEvent event)
		{
			String arg = (String) event.getActionCommand();

			// Perform following if 'New' is selected from menubar ********************************
			if (arg.equals("New"))
			{
				client.setText(null);
				clientLogoPath.setText(null);
				prospect.setText(null);
				shotPointSpacing.setText(null);
				receiverInterval.setText(null);
				pattern.setText(null);
				comments.setText(null);

				SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", java.util.Locale.CANADA);
				Date MoYr = new Date();
				date.setText(formatter.format(MoYr));

				compiledBy.setText(null);
				lineNumber.setText(null);
				lineDirection.setText(null);
				pointsPerLine.setText(null);
				bol.setText(null);
				eol.setText(null);
			}

			// Perform following if 'Save' is selected from menubar *******************************
			else if (arg.equals("Save"))
			{
				FileDialog fd = new FileDialog(SmokerGI_Frame.this, "Save file...");
				fd.setDirectory("");
				fd.setLocation(100, 100);
				fd.setVisible(true);

				if (fd.getFile() != null)
				{
					try
					{
						// save current data to file for autoloading before quiting program
						PrintWriter out = new PrintWriter(new FileWriter(fd.getDirectory() + new String(fd.getFile())));
						writeData(out);
						out.close();
					} catch (IOException exception)
					{
						System.out.println("IOException Thrown: 2");
						exception.printStackTrace();
					}
				}
			}

			// Perform following if 'Open' is selected from menubar *******************************
			else if (arg.equals("Load"))
			{
				FileDialog fd = new FileDialog(SmokerGI_Frame.this, "Load File...");
				fd.setDirectory("");
				fd.setLocation(100, 100);
				fd.setVisible(true);

				if (fd.getFile() != null)
				{
					try
					{
						BufferedReader in = new BufferedReader(new FileReader(fd.getDirectory() + new String(fd.getFile())));
						readData(in);
						in.close();

					} catch (IOException exception)
					{
						System.out.println("IOException Thrown: 1");
						exception.printStackTrace();
					}
				}
			}

			// Perform following if 'Import File' is selected from menubar ************************
			else if (arg.equals("Import File"))
			{
				int ppl = 0;
				int sps = 0;
				int ri = 0;
				int b = 0, e = 0;
				Vector lineVector = new Vector();

				// Provide file dialog to open up bol/eol list
				FileDialog fd = new FileDialog(SmokerGI_Frame.this, "Import File...");
				fd.setDirectory("");
				fd.setLocation(100, 100);
				fd.setVisible(true);

				if (fd.getFile() != null)
				{
					try
					{
						BufferedReader in = new BufferedReader(new FileReader(fd.getDirectory() + new String(fd.getFile())));
						readSeg(in, lineVector);
						in.close();
					} catch (IOException exception)
					{
						System.out.println("IOException Thrown: 1");
						exception.printStackTrace();
					}
				}

				try // convert the text fields into integers
				{
					ppl = Integer.parseInt(pointsPerLine.getText().trim());
				} catch (NumberFormatException nfe)
				{
				}

				try
				{
					sps = Integer.parseInt(shotPointSpacing.getText().trim());
				} catch (NumberFormatException nfe)
				{
				}

				try
				{
					ri = Integer.parseInt(receiverInterval.getText().trim());
				} catch (NumberFormatException nfe)
				{
				}

				if (!((typeSelected <= 2) && ((sps < 1) || (ri < 1))))
				{
					if (!((typeSelected <= 2) && (sps % ri != 0)))
					{
						if (ppl > 0)
						{
							ImportFile frame = new ImportFile(client.getText(), clientLogoPath.getText(), prospect.getText(),
							                                  sps, ri, pattern.getText(), comments.getText(), date.getText(),
							                                  compiledBy.getText(), lineDirection.getText(), ppl,
							                                  typeSelected, lineVector);
							frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							frame.show();
						}
						else
						{
							String message = "Points Per Line must be a positive integer";
							JOptionPane.showMessageDialog(null, message, null, JOptionPane.WARNING_MESSAGE);
						}
					}
					else
					{
						String message = "Shot Point Spacing must be evenly divisible by Receiver Interval";
						JOptionPane.showMessageDialog(null, message, null, JOptionPane.WARNING_MESSAGE);
					}
				}
				else
				{
					String message = "Unknown Error";
					if (sps < 1)
						message = "Shot Point Spacing must be a positive integer";
					if (ri < 1)
						message = "Receiver Interval must be a positive integer";

					JOptionPane.showMessageDialog(null, message, null, JOptionPane.WARNING_MESSAGE);
				}
			}

			else if(arg.equals("Licence"))
			{
				String userName = null;
				String expiryDate = null;
				long eD = 0;
				Calendar date = new GregorianCalendar();

				try
				{
					BufferedReader in = new BufferedReader(new FileReader("keychain.pub"));
					userName = in.readLine().trim();
					in.readLine();
					expiryDate = in.readLine().trim();
					in.close();
				} catch (IOException exception)
				{
					String message = "Error reading licence.";
					JOptionPane.showMessageDialog(null, message, null, JOptionPane.WARNING_MESSAGE);
					System.out.println("IOException Thrown: 1");
					exception.printStackTrace();
					System.exit(0);
				}

				try
				{
					eD = Long.parseLong(expiryDate.trim());
				} catch (NumberFormatException nfe)
				{
				}

				date.setTimeInMillis(eD);

				String message = "This version of Chain Notes registered to: " + userName + ", expires on: " + date.getTime();
				JOptionPane.showMessageDialog(null, message, null, JOptionPane.WARNING_MESSAGE);
			}

			// Perform following if 'Help Topics' is selected from menubar ************************
			else if (arg.equals("Help Topics"))
			{
				try
				{
					Runtime.getRuntime().exec("Help\\Html\\helpexec.bat");
				} catch (Exception e)
				{
					JOptionPane.showMessageDialog(null, "Unable to open Help Topics.", null, JOptionPane.WARNING_MESSAGE);
				}
			}

			// Perform following if 'About' is selected from menubar ******************************
			else if (arg.equals("About"))
			{
				// only create dialog if it is the first one
				if (dialog == null)
					dialog = new AboutDialog(null);
				dialog.show();
			}
		}

		private AboutDialog dialog;
	}

	// North Panel Stuff *************************************************************************
	private JPanel northPanel = new JPanel();      // logo area
	private JLabel wolfLogo = new JLabel(new ImageIcon("SmokerGI/3gw.adm"));

	// South Panel Stuff *************************************************************************
	private JPanel southPanel;
	private JButton quitButton;
	private JButton createButton;

	// Center Panel Stuff *************************************************************************
	private JPanel centerPanel = new JPanel();     // will hold the left and the right panels

	// Left Panel Stuff
	private JPanel leftPanel = new JPanel();
	private JLabel clientLabel = new JLabel("Client : ");
	private JLabel clientLogoLabel = new JLabel("Client Logo : ");
	private JButton openFile = new JButton("...");

	private JLabel prospectLabel = new JLabel("Prospect : ");
	private JLabel shotPointSpacingLabel = new JLabel("Shot Point Spacing : ");
	private JLabel receiverIntervalLabel = new JLabel("Receiver Interval : ");
	private JLabel patternLabel = new JLabel("Pattern : ");
	private JLabel commentsLabel = new JLabel("Comments : ");

	private JTextField client = new JTextField(20);
	private JTextField clientLogoPath = new JTextField(10);
	private JTextField prospect = new JTextField();
	private JTextField shotPointSpacing = new JTextField();
	private JTextField receiverInterval = new JTextField();
	private JTextField pattern = new JTextField();
	private JTextField comments = new JTextField();

	// Right Panel Stuff **************************************************************************
	private JPanel rightPanel = new JPanel();
	private JLabel dateLabel = new JLabel("Date : ");
	private JLabel compiledByLabel = new JLabel("Compiled By : ");
	private JLabel lineNumberLabel = new JLabel("Line Number : ");
	private JLabel lineDirectionLabel = new JLabel("Line Direction : ");
	private JLabel pointsPerLineLabel = new JLabel("Points Per Line :    ");
	private JLabel bolLabel = new JLabel("BOL : ");
	private JLabel eolLabel = new JLabel("EOL : ");

	private JTextField date = new JTextField(20);
	private JTextField compiledBy = new JTextField();
	private JTextField lineNumber = new JTextField();
	private JTextField lineDirection = new JTextField();
	private JTextField pointsPerLine = new JTextField();
	private JTextField bol = new JTextField();
	private JTextField eol = new JTextField();

	// Type Panel Stuff ***************************************************************************
	private JPanel typePanel = new JPanel();
	private ButtonGroup typeButtonGroup = new ButtonGroup();
	private JRadioButton megaBin = new JRadioButton("Mega Bin");
	private JRadioButton twoD = new JRadioButton("2D");
	private JRadioButton xStat = new JRadioButton("2D : X Station");
	private JRadioButton source = new JRadioButton("3D Source");
	private JRadioButton receiver = new JRadioButton("3D Receiver");
	int typeSelected = 1;

	// MenuBar Stuff ******************************************************************************
	private JMenuItem newItem;
	private JMenuItem openItem;
	private JMenuItem importItem;
	private JMenuItem saveItem;
	private JMenuItem helpItem;
	private JMenuItem licenceItem;
	private JPopupMenu popup;
}

/**************************************************************************************************
 * AboutDialog extends JDialog
 *  Purpose:
 *	    Create a nifty little About page
 *	Use:
 *	    Help->About to access the About page.
 **************************************************************************************************/
class AboutDialog extends JDialog
{
	public AboutDialog(JFrame owner)
	{
		super(owner, "About Chain Notes v0.1", true);
		Container contentPane = getContentPane();

		contentPane.setBackground(Color.decode("#CCCCCC"));

		// add HTML label to center

		JLabel logo = new JLabel(null, new ImageIcon("SmokerGI/3gw.adm"), JLabel.CENTER);
		logo.setVerticalTextPosition(JLabel.BOTTOM);
		logo.setHorizontalTextPosition(JLabel.CENTER);
		contentPane.add(logo, BorderLayout.NORTH);

		contentPane.add(new JLabel(
		        "<html>" +
		        "<body bgcolor=#CCCCCC>" +
		        "<font face=arial, helvetica>" +
		        "<div align=center>" +
		        "<table cellspacing=0 cellpadding=0 width=400 border=0>" +
		        "<tr>" +
		        "<td bgcolor=#CCCCCC height=2 colspan=6></td>" +
		        "</tr>" +
		        "<tr>" +
		        "<td bgcolor=#CCCCCC width=5></td>" +
		        "<td bgcolor=#033E84 width=10></td>" +
		        "<td bgcolor=#033E84>" +
		        "<table cellspacing=0 cellpadding=0 width=383>" +
		        "<tr>" +
		        "<td bgcolor=#033E84 height=2></td>" +
		        "</tr>" +
		        "<tr>" +
		        "<td bgcolor=#033E84 colspan=2>" +
		        "<font color=#48AAEE>" +
		        "<font size=2>" +
		        "<b>Development Team:</b>" +
		        "</font>" +
		        "</font>" +
		        "</td>" +
		        "</tr>" +
		        "<tr>" +
		        "<td width=10></td>" +
		        "<td>" +
		        "<font color=#FFFFFF>" +
		        "<font size=2>" +
		        "Alan Maryniuk<br>" +
		        "Jason Fong<br>" +
		        "Mike James<br>" +
		        "</font>" +
		        "</font>" +
		        "</td>" +
		        "</tr>" +
		        "<tr>" +
		        "<td bgcolor=#033E84 colspan=2>" +
		        "<font color=#48AAEE>" +
		        "<font size=2>" +
		        "Licensed to:" +
		        "</font>" +
		        "</font>" +
		        "</td>" +
		        "</tr>" +
		        "<tr>" +
		        "<td width=10></td>" +
		        "<td>" +
		        "<font color=#FFFFFF>" +
		        "<font size=2>" +
		        "Wolf Survey and Mapping," +
		        "<br>" +
		        "a Division of Destiny Resource Services Partnership" +
		        "</font>" +
		        "</font>" +
		        "</td>" +
		        "</tr>" +
		        "<tr>" +
		        "<td bgcolor=#033E84 colspan=2>" +
		        "<font color=#48AAEE>" +
		        "<font size=2>" +
		        "<br>" +
		        "<b>URL:</b>" +
		        "</font>" +
		        "</font>" +
		        "</td>" +
		        "</tr>" +
		        "<tr>" +
		        "<td width=10></td>" +
		        "<td>" +
		        "<font color=#FFFFFF>" +
		        "<font size=2>" +
		        "http://www.destiny-resources.com" +
		        "</font>" +
		        "</font>" +
		        "</td>" +
		        "</tr>" +
		        "<tr>" +
		        "<td bgcolor=#033E84 colspan=2>" +
		        "<font color=#48AAEE>" +
		        "<font size=2>" +
		        "<br>" +
		        "<b>Support:</b>" +
		        "</font>" +
		        "</font>" +
		        "</td>" +
		        "</tr>" +
		        "<tr>" +
		        "<td width=10></td>" +
		        "<td>" +
		        "<font color=#FFFFFF>" +
		        "<font size=2>" +
		        "chain-notes@destiny-resources.com" +
		        "</font>" +
		        "</font>" +
		        "</td>" +
		        "</tr>" +
		        "</table>" +
		        "</td>" +
		        "<td bgcolor=#033E84 width=10></td>" +
		        "<td bgcolor=#CCCCCC width=5></td>" +
		        "</tr>" +
		        "<tr>" +
		        "<td bgcolor=#CCCCCC height=2 colspan=6></td>" +
		        "</tr>" +
		        "</table>" +
		        "</div>" +
		        "</font>" +
		        "</body>" +
		        "</html>"), BorderLayout.CENTER);

		// Ok button closes the dialog
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				setVisible(false);
			}
		});

		// add Ok button to southern border

		JPanel panel = new JPanel();
		panel.setBackground(Color.decode("#CCCCCC"));
		panel.add(ok);
		contentPane.add(panel, BorderLayout.SOUTH);
		setSize(420, 380);
		setLocation(300, 200);
		setResizable(false);
	}
}
