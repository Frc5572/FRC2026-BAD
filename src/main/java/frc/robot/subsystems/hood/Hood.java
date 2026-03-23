package frc.robot.subsystems.hood;

import static edu.wpi.first.units.Units.Degrees;
import java.util.function.Supplier;
import org.littletonrobotics.junction.Logger;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Adjustable Hood
 */
public final class Hood extends SubsystemBase {

    private final HoodIO io;
    public final HoodInputsAutoLogged inputs = new HoodInputsAutoLogged();

    /**
     * Creates a new Hood subsystem.
     *
     * @param io Hardware abstraction
     */
    public Hood(HoodIO io) {
        super("Hood");
        this.io = io;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("Hood", inputs);

        Logger.recordOutput("Hood/ActualAngle (Deg)", inputs.relativeAngle.in(Degrees));
        SmartDashboard.putNumber("Hood/ActualAngle (Deg)", inputs.relativeAngle.in(Degrees));
    }

    public Command moveWithVoltage(double voltage) {
        return run(() -> io.setHoodVoltage(voltage));
    }

    public void setTargetAngle(Angle setAngle) {
        io.setTargetAngle(setAngle);
    }

    public Command setGoal(Angle setAngle) {
        return runOnce(() -> io.setTargetAngle(setAngle));
    }

    public Command setGoal(Supplier<Angle> setAngle) {
        return runOnce(() -> io.setTargetAngle(setAngle.get()));
    }
}
