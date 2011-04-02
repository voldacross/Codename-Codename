package voldaran.com.Upright;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;


public class MenuSystem {

	public MenuSystem () {
		
//		AssetManager assets = Game.mContext.getAssets();
//		
//		String[] levelList = null;
//		
//		try {
//			levelList = assets.list("level/" /*+ AssetFolder*/);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
////		if (levelList.)
		
		
		
		
	}
	
	public static MenuPanel mainPanel;
	
	public static void CreateMenus() {
		Rect butContinue = new Rect(50, 52, 250, 115);
		mainPanel = new MenuPanel();
		
		mainPanel.addButton(new Button(butContinue) {
			public void onClick() {
				Log.d("GSTA", "You clicked!");
			}
		});
		
	}
	
	public static void updateMenus(Vec2d Click) {
		mainPanel.update(Click);
		
	}
	
	public static void drawMenus(Canvas c) {
		mainPanel.draw(c);
	}
}
