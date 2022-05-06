package deffender;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.HiTechnicCompass;
import lejos.hardware.sensor.HiTechnicIRSeekerV2;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

public class Data {

	private RegulatedMotor leftMotor;
	private RegulatedMotor rightMotor;
	private RegulatedMotor ballMotor;
	private HiTechnicIRSeekerV2 irSensor;
	private EV3UltrasonicSensor ballSensor;
	private EV3UltrasonicSensor distanceSensor;
	private HiTechnicCompass compassSensor;
	private float[] ballData;
	private float[] irData;
	private float[] distanceData;
	private float[] compassData;
	private int changer;
	private float startingDistance;
	private float startingPosition;

	

	public Data() {
		irSensor = new HiTechnicIRSeekerV2(SensorPort.S4);
		ballSensor = new EV3UltrasonicSensor(SensorPort.S1);
		distanceSensor = new EV3UltrasonicSensor(SensorPort.S3);
		compassSensor = new HiTechnicCompass(SensorPort.S2);
		
		leftMotor=new EV3LargeRegulatedMotor(MotorPort.B);
		rightMotor=new EV3LargeRegulatedMotor(MotorPort.C);
		ballMotor = new EV3MediumRegulatedMotor(MotorPort.A);	
	}

	public float getBallData() {
		SampleProvider ballUltrasonic = ballSensor.getDistanceMode();
		ballData=new float[ballUltrasonic.sampleSize()];
		ballUltrasonic.fetchSample(ballData, 0);
		return ballData[0];
	}
	
	public float getIrData() {
		SampleProvider modulatedIr = irSensor.getModulatedMode();
		irData = new float[modulatedIr.sampleSize()];
		modulatedIr.fetchSample(irData, 0);
		return irData[0];
	}

	public float getDistanceData() {
		SampleProvider distanceUltrasonic = distanceSensor.getDistanceMode();
		distanceData = new float[distanceUltrasonic.sampleSize()];
		distanceUltrasonic.fetchSample(distanceData, 0);
		return distanceData[0];
	}

	public float getCompassData() {		
		SampleProvider sampleCompass = compassSensor.getAngleMode();
		compassData = new float[sampleCompass.sampleSize()];
		sampleCompass.fetchSample(compassData, 0);
		return compassData[0];
	}

	public RegulatedMotor getLeftMotor() {
		return leftMotor;
	}

	public RegulatedMotor getRightMotor() {
		return rightMotor;
	}

	public RegulatedMotor getBallMotor() {
		return ballMotor;
	}

	public int getChanger() {
		return changer;
	}

	public void setChanger(int changer) {
		this.changer = changer;
	}

	public float getStartingDistance() {
		return startingDistance;
	}

	public void setStartingDistance(float startingDistance) {
		this.startingDistance = startingDistance;
	}

	public float getStartingPosition() {
		return startingPosition;
	}

	public void setStartingPosition(float startingPosition) {
		this.startingPosition = startingPosition;
	}

	

}
