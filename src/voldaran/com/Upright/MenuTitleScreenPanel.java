package voldaran.com.Upright;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class MenuTitleScreenPanel {
	public int mainLeft;
	public int activeLeft;
	public int drawOrder;
	public int goneLeft;
	public int currentLeft;
	public Bitmap bit;
	public Rect rec;
	
	
	public static MenuTitleScreenPanel activePanel;
	
	public ArrayList<MenuButton> menuButtons = new ArrayList<MenuButton>();
	
	
	
	public Rect r;
	
	int left, right, top, bottom;

	public void drawButtons(Canvas c, MenuButton activeButton) {
		for(MenuButton b : menuButtons){
			if (b!=activeButton) b.drawButton(c, true);
		}
		
		if (activeButton!=null) activeButton.drawButton(c, false);
		
	}
	
	public void addButton(Rect b, String name) {
		MenuButton button = new MenuButton(b, name);
		menuButtons.add(button);
	}
	
	public void addButton(MenuButton button) {
		menuButtons.add(button);
	}
	
	public MenuButton returnButton(Vec2d click) {
		for(MenuButton b : menuButtons){
			
			Rect test = b.clickableArea;
			
			if (test.intersects((int) click.x,(int) click.y,(int) click.x,(int) click.y)) {
//				Log.d("GSTA", "You have clicked on " + b.name);
				return b;
			}
			
		}
		return null;
	}
	
	
	
	public MenuTitleScreenPanel(Rect rect, Bitmap bitmap, int mainL, int activeL, int goneL) {
		bit = bitmap;
		rec = rect;
		mainLeft = mainL;
		activeLeft = activeL;
		goneLeft = goneL;
	}

}