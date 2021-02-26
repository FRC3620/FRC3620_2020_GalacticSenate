
import static org.junit.Assert.assertFalse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import frc.robot.subsystems.PixySubsystem;
import io.github.pseudoresonance.pixy2api.Pixy2CCC;
import org.junit.Test;

import java.lang.reflect.Type;
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
    public void pixyGsonTest() {
        String s;
        Gson gson = new Gson();

        Pixy2CCC.Block b = new Pixy2CCC.Block(1, 2, 3, 4, 5, 6, 7, 8);
        Pixy2CCC.Block c = new Pixy2CCC.Block(1, 2, 3, 5, 5, 6, 7, 8);
        System.out.println (b);
        s = gson.toJson(b);
        System.out.println (s);
        Pixy2CCC.Block b1 = gson.fromJson(s, Pixy2CCC.Block.class);
        System.out.println (b1);

        ArrayList<Pixy2CCC.Block> bb = new ArrayList<>();
        bb.add(b);
        bb.add(c);
        bb.add(b);
        System.out.println (bb);
        s = gson.toJson(bb);
        System.out.println (s);

        Type userListType = new TypeToken<ArrayList<Pixy2CCC.Block>>(){}.getType();

        ArrayList<Pixy2CCC.Block> bb1 = gson.fromJson(s, userListType);
        System.out.println (bb1);
        bb1.sort(PixySubsystem.byDecreasingSize);

        System.out.println (bb1);

    }

}