package manipulator;

import mmath.Array2d;
import mmath.Pos2d;
import mmath.Vec2d;

import java.io.File;

/**
 * Klasse für die Bildbearbeitungs Methoden
 * Created by chris on 14.01.16.
 */


public class ImageChanger {
    Array2d raw;
    Array2d edit;

    boolean invert = false;
    boolean noise = false;
    boolean rotate = false;
    float noiseFact = 0.0f;


// Konstruktor:


    public ImageChanger()
    {
        raw = new Array2d(new double[1][1], new Pos2d(), new Vec2d(1,1));
    }

//	Methoden:

    // Setter################################
    public void setNoise(boolean noise)
    {
        this.noise = noise;
    }


    //Getter#################################
    public void setInvert(boolean invert)
    {
        this.invert = invert;
    }

    public void setNoiseFactor(double noiseFactor)
    {
        noiseFact = (float) noiseFactor;
    }

    public void setRotate(boolean rotate)
    {
        this.rotate = rotate;
    }

    public Array2d getRawArray()
    {
        return raw;
    }

    public Array2d getEditArray()
    {
        return edit;
    }

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



    public void calculateData()
    {
        edit = new Array2d(raw);

        if(invert) invert();
        if(noise) noise();
        if(rotate) rotate();
    }

    /**
     * Method for saving the edited data
     * @param f target
     */
    public void saveImage(File f)
    {
        edit.saveAsImage(f.getPath());

    }

    /**
     * Methode zum invertieren der einzelnen Pixel-Intensitaeten
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
     * Method for noising the editable Data
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
     * Method for rotating the editable data about 90° to the right
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


    /**
     * method for checking out if data is editable
     * @return resolution of object.x and object.y
     */
    public boolean hasEditableData()
    {
        return raw.getResX() > 0 && raw.getResY() > 0;
    }



}
