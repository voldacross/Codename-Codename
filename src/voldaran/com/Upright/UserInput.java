package voldaran.com.Upright;


import android.view.MotionEvent;
import android.view.SurfaceView;



public class UserInput {
	private static SurfaceView surface;
	
	public UserInput(SurfaceView surface) {
		mCurrentTouch = new Vec2d();
		mDragStart = new Vec2d();
		mDragEnd = new Vec2d();
		UserInput.surface = surface;
	}

	public Input uInput = Input.NONE;
	public Input previousInput;
	
	public Vec2d mCurrentTouch;
	
	private Vec2d mDragStart;
	private Vec2d mDragEnd;

	
	
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

	public void UpdateInput (MotionEvent event) {
		final int action = event.getAction();
		
		previousInput = uInput;
		
		fingerDown = true;
		
        
		int screenWidth = UserInput.surface.getWidth();

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

				if (uInput.equals(Input.PRESS_DRAGGING)) {
					mDragEnd.set(event.getX(), event.getY());
					uInput = calcDirection(mDragStart, mDragEnd);
				} else {
					uInput = Input.NONE;
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
		Input oInput = uInput;
		if (uInput==Input.SWIPE_DOWN || uInput==Input.SWIPE_LEFT || uInput==Input.SWIPE_RIGHT || uInput==Input.SWIPE_UP) {
			uInput = Input.NONE;
		} 
		return oInput;
	}



}

