package voldaran.com.Upright;

import java.util.ArrayList;

public class MovingObject extends GameObject{
	public static ArrayList<MovingObject> movingObjects = new ArrayList<MovingObject>();
	
	public static void updateObjects(){
		for(MovingObject o : MovingObject.movingObjects){
			o.update();
		}
	}

	public MovingObject(Vec2d pos, Vec2d extent, Vec2d velocity){
		super(pos, extent, velocity);
		MovingObject.movingObjects.add(this);
	}

	public MovingObject(Vec2d pos, Vec2d extent){
		super(pos, extent);
		MovingObject.movingObjects.add(this);
	}
	
	public MovingObject applyForce(Vec2d force){
		velocity.add(force);
		return this;
	}
	
	public void update(){
		pos.add(velocity);
	}
}