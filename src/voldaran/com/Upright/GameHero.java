package voldaran.com.Upright;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameHero extends MovingObject{
	private final static Vec2d leftVelocity = new Vec2d(-2666, 0);
	private final static Vec2d rightVelocity = new Vec2d(2666, 0);
	
	public Bitmap bitmap;

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
		case PRESS_LEFT: applyForce(leftVelocity); break;
		case PRESS_RIGHT: applyForce(rightVelocity); break;
		}
	}
	
	public void update() {
		super.update();
		velocity.mul(0.60);
	}
	
	public void draw(Canvas c){
		c.drawBitmap(bitmap, (left - GameObject.offset.x) / 1000, (top - GameObject.offset.y) / 1000, null);
	}
}
