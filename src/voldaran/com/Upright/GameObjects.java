package voldaran.com.Upright;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

public class GameObjects {
	final public int GAME_OBJECT_WALL = 0000;
	final public int GAME_OBJECT_ARMOR = 0001;
	final public int GAME_OBJECT_ENEMY = 0002;
	final public int GAME_OBJECT_MOVING_ENEMY = 0003;
	final public int GAME_OBJECT_PLATFORM = 0004;
	final public int GAME_OBJECT_DOOR = 0005;
	final public int GAME_OBJECT_HERO = 0006;
	
	final public int GAME_OBJECT_VERTICAL = 90;
	final public int GAME_OBJECT_HORIZONTAL = 0;
	int type = 9999;
	
	public float x,y;
	public int speed, height, width;
	public int color = Color.WHITE;
	public int pathPoint = 0;
	boolean vertical = true;
	
	
	private ArrayList<Integer[]> pathPoints = new ArrayList<Integer[]>();
	
	

	public ArrayList<Integer[]> getPathPoints() {
		return pathPoints;
	}
	
	//Used for moving objects
	public void addPathPoint(float X, float Y) {
		Integer[] test = new Integer[2];
		test[0] = (int) X;
		test[1] = (int) Y;
		pathPoints.add(test);
	}
	
	
	
	
	public void Initialize (int Type) {
		type = Type;

	}
	
	public class Wall extends GameObjects {
		
		
	}
	
	

}
