package voldaran.com.Upright;

import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceView;

public class PauseMenu {
	
	//Needs to be updated when we start using a board size, right now board size is set to the screen size
	
	private Picture picture;
	private Rect dst;

	private SurfaceView surface;
	private Vec2d hero, offset;
	
	
	public boolean exiting = false;
	private double zoomMagnify = 1;
	
	public PauseMenu(SurfaceView s) {
		offset = new Vec2d();
		offset.x = 0;
		offset.y = 0;
		surface = s;
	}
	
	public void createPause(Picture p, Vec2d h) {
		GameObject.drawPause(p.beginRecording(800, 480));
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
		
		int pictureCenterX = (int) (surface.getWidth() / (2 * magnify)); //200
		int pictureCenterY = (int) (surface.getHeight() / (2 * magnify)); //120
		
		int canvasCenterX=  (surface.getWidth() / 2); //400
		int canvasCenterY = (surface.getHeight() / 2); //240
		
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
		
		Log.d("GSTA", "zoom - " + zoomMagnify);
		zoomMagnify += 0.05;
		centerScreen(zoomMagnify, hero);
		UpdateRect(zoomMagnify);

		return false;
	}
	
	
	
	public boolean zoomOut() {
		if (zoomMagnify<=1) return true;
		
		Log.d("GSTA", "zoom - " + zoomMagnify);
		zoomMagnify -= 0.05;
		centerScreen(zoomMagnify, hero);
		UpdateRect(zoomMagnify);
		
		return false;
		
	}
	public void UpdateInput(Vec2d velocity) {
		offset.add(velocity);
	}
	
	
	public void UpdateRect(double magnify) {
		int pictureWidth = (surface.getWidth() / 2); 
		int pictureHeight = (surface.getHeight() / 2);

		dst = new Rect( (int) (offset.x - (pictureWidth / magnify)),
				(int) (offset.y - (pictureHeight / magnify)),
				(int) (offset.x + (pictureWidth / magnify)),
				(int) (offset.y + (pictureHeight / magnify)));	
	}
	
	public void Draw(Canvas c) {
		c.drawPicture(picture, dst);
	}

}
