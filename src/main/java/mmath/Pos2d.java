package mmath;

public class Pos2d 
{
	// ATTRIBUTE**********************************************************************
	
	public double x;
	public double y;
	
	// KONSTRUKTOREN******************************************************************
	
	public Pos2d()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public Pos2d(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Pos2d(Pos2d p)
	{
		this.x = p.x;
		this.y = p.y;
	}
	
	public Pos2d(Vec2d v)
	{
		this.x = v.x;
		this.y = v.y;
	}
	
	// METHODEN************************************************************************
	
	public Pos2d clone()
	{
		return new Pos2d(this.x, this.y);
	}
	
	public double abs()
	{
		return Math.sqrt(this.x*this.x + this.y*this.y);
	}
	
	public static double abs(Pos2d p)
	{
		return Math.sqrt(p.x*p.x + p.y*p.y);
	}
	
	
	public void mul(double factor)
	{
		this.x *= factor;
		this.y *= factor;
	}
	
	public static Pos2d mul(Pos2d p, double factor)
	{
		return new Pos2d(factor * p.x, factor * p.y);
	}
	
	public void add(Pos2d p)
	{
		this.x += p.x;
		this.y += p.y;
	}
	
	public String getInfo()
	{
		return "(" + this.x + ", " + this.y + ")";
	}
	
	public void info(String appendix)
	{
		System.out.println("(" + this.x + ", " + this.y + ") [" + appendix + "]");
	}
	
	public static Pos2d parseInfoString(String line)
	{
		try
		{
			Pos2d out = new Pos2d();
			
			int i0 = line.indexOf('(');
			int i1 = line.indexOf(',');
			int i2 = line.indexOf(')');
			
			out.x = Double.parseDouble(line.substring(i0 + 1, i1));
			out.y = Double.parseDouble(line.substring(i1 + 1, i2));
			
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
				"- Kommas trennen Komponenten, Punkte Vor- und Nachkommastellen\n" +
				"z.B.: (1.0, 2.0)";
	}
}
