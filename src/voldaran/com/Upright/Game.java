package voldaran.com.Upright;

import java.math.BigInteger;

import voldaran.com.Upright.UserInput.Input;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

// To "play" the game, played with the unit placed horizontally. Press on the right side to move right, press on the 
// left side to move left. "Swiping" your fingers up or down will shift the gravity in the direction you swipe.
// Swipping left or right will cause hero to jump - NOT IMPLEMENTED YET


//For more information on design, please see below
/*
This is me typing....

This is a test
Feel free to update with new ideas.

Platform / Puzzle game.

Armor / Body Part Items provide abilities and are collected

	Boots - provide gravity switch. Floor to ceiling.
			If boots are on, hero cannot jump unless legs are found
			
	Legs -	Provide jump bonus. If no boots are on, jump even higher.
	
	Arms -	Provide climbing up and down vertical walls.
			With the ability to wall jump back and forth - Maybe utilizing another item, Boots? Legs?
			
	Head -	Options: Shows invisible platforms
					 Alters platforms to new position
					 Mind control to be able to move around existing "special" platforms. I LIKE THIS IDEA.
					 Activate/Always-On to slow down time. Enabling fast hero to move passed moving objects
					 
All these parts are situated(!) throughout the level. User must use quick timing and ingenuity to gain access
to more body parts and be able to explore new areas of the map.


Level - Game Design

	1-15 "Tutorial" levels
	30 Standard Mission Levels
	
	Daily Mission
		Only can be played on that day. All previous missions are playable with AdFree+ version
		Mission should have a difficultly warning. "Easy, Medium, Insane, etc". Not a toggle.
		
		Beating the level unlocks same level with more obstacles, less body parts?
		
Obstacle Ideas

	Buzz Saw
	Spikes that shoot up from little holes
	Spiked Floor / Lava floor
	Treadmills
	Floor you can't gravitate towards?
	Shooting projectiles
	Walking/Moving Enemies. NOT A FAN OF THIS IDEA
	Ejection Pad. Shoots you into the air at a designated target.
	
	



Notes: 
*/
//END NOTES

public class Game extends SurfaceView implements SurfaceHolder.Callback {
	private GameThread thread;
	public float fingerX;
	public float fingerY;
	public boolean keyDown;
	public boolean keyDrag;
	public float fingerDragStartX;
	public float fingerDragStartY;
	public float fingerDragEndX;
	public float fingerDragEndY;
	public boolean keyActualDrag;
	public boolean launch;
	public float mCurrentTouchX;
	public float mCurrentTouchY;
    public float mPreviousTouchX;
    public float mPreviousTouchY;
    public boolean bolFingerDown = false;
    public boolean fingerDrag = false;
    public boolean bolDragActive;
    public boolean bolDragRelease;
    
	public int gameTIME = 0;
	
	public Context mContext;
	public static final int USER_TOUCH_NONE = 1000;
	public static final int USER_SWIPE_LEFT = 1001;
	public static final int USER_SWIPE_RIGHT = 1002;
	public static final int USER_SWIPE_UP = 1003;
	public static final int USER_SWIPE_DOWN = 1004;
	public static final int USER_PRESS_RIGHT = 1005;
	public static final int USER_PRESS_MIDDLE = 1006;
	public static final int USER_PRESS_LEFT = 1007;
	
	public int USER_TOUCH = 1000;
	
	public UserInput _input = new UserInput(this);
	
	
	public Game(Context context) {
		super(context);
		getHolder().addCallback(this);
		setFocusable(true);
		mContext = context;
		
	}
	
	
	@Override
	public boolean onTouchEvent (MotionEvent event) {
		_input.UpdateInput(event);
		
		return true;
	}
	
	//Finds distance
	public double distance (float X1, float Y1, float X2, float Y2) {
		return Math.sqrt(((X2 - X1) * ( X2 - X1)) + ((Y2 - Y1) * (Y2 - Y1)));

	}
	
	public void createThread(){
		thread = new GameThread(getHolder(), this);

	}
	
	public void stopThread(){
		thread.setRunning(false);
		
		boolean retry = true;
        while (retry) {
        	try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
        
		thread = null;
		
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
	int height) {
	// TODO Auto-generated method stub
	}
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("GSTA", "surfaceCreated");
		thread.setRunning(true);
		thread.start();		
	}
	
	//Destroys thread if app is closed, kind of buggy at the moment.
	//Game doesn't force close on shutdown, but doesn't re-open smoothly

	@Override 
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	
	public GameThread getThread () {
		return thread;
	}
	
	
	//GAME THREAD
	class GameThread extends Thread {
		private SurfaceHolder _surfaceHolder;
		private GameHero hero;
		public boolean mRun = false;
		
		public MenuTitle menuSetup, menuAbout, menuPlay;

		
		//public MapClass.TileClass[][] aTile;
		
		public Bitmap bitHero;
		
//		private ArrayList<Walls> _walls = new ArrayList<Walls>();
		
		public Bundle saveGameState() {
			Bundle save = new Bundle();
			//Data to be saved
			save.putLong("HERO_X",hero.pos.x);
			save.putLong("HERO_Y",hero.pos.y);
			
			return save;
		}
		
		public void resumeGameState(Bundle resume) {
			Log.d("GSTA", "Setting hero.pos");
			Log.d("GSTA","resuming... " + resume.getLong("HERO_X") + "," +  resume.getLong("HERO_Y"));
			hero.pos.set(resume.getLong("HERO_X"), resume.getLong("HERO_Y"));
			
			
		}
		
		public void addWall(int posx, int posy, int extentx, int extenty){
			new GameObject(new Vec2d(posx, posy).mul(1000), new Vec2d(extentx, extenty).mul(1000));
		}
		
		
		private MenuTitleScreen titleMenu;
		
		public GameThread(SurfaceHolder surfaceHolder, Game game) {
			_surfaceHolder = surfaceHolder;
			
			GameObject.gameObjects.clear();
			MovingObject.movingObjects.clear();
			
		//creating a level, added hero and walls.
			
			
			bitHero = BitmapFactory.decodeResource(getResources(),R.drawable.icon);
			hero = new GameHero(new Vec2d(600000,300000), new Vec2d(bitHero.getWidth() / 2 * 1000,bitHero.getHeight() / 2 * 1000), bitHero);

			Log.d("GSTA", "hero : " + hero.pos);
			Log.d("GSTA", "hero top, left, bottom, right : " + (hero.pos.x - hero.extent.x) + "," 
					                                         + (hero.pos.y - hero.extent.y) + ","
					                                         + (hero.pos.x + hero.extent.x) + ","
					                                         + (hero.pos.y + hero.extent.y));
			
			addWall(400,475,400,10);
			addWall(400,5,400,10);
			addWall(145,105,145,10);
			addWall(375,100,10,100);
			addWall(190,205,110,10);
			addWall(85,185,10,30);
			addWall(185,260,10,65);
			addWall(140,315,40,10);
			addWall(290,260,10,65);
			
			titleMenu = new MenuTitleScreen(game.mContext);
			
			
			int height = 480;
			int width = 800;
			Rect recSetup = new Rect(0, 0, width, height);
			Rect recPlay = new Rect(0, 0, width, height);
			Rect recAbout = new Rect(0, 0, width, height);

			
			Bitmap bitPlay = BitmapFactory.decodeResource(getResources(),R.drawable.menu_play);
			Bitmap bitAbout = BitmapFactory.decodeResource(getResources(),R.drawable.menu_about);
			Bitmap bitSetup= BitmapFactory.decodeResource(getResources(),R.drawable.menu_setup);
			
			
			menuSetup = new MenuTitle(recSetup, 0, 0, 0, bitSetup, 0);
			menuAbout = new MenuTitle(recAbout, 0, 533, 800, bitAbout, 1);
			menuPlay = new MenuTitle(recPlay, 0, -533, -800, bitPlay, 2);


//			addWall(465,270,10,200);
		
			Platform p = new Platform(new Vec2d(700000, 120000), new Vec2d(50000, 10000));
			p.addStep(-200000, 0);
			p.addStep(200000,426000);
//			p.addStep(200000, 0);
			
			
			//gameOverlay.addWall(_wall2);
			
//			Log.d("GSTA", "Wall test : " + gameOverlay.testWall(0, 0, _wall9));
//			Log.d("GSTA", "Wall test : " + gameOverlay.testWall(0, 0, _wall5));
//			Log.d("GSTA", "Wall test : " + gameOverlay.testWall(1, 1, _wall5));
			
			//ArrayList returnTest = gameOverlay.findWall(_wall9);
			
//			ArrayList returnTest = gameOverlay.getTile(0, 0);
			
//			if (returnTest.contains(_wall9)) {
//				Log.d("GSTA", "Wall9 is in 0,0");
//			}
			
			
			
			
		}
		
		public void setRunning(boolean run) {
			mRun = run;
		}
		
     //for consistent rendering
        //amount of time to sleep for (in milliseconds)
        BigInteger StartTime;
    //This is my main loop, runs as fast as it can possibly go!
		@Override
		public void run() {
			Vec2d offset = new Vec2d();
			GameObject.offset = offset;
			Canvas c = null;
			UserInput.Input currentInput;

//			try {
//				sleep(250);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
////				e1.printStackTrace();
//			}
			while (mRun) {
				if (true) pause();
				
				
//				gameTIME += 1;
//				
//				
//				
//				currentInput = _input.getInput();
//				hero.processInput(currentInput);
//				hero.collisionAvoid();
//				MovingObject.updateAll();
				
				try {
					c = _surfaceHolder.lockCanvas(null);
					synchronized (_surfaceHolder) {
						clearScreen(c);
						offset.x = hero.pos.x - (long)(getWidth() / 2 * 1000);
						offset.y = hero.pos.y - (long)(getHeight() / 2 * 1000);
						GameObject.drawAll(c);
					}
                }
				finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
			}
		}
		
		
		public void pause() {
			Canvas c = null;

			while (mRun) {
				try {
					c = _surfaceHolder.lockCanvas(null);
					synchronized (_surfaceHolder) {
//						Log.d("GSTA", "clearScreens");
						clearScreen(c);
//						Log.d("GSTA", "drawMenu");
						showTitleScreen(c);
					}
                }
				finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
			}
		}
		
		
		public void showTitleScreen(Canvas c) {
			Input currentInput = _input.getInput();
			titleMenu.processInput(currentInput);
			titleMenu.update();
			titleMenu.drawPanels(c);
			
//			MenuTitle.drawMenus(c);
		}
		
		
		public void drawMenu(Canvas c) {
			int height = getHeight();
			int width = getWidth();
			Rect rec = new Rect (0,0, width, height);
			Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
			backgroundPaint.setColor(Color.RED);
			c.drawRect(rec, backgroundPaint);	
			c.drawBitmap(bitHero, 50, 50, null);

			
		}
		
		public void transition(UserInput.Input input) {
			
			if (!MenuTitle.inTransition) {
				Log.d("GSTA", "Not in Transition");
				if (MenuTitle.activePanel==menuPlay) {
					Log.d("GSTA", "Play - Active");
					if (input==UserInput.Input.PRESS_RIGHT) MenuTitle.activePanel = null;
				} else if (MenuTitle.activePanel==menuAbout) {
					Log.d("GSTA", "About - Active");
					if (input==UserInput.Input.PRESS_LEFT) MenuTitle.activePanel = null;
				} else if (MenuTitle.activePanel==menuSetup) {
					Log.d("GSTA", "Setup - Active");
					if (input==UserInput.Input.PRESS_MIDDLE) MenuTitle.activePanel = null;
				} else {
					
					Log.d("GSTA", "Null - Active");
					if (input==UserInput.Input.PRESS_MIDDLE) MenuTitle.activePanel = menuSetup;
					if (input==UserInput.Input.PRESS_LEFT) MenuTitle.activePanel = menuPlay;
					if (input==UserInput.Input.PRESS_RIGHT) MenuTitle.activePanel = menuAbout;
				}
			} else Log.d("GSTA", "In Transition");
		}
		
		public void drawTitleScreen(Canvas c){
	//			MenuSystem.activeMenu=null;
			MenuTitle.drawMenus(c);
		}
		
		
		public void clearScreen(Canvas c){
			synchronized (_surfaceHolder) {
				int height = getHeight();
				int width = getWidth();
				Rect rec = new Rect (0,0, width, height);
				Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
				backgroundPaint.setColor(Color.DKGRAY);
				c.drawRect(rec, backgroundPaint);
			}
		}



	}
}



