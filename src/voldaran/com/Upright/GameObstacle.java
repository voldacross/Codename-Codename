package voldaran.com.Upright;

import android.graphics.Color;

public class GameObstacle extends GameObject{
	
	public GameObstacle(Vec2d pos, Vec2d extent) {
		super(pos, extent);
		color = Color.RED;
	}

}
