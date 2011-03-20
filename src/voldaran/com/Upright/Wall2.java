package voldaran.com.Upright;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Wall2 extends Wall {
	
	private boolean firstBulbLit = false;
	private boolean secondBulbLit = false;
	private boolean horizontal = true;
	private int firstBulbColor = Color.WHITE;
	private int secondBulbColor = Color.WHITE;
	
	public static Wall2 fromString(String objectData){
		String data[] = objectData.split(",");
		Vec2d pos = new Vec2d(Integer.parseInt(data[0]), Integer.parseInt(data[1])).mul(1000);
		Vec2d extent = new Vec2d(Integer.parseInt(data[2]), Integer.parseInt(data[3])).mul(1000);
		boolean lit = (data.length > 4 && Integer.parseInt(data[4]) == 1 );
		return new Wall2(pos, extent, lit);
	}

	protected int colorCheckpoint;
	
	public Wall2(Vec2d pos, Vec2d extent, boolean lit) {
		this(pos, extent, new Vec2d(0,0), lit);
		if ((extent.x / 1000)==8) horizontal = false;
		Log.d("GSTA", extent.toString() + ", " + horizontal);
		
	}

	public Wall2(Vec2d pos, Vec2d extent, Vec2d velocity, boolean lit) {
		super(pos, extent, lit);
	}
	
	@Override
	public String toString(){
		int lit = (color == ON) ? 1:0;
		return "Wall: pos: " + pos + " extent: " + extent + " lit: " + lit;
	}

	@Override
	public void toggle(GameObject o){
		super.touch(o);
		if(o == GameHero.hero){

			switch(((GameHero)o).lastDirection){
	    		case RIGHT:
	    			if (firstBulbLit) {
	    				firstBulbLit = false;
	    				firstBulbColor = OFF;
	    			} else {
	    				firstBulbLit = true; 
	    				firstBulbColor = ON;
	    			}
	    			break;
	    		case UP:
	    			if (secondBulbLit) {
	    				secondBulbLit = false;
	    				secondBulbColor = OFF;
	    			} else {
	    				secondBulbLit = true;
	    				secondBulbColor = ON;
	    			}
	    			break;
	    		case LEFT:
	    			if (secondBulbLit) {
	    				secondBulbLit = false;
	    				secondBulbColor = OFF;
	    			} else {
	    				secondBulbLit = true;
	    				secondBulbColor = ON;
	    			}
	    			break;
	    		case DOWN:
	    			if (firstBulbLit) {
	    				firstBulbLit = false;
	    				firstBulbColor = OFF;
	    			} else {
	    				firstBulbLit = true; 
	    				firstBulbColor = ON;
	    			}
	    			break;
	    		}				
				
		}
	}
	
	@Override
	public boolean checkWin() {
		return (firstBulbLit&&secondBulbLit);
	}
	
	@Override
	public void draw(Canvas c){
		Rect recBulb1 = null;
		Rect recBulb2 = null;
		
		int lineX1 = 0;
		int lineX2 = 0;
		int lineY1 = 0;
		int lineY2 = 0;
		
		if (!horizontal) {
			recBulb1 = new Rect((int)((left) / 1000), 
	                  (int)((top) / 1000), 
	                  (int)((right - extent.x)/ 1000), 
	                 (int)((bottom) / 1000));
			
			recBulb2 = new Rect((int)((left + extent.x) / 1000), 
	                  (int)((top) / 1000), 
	                  (int)((right)/ 1000), 
	                 (int)((bottom) / 1000));
			lineX1 = (int) pos.x / 1000;
			lineX2 = (int) pos.x / 1000;
			lineY1 = (int) top / 1000;
			lineY2 = (int) bottom / 1000;
			
		} else {
			
			recBulb1 = new Rect((int)((left) / 1000), 
	                  (int)((top) / 1000), 
	                  (int)((right)/ 1000), 
	                 (int)((bottom - extent.y) / 1000));
			
			recBulb2 = new Rect((int)((left) / 1000), 
	                  (int)((top + extent.y) / 1000), 
	                  (int)((right)/ 1000), 
	                 (int)((bottom) / 1000));
			
			lineX1 = (int) left / 1000;
			lineX2 = (int) right / 1000;
			lineY1 = (int) pos.y / 1000;
			lineY2 = (int) pos.y / 1000;
			
		}

		Paint paintObject1 = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
			paintObject1.setColor(firstBulbColor);
		
		Paint paintObject2 = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
			paintObject2.setColor(secondBulbColor);

		Paint lineObject = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
			lineObject.setColor(Color.BLACK);
		
		c.drawRect(recBulb1, paintObject1);
		c.drawRect(recBulb2, paintObject2);
		c.drawLine(lineX1, lineY1, lineX2, lineY2, lineObject);

	}
}
