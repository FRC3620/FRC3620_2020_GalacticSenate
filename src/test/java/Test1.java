
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Add your docs here.
 */
public class Test1 {

    //@Test
    public void test01() {
        assertFalse(false);
        //
        System.out.println ("test01 passed");
    }
    
    //@Test
    public void test02() {
        System.out.println ("test02 passed");
    }

    //@Test
    public void test03() {
        System.out.println (-540 % 360);
        System.out.println (-181 % 360);
        System.out.println (-180 % 360);
        System.out.println (-179 % 360);
        System.out.println (179 % 360);
        System.out.println (180 % 360);
        System.out.println (181 % 360);
        System.out.println (540 % 360);
    }

}