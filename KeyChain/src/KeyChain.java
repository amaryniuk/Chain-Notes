package KeyChain;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.swing.*;

public class KeyChain
{
	public static void main(String[] args)
	{
		KeyChainFrame frame = new KeyChainFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.show();
	}
}

class KeyChainFrame extends JFrame
{
	public KeyChainFrame()
	{
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				System.exit(0);
			}
		});

		createButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				String temp;
				SHA1 createdHash;
				Calendar date = new GregorianCalendar();

				userName = userNameField.getText().trim();

				dateCreated = date.getTimeInMillis();

				// year index is 0 so add 2003 to it to get correct year.
				// use '1' for the day because we want it to expire on the first of the month
				date = new GregorianCalendar(yearComboBox.getSelectedIndex() + 2003,
				                             monthComboBox.getSelectedIndex(), 1);
				dateExpired = date.getTimeInMillis();

				if (!(userName.length() < 6) && (dateCreated < dateExpired))
				{
					createdHash = new SHA1(userName);
					userNameHash = createdHash.getDigest();

					temp = String.valueOf(dateCreated);
					createdHash = new SHA1(temp);
					dateCreatedHash = createdHash.getDigest();

					temp = String.valueOf(dateExpired);
					createdHash = new SHA1(temp);
					dateExpiredHash = createdHash.getDigest();

					try
					{
						PrintWriter out = new PrintWriter(new FileWriter("keychain.pub"));
						writeData(out);
						out.close();
					} catch (IOException exception)
					{
						System.out.println("IOException Thrown: 2");
						exception.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "Security key written to keychain.pub", null, JOptionPane.WARNING_MESSAGE);
					System.exit(0);
				}
				else
				{
					SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", java.util.Locale.CANADA);
					Date MoYr = new Date();
					String message = null;

					if (userName.length() < 6)
						message = "User Name must be at least 6 characters";
					else
						message = "Expiration Date must be greater than " + formatter.format(MoYr);
			        JOptionPane.showMessageDialog(null, message, null, JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		// GUI STUFF ******************************************************************************
		GridBagConstraints constraints = new GridBagConstraints();
		centerPanel.setLayout(new GridBagLayout());

		constraints.fill = GridBagConstraints.HORIZONTAL;

		constraints.gridx = 0;
		constraints.gridy = 0;

		centerPanel.add(userNameLabel, constraints);

		constraints.gridx = 1;
		constraints.gridwidth = 2;
		constraints.weightx = 100;
		centerPanel.add(userNameField, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.weightx = 0;
		centerPanel.add(dateLabel, constraints);

		constraints.gridx = 1;
		constraints.weightx = 50;
		centerPanel.add(monthComboBox, constraints);

		constraints.gridx = 2;
		constraints.weightx = 50;
		centerPanel.add(yearComboBox, constraints);

		southPanel.add(createButton);
		southPanel.add(cancelButton);

		this.setTitle("Chain Notes : Key Generator");
		this.setSize(350, 150);
		this.setResizable(false);
		this.setLocation(200, 200);

		Container contentPane = this.getContentPane();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		contentPane.add(southPanel, BorderLayout.SOUTH);
	}

	void writeData(PrintWriter out) throws IOException
	{
		out.println(userName);
		out.println(dateCreated);
		out.println(dateExpired);

		out.println(userNameHash);
		out.println(dateCreatedHash);
		out.println(dateExpiredHash);
	}

	private JFrame frame = new JFrame();

	private JPanel centerPanel = new JPanel();
	private JPanel southPanel = new JPanel();

	// username stuff
	private JLabel userNameLabel = new JLabel("User Name");
	private JTextField userNameField = new JTextField(80);
	private String userName = new String();
	private String userNameHash = new String();

	// expiry date stuff for gui
	private JLabel dateLabel = new JLabel("Expiry Date");

	private JLabel monthLabel = new JLabel("Month");
	String[] monthList = {"January", "February", "March", "April", "May", "June",
	                      "July", "August", "September", "October", "November", "December"};
	private JComboBox monthComboBox = new JComboBox(monthList);

	private JLabel yearLabel = new JLabel("Year");
	String[] yearList = {"2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013"};
	private JComboBox yearComboBox = new JComboBox(yearList);

	// date stuff
	long dateCreated = 0;
	String dateCreatedHash = new String();

	long dateExpired = 0;
	String dateExpiredHash = new String();

	private JButton createButton = new JButton("Create");
	private JButton cancelButton = new JButton("Cancel");
}
