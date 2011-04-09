package voldaran.com.Upright;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import voldaran.com.Upright.Game.GameState;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
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
	
	public static MenuPanel activePanel;
	
	public static MenuPanel mainPanel, worldSelect, levelSelect;
	public static Rect testRect = new Rect();
	public static Bitmap background;
	
	public static void clearMenus() {
//		
//		mainPanel = null;
//		worldSelect = null;
//		levelSelect = null;
	}
	
	public static void createMenus() {
		mainPanel = loadMainPanel();
		activePanel = mainPanel;
		worldSelect = loadWorldSelect();
//		activePanel = worldSelect;
	}
	
	public static PreviewButton continueButton;
	
//	+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
//	LOAD MAIN SCREEN
//	+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
	
	
	public static MenuPanel loadMainPanel() {
		
		background = Game.loadBitmapAsset("menu_background.png");
		
		
		MenuPanel o = new MenuPanel(background);
		
		o.step = 15;
		
		preview = Game.thread.loadLeveltoBitmap(Game.currentLevel);
		
		continueButton = new PreviewButton(new Rect(50, 150, 270, 282), preview) {
			public void onClick () {
				Game.thread.loadLevel(Game.currentLevel, true);
				Game.gameState = GameState.PLAYING;
			}
		};
		
		o.addButton(continueButton);
		preview = MenuSystem.loadWorldSelectToBitmap();
		o.addButton(new PreviewButton(new Rect(380, 220, 780, 460), preview) {
			
			public void onClick () {
//				MenuSystem.levelSelect = MenuSystem.loadLevelSelect("world2");
				MenuSystem.activePanel = MenuSystem.worldSelect;
			}
		});
		
		return o;
	}
	

	public static void updateContinue() {
		Bitmap preview = Game.thread.loadLeveltoBitmap(Game.currentLevel);
		
		continueButton.bitmap = preview;
//		continueButton = new PreviewButton(new Rect(0, 0, 100, 60), preview) {
//			public void onClick () {
//				Game.thread.loadLevel(Game.currentLevel, true);
//				Game.gameState = GameState.PLAYING;
//			}
//		};
		
	}
	
	public static Bitmap loadLevelSelectToBitmap(String world) {
		Log.d("GSTA", "loadLevelSelectToBitmap " + world);
		if (world!="world1") {
			world="world2";
		}
//		MenuPanel tempO = loadLevelSelect(world);
//		Bitmap previewLevel = Bitmap.createBitmap((int) Game.cameraSize.x, (int) Game.cameraSize.y, Bitmap.Config.ARGB_8888);
//		Canvas c = new Canvas(previewLevel);
//		tempO.draw(c);
		Bitmap previewLevel = Game.loadBitmapAsset(world + ".png");
		Matrix matrix = new Matrix();
		matrix.postScale((float) 0.50,(float) 0.50);
		
		Bitmap scaledPreview = Bitmap.createBitmap(previewLevel, 0, 0, previewLevel.getWidth(), previewLevel.getHeight(), matrix, true); 
		return scaledPreview;
	}
	
//	+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
//	LOAD LEVEL SELECT
//	+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
	
	
	public static MenuPanel loadLevelSelect(String world) {
		MenuPanel o = new MenuPanel(background);
		AssetManager assets = Game.mContext.getAssets();
		
		String[] levelList = null;
		
		try {
			levelList = assets.list("level/" + world);
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
				
				final String strLevel = world + "/level" + level + ".txt";
				preview = Game.thread.loadLeveltoBitmap(strLevel);
				
				o.addButton(new PreviewButton(new Rect(left, top, left + 110, top + 66), preview) {
					
					public void onClick () {
						Game.thread.loadLevel(strLevel, true);
						Game.gameState = GameState.PLAYING;
						Game.currentLevel = strLevel;
					}
					
				});
				int row = 4;
				if (colCount>1) row = 5;
				if (rowCount>=row) {
					rowCount = 0;
					colCount++;
				} else rowCount++;
					
			}
			
		}
		
		o.addButton(new PreviewButton(new Rect(750, 0, 800, 50), null, true) {
			
			public void onClick () {
				MenuSystem.activePanel = MenuSystem.worldSelect;
			}
		});
		
		
		return o;
	}
	
	public static Bitmap loadWorldSelectToBitmap() {
		
//		MenuPanel tempO = loadWorldSelect();
//		Bitmap previewLevel = Bitmap.createBitmap((int) Game.cameraSize.x, (int) Game.cameraSize.y, Bitmap.Config.ARGB_8888);
//		Canvas c = new Canvas(previewLevel);
//		tempO.draw(c);
		
		Bitmap previewLevel = Game.loadBitmapAsset("world_select.png");
		Matrix matrix = new Matrix();
		matrix.postScale((float) 0.50,(float) 0.50);
		
		Bitmap scaledPreview = Bitmap.createBitmap(previewLevel, 0, 0, previewLevel.getWidth(), previewLevel.getHeight(), matrix, true); 
		return scaledPreview;
		
		
	}
	public static Bitmap preview;
	
//	+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
//	LOAD WORLD SELECT
//	+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
	
	public static MenuPanel loadWorldSelect() {
		MenuPanel o = new MenuPanel(background);
		Log.d("GSTA", "loading world select");
		AssetManager assets = Game.mContext.getAssets();
		String[] worldList = null;
		
		try {
			worldList = assets.list("level");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ArrayList<Integer> newWorldList = new ArrayList<Integer>();
		
		for (String world : worldList) {
			if (world.indexOf("world")>=0) {
				int lvlID = Integer.valueOf(world.substring(5, world.length()));
				newWorldList.add(lvlID);
			}
		}
		
		Collections.sort(newWorldList);
		int colCount = 0;
		int rowCount = 0;
		
		for (final Integer world : newWorldList) {
			Log.d("GSTA", "WORLD" + world);
			
			//Load Level button
			int left = 10 + rowCount * 130;
			int top = 10 + colCount * 86;
			preview = MenuSystem.loadLevelSelectToBitmap("world" + world);
			
//			if (world!=1) {
//				preview = Game.loadBitmapAsset("world2.png");
//			} else preview = Game.loadBitmapAsset("world" + world + ".png");
			
			
			o.addButton(new PreviewButton(new Rect(left, top, left + 110, top + 66), preview) {
				
				public void onClick () {
					MenuSystem.levelSelect = MenuSystem.loadLevelSelect("world" + world);
					activePanel = MenuSystem.levelSelect;
				}
				
			});
			int row = 4;
			if (colCount>1) row = 5;
			if (rowCount>=row) {
				rowCount = 0;
				colCount++;
			} else rowCount++;
			
		}
		
		o.addButton(new PreviewButton(new Rect(750, 0, 800, 50), null, true) {
			
			public void onClick () {
				MenuSystem.activePanel = MenuSystem.mainPanel;
			}
		});
		
		
		return o;
	}
	
	public static void returnToMain() {
		MenuSystem.continueButton.bitmap = Game.thread.loadLeveltoBitmap(Game.currentLevel);
		MenuSystem.mainPanel.deactiveButton = MenuSystem.continueButton;
		MenuSystem.mainPanel.activeButton = null;
		MenuSystem.mainPanel.fullscreenButton(MenuSystem.continueButton);
		MenuSystem.mainPanel.setSnapshot();
		MenuSystem.activePanel = MenuSystem.mainPanel;
	}

	
	public static void processInput(Vec2d click) {
		MenuSystem.activePanel.processInput(click);
	}
	
	public static void updateMenus() {
		MenuSystem.activePanel.update();
		
	}
	
	public static void drawMenus(Canvas c) {
//		c.drawBitmap(background, 0, 0, null);
		MenuSystem.activePanel.draw(c);
	}
}
