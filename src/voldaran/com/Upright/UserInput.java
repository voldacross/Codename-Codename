package voldaran.com.Upright;


import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;




public class UserInput {
	
	public UserInput() {
		mCurrentTouch = new Vector2D();
		mDragStart = new Vector2D();
		mDragEnd = new Vector2D();
		
	}

	public Input uInput = Input.NONE;
	public Input previousInput;
	
	public Vector2D mCurrentTouch;
	
	private Vector2D mDragStart;
	private Vector2D mDragEnd;

	
	
	public enum Input {
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
	


	public Input calcDirection (Vector2D mStart, Vector2D mEnd) {
		
		int xDifference = (int) Math.abs((mStart.x - mEnd.x));
		int yDifference = (int) Math.abs((mStart.y - mEnd.y));
		
		if (xDifference>=yDifference) {
			if (mEnd.x<=mStart.x) {
				return Input.SWIPE_LEFT;
			} else {
				return Input.SWIPE_RIGHT;
			}
			
		} else if (xDifference<=yDifference) {
			if (mEnd.y<=mStart.y) {
				return Input.SWIPE_UP;
			} else {
				return Input.SWIPE_DOWN;
			}
		}
		return null;
	}

	public void UpdateInput (MotionEvent event, int screenWidth, int screenHeight) {
		final int action = event.getAction();
		
		
		
		fingerDown = true;
	

		
		mCurrentTouch.set(event.getX(), event.getY());
		//mCurrentTouch = new Vector2D(event.getX(), event.getY());
		
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mDragStart.set(event.getX(), event.getY());
			
			if (mCurrentTouch.x>screenWidth-(screenWidth/3)) {
				uInput = Input.PRESS_RIGHT;
			} else if (mCurrentTouch.x<screenWidth/3) {
				uInput = Input.PRESS_LEFT;
			} else {
				uInput = Input.PRESS_MIDDLE;
			}
			break;

			
		case MotionEvent.ACTION_UP:
			
				if (uInput==Input.PRESS_DRAGGING) {
					mDragEnd.set(event.getX(), event.getY());
					uInput = calcDirection(mDragStart, mDragEnd);
				}

			break;
			
		case MotionEvent.ACTION_MOVE:
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
	
	public Input getInput() {
		
		if (uInput==Input.PRESS_RIGHT) {
		} else if (uInput==Input.PRESS_MIDDLE) {
		} else if (uInput==Input.PRESS_LEFT) {
		} else if (uInput==Input.SWIPE_UP) {
		} else if (uInput==Input.SWIPE_DOWN) {
		} else if (uInput==Input.SWIPE_RIGHT) {
		} else if (uInput==Input.SWIPE_LEFT) {
		} else if (uInput==Input.NONE) {
		}
		
		Input oldInput = uInput;
		uInput = Input.NONE;
		return oldInput;
		
	}



}

