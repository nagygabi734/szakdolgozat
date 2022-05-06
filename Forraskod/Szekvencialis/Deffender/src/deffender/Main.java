package deffender;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class Main {

	public static void main(String[] args) {

		LCD.drawString("If you are ready press Enter!", 0, 0);
		Button.ENTER.waitForPressAndRelease();
		LCD.clearDisplay();

		Data data = new Data();

		Deffender def = new Deffender(data);
		def.deff();
	}

}
