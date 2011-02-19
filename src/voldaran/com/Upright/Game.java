package voldaran.com.Upright;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
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
	public GameThread thread;
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
	
	public UserInput _input;
	private PauseMenu pause;
	public Vec2d surfaceSize = new Vec2d();
	public Vec2d cameraSize;
	
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
		
		//Create Camera Size
		
		cameraSize = new Vec2d(800,480);
		pause = new PauseMenu(cameraSize);
		_input = new UserInput(cameraSize, surfaceSize);
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
	
	//creates a bundle with all valued gamestate information.
	// current GameState
	// current level
	// hero/platform/item position
	
	public Bundle saveGameBundle() {
		Bundle saveGame = new Bundle();
		saveGame.putInt("GAME_STATE", gameState.ordinal());
		return saveGame;
	}
	

	//Receives savedgame Bundle and sets game
	public void resumeGameBundle(Bundle resumeGame) {
		
		gameState=GameState.values()[resumeGame.getInt("GAME_STATE")];
		
		if (gameState==GameState.PLAYING) thread.loadLevel();
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

	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("GSTA", "surfaceCreated");
		surfaceSize.set(getWidth(), getHeight());
		thread.setRunning(true);
		thread.start();
		
	}
	
	@Override public void surfaceDestroyed(SurfaceHolder holder) {
	}
	
	//GAME THREAD
	class GameThread extends Thread {
		private SurfaceHolder _surfaceHolder;
		private GameHero hero;
		public boolean mRun = false;
		private MenuTitleScreen titleMenu;
		
		Vec2d mapSize;
		//public MapClass.TileClass[][] aTile;
		
		public Bitmap bitHero, bitArrow;
		
//		private ArrayList<Walls> _walls = new ArrayList<Walls>();
		
		public void addWall(int posx, int posy, int extentx, int extenty){
			new GameObject(new Vec2d(posx, posy).mul(1000), new Vec2d(extentx, extenty).mul(1000));
		}
		
		public void addObstacle(int posx, int posy, int extentx, int extenty){
			new GameObstacle(new Vec2d(posx, posy).mul(1000), new Vec2d(extentx, extenty).mul(1000));
		}
		
		//Loads a level, still needs a function to parse level file and load correct level
		public void loadLevel() {
			
			//loading new level
			
			//Clear out old level if present
			GameObject.gameObjects.clear();
			MovingObject.movingObjects.clear();
			
			
			//Set map size
			
			mapSize = new Vec2d(10000,1000);
			
			//Create hero
			bitHero = BitmapFactory.decodeResource(getResources(),R.drawable.meatwad2);
//			bitArrow = BitmapFactory.decodeResource(getResources(),R.drawable.arrow);
			
			Log.d("GSTA", "" + bitHero.getHeight() + "," + bitHero.getWidth());
			
			hero = new GameHero(new Vec2d(300000,30000), new Vec2d(bitHero.getWidth() / 6 * 1000,bitHero.getHeight() / 6 * 1000), bitHero);
			
			hero.setGame(this);
			Log.d("GSTA", "hero : " + hero.pos);
			Log.d("GSTA", "hero top, left, bottom, right : " + (hero.pos.x - hero.extent.x) + "," 
					                                         + (hero.pos.y - hero.extent.y) + ","
					                                         + (hero.pos.x + hero.extent.x) + ","
					                                         + (hero.pos.y + hero.extent.y));

			//Top
			
			//x,y, extentx, extenty

			//Borders
//			addWall(4,240,4,232);
//			addWall(392,4,400,4);
//			addWall(372,476,428,4);
//			addWall(796,232,4,240);
			
			
			//Boarders //Obstacles
			addObstacle(4,240,4,232);
			addObstacle(392,4,400,4);
			addObstacle(372,476,428,4);
			addObstacle(796,232,4,240);
			
			addWall(356,220,4,4);
			addWall(160,408,64,8);
			addWall(248,352,8,64);
			addWall(184,304,8,48);
			addWall(232,232,56,8);
			addWall(552,248,56,8);
			addWall(488,200,8,56);
			addWall(568,136,8,56);
			addWall(552,72,24,8);
			addWall(464,72,48,8);

			addWall(336,80,16,16);
			addWall(368,80,16,16);
			addWall(336,112,16,16);
			addWall(368,112,16,16);
			addWall(336,176,16,16);
			addWall(368,176,16,16);
			addWall(368,208,16,16);
			addWall(336,208,16,16);
			addWall(336,272,16,16);
			addWall(368,272,16,16);
			addWall(336,304,16,16);
			addWall(368,304,16,16);
			addWall(336,368,16,16);
			addWall(368,368,16,16);
			addWall(336,400,16,16);
			addWall(368,400,16,16);
			
			
//			Platform p = new Platform(new Vec2d(500000, 100000), new Vec2d(50000, 10000));
//			p.addStep(-250000, 0);
//			
//			Platform p2 = new Platform(new Vec2d(740000, 100000), new Vec2d(50000, 10000));
//			p2.addStep(0, 400000);

		}
		
		public Game mGame;
		
		public GameThread(SurfaceHolder surfaceHolder, Game game) {
			_surfaceHolder = surfaceHolder;
			mGame = game;
			
		}
		
		public void setRunning(boolean run) {
			mRun = run;
		}
		
		public void drawAd(Canvas c) {
			
			Rect ad = new Rect(0, 0, 320, 50);
			Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
			backgroundPaint.setColor(Color.YELLOW);
			c.drawRect(ad, backgroundPaint);
		}
		
		public void createTitleScreen() {
			
			titleMenu = new MenuTitleScreen(mGame); //Has to be created after the surfaceView has been created
		}
		

		Long previousTime;
		Long currentTime;	
		
    //This is my main loop, runs as fast as it can possibly go!
		@Override
		public void run() {
			gameState = GameState.TITLE;
			while (mRun) {
				switch(gameState){
				case TITLE:gameState = titleScreen();
				case PLAYING:gameState = gameLoop();
				case PAUSED:gameState = pause();
				case LEVEL_COMPLETE:break;
				}
			}
		}

	boolean restoreCheckpoint = false;
		
		public GameState gameLoop(){
			Vec2d offset = new Vec2d(0,0);
			GameObject.offset = offset;
			UserInput.Input currentInput;
			
			long startTime = System.currentTimeMillis();
			Long lastSavedTime = startTime; 
			currentTime = System.currentTimeMillis();
			
			long sft = 0;
  			currentTime = System.currentTimeMillis();
  			while((gameState == GameState.PLAYING)  && (mRun)){
  		    
	  		    previousTime = currentTime;
	  		    currentTime = System.currentTimeMillis();
	  		    long ft = ((currentTime - previousTime));
	  		    
	  		    sft = (long) (sft * 0.9 + ft * 0.1);
	  		    
	  		    Log.d("FPS", "" + String.valueOf(sft));
			
				currentInput = _input.getInput();
				Vec2d mClicked = _input.getCurrentPress();
				
				hero.processInput(currentInput);
				hero.collisionAvoid();
				MovingObject.updateAll();
				

				//Every ten Seconds save checkpoint
				
				if (currentTime>lastSavedTime+10000) {
					lastSavedTime = currentTime;
					Log.d("GSTA", "Saving!");
					GameObject.saveCheckpointAll();
				}
				
				Picture picScreen = new Picture();
				Canvas c = picScreen.beginRecording((int) cameraSize.x, (int) cameraSize.y);
				
				synchronized (_surfaceHolder) {
					clearScreen(c, cameraSize);
//					offset.x = hero.pos.x - (long)(cameraSize.x / 2 * 1000);
//					offset.y = hero.pos.y - (long)(cameraSize.y / 2 * 1000);
					GameObject.drawAll(c);
//					drawAd(c);
					
					Checkpoint.Update(c, cameraSize);  //Saves gamestate for Checkpoint
					
					picScreen.endRecording();
					drawToScreen(picScreen);
//				}
				

				}
				
				if (mClicked!=null) //Pause
					if ((mClicked.x>700)&&(mClicked.y<100)) {
						pause.createPause(picture, hero.pos, mapSize);
						gameState=GameState.PAUSED;
						break;
					}
			}
			return gameState;
		}
		
		
		public GameState titleScreen() {
			createTitleScreen();
			
			Picture picScreen = new Picture();
			
			while((gameState == GameState.TITLE) && (mRun)){
				
				UserInput.Input currentInput = _input.getInput();
				Vec2d mClicked = _input.getCurrentPress();
				titleMenu.processInput(currentInput, mClicked);
				titleMenu.update();
				
					synchronized (_surfaceHolder) {
						titleMenu.drawPanels(picScreen.beginRecording((int) cameraSize.x, (int) cameraSize.y));
						picScreen.endRecording();
						drawToScreen(picScreen);
					}
					
			}
			return gameState;
		}
		
		Bitmap pausedState;
		Picture picture = new Picture();
		
		//TODO Add button checks
		//Buttons: 	Return to Title Screen
		//			Resume Game
		//			Reload board
		//			Maybe Level Select
		
		public GameState pause(){
			
			Picture picScreen = new Picture();
			
			while((gameState == GameState.PAUSED) && (mRun)){
//				Canvas c = null;
				Vec2d mClicked = _input.getCurrentPress();
				if (mClicked!=null) 
					if ((mClicked.x>750)&&(mClicked.y<50)) {
						Log.d("GSTA", "you clicked to unpause");
						pause.exiting = true;
					}
				
				if (pause.exiting) 
					if (pause.zoomOut()) {
						
						gameState=GameState.PLAYING; //unPause
						_input.Clear(); //Clear inputs
					}
				pause.Update(_input.mDragVelocity);
				
					synchronized (_surfaceHolder) {

						pause.Draw(picScreen.beginRecording((int) cameraSize.x, (int) cameraSize.y));
						
						picScreen.endRecording();
						drawToScreen(picScreen);
						
					}
				
				
				if (mClicked!=null) //Return to titleScreen
					if ((mClicked.x<50)&&(mClicked.y<50)) {
						Log.d("GSTA", "you clicked to return to title");
						gameState = GameState.TITLE;
					}
				
			}
			
			return gameState;
		}
		
		public void drawToScreen(Picture pic){
			Canvas actual = null;
			
			try {
				actual = _surfaceHolder.lockCanvas(null);
				synchronized (_surfaceHolder) {
					Rect rectSurface = new Rect(0,0,(int) surfaceSize.x,(int) surfaceSize.y);
					
					clearScreen(actual, surfaceSize);
					actual.drawPicture(pic, rectSurface);
				}
            }catch(NullPointerException e){
            }
			finally {
                if (actual != null) {
                    _surfaceHolder.unlockCanvasAndPost(actual);
                }
            }
			
		}
		
		public void clearScreen(Canvas c, Vec2d size){
			int height = (int) size.y;
			int width = (int) size.x;
			Rect rec = new Rect (0,0, width, height);
			Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
			backgroundPaint.setColor(Color.BLACK);
			c.drawRect(rec, backgroundPaint);
		}
	}
}





