package voldaran.com.Upright;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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
	private int left, right, top, bottom;
	
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
			e.printStackTrace();
		}
		if (levelList!=null) {
			
			int colCount = 0;
			int rowCount = 0;
			
		//sorting list
			
			
			ArrayList<Integer> newLevelList = new ArrayList<Integer>();
			
			
			for (String level : levelList) {
				if (level.indexOf("level")>=0) {
					int lvlID = Integer.valueOf(level.substring(5, level.indexOf(".txt")));
					newLevelList.add(lvlID);
				}
			}
			
			Collections.sort(newLevelList);
			
			for (int level : newLevelList) {
			
				//Load Level button
				int left = 10 + rowCount * 130;
				int top = 10 + colCount * 86;
				
				Bitmap preview;
				
				preview = g.thread.loadLeveltoBitmap("level" + level + ".txt");
				
				menu.addButton(new MenuButton(new Rect(left, top, left + 110, top + 66), level, b, preview));
				
				if (rowCount>=3) {
					rowCount = 0;
					colCount++;
				} else rowCount++;
					
			}
			
		}
		
		
	}
	
	public MenuButton(Rect r, String Name) {
		clickableArea = r;
		left = clickableArea.left;
		right = clickableArea.right;
		top = clickableArea.top;
		bottom = clickableArea.bottom;
	}
	
	public void reset() {
		clickableArea.left = left;
		clickableArea.right = right;
		clickableArea.top = top;
		clickableArea.bottom = bottom;
	}
	
	public MenuButton(Rect r, int l, Bitmap b, Bitmap p) {
		Log.d("GSTA", "creating button " + l);
		clickableArea = r;
		id = l;
		previewMap = p;
		MenuButton.menuButton.add(this);
	}
	
	public void onClick() {
		
		//Start zoom animation
		String level = "level" + String.valueOf(id) + ".txt";
		mGame.thread.loadLevel(level, true);
		mGame.gameState = GameState.PLAYING;
	}
	
	public void drawButton(Canvas c, boolean drawText) {
		
		Paint text = new Paint();
		    text.setColor(Color.RED);
		    text.setTextSize(36);
		    text.setTypeface(Typeface.DEFAULT_BOLD);
		    
		c.drawBitmap(previewMap, null, clickableArea, null);
		    
		if (drawText) c.drawText(String.valueOf(id), clickableArea.left + 48 - (String.valueOf(id).length()*8), clickableArea.top + 47, text);
	}
	
}
