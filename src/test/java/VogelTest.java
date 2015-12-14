import org.junit.Test;
import voegel.Lorri;

import java.awt.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


/**
 * Created by chris on 07.12.15.
 */
public class VogelTest {

    Lorri lorri = new Lorri(Color.BLUE);


    @Test
    public void testAweseomMathe(){

        double result = lorri.calculateAweseomFormula(3.0, 3.0);
        assertThat(result, is(9.0));

    }
}
