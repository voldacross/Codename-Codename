package voldaran.com.Upright;

import android.graphics.Color;

public class GameObstacle extends GameObject{
	public static GameObstacle addObstacle(long x, long y, long w, long h){
		Vec2d pos = new Vec2d(x, y).mul(1000);
		Vec2d extent = new Vec2d(w, h).mul(1000);
		return new GameObstacle(pos, extent);
	}
	
	public GameObstacle(Vec2d pos, Vec2d extent) {
		super(pos, extent);
		color = Color.RED;
	}

}
