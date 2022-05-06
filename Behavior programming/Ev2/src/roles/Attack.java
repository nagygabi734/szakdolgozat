package roles;

import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.robotics.subsumption.Behavior;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import data.Data;
import lejos.hardware.Bluetooth;
import lejos.utility.Delay;

public class Attack implements Behavior {

	private boolean searchBall;
	private boolean rightWay;
	private boolean goForward;
	private boolean goal;
	private boolean connect;
	private boolean isFirstConnect = true;
	private ScheduledExecutorService scheduledExecutorService;
	private ScheduledFuture<?> scheduleConnector;
	private ScheduledFuture<?> scheduleConn;
	private Thread attackThread;
	private int degrees;
	private Data data;

	public boolean takeControl() {

		if (data.getChanger() != 1) {
			return true;
		} else if (data.getChanger() == 1) {
			return false;
		} else {
			return true;
		}

	}

	public void suppress() {
		data.getLeftMotor().stop();
		data.getRightMotor().stop();
	}

	public void action() {
		degrees = (int) Math.round(((13.2 * Math.PI) / (5.6 * Math.PI)) * 90.0);
		System.out.println("Támadó");
		System.out.println("kezdõ adatok:" + data.getStartingDistance() + data.getStartingPosition());
		searchBall = false;
		rightWay = false;
		goForward = false;
		goal = false;
		connect = false;
		
		scheduledExecutorService = Executors.newScheduledThreadPool(2);
		attackThread = Thread.currentThread();
		
		data.getLeftMotor().setSpeed(400);
		data.getRightMotor().setSpeed(400);

		if (isFirstConnect == true && data.getBallData() > 0.1) {
			int time = 2;
			scheduleConnector = scheduledExecutorService.schedule(connector, time, TimeUnit.SECONDS);
		} else if (isFirstConnect == false && data.getBallData() > 0.1) {
			int time = 0;
			scheduleConnector = scheduledExecutorService.schedule(connector, time, TimeUnit.SECONDS);
		}

		do {
			if (data.getChanger() == 1) {
				searchBall = true;
				rightWay = true;
				goForward = true;
				goal = true;
				connect = true;
			}else if (data.getIrData() == 0.0f) {
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
				Delay.msDelay(65);// 63
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
		
		if (connect == false) {

			scheduledExecutorService.schedule(new Runnable() {
				public void run() {
					scheduleConnector.cancel(true);
				}
			}, 0, TimeUnit.SECONDS);
			Delay.msDelay(50);

			scheduleConn = scheduledExecutorService.schedule(conn, 0, TimeUnit.SECONDS);

		}

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

		if (data.getChanger() == 1) {
			rightWay = true;
		} else {
			rightWay = false;
		}
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

	final Runnable conn = new Runnable() {
		@SuppressWarnings({ "removal" })
		@Override
		public void run() {
			attackThread.suspend();
			data.getLeftMotor().stop(true);
			data.getRightMotor().stop();
			NXTCommConnector connector = Bluetooth.getNXTCommConnector();
			System.out.println("Connecting to " + "EV1");
			NXTConnection connection = connector.connect("EV1", NXTConnection.RAW);
			if (connection == null) {
				System.err.println("Failed to connect");
				return;
			}
			System.out.println("Connected");

			DataInputStream input = connection.openDataInputStream();
			DataOutputStream output = connection.openDataOutputStream();

			System.out.println("Sending data");

			try {
				output.writeInt(1);
				output.flush();
				System.out.println("All data sent");
				Delay.msDelay(1000);
				output.close();
				input.close();
				connection.close();
				System.out.println("Connection closed");
			} catch (Exception e) {
				System.out.println("nincs elkuldve");
				e.printStackTrace();
			}
			connect = true;
			isFirstConnect = false;
			attackThread.resume();

		}
	};

	final Runnable connector = new Runnable() {
		@SuppressWarnings({ "removal" })
		@Override
		public void run() {
			attackThread.suspend();
			data.getRightMotor().stop(true);
			data.getLeftMotor().stop();
			NXTCommConnector connector = Bluetooth.getNXTCommConnector();
			System.out.println("Waiting for connection ...");
			NXTConnection con = connector.waitForConnection(0, NXTConnection.RAW);
			System.out.println("Connected");
			DataInputStream dis = con.openDataInputStream();
			DataOutputStream dos = con.openDataOutputStream();

			try {
				data.setChanger(dis.readInt());
				System.out.println("Read " + data.getChanger());
				dos.writeInt(-data.getChanger());
				dos.flush();
				dis.close();
				dos.close();
				con.close();
			} catch (IOException e) {
				System.out.println("Doesn't work");
				e.printStackTrace();
			}
			connect = true;
			isFirstConnect = false;
			attackThread.resume();
		}
	};

	public Attack(Data globalData) {
		data = globalData;
	}

}