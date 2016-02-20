package deblurr;


import manipulator.ImageChanger;
import org.apache.commons.cli.*;

import java.io.File;

/**
 * Main Method for the deblurring program
 * Created by chris on 14.01.16.
 */
public class DeblurrMain
{

    private static final String CLASS_NAME = "de.physik.e1b.chris.cli.deblurring";


    // Main method
    public static void main(String[] args)
    {



        // Attributes ###########################
        ImageChanger ic ;                // Image Changer Object
        CommandLine cmd;          // Command Line Object
        Options dbOptions;               // Options Object
        CommandLineParser parser;        // Parser Object
        HelpFormatter dbFromatter;       // the Help Fomratter

        //Construcor ###########################
        ic = new ImageChanger();            // Create the Image Changer Object

        parser = new DefaultParser();       // Creates the CommandLineParser
        dbFromatter = new HelpFormatter();  // Creates the HelpFormatter

        dbOptions = new Options();          // Create the optios for the CLI

        Option dbHelp = new Option("h","help", false, "Shows the help dialog.");
        Option dbName = new Option("name",true,"Programs Name");
        Option dbRotate = new Option("r","rotate",false,"Rotates the given data file.");
        Option dbInvert = new Option("i", "invert", false, "Inverts the given data file.");

        Option dbNoise = Option.builder("n")
                .longOpt("noise")
                .valueSeparator()
                .numberOfArgs(1)
                .desc("Puts random noise on the data with a given noiseGainFactor.")
                .build();

        Option dbBlur = Option.builder("b")
                .longOpt("blur")
                .valueSeparator()
                .numberOfArgs(1)
                .build();

        Option dbLoad =  Option.builder("l")
                .required()
                .numberOfArgs(1)
                .valueSeparator()
                .longOpt("load")
                .desc("Loads the picture from the given path. " )
                .build();

        Option dbSave =  Option.builder("s")
                .numberOfArgs(1)
                .valueSeparator()
                .longOpt("save")
                .desc("Saves the picture to the given path and filename. \n" +
                        "If not selected: original file will be overwritten!! ")
                .build();

        // adding the Options .....................................
        dbOptions.addOption(dbHelp);
        dbOptions.addOption(dbName);
        dbOptions.addOption(dbRotate);
        dbOptions.addOption(dbInvert);
        dbOptions.addOption(dbNoise);
        dbOptions.addOption(dbBlur);
        dbOptions.addOption(dbLoad);
        dbOptions.addOption(dbSave);






        // parsing the Line !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        try
        {
            cmd = parser.parse(dbOptions,args);

            if(cmd.hasOption("name"))
            {
                System.out.println(CLASS_NAME);
            }
            else
            {
                if(cmd.hasOption("h")|cmd.hasOption("help"))
                {
                    dbFromatter.printHelp("deblurring",dbOptions);
                }
            }

            // load the Data
            try
            {
                File f = new File(cmd.getOptionValue("l"));
                ic.loadA2d(f);
                if(! ic.hasEditableData()) { throw new Exception("Dataset Empty!!!"); }
            }
            catch(Exception e)
            {
                System.err.println(e.getMessage());
            }

            ic.setRotate(cmd.hasOption("r")|cmd.hasOption("rotate"));

            ic.setInvert(cmd.hasOption("i")|cmd.hasOption("invert"));

            ic.setNoise(cmd.hasOption("n")|cmd.hasOption("noise"));
            if(cmd.hasOption("n")|cmd.hasOption("noise"))
            {
                ic.setNoiseFactor(Double.parseDouble(cmd.getOptionValue("n","noise")));
            }

            ic.setBlur(cmd.hasOption("b")|cmd.hasOption("blur"));
            if(cmd.hasOption("b")|cmd.hasOption("blur"))
            {
                ic.setBlurRadius(Integer.parseInt(cmd.getOptionValue("b","blur")));
            }

            // calculates the edited data array dependent on the selected options
            ic.calculateData();


            // save the file
            try
            {
                if(cmd.hasOption("s")|cmd.hasOption("save"))
                {
                    File f = new File(cmd.getOptionValue("s"));
                    ic.saveImage(f);
                }
                else
                {
                    File f = new File(cmd.getOptionValue("l"));
                    ic.saveImage(f);
                }


            }
            catch(Exception e)
            {
                System.err.println(e.getMessage());
            }

        }
        catch (ParseException pvException)
        {
            System.err.println(pvException.getMessage());
            dbFromatter.printHelp("deblurring",dbOptions);
        }








    }
}
