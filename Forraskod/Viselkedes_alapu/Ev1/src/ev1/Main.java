package ev1;

import data.Data;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import roles.Attack;
import roles.Closer;
import roles.Deffensive;
import roles.RobotStarting;

public class Main {

	public static void main(String[] args) {

		LCD.drawString("If you are ready press Enter!", 0, 0);
		Button.ENTER.waitForPressAndRelease();
		LCD.clearDisplay();

		Data data = new Data();

		Behavior b1 = new Deffensive(data);
		Behavior b2 = new Attack(data);
		Behavior b3 = new RobotStarting(data);
		Behavior b4 = new Closer();
		Behavior[] bArray = { b1, b2, b3, b4 };
		Arbitrator arby = new Arbitrator(bArray);
		arby.go();

	}

}
