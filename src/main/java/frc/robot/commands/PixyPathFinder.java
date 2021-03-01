package frc.robot.commands;

import frc.robot.subsystems.PixySubsystem;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class PixyPathFinder {
    static Logger logger = EventLogging.getLogger(PixyPathFinder.class, EventLogging.Level.INFO);

    public static GalacticSearchPath findPath (List<PixySubsystem.PixyBlockPlus> blocks) {
        return sillyPathFinder(blocks);
    }

    public static GalacticSearchPath testFindPath (List<PixySubsystem.PixyBlockPlus> blocks) {
        return sillyPathFinder(blocks);
    }

    public static GalacticSearchPath sillyPathFinder(List<PixySubsystem.PixyBlockPlus> blocks) {
        logger.info ("using pathFinderA");
        if (blocks.size() == 0) return GalacticSearchPath.UNKNOWN;

        final AtomicInteger position = new AtomicInteger(1);

        // this filter needs to be replaced
        // keep signature 1, area > 200, aspect ratio < 1.0, and put the lowest bottom first
        Optional<PixySubsystem.PixyBlockPlus> bbb = blocks.stream()
                .peek (b -> b.addTag ("o:" + Integer.toString(position.getAndIncrement()))) // tag with sequence
                .filter (b -> b.getSignature() == 1)  // signature 1
                .filter (b -> b.getArea() > 200)      // area > 200
                .peek(b -> b.addTag("big_enough"))            // mark it
                .filter (b -> b.getAspectRatio() < 1.0) // higher than is wide
                .peek(b -> b.addTag("aspectOk"))            // mark it
                .sorted (PixySubsystem.lowestBottomFirst)
                .findFirst();

        // well, nothing fit!
        if (bbb.isEmpty()) return GalacticSearchPath.UNKNOWN;

        PixySubsystem.PixyBlockPlus closestBlock = bbb.get();
        closestBlock.addTag("decider");

        // small Y are top of frame

        // the following logic is just for testing.
        // IT NEEDS TO BE REPLACED!

        // image is 316 ✕ 208
        // if the closest block is in the upper part of the frame (furthur away), it's blue, else red
        // if the closest block is in the left of the frame, it's path A, lese B
        //
        // +--------+--------+
        // | A_RED  | B_RED  |
        // +--------+--------+
        // | A_BLUE | B_BLUE |
        // +--------+--------+
        //


        if (closestBlock.getY() < 104) {
            // closest block is in bottom of frame, so it's RED
            if (closestBlock.getX() < 158) {
                return GalacticSearchPath.A_RED;
            } else {
                return GalacticSearchPath.B_RED;
            }
        } else {
            // closest block is in top of frame, so it's BLUE
            if (closestBlock.getX() < 158) {
                return GalacticSearchPath.A_BLUE;
            } else {
                return GalacticSearchPath.B_BLUE;
            }
        }
    }

}
