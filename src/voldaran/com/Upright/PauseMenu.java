package voldaran.com.Upright;

import voldaran.com.Upright.Game.GameState;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class PauseMenu {
	
	//Needs to be updated when we start using a board size, right now board size is set to the screen size
	
	private Bitmap bitPause;
	private Vec2d camera = new Vec2d();
	MenuTitleScreenPanel pause;
	Rect Continue, LevelSelect, Restart, Options, Pause;
	
	public PauseMenu(Vec2d c) {
		bitPause = Game.loadBitmapAsset("menu_pause.png");
		camera = c;
		pause = new MenuTitleScreenPanel(null, null, 0, 0, 0);
		
		Pause = new Rect((int) (camera.x / 2) - (bitPause.getWidth() / 2),(int) (camera.y / 2) - (bitPause.getHeight() / 2),(int) (camera.x / 2) + (bitPause.getWidth() / 2),(int) (camera.y / 2) + (bitPause.getHeight() / 2));
		Continue = new Rect(0,0,270,45);
			Continue.offsetTo((int) ((camera.x / 2) - (bitPause.getWidth() / 2) + 72), 
								(int) ((camera.y / 2) - (bitPause.getHeight() / 2) + 72));
		Options = new Rect(0,0,270,45);
			Options.offsetTo((int) ((camera.x / 2) - (bitPause.getWidth() / 2) + 72),
							(int) ((camera.y / 2) - (bitPause.getHeight() / 2) + 117));
		LevelSelect = new Rect(0,0,270,45);
			LevelSelect.offsetTo((int) ((camera.x / 2) - (bitPause.getWidth() / 2) + 72), 
							(int) ((camera.y / 2) - (bitPause.getHeight() / 2) + 162));
		Restart = new Rect(0,0,270,45);
			Restart.offsetTo((int) ((camera.x / 2) - (bitPause.getWidth() / 2) + 72), 
							(int) ((camera.y / 2) - (bitPause.getHeight() / 2) + 207));
			
			
	}
	
	public GameState onClick(Vec2d click, Game mGame) {
		
		if (Continue.intersects((int) click.x, (int) click.y,(int) (click.x+1), (int) (click.y+1)))
			return GameState.PLAYING;
		if (Options.intersects((int) click.x, (int) click.y,(int) (click.x+1), (int) (click.y+1)));
		if (Restart.intersects((int) click.x, (int) click.y,(int) (click.x+1), (int) (click.y+1))) {
			Game.thread.loadLevel(Game.currentLevel, true);
			return GameState.PLAYING;
		}
		if (LevelSelect.intersects((int) click.x, (int) click.y,(int) (click.x+1), (int) (click.y+1))) {
//			MenuTitleScreen.deactiveButton =; 
			return GameState.TITLE;
		}
		if (!Pause.intersects((int) click.x, (int) click.y,(int) (click.x+1), (int) (click.y+1)))
			return GameState.PLAYING;
		
		
		return GameState.PAUSED;
	}
	
	public void Draw(Canvas c) {
		c.drawBitmap(bitPause, (camera.x / 2) - (bitPause.getWidth() / 2),(camera.y / 2) - (bitPause.getHeight() / 2),null);
		
	}

}
