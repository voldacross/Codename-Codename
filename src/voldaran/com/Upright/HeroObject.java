package voldaran.com.Upright;

import android.graphics.Bitmap;

public class HeroObject {
	float x, y;
	//int direction = 1;
	Bitmap bitHero;
	boolean freeFall = true;
	int walkingDirection;
	GameObjects currentWall;
	int facingDirection = 0;
	boolean walking = false;
	int gravity = 1;
	int directionLocked=999;
	
	int movementState = 0;
	
	boolean jumpRight = false;
	boolean jumpLeft = false;
	
	int jumpCount = 0;
	
	int animationCount;
	int animationType;
	
	public int getHeight() {
		return bitHero.getHeight();
	}
	
	public int getWidth() {
		return bitHero.getWidth();
	}
	
	
	
}
