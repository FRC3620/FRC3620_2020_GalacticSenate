package frc.robot.commands;

import frc.robot.subsystems.PixySubsystem;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PixyPathFinder {
    static Logger logger = EventLogging.getLogger(PixyPathFinder.class, EventLogging.Level.INFO);

    static final double HEIGHT = 208;
    static final double WIDTH = 316;

    public static GalacticSearchPath findPath (List<PixySubsystem.PixyBlockPlus> blocks) {
        return findPathVersion20210301(blocks);
    }

    public static GalacticSearchPath findPathVersion20210301  (List<PixySubsystem.PixyBlockPlus> blocks) {
        logger.info("using findPathVersion20210301");

        if (blocks.size() == 0) return GalacticSearchPath.UNKNOWN;

        /*
         * this code gets the two "closest" (lowest in the frame) blocks.
         * keep signature 1
         * keep blocks whose centers are in the lowest 60% of the frame
         * put the lowest bottom first
         */
        List<PixySubsystem.PixyBlockPlus> bbb = blocks.stream()
                .filter(b -> b.getSignature() == 1)  // signature 1
                .filter(b -> b.getY() > 0.4 * HEIGHT) // in bottom 60% of frame
                .peek(b -> b.addTag("low"))            // mark them
                .sorted(PixySubsystem.lowestBottomFirst)
                .collect(Collectors.toList());

        // well, we didn't have any blocks like that
        if (bbb.isEmpty()) return GalacticSearchPath.UNKNOWN;

        // get the "closest" block (and mark it for debugging)
        PixySubsystem.PixyBlockPlus closestBlock = bbb.get(0);
        closestBlock.addTag("decider");

        // make decision based on the closest block
        double x = closestBlock.getX();
        double y = closestBlock.getY();
        
        // will probably need to look at 'Y' to tell differences between red and blue?
        //Tests X and Y cords of balls seen to determine corresponding paths
        if (x >= 66 && x <= 106) {
            if(y >= 104 && y <= 144){
                return GalacticSearchPath.B_RED;
            }
        }

        if (x >= 200 && x <= 240) {
            if(y >= 75 && y <= 115) {
                return GalacticSearchPath.B_BLUE;
            } 
        }

        if (x >= 244 && x <= 288) {
            if(y >= 77 && y <= 117) {
                return GalacticSearchPath.B_BLUE;
            }
        }

        if (x >= 144 && x <= 184) {
            if(y >= 99 && y <= 139){
                 return GalacticSearchPath.A_RED;
                }
        }
        
        // nothing matched!
        return GalacticSearchPath.UNKNOWN;
    }



        public static GalacticSearchPath sillyPathFinder(List<PixySubsystem.PixyBlockPlus> blocks) {
        logger.info ("using sillyPathFinder");
        return GalacticSearchPath.UNKNOWN;
    }

}
