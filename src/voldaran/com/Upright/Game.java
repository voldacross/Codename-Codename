package voldaran.com.Upright;


import java.io.IOException;
import java.io.InputStream;

import voldaran.com.Upright.UserInput.Input;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
    
	public int gameTIME = 0;
	
	public static Context mContext;
	
	public UserInput _input;
	private PauseMenu pause;
	public Vec2d surfaceSize = new Vec2d();
	public Vec2d cameraSize;
	
	public static DisplayMetrics displayMetrics = new DisplayMetrics();;
	
	
	public enum GameState {
		TITLE,
		PLAYING,
		PAUSED,
		LEVEL_COMPLETE
	}
	
	public GameState gameState;
	
	public Game(Context context, DisplayMetrics d) {
		super(context);
		getHolder().addCallback(this);
		setFocusable(true);
		Game.mContext = context;
		Game.displayMetrics = d;
		
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
	
	//Static Helper Class
	public static Bitmap loadBitmapAsset(String asset) {
		AssetManager assets = mContext.getAssets();
		InputStream stream = null;

		 
     	try {
            stream = assets.open("gfx/" + asset);
        } catch (IOException e) {
            Log.d("GSTA", "EXCEPTION!" + e.getMessage());
        }
	        
        Drawable drawable = BitmapDrawable.createFromStream(stream, "src");
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
	        
		return bitmap;
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
			new Wall(new Vec2d(posx, posy).mul(1000), new Vec2d(extentx, extenty).mul(1000));
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
			bitHero = Game.loadBitmapAsset("meatwad.png");
			
			Log.d("GSTA", "" + bitHero.getHeight() + "," + bitHero.getWidth());
			
			hero = new GameHero(new Vec2d(224000,96000), new Vec2d(bitHero.getWidth() / 6 * 1000,bitHero.getHeight() / 6 * 1000), bitHero);
			
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
			
//			addWall(356,220,4,4);
			
			//Level 1
//			addWall(160,408,64,4);
//			addWall(248,352,4,64);
//			addWall(184,304,4,48);
//			addWall(232,232,56,4);
//			addWall(552,248,56,4);
//			addWall(488,200,4,56);
//			addWall(568,136,4,56);
//			addWall(552,72,24,4);
//			addWall(464,72,48,4);
//
//			addWall(336,80,16,16);
//			addWall(368,80,16,16);
//			addWall(336,112,16,16);
//			addWall(368,112,16,16);
//			
//			addWall(336,176,16,16);
//			addWall(368,176,16,16);
//			addWall(368,208,16,16);
//			addWall(336,208,16,16);
//			
//			addWall(336,272,16,16);
//			addWall(368,272,16,16);
//			addWall(336,304,16,16);
//			addWall(368,304,16,16);
//			
//			addWall(336,368,16,16);
//			addWall(368,368,16,16);
//			addWall(336,400,16,16);
//			addWall(368,400,16,16);

			//level 2
//			addWall(136,248,72,8);
//			addWall(216,304,8,64);
//			addWall(192,440,80,8);
//			addWall(120,376,8,56);
//			
//			addWall(552,280,72,8);
//			addWall(472,224,8,64);
//			addWall(568,152,8,56);
//			addWall(496,88,80,8);
//			addWall(344,104,24,24);
//			addWall(344,424,24,24);
//			addWall(344,360,24,24);
//			addWall(344,296,24,24);
//			addWall(344,232,24,24);
//			addWall(344,168,24,24);

			
			//Level 3
//			addWall(752,376,16,8);
//			addWall(760,40,24,8);
//			addWall(592,40,80,8);
//			addWall(656,264,48,168);
//			addWall(584,440,24,8);
//			addWall(416,440,80,8);
//			addWall(392,296,24,88);
//			addWall(288,280,32,8);
//			addWall(208,88,48,8);
//			addWall(72,88,40,8);
//			addWall(32,216,16,8);
//			addWall(112,216,32,8);
//			addWall(168,256,24,48);
//			addWall(248,320,8,112);
//			addWall(216,440,24,8);
//			addWall(96,440,48,8);

			
			//Level 4
			
//			addWall(208,96,16,64);
//			addWall(160,112,16,80);
//			addWall(392,384,8,64);
//			addWall(272,328,32,8);
//			addWall(184,312,24,8);
//			addWall(72,408,24,8);
//			addWall(104,344,8,72);
//			addWall(152,280,40,8);
//			addWall(360,280,104,8);
//			addWall(472,208,8,80);
//			addWall(392,88,8,24);
//			addWall(392,120,88,8);
//			addWall(296,152,8,40);
//			addWall(352,200,64,8);
//			addWall(592,216,48,8);
			
			//Level 5
			
//			addWall(488,232,8,152);
//			addWall(408,72,88,8);
//			addWall(152,336,8,64);
//			addWall(448,136,48,8);
//			addWall(408,256,8,64);
//			addWall(320,328,96,8);
//			addWall(184,264,104,8);
//			addWall(72,192,8,80);
//			addWall(112,104,48,8);
//			addWall(216,200,88,8);
//			addWall(312,136,8,72);
//			addWall(248,56,72,8);
//			addWall(168,88,8,40);
//			addWall(208,136,48,8);
			
			//Maze 1
//			hero.pos.set(32000,48000);
//			addWall(792,240,8,240);
//			addWall(728,272,8,32);
//			addWall(600,312,40,8);
//			addWall(648,344,8,40);
//			addWall(696,424,8,40);
//			addWall(624,392,64,8);
//			addWall(488,432,8,32);
//			addWall(416,392,80,8);
//			addWall(416,312,80,8);
//			addWall(344,264,8,40);
//			addWall(408,272,8,32);
//			addWall(568,232,168,8);
//			addWall(568,192,8,32);
//			addWall(672,152,112,8);
//			addWall(728,56,8,40);
//			addWall(648,56,8,40);
//			addWall(568,48,8,32);
//			addWall(536,88,40,8);
//			addWall(488,120,8,40);
//			addWall(408,112,8,32);
//			addWall(344,112,8,32);
//			addWall(344,152,72,8);
//			addWall(264,88,8,72);
//			addWall(264,312,8,88);
//			addWall(176,312,80,8);
//			addWall(96,392,80,8);
//			addWall(88,280,8,40);
//			addWall(96,232,80,8);
//			addWall(216,88,40,8);
//			addWall(136,152,40,8);
//			addWall(88,88,8,72);
//			addWall(8,232,8,232);
//			addWall(432,8,352,8);
//			addWall(352,472,352,8);
			
			
			//New Game Level 2
//			hero.pos.set(32000,48000);
//			addWall(320,392,64,8);
//			addWall(600,80,8,64);
//			addWall(504,188,8,36);
//			addWall(456,152,56,8);
//			addWall(392,240,8,224);
//			addWall(392,232,376,8);
//			addWall(392,8,376,8);
//			addWall(8,232,8,232);
//			addWall(392,472,392,8);
//			addWall(776,232,8,232);
			
			
			//New Game Level 3
			hero.pos.set(32000,48000);
			addWall(592,200,80,8);
			addWall(424,248,8,104);
			addWall(352,360,80,8);
			addWall(144,424,48,8);
			addWall(200,424,8,56);
			addWall(104,360,104,8);
			addWall(136,56,56,8);
			addWall(184,96,8,32);
			addWall(72,64,8,80);
			addWall(328,64,8,64);
			addWall(208,136,128,8);


			
			
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
		
		public void drawFPS(Canvas c) {
			
			previousTime = currentTime;
  		    currentTime = System.currentTimeMillis();
  		    long ft = ((currentTime - previousTime));
  		    
  		    sft = (long) (sft * 0.9 + ft * 0.1);
  		    
  		    Paint text = new Paint();
  		    text.setColor(Color.GREEN);
  		    text.setTextSize(16);
  		    text.setTypeface(Typeface.MONOSPACE);
  		    
  		    c.drawText(String.valueOf(sft), 20, 24, text);
//  		    Log.d("FPS", "" + String.valueOf(sft));
  		    c.drawText("" + hero.getToggleCount(), 750, 30, text);
		}
		
//		public void drawPress(Canvas c, Input input) {
//			
//			Vec2d down = _input.getDown();
//			
//			if (!down.isVoid()) {
//				Vec2d current = _input.getCurrent();
//				
//				Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
//				paint.setColor(Color.RED);
//				paint.setAlpha(50);
//				
//				Path triPath = new Path();
//				Paint triPaint = new Paint();
//				triPaint.setColor(Color.WHITE);
//				triPaint.setAlpha(80);
//				triPaint.setStyle(Paint.Style.FILL);
//				
//				if (input==Input.PRESS_LEFT||input==Input.PRESS_LEFT_DOWN||input==Input.PRESS_LEFT_UP) {
//					triPath.moveTo(100, 50);
//					
//					triPath.lineTo(200, 50);
//					triPath.lineTo(200, 83);
//					triPath.lineTo(100, 83);  //Create left Arrow
//					
//					triPath.lineTo(100, 16);
//					triPath.lineTo(50,67);
//					triPath.lineTo(100,113);
//					triPath.close();
//					
//				} else if (input==Input.PRESS_RIGHT||input==Input.PRESS_RIGHT_DOWN||input==Input.PRESS_RIGHT_UP) {
//					
//					triPath.moveTo(700, 50);
//					
//					triPath.lineTo(600, 50);
//					triPath.lineTo(600, 83);
//					triPath.lineTo(700, 83); //Create right Arrow
//					
//					triPath.lineTo(700, 16);
//					triPath.lineTo(750,67);
//					triPath.lineTo(700,113);
//					triPath.close();
//				}
//				
//				c.drawPath(triPath, triPaint);
//				paint.setAlpha(150);
//				c.drawCircle(down.x, down.y, 13, paint);
//				c.drawCircle(current.x, current.y, 7, paint);
//				c.drawLine(down.x, down.y, current.x, current.y, paint);
//			}
//			
//		}
		
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
		long sft = 0;
		
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
		
		
		public GameState gameLoop(){
			Vec2d offset = new Vec2d(0,0);
			GameObject.offset = offset;
			UserInput.Input currentInput;
			
			long startTime = System.currentTimeMillis();
			long lastSavedTime = startTime; 
			
  			currentTime = System.currentTimeMillis();
  			while((gameState == GameState.PLAYING)  && (mRun)){
	  		    
				currentInput = _input.getInput();
				
				Vec2d mClicked = _input.getCurrentPress();
				
				hero.processInput(currentInput);
				hero.collisionAvoid();
				MovingObject.updateAll();
				

				Picture picScreen = new Picture();
				Canvas c = picScreen.beginRecording((int) cameraSize.x, (int) cameraSize.y);
				
				synchronized (_surfaceHolder) {
					clearScreen(c, cameraSize);
					GameObject.drawAll(c);
					
//					drawPress(c, currentInput);
					drawFPS(c);
					
					picScreen.endRecording();
					drawToScreen(picScreen);
				}

				if(hero.dead){
					death();
				}else if (currentTime>lastSavedTime+25000){			//Every ten Seconds save checkpoint
					if(GameObject.saveCheckpointAll())
						lastSavedTime = currentTime;
				}

				if(!mClicked.isVoid()) //Pause
					if ((mClicked.x>700)&&(mClicked.y<100)) {
						pause.createPause(picture, hero.pos, mapSize);
						gameState = GameState.PAUSED;
					}
			}
			return gameState;
		}
		
		public void death(){
			float alpha = 255;
			float fade = 0;
			int height = (int) cameraSize.y;
			int width = (int) cameraSize.x;

			GameObject.restoreCheckpointAll();
			while(mRun && alpha > 50){
				Picture picScreen = new Picture();
				Canvas c = picScreen.beginRecording((int) cameraSize.x, (int) cameraSize.y);
				
				synchronized (_surfaceHolder) {
					clearScreen(c, cameraSize);
					GameObject.drawAll(c);
					Rect rec = new Rect (0,0, width, height);
					Paint whitePaint = new Paint();
					whitePaint.setColor(Color.WHITE);
					whitePaint.setAlpha((int) alpha);
					c.drawRect(rec, whitePaint);
					picScreen.endRecording();
					drawToScreen(picScreen);
				}
				fade += 0.6;
				alpha -= fade;
				
			}
		}
		
		public GameState titleScreen() {
			createTitleScreen();
			
			Picture picScreen = new Picture();
			
			while((gameState == GameState.TITLE) && (mRun)){
				
				UserInput.Input currentInput = _input.getInput();
//				Log.d("GSTA", "INPUT + " + currentInput);
				
				Vec2d mClicked = _input.getCurrentPress();
				if (!mClicked.isVoid()) titleMenu.processInput(currentInput, mClicked);
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
				if (!mClicked.isVoid()) 
					if ((mClicked.x>750)&&(mClicked.y<50)) {
						Log.d("GSTA", "you clicked to unpause");
						pause.exiting = true;
					}
				
				if (pause.exiting) 
					if (pause.zoomOut()) {
						
						gameState=GameState.PLAYING; //unPause
						_input.Clear(); //Clear inputs
					}
//				pause.Update(_input.mDragVelocity);
				
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





