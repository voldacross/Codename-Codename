package voldaran.com.Upright;

import android.util.Log;
import android.view.MotionEvent;

public class UserInput {
	private Vec2d cameraSize, surfaceSize;
	private Vec2d mCurrentTouch  = new Vec2d();
	private Vec2d mPress  = new Vec2d();
	private Vec2d mDownPress = new Vec2d();
	
	public Input uInput = Input.NONE;
	
	public static enum Input {
		NONE,
		PRESS_RIGHT,
		PRESS_UP,
		PRESS_LEFT,
		PRESS_DOWN,
		
		DOWN_RIGHT,
		DOWN_UP,
		DOWN_LEFT,
		DOWN_DOWN
	}
	
	public UserInput(Vec2d c, Vec2d s) {
		cameraSize = c;
		surfaceSize = s;
		mPress.setVoid();
	}

	
	private int calcQuad (Vec2d point, Game mGame) {
		float x1, x2, x3, y1, y2, y3, a0, a1, a2, a3;
		if (mGame.gameState==Game.GameState.PLAYING) {
			x1 = (GameHero.hero.pos.x / 1000); y1 = (GameHero.hero.pos.y / 1000);
			x2 = (GameHero.hero.pos.x / 1000) + 1000; y2 = (GameHero.hero.pos.y / 1000) - 1000;
			x3 = (GameHero.hero.pos.x / 1000) + 1000; y3 = (GameHero.hero.pos.y / 1000) + 1000;			
			
		} else {
			x1 = surfaceSize.x / 2; y1 = surfaceSize.y / 2;
			x2 = surfaceSize.x; y2 = 0;
			x3 = surfaceSize.x; y3 = surfaceSize.y;
		}
		Log.d("GSTA", "" + x1 + "," + y1 + " "+ x2 + "," + y2+ " "+ x3 + "," + y3);
		for (int i = 0; i<4; i++) {
			a0 = Math.abs((x2-x1)*(y3-y1)-(x3-x1)*(y2-y1));
			a1 = Math.abs((x1-point.x)*(y2-point.y)-(x2-point.x)*(y1-point.y));
			a2 = Math.abs((x2-point.x)*(y3-point.y)-(x3-point.x)*(y2-point.y));
			a3 = Math.abs((x3-point.x)*(y1-point.y)-(x1-point.x)*(y3-point.y));
			
			if (Math.abs(a1+a2+a3-a0) <= 1/256) return i;
			
			switch (i) {
			case 0: //1
				if (mGame.gameState==Game.GameState.PLAYING) {
					
					x2 = (GameHero.hero.pos.x / 1000) + 1000; y2 = (GameHero.hero.pos.y / 1000) + 1000;
					x3 = (GameHero.hero.pos.x / 1000) - 1000; y3 = (GameHero.hero.pos.y / 1000) + 1000;	
				} else {
				
					x2 = 0; y2 = surfaceSize.y;
					x3 = surfaceSize.x; y3 = surfaceSize.y;
				}
				break;
			case 1: //2
				if (mGame.gameState==Game.GameState.PLAYING) {
					x2 = (GameHero.hero.pos.x / 1000) - 1000; y2 = (GameHero.hero.pos.y / 1000) - 1000;
					x3 = (GameHero.hero.pos.x / 1000) - 1000; y3 = (GameHero.hero.pos.y / 1000) + 1000;	
				} else {
				
					x2 = 0; y2 = 0;
					x3 = 0; y3 = surfaceSize.y;
				}
				break;
			case 2: //3
				if (mGame.gameState==Game.GameState.PLAYING) {
					x2 = (GameHero.hero.pos.x / 1000) + 1000; y2 = (GameHero.hero.pos.y / 1000) - 1000;
					x3 = (GameHero.hero.pos.x / 1000) - 1000; y3 = (GameHero.hero.pos.y / 1000) - 1000;	
				} else {
				
					x2 = 0; y2 = 0;
					x3 = surfaceSize.x; y3 = 0;
				}
				break;
			}

		}
		return 6;
	}

	
	public void UpdateInput (MotionEvent event, Game mGame) {
		final int action = event.getAction();
		
		switch (action & MotionEvent.ACTION_MASK) {
		
		case MotionEvent.ACTION_DOWN:
			mCurrentTouch.set(event.getX(),event.getY());
			
			mPress.setVoid();
			
			Log.d("GSTA", "" + calcQuad(mCurrentTouch, mGame));
			
			switch (calcQuad(mCurrentTouch, mGame)) {
			case 0:
				uInput = Input.DOWN_RIGHT;
				break;
				
			case 1:
				uInput = Input.DOWN_DOWN;
				break;
				
			case 2:
				uInput = Input.DOWN_LEFT;
				break;
			
			case 3:
				uInput = Input.DOWN_UP;
				break;
			}
			
			break;
			
		case MotionEvent.ACTION_UP:
			mCurrentTouch.set(event.getX(),event.getY());
			
			mPress.set(event.getX(), event.getY());
			Log.d("GSTA", "UP! " + mPress.x);
			switch (calcQuad(mCurrentTouch, mGame)) {
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
			
		case MotionEvent.ACTION_MOVE:
			mCurrentTouch.set(event.getX(),event.getY());
			
			switch (calcQuad(mCurrentTouch, mGame)) {
			case 0:
				uInput = Input.DOWN_RIGHT;
				break;
				
			case 1:
				uInput = Input.DOWN_DOWN;
				break;
				
			case 2:
				uInput = Input.DOWN_LEFT;
				break;
			
			case 3:
				uInput = Input.DOWN_UP;
				break;
			}
			break;
		}
		
	}
	
	public void Clear() {
		uInput = Input.NONE;
		mPress.setVoid();
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
		
		if ((uInput==Input.PRESS_LEFT)||(uInput==Input.PRESS_RIGHT)||(uInput==Input.PRESS_UP)||(uInput==Input.PRESS_DOWN)) {
			uInput = Input.NONE;
		}
		 
		return oInput;
	}



}

