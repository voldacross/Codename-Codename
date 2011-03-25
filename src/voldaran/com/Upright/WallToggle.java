package voldaran.com.Upright;

import java.util.ArrayList;

import android.graphics.Color;
import android.util.Log;

public class WallToggle extends Wall {
	public static ArrayList<WallToggle> WallToggles = new ArrayList<WallToggle>();

	public static WallToggle fromString(String objectData){
		String data[] = objectData.split(",");
		Vec2d pos = new Vec2d(Integer.parseInt(data[0]), Integer.parseInt(data[1])).mul(1000);
		Vec2d extent = new Vec2d(Integer.parseInt(data[2]), Integer.parseInt(data[3])).mul(1000);
		boolean lit = (data.length > 4 && Integer.parseInt(data[4]) == 1 );
		return new WallToggle(pos, extent, lit);
	}
	
	public static void setLists() {
		for(WallToggle o: WallToggle.WallToggles){
				((WallToggle) o).createList();
		}
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
		WallToggles.add(this);
	}
	
	private ArrayList<Wall> walls = new ArrayList<Wall>();
	private ArrayList<Wall> wallbucket = new ArrayList<Wall>();
	private ArrayList<Wall> tempbucket = new ArrayList<Wall>();
	
	public void createList() {
		walls.add(this);
		wallbucket.add(this);
		
		while (!wallbucket.isEmpty()) {
			for(Wall o : wallbucket){
				Log.d("GSTA", "Wall bucket ");
					for(GameObject x: GameObject.gameObjects){
						
						if ((x instanceof Wall) && ((o.touching(x))||(overlaps(x, o))) && (!walls.contains(x))) {
//						if(x instanceof Wall){
//							if ((o.touching(x))||(overlaps(x, o))) {
//								if (!walls.contains(x)) {
									walls.add((Wall) x);
									if (x instanceof WallToggle) tempbucket.add((Wall) x);
								}
//							}
//						}
					}
			}
			wallbucket.clear(); wallbucket.addAll(tempbucket); tempbucket.clear();
		}
	}
	
	@Override
	public String toString(){
		int lit = (color == ON) ? 1:0;
		return "twall: pos: " + pos + " extent: " + extent + " lit: " + lit;
	}

	@Override
	public void touch(GameObject o){
//		super.touch(o);
		if(o instanceof GameHero){
//			for(GameObject oo: GameObject.gameObjects){
//				if(oo instanceof Wall && oo != this){
//					if ((touching(oo))||(overlaps(oo)))
//						oo.toggle();
//				}
//			}
			for(GameObject w: walls){
				w.toggle();
			}
			
		}
	}
	
	protected boolean overlaps(GameObject b, GameObject a){
		return a.right > b.left && a.left < b.right && a.bottom > b.top && a.top < b.bottom;
	}
	
	
	protected boolean overlaps(GameObject b){
		return this.right > b.left && this.left < b.right && this.bottom > b.top && this.top < b.bottom;
	}
	
}
