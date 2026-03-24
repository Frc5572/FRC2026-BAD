package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import frc.robot.Constants.Shooter;
import frc.robot.util.PhoenixSignals;

/** Shooter Real Implementation */
public class ShooterReal implements ShooterIO {
    private final TalonFX shooterMotorLeft;
    private final TalonFX shooterMotorRight;
    private final TalonFXConfiguration motorConfig;

    private final StatusSignal<AngularVelocity> shooterVelocityLeft;
    private final StatusSignal<AngularVelocity> shooterVelocityRight;
    private final StatusSignal<Voltage> shooterVoltageLeft;
    private final StatusSignal<Voltage> shooterVoltageRight;
    private final StatusSignal<Current> shooterCurrentLeft;
    private final StatusSignal<Current> shooterCurrentRight;

    private final VelocityDutyCycle velocityDutyCycle = new VelocityDutyCycle(0.0);
    private final VoltageOut voltageOut = new VoltageOut(0.0);
    private final VelocityVoltage velocityVoltage = new VelocityVoltage(0.0).withSlot(1);

    public ShooterReal() {
        shooterMotorLeft = new TalonFX(Shooter.leftMotorID);
        shooterMotorRight = new TalonFX(Shooter.rightMotorID);
        motorConfig = new TalonFXConfiguration();

        shooterVelocityLeft = shooterMotorLeft.getVelocity();
        shooterVelocityRight = shooterMotorRight.getVelocity();
        shooterVoltageLeft = shooterMotorLeft.getMotorVoltage();
        shooterVoltageRight = shooterMotorRight.getMotorVoltage();
        shooterCurrentLeft = shooterMotorLeft.getStatorCurrent();
        shooterCurrentRight = shooterMotorRight.getStatorCurrent();

        PhoenixSignals.registerSignals(false, shooterVelocityLeft, shooterVelocityRight,
            shooterVoltageLeft, shooterVoltageRight, shooterCurrentLeft, shooterCurrentRight);
    }

    @Override
    public void runVolts(double volts) {
        shooterMotorLeft.setControl(voltageOut.withOutput(volts));
    }

    @Override
    public void runVelocity(double velocity) {
        shooterMotorLeft.setControl(velocityVoltage.withVelocity(velocity));
    }

    @Override
    public void updateInputs(ShooterInputs inputs) {
        inputs.shooterAngularVelocityLeft = shooterVelocityLeft.getValue();
        inputs.shooterAngularVelocityRight = shooterVelocityRight.getValue();
        inputs.shooterVoltageLeft = shooterVoltageRight.getValue();
        inputs.shooterVoltageRight = shooterVoltageRight.getValue();
        inputs.shooterCurrentLeft = shooterCurrentLeft.getValue();
        inputs.shooterCurrentRight = shooterCurrentRight.getValue();
    }

    @Override
    public void configMotors() {
        motorConfig.MotorOutput.Inverted = Shooter.isReversed ? InvertedValue.Clockwise_Positive
            : InvertedValue.CounterClockwise_Positive;
        motorConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;

        motorConfig.Slot0.kP = 9999.0;
        motorConfig.TorqueCurrent.PeakForwardTorqueCurrent = 1.0;
        motorConfig.TorqueCurrent.PeakReverseTorqueCurrent = 0.0;
        motorConfig.MotorOutput.PeakForwardDutyCycle = 1.0;
        motorConfig.MotorOutput.PeakReverseDutyCycle = 0.0;


        shooterMotorLeft.getConfigurator().apply(motorConfig);
        shooterMotorRight.getConfigurator().apply(motorConfig);

        shooterMotorRight
            .setControl(new Follower(shooterMotorLeft.getDeviceID(), MotorAlignmentValue.Opposed));
    }
}
