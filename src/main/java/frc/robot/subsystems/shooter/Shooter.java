package frc.robot.subsystems.shooter;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import org.littletonrobotics.junction.Logger;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

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
        SmartDashboard.putBoolean("Shooter/UpToSpeed", inputs.shooterAngularVelocityLeft
            .in(RadiansPerSecond) > Constants.Shooter.atSpeedThreshold);
        Constants.Shooter.constants.ifDirty(constants -> {
            io.setConstants(constants);
        });
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
