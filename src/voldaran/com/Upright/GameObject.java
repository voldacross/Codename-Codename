package voldaran.com.Upright;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameObject {
	public static ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	public static Vec2d offset;
	public static void drawAll(Canvas c){
		for(GameObject o : GameObject.gameObjects){
			o.draw(c);
		}
	}
	
	public Vec2d pos;
	public Vec2d extent;
	public Vec2d velocity;
	
	public long left;
	public long top;
	public long right;
	public long bottom;
	
	private int color;
	
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
		GameObject.gameObjects.add(this);
	}
	
	protected GameObject grounding(){
		return ground;
	}
	
	public void update(){
	}
	
	public void draw(Canvas c){
		
		Rect recWall = new Rect((int)((left - GameObject.offset.x) / 1000), 
				                (int)((top - GameObject.offset.y) / 1000), 
				                (int)((right - GameObject.offset.x)/ 1000 - 1), 
				                (int)((bottom - GameObject.offset.y) / 1000 - 1));
		
		Paint paintWall = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		paintWall.setColor(color);
		c.drawRect(recWall, paintWall);

	}
}

