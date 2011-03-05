package voldaran.com.Upright;

import java.util.ArrayList;

import voldaran.com.Upright.Game.GameState;
import voldaran.com.Upright.UserInput.Input;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class MenuTitleScreen {

	private MenuTitleScreenPanel Setup;
	private MenuTitleScreenPanel About;
	private MenuTitleScreenPanel Play;
	private MenuTitleScreenPanel activePanel;
	private MenuTitleScreenPanel releasePanel;
	private boolean inTransition;
	private Game mGame;
	
	public static ArrayList<MenuTitleScreenPanel> menuTitlePanels = new ArrayList<MenuTitleScreenPanel>();

	public MenuTitleScreen(Game game) {
		//Create the 3 panels
		
		mGame = game;

		int height = (int) game.cameraSize.y;
		int width = (int) game.cameraSize.x;

		
		Rect recSetup = new Rect(0, 0, width, height);
		
		Rect recPlay = new Rect(0, 0, width, height);
		recPlay.offsetTo(-(width * 2 / 3), 0);
		Rect recAbout = new Rect(0, 0, width, height);
		recAbout.offsetTo(width * 2 / 3, 0);

		Bitmap bitPlay = Game.loadBitmapAsset("menu_play2.png");
		Bitmap bitAbout = Game.loadBitmapAsset("menu_about.png");
		Bitmap bitSetup= Game.loadBitmapAsset("menu_setup2.png");
		
		//Create Panels - #s - main, active, gone
		Setup = new MenuTitleScreenPanel(recSetup, bitSetup, 0, 0, 0);
		About = new MenuTitleScreenPanel(recAbout, bitAbout, width * 2 / 3, 0, width);
		Play = new MenuTitleScreenPanel(recPlay, bitPlay, -(width * 2 / 3), 0,-width);

		activePanel = null;

		MenuTitleScreen.menuTitlePanels.add(Setup);
		MenuTitleScreen.menuTitlePanels.add(About);
		MenuTitleScreen.menuTitlePanels.add(Play);
		
		
		Bitmap menuButton = Game.loadBitmapAsset("menublock.png");
		MenuButton.loadLevelButtons(Play, menuButton, mGame);
		
		
		
		
		
		
	}
	
	public void processInput(Input input, Vec2d clicked) {
		Log.d("GSTA", "processInput " + input);
		
		if ((!inTransition) && (!clicked.isVoid())){
			if (activePanel==About) {
				if (input==UserInput.Input.PRESS_LEFT) {
					activePanel = null;
//					Log.d("GSTA", "setting releasePanel to About");
					releasePanel = About;
					inTransition = true;
				} else {
					
					
				}
				
			} else if (activePanel==Play) {
				if (input==UserInput.Input.PRESS_RIGHT) {
					activePanel = null;
					releasePanel = Play;
//					Log.d("GSTA", "setting releasePanel to Play");
					inTransition = true;
				} else {
					if ((clicked!=null)&&(Play.returnButton(clicked)!= null)&&(!inTransition)){
						Play.returnButton(clicked).onClick();
					}
				}
				
			} else if (activePanel==Setup) {
//				if (input==UserInput.Input.PRESS_UP){
//					activePanel = null;
//					inTransition = true;
//				} else {
//					if ((clicked!=null)&&(Setup.returnButton(clicked)!= null)&&(!inTransition)){
//						Log.d("GSTA", "You clicked " + Setup.returnButton(clicked).name);
//						Setup.returnButton(clicked).onClick();
//					}
//				}
				
			} else {
				if (!inTransition) {
					if (input==UserInput.Input.PRESS_UP) {
						mGame.thread.loadLevel();
						mGame.gameState = GameState.PLAYING;
//						activePanel = Setup;
					}
					if (input==UserInput.Input.PRESS_LEFT) activePanel = Play;
					if (input==UserInput.Input.PRESS_RIGHT) activePanel = About;
				}
			}
			
			if (!inTransition) {
				
//				Log.d("GSTA", "setting releasePanel to null");
				releasePanel=null;
			}
		} 
	}
	
	public final int transitionSpeed = 30;
	
	public void update() {
		
		inTransition = false;
		for(MenuTitleScreenPanel m : menuTitlePanels){
			
				m.currentLeft = m.rec.left;
				
				if (activePanel==m) {
					if (m.currentLeft!=m.activeLeft) {
						inTransition = true;
						//not in the correct position
						//Transition to correct Active position
						if (m.currentLeft > m.activeLeft) {
							m.currentLeft -= transitionSpeed;
							if (m.currentLeft < m.activeLeft) m.currentLeft = m.activeLeft;
						} else if (m.currentLeft < m.activeLeft) {
							m.currentLeft += transitionSpeed;
							if (m.currentLeft > m.activeLeft) m.currentLeft = m.activeLeft;
						}
					}
				} else if (activePanel==null) {
					//Main Menu, return to main position
					if (m.currentLeft!=m.mainLeft) {
						inTransition = true;
						if (m.currentLeft > m.mainLeft) {
							m.currentLeft -= transitionSpeed;
							if (m.currentLeft < m.mainLeft) m.currentLeft = m.mainLeft;
						} else if (m.currentLeft < m.mainLeft) {
							m.currentLeft += transitionSpeed;
							if (m.currentLeft > m.mainLeft) m.currentLeft = m.mainLeft;
						}
					}
				} else {
					if (activePanel==Setup) {
						//Not main menu, Not Self, return to offScreen Position
						if (m.currentLeft!=m.goneLeft) {
							inTransition = true;
							//Transition back 
							if (m.currentLeft > m.goneLeft) {
								m.currentLeft -= transitionSpeed;
								if (m.currentLeft < m.goneLeft) m.currentLeft = m.goneLeft;
							} else if (m.currentLeft < m.goneLeft) {
								m.currentLeft += transitionSpeed;
								if (m.currentLeft > m.goneLeft) m.currentLeft = m.goneLeft;
							}
						}
					}
				}
				
				
				
//				Log.d("GSTA", "offsetting " + m.rec.left + " to " + m.currentLeft);
				m.rec.offsetTo(m.currentLeft, 0);
			
		}
	}
	
	public void drawPanels(Canvas c) {
		
		
		
		c.drawBitmap(Setup.bit, null, Setup.rec, null);

		if ((activePanel==About)||(releasePanel==About)) {
			c.drawBitmap(Play.bit, null, Play.rec, null);
			c.drawBitmap(About.bit, null, About.rec, null);
		} else if ((activePanel==Play)||(releasePanel==Play)) {
			c.drawBitmap(About.bit, null, About.rec, null);
			c.drawBitmap(Play.bit, null, Play.rec, null);
			
			if ((!inTransition)&&(activePanel==Play)) Play.drawButtons(c);
			
		} else {
			
			c.drawBitmap(About.bit, null, About.rec, null);
			c.drawBitmap(Play.bit, null, Play.rec, null);
		}
		
		
		
		
	}
	
	
	
	
	

}
