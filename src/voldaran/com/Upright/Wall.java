package voldaran.com.Upright;

import android.graphics.Color;

public class Wall extends GameObject {
	protected int colorCheckpoint;

	public Wall(Vec2d pos, Vec2d extent) {
		this(pos, extent, new Vec2d(0,0));
	}

	public Wall(Vec2d pos, Vec2d extent, Vec2d velocity) {
		super(pos, extent, velocity);
		colorCheckpoint = color;
	}

	@Override
	public void toggle(GameObject o){
		super.touch(o);
		if(o instanceof GameHero){
			if(color == Color.WHITE) color = Color.GREEN;
			else color = Color.WHITE;
		}
	}

	@Override
	protected void saveCheckpoint(){
		colorCheckpoint = color;
	}
	
	@Override
	protected void restoreCheckpoint(){
		color = colorCheckpoint;
	}
	
}
