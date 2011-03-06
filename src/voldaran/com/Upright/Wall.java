package voldaran.com.Upright;

import android.graphics.Color;

public class Wall extends GameObject {
	public static Wall fromString(String objectData){
		String data[] = objectData.split(",");
		Vec2d pos = new Vec2d(Integer.parseInt(data[0]), Integer.parseInt(data[1])).mul(1000);
		Vec2d extent = new Vec2d(Integer.parseInt(data[2]), Integer.parseInt(data[3])).mul(1000);
		return new Wall(pos, extent);
	}

	protected int colorCheckpoint;

	public Wall(Vec2d pos, Vec2d extent) {
		this(pos, extent, new Vec2d(0,0));
	}

	public Wall(Vec2d pos, Vec2d extent, Vec2d velocity) {
		super(pos, extent, velocity);
		colorCheckpoint = color;
	}
	
	@Override
	public String toString(){
		return "Wall: pos: " + pos + " extent: " + extent;
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
