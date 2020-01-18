package frc.robot.commands;
import org.usfirst.frc3620.logger.EventLogging;

import java.util.function.Consumer;
import edu.wpi.first.wpilibj2.command.Command;
import com.ctre.phoenix.Logger;
import edu.wpi.first.wpilibj2.command.CommandBase;
/**
 * @author Sean Thursby (sthursbyg@gmail.com) / Charlie Vaughn (Cvaughn123)
 * @version 18 January 2020
 * 
 * Finalised command -- rules in subsystem
 */
    public class onCommandEnd_ implements Consumer<Command> {
      public void accept (Command command) {
        System.out.println ("Ended " + command);
    }
}