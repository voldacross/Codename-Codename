package voldaran.com.Upright;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class GameHero extends MovingObject{
	public final static Bitmap bitmap = Game.loadBitmapAsset("meatwad.png");
	private final static int WALKSPEED = 4000;
	private final static Vec2d Velocities[] = {new Vec2d(0,0),
											   new Vec2d(WALKSPEED, 0),
											   new Vec2d(0, -WALKSPEED),
											   new Vec2d(-WALKSPEED, 0),
											   new Vec2d(0, WALKSPEED)};
	public static GameHero hero = null;

	public static GameHero fromString(String objectData){
		String data[] = objectData.split(",");
		Vec2d pos = new Vec2d(Integer.parseInt(data[0]), Integer.parseInt(data[1])).mul(1000);
		int direction = -DOWN;
		if(data.length > 2)
			direction = Integer.parseInt(data[2]);
		return createHero(pos, direction);
	}
	
	public static GameHero createHero(Vec2d pos, int direction){
		hero = new GameHero(pos, direction);
		return hero;
	}
	
	private int toggleCount = 0;
	private int previousToggleCount = 0;
	protected int lastDirection = NODIR;
	protected int lastDirectionCheckpoint = NODIR;
	public boolean dead = false;

	private GameHero(Vec2d pos, int direction) {
		super(pos, new Vec2d(bitmap.getWidth() / 6 * 1000,bitmap.getHeight() / 6 * 1000), new Vec2d(0,0));
		lastDirection = Math.abs(direction);
		if(direction < 0) applyForce(Velocities[lastDirection]);
	}
	
	@Override
	public String toString(){
		return "Hero: pos: " + pos + " lastDirection: " + lastDirection;
	}

	@Override
	protected void grounding(GameObject o){
   		if(o instanceof Wall){
   			((Wall)o).toggle(this);
    		switch(lastDirection){
    		case RIGHT:
    			pos.x = o.right + extent.x;
    			break;
    		case UP:
    			pos.y = o.top - extent.y;
    			break;
    		case LEFT:
    			pos.x = o.left - extent.x;
    			break;
    		case DOWN:
    			pos.y = o.bottom + extent.y;
    			break;
    		}
    		GameObject.saveCheckpointAll();
    	}
	}
	
	public int getToggleCount () {
		return toggleCount;
	}
	
	public void processInput(UserInput.Input input){
		if(velocity.isZero() && input != UserInput.Input.NONE){
			switch(input){
			case PRESS_RIGHT:
				if(lastDirection != LEFT){
					applyForce(Velocities[RIGHT]);
					lastDirection = RIGHT;
				}
				break;
			case PRESS_UP:
				if(lastDirection != DOWN){
					applyForce(Velocities[UP]);
					lastDirection = UP;
				}
				break;
			case PRESS_LEFT:
				if(lastDirection != RIGHT){
					applyForce(Velocities[LEFT]);
					lastDirection = LEFT;
				}
				break;
			case PRESS_DOWN:
				if(lastDirection != UP){
					applyForce(Velocities[DOWN]);
					lastDirection = DOWN;
				}
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
		c.drawBitmap(bitmap, null, recHero, null);
	}
	
	@Override
	protected boolean allowCheckpoint(){
		return velocity.isZero();
	}
	
	@Override
	protected void saveCheckpoint(){
		super.saveCheckpoint();
		Log.d("GSTA", this.toString());
		lastDirectionCheckpoint = lastDirection;
		previousToggleCount = toggleCount; 
	}
	
	@Override
	protected void restoreCheckpoint(){
		super.restoreCheckpoint();
		lastDirection = lastDirectionCheckpoint;
		dead = false;
		toggleCount = previousToggleCount;
	}
}
