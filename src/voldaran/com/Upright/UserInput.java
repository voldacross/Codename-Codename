package voldaran.com.Upright;


import android.view.MotionEvent;
import android.view.SurfaceView;




public class UserInput {
	private static SurfaceView surface;
	private Vec2d cameraSize, surfaceSize;
	
	public UserInput(Vec2d c, Vec2d s) {
		mCurrentTouch = new Vec2d();
		mDragStart = new Vec2d();
		mDragEnd = new Vec2d();
		mDragVelocity = new Vec2d();
		mDragVelocityStart = new Vec2d();
//		UserInput.surface = surface;
		
		cameraSize = c;
		surfaceSize = s;
		
	}

	public Input uInput = Input.NONE;
	public Input previousInput;
	
	public Vec2d mCurrentTouch;
	
	private Vec2d mDragStart;
	private Vec2d mDragEnd;
	private Vec2d mCurrentPress;
	
	public Vec2d mDragVelocity;
	public Vec2d mDragVelocityStart;
	
	public static enum Input {
		PRESS_RIGHT,
		PRESS_MIDDLE,
		PRESS_LEFT,
		PRESS_DRAGGING,
		SWIPE_UP,
		SWIPE_DOWN,
		SWIPE_RIGHT,
		SWIPE_LEFT,
		NONE
	
	}
	
	boolean fingerDown = false;
	boolean fingerDrag = false;
	boolean userSwipped = false;
	boolean userDragActive = false;
	


	public Input calcDirection (Vec2d mStart, Vec2d mEnd) {
		
		long xDifference = Math.abs(mStart.x - mEnd.x);
		long yDifference = Math.abs(mStart.y - mEnd.y);
		
		if (xDifference>=yDifference) {
			if (mEnd.x<=mStart.x) {
				//LEFT
				return Input.SWIPE_LEFT;
			} else {
				//RIGHT
				return Input.SWIPE_RIGHT;
			}
			
		} else if (xDifference<=yDifference) {
			if (mEnd.y<=mStart.y) {
				//UP
				return Input.SWIPE_UP;
			} else {
				//DOWN
				return Input.SWIPE_DOWN;
			}
		}
		return null;
	}

	public void Clear() {
		uInput = Input.NONE;
	}
	public void UpdateInput (MotionEvent event) {
		final int action = event.getAction();
		
		previousInput = uInput;
		
		fingerDown = true;
		

		int screenWidth = (int) surfaceSize.x;
		mCurrentTouch.set(event.getX(), event.getY());
		
		
		switch (action & MotionEvent.ACTION_MASK) {
		
		case MotionEvent.ACTION_DOWN:
			mDragVelocityStart.set(mCurrentTouch.x, mCurrentTouch.y);
			mDragStart.set(mCurrentTouch.x, mCurrentTouch.y);
			mCurrentPress = new Vec2d(mCurrentTouch.x, mCurrentTouch.y);
			
			if (mCurrentTouch.x>screenWidth-(screenWidth/3)) {
				uInput = Input.PRESS_RIGHT;
				
			} else if (mCurrentTouch.x<screenWidth/3) {
				
				uInput = Input.PRESS_LEFT;
			} else {
				
				uInput = Input.PRESS_MIDDLE;
			}
			break;

			
		case MotionEvent.ACTION_UP:
			fingerDown = false;
			mDragVelocity.clear();
				if (uInput.equals(Input.PRESS_DRAGGING)) {
					mDragEnd.set(mCurrentTouch.x, mCurrentTouch.y);
					uInput = calcDirection(mDragStart, mDragEnd);
				} else {
					uInput = Input.NONE;
				}
			
			break;
			
		case MotionEvent.ACTION_MOVE:
			
			mDragVelocity = (mCurrentTouch.subtract(mDragVelocityStart));
			mDragVelocityStart.set(mCurrentTouch.x, mCurrentTouch.y);
			float ddd = ((mDragStart.x - mCurrentTouch.x) * (mDragStart.x - mCurrentTouch.x)) + ((mDragStart.y - mCurrentTouch.y) * (mDragStart.y - mCurrentTouch.y));
			
			if (ddd>35*35) {
				uInput = Input.PRESS_DRAGGING;
				
			} else {
				if (mCurrentTouch.x>screenWidth-(screenWidth/3)) {
					uInput = Input.PRESS_RIGHT;
					
				} else if (mCurrentTouch.x<screenWidth/3) {
					
					uInput = Input.PRESS_LEFT;
				} else {
					
					uInput = Input.PRESS_MIDDLE;
				}
			}
			break;
			
		}
		
	}
	
	public Vec2d getCurrentPress() {
		Vec2d tempCurrentPress = mCurrentPress;
		if (mCurrentPress!=null) {
			double convertX = (double) ((double) cameraSize.x / (double) surfaceSize.x) * mCurrentPress.x;
			double convertY = (double) ((double) cameraSize.y / (double) surfaceSize.y) * mCurrentPress.y;
			tempCurrentPress.set(convertX, convertY);
		}
		mCurrentPress = null;
		return tempCurrentPress;
	}
	
	public Input getInput() {
		Input oInput = uInput;
		if (uInput==Input.SWIPE_DOWN || uInput==Input.SWIPE_LEFT || uInput==Input.SWIPE_RIGHT || uInput==Input.SWIPE_UP) {
			uInput = Input.NONE;
		} 
		return oInput;
	}



}
