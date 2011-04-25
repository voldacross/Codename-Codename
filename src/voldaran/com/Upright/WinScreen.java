package voldaran.com.Upright;

import voldaran.com.Upright.Game.GameState;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class WinScreen {
	
	private static Vec2d camera = new Vec2d();
	private static MenuPanel winScreen;
	private static PreviewButton nextButton, currentButton, homeButton;
	private static Bitmap bitWin;
	private static Rect rect = new Rect();

	public WinScreen(Vec2d c, LevelInfo currentLevel, LevelInfo OldInfo) {
		camera = c;
		
			
	}
	
	public static void CreateWin() {
		WinScreen.winScreen = CreateWinScreen();
	}
	
	public static MenuPanel CreateWinScreen() {
		Log.d("GSTA", "Create Menu....");
		Bitmap background = Game.loadBitmapAsset("win.png");

		MenuPanel o = new MenuPanel(background);
		o.step = 15;

		bitWin = Game.thread.loadLeveltoBitmap(Game.currentLevel + 1);
		Bitmap nButton = Game.loadBitmapAsset("next_button.png");
		Canvas canvas = new Canvas(bitWin);

		rect.left = 0;rect.top = 0;
		rect.right = bitWin.getWidth();rect.bottom = bitWin.getHeight();

		canvas.drawBitmap(nButton, null, rect, null);

		WinScreen.nextButton = new PreviewButton(new Rect(524,307,792,468), bitWin){
			public void onClick() {
				Game.currentLevel = Game.currentLevel + 1;
				Game.thread.loadLevel(Game.currentLevel, true);
				Game.gameState = GameState.PLAYING;
			}
		};
		o.addButton(nextButton);
		//268x161

		//Add menu button.
		
		bitWin = MenuSystem.loadMainPaneltoBitmap();
		Log.d("GSTA", "bitWin " + bitWin.getWidth());
		WinScreen.homeButton = new PreviewButton(new Rect(524,19,792,180), bitWin){
			public void onClick() {
//				MenuSystem.returnToMain();
				Game.gameState = Game.GameState.TITLE;
				if (MenuSystem.mainPanel.activeButton!=null)MenuSystem.mainPanel.activeButton.reset();
				if (MenuSystem.mainPanel.deactiveButton!=null)MenuSystem.mainPanel.deactiveButton.reset();
				MenuSystem.mainPanel.activeButton = null;
				MenuSystem.mainPanel.deactiveButton = null;
				MenuSystem.mainPanel.inTransition = false;
				MenuSystem.activePanel = MenuSystem.mainPanel;
			}
		};
		
		o.addButton(homeButton);
		
		Log.d("GSTA", "homeButton ----- " + homeButton.toString());
		
		bitWin = Game.thread.loadLeveltoBitmap(Game.currentLevel);

		WinScreen.currentButton = new PreviewButton(new Rect(270,162,537,322), bitWin){
			public void onClick() {
				Game.thread.loadLevel(Game.currentLevel, true);
				Game.gameState = GameState.PLAYING;
			}
		};
		o.addButton(currentButton);
		
		o.deactiveButton = WinScreen.currentButton;
		o.activeButton = null;
		o.fullscreenButton(WinScreen.currentButton);
		o.setSnapshot();
		o.inTransition = true;

		return o;
	}

	public static void processInput(Vec2d click) {
		winScreen.processInput(click);
	}

	public static void updateMenu() {
		WinScreen.winScreen.update();
	}

	public static void Draw(Canvas c) {
		WinScreen.winScreen.draw(c);
	}

}
