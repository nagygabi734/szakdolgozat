package roles;

import data.Data;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class RobotStarting implements Behavior{

	private boolean isFirstStart=true;
	private Data data;
	private int degrees;
	
	public boolean takeControl() {
		return isFirstStart;
	}

	public void suppress() {
		isFirstStart=false;
	}
	
	public void action() {
		degrees = (int) Math.round(((11.8 * Math.PI) / (5.6 * Math.PI)) * 90.0);
		if (isFirstStart==true) {

			data.getLeftMotor().setSpeed(400);
			data.getRightMotor().setSpeed(400);
			data.setStartingPosition(data.getCompassData());
			Delay.msDelay(400);
			data.getLeftMotor().rotate(degrees, true);
			data.getRightMotor().rotate(-degrees);
			data.getLeftMotor().stop(true);

			data.setStartingDistance(data.getDistanceData());
			Delay.msDelay(200);
			data.getLeftMotor().rotate(-degrees, true);
			data.getRightMotor().rotate(degrees);
			data.getLeftMotor().stop(true);
		}
		LCD.drawString("If you are ready press Enter!", 0, 0);
		Button.ENTER.waitForPressAndRelease();
		LCD.clearDisplay();
		isFirstStart=false;
		
	}


	public RobotStarting(Data globalData) {
		data = globalData;
	}
	
}
