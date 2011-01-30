package voldaran.com.Upright;

import voldaran.com.Upright.UserInput.Input;
import android.graphics.Bitmap;

public class GameHero extends GameObject{
	
	public Bitmap bitHero;
	
	public Vector2D velocity = new Vector2D(0,0);

	public GameHero(Vector2D pos, Extent extent) {
		super(pos, extent);
	}
	
	public GameHero(Vector2D v, Extent extent, Vector2D vel) {
		super(v, extent);
		velocity = vel;
	}

	
	public void update(UserInput input) {
		
		
		Input uInput = input.getInput(); 	
			
		if (uInput==Input.PRESS_RIGHT) {
		} else if (uInput==Input.PRESS_MIDDLE) {
		} else if (uInput==Input.PRESS_LEFT) {
		} else if (uInput==Input.SWIPE_UP) {
		} else if (uInput==Input.SWIPE_DOWN) {
		} else if (uInput==Input.SWIPE_RIGHT) {
		} else if (uInput==Input.SWIPE_LEFT) {
		} else if (uInput==Input.NONE) {
		}
		//super.pos.add(velocity);
		
		//Collision detection!!
	}
	
}
