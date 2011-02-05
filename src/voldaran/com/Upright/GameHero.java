package voldaran.com.Upright;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class GameHero extends MovingObject{
	private final static Vec2d LEFTVELOCITY = new Vec2d(-2000, 0);
	private final static Vec2d RIGHTVELOCITY = new Vec2d(2000, 0);
	private final static Vec2d JUMPLEFTVELOCITY = new Vec2d(-30000, -20000);
	private final static Vec2d JUMPRIGHTVELOCITY = new Vec2d(30000, -20000);
	
	
	public Bitmap bitmap;
	public boolean gravityDown = true;

	public GameHero(Vec2d pos,Vec2d extent, Bitmap bitmap) {
		super(pos, extent);
		this.bitmap = bitmap;
	}
	
	public GameHero(Vec2d pos, Vec2d extent, Vec2d vel, Bitmap bitmap) {
		super(pos, extent, vel);
		this.bitmap = bitmap;
	}
	
	public void processInput(UserInput.Input input){
		switch(input){
		case PRESS_LEFT: applyForce(LEFTVELOCITY); break;
		case PRESS_RIGHT: applyForce(RIGHTVELOCITY); break;
		case SWIPE_LEFT: applyForce(JUMPLEFTVELOCITY); break;
		case SWIPE_RIGHT: applyForce(JUMPRIGHTVELOCITY); break;
		case SWIPE_UP: gravityDown = false; break;
		case SWIPE_DOWN: gravityDown = true; break;
			
		
		}
		if(input != UserInput.Input.NONE) Log.d("GSTA", "" + input);
	}
	
	public GameObject getGround(){
		return null;
	}
	
	public void update() {
		super.update();
		velocity.mul(.75);
		if (gravityDown) {velocity.add(0, 10000);} else {velocity.sub(0, 10000);}
	}
	
	public void draw(Canvas c){
		c.drawBitmap(bitmap, (left - GameObject.offset.x) / 1000, (top - GameObject.offset.y) / 1000, null);
	}
}
