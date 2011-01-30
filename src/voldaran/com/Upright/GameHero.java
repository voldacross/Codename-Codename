package voldaran.com.Upright;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameHero extends MovingObject{
	public Bitmap bitmap;

	public GameHero(Vec2d pos,Vec2d extent, Bitmap bitmap) {
		super(pos, extent);
		this.bitmap = bitmap;
	}
	
	public GameHero(Vec2d pos, Vec2d extent, Vec2d vel, Bitmap bitmap) {
		super(pos, extent, vel);
		this.bitmap = bitmap;
	}
	
	public void update() {
		pos.add(velocity);
	}
	
	public void draw(Canvas c){
		c.drawBitmap(bitmap, (left() - GameObject.offset.x) / 1000, (top() - GameObject.offset.y) / 1000, null);
	}
}
