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
	private long oldTime;
	private int dir = 0;
	
//	private boolean swipeToggle = false;
	private final float convert = 1500 / (240 / (float) Game.displayMetrics.densityDpi);
	
	public Input uInput = Input.NONE;
	
	public static enum Input {
		PRESS_RIGHT,
		PRESS_LEFT,
		PRESS_UP,
		PRESS_DOWN,
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
		int screenHeight = (int) surfaceSize.y;
		
		if (touch.x>screenWidth-(screenWidth/3)) { //Right side of screen
			return 0;
			
		} else if (touch.x<screenWidth/3) { //Left side of screen
			return 2;
			
		} else if ((touch.x>screenWidth/3)&&(touch.x<screenWidth-(screenWidth/3))) { //middle
			if (touch.y>screenHeight/2) {
				return 1; //DOWN
			} else
				return 3; //UP
		}
			
		return 6;

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
		
		switch (action & MotionEvent.ACTION_MASK) {
		
		case MotionEvent.ACTION_DOWN:
			mCurrentTouch.set(event.getX(),event.getY());
			mPress.set(event.getX(), event.getY());
			
			Log.d("GSTA", "" + slicePiece(mCurrentTouch));
			switch (slicePiece(mCurrentTouch)) {
			case 0:
				uInput = Input.PRESS_RIGHT;
				break;
				
			case 1:
				uInput = Input.PRESS_DOWN;
				break;
				
			case 2:
				uInput = Input.PRESS_LEFT;
				break;
			
			case 3:
				uInput = Input.PRESS_UP;
				break;
			}
			
			break;
			
		case MotionEvent.ACTION_UP:
			mCurrentTouch.set(event.getX(),event.getY());
			mPress.setVoid();

			break;
			
		case MotionEvent.ACTION_MOVE:
			
			
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
		uInput = Input.NONE;
		 
		return oInput;
	}



}

