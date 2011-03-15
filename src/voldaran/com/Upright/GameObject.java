package voldaran.com.Upright;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class GameObject {
	public static ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	public static Vec2d offset;
	public final  int NODIR = 0; 
	public final static int RIGHT = 1; 
	public final static int UP = 2; 
	public final static int TOP = 2; 
	public final static int LEFT = 3; 
	public final static int DOWN = 4; 
	public final static int BOTTOM = 4; 
	
	public static void drawPause(Canvas c, Vec2d cameraSize) {
		for (GameObject o : GameObject.gameObjects){
			o.drawP(c);
		}
		
		Paint whitePaint = new Paint();
		whitePaint.setColor(Color.BLACK);
		whitePaint.setAlpha((int) 185);
		c.drawRect(0, 0, cameraSize.x, cameraSize.y, whitePaint);
	}

	public static void drawAllPreview(Canvas c){  //TODO Not entirely needed anymore
		for(GameObject o : GameObject.gameObjects){
				o.drawPreview(c);
		}
	}
	
	public static void drawAll(Canvas c){  //TODO Not entirely needed anymore
		for(GameObject o : GameObject.gameObjects){
			if(o.onScreen(c))
				o.draw(c);
		}
	}
	
	public static boolean returnWin() {
		boolean exit=true;
		for(GameObject o : GameObject.gameObjects){
			exit = o.checkWin();
			if (exit==false) break;
		}
		
		return exit;
	}
	
	public static boolean saveCheckpointAll() {
		boolean checkpointOK = true;
		
		Iterator<GameObject> iter = GameObject.gameObjects.iterator();
		while(checkpointOK && iter.hasNext()){
			checkpointOK = checkpointOK && iter.next().allowCheckpoint(); 
		}
		if(checkpointOK){
			Log.d("CP", "Saved!");
			for(GameObject o : GameObject.gameObjects){
				o.saveCheckpoint();
			}
		}
		return checkpointOK;
	}
	
	public static void restoreCheckpointAll() {
		for(GameObject o : GameObject.gameObjects){
			o.restoreCheckpoint();
		}
	}
	
	public static void toFile(BufferedWriter writer) throws IOException{
		for(GameObject o : GameObject.gameObjects){
			writer.write(o.toString());
			writer.newLine();
		}
	}
	
	public Vec2d pos;
	public Vec2d extent;
	public Vec2d velocity;
	
	
	public long left;
	public long top;
	public long right;
	public long bottom;
	
	public boolean obstacle = false;
	
	public int color;
	
	public GameObject ground = null;
	
	public GameObject (Vec2d pos, Vec2d extent) {
		this(pos, extent, new Vec2d(0,0));
	}
	
	public GameObject(Vec2d pos, Vec2d extent, Vec2d velocity){
		this.pos = pos;
		this.extent = extent;
		setSides();
		
		this.velocity = velocity;
		color = Color.WHITE;

		GameObject.gameObjects.add(this);
		
	}
	
	public String toString(){
		return "GameObject: pos: " + pos + " extent: " + extent;
	}
	
	protected void grounding(GameObject ground){
		this.ground = ground;
	}
	
	public void setSides(){
		left = pos.x - extent.x;
		top = pos.y - extent.y;
		right = pos.x + extent.x;
		bottom = pos.y + extent.y;
	}
	
	// touching - tests is this object's side is touching GameObject o
	// 	in: side - right = 1, top = 2, left = 3, bottom = 4
	//      o - any GameObject
	//  returns: True - if this objects side touches o
	//  calls: overlap
	public boolean touching(int side, GameObject o){
        if(side == RIGHT)
            return right == o.left && overlap(o, false);
        else if(side == TOP)
            return top == o.bottom && overlap(o, true);
        else if(side == LEFT)
            return left == o.right && overlap(o, false);
        else
            return bottom == o.top && overlap(o, true);
	
	}
	
	// overlap - tests if the project of the horizontal or vertical extent of this object and given object overlap
	// in: o - object to test against
	//     horizontal - true = horizontal extent, false = vertical extent
	// returns: True if there is overlap
	//          False if not
	public boolean overlap(GameObject o, boolean horizontal){
        if(horizontal)
            return right > o.left && left < o.right;
        else
        	return bottom > o.top && top < o.bottom;
	}
	
	public void update(){
	}
	
	public void touch(GameObject o){
	}
	
	public void toggle(GameObject o){
	}
		
	private boolean onScreen(Canvas c){
		return (right - GameObject.offset.x) >= 0 
			&& (left - GameObject.offset.x) <= c.getWidth() * 1000 
			&& (bottom - GameObject.offset.y) >= 0 
			&& (top - GameObject.offset.y) <= c.getHeight() * 1000;
	}
	
	public boolean checkWin() {
		return true;
	}
	
	public void drawP(Canvas c){
	}
	
	public void drawPreview(Canvas c){
	}
	
	public void draw(Canvas c){
		
		Rect recObject = new Rect((int)((left - GameObject.offset.x) / 1000), 
				                  (int)((top - GameObject.offset.y) / 1000), 
				                  (int)((right - GameObject.offset.x)/ 1000), 
		 		                 (int)((bottom - GameObject.offset.y) / 1000));

		Paint paintObject = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		paintObject.setColor(color);
		c.drawRect(recObject, paintObject);
	}
	
	protected boolean allowCheckpoint(){
		return true;
	}
	
	protected void saveCheckpoint(){
	}
	
	protected void restoreCheckpoint(){
	}
}
