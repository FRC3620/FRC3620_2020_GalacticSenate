
import static org.junit.Assert.assertFalse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import frc.robot.subsystems.PixySubsystem;
import io.github.pseudoresonance.pixy2api.Pixy2CCC;
import org.junit.Test;

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
    public void testDateFormatter() {
        System.out.println (DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now()));
    }


}