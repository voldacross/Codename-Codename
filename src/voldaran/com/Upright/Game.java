package voldaran.com.Upright;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

public class Game extends SurfaceView implements SurfaceHolder.Callback {
	public GameThread thread;
    
	public int gameTIME = 0;
	
	public static Context mContext;
	
	public UserInput _input;
	private PauseMenu pause;
	public static Vec2d surfaceSize = new Vec2d();
	public static Vec2d cameraSize;
	
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
		
		Game.cameraSize = new Vec2d(800,480);
		pause = new PauseMenu(Game.cameraSize);
		_input = new UserInput(Game.cameraSize, Game.surfaceSize);
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
		thread.createTitleScreen();
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
		
//		private ArrayList<Walls> _walls = new ArrayList<Walls>();
		
		public void loadLevel() {
			//Called without a level, load current level. TODO 
			if (currentLevel=="") {
				loadLevel("level3.txt"); 
			} else {
				loadLevel(currentLevel);
			}
					
		}
		
		public String currentLevel = "";
		
		public Bitmap loadLeveltoBitmap(String levelAsset) {
			//loading new level
			
			//Clear out old level if present
			GameObject.gameObjects.clear();
			MovingObject.movingObjects.clear();
			
			GameObstacle.addObstacle(4,240,4,232);
			GameObstacle.addObstacle(392,4,400,4);
			GameObstacle.addObstacle(372,476,428,4);
			GameObstacle.addObstacle(796,232,4,240);
			
			InputStream stream = null;
			//Parse Level file
			try{
				AssetManager assets = mContext.getAssets();
	            stream = assets.open("level/" + levelAsset);
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 4096);
				String rline, line;
				GameObject o;
				while((rline = reader.readLine()) != null){
					line = rline.replaceAll("\\) *extent", ",")
						   	    .replaceAll(" |\\(|\\)|:|pos|extent", "")
						        .toLowerCase();
					if (line.startsWith("wall")){
						o = (GameObject) Wall.fromString(line.substring(4));
						Log.d("LoadLevel", o.toString());
					}
					else if (line.startsWith("hero")) {
						hero = GameHero.fromString(line.substring(4));
						Log.d("LoadLevel", hero.toString());
					}
					else Log.d("Load", rline);
				}
			}
			catch (Exception e){
				e.printStackTrace();
				Log.d("LoadLevel", "error - " + e);
			}
			finally{
				if(stream != null)
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
						Log.d("LoadLevel", "error - " + e);
					}
			}
			Vec2d offset = new Vec2d(0,0);
			GameObject.offset = offset;
			Bitmap previewLevel = Bitmap.createBitmap((int) Game.cameraSize.x / 4, (int) Game.cameraSize.y / 4, Bitmap.Config.ARGB_8888);
			
			Canvas c = new Canvas(previewLevel);
			
			Rect rec = new Rect (0,0, (int) Game.cameraSize.x / 4, (int) Game.cameraSize.y / 4);
			Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
			backgroundPaint.setColor(Color.BLACK);
			c.drawRect(rec, backgroundPaint);
			
			GameObject.drawAllPreview(c);
			
			return previewLevel;
		}
		
		
		//Loads a level
		public void loadLevel(String levelAsset) {
			//loading new level
			currentLevel = levelAsset;
			
			//Clear out old level if present
			GameObject.gameObjects.clear();
			MovingObject.movingObjects.clear();
			
			//Set map size
			mapSize = new Vec2d(10000,1000);
			
			//Boarders //Obstacles
			GameObstacle.addObstacle(4,240,4,232);
			GameObstacle.addObstacle(392,4,400,4);
			GameObstacle.addObstacle(372,476,428,4);
			GameObstacle.addObstacle(796,232,4,240);
			
			InputStream stream = null;
			//Parse Level file
			try{
				AssetManager assets = mContext.getAssets();
	            stream = assets.open("level/" + levelAsset);
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 4096);
				String rline, line;
				GameObject o;
				while((rline = reader.readLine()) != null){
					line = rline.replaceAll("\\) *extent", ",")
						   	    .replaceAll(" |\\(|\\)|:|pos|extent", "")
						        .toLowerCase();
					if (line.startsWith("wall")){
						o = (GameObject) Wall.fromString(line.substring(4));
						Log.d("LoadLevel", o.toString());
					}
					else if (line.startsWith("hero")) {
						hero = GameHero.fromString(line.substring(4));
						Log.d("LoadLevel", hero.toString());
					}
					else Log.d("Load", rline);
				}
			}
			catch (Exception e){
				e.printStackTrace();
				Log.d("LoadLevel", "error - " + e);
			}
			finally{
				if(stream != null)
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
						Log.d("LoadLevel", "error - " + e);
					}
			}
		}
		
		public Game mGame;
		private Bitmap pauseButton;
		
		public GameThread(SurfaceHolder surfaceHolder, Game game) {
			_surfaceHolder = surfaceHolder;
			mGame = game;
			pauseButton = Game.loadBitmapAsset("pause_button.png");
			
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
  		    c.drawText("" + hero.getToggleCount(), 700, 30, text);
		}
		
		private void drawPause(Canvas c) {
			c.drawBitmap(pauseButton, Game.cameraSize.x - pauseButton.getWidth() - 15, 15, null);
		}
		
		public void drawPress(Canvas c, Input input) {
			
			Vec2d down = _input.getDown();
			Vec2d mClicked = new Vec2d(_input.getCurrent());
			if ((!down.isVoid()) && !((mClicked.x>700)&&(mClicked.y<100))){
				
				Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
				paint.setColor(Color.RED);
				paint.setAlpha(50);
				
				Path triPath = new Path();
				Paint triPaint = new Paint();
				triPaint.setColor(Color.WHITE);
				triPaint.setAlpha(80);
				triPaint.setStyle(Paint.Style.FILL);
				
				if (input==Input.DOWN_LEFT) {
					if (hero.ground!=null) {
						if (hero.findGround()==3) triPaint.setColor(Color.RED);
					} else triPaint.setColor(Color.RED);
					triPath.moveTo(Game.cameraSize.x/2, Game.cameraSize.y/2);
					triPath.lineTo(0, 0);
					triPath.lineTo(0, cameraSize.y);
					triPath.lineTo(Game.cameraSize.x/2, Game.cameraSize.y/2);
					triPath.close();
					
				} else if (input==Input.DOWN_RIGHT) {
					if (hero.ground!=null) {
						if (hero.findGround()==1) triPaint.setColor(Color.RED);
					} else triPaint.setColor(Color.RED);
					
					triPath.moveTo(Game.cameraSize.x/2, Game.cameraSize.y/2);
					triPath.lineTo(Game.cameraSize.x, 0);
					triPath.lineTo(Game.cameraSize.x, Game.cameraSize.y);
					triPath.lineTo(Game.cameraSize.x/2, Game.cameraSize.y/2);
					triPath.close();
				} else if (input==Input.DOWN_UP) {
					if (hero.ground!=null) {
						if (hero.findGround()==2) triPaint.setColor(Color.RED);
					} else triPaint.setColor(Color.RED);
					
					triPath.moveTo(Game.cameraSize.x/2, Game.cameraSize.y/2);
					triPath.lineTo(0,0);
					triPath.lineTo(Game.cameraSize.x,0);
					triPath.lineTo(Game.cameraSize.x/2, Game.cameraSize.y/2);
					triPath.close();
				} else if (input==Input.DOWN_DOWN) {
					if (hero.ground!=null) {
						if (hero.findGround()==4) triPaint.setColor(Color.RED);
					} else triPaint.setColor(Color.RED);
					
					triPath.moveTo(Game.cameraSize.x/2, Game.cameraSize.y/2);
					triPath.lineTo(0,Game.cameraSize.y);
					triPath.lineTo(Game.cameraSize.x,Game.cameraSize.y);
					triPath.lineTo(Game.cameraSize.x/2, Game.cameraSize.y/2);
					triPath.close();
				}
				triPaint.setAlpha(80);				
				c.drawPath(triPath, triPaint);
//				c.drawCircle(down.x, down.y, 13, paint);
//				c.drawCircle(current.x, current.y, 7, paint);
//				c.drawLine(down.x, down.y, current.x, current.y, paint);
			}
			
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
				case LEVEL_COMPLETE:gameState = levelComplete();
				}
			}
		}
		
		
		public GameState gameLoop(){
			Vec2d offset = new Vec2d(0,0);
			GameObject.offset = offset;
			UserInput.Input currentInput;
			
  			currentTime = System.currentTimeMillis();
  			while((gameState == GameState.PLAYING)  && (mRun)){
	  		    
				
				
				Vec2d mClicked = _input.getCurrentPress();
				if(!mClicked.isVoid()) //Pause
					if ((mClicked.x>700)&&(mClicked.y<100)) {
						gameState = GameState.PAUSED;
						_input.Clear();
					}
				
				
				currentInput = _input.getInput();
				
				hero.processInput(currentInput);
				hero.collisionAvoid();
				MovingObject.updateAll();
				

				Picture picScreen = new Picture();
				Canvas c = picScreen.beginRecording((int) Game.cameraSize.x, (int) Game.cameraSize.y);
				
				synchronized (_surfaceHolder) {
					clearScreen(c, Game.cameraSize);
					GameObject.drawAll(c);
					
					drawPress(c, currentInput);
					drawFPS(c);
					drawPause(c);
					
					picScreen.endRecording();
					drawToScreen(picScreen);
				}

				if(hero.dead){
					death();
//				}else if (currentTime>lastSavedTime+25000){			//Every ten Seconds save checkpoint
//					if(GameObject.saveCheckpointAll())
//						lastSavedTime = currentTime;
					
				}

				if (GameObject.returnWin()) gameState = GameState.LEVEL_COMPLETE;
			}
			return gameState;
		}
		
		
		public void death(){
			float alpha = 255;
			float fade = 0;
			int height = (int) Game.cameraSize.y;
			int width = (int) Game.cameraSize.x;

			GameObject.restoreCheckpointAll();
			while(mRun && alpha > 50){
				Picture picScreen = new Picture();
				Canvas c = picScreen.beginRecording((int) Game.cameraSize.x, (int) Game.cameraSize.y);
				
				synchronized (_surfaceHolder) {
					clearScreen(c, Game.cameraSize);
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
		
		public GameState levelComplete() {
			
			while((gameState == GameState.LEVEL_COMPLETE) && (mRun)){
				
				Vec2d mClicked = _input.getCurrentPress();

				Picture picScreen = new Picture();
				Canvas c = picScreen.beginRecording((int) Game.cameraSize.x, (int) Game.cameraSize.y);
				
				titleMenu.update();
				
					synchronized (_surfaceHolder) {
						clearScreen(c, Game.cameraSize);
						GameObject.drawAll(c);
						
						Paint text = new Paint();
						    text.setColor(Color.RED);
						    text.setTextSize(86);
						    text.setTypeface(Typeface.DEFAULT_BOLD);
						    
						c.drawText("YOU WON!", 75, 125, text);
						picScreen.endRecording();
						drawToScreen(picScreen);
					}
					
					if (!mClicked.isVoid()) gameState = GameState.TITLE;
					
			}
			return gameState;
		}
		
		public GameState titleScreen() {
//			createTitleScreen();
			
			Picture picScreen = new Picture();
			
			while((gameState == GameState.TITLE) && (mRun)){
				
				UserInput.Input currentInput = _input.getInput();
				
				Vec2d mClicked = _input.getCurrentPress();
				if (!mClicked.isVoid()) titleMenu.processInput(currentInput, mClicked);
				titleMenu.update();
				
					synchronized (_surfaceHolder) {
						titleMenu.drawPanels(picScreen.beginRecording((int) Game.cameraSize.x, (int) Game.cameraSize.y));
						picScreen.endRecording();
						drawToScreen(picScreen);
					}
					
			}
			return gameState;
		}
		
		Bitmap pausedState;
		Picture picture = new Picture();
		
		public GameState pause(){
			Picture picScreen = null;
			Canvas c = null;
			_input.Clear();
			while((gameState == GameState.PAUSED) && (mRun)){
				
				picScreen = new Picture();
				c = picScreen.beginRecording((int) Game.cameraSize.x, (int) Game.cameraSize.y);
				Vec2d mClicked = _input.getCurrentPress();
				if (!mClicked.isVoid()) gameState = pause.onClick(mClicked, mGame);
				
					synchronized (_surfaceHolder) {
						GameObject.drawPause(c, Game.cameraSize);
						drawFPS(c);
						pause.Draw(c);
						picScreen.endRecording();
						drawToScreen(picScreen);
					}
				
				if (gameState!=GameState.PAUSED) _input.Clear();
			}
			
			return gameState;
		}
		
		public void drawToScreen(Picture pic){
			drawToScreen(pic, 0);
		}
		
		public void drawToScreen(Picture pic, float fade){
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





