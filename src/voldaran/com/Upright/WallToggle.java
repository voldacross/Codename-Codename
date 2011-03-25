package voldaran.com.Upright;

import android.graphics.Color;

public class WallToggle extends Wall {

	public static WallToggle fromString(String objectData){
		String data[] = objectData.split(",");
		Vec2d pos = new Vec2d(Integer.parseInt(data[0]), Integer.parseInt(data[1])).mul(1000);
		Vec2d extent = new Vec2d(Integer.parseInt(data[2]), Integer.parseInt(data[3])).mul(1000);
		boolean lit = (data.length > 4 && Integer.parseInt(data[4]) == 1 );
		return new WallToggle(pos, extent, lit);
	}

	protected int colorCheckpoint;
	
	public WallToggle(Vec2d pos, Vec2d extent, boolean lit) {
		this(pos, extent, new Vec2d(0,0), lit);
	}

	public WallToggle(Vec2d pos, Vec2d extent, Vec2d velocity, boolean lit) {
		super(pos, extent, lit);
		OFF = Color.CYAN;
		ON = Color.BLUE;
		updateColors(lit);
	}
	
	@Override
	public String toString(){
		int lit = (color == ON) ? 1:0;
		return "twall: pos: " + pos + " extent: " + extent + " lit: " + lit;
	}

	@Override
	public void touch(GameObject o){
		super.touch(o);
		if(o instanceof GameHero){
			for(GameObject oo: GameObject.gameObjects){
				if(oo instanceof Wall && oo != this){
					if ((touching(oo))||(overlaps(oo)))
						oo.toggle();
				}
			}
		}
	}
	
	protected boolean overlaps(GameObject b){
		return this.right > b.left && this.left < b.right && this.bottom > b.top && this.top < b.bottom;
	}
	
}
