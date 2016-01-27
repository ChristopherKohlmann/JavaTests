package mio;

import java.io.FileWriter;
import java.io.Writer;

public class MWriter 
{
	/**
	 * 
	 * Allgemeine Methode zum beschreiben von Text-Dateien. Befindet sich keine Datei unter dem angegeben
	 * Pfad, wird automatisch eine neue Datei an dieser Stelle erzeugt.
	 * 
	 * @param path
	 * 
	 * Pfad der Datei, in die der String "value" geschrieben werden soll
	 * 
	 * @param value
	 * 
	 * String der in die angegebene Datei geschrieben werden soll
	 * 
	 * @param append
	 * 
	 * true = "value" wird ans Ende der Datei geschrieben; false = Datei wird mit "value" überschrieben
	 * 
	 */
	
	
	public static void write(String path, String value, boolean append)
	{
		Writer out = null;
		
		try 
		{ 
			out = new FileWriter(path, append);
			out.write(value);
		} 
		catch (Exception exc) 
		{ 
			System.err.println(exc); 
		} 
	
		finally 
		{ 
			try 
			{ 
				out.close(); 
			} 
			catch (Exception exc) 
			{ 
				
			} 
		}
	}
	
	
}
