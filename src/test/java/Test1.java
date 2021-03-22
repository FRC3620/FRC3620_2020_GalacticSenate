
import static org.junit.Assert.assertFalse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import frc.robot.subsystems.PixySubsystem;
import io.github.pseudoresonance.pixy2api.Pixy2CCC;
import org.junit.Test;

import java.util.Date;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

    //@Test
    public void test04() {
        double error = Double.NaN;
        if (error >= 0.98 && error <= 1.02) {
            System.out.println ("" + error + " in range");
        } else {
            System.out.println ("" + error + " out of range");
        }
    }

    //@Test
    public void test05() {
        Date date = new Date();
        System.out.println (date);
    }

    @Test
    public void test06() {
        calltest();
    }

    void calltest() {
        System.out.println (miniTraceback());
    }

    String miniTraceback() {
        StackTraceElement[] st = Thread.currentThread().getStackTrace();
        for (StackTraceElement st1 : st) {
            System.out.println(st1.getClassName() + "." + st1.getMethodName() + ":" + st1.getLineNumber());
        }
        StackTraceElement st1 = st[3];
        String rv = st1.getClassName() + "." + st1.getMethodName() + ":" + st1.getLineNumber();
        return rv;
    }
    public void testDateFormatter() {
        System.out.println (DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now()));
    }


}