package mmath;

public class Int2 
{
//	 ATTRIBUTE**********************************************************************
	
	public int x;
	public int y;
	
	// KONSTRUKTOREN******************************************************************
	
	public Int2()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public Int2(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Int2(Int2 p)
	{
		this.x = p.x;
		this.y = p.y;
	}
	
	// METHODEN************************************************************************
	
	// TODO
	
	public Int2 sub(Int2 p)
	{
		return new Int2(this.x - p.x, this.y - p.y);
	}
	
	public float[] getArray()
	{
		return new float[]{x, y};
	}
	
	// I/O-Methoden..........................................................................................
	
	public void info(String appendix)
	{
		System.out.println("(" + this.x + ", " + this.y + ") [" + appendix + "]");
	}
	
	public String getInfo()
	{
		return "(" + this.x + ", " + this.y + ")";
	}
	
	public static Int2 parseInfoString(String line)
	{
		try
		{
			Int2 out = new Int2();
			
			int i0 = line.indexOf('(');
			int i1 = line.indexOf(',');
			int i2 = line.indexOf(')');
			
			out.x = (int) Double.parseDouble(line.substring(i0 + 1, i1));
			out.y = (int) Double.parseDouble(line.substring(i1 + 1, i2));
			
			return out;
		}
		catch(Exception exc)
		{
			return null;
		}
	}
	
	public static String getRightInputInfo()
	{
		return 	"Eingabe muss in folgender Form vorliegen:\n" +
				"- Es müssen genau 2 Komponenten vorhanden sein\n" +
				"- Komponenten müssen von Klammern umgeben sein\n" +
				"- Kommas trennen Komponenten\n" +
				"z.B.: (1, -2)";
	}
	
}
