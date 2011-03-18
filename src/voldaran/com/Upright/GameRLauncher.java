package voldaran.com.Upright;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class GameRLauncher extends GameObject{

	public int direction = 3;
	private int lastDirection = 3;
	private Bitmap bitmap;
	
	private ArrayList<GameRLauncherToggle> launchToggles = new ArrayList<GameRLauncherToggle>();
	private ArrayList<GameRLauncherToggle> launchActiveToggles = new ArrayList<GameRLauncherToggle>();
	
	
	public static GameRLauncher fromString(String objectData){
		String data[] = objectData.split(",");
		Vec2d pos = new Vec2d(Integer.parseInt(data[0]), Integer.parseInt(data[1])).mul(1000);
		Vec2d extent = new Vec2d(Integer.parseInt(data[2]), Integer.parseInt(data[3])).mul(1000);
		return new GameRLauncher(pos, extent);
	}
	
	public void addToggle(Vec2d pos) {
		GameRLauncherToggle o = new GameRLauncherToggle(pos, new Vec2d(20000,20000), this);
		launchToggles.add(o);
		launchActiveToggles.add(o);
	}
	
	public void toggle(GameRLauncherToggle toggle) {
		launchActiveToggles.clear();
		launchActiveToggles.addAll(launchToggles);
		launchActiveToggles.remove(toggle);
		for(GameRLauncherToggle o : launchToggles){
			o.active = true;
		}
		toggle.active = false;
		this.hit();
		
	}
	
	public GameRLauncher(Vec2d pos, Vec2d extent) {
		super(pos, extent);
		color = Color.BLUE;
		bitmap = Game.loadBitmapAsset("rlaunch.png");
	}
	
	public void hit() {
		direction++;
		if (direction>=4) direction = 0;
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
