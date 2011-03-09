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
	private static MenuTitleScreenPanel activePanel;
	private MenuTitleScreenPanel releasePanel;
	private boolean inTransition;
	private Game mGame;
	
	public static ArrayList<MenuTitleScreenPanel> menuTitlePanels = new ArrayList<MenuTitleScreenPanel>();

	public static void Reset() {
		
		MenuTitleScreen.activePanel = null;
	}
	public MenuTitleScreen(Game game) {
		//Create the 3 panels
		
		mGame = game;

		int height = (int) Game.cameraSize.y;
		int width = (int) Game.cameraSize.x;

		
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

		MenuTitleScreen.activePanel = null;

		MenuTitleScreen.menuTitlePanels.add(Setup);
		MenuTitleScreen.menuTitlePanels.add(About);
		MenuTitleScreen.menuTitlePanels.add(Play);
		
		Play.menuButtons.clear();
		Log.d("GSTA", "Clearing menuButtons");
		Bitmap menuButton = Game.loadBitmapAsset("menublock.png");
		MenuButton.loadLevelButtons(Play, menuButton, mGame);
		
	}
	
	private static MenuButton activeButton = null;
	public static MenuButton deactiveButton = null;
	
	int distanceLeft;
	int distanceRight;
	int distanceUp;
	int distanceDown;
	
	public void processInput(Input input, Vec2d clicked) {
		Log.d("GSTA", "processInput " + input);
		
		if ((!inTransition) && (!clicked.isVoid())){
			if (MenuTitleScreen.activePanel==About) {
				if (input==UserInput.Input.PRESS_LEFT) {
					MenuTitleScreen.activePanel = null;
//					Log.d("GSTA", "setting releasePanel to About");
					releasePanel = About;
					inTransition = true;
				} else {
					
					
				}
				
			} else if (MenuTitleScreen.activePanel==Play) {
				if (input==UserInput.Input.PRESS_RIGHT) {
					MenuTitleScreen.activePanel = null;
					releasePanel = Play;
//					Log.d("GSTA", "setting releasePanel to Play");
					inTransition = true;
				} else {
					if ((clicked!=null)&&(Play.returnButton(clicked)!= null)&&(!inTransition)&&(activeButton==null)&&(deactiveButton==null)){
						
						activeButton = Play.returnButton(clicked);

						distanceLeft = activeButton.clickableArea.left;
						distanceRight = (int) Game.cameraSize.x - activeButton.clickableArea.right;
						distanceUp = activeButton.clickableArea.top;
						distanceDown = (int) Game.cameraSize.y - activeButton.clickableArea.bottom;
						
						
//						Play.returnButton(clicked).onClick();
					}
				}
				
			} else if (MenuTitleScreen.activePanel==Setup) {
//				if (input==UserInput.Input.PRESS_UP){
//					MenuTitleScreen.activePanel = null;
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
//						MenuTitleScreen.activePanel = Setup;
					}
					if (input==UserInput.Input.PRESS_LEFT) MenuTitleScreen.activePanel = Play;
					if (input==UserInput.Input.PRESS_RIGHT) MenuTitleScreen.activePanel = About;
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
				
				if (MenuTitleScreen.activePanel==m) {
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
				} else if (MenuTitleScreen.activePanel==null) {
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
					if (MenuTitleScreen.activePanel==Setup) {
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

		if ((MenuTitleScreen.activePanel==About)||(releasePanel==About)) {
			c.drawBitmap(Play.bit, null, Play.rec, null);
			c.drawBitmap(About.bit, null, About.rec, null);
		} else if ((MenuTitleScreen.activePanel==Play)||(releasePanel==Play)) {
			c.drawBitmap(About.bit, null, About.rec, null);
			c.drawBitmap(Play.bit, null, Play.rec, null);
			
			if ((!inTransition)&&(MenuTitleScreen.activePanel==Play)) Play.drawButtons(c, activeButton, deactiveButton);
			
		} else {
			
			c.drawBitmap(About.bit, null, About.rec, null);
			c.drawBitmap(Play.bit, null, Play.rec, null);
		}
		
		if(activeButton!=null) {
			
//			activeButton.clickableArea.offsetTo(0, 0);
//			activeButton.clickableArea.left = 0;



//			activeButton.clickableArea.right = activeButton.clickableArea.right + (distanceRight / 15);
//			activeButton.clickableArea.left = activeButton.clickableArea.left - (distanceLeft / 15);
//			activeButton.clickableArea.bottom = activeButton.clickableArea.bottom + (distanceDown / 15);
//			activeButton.clickableArea.top = activeButton.clickableArea.top - (distanceUp / 15);
			
			if (activeButton.clickableArea.right<=Game.cameraSize.x) activeButton.clickableArea.right = activeButton.clickableArea.right + ((distanceRight / 15) + 1);
			if (activeButton.clickableArea.left>=0) activeButton.clickableArea.left = activeButton.clickableArea.left - ((distanceLeft / 15) + 1);
			if (activeButton.clickableArea.bottom<=Game.cameraSize.y) activeButton.clickableArea.bottom = activeButton.clickableArea.bottom + ((distanceDown / 15) + 1);
			if (activeButton.clickableArea.top>=0) activeButton.clickableArea.top = activeButton.clickableArea.top - ((distanceUp / 15) + 1);
			
			if ((activeButton.clickableArea.right>=Game.cameraSize.x) 
					&& (activeButton.clickableArea.bottom>=Game.cameraSize.y) 
					&& ((activeButton.clickableArea.top<=0)) 
					&& ((activeButton.clickableArea.left<=0))) {
				
				activeButton.onClick();
				
				//Reset Button
//				activeButton.clickableArea.right = (int) (Game.cameraSize.x - distanceRight);
//				activeButton.clickableArea.left = distanceLeft;
//				activeButton.clickableArea.bottom = (int) (Game.cameraSize.y - distanceDown);
//				activeButton.clickableArea.top = distanceUp;
				//save Button
				deactiveButton = activeButton;
				activeButton = null;
				
			}
		}
		
		
		if ((activeButton==null)&&(deactiveButton!=null)) {
			deactiveButton.clickableArea.right = deactiveButton.clickableArea.right - ((distanceRight / 15) + 1);
			deactiveButton.clickableArea.left = deactiveButton.clickableArea.left + ((distanceLeft / 15) + 1);
			deactiveButton.clickableArea.bottom = deactiveButton.clickableArea.bottom - ((distanceDown / 15) + 1);
			deactiveButton.clickableArea.top = deactiveButton.clickableArea.top + ((distanceUp / 15) + 1);
			
			if ((deactiveButton.clickableArea.right<=(int) (Game.cameraSize.x - distanceRight)) 
					&& (deactiveButton.clickableArea.bottom<=(int) (Game.cameraSize.y - distanceDown)) 
					&& ((deactiveButton.clickableArea.top>=distanceUp)) 
					&& ((deactiveButton.clickableArea.left>=distanceLeft))) {
				
				deactiveButton.clickableArea.right = (int) (Game.cameraSize.x - distanceRight);
				deactiveButton.clickableArea.left = distanceLeft;
				deactiveButton.clickableArea.bottom = (int) (Game.cameraSize.y - distanceDown);
				deactiveButton.clickableArea.top = distanceUp;
				
				deactiveButton = null;
			}
		}
		
		
		
	}
	
	
	
	
	

}
