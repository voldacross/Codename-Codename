package voldaran.com.Upright;

import java.io.IOException;
import java.util.ArrayList;

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
	
	public static Game mGame;
	private Bitmap previewMap;
	
	public static ArrayList<MenuButton> menuButton = new ArrayList<MenuButton>();
	
	public static void loadLevelButtons(MenuTitleScreenPanel menu, Bitmap b, Game g) {
		AssetManager assets = Game.mContext.getAssets();
		String[] levelList = null;
		mGame = g;
		try {
			levelList = assets.list("level");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (levelList!=null) {
			
			int colCount = 0;
			int rowCount = 0;
		
			for (String level : levelList) {
			
				if (level.indexOf("level")>=0) {
					//Load Level button
					int lvlID = Integer.valueOf(level.substring(5, level.indexOf(".txt")));
					int left = 10 + rowCount * 130;
					int top = 10 + colCount * 86;
					
					Bitmap preview;
					
					preview = g.thread.loadLeveltoBitmap("level" + String.valueOf(lvlID) + ".txt");
					
					menu.addButton(new MenuButton(new Rect(left, top, left + 110, top + 66), lvlID, b, preview));
					
					
					if (rowCount>=3) {
						rowCount = 0;
						colCount++;
					} else rowCount++;
					
				}
			
			}
			
		}
		
		
	}
	
	public MenuButton(Rect r, String Name) {
		clickableArea = r;
	}
	
	public MenuButton(Rect r, int l, Bitmap b, Bitmap p) {
		Log.d("GSTA", "creating button " + l);
		clickableArea = r;
		id = l;
		previewMap = p;
		MenuButton.menuButton.add(this);
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
		    
		c.drawBitmap(previewMap, null, clickableArea, null);
		    
		c.drawText(String.valueOf(id), clickableArea.left + 50 - (String.valueOf(id).length()*8), clickableArea.top + 45, text);
	}
	
}
