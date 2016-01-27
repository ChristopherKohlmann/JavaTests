
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;



/**
 * Test class for the Filechoosing Method
 * Created by chris on 14.01.16.
 */


public class FileChooserTester {
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        JFileChooser jfc = new JFileChooser("/home/chris/Dokumente/MasterArbeit/Examples/");
		/*
		jfc.removeChoosableFileFilter(jfc.getChoosableFileFilters()[0]);
   		jfc.setFileFilter(new FileFilter()
   		{
   			public boolean accept(File f)
   			{
   				if (f.isDirectory()){return true;}

	    		return 	f.getName().toLowerCase().endsWith(".png");
   			}

   			public String getDescription() {return "Nur png-Files auswählbar!!!";}
   		});
		*/
        if(jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            File f = jfc.getSelectedFile();



            System.out.println(f.getPath());
        }
    }


}
