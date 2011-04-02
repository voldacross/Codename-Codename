package voldaran.com.Upright;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class PreviewButton extends Button{

	private int distanceLeft, distanceRight, distanceUp, distanceDown;

	public PreviewButton(Rect r, Bitmap p) {
		super(r, p);
	}
	
	public void Expand() {
		
		distanceLeft = buttonSize.left;
		distanceRight = (int) Game.cameraSize.x - buttonSize.right;
		distanceUp = buttonSize.top;
		distanceDown = (int) Game.cameraSize.y - buttonSize.bottom;
		
		
		if (buttonSize.right<=Game.cameraSize.x) buttonSize.right = buttonSize.right + ((distanceRight / 15) + 1);
		if (buttonSize.left>=0) buttonSize.left = buttonSize.left - ((distanceLeft / 15) + 1);
		if (buttonSize.bottom<=Game.cameraSize.y) buttonSize.bottom = buttonSize.bottom + ((distanceDown / 15) + 1);
		if (buttonSize.top>=0) buttonSize.top = buttonSize.top - ((distanceUp / 15) + 1);
		
	}
	
	public void Contract () {
		
		distanceLeft = buttonSize.left;
		distanceRight = (int) Game.cameraSize.x - buttonSize.right;
		distanceUp = buttonSize.top;
		distanceDown = (int) Game.cameraSize.y - buttonSize.bottom;
		
		
		buttonSize.right = buttonSize.right - ((distanceRight / 15) + 1);
		buttonSize.left = buttonSize.left + ((distanceLeft / 15) + 1);
		buttonSize.bottom = buttonSize.bottom - ((distanceDown / 15) + 1);
		buttonSize.top = buttonSize.top + ((distanceUp / 15) + 1);
		
		
	}

}
