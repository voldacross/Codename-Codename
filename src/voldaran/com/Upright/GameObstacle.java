package voldaran.com.Upright;

import android.graphics.Color;

public class GameObstacle extends GameObject{
	
	public GameObstacle(Vec2d pos, Vec2d extent) {
		super(pos, extent);
		super.obstacle = true;
		super.color = Color.RED;
		super.checkPointColor = Color.RED;
	}

}
