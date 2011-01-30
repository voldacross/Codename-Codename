package voldaran.com.Upright;

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
	
	
	public static final int USER_TOUCH_NONE = 1000;
	public static final int USER_SWIPE_LEFT = 1001;
	public static final int USER_SWIPE_RIGHT = 1002;
	public static final int USER_SWIPE_UP = 1003;
	public static final int USER_SWIPE_DOWN = 1004;
	public static final int USER_PRESS_RIGHT = 1005;
	public static final int USER_PRESS_MIDDLE = 1006;
	public static final int USER_PRESS_LEFT = 1007;
	
	public int USER_TOUCH = 1000;
	
	public UserInput _input = null;
	
	
	public Game(Context context) {
		super(context);
		getHolder().addCallback(this);
		Log.d("GSTA","here1");
		Log.d("GSTA","here2");
		thread = new GameThread(getHolder(), this);
		setFocusable(true);
	}
	
	
	@Override
	public boolean onTouchEvent (MotionEvent event) {
		_input = new UserInput();
		_input.UpdateInput(event);
		

		
		final int action = event.getAction();
		bolFingerDown = true;
		final float x = event.getX();
		final float y = event.getY();
		
		mCurrentTouchX = x;
		mCurrentTouchY = y;

		switch (action & MotionEvent.ACTION_MASK) {
		
		
		//Finger press down
			case MotionEvent.ACTION_DOWN:
				//Log.d("GSTA", "You pressed down!");
				
			//record X,Y locations to determine fingerDrag
				
					fingerDragStartX = event.getX();
					fingerDragStartY = event.getY();
					
					if (mCurrentTouchX>getWidth()-(getWidth()/3)) {
						//Log.d("GSTA", "To the right");
						USER_TOUCH = USER_PRESS_RIGHT;
					} else if (mCurrentTouchX<getWidth()/3) {
						//Log.d("GSTA", "To the left");
						USER_TOUCH = USER_PRESS_LEFT;
					} else {
						//Log.d("GSTA", "the Middle!");
						USER_TOUCH = USER_PRESS_MIDDLE;
					}
				break;
				
				
		//Finger press up
			case MotionEvent.ACTION_UP: 
				//Log.d("GSTA", "You released!");
				bolFingerDown = false;
				
				//if finger is dragging above tolerance 
				if (bolDragActive) {
					//record ending X, Y
					fingerDragEndX = event.getX();
					fingerDragEndY = event.getY();


					double dd = distance(fingerDragStartX, fingerDragStartY, event.getX(), event.getY());
					
					if (dd>35) {
						int xDifference = (int) Math.abs((fingerDragStartX - fingerDragEndX));
						int yDifference = (int) Math.abs((fingerDragStartY - fingerDragEndY));
						if (xDifference>=yDifference) {
							if (fingerDragEndX<=fingerDragStartX) {
								//LEFT
									USER_TOUCH = USER_SWIPE_LEFT;
							} else {
							//They swiped Right						
							//Jump is not fully implemented
								USER_TOUCH = USER_SWIPE_RIGHT;
								}
							} else if (xDifference<=yDifference) {
								if (fingerDragEndY<=fingerDragStartY) {
									//Log.d("GSTA", "You released Up!");
							//They swiped Up
									USER_TOUCH = USER_SWIPE_UP;
								} else {
									//Log.d("GSTA", "You released Down!");
							//They swiped down
									//adjust Gravity Down
									USER_TOUCH = USER_SWIPE_DOWN;
								} 
							}
					} else {
						USER_TOUCH = USER_TOUCH_NONE;
					}
//					//user is no longer dragging their finger
//					bolDragActive = false;
//					//They have activated a Swipe / Drag
//					bolDragRelease = true;

				}
				
				break;
			
			case MotionEvent.ACTION_CANCEL:
				
				break;
				
		//Finger press drag, moving their finger across the screen. Swiping.
			case MotionEvent.ACTION_MOVE:

				double dd = distance(fingerDragStartX, fingerDragStartY, event.getX(), event.getY());
				//Log.d("GSTA", "Distance - " + dd);
				
				
				//built in tolerance.
				//finger press on actual device always yields an ACTION_MOVE. 
				//built a tolerence of a distance of 35 pixels to allow a little leeway
				
				if (dd>35) {
					bolDragActive = true;
//					int OLD_TOUCH = USER_TOUCH;
					//Log.d("GSTA", "CLEARING OUT TOUCH");
					USER_TOUCH = USER_TOUCH_NONE;
				} else {
					if (mCurrentTouchX>getWidth()-(getWidth()/3)) {
						//Log.d("GSTA", "To the right");
						USER_TOUCH = USER_PRESS_RIGHT;
					} else if (mCurrentTouchX<getWidth()/3) {
						//Log.d("GSTA", "To the left");
						USER_TOUCH = USER_PRESS_LEFT;
					} else {
						//Log.d("GSTA", "the Middle!");
						USER_TOUCH = USER_PRESS_MIDDLE;
					}
				}
				
				break;
			}
		
		return true;
	}
	
	//Finds distance
	public double distance (float X1, float Y1, float X2, float Y2) {
		return Math.sqrt(((X2 - X1) * ( X2 - X1)) + ((Y2 - Y1) * (Y2 - Y1)));

	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
	int height) {
	// TODO Auto-generated method stub
	}
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread.setRunning(true);
		thread.start();
	}
	
	//Destroys thread if app is closed, kind of buggy at the moment.
	//Game doesn't force close on shutdown, but doesn't re-open smoothly
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
			// tell the thread to shut down and wait for it to finish
			// this is a clean shutdown
//			boolean retry = true;
//			while (retry) {
//				try {
//					thread.join();
//					retry = false;
//				} catch (InterruptedException e) {
//					// try again shutting down the thread
//				}
//			}
	}
	
	//GAME THREAD
	class GameThread extends Thread {
		private SurfaceHolder _surfaceHolder;
		public boolean mRun = false;
		//public MapClass.TileClass[][] aTile;
		
		public Bitmap bitHero;
		
//		private ArrayList<Walls> _walls = new ArrayList<Walls>();
		
		public void addWall(int posx, int posy, int extentx, int extenty){
			new GameObject(new Vec2d(posx, posy).mul(1000), new Vec2d(extentx, extenty).mul(1000));
		}
		
		public GameThread(SurfaceHolder surfaceHolder, Game game) {
			
			_surfaceHolder = surfaceHolder;
			
			
		//Arcahic way of creating a level, added hero and walls.
			bitHero = BitmapFactory.decodeResource(getResources(),R.drawable.icon);
			GameHero hero = new GameHero(new Vec2d(600000,300000), new Vec2d(72000,72000), bitHero);

			//_hero2.bitHero = bitHero
			Log.d("GSTA", "hero : " + hero.pos);
			Log.d("GSTA", "hero top, left, bottom,right : " + hero.pos.sub(hero.extent) + ", " + hero.pos.add(hero.extent));
				

			addWall(400,475,400,10);
			addWall(400,5,400,10);
			addWall(145,105,145,10);
			addWall(375,100,10,100);
			addWall(190,205,110,10);
			addWall(85,185,10,30);
			addWall(185,260,10,65);
			addWall(140,315,40,10);
			addWall(290,260,10,65);
			addWall(465,270,10,200);
		
		
/*			GameObjects _firstPlatform = new GameObjects();
			_firstPlatform.x = 600;
			_firstPlatform.y = 300;
			_firstPlatform.type = _firstPlatform.GAME_OBJECT_PLATFORM;
			_firstPlatform.width = 100;
			_firstPlatform.height = 10;
			_firstPlatform.speed = 1;
			_firstPlatform.addPathPoint(600, 300);
			_firstPlatform.addPathPoint(450, 300);
			_firstPlatform.addPathPoint(450, 150);
			_firstPlatform.addPathPoint(600, 150);
			_gameMoving.add(_firstPlatform);
			_gameO.add(_firstPlatform);
*/			

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
			while (mRun) {
				gameTIME += 1;
				Canvas c = null;
				
				//MovingObject.updateObjects();
				
				try {
					c = _surfaceHolder.lockCanvas(null);
					synchronized (_surfaceHolder) {
						clearScreen(c);
						GameObject.drawObjects(c);
					}
                } finally {
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



