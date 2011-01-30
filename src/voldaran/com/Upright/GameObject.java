package voldaran.com.Upright;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameObject {
	public static ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	public static Vec2d offset;
	public static void drawObjects(Canvas c){
		for(GameObject o : GameObject.gameObjects){
			o.draw(c);
		}
	}
	
	public Vec2d pos;
	public Vec2d extent;
	public Vec2d velocity;
	
	private int color;
	
	
	public GameObject (Vec2d pos, Vec2d extent) {
		this(pos, extent, new Vec2d(0,0));
	}
	
	public GameObject(Vec2d pos, Vec2d extent, Vec2d velocity){
		this.pos = pos;
		this.extent = extent;
		this.velocity = velocity;
		color = Color.WHITE;
		GameObject.gameObjects.add(this);
	}
	
	public long top(){
		return pos.y - extent.y;
	}
	
	public long left(){
		return pos.x - extent.x;
	}
	
	public long bottom(){
		return pos.y + extent.x;
	}
	
	public long right(){
		return pos.x + extent.x;
	}
	
	public void draw(Canvas c){
		
		Vec2d p = new Vec2d(pos).sub(GameObject.offset).div(1000);
		Vec2d e = new Vec2d(extent).div(1000);
		
		Rect recWall = new Rect((int)(p.x - e.x), (int)(p.y - e.y), (int)(p.x + e.x - 1), (int)(p.y + e.y - 1));
		
		Paint paintWall = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		paintWall.setColor(color);
		c.drawRect(recWall, paintWall);

	}
}

