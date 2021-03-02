import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import frc.robot.commands.GalacticSearchPath;
import frc.robot.commands.PixyPathFinder;
import frc.robot.subsystems.PixySubsystem;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class TestPixyFromRoboRIOData {
    Gson gson = new Gson();

    interface I {
        public GalacticSearchPath pick(List<PixySubsystem.PixyBlockPlus> c);
    }

    //@Test
    public void test_20210301() {
        runAgainstTestData("pixy_test_data/20210301", PixyPathFinder::findPathVersion20210301);
    }

    boolean runAgainstTestData(String directoryName, I i) {
        boolean ok = true;
        try {
            Path p = Paths.get(directoryName);
            PathMatcher filter = p.getFileSystem().getPathMatcher("glob:**/*.json");
            List<Path> jsonFiles = Files.walk(p)
                    .filter(Files::isRegularFile)
                    .filter(filter::matches)
                    .collect(toList());
            for (Path f : jsonFiles) {
                if (!processOneTestFile(f, i)) ok = false;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ok;
    }

    boolean processOneTestFile(Path f, I i) throws IOException {
        boolean rv = true;
        byte[] byteArray;
        byteArray = Files.readAllBytes(f);
        String s = new String (byteArray);
        List<PixySubsystem.PixyBlockPlus> blocks = parse(s);
        GalacticSearchPath result = i.pick(blocks);
        String parent = f.getParent().getFileName().toString();
        GalacticSearchPath shouldBe = GalacticSearchPath.valueOf(parent);
        System.out.println (f + ", " + result + ", should be " + shouldBe);
        return rv;
    }

    class SavedBlockPlus {
        List<PixySubsystem.PixyBlockPlus> blocks;
    }


    List<PixySubsystem.PixyBlockPlus> parse(String s) {
        Type userListType = new TypeToken<SavedBlockPlus>() {
        }.getType();

        SavedBlockPlus sbp = gson.fromJson(s, userListType);
        return sbp.blocks;
    }
}
