package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.Logger;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Shooter Subsystem
 */
public class Shooter extends SubsystemBase {
    private final ShooterIO io;
    public final ShooterInputsAutoLogged inputs = new ShooterInputsAutoLogged();

    public Shooter(ShooterIO io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("Shooter", inputs);
    }

    /** Set shooter velocity */
    public void setVelocity(double velocity) {
        io.runVelocity(velocity);
    }

    /** Shoot at a given velocity */
    public Command shoot(double velocity) {
        return run(() -> setVelocity(velocity));
    }
}
