package voegel;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import katzen.Tiger;

import java.awt.*;
import java.net.URL;

/**
 * This is a test class for learning maven
 * Created by chris on 07.12.15.
 */
public class Lorri {



    @Parameter(names={"-legs", "-beine" }, description = "Anzahl der Beine", help = true)
    public Integer beine =  3;

    public final Color color;

    public Lorri(Color color) {
        this.color = color;
    }


    /**
     * This method calcualtes awesoem mathematicla stuff from quantum fluctuantions
     *
     * @param point_x the x coordiante of the point to calculate stuff for
     * @param point_y the y coordiante of the point to calculate stuff for
     * @return a positive number smaller than 100000
     */
    public double calculateAweseomFormula(double point_x, double point_y){
        return point_x*point_y;
    }

    public static void main(String[] argv){
        URL url = Lorri.class.getResource("/site.html");
        System.out.println(url);

        Lorri lorri = new Lorri(new Color(53, 255, 64));
        new JCommander(lorri, argv);
        System.out.println(lorri.color);

        double result = lorri.calculateAweseomFormula(1.0, 3.0);
        System.out.println(result);

        Tiger t = new Tiger();
        System.out.println(t.beine);

        Integer beine = lorri.beine;
        System.out.println(beine);

    }

}