package voldaran.com.Upright;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Checkpoint extends GameObject{
	
	private static Long refreshTime = 1L;
	private static boolean deathTransition;
	
	public static boolean Update(Canvas c, Vec2d s) {
		Long currentTime = System.currentTimeMillis();
		
		if (hero.death) {
			Checkpoint.deathTransition = true;
			GameObject.restoreCheckpointAll();
			ALPHA = 255;
			fade = 0; 
			hero.death = false;
		}
		
		if (Checkpoint.deathTransition) {
			if (drawDeath(c, s)) Checkpoint.deathTransition = false; 
		}
		
		if (currentTime>Checkpoint.refreshTime+10000) {
			if (hero.ground!=null) {
			
				//Save game,
				GameObject.saveCheckpointAll();
				Checkpoint.refreshTime = System.currentTimeMillis();
				return true;
			} else Checkpoint.refreshTime = currentTime - 8000; //If checkpoint time has occured, and hero is not grounded, checks every 2 seconds until grounded. then saves.
			
		}
		return false;
		
	}
	
	
	private static double ALPHA = 255;
	private static double fade = 0;
	
	//Can be removed when we implement death. Just an animation to play to signify death
	public static boolean drawDeath(Canvas c, Vec2d cameraSize) {
		
		if (Checkpoint.ALPHA<50) return true;
		
		int height = (int) cameraSize.y;
		int width = (int) cameraSize.x;
		
		Rect rec = new Rect (0,0, width, height);
			Paint whitePaint = new Paint();
			whitePaint.setColor(Color.WHITE);
			whitePaint.setAlpha((int) ALPHA);
		c.drawRect(rec, whitePaint);
		
		Checkpoint.fade += 0.2;
		Checkpoint.ALPHA -= (int) Checkpoint.fade;
		return false;
	}

	
	
	public Checkpoint(Vec2d pos, Vec2d extent) {
		super(pos, extent);
	}
}
