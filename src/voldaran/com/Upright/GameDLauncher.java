package voldaran.com.Upright;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class GameDLauncher extends GameObject{

	public int direction = 1;
	private int lastDirection;
	private Bitmap bitmap;
	
	public GameDLauncher(Vec2d pos, Vec2d extent) {
		super(pos, extent);
		color = Color.BLUE;
		bitmap = Game.loadBitmapAsset("rlaunch.png");
	}
	
	public void hit() {
		if (direction==0) direction=1; else direction=0;
	}
	
	@Override
	public void draw(Canvas c){
		Rect recObject = new Rect((int)((left - GameObject.offset.x) / 1000), 
				                  (int)((top - GameObject.offset.y) / 1000), 
				                  (int)((right - GameObject.offset.x)/ 1000), 
		 		                 (int)((bottom - GameObject.offset.y) / 1000));

		Paint paintObject = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		paintObject.setColor(color);
//		c.drawRect(recObject, paintObject);
		
		Matrix mtx = new Matrix();
		mtx.postRotate(direction*90);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);
		    Paint text = new Paint();
  		    text.setColor(Color.WHITE);
  		    text.setTextSize(16);
  		    text.setTypeface(Typeface.MONOSPACE);
  		    
  		    c.drawText(String.valueOf(direction), pos.x / 1000, pos.y / 1000, text);
  		    c.drawBitmap(newBitmap, null, recObject, null);
  		    
	}
	@Override
	protected void saveCheckpoint(){
		lastDirection = direction;
	}
	
	@Override
	protected void restoreCheckpoint(){
		direction = lastDirection;
	}
	
	

}
