package SmokerGI;

import java.security.*;

/** This frame contains a menu for computing the message
 digest of a file or text area, radio buttons to toggle between
 SHA-1 and MD5, a text area, and a text field to show the
 messge digest. */

class SHA1
{
	public SHA1(String input)
	{
		setAlgorithm("SHA1");
		computeDigest(input.getBytes());
	}

	/**	 Sets the algorithm used for computing the digest. */
	public void setAlgorithm(String alg)
	{
		try
		{
			currentAlgorithm = MessageDigest.getInstance(alg);
			digest = ("");
		} catch (NoSuchAlgorithmException e)
		{
			digest = ("" + e);
		}
	}

	/**	 Computes the message digest of an array of bytes
	 and displays it in the text field.
	 @param b the bytes for which the message digest should
	 be computed.	 */
	public void computeDigest(byte[] b)
	{
		currentAlgorithm.reset();
		currentAlgorithm.update(b);
		byte[] hash = currentAlgorithm.digest();
		String d = "";
		for (int i = 0; i < hash.length; i++)
		{
			int v = hash[i] & 0xFF;
			if (v < 16) d += "0";
			d += Integer.toString(v, 16).toUpperCase() + " ";
		}
		digest = d;
//		System.out.println(digest);
	}

	String getDigest()
	{
		return digest;
	}

	private String digest;
	private MessageDigest currentAlgorithm;
}
