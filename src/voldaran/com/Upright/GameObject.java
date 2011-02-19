package voldaran.com.Upright;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameObject {
	public static ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	public static Vec2d offset;
	public static GameHero hero;
	
	public static void drawPause(Canvas c) {
		for (GameObject o : GameObject.gameObjects){
			o.drawP(c);
		}
	}

	public static void drawAll(Canvas c){  //TODO Not entirely needed anymore
		for(GameObject o : GameObject.gameObjects){
			if(o.onScreen(c))
				o.draw(c);
		}
	}
	
	public static void saveCheckpointAll() {
		if (hero.ground!=null) {
			for(GameObject o : GameObject.gameObjects){
				o.saveCheckpoint();
			}
		}
	}
	
	public static void restoreCheckpointAll() {
		for(GameObject o : GameObject.gameObjects){
			o.restoreCheckpoint();
		}
	}
	
	public Vec2d pos, checkpointPOS;
	public Vec2d extent;
	public Vec2d velocity, checkpointVelocity;
	public int GRAVITY = 400;  //RICK: added to make swapping gravity work ORG: 500
	public int checkpointGRAVITY;
	
	
	public long left;
	public long top;
	public long right;
	public long bottom;
	
	public boolean obstacle = false;
	public boolean death = false;
	
	public int color, checkPointColor;
	
	public GameObject ground = null;
	
	public GameObject (Vec2d pos, Vec2d extent) {
		this(pos, extent, new Vec2d(0,0));
	}
	
	public GameObject(Vec2d pos, Vec2d extent, Vec2d velocity){
		this.pos = pos;
		this.extent = extent;
		left = pos.x - extent.x;
		top = pos.y - extent.y;
		right = pos.x + extent.x;
		bottom = pos.y + extent.y;
		
		this.velocity = velocity;
		color = Color.WHITE;
		
		
		checkPointColor = color;
		checkpointPOS = new Vec2d(pos);
		checkpointVelocity = new Vec2d(velocity);
		checkpointGRAVITY = GRAVITY;
		
		GameObject.gameObjects.add(this);
	}
	
	protected GameObject grounding(){
		return ground;
	}
	
	public void update(){
	}
	
	private boolean onScreen(Canvas c){
		return (right - GameObject.offset.x) >= 0 
			&& (left - GameObject.offset.x) <= c.getWidth() * 1000 
			&& (bottom - GameObject.offset.y) >= 0 
			&& (top - GameObject.offset.y) <= c.getHeight() * 1000;
	}
	
	public void drawP(Canvas c){
		Rect recObject = new Rect((int) left / 1000, (int) top  / 1000, (int) right  / 1000, (int)bottom  / 1000);
		Paint paintObject = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		paintObject.setColor(color);
		c.drawRect(recObject, paintObject);
	}
	
	public void saveCheckpoint() {
		checkPointColor = color;
		checkpointPOS.set(pos);
		checkpointVelocity.set(velocity);
		checkpointGRAVITY = GRAVITY;
		if (this.equals(GameObject.hero)) {
//			Log.d("GSTA", "saving gravity " + GRAVITY);
//			Log.d("GSTA", "saving pos " + pos.toString());
//			Log.d("GSTA", "saving velocity " + velocity.toString());
		}
	}
	
	public void restoreCheckpoint() {
		GRAVITY = checkpointGRAVITY;
		color=checkPointColor;
		pos.set(checkpointPOS);
		velocity.set(checkpointVelocity);
		
		if (this.equals(GameObject.hero)) {
//			Log.d("GSTA", "restoring Checkpoint gravity " + GRAVITY);
//			Log.d("GSTA", "restoring Checkpoint pos " + pos.toString());
//			Log.d("GSTA", "restoring Checkpoint vel " + velocity.toString());
		}
		
	}
	
	public void draw(Canvas c){
		Rect recObject = new Rect((int)((left - GameObject.offset.x) / 1000), 
				                  (int)((top - GameObject.offset.y) / 1000), 
				                  (int)((right - GameObject.offset.x)/ 1000 - 1), 
		 		                 (int)((bottom - GameObject.offset.y) / 1000 - 1));
		 
		Paint paintObject = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		paintObject.setColor(color);
		c.drawRect(recObject, paintObject);
	}
}
