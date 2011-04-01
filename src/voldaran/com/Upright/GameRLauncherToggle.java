package voldaran.com.Upright;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameRLauncherToggle extends GameObject{
	public boolean lastActive = true;
	public boolean active = true;
	public GameRLauncher parent = null; 
	
	public GameRLauncherToggle(Vec2d pos, Vec2d extent, GameRLauncher p) {
		super(pos, extent);
		color = Color.CYAN;
		parent = p;
		solid = false;
	}
	
	public void hit() {
		if (active) 
			parent.toggle(this);
	}
	

	@Override
	protected void saveCheckpoint(){
		lastActive = active;
	}
	
	@Override
	protected void restoreCheckpoint(){
		active = lastActive;
	}
	
	@Override
	public void draw(Canvas c, float interpolation) {
		if (active) color = Color.CYAN; else color = Color.GRAY;
		
		Rect recObject = new Rect((int)((left - GameObject.offset.x) / 1000), 
                (int)((top - GameObject.offset.y) / 1000), 
                (int)((right - GameObject.offset.x)/ 1000), 
                (int)((bottom - GameObject.offset.y) / 1000));
		Paint paintObject = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		paintObject.setColor(color);
		c.drawRect(recObject, paintObject);
	}
	
	

}
