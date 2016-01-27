import mmath.Array2d;

import java.io.File;


/**
 * Tester Class for the simple methods
 * Created by chris on 14.01.16.
 */
public class MiniTester {


    public static void main(String[] args) {

        Array2d a = new Array2d(new File("/home/chris/Dokumente/MasterArbeit/Examples/Lorri1.png"));



        //a.save("/home/chris/Dokumente/MasterArbeit/Examples/Lorri.a2d");  // speichert die a2d Datei


        rotate(a).saveAsImage("/home/chris/Dokumente/MasterArbeit/Examples/Lorri1-1.png");  // ruft eine der  Methoden auf und speichert diese direkt als .png

    }

    /**
     * Methode zum Invertieren der gegebenen Bilddatei bzw, des gegb. Datensatzes
     * @param a Datensatz (Bild Datei)vom Typ Array2d
     * @return Array2d mit den verrauschten Daten, der Ursprungskoordinate und der Schrittweite
     */
    public static Array2d invert(Array2d a)  // invert: Methode zum invertieren der einzelnen Pixel-Intensitaeten
    {
        double [][] data= a.getNorm();

        for(int i =0; i< data.length; i++) 		// invertieren algorithmus
        {
            for(int j = 0; j<data[i].length; j++)
            {
                data[i][j] = 1.0 - data[i][j];
            }
        }
        return new Array2d(data, a.getOrigin(), a.getStep());
    }


    /**
     * Methode zum Verrauschen der gegebenen Bilddatei bzw, des gegb. Datensatzes
     * @param a Datensatz (Bild Datei)vom Typ Array2d
     * @return Array2d mit den verrauschten Daten, der Ursprungskoordinate und der Schrittweite
     */
    public static Array2d noise(Array2d a) // corn: Methode zum verrauschen von Bildern
    {
        double [][] data = a.getNorm();
        System.out.println("Bitte geben Sie einen Rausch Faktor ein (in %):");

        //@Parameter(names={"-noiseFactor", "-rauschFaktor"},description = "gewuenschter Rauschfaktor", help= true)
        float noiseFact = new java.util.Scanner(System.in).nextFloat();
        for(int i=0;i<data.length;i++)
        {
            for(int j=0; j<data[i].length; j++)
            {
                data[i][j] += (noiseFact/100)*Math.random();
            }
        }

        return new Array2d(data, a.getOrigin(),a.getStep());
    }

    public static Array2d rotate(Array2d a)
    {
        double[][] data = a.getNorm();
        double[][] dataHelp = new double[data[0].length][data.length];
        for(int i=0;i<dataHelp.length; i++)
        {
            for(int j=0; j<dataHelp[i].length;j++)
            {
                dataHelp[i][j] = data[j][data[j].length-i-1];
            }
        }
        return new Array2d(dataHelp,a.getOrigin(),a.getStep());
    }


}
