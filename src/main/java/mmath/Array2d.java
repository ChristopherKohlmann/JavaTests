package mmath;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import javax.imageio.ImageIO;
import mio.MWriter;

public class Array2d 
{
	// ATTRIBUTE***********************************************************************************
	
	double[][] data = null;	// Werte
	
	Pos2d origin 	= new Pos2d(0.0, 0.0);	// Startpunkt des Definitionsbereichs
	Vec2d step 		= new Vec2d(1.0, 1.0);	// Schrittweite des Definitionsbereichs
	Int2 res 		= new Int2();			// Array-Aufloesung
	
	double min;	// Kleinster Wert im Array
	double max;	// Groesster Wert im Array
	
	boolean seamlessX = false; 	// Flag fuer periodischen x-Verlauf
	boolean seamlessY = false;	// Flag fuer periodischen y-Verlauf
	
	// KONSTRUKTOR*********************************************************************************
	
	/**
	 * Konstruktor zur Erzeugung eines Array2d-Objektes aus einer Datei
	 * @param f
	 * Unterstuetzte Formate: *.a2d, *.pat (kaum verwendetes Zwischenformat), altes DELTA-Datenblatt
	 * Ausserdem alle von Java unterstuetzten Bilddateien.
	 */
	public Array2d(File f)
	{
		loadData(f);
		determineResolution();
		determineValueLimits();
	}
	
	/**
	 * Standard-Konstruktor zur Erzeugung eines Array2d-Objektes
	 */
	public Array2d(double[][] data, Pos2d origin, Vec2d step)
	{
		this.data = data;
		this.origin = origin;
		this.step = step;

		this.seamlessX = false;
		this.seamlessY = false;
		
		determineResolution();
		determineValueLimits();
	}
	
	/**
	 * Konstruktor zum Klonen eines Array2d-Objektes
	 * @param a
	 * 
	 */
	public Array2d(Array2d a)
	{
		this.data = a.data.clone();
		this.origin = a.getOrigin();
		this.step = a.getStep();
		
		this.seamlessX = a.seamlessX;
		this.seamlessY = a.seamlessY;
		
		determineResolution();
		determineValueLimits();
	}
	
	
	// METHODEN************************************************************************************
	
	// GETTER und SETTER...........................................................................
	
	public int getResX()
	{
		return res.x;
	}
	
	public int getResY()
	{
		return res.y;
	}
	
	public Int2 getRes()
	{
		return new Int2(res);
	}
	
	public Pos2d getOrigin()
	{
		return new Pos2d(origin);
	}
	
	public void setOrigin(Pos2d origin)
	{
		this.origin = new Pos2d(origin);
	}
	
	public void setOrigin(double x, double y)
	{
		this.origin = new Pos2d(x, y);
	}
	
	public Vec2d getStep()
	{
		return new Vec2d(step);
	}
	
	public void setStep(Vec2d step)
	{
		this.step = new Vec2d(step);
	}
	
	public void setStep(double dx, double dy)
	{
		this.step = new Vec2d(dx, dy);
	}
	
	public double getMin()
	{
		return min;
	}
	
	public double getMax()
	{
		return max;
	}
	
	public void setMinMax(double min, double max)
	{
		this.min = min;
		this.max = max;
	}
	
	public double[][] getData()
	{
		double[][] out = new double[res.x][];
		
		for(int i = 0; i < out.length; i++)
		{
			out[i] = data[i].clone();
		}
		
		return out;
	}
	
	public void setData(double[][] data)
	{
		this.data = data.clone();
		
		determineResolution();
		determineValueLimits();
	}
	
	public void setSeamlessX(boolean seamless)
	{
		this.seamlessX = seamless;
	}
	
	public boolean isSeamlessX()
	{
		return seamlessX;
	}
	
	public void setSeamlessY(boolean seamless)
	{
		this.seamlessY = seamless;
	}
	
	public boolean isSeamlessY()
	{
		return seamlessY;
	}
	
	// Datenausgabe...............................................................................
	
	public double[][] getNorm()
	{
		double[][] out = new double[res.x][res.y];
		double m = max - min;
		double n;
		
		if(m != 0.0) m = 1.0/m;
		
		n = -m * min;
		
		for(int i = 0; i < out.length; i++)
		{
			for(int j = 0; j < out[i].length; j++)
			{
				out[i][j] = Math.max(0.0, Math.min(1.0, m * data[i][j] + n));
			}
		}
		
		return out;
	}
	
	// Einzelwertausgabe..........................................................................
	
	public static int getClosedIndex(int index, int length)
	{
		int out = index%length;
		
		if(out >= 0) 	return out;
		else			return length+out;
	}
	
	public double get(int i, int j)
	{
		int x, y;
		
		if(seamlessX)	x = getClosedIndex(i, res.x);
		else			x = Math.max(0, Math.min(getResX()-1, i));
		
		if(seamlessY)	y = getClosedIndex(j, res.y);
		else			y = Math.max(0, Math.min(getResY()-1, j));
		
		return data[x][y];
	}
	
	public double getNearestValue(double xPseudo, double yPseudo)
	{
		return get((int) Math.round(xPseudo), (int) Math.round(yPseudo));
	}
	
	public double getNearestValue(Pos2d pos)
	{
		Pos2d index = getPseudoIndices(pos);
		return getNearestValue(index.x, index.y);
	}
	
	public double getBiLinearValue(double xPseudo, double yPseudo)
	{
		double s = Math.floor(xPseudo);
		double t = Math.floor(yPseudo);
		double[][] sample = get2x2Sample(s, t);
		
		s = xPseudo - s;
		t = yPseudo - t;
		
		return 	(1.0 - s) 	* 	(1.0 - t) 	* 	sample[0][0] +
				(1.0 - s)	* 		t 		* 	sample[0][1] +
				 	s		* 	(1.0 - t) 	* 	sample[1][0] +
					s 		* 		t 		* 	sample[1][1];
	}
	
	public double getBiLinearValue(Pos2d pos)
	{
		Pos2d index = getPseudoIndices(pos);
		return getBiLinearValue(index.x, index.y);
	}
	
	public double getBSplineValue(double xPseudo, double yPseudo)
	{
		double out = 0.0;
		
		double s = Math.floor(xPseudo);
		double t = Math.floor(yPseudo);
		double[][] sample = get4x4Sample(s, t);
		
		double xWeight, yWeight;
		
		s = xPseudo - s;
		t = yPseudo - t;
		
		for(int i = 0; i < sample.length; i++)
		{
			xWeight = getBSplineWeight(s, i);
			
			for(int j = 0; j < sample[i].length; j++)
			{
				yWeight = getBSplineWeight(t, j);
				
				out += xWeight * yWeight * sample[i][j];
			}
		}
		
		return out;
	}
	
	public double getBSplineValue(Pos2d pos)
	{
		Pos2d index = getPseudoIndices(pos);
		return getBSplineValue(index.x, index.y);
	}
	
	public Pos2d getPosition(double xIndex, double yIndex)
	{
		Pos2d out = new Pos2d();
		
		out.x = origin.x + step.x * xIndex;
		out.y = origin.y + step.y * yIndex;
		
		return out;
	}
	
	public Pos2d getPseudoIndices(Pos2d pos)
	{
		return new Pos2d(getPseudoXIndex(pos.x), getPseudoYIndex(pos.y));
	}
	
	public double getPseudoXIndex(double pos)
	{
		if(step.x != 0.0) 	return (pos - origin.x) / step.x;
		else				return 0.0;
	}
	
	public double getPseudoYIndex(double pos)
	{
		if(step.y != 0.0) 	return (pos - origin.y) / step.y;
		else				return 0.0;
	}
	
	// Nicht oeffentliche Methoden.................................................................
	
	/**
	 * Bestimmung der Aufloesung des Arrays
	 */
	private void determineResolution()
	{
		res = new Int2(0, 0);
		
		if(data != null)
		{
			res.x = data.length;
			res.y = data[0].length;
		}
	}
	
	/**
	 * Bestimmung des niedrigsten und des groessten Wertes innerhalb des Arrays
	 */
	private void determineValueLimits()
	{
		if(data != null)
		{
			min = data[0][0];
			max = data[0][0];
			
			for(int i = 0; i < res.x; i++)
			{
				for(int j = 0; j < res.y; j++)
				{
					min = Math.min(min, data[i][j]);
					max = Math.max(max, data[i][j]);
				}
			}
		}
		else
		{
			min = 0.0;
			max = 0.0;
		}
	}
	
	/**
	 * Stuetzstellenbestimmung fuer Bilineare Interpolation
	 */
	private double[][] get2x2Sample(double xBase, double yBase)
	{
		double[][] out = new double[2][2];
		
		for(int i = 0; i < 2; i++)
		{
			for(int j = 0; j < 2; j++)
			{
				out[i][j] = getNearestValue(xBase + i, yBase + j);
			}
		}
		
		return out;
	}
	
	/**
	 * Stuetzstellenbestimmung fuer B-Spline-Interpolation
	 */
	private double[][] get4x4Sample(double xBase, double yBase)
	{
		double[][] out = new double[4][4];
		
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				out[i][j] = getNearestValue(xBase + i - 1, yBase + j - 1);
			}
		}
		
		return out;
	}
	
	/**
	 * Bestimmung der B-Spline-Koeffizienten
	 */
	private double getBSplineWeight(double t, int index)
	{
		switch(index)
		{
			case 0: return (Math.pow(1.0 - t, 3.0) / 6);
			case 1: return ((3.0*t*t*t - 6.0*t*t + 4.0) / 6.0);
			case 2: return ((3.0*t * (-t*t + t + 1.0) + 1.0) / 6.0);
			case 3: return (t*t*t / 6.0);
		}
		
		return 0.0;
	}


	// Statische Methoden...................................................................................
	
	public static String[] getSupportedFormats()
	{
		String[] img = ImageIO.getWriterFormatNames();
		String out[] = new String[img.length+3];
		
		out[0] = "a2d";
		out[1] = "pat";
		out[2] = "xpda";
		
		for(int i = 0; i < img.length; i++)
		{
			out[i+3] = img[i];
		}
		
		return out;
	}
	
	public static String getFileChooserDescription()
	{
		String[] format = getSupportedFormats();
		String out = "";
		String sep = ", ";
		
		for(int i = 0; i < format.length; i++)
		{
			if(i >= format.length-1) sep = "";
			
			out += "*." + format[i] + sep;
		}
		
		return out;
	}
	
	public static boolean isCompatible(File f)
	{
		String[] format = getSupportedFormats();
		
		for(int i = 0; i < format.length; i++)
		{
			if(f.getName().toLowerCase().endsWith("." + format[i])) return true;
		}
		
		return false;
	}
	
	// I/O-Methoden.........................................................................................
	
	public void save(String path)
	{
		if(path.toLowerCase().endsWith(".a2d") == false) path += ".a2d";
		String ws;
		String s = 	"#Size: (" + getResX() + ", " + getResY() + ")\n" +
					"#Origin: (" + origin.x + ", " + origin.y + ")\n" +
					"#Step: (" + step.x + ", " + step.y + ")\n" +
					"#SeamlessX: " + seamlessX + "\n" +
					"#SeamlessY: " + seamlessY + "\n";
		
		MWriter.write(path, s, false);
		
		
		for(int i = 0; i < data.length; i++)
		{
			for(int j = 0; j < data[i].length; j++)
			{
				if(j < data[i].length-1) 	ws = " ";
				else						ws = "\n";
				
				s = data[i][j] + ws;
				
				MWriter.write(path, s, true);
			}
		}
	}
	
	public BufferedImage getBufferedImage()
	{
		BufferedImage img = new BufferedImage(getResX(), getResY(), BufferedImage.TYPE_INT_RGB);
		double data[][] = getNorm();
		int val, y;
		Color c;
		
		System.out.println("Beginne Datenpufferung...");
		
		for(int i = 0; i < data.length; i++)
		{
			y = data[i].length-1;
			
			for(int j = 0; j <= y; j++)
			{
				val = Math.max(0, Math.min(255, (int) Math.round(255.0 * data[i][j])));
				c = new Color(val, val, val);
				
				img.setRGB(i, y-j, c.getRGB());
			}
		}
		
		return img;
	}
	
	public void saveAsImage(String path)
	{
		if(path.toLowerCase().endsWith(".png") == false) path += ".png";
		
		System.out.println("Lese Datensatz ein...");
		
		BufferedImage img = getBufferedImage();
		
		/*
		BufferedImage img = new BufferedImage(getResX(), getResY(), BufferedImage.TYPE_INT_RGB);
		double data[][] = getNorm();
		int val, y;
		Color c;
		
		System.out.println("Beginne Datenpufferung...");
		
		for(int i = 0; i < data.length; i++)
		{
			y = data[i].length-1;
			
			for(int j = 0; j <= y; j++)
			{
				val = Math.max(0, Math.min(255, (int) Math.round(255.0 * data[i][j])));
				c = new Color(val, val, val);
				
				img.setRGB(i, y-j, c.getRGB());
			}
		}
		/*
		for(int i = 0; i < data.length; i++)
		{
			for(int j = 0; j < data[i].length; j++)
			{
				val = Math.max(0, Math.min(255, (int) Math.round(255.0 * data[i][j])));
				c = new Color(val, val, val);
				
				img.setRGB(i, j, c.getRGB());
			}
		}
		*/
		System.out.println("Gepuffertes Image erzeugt!");
		System.out.println("Speichere Image...");
		
		try
		{
			ImageIO.write(img, "png", new File(path));
			System.out.println("Image gespeichert!");
		}
		catch(Exception exc)
		{
			System.err.println("Konnte Datei nicht schreiben (" + path + "): " + exc);
			System.err.println(exc.getStackTrace());
		}
	}
	
	
	private void loadData(File f)
	{
		if		(f.getName().toLowerCase().endsWith("a2d"))		loadA2D(f);
		else if	(f.getName().toLowerCase().endsWith("pat"))		loadPAT(f);
		else if	(f.getName().toLowerCase().endsWith("xpda"))	loadOldFormat(f);
		else
		{
			boolean rightFormat = false;
			String[] formatNames = ImageIO.getWriterFormatNames();
			
			for(int i = 0; i < formatNames.length; i++)
			{
				rightFormat |= f.getName().toLowerCase().endsWith(formatNames[i]);
			}
			
			if(rightFormat) loadImage(f);
		}
	}
	
	private void loadA2D(File f)
	{
		Reader in = null;
		String q = "", name, value;
		
		boolean keepOn = true, hasInfo, endOfLine, isHeader;
		int xIndex = 0, yIndex = 0, chIndex;
		char c;
		
		data = new double[0][0];
		
		try
		{
			in = new FileReader(f);
			
			while(keepOn)
			{
				chIndex = in.read();
				c = (char) chIndex;
				
				hasInfo = q.length() > 0;
				endOfLine = (c == '\n');
				isHeader = hasInfo && (q.charAt(0) == '#');
				
				if(hasInfo && endOfLine && isHeader)
				{
					name = q.substring(q.indexOf('#')+1, q.indexOf(':'));
					value = q.substring(q.indexOf(':')+1).trim();
					
					if(name.equals("Size"))
					{
						res = Int2.parseInfoString(value);
						data = new double[res.x][res.y];
					}
					else if(name.equals("Origin")) this.origin = Pos2d.parseInfoString(value);
					else if(name.equals("Step")) this.step = Vec2d.parseInfoString(value);
					else if(name.equals("SeamlessX")) this.seamlessX = Boolean.parseBoolean(value);
					else if(name.equals("SeamlessY")) this.seamlessY = Boolean.parseBoolean(value);

					q = "";
				}
				else if(hasInfo && (isHeader == false) && (Character.isWhitespace(c) || chIndex == -1))
				{
					data[xIndex][yIndex++] = Double.parseDouble(q);
					q = "";
					
					if(endOfLine) {xIndex++; yIndex = 0;}
				}
				else {q += c;}

				keepOn = (chIndex != -1);
			}
		}
		catch(Exception exc)
		{
			System.err.println("Fehler beim Lesen der Datei \"" + f.getPath() + "\": " + exc);
		}
		
		finally
		{
			try {in.close();} catch(Exception exc) {}
		}
	}
	
	private void loadImage(File f)
	{
		BufferedImage img = null;
		
		try
		{
			img = ImageIO.read(f);
		}
		catch(Exception exc)
		{
			System.err.println("Die Datei \"" + f.getPath() + "\" konnte nicht eingelesen werden: " + exc);
		}
		
		if(img != null)
		{
			int w = img.getWidth();
			int h = img.getHeight();
			double val;
			Color c;
			
			data = new double[w][h];
			
			for(int i = 0; i < w; i++)
			{
				for(int j = 0; j < h; j++)
				{
					c = new Color(img.getRGB(i, j));
					
					val = 	0.30 * (double) c.getRed() +
							0.59 * (double) c.getGreen() +
							0.11 * (double) c.getBlue();
					
					data[i][h-1-j] = val / 255.0;
				}
			}
		}
		else
		{
			data = new double[0][0];
		}
	}
	
	private void loadPAT(File f)
	{
		int MAX_CACHE = 1000;
		double[][] cache = new double[MAX_CACHE][MAX_CACHE];
		double[][] out;
		
		int index1 = 0, index2 = 0;
		
		Reader in = null;
		String q = "";
		double x0 = 0, y0 = 0, x1 = 1, y1 = 1;
		double theta0 = -1, theta1;
		char c;
		boolean alpha = false;
		
		try
		{
			in = new FileReader(f);
			
			for(int i; (i = in.read()) != -1;)
			{
				c = (char) i;
				
				if(c != '\n')
				{
					q += c;
				}
				else if(alpha)
				{
					y1 = Double.parseDouble(q.substring(q.indexOf("(")+1, q.indexOf(",")));
					x1 = Double.parseDouble(q.substring(q.indexOf(",")+1, q.indexOf(")")));
					
					theta1 = y1;
					
					if(theta1 > theta0)
					{
						index2 = 0;
						index1++;
						theta0 = theta1;
					}
					else
					{
						index2++;
					}
					
					cache[index1][index2] = Double.parseDouble(q.substring(q.indexOf(":")+1, q.length()));
					
					q = "";
				}
				else
				{
					y0 = Double.parseDouble(q.substring(q.indexOf("(")+1, q.indexOf(",")));
					x0 = Double.parseDouble(q.substring(q.indexOf(",")+1, q.indexOf(")")));
					
					theta0 = y0;
					
					cache[index1][index2] = Double.parseDouble(q.substring(q.indexOf(":")+1, q.length()));
					
					q = "";
					alpha = true;
				}
			}
		}
		catch(Exception exc)
		{
			System.err.println("Fehler beim Lesen der Datei \"" + f.getPath() + "\": " + exc);
		}
		
		finally
		{
			try{in.close();}
			catch(Exception exc){}
		}
		
		out = new double[index1+1][index2+1];
		
		for(int i = 0; i <= index1; i++)
		{
			for(int j = 0; j <= index2; j++)
			{
				out[i][j] = cache[i][j];
			}
		}

		this.origin = new Pos2d(x0, y0);
		this.step = new Vec2d((x1-x0)/(double)index2, (y1-y0)/ (double)index1);
		this.seamlessX = false;
		this.seamlessY = false;
		this.data = out;
	}
	
	private void loadOldFormat(File f)
	{
		Reader in = null;
		
		double[][] data = null;
		char c;
		String q = "";
		boolean rec = false;
		int index;
		String[] part;
		int t = 0, p = 0;
		
		try
		{
			in =  new FileReader(f);
			
			data = new double[90][360];
			
			for(int i; (i = in.read()) != -1;)
			{
				c = (char) i;
				
				if(c == '\n' && q != "")
				{
					if(rec)
					{
						index = 0;
						
						part = q.split(" ");
						
						for(int j = 0; j < part.length; j++)
						{
							if(part[j].length() > 0)
							{
								switch(index)
								{
									case 0: {t = Integer.parseInt(part[j]); break;}
									case 1: {p = Integer.parseInt(part[j]); break;}
									case 2: {data[t][p] = Double.parseDouble(part[j]); break;}
								}
								
								index++;
							}
						}
					}
					
					if(q.charAt(0) == '$') rec = true;
					
					q = "";
				}
				else
				{
					if(c != '\n') q += c;
				}
			}
		}
		catch(Exception exc)
		{
			System.err.println("Fehler beim einlesen: " + exc);
			exc.printStackTrace();
		}
		
		finally
		{
			try
			{
				in.close();
			}
			catch(Exception exc){}
		}
		
		this.data = data;
		this.origin = new Pos2d();
		this.step = new Vec2d(1.0, 1.0);
		
		this.seamlessX = false;
		this.seamlessY = false;
	}

}
