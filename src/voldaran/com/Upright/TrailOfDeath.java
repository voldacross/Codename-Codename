package voldaran.com.Upright;

public class TrailOfDeath extends GameObstacle{
	
	public static boolean enabled = false;
	
	public static void updateTrail(GameHero h) {
		Vec2d Start = new Vec2d(h.posCheckpoint);
		Vec2d End = new Vec2d(h.pos);
		
		Vec2d tPOS = new Vec2d((Start.x + End.x) / 2, (Start.y + End.y) / 2);
		Vec2d tEXT = new Vec2d(Math.abs((Start.x - End.x) / 2), Math.abs((Start.y - End.y) / 2));
		if (tEXT.x==0) tEXT.x = 4000; else tEXT.y = 4000;
		
		if (enabled) new TrailOfDeath(tPOS, tEXT);
	}
	
	public boolean checkpoint = false;
	
	public TrailOfDeath(Vec2d pos, Vec2d extent) {
		super(pos, extent);
	}
	
	@Override
	public void saveCheckpoint() {
		checkpoint = true;
		
	}
	
	@Override
	public void restoreCheckpoint() {
		if (!checkpoint) GameObject.gameObjects.remove(this);
	}
	
	

}
