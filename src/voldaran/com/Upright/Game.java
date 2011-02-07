package voldaran.com.Upright;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
	
	public enum GameState {
		TITLE,
		PLAYING,
		PAUSED,
		LEVEL_COMPLETE
	}
	
	public GameState gameState;
	
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
		boolean retry = false;
		while(retry){
			try{
				thread.join();
				retry = false;
			}catch(InterruptedException e){
				
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
		Log.d("Game.surfaceCreated", "Surface created");
		thread.setRunning(true);
		thread.start();		
	}
	
	//Destroys thread if app is closed, kind of buggy at the moment.
	//Game doesn't force close on shutdown, but doesn't re-open smoothly
	@Override public void surfaceDestroyed(SurfaceHolder holder) {
	}
	
	//GAME THREAD
	class GameThread extends Thread {
		private SurfaceHolder _surfaceHolder;
		private Game game;
		private GameHero hero;
		public boolean mRun = false;
		//public MapClass.TileClass[][] aTile;
		
		public Bitmap bitHero;
		
		private MenuTitleScreen titleMenu;
		
//		private ArrayList<Walls> _walls = new ArrayList<Walls>();
		
		public void addWall(int posx, int posy, int extentx, int extenty){
			new GameObject(new Vec2d(posx, posy).mul(1000), new Vec2d(extentx, extenty).mul(1000));
		}
		
		
		public void loadLevel() {
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
//			addWall(465,270,10,200);
		
			Platform p = new Platform(new Vec2d(700000, 120000), new Vec2d(50000, 10000));
			p.addStep(-200000, 0);
			p.addStep(200000,426000);
//			p.addStep(200000, 0);
		}
		
		public GameThread(SurfaceHolder surfaceHolder, Game game) {
			
			_surfaceHolder = surfaceHolder;
			this.game = game;
			
			titleMenu = new MenuTitleScreen(game);
			
			GameObject.gameObjects.clear();
			MovingObject.movingObjects.clear();
			
		//creating a level, added hero and walls.
			
			
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
			gameState = GameState.TITLE;
			while (mRun) {
				switch(gameState){
				case TITLE:gameState = titleScreen();
				case PLAYING:gameState = gameLoop();
				case PAUSED:break;
				case LEVEL_COMPLETE:break;
				}
			}
		}

		public GameState titleScreen() {
			while((gameState == GameState.TITLE) && (mRun)){
				Canvas c = null;
				UserInput.Input currentInput = _input.getInput();
				Vec2d mClicked = _input.getCurrentPress();
				titleMenu.processInput(currentInput, mClicked);
				titleMenu.update();
				try {
					c = _surfaceHolder.lockCanvas(null);
					synchronized (_surfaceHolder) {
						Log.d("gameThread.run", "clear screen");
						clearScreen(c);
						Log.d("gameThread.run", "draw screen");
						titleMenu.drawPanels(c);
					}
					
				}catch(NullPointerException e){
                }
				finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }	
				
				
			}
			return gameState;
		}
		

		public GameState gameLoop(){
			Vec2d offset = new Vec2d();
			GameObject.offset = offset;
			UserInput.Input currentInput;
			while(gameState == GameState.PLAYING){
				Canvas c = null;
				currentInput = _input.getInput();
				hero.processInput(currentInput);
				hero.collisionAvoid();
				MovingObject.updateAll();
				
				try {
					c = _surfaceHolder.lockCanvas(null);
					synchronized (_surfaceHolder) {
						Log.d("gameThread.run", "clear screen");
						clearScreen(c);
						offset.x = hero.pos.x - (long)(getWidth() / 2 * 1000);
						offset.y = hero.pos.y - (long)(getHeight() / 2 * 1000);
						Log.d("gameThread.run", "draw");
						GameObject.drawAll(c);
					}
                }catch(NullPointerException e){
                }
				finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
			}
			return gameState;
		}
		
		public void pause(){
			Vec2d offset = new Vec2d();
			while(mRun){
				Canvas c = null;
				try {
					c = _surfaceHolder.lockCanvas(null);
					synchronized (_surfaceHolder) {
						Log.d("gameThread.run", "clear screen");
						clearScreen(c);
						offset.x = hero.pos.x - (long)(getWidth() / 2 * 1000);
						offset.y = hero.pos.y - (long)(getHeight() / 2 * 1000);
						Log.d("gameThread.run", "draw");
						GameObject.drawAll(c);
					}
                }catch(NullPointerException e){
                }
				finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
				
			}
		}
		
		public void clearScreen(Canvas c){
			int height = getHeight();
			int width = getWidth();
			Rect rec = new Rect (0,0, width, height);
			Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
			backgroundPaint.setColor(Color.DKGRAY);
			c.drawRect(rec, backgroundPaint);
		}
	}
}



