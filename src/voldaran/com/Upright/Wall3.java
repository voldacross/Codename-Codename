package voldaran.com.Upright;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Wall3 extends Wall {
	
	public int DIM = Color.rgb(255, 239, 132);
	int tempColor = Color.MAGENTA;
	
	public static Wall3 fromString(String objectData){
		String data[] = objectData.split(",");
		Vec2d pos = new Vec2d(Integer.parseInt(data[0]), Integer.parseInt(data[1])).mul(1000);
		Vec2d extent = new Vec2d(Integer.parseInt(data[2]), Integer.parseInt(data[3])).mul(1000);
		boolean lit = (data.length > 4 && Integer.parseInt(data[4]) == 1 );
		return new Wall3(pos, extent, lit);
	}

	protected int colorCheckpoint;
	
	public Wall3(Vec2d pos, Vec2d extent, boolean lit) {
		this(pos, extent, new Vec2d(0,0), lit);
	}

	public Wall3(Vec2d pos, Vec2d extent, Vec2d velocity, boolean lit) {
		super(pos, extent, lit);
		OFF = Color.rgb(199, 199, 199);
		ON = Color.rgb(255, 222, 0);
		updateColors(lit);
	}
	
	@Override
	public String toString(){
		int lit = (color == ON) ? 1:0;
		return "Wall: pos: " + pos + " extent: " + extent + " lit: " + lit;
	}

	@Override
	public void toggle(){
		if (color == OFF) { 
			color = DIM;
		} else if (color == DIM){
			color = ON;
			
		} else if (color == ON) {
			color = OFF;
		}
	}
	
	@Override
	public void draw(Canvas c){
		if (color == DIM){
			if (tempColor==DIM) tempColor = OFF; else tempColor=DIM;
		} else tempColor = color;
			Rect recObject = new Rect((int)((left - GameObject.offset.x) / 1000), 
				                  (int)((top - GameObject.offset.y) / 1000), 
				                  (int)((right - GameObject.offset.x)/ 1000), 
		 		                 (int)((bottom - GameObject.offset.y) / 1000));

		Paint paintObject = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		paintObject.setColor(tempColor);
		c.drawRect(recObject, paintObject);
	}
}
