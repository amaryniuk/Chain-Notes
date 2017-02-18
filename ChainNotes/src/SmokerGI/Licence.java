package SmokerGI;

import java.io.*;
import java.util.*;
import javax.swing.*;

/* First thing we ever want to do is see if this version of ChainNotes is licenced
	and that the licence is not expired */

public class Licence
{
	public static void main(String args[])
	{
		isLicenced l = new isLicenced();
	}
}

class isLicenced
{
	public isLicenced()
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader("keychain.pub"));
			readData(in);
			in.close();
		} catch (IOException exception)
		{
			String message = "This product does not appear to be licenced.  Please contact your administrator.";
			JOptionPane.showMessageDialog(null, message, null, JOptionPane.WARNING_MESSAGE);
			System.out.println("IOException Thrown: 1");
			exception.printStackTrace();
			System.exit(0);
		}

		// do some checks to ensure key is valid... see documentation within verifyData for more details.
		verifyData();
		// the keys match - we can load up chain notes... but first...
		// To prevent (or discourage) people from rolling back their system clock - update the key with todays values.
		// call uypdate() to update the values for dateCreated and dateCreatedHash with todays values.
		update();

		// now write the new values to our security key.
		try
		{
			PrintWriter out = new PrintWriter(new FileWriter("keychain.pub"));
			writeData(out);
			out.close();
		} catch (IOException exception)
		{
			System.out.println("IOException Thrown: 1");
			exception.printStackTrace();
			System.exit(0);
		}

		SmokerGI_Frame frame = new SmokerGI_Frame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.show();
	}

	// read in the security key - and call verifyData to ensure all fields hash correctly.
	void readData(BufferedReader in) throws IOException
	{
		userName = in.readLine().trim();
		dateCreated = in.readLine().trim();
		dateExpired = in.readLine().trim();

		userNameKeyHash = in.readLine().trim();
		dateCreatedKeyHash = in.readLine().trim();
		dateExpiredKeyHash = in.readLine().trim();
	}

	void verifyData()
	{
		String temp;
		SHA1 createdHash;
		long dateC = 0;
		long dateE = 0;
		Calendar date = new GregorianCalendar();
		long dateT = date.getTimeInMillis();        // get todays date in milliseconds.

		// to verify the username and dates match, hash them, and compare to what is stored in the key.
		createdHash = new SHA1(userName);
		userNameHash = createdHash.getDigest();

		createdHash = new SHA1(dateCreated);
		dateCreatedHash = createdHash.getDigest();

		createdHash = new SHA1(dateExpired);
		dateExpiredHash = createdHash.getDigest();

		// get todays date
		dateC = date.getTimeInMillis();

		// convert the dates into long so we can compare easily.
		try
		{
			dateC = Long.parseLong(dateCreated.trim());
		} catch (NumberFormatException nfe)
		{
		}
		try
		{
			dateE = Long.parseLong(dateExpired.trim());
		} catch (NumberFormatException nfe)
		{
		}

		// need to add a ' ' space for some reason.
		userNameKeyHash += ' ';
		dateCreatedKeyHash += ' ';
		dateExpiredKeyHash += ' ';

		// compare the new hashed strings to the keys
		if (!((userNameHash.equals(userNameKeyHash)) && (dateCreatedHash.equals(dateCreatedKeyHash)) && (dateExpiredHash.equals(dateExpiredKeyHash))))
		{
			// the keys do not match - there is something is wrong with key
			String message = "Secuity key has been tampered with.  Unable to open Chain Notes.";
			JOptionPane.showMessageDialog(null, message, null, JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
		if (dateC > dateE)     // app expired since the dateCreated is bigger than the expiry date.
		{
			// the keys match, but the licence is expired
			String message = "Your licence for Chain Notes has expired.  Please contact your administrator.";
			JOptionPane.showMessageDialog(null, message, null, JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
		if(dateC > dateT)      // looks like they rolled back the clock, since date in the file is bigger than todays date.
		{
			String message = "System clock rolled back.  Unable to open Chain Notes.";
			JOptionPane.showMessageDialog(null, message, null, JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
	}

	// ok... get todays date, and hash it - save the new date and the new hash into their proper variables
	// so we can write them to our security key.
	void update()
	{
		SHA1 createdHash;
		Calendar date = new GregorianCalendar();
		long temp = date.getTimeInMillis();

		dateCreated = String.valueOf(temp);
		createdHash = new SHA1(dateCreated);
		dateCreatedHash = createdHash.getDigest();
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

	private String userName = new String();
	private String dateCreated = new String();
	private String dateExpired = new String();

	private String userNameHash = new String();
	private String dateCreatedHash = new String();
	private String dateExpiredHash = new String();

	private String userNameKeyHash = new String();
	private String dateCreatedKeyHash = new String();
	private String dateExpiredKeyHash = new String();
}
