package voldaran.com.Upright;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class PreviewButton extends Button{

	private int distanceLeft, distanceRight, distanceUp, distanceDown;

	public PreviewButton(Rect r, Bitmap p, boolean b) {
		super(r, p);
		Back = b;
		distanceLeft = buttonSize.left;
		distanceRight = (int) Game.cameraSize.x - buttonSize.right;
		distanceUp = buttonSize.top;
		distanceDown = (int) Game.cameraSize.y - buttonSize.bottom;
	}
	
	public PreviewButton(Rect r, Bitmap p) {
		super(r, p);
		distanceLeft = buttonSize.left;
		distanceRight = (int) Game.cameraSize.x - buttonSize.right;
		distanceUp = buttonSize.top;
		distanceDown = (int) Game.cameraSize.y - buttonSize.bottom;
	}
	
	
	public boolean Expand() {
		if (buttonSize.right<=Game.cameraSize.x) buttonSize.right = buttonSize.right + ((distanceRight / 15) + 1);
		if (buttonSize.left>=0) buttonSize.left = buttonSize.left - ((distanceLeft / 15) + 1);
		if (buttonSize.bottom<=Game.cameraSize.y) buttonSize.bottom = buttonSize.bottom + ((distanceDown / 15) + 1);
		if (buttonSize.top>=0) buttonSize.top = buttonSize.top - ((distanceUp / 15) + 1);
		
		
		if ((buttonSize.right>=Game.cameraSize.x) 
				&& (buttonSize.bottom>=Game.cameraSize.y) 
				&& ((buttonSize.top<=0)) 
				&& ((buttonSize.left<=0))) {
			return true;
		} else {
			return false;
		}
			
		
	}
	
	@Override
	public void fullScreen() {
		buttonSize.right = (int) Game.cameraSize.x;
		buttonSize.left = 0;
		buttonSize.bottom = (int) Game.cameraSize.y;
		buttonSize.top = 0;
	}
	
	@Override
	public void reset() {
		buttonSize.right = (int) (Game.cameraSize.x - distanceRight);
		buttonSize.left = distanceLeft;
		buttonSize.bottom = (int) (Game.cameraSize.y - distanceDown);
		buttonSize.top = distanceUp;
	}
	
	public boolean Contract () {
		buttonSize.right = buttonSize.right - ((distanceRight / 15) + 1);
		buttonSize.left = buttonSize.left + ((distanceLeft / 15) + 1);
		buttonSize.bottom = buttonSize.bottom - ((distanceDown / 15) + 1);
		buttonSize.top = buttonSize.top + ((distanceUp / 15) + 1);
		
		
		if ((buttonSize.right<=(int) (Game.cameraSize.x - distanceRight)) 
				&& (buttonSize.bottom<=(int) (Game.cameraSize.y - distanceDown)) 
				&& ((buttonSize.top>=distanceUp)) 
				&& ((buttonSize.left>=distanceLeft))) {
			reset();
			return true;
		} else {
			return false;
		}
		
	}

}
