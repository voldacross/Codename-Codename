package voldaran.com.Upright;

import java.math.BigInteger;
import java.util.ArrayList;

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
import android.view.animation.AnimationSet;

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
	
	

*/
//END NOTES

public class Game extends SurfaceView implements SurfaceHolder.Callback {


	private GameThread thread;
	private SurfaceHolder mSurfaceHolder;
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
    
	private static final int INVALID_POINTER_ID = -1;
	private int mActivePointerId = INVALID_POINTER_ID;
	public int gameTIME = 0;
	
	HeroAnimated hero;
	
	public Game(Context context) {
		super(context);
		getHolder().addCallback(this);
		setFocusable(true);
		thread = new GameThread(getHolder(), this);
		setFocusable(true);

	}
	
	
	
	
	@Override
	public boolean onTouchEvent (MotionEvent event) {
		
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
					//user is no longer dragging their finger
					bolDragActive = false;
					//They have activated a Swipe / Drag
					bolDragRelease = true;
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
				} else {
					bolDragActive = false;
				}
				
				break;
			}
		
//		if (event.getAction()==0) {
//			Log.d("GSTA", "You pressed down");
//		}
		
		
		//ignore all commented sections, as they are old work.
		
//		int pointerCount = event.getPointerCount();
/*		synchronized (mSurfaceHolder) {
			//Log.d("GSTA","action" + event.getAction());
			if (event.getAction()==0) {
				keyDown = true;
			}
			if (event.getAction()==2) {
				if (!keyDrag) {
					keyDrag = true;
					keyDragStartX = event.getX();
					keyDragStartY = event.getY();
				}
			}
			if (event.getAction()==1) {
				
				keyDown = false;
				if (keyDrag) {
					keyDrag = false;
					keyDragEndX = event.getX();
					keyDragEndY = event.getY();
				}
				if (keyActualDrag) {
				launch = true;
				keyActualDrag = false;
				}
			}
			fingerX = event.getX();
			fingerY = event.getY();
		}
		
	*/
		
		
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
		private GameThread _Thread;
		private Game _game;
		private SurfaceHolder _surfaceHolder;
		public boolean mRun = false;
		public HeroObject _hero;
		//public MapClass.TileClass[][] aTile;
		
		public Bitmap bitHero;
		
//		private ArrayList<Walls> _walls = new ArrayList<Walls>();
		private ArrayList<Obstacles> _obstacles = new ArrayList<Obstacles>();
		private ArrayList<GameObjects> _gameO = new ArrayList<GameObjects>();
		private ArrayList<GameObjects> _gameMoving = new ArrayList<GameObjects>();
		
		public Overlay gameOverlay = new Overlay();
		
		
		
		public void addWall(float x, float y, int width, int height) {
			GameObjects _tempWall = new GameObjects();
				_tempWall.x = x;
				_tempWall.y = y;
				_tempWall.type = _tempWall.GAME_OBJECT_WALL;
				_tempWall.width = width;
				_tempWall.height = height;
				
				
				if (height>width) {
					_tempWall.vertical = true;
				} else if (height<width) {
					_tempWall.vertical = false;
				}
				_gameO.add(_tempWall);
			gameOverlay.addGameObject(_tempWall);
		}
		
		
		public GameThread(SurfaceHolder surfaceHolder, Game game) {
			
			mSurfaceHolder = surfaceHolder;
			_surfaceHolder = surfaceHolder;
			_game = game;
			
//			gameBoard.Initialize();
//			aTile = gameBoard.getBoard();
//			Log.d("GSTA", aTile[0][0].stringName);
			
			
			//Creating board overlay
			
//			ArrayList[][] overlay = new ArrayList[100][100];
//			
//			overlay[0][0] = new ArrayList<Walls>();
			
			
			
		//Arcahic way of creating a level, added hero and walls.
			bitHero = BitmapFactory.decodeResource(getResources(),R.drawable.icon);
			_hero = new HeroObject();
				_hero.x = 600;
				_hero.y = 600;
				_hero.direction = 1;
				_hero.bitHero = bitHero;
				
		//HeroAnimated hero = new HeroAnimated(BitmapFactory.decodeResource(getResources(), R.drawable.spritesheet),0,0,64,64,5,8);

		addWall(0,475,800,10);
		addWall(0,0,800,10);
		addWall(0,100,290,10);
		addWall(370,0,10,200);
		addWall(80,200,300,10);
		addWall(80,205,10,175);
		addWall(180,310,10,180);
		addWall(180,310,100,10);
		addWall(280,310,10,180);
		addWall(180,310,10,180);
		addWall(460,100,10,400);
		
		
		GameObjects _firstPlatform = new GameObjects();
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
		gameOverlay.addGameObject(_firstPlatform);
//		
//		GameObjects _firstPlatform2 = new GameObjects();
//		_firstPlatform2.x = 600;
//		_firstPlatform2.y = 300;
//		_firstPlatform2.type = _firstPlatform2.GAME_OBJECT_PLATFORM;
//		_firstPlatform2.width = 100;
//		_firstPlatform2.height = 10;
//		_firstPlatform2.speed = 2;
//		_firstPlatform2.addPathPoint(600, 300);
//		_firstPlatform2.addPathPoint(450, 300);
//		_firstPlatform2.addPathPoint(450, 150);
//		_firstPlatform2.addPathPoint(600, 150);
//		_gameMoving.add(_firstPlatform2);
//		_gameO.add(_firstPlatform2);
//		gameOverlay.addGameObject(_firstPlatform2);
//		
//		GameObjects _firstPlatform3 = new GameObjects();
//		_firstPlatform3.x = 600;
//		_firstPlatform3.y = 300;
//		_firstPlatform3.type = _firstPlatform3.GAME_OBJECT_PLATFORM;
//		_firstPlatform3.width = 100;
//		_firstPlatform3.height = 10;
//		_firstPlatform3.speed = 3;
//		_firstPlatform3.addPathPoint(600, 300);
//		_firstPlatform3.addPathPoint(450, 300);
//		_firstPlatform3.addPathPoint(450, 150);
//		_firstPlatform3.addPathPoint(600, 150);
//		_gameMoving.add(_firstPlatform3);
//		_gameO.add(_firstPlatform3);
//		gameOverlay.addGameObject(_firstPlatform3);
//		
//		GameObjects _firstPlatform4 = new GameObjects();
//		_firstPlatform4.x = 600;
//		_firstPlatform4.y = 300;
//		_firstPlatform4.type = _firstPlatform4.GAME_OBJECT_PLATFORM;
//		_firstPlatform4.width = 100;
//		_firstPlatform4.height = 10;
//		_firstPlatform4.speed = 4;
//		_firstPlatform4.addPathPoint(600, 300);
//		_firstPlatform4.addPathPoint(450, 300);
//		_firstPlatform4.addPathPoint(450, 150);
//		_firstPlatform4.addPathPoint(600, 150);
//		_gameMoving.add(_firstPlatform4);
//		_gameO.add(_firstPlatform4);
//		gameOverlay.addGameObject(_firstPlatform4);
//		
//		GameObjects _firstPlatform5 = new GameObjects();
//		_firstPlatform5.x = 600;
//		_firstPlatform5.y = 300;
//		_firstPlatform5.type = _firstPlatform5.GAME_OBJECT_PLATFORM;
//		_firstPlatform5.width = 100;
//		_firstPlatform5.height = 10;
//		_firstPlatform5.speed = 5;
//		_firstPlatform5.addPathPoint(600, 300);
//		_firstPlatform5.addPathPoint(450, 300);
//		_firstPlatform5.addPathPoint(450, 150);
//		_firstPlatform5.addPathPoint(600, 150);
//		_gameMoving.add(_firstPlatform5);
//		_gameO.add(_firstPlatform5);
//		gameOverlay.addGameObject(_firstPlatform5);
//		
//		GameObjects _firstPlatform6 = new GameObjects();
//		_firstPlatform6.x = 600;
//		_firstPlatform6.y = 300;
//		_firstPlatform6.type = _firstPlatform6.GAME_OBJECT_PLATFORM;
//		_firstPlatform6.width = 100;
//		_firstPlatform6.height = 10;
//		_firstPlatform6.speed = 6;
//		_firstPlatform6.addPathPoint(600, 300);
//		_firstPlatform6.addPathPoint(450, 300);
//		_firstPlatform6.addPathPoint(450, 150);
//		_firstPlatform6.addPathPoint(600, 150);
//		_gameMoving.add(_firstPlatform6);
//		_gameO.add(_firstPlatform6);
//		gameOverlay.addGameObject(_firstPlatform6);
//		
//		GameObjects _firstPlatform7 = new GameObjects();
//		_firstPlatform7.x = 600;
//		_firstPlatform7.y = 300;
//		_firstPlatform7.type = _firstPlatform7.GAME_OBJECT_PLATFORM;
//		_firstPlatform7.width = 100;
//		_firstPlatform7.height = 10;
//		_firstPlatform7.speed = 7;
//		_firstPlatform7.addPathPoint(600, 300);
//		_firstPlatform7.addPathPoint(450, 300);
//		_firstPlatform7.addPathPoint(450, 150);
//		_firstPlatform7.addPathPoint(600, 150);
//		_gameMoving.add(_firstPlatform7);
//		_gameO.add(_firstPlatform7);
//		gameOverlay.addGameObject(_firstPlatform7);
//		
//		GameObjects _firstPlatform8 = new GameObjects();
//		_firstPlatform8.x = 600;
//		_firstPlatform8.y = 300;
//		_firstPlatform8.type = _firstPlatform8.GAME_OBJECT_PLATFORM;
//		_firstPlatform8.width = 100;
//		_firstPlatform8.height = 10;
//		_firstPlatform8.speed = 8;
//		_firstPlatform8.addPathPoint(600, 300);
//		_firstPlatform8.addPathPoint(450, 300);
//		_firstPlatform8.addPathPoint(450, 150);
//		_firstPlatform8.addPathPoint(600, 150);
//		_gameMoving.add(_firstPlatform8);
//		_gameO.add(_firstPlatform8);
//		gameOverlay.addGameObject(_firstPlatform8);
			
		
//			Walls _wall8 = new Walls();
//				_wall8.X = 180;
//				_wall8.Y = 310;
//				_wall8.width = 10;
//				_wall8.height = 180;
//				_walls.add(_wall8);
//				gameOverlay.addWall(_wall8);
//				
//			Walls _wall9 = new Walls();
//				_wall9.X = 460;
//				_wall9.Y = 100;
//				_wall9.width = 10;
//				_wall9.height = 400;
//				_walls.add(_wall9);	
//				gameOverlay.addWall(_wall9);
				
				
				//Add some dummy obstacles
			Obstacles _obstacles1 = new Obstacles();
				_obstacles1.X = 200;
				_obstacles1.Y = 200;
				_obstacles1.width = 10;
				_obstacles1.height = 10;
				_obstacles.add(_obstacles1);
				
				

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
        private long sleepTime = 1000;
        //amount of time to sleep for (in milliseconds)
        private long delay=10;
        BigInteger StartTime;

    //This is my main loop, runs as fast as it can possibly go!
		@Override
		public void run() {
			int z = 0;
			while (mRun) {
				gameTIME += 1;
				Canvas c = null;
				
				try {
					c = _surfaceHolder.lockCanvas(null);
					synchronized (_surfaceHolder) {
						
						//Update Game State
						updateGame(c);
						
						//Draw Game
						draw(c);
						//MAIN LOOP
						
//						 try {
//				                //actual sleep code
//				                	Thread.sleep(sleepTime);
//
//				            } catch (InterruptedException ex) {
//				                //Logger.getLogger(PaintThread.class.getName()).log(Level.SEVERE, null, ex);
//				            }
						
						
					}
					
					
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
			}
		}
		
		
		public int directionLocked = 777;
		public int jumpTimer = 0;
		
		public void updateGame (Canvas c) {
		//These were used in the old method to set which state _hero was in
			final int HERO_STANDING = 0;
			final int HERO_FREEFALL = 1;
			final int HERO_WALKING = 2;
			final int HERO_JUMPING = 3;
			final int HERO_CLIMBING = 4;
			final int HERO_COLLIDING = 5;
			
			final int HERO_RIGHT = 0;
			final int HERO_DOWN = 1;
			final int HERO_LEFT = 2;
			final int HERO_UP = 3;
			
			final int HERO_WALKING_SPEED = 6;
			final int HERO_FALLING_SPEED = 10;
			
			boolean fingerRight = false;
			boolean fingerLeft = false;
			boolean jumpRight = false;
			boolean jumpLeft = false;
			
			int Gravity = 0;

			
//			int rows = gameBoard.ROWS;
//			int cols = gameBoard.COLS;
//			
//			if ((currentTileRow<= rows && currentTileRow > 0) && (currentTileColumn<= cols && currentTileColumn > 0)) {
//				Log.d("GSTA", "You are on the current game board");
//				Log.d("GSTA", aTile[currentTileRow][currentTileColumn].stringName);
//			} else {
//				Log.d("GSTA", "You are NOT on the current game board");
//			}
			
			
			//Log.d("GSTA", "You are in tile " + currentTileX + "," + currentTileY + " at position: " + _hero.x + "," + _hero.y + " " + aTile[currentTileX][currentTileY].stringName);
			
			
			
			//fingerPress
			
			if ((bolFingerDown) && (!bolDragActive)) {

		// separates screen into 3 equal parts, Right , Middle, and Left
				if ((mCurrentTouchX>getWidth()-(getWidth()/3))&& (!(_hero.movementState==HERO_FREEFALL))) {
					//Log.d("GSTA", "To the right");
					fingerRight = true;
				} else if ((mCurrentTouchX<getWidth()/3) && (!(_hero.movementState==HERO_FREEFALL))){
					//Log.d("GSTA", "To the left");
					fingerLeft = true;
				} else {
					//Log.d("GSTA", "the Middle!");
				
				}
			}
			
			
//physical checks

			boolean bolTest = true;
			
			
			//fingerDrag settings... Launch... or Jump... or do nothing

				//If user has been dragging their finger and releases
			if (bolDragRelease) {
				//Log.d("GSTA", "You released from a drag");
				bolDragRelease = false;
				//Determine direction of release - Right, Left, Up, Down
				int xDifference = (int) Math.abs((fingerDragStartX - fingerDragEndX));
				int yDifference = (int) Math.abs((fingerDragStartY - fingerDragEndY));
				if (xDifference>=yDifference) {
					if (fingerDragEndX<=fingerDragStartX) {
				//They swiped Left
						//Jump is not fully implemented
						_hero.jumpLeft = true;
					} else {
				//They swiped Right						
						//Jump is not fully implemented
						_hero.jumpRight = true;
						
					}
				} else if (xDifference<=yDifference) {
					if (fingerDragEndY<=fingerDragStartY) {
						//Log.d("GSTA", "You released Up!");
				//They swiped Up
						//Adjust Gravity up
						if (_hero.gravity==HERO_DOWN) {
							_hero.gravity=HERO_UP;
							_hero.currentWall = null;
						}
					} else {
						//Log.d("GSTA", "You released Down!");
				//They swiped down
						//adjust Gravity Down
						if (_hero.gravity==HERO_UP) {
							_hero.gravity=HERO_DOWN;
							_hero.currentWall = null;
						}
					} 
				}
			}
			

			//Adjust position

			
			int adjustX = 0;
			int adjustY = 0;
			
			
			//APPLY GRAVITY
			
			if (_hero.gravity==HERO_DOWN) {
				adjustY += HERO_FALLING_SPEED;
			} else if (_hero.gravity==HERO_UP) {
				adjustY -= HERO_FALLING_SPEED;
			} else if (_hero.gravity==HERO_RIGHT) {
				adjustX += HERO_FALLING_SPEED;
			} else if (_hero.gravity==HERO_LEFT) {
				adjustX -= HERO_FALLING_SPEED;
			}
			
			//apply walking speed
		//If finger is pressed down, booleans fingerRight/fingerLeft will be true 
			if (fingerRight) {
				adjustX += HERO_WALKING_SPEED;
			} else if (fingerLeft) {
				adjustX -= HERO_WALKING_SPEED;
				
			}
			
			//My terrible attempt at jumping, just goes up and down in a right angle.
			if (_hero.jumpRight) {
				_hero.jumpCount += 1;
				
				if (_hero.jumpCount > 0 && _hero.jumpCount <=10) {
					//on the way up
					adjustX += HERO_WALKING_SPEED;
					
					//Counteract gravity
					adjustY -= HERO_FALLING_SPEED + HERO_WALKING_SPEED;
					
					
				} else if (_hero.jumpCount > 10 && _hero.jumpCount<=20) {
					//on the way down
					adjustX += HERO_WALKING_SPEED;
					
					//Let gravity take over
					//adjustY += HERO_WALKING_SPEED;
					
				} else if (_hero.jumpCount>=21) {
					//Turn jump off
					_hero.jumpRight = false;
					_hero.jumpCount = 0;
					
				}
				Log.d("GSTA","hero jumpcount - " + _hero.jumpCount);
			} else if (_hero.jumpLeft) {
				
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			

			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		//Collision detection
			//Save previous X,Y
			float previousX = _hero.x;
			float previousY = _hero.y;

			
			
			ArrayList<GameObjects> _potentialCollides;
			//_potentialCollides = CollidingWalls(_hero); //Find all walls in Hero's Current Tiles
			
			
			//Log.d("GSTA", "You are in the same tile as " + _potentialCollides.size());
			
			//This is used to determine the max distance away from an object the hero is
				//example, hero wants to move 6 pixels to the right. Wall is 5 pixels away, this will determine
				//that the hero should move 4 pixels, so that the hero is always flush with the wall when colliding.
			//This could be CPU intensive, I do not know. If we have 1,000 walls, this loop could go 1,000 times
			boolean bolBreak;
			boolean freeFall = true;
			//for (int testX = 0; testX <= Math.abs(adjustX); testX++) {
			for (int testX = Math.abs(adjustX); testX >= 0; testX--) {
				bolBreak = true;
				//Loops through test scenarios

					//previousX = _hero.x;

					if (adjustX>0){
						_hero.x = previousX + testX;
					} else if (adjustX<0) {
						_hero.x = previousX - testX;
					}

					_potentialCollides = CollidingGameObjects(_hero); //Update Walls based on testExample

					for (GameObjects GameObj : _potentialCollides) {

						if (CollisionDetection(_hero, GameObj)) {
							//go until hero is not crashing into a wall 
							//_hero.x = previousX;
							_hero.currentWall = GameObj;
//							Log.d("GSTA", "you are colliding : X");
							freeFall = false;
							GameObj.color = Color.RED;
							bolBreak = false;
							
							if (GameObj.vertical) {
								_hero.gravity = HERO_RIGHT;
							}
							
							break;
						} 

					}
					
					if (bolBreak){
//						if (testX==Math.abs(adjustX)) {
//							_hero.currentWall = null;
//						}
						break;
					}
			}
			
			
			for (int testY = Math.abs(adjustY); testY >= 0; testY--) {
				bolBreak = true;
				//Loops through test scenarios

					//previousX = _hero.x;

					if (adjustY>0){
						_hero.y = previousY + testY;
					} else if (adjustY<0) {
						_hero.y = previousY - testY;
					}

					_potentialCollides = CollidingGameObjects(_hero); //Update Walls based on testExample

					for (GameObjects GameObj : _potentialCollides) {

						if (CollisionDetection(_hero, GameObj)) {
							//If the hero would have crashed into the wall 
							//break out of loops and set hero position to be flush with wall
							
							//_hero.x = previousX;
							_hero.currentWall = GameObj;
							GameObj.color = Color.RED;
							freeFall = false;
//							Log.d("GSTA", "you are colliding : Y");
							bolBreak = false;
							break;
						} 

					}

					if (bolBreak) break;
			}
			

			
			
			if (freeFall) {
				_hero.currentWall = null;
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			//Adjust Moving Objects - Platforms, enemies, etc.
			
			int pathX, pathY;
			for (GameObjects _moving: _gameMoving) {
				if (_moving.type==(_moving.GAME_OBJECT_PLATFORM)) {
					ArrayList<Integer[]> pathPoints = _moving.getPathPoints();

					Integer[] points = pathPoints.get(_moving.pathPoint);
						pathX = points[0];
						pathY = points[1];
						
						if (pathX>_moving.x) {
							_moving.x += _moving.speed;
						} else if (pathX<_moving.x) {
							_moving.x -= _moving.speed;
						}

						
						if (pathY>_moving.y) {
							_moving.y += _moving.speed;
						} else if (pathY<_moving.y) {
							//Up
							_moving.y -= _moving.speed;
						}

						
						if (_hero.currentWall==_moving) {

							if (_hero.gravity==HERO_DOWN)  {
								_hero.y = _moving.y - _hero.getHeight() - 1;
							} else if (_hero.gravity==HERO_UP) {
								_hero.y = _moving.y + _moving.height + 1;
							}
							
//							Log.d("GSTA","1: " + adjustX);
							if (pathX>_moving.x) {
								_hero.x += _moving.speed;
							
							} else if (pathX<_moving.x) {
								_hero.x -= _moving.speed;
								
							}
//							Log.d("GSTA","2: " + adjustX);
							
//							if (pathY>_moving.y) {
//								//Down
//								
//								adjustY += (_moving.speed);
//								
//							} else if (pathY<_moving.y) {
//								//Up
//
//							}
//							if (CollisionDetection(_hero,_hero.currentWall)) {
//								Log.d("GSTA", "You are crashing into the wall thats moving");
//								Log.d("GSTA", "-----------------------------------");
//							}
						}
						
						
						//XY1 current position
						//XY2 Point position
						double distance = Math.sqrt(((pathX-_moving.x) * (pathX-_moving.x)) + ((pathY-_moving.y) * (pathY-_moving.y)));
//						Log.d("GSTA", "You are " + distance);
						if (distance<_moving.speed) {
							//you are close

							_moving.x = pathX;

							_moving.y = pathY;

							_moving.pathPoint += 1;
							//Log.d("GSTA", "" + _moving.pathPoint);
							//Log.d("GSTA", "" + pathPoints.size());
							if (_moving.pathPoint>=pathPoints.size()) {

								_moving.pathPoint = 0;
							}
						
						}	

				gameOverlay.updateGameObject(_moving);
				}
			}
			
			
			
			//Log.d("GSTA", "You are in : " + rowTL + "," + columnTL);
			
			//Log.d("GSTA","hero" + _hero.x + _hero.getWidth() + "," + _hero.y + _hero.getHeight());
			

//
//
//			//Same collision detection method for Y
//			bolBreak = false;			
//			for (int testY = 0; testY <= Math.abs(adjustY); testY++) {
//				previousY = _hero.y;
//				if (adjustY>0){
//					_hero.y += 1;
//				} else if (adjustY<0) {
//					_hero.y -= 1;
//				}
//				
//				_potentialCollides = CollidingWalls(_hero); //Update Walls based on testExample
//				
//				for (Walls wall3 : _potentialCollides) {
//					if (CollisionDetection(_hero, wall3)) {
//						_hero.y = previousY;
//						//Log.d("GSTA", "You have collided with YYYYYY " + wall3.X + "," + wall3.Y + "," + wall3.width);
//						bolBreak = true;
//						break;
//					}
//				}
//			
//				if (bolBreak) break;
//		}
			
			

		}
			
		
		public ArrayList<GameObjects> CollidingGameObjects(HeroObject hero) {
			
			ArrayList<GameObjects> _potentialCollides = new ArrayList<GameObjects>();

			//Top Left Point
			int rowTL = (int) Math.round(((int) _hero.y / 100) - 0.5);
			int columnTL = (int) Math.round(((int) _hero.x / 100) - 0.5);

			//Bottom right point
			int rowBR = (int) Math.round(((int) (_hero.y + _hero.getHeight()) / 100) - 0.5);
			int columnBR = (int) Math.round(((int) (_hero.x+ _hero.getWidth()) / 100) - 0.5);
			
//			Log.d("GSTA","------------------------------------------");
			for (int row = rowTL; row <= rowBR; row++) {
				for (int column = columnTL; column <= columnBR; column++) {
					//Hero is in overlay[row][column]
					ArrayList<GameObjects> tempGameObjects = gameOverlay.getGameObjects(row, column);
					if (tempGameObjects==null){
						//Log.d("GSTA", "Empty Space! No objects Present");
					} else {
						_potentialCollides.addAll(tempGameObjects);
					}
					
//					Log.d("GSTA" , "You are in Column - " + row + "," + column);
				}
			}
			return _potentialCollides;
		}
		
		
	//NEW Collision Detection. Formated right now for only Hero and Walls, can be easily made generic
		public boolean CollisionDetection (HeroObject hero, GameObjects GameObj) {
			//Create JAVA Rect for each object
			
																																				//			Log.d("GSTA", "Collision Detection------------");
			Rect heroRect = new Rect((int) hero.x,(int) hero.y, (int) (hero.x + hero.getWidth()), (int) (hero.y + hero.getHeight()));
			Rect wallRect = new Rect((int) GameObj.x,(int) GameObj.y, (int) (GameObj.x + GameObj.width), (int) (GameObj.y + GameObj.height));
			
																																				//			Log.d("GSTA", "wallRect - " + (int) wall.X + "," + (int) wall.Y + "," + (int) (wall.X + wall.width) + "," + (int) (wall.Y + wall.height));
																																				//			Log.d("GSTA", "heroRect - " + (int) hero.x + "," + (int) hero.y + "," + (int) (hero.x + hero.getWidth()) + "," + (int) (hero.y + hero.getHeight()));
			//Then use JAVA Rect Intersect to determine if intersect
			return (heroRect.intersect(wallRect));
		}
		
		//Determine distance based on 2 X,Y's
		public double distance(float X1, float Y1, float X2, float Y2) {
			return Math.sqrt(((X2 - X1) * ( X2 - X1)) + ((Y2 - Y1) * (Y2 - Y1)));
		}
		
		public void draw(Canvas c) {
			float centerX = getWidth()/2;
			float centerY = getHeight()/2;
			
			
			//Draw Background
			int height = getHeight();
			int width = getWidth();
				Rect rec = new Rect (0,0, width, height);
				Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
				backgroundPaint.setColor(Color.DKGRAY);
				c.drawRect(rec, backgroundPaint);


				
//Trying animation
//hero.update(System.currentTimeMillis());
//hero.draw(c);
	// where to draw the sprite



			//Draw Hero
				

			c.drawBitmap(bitHero, centerX, centerY, null);
			//Draw Obst
			//Draw walls
			
			//Loop through walls to draw
			
			for (GameObjects gameO : _gameO) {
				if (gameO.type==gameO.GAME_OBJECT_WALL || gameO.type==gameO.GAME_OBJECT_PLATFORM) {
				
					//New drawing method
				//offsetting walls. adjust walls based on previous position in relation to Hero. 
					float gameOX = 0;
					gameOX = gameO.x - _hero.x ;
					gameOX += centerX;
					
					float gameOY = 0;
					gameOY = gameO.y - _hero.y;
					gameOY += centerY;
					
					Rect recWall = new Rect((int) gameOX,(int) gameOY,(int) (gameOX + gameO.width) ,(int) (gameOY + gameO.height));
	
					Paint paintWall = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
					paintWall.setColor(gameO.color);
					c.drawRect(recWall, paintWall);
				} //else gameObjects are something else and draw accordingly
				
		}
			
			
			boolean enableOverlay = true;

			if (enableOverlay) {
				
				Paint paintOverlay = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
				paintOverlay.setColor(Color.LTGRAY);
				
				for (int overlay = 1; overlay <= 40; overlay++) {
					c.drawLine(overlay*100 - _hero.x, -100, overlay*100 - _hero.x, 8000, paintOverlay);
					c.drawLine(-100, overlay * 100 - _hero.y , 8000,  overlay * 100 - _hero.y, paintOverlay);
				}
				
			}
		
		
		}
		//OLD Collision Detection... was terrible.
		public boolean TestImpact(HeroObject object1, Walls object2) {
			double object1XRangeStart = (double) (object1.x);
			double object1XRangeEnd = (double) (object1.x + object1.getWidth());
			double object1YRangeStart = (double) (object1.y);
			double object1YRangeEnd = (double) (object1.y + object1.getHeight());
			
			
			double object2XRangeStart = (double) (object2.X);
			double object2XRangeEnd = (double) (object2.X + object2.width);
			double object2YRangeStart = (double) (object2.Y);
			double object2YRangeEnd = (double) (object2.Y + object2.height);
			
			
			
			
			//if (((object1XRangeStart >= object2XRangeStart) && (object1XRangeStart <= object2XRangeEnd)) && (object1YRangeStart >= object2YRangeStart) && (object1YRangeStart <= object2YRangeEnd)){
			if ((((object1XRangeStart >= object2XRangeStart) && (object1XRangeStart <= object2XRangeEnd)) && (object1YRangeStart >= object2YRangeStart) && (object1YRangeStart <= object2YRangeEnd)) || (((object1XRangeEnd >= object2XRangeStart) && (object1XRangeEnd <= object2XRangeEnd)) && (object1YRangeStart >= object2YRangeStart) && (object1YRangeStart <= object2YRangeEnd)) || (((object1XRangeStart >= object2XRangeStart) && (object1XRangeStart <= object2XRangeEnd)) && (object1YRangeEnd >= object2YRangeStart) && (object1YRangeEnd <= object2YRangeEnd)) || (((object1XRangeEnd >= object2XRangeStart) && (object1XRangeEnd <= object2XRangeEnd)) && (object1YRangeEnd >= object2YRangeStart) && (object1YRangeEnd <= object2YRangeEnd))) { 
			return true;
		}
		
		
		//////////////////////
		
		
		/* if ((object2XRangeStart >= object1XRangeStart && object2XRangeStart <= object1XRangeEnd) || (object2XRangeEnd >= object1XRangeStart && object2XRangeEnd <= object1XRangeEnd)) {
		Log.d("GSTA", "FIRST TEST");
		if ((object2YRangeStart >= object1YRangeStart && object2YRangeStart <= object1YRangeEnd) || (object2YRangeEnd >= object1YRangeStart && object2YRangeEnd <= object1YRangeEnd)) {
		Log.d("GSTA", "second TEST");
		//Log.d("PHYS", "Collide");
		return true;
		}
		}*/
		return false;
		}
	}
}



