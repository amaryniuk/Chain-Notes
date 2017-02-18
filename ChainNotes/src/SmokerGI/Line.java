package SmokerGI;

// Purpose:
//   Store essiential information needed to create a basic line.  This includes
//      Line number : to distinguish two lines from one another
//      bol :
//      eol :
// Use:
//   Line (String lineNumber, int bol, int eol)
//   get methods allow for retrieval of information
class Line
{
	public Line(String l, int b, int e)
	{
		lineNumber = l;
		bol = b;
		eol = e;
	}

	public String lineNumber()
	{
		return lineNumber;
	}

	public int bol()
	{
		return bol;
	}

	public int eol()
	{
		return eol;
	}

	private String lineNumber;
	private int bol;
	private int eol;
}
