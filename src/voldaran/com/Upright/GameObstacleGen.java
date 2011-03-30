package voldaran.com.Upright;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class GameObstacleGen extends GameObject{
	public static ArrayList<GameObstacleGen> gameLasers = new ArrayList<GameObstacleGen>();
	private Vec2d startPOS = new Vec2d();
	private int direction = 0; 
	
	public static void adjustLasers() {
		for (GameObstacleGen l: GameObstacleGen.gameLasers) {
			l.adjustLaser();
		}
	}
	
	public static GameObstacleGen fromString(String objectData){
		String data[] = objectData.split(",");
		Vec2d pos = new Vec2d(Integer.parseInt(data[0]), Integer.parseInt(data[1])).mul(1000);
		int d = Integer.parseInt(data[2]);
		return new GameObstacleGen(pos, d);
	}
	
	public GameObstacleGen(Vec2d pos, int d) {
		super(pos, new Vec2d(8000,8000));
		startPOS = new Vec2d(pos);
		direction = d;
		adjustLaser();
		color = Color.RED;
		gameLasers.add(this);
	}

	@Override
	public String toString(){
		return "GameObstacleGen: pos: " + pos + " extent: " + extent;
	}
	
	public void adjustLaser() {
		long lowest = 10000000;
		
		switch (direction) {
		case 0: //RIGHT
			pos.x = (startPOS.x + Game.cameraSize.x * 1000) / 2;
			extent.x = ((Game.cameraSize.x * 1000) - startPOS.x) / 2;
			setSides();
			for (GameObject o: GameObject.gameObjects) {
				if ((o instanceof Wall) && (o.color==((Wall)o).ON)) {
					if (overlaps(o)) {
						if (o.pos.x<=lowest) lowest = o.pos.x; 
					}
				}
			}
			
			pos.x = (startPOS.x + lowest - 8000) / 2;
			extent.x = (lowest - startPOS.x) / 2;
			setSides();
			break;
		case 1:
			pos.y = (startPOS.y + Game.cameraSize.y * 1000) / 2;
			extent.y = ((Game.cameraSize.y * 1000) - startPOS.y) / 2;
			setSides();
			
			for (GameObject o: GameObject.gameObjects) {
				if ((o instanceof Wall) && (o.color==((Wall)o).ON)) {
					if (overlaps(o)) {
						if (o.pos.y<=lowest) lowest = o.pos.y; 
					}
				}
			}
			
			pos.y = (startPOS.y + lowest - 8000) / 2;
			extent.y = (lowest - startPOS.y) / 2;
			setSides();
			break;
		case 2:
			lowest=0;
			pos.x = (startPOS.x - (Game.cameraSize.x * 1000)) / 2;
			extent.x = ((Game.cameraSize.x * 1000) + startPOS.x) / 2;
			setSides();
			for (GameObject o: GameObject.gameObjects) {
				if ((o instanceof Wall) && (o.color==((Wall)o).ON)) {
					if (overlaps(o)) {
						if (o.pos.x>=lowest) lowest = o.pos.x; 
					}
				}
			}
			
			pos.x = (startPOS.x + lowest + 8000) / 2;
			extent.x = (startPOS.x - lowest) / 2;
			setSides();
			break;
		case 3:
			lowest=0;
			pos.y = (startPOS.y - (Game.cameraSize.y * 1000)) / 2;
			extent.y = ((Game.cameraSize.y * 1000) + startPOS.y) / 2;
			setSides();
			for (GameObject o: GameObject.gameObjects) {
				if ((o instanceof Wall) && (o.color==((Wall)o).ON)) {
					if (overlaps(o)) {
						if (o.pos.y>=lowest) lowest = o.pos.y; 
					}
				}
			}
			pos.y = (startPOS.y + lowest + 8000) / 2;
			extent.y = (startPOS.y - lowest) / 2;

			setSides();
			break;
		}

		setSides();
	}
	
	protected boolean overlaps(GameObject b){
		return this.right > b.left && this.left < b.right && this.bottom > b.top && this.top < b.bottom;
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
		
		Bitmap laserUp = Game.loadBitmapAsset("laser.GIF");
		Matrix mtx = new Matrix();
		
		mtx.postRotate(90);
		Bitmap laserRight = Bitmap.createBitmap(laserUp, 0, 0, laserUp.getWidth(), laserUp.getHeight(), mtx, true);
		mtx.postRotate(90);
		Bitmap laserDown = Bitmap.createBitmap(laserUp, 0, 0, laserUp.getWidth(), laserUp.getHeight(), mtx, true);
		mtx.postRotate(90);
		Bitmap laserLeft = Bitmap.createBitmap(laserUp, 0, 0, laserUp.getWidth(), laserUp.getHeight(), mtx, true);
		
		switch (direction) {
		case 0:
			c.drawBitmap(laserRight, left / 1000 , bottom / 1000 , null);
			break;
		case 1:
			c.drawBitmap(laserDown, left / 1000 , top / 1000 , null);
			break;
		case 2:
			c.drawBitmap(laserLeft, right / 1000 , bottom / 1000 , null);
			break;
		case 3:
			c.drawBitmap(laserUp, left / 1000, bottom / 1000 , null);
			break;
			
		
		}
		
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
