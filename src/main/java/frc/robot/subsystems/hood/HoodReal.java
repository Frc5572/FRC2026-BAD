package frc.robot.subsystems.hood;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import frc.robot.Constants;
import frc.robot.util.PhoenixSignals;

public class HoodReal implements HoodIO {
    private final TalonFX hoodMotor = new TalonFX(Constants.hood.hoodID);
    private final TalonFXConfiguration hoodMotorConfig = new TalonFXConfiguration();

    private StatusSignal<Angle> hoodAngle = hoodMotor.getPosition();
    private StatusSignal<Voltage> hoodVoltage = hoodMotor.getMotorVoltage();
    private StatusSignal<Current> hoodCurrent = hoodMotor.getStatorCurrent();
    private StatusSignal<AngularVelocity> hoodVelocity = hoodMotor.getVelocity();

    private final VoltageOut voltage = new VoltageOut(0.0);

    private final PositionVoltage mmVoltage = new PositionVoltage(0);

    /** Real Hood Implementation */
    public HoodReal() {

        // PID and feedforward

        hoodMotorConfig.Slot0.kP = Constants.hood.KP;
        hoodMotorConfig.Slot0.kI = Constants.hood.KI;
        hoodMotorConfig.Slot0.kD = Constants.hood.KD;
        hoodMotorConfig.Slot0.kS = Constants.hood.KS;
        hoodMotorConfig.Slot0.kV = Constants.hood.KV;
        hoodMotorConfig.Slot0.kA = Constants.hood.KA;
        hoodMotorConfig.Slot0.kG = Constants.hood.KG;

        hoodMotorConfig.Feedback.SensorToMechanismRatio = Constants.hood.gearRatio;

        hoodMotor.getConfigurator().apply(hoodMotorConfig);

        hoodMotor.setNeutralMode(NeutralModeValue.Brake);

        PhoenixSignals.tryUntilOk(5, () -> BaseStatusSignal.setUpdateFrequencyForAll(50, hoodAngle,
            hoodVoltage, hoodCurrent, hoodVelocity));
        PhoenixSignals.tryUntilOk(5, () -> ParentDevice.optimizeBusUtilizationForAll(hoodMotor));
        PhoenixSignals.registerSignals(false, hoodAngle, hoodVoltage, hoodCurrent, hoodVelocity);
    }



    @Override
    public void setHoodVoltage(double volts) {
        hoodMotor.setControl(voltage.withOutput(volts));
    }

    @Override
    public void updateInputs(HoodInputs inputs) {
        inputs.relativeAngle = hoodAngle.getValue();
        inputs.voltage = hoodVoltage.getValue();
        inputs.current = hoodCurrent.getValue();
        inputs.velocity = hoodVelocity.getValue();

        inputs.hoodLocation = hoodAngle.getValueAsDouble();
    }

    @Override
    public void setTargetAngle(Angle angle) {
        hoodMotor.setControl(mmVoltage.withPosition(angle));
    }

}
