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

	public void UpdateInput (MotionEvent event) {
		final int action = event.getAction();
		
		previousInput = uInput;
		
		fingerDown = true;
		
Log.d("GSTA", "1111111111 " + voldaran.com.Upright.Game.);
        
		int screenWidth = 0;
		int screenHeight = 0;

		
		mCurrentTouch.set(event.getX(), event.getY());
		//mCurrentTouch = new Vector2D(event.getX(), event.getY());
		
		//Log.d("GSTA", "" + screenWidth);

		
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
			fingerDown = false;
			
			if (userSwipped) {
				//mDragEnd = new Vector2D
				mDragEnd.set(event.getX(), event.getY());
				
//				float ddd = ((mDragStart.x - mDragEnd.x) * (mDragStart.x - mDragEnd.x)) + ((mDragStart.y - mDragEnd.y) * (mDragStart.y - mDragEnd.y));
//				if (ddd > 35 * 35) {

				if (uInput.equals(Input.PRESS_DRAGGING)) {
					uInput = calcDirection(mDragStart, mDragEnd);
					//Log.d("GSTA", "" + uInput);
				}

				
			}
			break;
			
		case MotionEvent.ACTION_MOVE:
			float ddd = ((mDragStart.x - mCurrentTouch.x) * (mDragStart.x - mCurrentTouch.x)) + ((mDragStart.y - mCurrentTouch.y) * (mDragStart.y - mCurrentTouch.y));
			if (ddd>35*35) {
				uInput = Input.PRESS_DRAGGING;
				//Log.d("GSTA", "YOU ARE PASS 35");
			} else {
				uInput = previousInput;
			}
			break;
			
		}
		
	}



}

