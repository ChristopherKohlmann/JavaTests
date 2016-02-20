package manipulator;

import mmath.Array2d;
import mmath.Pos2d;
import mmath.Vec2d;

import java.io.File;

/**
 * calss for the image changig methods
 * Created by chris on 14.01.16.
 */


public class ImageChanger
{
    Array2d raw;  // raw Image, important for the GUI version
    Array2d edit;   // editable Image, gets all the changes

    boolean invert = false;
    boolean noise = false;
    boolean rotate = false;
    boolean blur = false;

    int blurRadius = 3;
    float noiseFact = 0.0f;


// Konstruktor#######################################

    // creates a null Object wich does no damage if falsely edited
    public ImageChanger()
    {
        raw = new Array2d(new double[1][1], new Pos2d(), new Vec2d(1,1));
    }

//	Methoden#########################################

    /**
     * method for loading the picture file
     * @param f to be loaded file
     */
    public void loadA2d(File f)
    {
        raw = null;

        if(f != null)
            raw = new Array2d(f);

        if(raw == null)
            raw = new Array2d(new double[1][1], new Pos2d(), new Vec2d(1,1));

        edit = new Array2d(raw);
    }


    /**
     * Method, which checks which treatments should be executed and executes them
     */
    public void calculateData()
    {
        edit = new Array2d(raw);

        if(invert) invert();
        if(noise) noise();
        if(rotate) rotate();
        if(blur) blurSimple();
    }

    /**
     * Method for saving the edited data
     * @param f target file
     */
    public void saveImage(File f)
    {
        edit.saveAsImage(f.getPath());

    }

    /**
     * Method to invert the intensity of the data points
     */
    private void invert()
    {
        System.out.println("Invertiere!");

        double[][] data= edit.getNorm();

        for(int i =0; i< data.length; i++) 		// invertieren algorithmus
        {
            for(int j = 0; j<data[i].length; j++)
            {
                data[i][j] = 1.0 - data[i][j];
            }
        }
        edit.setData(data);
    }


    /**
     * Method for putting noise on the editable data
     * Needs a noise gain factor
     */
    private void noise()
    {
        System.out.println("Verrausche!");


        double [][] data = edit.getNorm();

        for(int i=0;i<data.length;i++)
        {
            for(int j=0; j<data[i].length; j++)
            {
                data[i][j] += (noiseFact/100.0f)*Math.random();
            }
        }

        // sets the object "edit" to the manipulated data
        edit.setData(data);

    }

    /**
     * Method for rotating the editable data about 90° to the left
     */
    private void rotate()
    {
        System.out.println("Rotiere!");
        double[][] data = edit.getNorm();
        double[][] dataHelp = new double[data[0].length][data.length];
        for(int i=0;i<dataHelp.length; i++)
        {
            for(int j=0; j<dataHelp[i].length;j++)
            {
                dataHelp[i][j] = data[j][data[j].length-i-1];
            }
        }
        edit.setData(dataHelp);
    }

    // TODO blur with convolution erarbeiten!!
    /**
     * method for blurring data with a given blur radius
     * This algorithm adds the blur iteratively to each data point
     * No convolution!
     *
     */
    private void blurSimple()
    {
        System.out.println("Blur!");
        double[][] data = edit.getNorm();
        int blurSQ = blurRadius * blurRadius;
        double norm, sum;
        int x, y;
        boolean addValue;

        for(int i = 0; i < data.length; i++)
        {
            for(int j = 0; j < data[i].length; j++)
            {
                norm = 0.0;
                sum = 0.0;

                for(int m = -blurRadius; m <= blurRadius; m++)
                {
                    for(int n = -blurRadius; n <= blurRadius; n++)
                    {
                        if(m*m + n*n <= blurSQ)
                        {
                            x = i+m;
                            y = j+n;

                            addValue = 	((x >= 0 && x < edit.getResX()) || edit.isSeamlessX()) &&
                                    ((y >= 0 && y < edit.getResY()) || edit.isSeamlessY());

                            if(addValue) {sum += edit.get(x, y); norm++;}
                        }
                    }
                }

                if(norm > 0.0) 	data[i][j] = sum / norm;
                else			data[i][j] = edit.get(i, j);
            }
        }

        edit.setData(data);

    }
    /**
     * method for checking out if data is editable
     * @return True if data is editable
     */
    public boolean hasEditableData()
    {
        return raw.getResX() > 0 && raw.getResY() > 0;
    }



    // Setter..........................................................................
    public void setNoise(boolean noise)
    {
        this.noise = noise;
    }

    public void setNoiseFactor(double noiseFactor)
    {
        noiseFact = (float) noiseFactor;
    }

    public void setInvert(boolean invert)
    {
        this.invert = invert;
    }

    public void setRotate(boolean rotate)
    {
        this.rotate = rotate;
    }

    public void setBlur(boolean blur) {this.blur = blur;}

    public void setBlurRadius (int blurRadius){this.blurRadius = blurRadius;}

    //Getter..........................................................................


    public Array2d getRawArray()
    {
        return raw;
    }

    public Array2d getEditArray()
    {
        return edit;
    }




}
