package voldaran.com.Upright;

import java.util.ArrayList;

public class MovingObject extends GameObject{
	public static ArrayList<MovingObject> movingObjects = new ArrayList<MovingObject>();
	
	public static void updateAll(){
		for(MovingObject o : MovingObject.movingObjects){
			o.update();
		}
	}
	
	protected Vec2d posCheckpoint = new Vec2d();
	protected Vec2d velocityCheckpoint = new Vec2d();
	protected GameObject newGround = null;
	
	public MovingObject(Vec2d pos, Vec2d extent, Vec2d velocity){
		super(pos, extent, velocity);
		MovingObject.movingObjects.add(this);
		posCheckpoint.set(pos);
		velocityCheckpoint.set(velocity);
	}

	public MovingObject(Vec2d pos, Vec2d extent){
		this(pos, extent, new Vec2d(0,0));
	}
	
	public MovingObject applyForce(Vec2d force){
		velocity.add(force);
		return this;
	}
	
	@Override
	public void update(){
		pos.add(velocity);
		setSides();
	}
	
	protected class Collision{
		public float time;
		public Vec2d correctedVelocity;
		public GameObject collider;
		
		public Collision(float time, Vec2d correctedVelocity, GameObject collider){
			this.time = time;
			this.correctedVelocity = correctedVelocity;
			this.collider = collider;
		}
		
	}
	
	protected boolean overlaps(GameObject b){
		return this.right > b.left && this.left < b.right && this.bottom > b.top && this.top < b.bottom;
	}
	
	public void collisionAvoid(){
		Collision collision = null;
		Collision firstcollision;
		
		firstcollision = null;
		GameObject contact = null;
		for(GameObject o : GameObject.gameObjects){
			if((o != this)&&(o.solid)) {
				collision = sweepOverlaps(o);
				if((collision != null) && (firstcollision == null || collision.time < firstcollision.time)){
					firstcollision = collision;
				}
			} else if ((!o.solid)&&(overlaps(o))) {
				contact = o;
			}
		}
		if(firstcollision != null){
			this.velocity = firstcollision.correctedVelocity;
			this.touch(firstcollision.collider);
			firstcollision.collider.touch(this);
   			GameObstacleGen.adjustLasers();
		} else if (contact!=null) {
			this.touch(contact);
		}
	}
	
	protected Collision sweepOverlaps(GameObject b){
//		if (!b.solid) return new Collision(0,b.velocity,b);
		if(overlaps(b)) return null;
		
		float[] to = {-1, -1};								// No collision flag
		float[] ts = {1, 1};								// No separation flag
		
		Vec2d v = new Vec2d(velocity).sub(b.velocity);		// calculate from b's frame of reference
		
		if((b.right <= left) && (v.x < 0))					// approaches b from the right
			to[0] = (b.right - left) / (float)v.x;			// time of x overlap
		else if((right <= b.left) && (v.x > 0))				// approaches b from the left
			to[0] = (b.left - right) / (float)v.x;			// time of x overlap
		else if(!(right > b.left && left < b.right))		// if no move to x overlap, must start x overlapped
			return null;
		
		if((b.bottom <= top) && (v.y < 0))					// approaches b from the bottom
			to[1] = (b.bottom - top) / (float)v.y;			// time of y overlap
		else if((bottom <= b.top) && (v.y > 0))				// approaches b from the top
			to[1] = (b.top - bottom) / (float)v.y;			// time of y overlap
		else if(!(bottom > b.top && top < b.bottom))		// if no move to y overlap, must start y overlapped
			return null;

		float tom = to[0] > to[1]?to[0]:to[1];				// time of latter overlapped (when both x and y overlap i.e. collision)
		if(tom > 1) return null;									// if > 1 it won't happen in this frame
		
		if((right > b.left) && (v.x < 0))                  	// moves away from b to the left
			ts[0] = (b.left - right) / (float)v.x;		  	// time of x separation
		else if((b.right > left) && (v.x > 0))             	// moves away from b to the right
			ts[0] = (b.right - left) / v.x;					// time of x separation

       //calculate time of y overlap end           
		if((bottom > b.top) && (v.y < 0))                  	// moves away from b to the top
			ts[1] = (b.top - bottom) / (float)v.y;			// time of y separation			
		else if((b.bottom > top) && (v.y > 0))             	// moves away from b to the bottom
			ts[1] = (b.bottom - top) / v.y;					// time of y separation
		
		//clamp time of separation to the end of the frame
		if(ts[0] > 1) ts[0] = 1;							
		if(ts[1] > 1) ts[1] = 1;
		float tsm = ts[0] < ts[1]?ts[0]:ts[1];				// get the first separation (i.e. end of collision)
		if(tom >= tsm) return null;								// if we don't overlap before we separate, then we didn't collide
		
		// calculate the avoidance velocity
		if(to[0] > to[1]) 
			return new Collision(tom, new Vec2d(velocity.x * tom + b.velocity.x * (tsm - tom), velocity.y), b);
		else 
			return new Collision(tom, new Vec2d(velocity.x, velocity.y * tom + b.velocity.y * (tsm - tom)), b);
	}
	
	@Override
	protected void saveCheckpoint(){
		super.saveCheckpoint();
		posCheckpoint.set(pos);
		velocityCheckpoint.set(velocity);
	}
	
	@Override
	protected void restoreCheckpoint(){
		super.restoreCheckpoint();
		pos.set(posCheckpoint);
		velocity.set(velocityCheckpoint);
		setSides();
	}
}