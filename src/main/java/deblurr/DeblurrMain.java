package deblurr;


import manipulator.ImageChanger;
import org.apache.commons.cli.*;

/**
 * Main Method for the deblurring program
 * Created by chris on 14.01.16.
 */
public class DeblurrMain {

    private static final String CLASS_NAME = "de.physik.e1b.chris.cli.deblurring";


    // Main method
    public static void main(String[] args){



        // Attributes ###########################
        ImageChanger ic ;                // Image Changer Object
        CommandLine cmd = null;          // Command Line Object
        Options dbOptions;               // Options Object
        CommandLineParser parser;        // Parser Object
        HelpFormatter dbFromatter;       // the Help Fomratter

        //Construcor ###########################
        ic = new ImageChanger();            // Create the Image Changer Object

        parser = new DefaultParser();       // Creates the CommandLineParser
        dbFromatter = new HelpFormatter();  // Creates the HelpFormatter

        dbOptions = new Options();          // Create the optios for the CLI

        Option dbHelp = new Option("h","help", false, "shows the help dialoge");
        Option dbName = new Option("name",true,"the name.");

        Option dbRotate = new Option("r","rotate",true,"rotates the given data file");
        Option dbInvert = new Option("i", "invert", true, "inverts the given data file");
        Option dbNoise = new Option("n","noise",true,"puts a noise with a given gain to the data file");


        // adding the Options #########################
        dbOptions.addOption(dbHelp);
        dbOptions.addOption(dbName);
        dbOptions.addOption(dbRotate);
        dbOptions.addOption(dbInvert);
        dbOptions.addOption(dbNoise);












        /* doing the real stuff stuff !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         TODO Schreibe methode die bei Optionsaufruf die Booleans setted
         TODO Frage!!! wie macht man der CLI ohne option builder klar dass 2 oder 3 args übergebe werden und wie greife ich auf diese zu?
        */
        try{
            cmd = parser.parse(dbOptions,args);

            if(cmd.hasOption("r")| cmd.hasOption("rotate")){


            }
        }
        catch (ParseException pvException){
            System.err.println(pvException.getMessage());
        }




        // create help formatter
        dbFromatter.printHelp("deblurring",dbOptions);


    }
}
