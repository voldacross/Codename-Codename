package voldaran.com.Upright;

import java.io.IOException;

import voldaran.com.Upright.Game.GameState;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

public class MenuButton {

	public int id;
	public Rect clickableArea;
	private Bitmap bitmap;
	
	public static Game mGame;
	
	public static void loadLevelButtons(MenuTitleScreenPanel menu, Bitmap b, Game g) {
		AssetManager assets = Game.mContext.getAssets();
		String[] happy = null;
		mGame = g;
		try {
			happy = assets.list("level");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (happy!=null) {
			
			int colCount = 0;
			int rowCount = 0;
		
			for (String fun : happy) {
			
//				level25.txt
				if (fun.indexOf("level")>=0) {
					//Load Level button
					
					
					int lvlID = Integer.valueOf(fun.substring(5, fun.indexOf(".txt")));
					int left = 10 + rowCount * 110;
					int top = 10 + colCount * 110;
					menu.addButton(new MenuButton(new Rect(left, top, left + 90, top + 90), lvlID, b));
					if (rowCount>=4) {
						rowCount = 0;
						colCount++;
					} else rowCount++;
					
				}
			
			}
			
		}
		
		
	}
	
//	MenuButton level2 = new MenuButton(levelButton2, "levelTwo", mGame, menuButton) {
//	public void onClick() {
//		game.thread.loadLevel("level2.txt");
//		game.gameState = GameState.PLAYING;
//	}
//};
	
	public MenuButton(Rect r, String Name) {
		clickableArea = r;
	}
	
	public MenuButton(Rect r, int l, Bitmap b) {
		Log.d("GSTA", "creating button " + l);
		clickableArea = r;
		id = l;
		bitmap = b;
	}
	
	public void onClick() {
		String level = "level" + String.valueOf(id) + ".txt";
		mGame.thread.loadLevel(level);
		
		mGame.gameState = GameState.PLAYING;
	}
	
	public void drawButton(Canvas c) {
		
//		Paint paintObject = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
//		paintObject.setColor(Color.GREEN);
//		c.drawRect(clickableArea, paintObject);
		
		Paint text = new Paint();
		    text.setColor(Color.BLACK);
		    text.setTextSize(32);
		    
		    text.setTypeface(Typeface.DEFAULT_BOLD);
		    
		c.drawBitmap(bitmap, null, clickableArea, null);
		c.drawText(String.valueOf(id), clickableArea.left + 45 - (String.valueOf(id).length()*8), clickableArea.top + 60, text);
	}
	
}
