package voldaran.com.Upright;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import voldaran.com.Upright.UserInput.Input;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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
	public static Context mContext;
	public static Vec2d surfaceSize = new Vec2d();
	public static Vec2d cameraSize;
	public static DisplayMetrics displayMetrics = new DisplayMetrics();;

	public enum GameState {
		TITLE,
		PLAYING,
		PAUSED,
		LEVEL_COMPLETE

	}

	public static GameThread thread;
	public int gameTIME = 0;
	public UserInput _input;
	private PauseMenu pause;
	public static GameState gameState;
	public static String currentLevel = "world1/level104.txt";
	
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
		gameState=GameState.TITLE;
//		if (gameState==GameState.PLAYING) thread.loadLevel();
	}
	
	public void pauseThread() {
		Log.d( "GameThread", "pauseThread");
		thread.setRunning(false);
		
	}
	public void resumeThread() {
		Log.d( "GameThread", "resumeThread");
		thread.setRunning(true);
	}
	
	public void createThread(){
		Log.d( "GameThread", "createThread");
		thread = new GameThread(getHolder(), this);
		startThread();
	}
	
	public void stopThread(){
		Log.d( "GameThread", "stopThread");
		if (thread!=null) {
		thread.setRunning(false);
		thread.active = false;
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
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
	int height) {
		Log.d( "GameThread", "surfaceChanged");
		surfaceSize.set(getWidth(), getHeight());
		
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d( "GameThread", "surfaceCreated");
		surfaceSize.set(getWidth(), getHeight());
		
	}
	
	public void startThread() {
		thread.createTitleScreen();
		thread.setRunning(true);
		thread.active = true;
		thread.start();
	}
	
	public void resumeStartThread() {
		
		
	}
	
	
	@Override public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d( "GameThread", "surfaceDestoyed");
		stopThread();
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
		public boolean mRun = false;
		private MenuTitleScreen titleMenu;
		
		Vec2d mapSize;
		//public MapClass.TileClass[][] aTile;
		
//		private ArrayList<Walls> _walls = new ArrayList<Walls>();
		

		
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
				while((rline = reader.readLine()) != null){
					line = rline.replaceAll("\\) *extent", ",")
						   	    .replaceAll(" |\\(|\\)|:|pos|extent", "")
						        .toLowerCase();
					parseLine(line, false);
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
		
		public void loadLevel() {
			if (currentLevel=="") {
				loadLevel("level3.txt", true); 
			} else {
				loadLevel(currentLevel, true);
			}
					
		}
		
		public void loadLevel(String level, boolean asset){
			if(asset){
				currentLevel = level;
				try{
					InputStream stream = mContext.getAssets().open("level/" + level);
					loadLevel(stream);
				}catch (Exception e){
					e.printStackTrace();
					Log.d("LoadLevel", "error - " + e);
				}
			}else loadLevel(level);
		}
		
		public void loadLevel(String level){
			currentLevel = level;
			try{
				InputStream stream = mContext.openFileInput(level);
				loadLevel(stream);
			}catch (Exception e){
				e.printStackTrace();
				Log.d("LoadLevel", "error - " + e);
			}
		}
		
		//added so we didn't have to change 2 different places every time we needed to adjust the parsing.
		public void parseLine(String line, Boolean gameLoad) {
			GameObject o;
			if (line.startsWith("wall")){
				o = (GameObject) Wall.fromString(line.substring(4));
				Log.d("LoadLevel", o.toString());
			}
			else if (line.startsWith("hero")) {
				GameHero.fromString(line.substring(4));
				Log.d("LoadLevel", GameHero.hero.toString());
			}
			else if (line.startsWith("rlaunch")) {
				GameRLauncher rLaunch = GameRLauncher.fromString(line.substring(7));
				rLaunch.addToggle(new Vec2d(660000,290000));
				rLaunch.addToggle(new Vec2d(440000,380000));
			}
			else if (line.startsWith("twall")) {
				o = (GameObject) WallToggle.fromString(line.substring(5));
				Log.d("LoadLevel", o.toString());
			}
			else if (line.startsWith("3wall")) {
				o = (GameObject) Wall3.fromString(line.substring(5));
				Log.d("LoadLevel", o.toString());
			}
			else if (line.startsWith("2wall")) {
				o = (GameObject) Wall2.fromString(line.substring(5));
				Log.d("LoadLevel", o.toString());
			}
			else if (line.startsWith("rlas")) { //redLaserofDeath!!!!
				o = (GameObject) GameObstacleGen.fromString(line.substring(4));
				Log.d("LoadLevel", o.toString());
			}
			else if (line.startsWith("mode")) {
				if (gameLoad) {
					if (line.indexOf("trail")>0) {
						TrailOfDeath.enabled = true;
					} else 
						TrailOfDeath.enabled = false;
				} 
			}
			else Log.d("Load", line);
			
		}
		
		
		
		//Loads a level
		public void loadLevel(InputStream stream) {
			//Clear out old level if present
			GameObject.gameObjects.clear();
			MovingObject.movingObjects.clear();
			
			//Boarders //Obstacles
			GameObstacle.addObstacle(4,240,4,232);
			GameObstacle.addObstacle(392,4,400,4);
			GameObstacle.addObstacle(372,476,428,4);
			GameObstacle.addObstacle(796,232,4,240);
			
			BufferedReader reader = null;
			WallToggle.WallToggles.clear();
			//Parse Level file
			
			try{
				reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 4096);
				String rline, line;
				while((rline = reader.readLine()) != null){
					line = rline.toLowerCase()
								.replaceAll("\\) *extent|\\) *direction|\\) *light", ",")
						   	    .replaceAll(" |\\(|\\)|:|pos|extent|direction|light", "");
					parseLine(line, true);
				}
			}catch (Exception e){
				e.printStackTrace();
				Log.d("LoadLevel", "error - " + e);
			}finally{
				try{
					if(reader != null) reader.close();
				}catch (Exception e){
					e.printStackTrace();
					Log.d("LoadLevel", "error - " + e);
				}
			}
//			Wall448,376
//			new GameObstacleGen(new Vec2d(296000,8000),1);
//			new GameObstacleGen(new Vec2d(488000,472000),3);
//			Wall296,8,8,8
//			Wall488,472,8,8
			
//			new GameObstacleGen(new Vec2d(400000,480000),3);
//			new GameObstacleGen(new Vec2d(600000,40000),1);
//			new GameObstacleGen(new Vec2d(40000,200000),0);
//			new GameObstacleGen(new Vec2d(740000,300000),2);
//			new GameObstacleGen(new Vec2d(448000,376000),3);

			
			WallToggle.setLists();
			GameObstacleGen.adjustLasers();
		}
		
		public void SaveLevel(String level){
			FileOutputStream stream = null;
			BufferedWriter writer = null;
			try{
				stream = mContext.openFileOutput(level, Context.MODE_PRIVATE);
				writer = new BufferedWriter (new OutputStreamWriter(stream));
				GameObject.toFile(writer);
			}catch(Exception e){
				e.printStackTrace();
				Log.d("SaveLevel", "error - "  + e);
			}finally{
				try{
					if(writer != null){
						writer.flush();
						writer.close();
					}
				}catch(Exception e){
					e.printStackTrace();
					Log.d("SaveLevel", "error - "  + e);
				}
			}
		}
		
		public Game mGame;
		private Bitmap pauseButton;
		private Bitmap downArrow, leftArrow, upArrow, arrow;
		public GameThread(SurfaceHolder surfaceHolder, Game game) {
			_surfaceHolder = surfaceHolder;
			mGame = game;
			pauseButton = Game.loadBitmapAsset("pause_button.png");
			currentTime = System.currentTimeMillis();
			
			arrow = Game.loadBitmapAsset("arrow_guide.png");
			Matrix mtx = new Matrix();
			mtx.postRotate(90);
			downArrow = Bitmap.createBitmap(arrow, 0, 0, arrow.getWidth(), arrow.getHeight(), mtx, true);
			mtx.postRotate(90);
			leftArrow = Bitmap.createBitmap(arrow, 0, 0, arrow.getWidth(), arrow.getHeight(), mtx, true);
			mtx.postRotate(90);
			upArrow = Bitmap.createBitmap(arrow, 0, 0, arrow.getWidth(), arrow.getHeight(), mtx, true);
			
			
			
		}
		
		public void setRunning(boolean run) {
			mRun = run;
		}
		
		private long getFrameTime(){
			previousTime = currentTime;
  		    currentTime = System.currentTimeMillis();
  		    return (currentTime - previousTime);
		}
		public Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);;
		public Rect rect = new Rect();
		
		public void drawFPS(Canvas c, long FrameTime) {
		    sft = (long) (sft * 0.9 + FrameTime * 0.1);
  		    
			paint.setColor(Color.GREEN);
			paint.setTextSize(16);
			paint.setTypeface(Typeface.MONOSPACE);
			    
			c.drawText(String.valueOf(sft), 20, 24, paint);
			if (Game.gameState==GameState.PLAYING) c.drawText(String.valueOf(GameHero.hero.getToggleCount()), 700, 30, paint);
			
			
  		  
		}

		
		private void drawPause(Canvas c) {
			c.drawBitmap(pauseButton, Game.cameraSize.x - pauseButton.getWidth() - 15, 15, null);
		}
		
		
		
/*		public void drawArrows(Canvas c) {
			GameHero h = GameHero.hero;
			Matrix mtx = new Matrix();
			mtx.postRotate(90);
			Bitmap downArrow = Bitmap.createBitmap(arrow, 0, 0, arrow.getWidth(), arrow.getHeight(), mtx, true);
			mtx.postRotate(90);
			Bitmap leftArrow = Bitmap.createBitmap(arrow, 0, 0, arrow.getWidth(), arrow.getHeight(), mtx, true);
			mtx.postRotate(90);
			Bitmap upArrow = Bitmap.createBitmap(arrow, 0, 0, arrow.getWidth(), arrow.getHeight(), mtx, true);
			if (h.velocity.isZero()) {
				
				if(GameHero.hero.lastDirection == GameObject.RIGHT) {
					RIGHTc.drawBitmap(arrow, ((h.pos.x + h.extent.x) / 1000)  + 10, (h.pos.y / 1000) - (arrow.getHeight() / 2), null);
					UPc.drawBitmap(upArrow, (h.pos.x / 1000) - (upArrow.getWidth() / 2), ((h.pos.y - h.extent.y) / 1000) - (upArrow.getHeight()) - 10, null);
					DOWNc.drawBitmap(downArrow, h.pos.x / 1000 - (downArrow.getWidth() / 2), ((h.pos.y + h.extent.y) / 1000) + 10, null);
				} else if(GameHero.hero.lastDirection == GameObject.LEFT) {
					LEFTc.drawBitmap(leftArrow, (((h.pos.x - h.extent.x) / 1000) - ((leftArrow.getWidth()) + 10)), (h.pos.y / 1000) - (leftArrow.getHeight() / 2), null);
					UPc.drawBitmap(upArrow, (h.pos.x / 1000) - (upArrow.getWidth() / 2), ((h.pos.y - h.extent.y) / 1000) - (upArrow.getHeight()) - 10, null);
					DOWNc.drawBitmap(downArrow, h.pos.x / 1000 - (downArrow.getWidth() / 2), ((h.pos.y + h.extent.y) / 1000) + 10, null);
					
				} else if(GameHero.hero.lastDirection == GameObject.DOWN) {
					RIGHTc.drawBitmap(arrow, ((h.pos.x + h.extent.x) / 1000)  + 10, (h.pos.y / 1000) - (arrow.getHeight() / 2), null);
					LEFTc.drawBitmap(leftArrow, (((h.pos.x - h.extent.x) / 1000) - ((leftArrow.getWidth()) + 10)), (h.pos.y / 1000) - (leftArrow.getHeight() / 2), null);
					DOWNc.drawBitmap(downArrow, h.pos.x / 1000 - (downArrow.getWidth() / 2), ((h.pos.y + h.extent.y) / 1000) + 10, null);
					
				} else if(GameHero.hero.lastDirection == GameObject.UP) {
					RIGHTc.drawBitmap(arrow, ((h.pos.x + h.extent.x) / 1000)  + 10, (h.pos.y / 1000) - (arrow.getHeight() / 2), null);
					LEFTc.drawBitmap(leftArrow, (((h.pos.x - h.extent.x) / 1000) - ((leftArrow.getWidth()) + 10)), (h.pos.y / 1000) - (leftArrow.getHeight() / 2), null);
					UPc.drawBitmap(upArrow, (h.pos.x / 1000) - (upArrow.getWidth() / 2), ((h.pos.y - h.extent.y) / 1000) - (upArrow.getHeight()) - 10, null);
				}
					
				
				
				
			}
		}*/
		
		private Vec2d down = new Vec2d();
		private Vec2d mClicked  = new Vec2d();
public void drawPress(Canvas c, Input input) {
			

			down.set(_input.getDown());
			mClicked.set(_input.getCurrent());
			if ((!down.isVoid()) && !((mClicked.x>700)&&(mClicked.y<100))){

				Path triPath = new Path();
				paint.setColor(Color.WHITE);
				paint.setAlpha(80);
				paint.setStyle(Paint.Style.FILL);
				
				if (input==Input.DOWN_LEFT) {
					c.drawBitmap(leftArrow, cameraSize.x / 4 - leftArrow.getWidth(), cameraSize.y / 2 - leftArrow.getHeight() / 2, null);
					if(!GameHero.hero.velocity.isZero() || GameHero.hero.lastDirection == GameObject.RIGHT) paint.setColor(Color.RED);
					triPath.moveTo(Game.cameraSize.x/2, Game.cameraSize.y/2);
					triPath.lineTo(0, 0);
					triPath.lineTo(0, cameraSize.y);
					triPath.lineTo(Game.cameraSize.x/2, Game.cameraSize.y/2);
					triPath.close();
					
				} else if (input==Input.DOWN_RIGHT) {
					c.drawBitmap(arrow, (cameraSize.x - (cameraSize.x / 4)), cameraSize.y / 2 - (arrow.getHeight() / 2), null);
					if(!GameHero.hero.velocity.isZero() || GameHero.hero.lastDirection == GameObject.LEFT)paint.setColor(Color.RED);
					triPath.moveTo(Game.cameraSize.x/2, Game.cameraSize.y/2);
					triPath.lineTo(Game.cameraSize.x, 0);
					triPath.lineTo(Game.cameraSize.x, Game.cameraSize.y);
					triPath.lineTo(Game.cameraSize.x/2, Game.cameraSize.y/2);
					triPath.close();
				} else if (input==Input.DOWN_UP) {
					c.drawBitmap(upArrow, ((cameraSize.x / 2)) - (upArrow.getWidth() / 2), (cameraSize.y / 4) - upArrow.getHeight(), null);
					if(!GameHero.hero.velocity.isZero() || GameHero.hero.lastDirection == GameObject.DOWN)paint.setColor(Color.RED);
					triPath.moveTo(Game.cameraSize.x/2, Game.cameraSize.y/2);
					triPath.lineTo(0,0);
					triPath.lineTo(Game.cameraSize.x,0);
					triPath.lineTo(Game.cameraSize.x/2, Game.cameraSize.y/2);
					triPath.close();
				} else if (input==Input.DOWN_DOWN) {
					c.drawBitmap(downArrow, ((cameraSize.x / 2)) - (downArrow.getWidth() / 2), (cameraSize.y - (cameraSize.y / 4)), null);
					if(!GameHero.hero.velocity.isZero() || GameHero.hero.lastDirection == GameObject.UP)paint.setColor(Color.RED);
					triPath.moveTo(Game.cameraSize.x/2, Game.cameraSize.y/2);
					triPath.lineTo(0,Game.cameraSize.y);
					triPath.lineTo(Game.cameraSize.x,Game.cameraSize.y);
					triPath.lineTo(Game.cameraSize.x/2, Game.cameraSize.y/2);
					triPath.close();
				}
				paint.setAlpha(80);				
				c.drawPath(triPath, paint);
//				c.drawCircle(down.x, down.y, 13, paint);
//				c.drawCircle(current.x, current.y, 7, paint);
//				c.drawLine(down.x, down.y, current.x, current.y, paint);
			}
			
		}
		
		public void drawAd(Canvas c) {
			
			Rect ad = new Rect(0, 0, 320, 50);
			paint.setColor(Color.YELLOW);
			c.drawRect(ad, paint);
		}
		
		public void createTitleScreen() {
			
			titleMenu = new MenuTitleScreen(mGame); //Has to be created after the surfaceView has been created
		}
		

		Long previousTime;
		Long currentTime;
		long sft = 0;
		
		public boolean active = false;
		
    //This is my main loop, runs as fast as it can possibly go!
		@Override
		public void run() {
			gameState = GameState.TITLE;
			MenuSystem.createMenus();
			while (active) {
				while (mRun) {
					switch(gameState){
					case TITLE:gameState = titleScreen();
					case PLAYING:gameState = gameLoop();
					case PAUSED:gameState = pause();
					case LEVEL_COMPLETE:gameState = levelComplete();
					}
				}
			}
		}
		
		
		public GameState gameLoop(){
			Vec2d offset = new Vec2d(0,0);
			GameObject.offset = offset;
			UserInput.Input currentInput = UserInput.Input.NONE;
			int TICKS_PER_SECOND = 25;
			int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
			int MAX_FRAMESKIP = 5;
			long frameTime;
			
			long next_game_tick = android.os.SystemClock.elapsedRealtime();
			int loops;
			float interpolation = 0;
  			
			Picture picScreen = new Picture();
			
  			while((gameState == GameState.PLAYING)  && (mRun)){
  				loops = 0;
  				while(android.os.SystemClock.elapsedRealtime() > next_game_tick && loops < MAX_FRAMESKIP){
					Vec2d mClicked = _input.getCurrentPress();
					if(!mClicked.isVoid()) //Pause
						if ((mClicked.x>700)&&(mClicked.y<100)) {
							gameState = GameState.PAUSED;
							_input.Clear();
						}
					
					currentInput = _input.getInput();
					GameHero.hero.processInput(currentInput);
					GameHero.hero.collisionAvoid();
					MovingObject.updateAll();
					
					next_game_tick += SKIP_TICKS;
					loops++;
  				}

				frameTime = getFrameTime();
  				interpolation = (float)(android.os.SystemClock.elapsedRealtime() + SKIP_TICKS - next_game_tick) / (float)SKIP_TICKS;
				
				Canvas c = picScreen.beginRecording((int) Game.cameraSize.x, (int) Game.cameraSize.y);
				
				synchronized (_surfaceHolder) {
					clearScreen(c, Game.cameraSize);
					GameObject.drawAll(c, interpolation);
//					drawArrows(c);
					drawPress(c, currentInput);
					drawFPS(c, frameTime);
					drawPause(c);

					picScreen.endRecording();
					drawToScreen(picScreen);
				}

				if(GameHero.hero.dead){
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
			long frameTime;
			float interpolation = 0;

			GameObject.restoreCheckpointAll();
			while(mRun && alpha > 50){
				frameTime = getFrameTime();
				Picture picScreen = new Picture();
				Canvas c = picScreen.beginRecording((int) Game.cameraSize.x, (int) Game.cameraSize.y);
				
				synchronized (_surfaceHolder) {
					clearScreen(c, Game.cameraSize);
					GameObject.drawAll(c, interpolation);
					drawFPS(c, frameTime);
					drawPause(c);
					Rect rec = new Rect (0,0, width, height);
					paint.setColor(Color.WHITE);
					paint.setAlpha((int) alpha);
					c.drawRect(rec, paint);
					picScreen.endRecording();
					drawToScreen(picScreen);
				}
				fade += 0.6;
				alpha -= fade;
				
			}
		}
		
		public GameState levelComplete() {
			long frameTime;
			float interpolation = 0;
			Picture picScreen = new Picture();
			while((gameState == GameState.LEVEL_COMPLETE) && (mRun)){
				Vec2d mClicked = _input.getCurrentPress();
				
				frameTime = getFrameTime();
				
				Canvas c = picScreen.beginRecording((int) Game.cameraSize.x, (int) Game.cameraSize.y);
				
				titleMenu.update();
				
					synchronized (_surfaceHolder) {
						clearScreen(c, Game.cameraSize);
						GameObject.drawAll(c, interpolation);
						drawFPS(c, frameTime);
						paint.setColor(Color.RED);
						paint.setTextSize(86);
						paint.setTypeface(Typeface.DEFAULT_BOLD);
						    
						c.drawText("YOU WON!", 75, 125, paint);
						picScreen.endRecording();
						drawToScreen(picScreen);
					}
					
					if (!mClicked.isVoid()) gameState = GameState.TITLE;
					
			}
			return gameState;
		}
		public GameState titleScreen() {
//			createTitleScreen();
			long frameTime;
			Picture picScreen = null;
			Canvas c = null;
			mClicked.setVoid();
			
			MenuSystem.activePanel = MenuSystem.mainPanel;
			MenuSystem.mainPanel.deactiveButton = MenuSystem.continueButton;
			MenuSystem.updateContinue();
			
			while((gameState == GameState.TITLE) && (mRun)){
				
				frameTime = getFrameTime();
				picScreen = new Picture();
				c = picScreen.beginRecording((int) Game.cameraSize.x, (int) Game.cameraSize.y);
				
				mClicked = _input.getCurrentPress();
				if (!mClicked.isVoid()) MenuSystem.processInput(mClicked);
				MenuSystem.updateMenus();
				
					synchronized (_surfaceHolder) {
						MenuSystem.drawMenus(c);
						drawFPS(c, frameTime);
						picScreen.endRecording();
						drawToScreen(picScreen);
					}
					
			}
			
			return gameState;
		}
		
		Bitmap pausedState;
		Picture picture = new Picture();
		
		public GameState pause(){
			long frameTime;
			Picture picScreen = null;
			Canvas c = null;
			_input.Clear();
			while((gameState == GameState.PAUSED) && (mRun)){
				frameTime = getFrameTime();
				picScreen = new Picture();
				c = picScreen.beginRecording((int) Game.cameraSize.x, (int) Game.cameraSize.y);
				Vec2d mClicked = _input.getCurrentPress();
				if (!mClicked.isVoid()) gameState = pause.onClick(mClicked, mGame);
				
					synchronized (_surfaceHolder) {
						GameObject.drawPause(c, Game.cameraSize);
						drawFPS(c, frameTime);
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
		private Rect rectSurface = new Rect();
		private Canvas actual = null;
		public void drawToScreen(Picture pic, float fade){
			actual = null;
			try {
				actual = _surfaceHolder.lockCanvas(null);
				synchronized (_surfaceHolder) {
					rectSurface.left = 0;
					rectSurface.top = 0;
					rectSurface.right = (int) surfaceSize.x;
					rectSurface.bottom = (int) surfaceSize.y;
					
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
		private Rect clearRec = new Rect ();
		public void clearScreen(Canvas c, Vec2d size){
			clearRec.left = 0;
			clearRec.right = (int) size.x;
			clearRec.top = 0;
			clearRec.bottom = (int) size.y;
			paint.setColor(Color.BLACK);
			c.drawRect(clearRec, paint);
		}
	}
}










