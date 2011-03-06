package voldaran.com.Upright;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class GameHero extends MovingObject{
	public final static Bitmap bitmap = Game.loadBitmapAsset("meatwad.png");
	private final static int WALKSPEED = 4000;
	private final static Vec2d LEFTVELOCITY = new Vec2d(-WALKSPEED, 0);
	private final static Vec2d RIGHTVELOCITY = new Vec2d(WALKSPEED, 0);
	private final static Vec2d UPVELOCITY = new Vec2d(0, -WALKSPEED);
	private final static Vec2d DOWNVELOCITY = new Vec2d(0, WALKSPEED);

	public static GameHero fromString(String objectData){
		String data[] = objectData.split(",");
		Vec2d pos = new Vec2d(Integer.parseInt(data[0]), Integer.parseInt(data[1])).mul(1000);
		return new GameHero(pos);
	}
	
	//Gravity moved to GameObject for testing
	
	protected GameObject groundCheckpoint = null;
	protected GameObject lastToggled = null; 
	protected GameObject lastToggledCheckpoint = null; 
	public boolean dead = false;

	public GameHero(Vec2d pos) {
		super(pos, new Vec2d(bitmap.getWidth() / 6 * 1000,bitmap.getHeight() / 6 * 1000), new Vec2d(0,0));
		applyForce(DOWNVELOCITY);
	}
	
	@Override
	public String toString(){
		return "Hero: pos: " + pos;
	}

	protected int findGround(){
		if(touching(RIGHT, ground))
			return RIGHT;
		else if(touching(UP, ground))
			return UP;
		else if(touching(LEFT, ground))
			return LEFT;
		else if(touching(DOWN, ground))
			return DOWN;
		else
			return NODIR;
	}
	
	@Override
	protected void grounding(GameObject newGround){
		ground = newGround;
    	if(ground != lastToggled){
    		lastToggled = ground;
    		toggleCount +=1;
    		ground.toggle(this);
    		switch(findGround()){
    		case RIGHT:
    			pos.x = ground.right + extent.x;
    			break;
    		case UP:
    			pos.y = ground.top - extent.y;
    			break;
    		case LEFT:
    			pos.x = ground.left - extent.x;
    			break;
    		case DOWN:
    			pos.y = ground.bottom + extent.y;
    			break;
    		}
    		GameObject.saveCheckpointAll();
    	}
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
				applyForce(RIGHTVELOCITY);
				ground = null;
				break;
			case PRESS_LEFT:
				applyForce(LEFTVELOCITY);
				ground = null;
				break;
			case PRESS_UP:
				applyForce(UPVELOCITY);
				ground = null;
				break;
			case PRESS_DOWN:
				applyForce(DOWNVELOCITY);
				ground = null;
				break;
			}
		}
	}
	

	@Override
	public void touch(GameObject o){
		dead = o instanceof GameObstacle;
		if(!dead){
			grounding(o);
		}
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
		Log.d("GSTA", "" + pos.toString());
		groundCheckpoint = ground;
		lastToggledCheckpoint = lastToggled;
		previousToggleCount = toggleCount; 
	}
	
	@Override
	protected void restoreCheckpoint(){
		super.restoreCheckpoint();
		ground = groundCheckpoint;
		lastToggled = lastToggledCheckpoint;
		dead = false;
		toggleCount = previousToggleCount;
	}
}
