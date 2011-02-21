package voldaran.com.Upright;

import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Rect;

public class PauseMenu {
	
	//Needs to be updated when we start using a board size, right now board size is set to the screen size
	
	private Picture picture;
	private Rect dst;

	private Vec2d hero, offset, mapSize, cameraSize;
	
	
	public boolean exiting = false;
	private double zoomMagnify = 1;
	
	public PauseMenu(Vec2d c) {
		offset = new Vec2d(0,0);
		cameraSize = c;
	}
	
	public void createPause(Picture p, Vec2d h, Vec2d m) {
		mapSize = new Vec2d(m);
		GameObject.drawPause(p.beginRecording((int)mapSize.x, (int)mapSize.y));
		
		p.endRecording();
		picture = p;
		dst = new Rect();
		hero = h;
		zoomMagnify = 1;
		exiting = false;
		
		//Center screen on hero
		centerScreen(zoomMagnify, h);
	}
	
	
	//Centers Screen on the hero
	public void centerScreen(double magnify, Vec2d centerPoint) {
		
		int pictureCenterX = (int) (mapSize.x / (2 * magnify)); //200
		int pictureCenterY = (int) (mapSize.y / (2 * magnify)); //120
		
		int canvasCenterX=  ((int) cameraSize.x / 2); //400  //Map Center
		int canvasCenterY = ((int) cameraSize.y / 2); //240
		
		offset.x = (int) (pictureCenterX - ((centerPoint.x/1000)/magnify)) + canvasCenterX;
		offset.y = (int) (pictureCenterY - ((centerPoint.y/1000)/magnify)) + canvasCenterY;
	}

	
	public void Update(Vec2d velocity) {
		if (!exiting) //if paused...
			if (zoomedIn()) //ZoomIN . returns True if zoomedIN, else adjusts zoom level 
			{
				UpdateInput(velocity);
				UpdateRect(2);
			}

	}
	

	
	public boolean zoomedIn() {
		if (zoomMagnify>=2) return true;
		
		zoomMagnify += 0.05;
		centerScreen(zoomMagnify, hero);
		UpdateRect(zoomMagnify);

		return false;
	}
	
	
	
	public boolean zoomOut() {
		if (zoomMagnify<=1) return true;
		
		zoomMagnify -= 0.05;
		centerScreen(zoomMagnify, hero);
		UpdateRect(zoomMagnify);
		
		return false;
		
	}
	public void UpdateInput(Vec2d velocity) {
		offset.add(velocity);
	}
	
	
	public void UpdateRect(double magnify) {
		int pictureWidth = (int) (mapSize.x / 2); 
		int pictureHeight = (int) (mapSize.y / 2);

		dst = new Rect( (int) (offset.x - (pictureWidth / magnify)),
				(int) (offset.y - (pictureHeight / magnify)),
				(int) (offset.x + (pictureWidth / magnify)),
				(int) (offset.y + (pictureHeight / magnify)));	
	}
	
	public void Draw(Canvas c) {
		c.drawPicture(picture, dst);
	}

}
