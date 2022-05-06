package roles;

import lejos.hardware.Button;
import lejos.robotics.subsumption.Behavior;

public class Closer implements Behavior{


	public boolean takeControl() {
		// TODO Auto-generated method stub
		return Button.ESCAPE.isDown();
	}

	
	public void action() {
		System.out.println("Záró");
		System.exit(0);
		
	}


	public void suppress() {
		
		
	}

}
