package frc.robot.commands;

import org.usfirst.frc3620.logger.EventLogging;

import java.util.function.Consumer;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * @author Sean Thursby (sthursbyg@gmail.com) / Charlie Vaguhn (Cvaughn123)
 * @version 18 January 2020
 * 
 * Finalised command -- rules in subsystem
 */
public class onCommandInitialize_ implements Consumer<Command> {
  public void accept (Command command) {
    System.out.println ("initialized " +command);
  }
}