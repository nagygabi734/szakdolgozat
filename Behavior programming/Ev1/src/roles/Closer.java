package roles;

import lejos.hardware.Button;
import lejos.robotics.subsumption.Behavior;

public class Closer implements Behavior{


	public boolean takeControl() {
		// TODO Auto-generated method stub
		return Button.ESCAPE.isDown();
	}

	
	public void action() {
		// TODO Auto-generated method stub
		
	}


	public void suppress() {
		System.exit(0);
		
	}

}
