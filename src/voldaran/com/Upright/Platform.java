package voldaran.com.Upright;

import java.util.ArrayList;

import android.util.Log;

public class Platform extends MovingObject {
	private ArrayList<Vec2d> steps = new ArrayList<Vec2d>();
	private int speed;
	private int step = 0;
	
	public Platform(Vec2d pos, Vec2d extent){
		this(pos, extent, 4000);
	}
	
	public Platform(Vec2d pos, Vec2d extent, int speed){
		super(pos, extent);
		this.speed = speed;
		steps.add(new Vec2d(pos));
	}
	
	public void addStep(long x, long y){
		Vec2d step = new Vec2d(steps.get(steps.size() - 1)).add(x, y);
		steps.add(step);
		Log.d("Platform", "Adding step " + step);
		if(steps.size() == 2) checkStep();
	}

	public void checkStep(){
		if(new Vec2d(pos).sub(steps.get(step)).len2() <= speed * speed){
			Vec2d stepVec;
			pos.x = steps.get(step).x;
			pos.y = steps.get(step).y;
			step = (step + 1) % steps.size();
			stepVec = new Vec2d(steps.get(step)).sub(pos).normalize().mul(speed / 1000);
			velocity = stepVec;
		}		
	}
	
	@Override
	public void update(){
		super.update();
		Log.d("Platform", "Before update pos " + pos + " velocity " + velocity + " step " + step + " : " + steps.get(step));
		checkStep();
		Log.d("Platform", "After update pos " + pos + " velocity " + velocity + " step " + step + " : " + steps.get(step));
	}
}
