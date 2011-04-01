package voldaran.com.Upright;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameObstacle extends GameObject{
	public static GameObstacle addObstacle(long x, long y, long w, long h){
		Vec2d pos = new Vec2d(x, y).mul(1000);
		Vec2d extent = new Vec2d(w, h).mul(1000);
		return new GameObstacle(pos, extent);
	}

	public GameObstacle(Vec2d pos, Vec2d extent) {
		super(pos, extent);
		color = Color.RED;
	}

	@Override
	public String toString(){
		return "GameObstacle: pos: " + pos + " extent: " + extent;
	}

	@Override
	public void drawP(Canvas c){
		Rect recObject = new Rect((int) left / 1000, (int) top  / 1000, (int) right  / 1000, (int)bottom  / 1000);
		
		GameObject.paint.setColor(color);
		c.drawRect(recObject, GameObject.paint);
	}

	@Override
	public void draw(Canvas c, float interpolation){
		GameObject.rect.left = (int)((left - GameObject.offset.x) / 1000);
		GameObject.rect.top = (int)((top - GameObject.offset.y) / 1000);
		GameObject.rect.right = (int)((right - GameObject.offset.x)/ 1000);
		GameObject.rect.bottom = (int)((bottom - GameObject.offset.y) / 1000);

		GameObject.paint.setColor(color);
		c.drawRect(GameObject.rect, GameObject.paint);
	}
	
	@Override
	public void drawPreview(Canvas c){
		GameObject.rect.left = (int)((left - GameObject.offset.x) / 1000 / 4);
		GameObject.rect.top = (int)((top - GameObject.offset.y) / 1000 / 4);
		GameObject.rect.right = (int)((right - GameObject.offset.x)/ 1000 / 4);
		GameObject.rect.bottom = (int)((bottom - GameObject.offset.y) / 1000 / 4);

		GameObject.paint.setColor(color);
		c.drawRect(GameObject.rect, GameObject.paint);
	}
}
