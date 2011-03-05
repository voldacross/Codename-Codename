package voldaran.com.Upright;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Wall extends GameObject {
	protected int colorCheckpoint;
	
	public Wall(Vec2d pos, Vec2d extent) {
		this(pos, extent, new Vec2d(0,0));
	}

	public Wall(Vec2d pos, Vec2d extent, Vec2d velocity) {
		super(pos, extent, velocity);
		colorCheckpoint = color;
	}

	@Override
	public void toggle(GameObject o){
		super.touch(o);
		if(o instanceof GameHero){
			if(color == Color.WHITE) color = Color.GREEN;
			else color = Color.WHITE;
		}
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
