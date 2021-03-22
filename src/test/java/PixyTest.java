import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import frc.robot.commands.GalacticSearchPath;
import frc.robot.commands.PixyPathFinder;
import frc.robot.subsystems.PixySubsystem;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PixyTest {
    Gson gson = new Gson();

    //@Test
    public void t1() {
        List<PixySubsystem.PixyBlockPlus> bb = parse("[{\"block\":{\"signature\":2,\"x\":165,\"y\":54,\"width\":86,\"height\":56,\"angle\":0,\"index\":60,\"age\":255},\"aspectRatio\":1.5357142857142858,\"area\":4816,\"bottom\":82.0,\"tags\":[\"o:1\"]},{\"block\":{\"signature\":1,\"x\":276,\"y\":93,\"width\":56,\"height\":59,\"angle\":0,\"index\":25,\"age\":255},\"aspectRatio\":0.9491525423728814,\"area\":3304,\"bottom\":122.5,\"tags\":[\"o:2\",\"big_enough\",\"aspectOk\",\"decider\"]}]");
        printBlocks("** t1 raw", bb);
        Assert.assertEquals("raw block size", 2, bb.size());

        List<PixySubsystem.PixyBlockPlus> b1 = new ArrayList<>();
        List<PixySubsystem.PixyBlockPlus> b2 = new ArrayList<>();
        List<PixySubsystem.PixyBlockPlus> bbb = bb.stream()
                .filter(b -> b.getArea() > 200)
                .peek(b1::add)
                .sorted(PixySubsystem.byDecreasingSize)
                .peek(b2::add)
                .filter(b -> b.getAspectRatio() < 1.0) // higher than is wide
                .collect(Collectors.toList());
        printBlocks("filtered by area", b1);
        printBlocks("filtered by area, sorted", b2);
        printBlocks("filtered by area, sorted, filtered by aspect ratio", bbb);
        Assert.assertEquals("filtered block size", bbb.size(), 1);
    }

    //@Test
    public void t2() {
        String s = "[{\"block\":{\"signature\":2,\"x\":165,\"y\":54,\"width\":86,\"height\":56,\"angle\":0,\"index\":60,\"age\":255},\"aspectRatio\":1.5357142857142858,\"area\":4816,\"bottom\":82.0,\"tags\":[\"o:1\"]},{\"block\":{\"signature\":1,\"x\":276,\"y\":93,\"width\":56,\"height\":59,\"angle\":0,\"index\":25,\"age\":255},\"aspectRatio\":0.9491525423728814,\"area\":3304,\"bottom\":122.5,\"tags\":[\"o:2\",\"big_enough\",\"aspectOk\",\"decider\"]}]";
        System.out.println(s);
        List<PixySubsystem.PixyBlockPlus> bb = parse(s);
        printBlocks("** t2 raw", bb);
        Assert.assertEquals("raw block size", 2, bb.size());

        List<PixySubsystem.PixyBlockPlus> b1 = new ArrayList<>();
        List<PixySubsystem.PixyBlockPlus> b2 = new ArrayList<>();
        List<PixySubsystem.PixyBlockPlus> bbb = bb.stream()
                .filter(b -> b.getArea() > 200)
                .peek(b1::add)
                .sorted(PixySubsystem.lowestBottomFirst)
                .peek(b2::add)
                .filter(b -> b.getAspectRatio() < 1.0) // higher than is wide
                .collect(Collectors.toList());
        printBlocks("filtered by area", b1);
        printBlocks("filtered by area, sorted", b2);
        printBlocks("filtered by area, sorted, filtered by aspect ratio", bbb);
        //Assert.assertEquals("filtered block size", bbb.size(), 1);
    }

    //@Test
    public void testPixySillyPathFinder1() {
        testOneSillyCase(
                "[{\"block\":{\"signature\":1,\"x\":111,\"y\":34,\"width\":38,\"height\":47,\"angle\":0,\"index\":35,\"age\":255},\"aspectRatio\":0.8085106382978723,\"area\":1786,\"bottom\":57.5,\"tags\":[\"o:1\",\"big_enough\",\"aspectOk\",\"decider\"]},{\"block\":{\"signature\":2,\"x\":102,\"y\":94,\"width\":44,\"height\":6,\"angle\":0,\"index\":37,\"age\":255},\"aspectRatio\":7.333333333333333,\"area\":264,\"bottom\":97.0,\"tags\":[\"o:2\"]},{\"block\":{\"signature\":1,\"x\":168,\"y\":125,\"width\":12,\"height\":5,\"angle\":0,\"index\":75,\"age\":30},\"aspectRatio\":2.4,\"area\":60,\"bottom\":127.5,\"tags\":[\"o:3\"]}]",
                GalacticSearchPath.UNKNOWN);
        testOneSillyCase(
                "[{\"block\":{\"signature\":1,\"x\":225,\"y\":42,\"width\":38,\"height\":44,\"angle\":0,\"index\":64,\"age\":255},\"aspectRatio\":0.8636363636363636,\"area\":1672,\"bottom\":64.0,\"tags\":[\"o:1\",\"big_enough\",\"aspectOk\",\"decider\"]},{\"block\":{\"signature\":2,\"x\":103,\"y\":93,\"width\":42,\"height\":7,\"angle\":0,\"index\":69,\"age\":255},\"aspectRatio\":6.0,\"area\":294,\"bottom\":96.5,\"tags\":[\"o:2\"]},{\"block\":{\"signature\":1,\"x\":168,\"y\":126,\"width\":12,\"height\":6,\"angle\":0,\"index\":162,\"age\":15},\"aspectRatio\":2.0,\"area\":72,\"bottom\":129.0,\"tags\":[\"o:3\"]}]",
                GalacticSearchPath.UNKNOWN);
        testOneSillyCase(
                "[{\"block\":{\"signature\":1,\"x\":100,\"y\":129,\"width\":60,\"height\":77,\"angle\":0,\"index\":183,\"age\":167},\"aspectRatio\":0.7792207792207793,\"area\":4620,\"bottom\":167.5,\"tags\":[\"o:1\",\"big_enough\",\"aspectOk\",\"decider\"]},{\"block\":{\"signature\":1,\"x\":168,\"y\":125,\"width\":12,\"height\":5,\"angle\":0,\"index\":197,\"age\":110},\"aspectRatio\":2.4,\"area\":60,\"bottom\":127.5,\"tags\":[\"o:2\"]}]",
                GalacticSearchPath.UNKNOWN);
        testOneSillyCase(
                "[{\"block\":{\"signature\":1,\"x\":245,\"y\":107,\"width\":54,\"height\":60,\"angle\":0,\"index\":201,\"age\":131},\"aspectRatio\":0.9,\"area\":3240,\"bottom\":137.0,\"tags\":[\"s1\",\"big_enough\",\"aspectOk\",\"decider\"]},{\"block\":{\"signature\":2,\"x\":106,\"y\":111,\"width\":56,\"height\":42,\"angle\":0,\"index\":203,\"age\":131},\"aspectRatio\":1.3333333333333333,\"area\":2352,\"bottom\":132.0,\"tags\":[]},{\"block\":{\"signature\":2,\"x\":270,\"y\":7,\"width\":44,\"height\":15,\"angle\":0,\"index\":229,\"age\":26},\"aspectRatio\":2.933333333333333,\"area\":660,\"bottom\":14.5,\"tags\":[]},{\"block\":{\"signature\":2,\"x\":178,\"y\":34,\"width\":28,\"height\":4,\"angle\":0,\"index\":218,\"age\":64},\"aspectRatio\":7.0,\"area\":112,\"bottom\":36.0,\"tags\":[]},{\"block\":{\"signature\":1,\"x\":169,\"y\":127,\"width\":10,\"height\":5,\"angle\":0,\"index\":213,\"age\":95},\"aspectRatio\":2.0,\"area\":50,\"bottom\":129.5,\"tags\":[\"s1\"]}]",
                GalacticSearchPath.UNKNOWN);
        testOneSillyCase(
                "[{\"block\":{\"signature\":2,\"x\":101,\"y\":94,\"width\":46,\"height\":9,\"angle\":0,\"index\":52,\"age\":170},\"aspectRatio\":5.111111111111111,\"area\":414,\"bottom\":98.5,\"tags\":[\"o:1\"]},{\"block\":{\"signature\":2,\"x\":115,\"y\":129,\"width\":18,\"height\":6,\"angle\":0,\"index\":98,\"age\":2},\"aspectRatio\":3.0,\"area\":108,\"bottom\":132.0,\"tags\":[\"o:2\"]},{\"block\":{\"signature\":1,\"x\":168,\"y\":126,\"width\":12,\"height\":6,\"angle\":0,\"index\":93,\"age\":16},\"aspectRatio\":2.0,\"area\":72,\"bottom\":129.0,\"tags\":[\"o:3\"]}]",
                GalacticSearchPath.UNKNOWN);
    }

    void testOneSillyCase(String s, GalacticSearchPath desiredResult) {
        System.out.println(s);
        List<PixySubsystem.PixyBlockPlus> bb = parse(s);
        GalacticSearchPath r = PixyPathFinder.sillyPathFinder(bb);
        printBlocks("results", bb);
        System.out.println(r);
        Assert.assertEquals("path", desiredResult, r);
    }


    List<PixySubsystem.PixyBlockPlus> parse(String s) {
        Type userListType = new TypeToken<ArrayList<PixySubsystem.PixyBlockPlus>>() {
        }.getType();

        ArrayList<PixySubsystem.PixyBlockPlus> bb1 = gson.fromJson(s, userListType);
        return bb1;
    }

    void printBlocks(String s, List<PixySubsystem.PixyBlockPlus> bb) {
        System.out.println(s);
        for (PixySubsystem.PixyBlockPlus b : bb) {
            System.out.println(b);
        }
    }
}
