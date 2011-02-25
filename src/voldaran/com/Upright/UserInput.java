package voldaran.com.Upright;

import android.util.Log;
import android.view.MotionEvent;

public class UserInput {
	

	private Vec2d cameraSize, surfaceSize;
	private Vec2d mCurrentTouch  = new Vec2d();
	private Vec2d mCurrentTouchH  = new Vec2d();
	private Vec2d mPress  = new Vec2d();
	private Vec2d mDownPress = new Vec2d();
	private Vec2d oldSpot = new Vec2d();
	private Vec2d oldSpotH = new Vec2d();
	private long oldTime, swipeTime;
//	private boolean swipeToggle = false;
	private final float convert = 1500 / (240 / (float) Game.displayMetrics.densityDpi);
	
	public Input uInput = Input.NONE;
	
	public static enum Input {
		PRESS_RIGHT_UP,
		PRESS_RIGHT,
		PRESS_RIGHT_DOWN,
		
		PRESS_LEFT_UP,
		PRESS_LEFT,
		PRESS_LEFT_DOWN,
		
		PRESS_MIDDLE,
		
		PRESS_DOWN,
		PRESS_DRAGGING,
		SWIPE_UP,
		SWIPE_DOWN,
		SWIPE_RIGHT,
		SWIPE_LEFT,
		NONE
	
	}
	
	public UserInput(Vec2d c, Vec2d s) {
		cameraSize = c;
		surfaceSize = s;
		
	}

	//Takes two points and calculates the Direction
	private int calcDirection (Vec2d mStart, Vec2d mEnd) { 
		
		long xDifference = Math.abs(mStart.x - mEnd.x);
		long yDifference = Math.abs(mStart.y - mEnd.y);
		
		if (xDifference>=yDifference) {
			if (mEnd.x<=mStart.x) {
				//LEFT
				return 2;
			} else {
				//RIGHT
				return 0;
			}
			
		} else if (xDifference<=yDifference) {
			if (mEnd.y<=mStart.y) {
				//UP
				return 3;
			} else {
				//DOWN
				return 1;
			}
		}
		return -1;
	}

	
	private int slicePiece(Vec2d touch) {
		int screenWidth = (int) surfaceSize.x;
		
		if (touch.x>screenWidth-(screenWidth/3)) { //Right side of screen
			return 2;
			
		} else if (touch.x<screenWidth/3) { //Left side of screen
			return 0;
			
		} else { //middle
			return 1;
			
		}

	}
	private boolean swipping=false;
//	private boolean setSwipe;
	
//	private boolean setSwipeTime() { TODO
//		if (setSwipe) return false;
//		swipeTime = System.currentTimeMillis();
//		setSwipe = true;
//		return true;
//	}
	
	public void UpdateInput (MotionEvent event) {
		final int action = event.getAction();
		
		int slice;
		
		
		
		switch (action & MotionEvent.ACTION_MASK) {
		
		case MotionEvent.ACTION_DOWN:
			
			mDownPress.set(event.getX(),event.getY());
			mCurrentTouch.set(event.getX(), event.getY());
			mCurrentTouchH.set(event.getX(), event.getY());
			mPress.set(event.getX(),event.getY());
			
			oldTime = System.currentTimeMillis();
			
			slice = slicePiece(mDownPress);

			switch (slice) {
			case 0:
				uInput = Input.PRESS_LEFT;
				break;
			case 1:
				uInput = Input.PRESS_MIDDLE;
				break;
			case 2:
				uInput = Input.PRESS_RIGHT;
				break;
			}
			break;
			
		case MotionEvent.ACTION_UP:
			
			if (uInput==Input.PRESS_DRAGGING) {
				swipping=false;
				int dir = calcDirection(mDownPress, mCurrentTouch);
				switch (dir) {
				case 0:
					uInput = Input.SWIPE_RIGHT;
					break;
					
				case 1:
					uInput = Input.SWIPE_DOWN;
					break;
					
				case 2:
					uInput = Input.SWIPE_LEFT;
					break;
					
				case 3:
					uInput = Input.SWIPE_UP;
					break;
					
				}
			} else uInput = Input.NONE;
			
			mDownPress.setVoid();
			break;
			
		case MotionEvent.ACTION_MOVE:
			
			oldSpot.set(mCurrentTouch);  //Used to measure speed
			mCurrentTouch.set(event.getX(), event.getY());
			
			Vec2d distance = mCurrentTouch.subtract(oldSpot);
			Vec2d speed = new Vec2d((float) ((float) distance.x / (float) (System.currentTimeMillis() - oldTime) * 1000),
									(float) ((float) distance.y / (float) (System.currentTimeMillis() - oldTime) * 1000));
			oldTime = System.currentTimeMillis();
			
			float ddd = ((mDownPress.x - mCurrentTouch.x) * (mDownPress.x - mCurrentTouch.x)) + ((mDownPress.y - mCurrentTouch.y) * (mDownPress.y - mCurrentTouch.y));
			int dir = calcDirection(mDownPress, mCurrentTouch);
			slice = slicePiece(mDownPress);  //Slice the original press was in, doesn't matter where it currently is

//			Log.d("GSTA", "speed " + speed.toString());
			
			for (int hs = 0; hs < event.getHistorySize();hs++) {
				oldSpotH.set(mCurrentTouchH);
				mCurrentTouchH.set(event.getHistoricalX(hs), event.getHistoricalY(hs));
				Vec2d distanceH = mCurrentTouchH.subtract(oldSpotH);
				Vec2d speedH = new Vec2d((float) ((float) distanceH.x / (float) (event.getHistoricalEventTime(hs) / 1000000) * 1000),
										 (float) ((float) distanceH.y / (float) (event.getHistoricalEventTime(hs) / 1000000) * 1000));
//				Log.d("GSTA", "speedH " + speedH.toString());
				
				if (((Math.abs(speedH.x)>convert)||(Math.abs(speedH.y)>convert))||(swipping)){
					swipping = ddd>8*8;
				}
			}
			
			if (((Math.abs(speed.x)>convert)||(Math.abs(speed.y)>convert))||(swipping)) {
//				setSwipeTime(); TODO
				dir = calcDirection(oldSpot, mCurrentTouch);
				Log.d("GSTA", "" + ((System.currentTimeMillis()-swipeTime)));
				swipping = ddd>8*8;//&&((System.currentTimeMillis()-swipeTime)<100); TODO
	
			}
			
//			Log.d("GSTA", "" + swipping);
			
			
			


			if (!swipping) {
				if (slice!=1) {
					if (ddd>8*8) {
						if (dir==1||dir==3) { //UP / DOWN
							switch (dir) {
							case 1:  //down
								if (slice==2)  {
									uInput=Input.PRESS_RIGHT_DOWN;
								} else if (slice==0) {
									uInput=Input.PRESS_LEFT_DOWN;
								}
								break;
							case 3:  //UP
								if (slice==2) {
									uInput=Input.PRESS_RIGHT_UP;
								} else if (slice==0) {
									uInput=Input.PRESS_LEFT_UP;
								}
								break;
							}
						} 
	
					}  else {
						switch (slice) {
						case 0:
							uInput = Input.PRESS_LEFT;
							break;
						case 2:
							uInput = Input.PRESS_RIGHT;
						}
					}
				}
			} else {
				
				int currentSlice = slicePiece(mCurrentTouch);
				
				if ((slice==0&&currentSlice==2)||(slice==2&&currentSlice==0)) { //if you've swipped clearing the middle slice, ignore swipe
					mDownPress.set(event.getX(),event.getY()); //act like pressing down for the first time
					//May need mPress set
					
					switch (currentSlice) { //Set press
					case 0:
						uInput = Input.PRESS_LEFT;
						swipping=false;
						break;
					case 2:
						uInput = Input.PRESS_RIGHT;
						swipping=false;
						break;
					}
				} else {
					uInput = Input.PRESS_DRAGGING;
				}
				
			}
			
			break;
		}
		
	}
	
	public void Clear() {
		uInput = Input.NONE;
		swipping = false;
	}

	private Vec2d convertCoord(Vec2d c) {
		Vec2d tempCurrentPress = new Vec2d(c);
		
		if (!tempCurrentPress.isVoid()) {
			double convertX = (double) ((double) cameraSize.x / (double) surfaceSize.x) * tempCurrentPress.x;
			double convertY = (double) ((double) cameraSize.y / (double) surfaceSize.y) * tempCurrentPress.y;
			tempCurrentPress.set(convertX, convertY);

		}
		
		return tempCurrentPress;
	}
	
	//Converts mPress to our native camera size and eats it
	public Vec2d getCurrentPress() {
		Vec2d tempCurrentPress = new Vec2d(convertCoord(mPress));
		mPress.setVoid();  
		return tempCurrentPress;
	}
	
	public Vec2d getCurrent() {
		return convertCoord(mCurrentTouch);
	}
	
	public Vec2d getDown() {
		return convertCoord(mDownPress);
	}
	
	public Input getInput() {
		Input oInput = uInput;
		if (uInput==Input.SWIPE_DOWN || uInput==Input.SWIPE_LEFT || uInput==Input.SWIPE_RIGHT || uInput==Input.SWIPE_UP) {
			uInput = Input.NONE;
		} 
//		if (oInput!=Input.NONE) Log.d("GSTA", "Input - " + oInput);
		 
		return oInput;
	}



}

