package voldaran.com.Upright;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class GameHero extends MovingObject{
	private final static Vec2d LEFTVELOCITY = new Vec2d(-2000, 0);
	private final static Vec2d RIGHTVELOCITY = new Vec2d(2000, 0);
	private final static Vec2d JUMPLEFTVELOCITY = new Vec2d(-20000, -15000);
	private final static Vec2d JUMPRIGHTVELOCITY = new Vec2d(20000, -15000);
	
	public Bitmap bitmap;
	private GameObject ground = null;

	public GameHero(Vec2d pos,Vec2d extent, Bitmap bitmap) {
		super(pos, extent);
		this.bitmap = bitmap;
	}
	
	public GameHero(Vec2d pos, Vec2d extent, Vec2d vel, Bitmap bitmap) {
		super(pos, extent, vel);
		this.bitmap = bitmap;
	}
	
	@Override
	protected GameObject grounding(){
		if(ground != null && (bottom != ground.top || right <= ground.left || left >= ground.right)) ground = null;
		for(GameObject o : GameObject.gameObjects){
			if(o != this && o != ground){
				if(bottom == o.top && right > o.left && left < o.right){
					ground = o;
					break;
				}
			}
		}
		if(ground != null) Log.d("Hero", "Ground :" + ground + " ground top " + ground.top + " ground velocity " + ground.velocity);
		return ground;
	}
	
	public void processInput(UserInput.Input input){
		switch(input){
		case PRESS_LEFT: applyForce(LEFTVELOCITY); break;
		case PRESS_RIGHT: applyForce(RIGHTVELOCITY); break;
		case SWIPE_LEFT: 
			applyForce(JUMPLEFTVELOCITY);
			ground = null;
			break;
		case SWIPE_RIGHT: 
			applyForce(JUMPRIGHTVELOCITY);
			ground = null;
			break;
		}
	}
	
	@Override
	public void update() {
		if(ground != null)
			velocity.x += ground.velocity.x;
		super.update();
		if(ground != null){
			velocity.x -= ground.velocity.x;
			velocity.mul(0.75);
		}
		else
			velocity.mul(0.95);
		velocity.add(0, 1000);
	}
	
	@Override
	public void draw(Canvas c){
		c.drawBitmap(bitmap, (left - GameObject.offset.x) / 1000, (top - GameObject.offset.y) / 1000, null);
	}
}
