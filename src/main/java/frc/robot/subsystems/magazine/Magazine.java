package frc.robot.subsystems.magazine;

import static edu.wpi.first.units.Units.Volts;
import org.littletonrobotics.junction.Logger;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Indexer class
 */
public class Magazine extends SubsystemBase {
    private final MagazineIO io;
    private final MagazineInputsAutoLogged inputs = new MagazineInputsAutoLogged();

    public Magazine(MagazineIO io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("Indexer", inputs);
        Constants.Magazine.constants.ifDirty(constants -> {
            io.setConstants(constants);
        });
    }


    public Command setVoltage(double voltage) {
        Logger.recordOutput("Magazine/targetVoltage", Volts.of(voltage));
        return run(() -> io.setMagazineVoltage(voltage));
    }
}


