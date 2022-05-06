package attacker;

import lejos.utility.Delay;

public class Attacker {

	private boolean searchBall;
	private boolean rightWay;
	private boolean goForward;
	private boolean goal;
	private int degrees;
	private Data data;

	public void attacker() {
		degrees = (int) Math.round(((11.8 * Math.PI) / (5.6 * Math.PI)) * 90.0);
		searchBall = false;
		rightWay = false;
		goForward = false;
		goal = false;
		
		data.getLeftMotor().setSpeed(400);
		data.getRightMotor().setSpeed(400);

		do {
			if (data.getIrData() == 0.0f) {
				data.getLeftMotor().forward();
				data.getRightMotor().forward();
				if (data.getBallData() < 0.1f) {
					for (int i = data.getLeftMotor().getSpeed() - 50; i >= 0; i -= 50) {
						data.getLeftMotor().setSpeed(i);
						data.getRightMotor().setSpeed(i);
						Delay.msDelay(250);
					}
					data.getLeftMotor().stop();
					data.getRightMotor().stop();
					searchBall = true;
				}

			} else if (data.getIrData() > 0.0f && data.getIrData() < 120.0f) {
				do {
					data.getRightMotor().rotate(720, true);
				} while (data.getIrData() != 0.0f);
				Delay.msDelay(65);
				data.getRightMotor().stop();
			} else if (data.getIrData() < 0.0f && data.getIrData() > -120.0f) {
				data.getRightMotor().stop();
				do {
					data.getLeftMotor().rotate(720, true);
				} while (data.getIrData() != 0.0f);
				Delay.msDelay(100);
				data.getLeftMotor().stop();
			} else {
				data.getRightMotor().rotate(720, true);
			}
		} while (!searchBall);

		data.getLeftMotor().setSpeed(150);
		data.getRightMotor().setSpeed(150);

		do {
			if (data.getCompassData() >= data.getStartingPosition() - 2.0f
					&& data.getCompassData() <= data.getStartingPosition() + 2.0f) {
				data.getLeftMotor().stop();
				data.getRightMotor().stop();
				rightWay = true;
			} else if (data.getCompassData() > data.getStartingPosition() - 2.0f) {
				data.getLeftMotor().rotate(720, true);
			} else if (data.getCompassData() < data.getStartingPosition() + 2.0f) {
				data.getRightMotor().rotate(720, true);
			}
		} while (!rightWay);
		rightWay = false;
		
		data.getLeftMotor().rotate(degrees * 2);

		while (!goForward) {
			if (data.getDistanceData() <= (data.getStartingDistance() + 0.1f)
					&& data.getDistanceData() >= (data.getStartingDistance() - 0.1f)) {
				data.getLeftMotor().stop();
				data.getRightMotor().stop();
				data.getRightMotor().setSpeed(200);
				data.getRightMotor().rotate(degrees * 2);
				goForward = true;
			} else if (data.getDistanceData() < data.getStartingDistance() - 0.1f) {
				data.getLeftMotor().stop();
				data.getRightMotor().stop();
				data.getRightMotor().rotate((degrees * 4));
				data.getRightMotor().forward();
				data.getLeftMotor().forward();
				if (data.getDistanceData() <= (data.getStartingDistance() + 0.1f)
						&& data.getDistanceData() >= (data.getStartingDistance() - 0.1f)) {
					data.getLeftMotor().stop();
					data.getRightMotor().stop();
					data.getLeftMotor().rotate(degrees * 2);
					goForward = true;
				}
			} else if (data.getDistanceData() > data.getStartingDistance() + 0.1f) {
				data.getLeftMotor().forward();
				data.getRightMotor().forward();

			}

		}
		do {
			if (data.getCompassData() >= data.getStartingPosition() - 2.0f
					&& data.getCompassData() <= data.getStartingPosition() + 2.0f) {
				data.getLeftMotor().stop();
				data.getRightMotor().stop();
				rightWay = true;
			} else if (data.getCompassData() > data.getStartingPosition() - 2.0f) {
				data.getLeftMotor().setSpeed(200);
				data.getLeftMotor().rotate(720, true);
			} else if (data.getCompassData() < data.getStartingPosition() + 2.0f) {
				data.getRightMotor().setSpeed(200);
				data.getRightMotor().rotate(720, true);
			}
		} while (!rightWay);

		while (!goal) {
			if (data.getDistanceData() <= 0.3) {
				data.getBallMotor().setSpeed(740);
				data.getBallMotor().rotate(-110);
				data.getBallMotor().rotate(110);
				data.getLeftMotor().stop(true);
				data.getRightMotor().stop();
				goal = true;

			} else if (data.getDistanceData() > 0.3) {
				data.getLeftMotor().setSpeed(400);
				data.getRightMotor().setSpeed(400);
				data.getRightMotor().forward();
				data.getLeftMotor().forward();
			}

		}

	}

	public Attacker(Data globalData) {
		data = globalData;
	}

}