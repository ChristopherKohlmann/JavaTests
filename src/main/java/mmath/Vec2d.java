package mmath;

public class Vec2d 
{
	// ATTRIBUTE********************************************************************************
	
	public double x;
	public double y;
	
	// KONSTRUKTOREN****************************************************************************
	
	public Vec2d()
	{
		this.x = 0.0;
		this.y = 0.0;
	}
	
	public Vec2d(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vec2d(Vec2d v)
	{
		this.x = v.x;
		this.y = v.y;
	}
	
	public Vec2d(Pos2d p)
	{
		this.x = p.x;
		this.y = p.y;
	}
	
	// METHODEN*********************************************************************************
	
	public void mul(double a)
	{
		this.x *= a;
		this.y *= a;
	}
	
	public static Vec2d mul(Vec2d v, double a)
	{
		return new Vec2d(v.x * a, v.y * a);
	}
	
	public double mul(Vec2d v)
	{
		return this.x * v.x + this.y * v.y;
	}
	
	public static double mul(Vec2d a, Vec2d b)
	{
		return a.x*b.x + a.y*b.y;
	}
	
	public double abs()
	{
		return Math.sqrt(this.x*this.x + this.y*this.y);
	}
	
	public static double abs(Vec2d v)
	{
		return Math.sqrt(v.x*v.x + v.y*v.y);
	}
	
	public void normalize()
	{
		if(this.x != 0.0 || this.y != 0.0)
		{
			double abs = this.abs();
			this.x /= abs;
			this.y /= abs;
		}
	}
	
	public static Vec2d normalize(Vec2d a)
	{
		if(a.x != 0.0 || a.y != 0.0)
		{
			double abs = a.abs();
			return new Vec2d(a.x / abs, a.y / abs);
		}
		
		return new Vec2d();	
	}
	
	
	public String getInfo()
	{
		return "(" + this.x + ", " + this.y + ")";
	}
	
	public static Vec2d parseInfoString(String line)
	{
		try
		{
			Vec2d out = new Vec2d();
			
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
