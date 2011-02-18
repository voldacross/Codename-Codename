package voldaran.com.Upright;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

public class GameHero extends MovingObject{
	private final static Vec2d LEFTVELOCITY = new Vec2d(-2000, 0);
	private final static Vec2d RIGHTVELOCITY = new Vec2d(2000, 0);
	private final static Vec2d JUMPLEFTVELOCITY = new Vec2d(-7500, -5625); //20000, -15000
	private final static Vec2d JUMPRIGHTVELOCITY = new Vec2d(7500, -5625);
	
	private int GRAVITY = 400;  //RICK: added to make swapping gravity work ORG: 500
	
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
		  
		  
		
		if (GRAVITY>0) if(ground != null && (bottom != ground.top || right <= ground.left || left >= ground.right)) ground = null;		
		if (GRAVITY<0) if(ground != null && (top != ground.bottom || right <= ground.left || left >= ground.right)) ground = null;
		  
		  for(GameObject o : GameObject.gameObjects){
		    if(o != this && o != ground && right > o.left && left < o.right){
		      if((GRAVITY>0 && bottom == o.top) || (GRAVITY < 0 && top == o.bottom)){
		        ground = o;

		        if (o.color==Color.WHITE) {
		         o.color=Color.GREEN;
		        } else o.color=Color.WHITE;
		      
		        break;
		      }
		    }
		  }
		
//		if(ground != null) Log.d("Hero", "Ground :" + ground + " ground top " + ground.top + " ground velocity " + ground.velocity);
		return ground;
	}
	
	public void processInput(UserInput.Input input){
		if(ground != null)
			switch(input){
			case PRESS_LEFT: applyForce(LEFTVELOCITY); break;
			case PRESS_RIGHT: applyForce(RIGHTVELOCITY); break;
			case SWIPE_LEFT: 
				if (GRAVITY>0) applyForce(JUMPLEFTVELOCITY); else applyForce(JUMPRIGHTVELOCITY.Negative());//RICK: added/modified to make swapping gravity work
				ground = null;
				break;
			case SWIPE_RIGHT: 
				if (GRAVITY>0) applyForce(JUMPRIGHTVELOCITY); else applyForce(JUMPLEFTVELOCITY.Negative());//RICK: added/modified to make swapping gravity work
				ground = null;
				break;
			case SWIPE_UP:  //RICK: added/modified to make swapping gravity work
				GRAVITY = -300;
				ground = null;
				break;
				
			case SWIPE_DOWN:
				GRAVITY = 300;
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
			velocity.mul(0.25);
		}
		else velocity.mul(0.95); //ORG: 0.95
		
			
		velocity.add(0, GRAVITY); //RICK: added/modified to make swapping gravity work
	}
	
	@Override
	public void draw(Canvas c){
		Rect recHero = new Rect((int) ((pos.x - extent.x) / 1000),
				(int) ((pos.y - extent.y) / 1000),
				(int) ((pos.x + extent.x) / 1000),
				(int) ((pos.y + extent.y) / 1000));
//		Log.d("GSTA", "-----------------------------");
//		Log.d("GSTA", "" + ((pos.x - extent.x) / 1000));
//		Log.d("GSTA", "" + ((pos.y - extent.y) / 1000));
//		Log.d("GSTA", "" + ((pos.x + extent.x) / 1000));
//		Log.d("GSTA", "" + ((pos.y + extent.y) / 1000));
		
//		Log.d("GSTA", "POS " + pos.toString() + " EXT " + extent.toString() + " HERO " + " bitmap " + bitmap.getWidth() + "," + bitmap.getHeight());
		c.drawBitmap(bitmap, null, recHero, null);
//		c.drawBitmap(bitmap, (left - GameObject.offset.x) / 1000, (top - GameObject.offset.y) / 1000, null);
	}
}
