package roles;

import data.Data;
import lejos.robotics.subsumption.Behavior;

public class Deffensive implements Behavior {

	private Data data;
	private boolean rightWay;
	private boolean gate;
	private boolean goBackward;
	private boolean enemyGate;
	private int degrees;

	public boolean takeControl() {
		if (data.getBallData() < 0.1f) {
			data.setChanger(0);
			return false;
		} else if (data.getChanger() == 1) {
			return true;
		} else
			return false;
	}

	public void suppress() {
		data.getLeftMotor().stop();
		data.getRightMotor().stop();
	}

	public void action() {
		degrees = (int) Math.round(((11.8 * Math.PI) / (5.6 * Math.PI)) * 90.0);		
		System.out.println("Védekezõ");
		rightWay = false;
		gate = false;
		goBackward = false;
		enemyGate = false;

		do {
			if (data.getCompassData() <= data.getStartingPosition() + 3.0
					&& data.getCompassData() >= data.getStartingPosition() - 3.0) {
				data.getLeftMotor().stop();
				data.getRightMotor().stop();
				rightWay = true;
			} else if (data.getCompassData() > data.getStartingPosition()) {
				data.getLeftMotor().setSpeed(200);
				data.getLeftMotor().rotate(720, true);
			} else if (data.getCompassData() < data.getStartingPosition()) {
				data.getRightMotor().setSpeed(200);
				data.getRightMotor().rotate(720, true);
			}
		} while (!rightWay);

		data.getLeftMotor().rotate(degrees * 2, true);
		data.getRightMotor().rotate(-degrees * 2);
		data.getLeftMotor().stop(true);

		do {

			if (data.getDistanceData() <= 0.3) {
				data.getLeftMotor().stop();
				data.getRightMotor().stop();
				gate = true;
			} else if (data.getDistanceData() > 0.3) {
				data.getLeftMotor().setSpeed(400);
				data.getRightMotor().setSpeed(400);
				data.getLeftMotor().forward();
				data.getRightMotor().forward();
			}
		} while (!gate);

		data.getLeftMotor().rotate(degrees, true);
		data.getRightMotor().rotate(-degrees);
		data.getLeftMotor().stop(true);

		do {
			if (data.getDistanceData() <= (data.getStartingDistance() + 0.15f)
					&& data.getDistanceData() >= (data.getStartingDistance() - 0.15f)) {
				data.getLeftMotor().stop();
				data.getRightMotor().stop();
				data.getLeftMotor().rotate(degrees, true);
				data.getRightMotor().rotate(-degrees);
				data.getLeftMotor().stop(true);
				goBackward = true;
			}
			else if (data.getDistanceData() < data.getStartingDistance()-0.15) {
				data.getLeftMotor().stop();
				data.getRightMotor().stop();
				data.getLeftMotor().rotate(degrees * 2, true);
				data.getRightMotor().rotate(-degrees * 2);
				data.getLeftMotor().stop(true);
				data.getRightMotor().forward();
				data.getLeftMotor().forward();
				if (data.getDistanceData() <= (data.getStartingDistance() + 0.15f)
						&& data.getDistanceData() >= (data.getStartingDistance() - 0.15f)) {
					data.getLeftMotor().stop();
					data.getRightMotor().stop();
					data.getLeftMotor().rotate(-degrees, true);
					data.getRightMotor().rotate(degrees);
					data.getLeftMotor().stop(true);
					goBackward = true;
				}
			} else if (data.getDistanceData() > data.getStartingDistance()+0.15) {
				data.getLeftMotor().forward();
				data.getRightMotor().forward();
			} 

		} while (!goBackward);

		do {
			if (data.getCompassData() <= data.getStartingPosition() + 3.0
					&& data.getCompassData() >= data.getStartingPosition() - 3.0) {
				data.getLeftMotor().stop();
				data.getRightMotor().stop();
				enemyGate = true;
			} else if (data.getCompassData() > data.getStartingPosition()) {
				data.getLeftMotor().setSpeed(200);
				data.getLeftMotor().rotate(720, true);
			} else if (data.getCompassData() < data.getStartingPosition()) {
				data.getRightMotor().setSpeed(200);
				data.getRightMotor().rotate(720, true);
			}
		} while (!enemyGate);

		do {
			data.getBallMotor().rotate(-1);
			data.getBallMotor().rotate(1);
		} while (data.getBallData() > 0.1f);

	}

	public Deffensive(Data globalData) {
		data = globalData;
	}
}
