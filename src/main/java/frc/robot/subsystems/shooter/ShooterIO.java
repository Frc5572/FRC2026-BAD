package frc.robot.subsystems.shooter;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;
import org.littletonrobotics.junction.AutoLog;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import frc.robot.util.GenerateEmptyIO;

/**
 * Shooter IO Interface
 */
@GenerateEmptyIO
public interface ShooterIO {
    /** Shooter Inputs Class */
    @AutoLog
    public static class ShooterInputs {
        public AngularVelocity shooterAngularVelocityLeft = RadiansPerSecond.zero();
        public AngularVelocity shooterAngularVelocityRight = RadiansPerSecond.zero();
        public Voltage shooterVoltageLeft = Volts.zero();
        public Voltage shooterVoltageRight = Volts.zero();
        public Current shooterCurrentLeft = Amps.zero();
        public Current shooterCurrentRight = Amps.zero();
    }

    public void updateInputs(ShooterInputs inputs);

    public void runVolts(double volts);

    public void runVelocity(double velocity);

    public void configMotors();
}
