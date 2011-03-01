package voldaran.com.Upright;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class GameHero extends MovingObject{
	private final static int WALKSPEED = 1000;
	private final static Vec2d LEFTVELOCITY = new Vec2d(-WALKSPEED, 0);
	private final static Vec2d RIGHTVELOCITY = new Vec2d(WALKSPEED, 0);
	private final static Vec2d UPVELOCITY = new Vec2d(0, -WALKSPEED);
	private final static Vec2d DOWNVELOCITY = new Vec2d(0, WALKSPEED);
	private final static Vec2d gravity = new Vec2d(0, 300);
	private final static Vec2d curVelocity = new Vec2d(DOWNVELOCITY);
	
	
	//Gravity moved to GameObject for testing
	
	public Bitmap bitmap;
	protected GameObject groundCheckpoint = null;
	protected GameObject lastToggled = null; 
	protected GameObject lastToggledCheckpoint = null; 
	protected int walking = NODIR;
	protected int walkingCheckpoint = NODIR;
	protected int downCheckpoint = DOWN;
	public boolean dead = false;

	public GameHero(Vec2d pos,Vec2d extent, Bitmap bitmap) {
		this(pos, extent, new Vec2d(0,0), bitmap);
	}
	
	public GameHero(Vec2d pos, Vec2d extent, Vec2d vel, Bitmap bitmap) {
		super(pos, extent, vel);
		this.bitmap = bitmap;
	}
	
	// findGround - check for ground on side on this object, preferring the supplied object
	// 	in: side - right = 1, top = 2, left = 3, bottom = 4
	//      prefer - the object given preference
	//  returns: null - no ground found
	//           GameObject - the found ground
	//  calls: touching
	protected GameObject findGround(int side, GameObject prefer){
		GameObject theground = null;
		ArrayList<GameObject> grounds = new ArrayList<GameObject>();
		for(GameObject o : GameObject.gameObjects){
			if(touching(side, o)) grounds.add(o);
		}
		if(grounds.contains(prefer)) theground = prefer;
		else if(!grounds.isEmpty())	theground = grounds.get(0);
		return theground;
	}
	
	@Override
	protected GameObject grounding(){
        if(walking == NODIR){
            if(velocity.x > 0){
                ground = findGround(RIGHT, ground);
                if(ground != null) walking = RIGHT;
            }
            if(velocity.x < 0){
                ground = findGround(LEFT, ground);
                if(ground != null) walking = LEFT;
            }
            if(velocity.y > 0){
                ground = findGround(BOTTOM, ground);
                if(ground != null) walking = DOWN;
            }
            if(velocity.y < 0){
                ground = findGround(TOP, ground);
                if(ground != null) walking = UP;
            }
        }
        else{
            ground = findGround(walking, ground);
            if(ground == null) walking = NODIR;
        }
        if(ground != null){
        	if(ground != lastToggled){
        		lastToggled = ground;
        		toggleCount +=1;
        		ground.toggle(this);
        		switch(walking){
        		case RIGHT:
        			pos.x = ground.right + extent.x;
        			walking = LEFT;
        			break;
        		case UP:
        			pos.y = ground.top - extent.y;
        			walking = DOWN;
        			break;
        		case LEFT:
        			pos.x = ground.left - extent.x;
        			walking = RIGHT;
        			break;
        		case DOWN:
        			pos.y = ground.bottom + extent.y;
        			walking = UP;
        			break;
        		}
        	}
        }
        return ground;
	}
	
	private int toggleCount = 0;
	private int previousToggleCount = 0;
	
	
	public int getToggleCount () {
		
		return toggleCount;
	}
	
	public void processInput(UserInput.Input input){
		
		if (ground!=null) {
			switch(input){
			case PRESS_RIGHT:
				if (!curVelocity.equals(LEFTVELOCITY)) {
					curVelocity.set(RIGHTVELOCITY);
					ground = null;
					walking = NODIR;
				}
				
				break;
			case PRESS_LEFT:
				if (!curVelocity.equals(RIGHTVELOCITY)){
					curVelocity.set(LEFTVELOCITY);
					ground = null;
					walking = NODIR;
				}
				
				break;
			case PRESS_UP:
				if (!curVelocity.equals(DOWNVELOCITY)){
					curVelocity.set(UPVELOCITY);
					ground = null;
					walking = NODIR;
				}
				break;
			case PRESS_DOWN:
				Log.d("GSTA", "pressed down " + curVelocity.toString());
				if (!curVelocity.equals(UPVELOCITY)) {
					curVelocity.set(DOWNVELOCITY);
					ground = null;
					walking = NODIR;
				}
				break;
			}
		} else {
			applyForce(curVelocity);
		}
		

	}
	

	@Override
	public void touch(GameObject o){
		dead = o instanceof GameObstacle;
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
	
	@Override
	protected boolean allowCheckpoint(){
		return ground != null;
	}
	
	@Override
	protected void saveCheckpoint(){
		super.saveCheckpoint();
		groundCheckpoint = ground;
		walkingCheckpoint = walking;
		lastToggledCheckpoint = lastToggled;
		previousToggleCount = toggleCount; 
	}
	
	@Override
	protected void restoreCheckpoint(){
		super.restoreCheckpoint();
		ground = groundCheckpoint;
		walking = walkingCheckpoint;
		lastToggled = lastToggledCheckpoint;
		dead = false;
		toggleCount = previousToggleCount;
	}
}
