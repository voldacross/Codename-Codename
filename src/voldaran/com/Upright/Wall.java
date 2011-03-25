package voldaran.com.Upright;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Wall extends GameObject {
	
	public int OFF = Color.WHITE;
	public int ON = Color.GREEN;
	
	public static Wall fromString(String objectData){
		String data[] = objectData.split(",");
		Vec2d pos = new Vec2d(Integer.parseInt(data[0]), Integer.parseInt(data[1])).mul(1000);
		Vec2d extent = new Vec2d(Integer.parseInt(data[2]), Integer.parseInt(data[3])).mul(1000);
		boolean lit = (data.length > 4 && Integer.parseInt(data[4]) == 1 );
		return new Wall(pos, extent, lit);
	}

	protected int colorCheckpoint;
	
	public Wall(Vec2d pos, Vec2d extent, boolean lit) {
		this(pos, extent, new Vec2d(0,0), lit);
	}

	public Wall(Vec2d pos, Vec2d extent, Vec2d velocity, boolean lit) {
		super(pos, extent, velocity);
		updateColors(lit);
		colorCheckpoint = color;
	}
	
	@Override
	public String toString(){
		int lit = (color == ON) ? 1:0;
		return "Wall: pos: " + pos + " extent: " + extent + " lit: " + lit;
	}

	public void updateColors(boolean lit) {
		if(lit) color = ON; else color = OFF; 
		
	}
	
	@Override
	public void touch(GameObject o){
		if(o instanceof GameHero){
			toggle();
		}
	}
	
	@Override
	public void toggle(){
		if(color == OFF) color = ON;
		else color = OFF;
	}
	
	@Override
	protected void saveCheckpoint(){
		colorCheckpoint = color;
	}
	
	@Override
	protected void restoreCheckpoint(){
		color = colorCheckpoint;
	}
	@Override
	public boolean checkWin() {
		return (color==ON);
	}
	
	@Override
	public void drawP(Canvas c){
		Rect recObject = new Rect((int) left / 1000, (int) top  / 1000, (int) right  / 1000, (int)bottom  / 1000);
		Paint paintObject = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		paintObject.setColor(color);
		c.drawRect(recObject, paintObject);
	}
	@Override
	public void draw(Canvas c){
		Rect recObject = new Rect((int)((left - GameObject.offset.x) / 1000), 
				                  (int)((top - GameObject.offset.y) / 1000), 
				                  (int)((right - GameObject.offset.x)/ 1000), 
		 		                 (int)((bottom - GameObject.offset.y) / 1000));

		Paint paintObject = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		paintObject.setColor(color);
		c.drawRect(recObject, paintObject);
	}
	
	@Override
	public void drawPreview(Canvas c){
		Rect recObject = new Rect((int)((left - GameObject.offset.x) / 1000 / 4), 
				                  (int)((top - GameObject.offset.y) / 1000 / 4), 
				                  (int)((right - GameObject.offset.x)/ 1000 / 4), 
		 		                 (int)((bottom - GameObject.offset.y) / 1000 / 4));

		Paint paintObject = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		paintObject.setColor(color);
		c.drawRect(recObject, paintObject);
	}
	
}
