package voldaran.com.Upright;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class MenuTitleScreenPanel extends MenuSystem{
	public int mainLeft;
	public int activeLeft;
	public int drawOrder;
	public int goneLeft;
	public int currentLeft;
	public Bitmap bit;
	public Rect rec;
	
	
	public MenuTitleScreenPanel(Rect rect, Bitmap bitmap, int mainL, int activeL, int goneL) {
		bit = bitmap;
		rec = rect;
		mainLeft = mainL;
		activeLeft = activeL;
		goneLeft = goneL;
	}

}
