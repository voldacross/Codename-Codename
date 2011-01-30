package voldaran.com.Upright;

import android.graphics.Bitmap;

public class GameHero extends GameObject{
	
	public Bitmap bitHero;

	public Vector2D velocity = new Vector2D(0,0);

	public GameHero(Vector2D pos, Extent extent) {
		super(pos, extent);
	}
	
	public GameHero(Vector2D v, Extent extent, Vector2D vel) {
		super(v, extent);
		velocity = vel;
	}

	
	public void update() {
		super.pos.add(velocity);
		
		//Collision detection!!
	}
	
}
