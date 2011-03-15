package voldaran.com.Upright;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class GameRLauncher extends GameObject{
	
	public GameRLauncher(Vec2d pos, Vec2d extent) {
		super(pos, extent);
		color = Color.BLUE;
		Log.d("GSTA", "creating a GameRLauncher");
	}
	
	
//	@Override
//	public void drawP(Canvas c){
//		Rect recObject = new Rect((int) left / 1000, (int) top  / 1000, (int) right  / 1000, (int)bottom  / 1000);
//		Paint paintObject = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
//		paintObject.setColor(color);
//		c.drawRect(recObject, paintObject);
//	}
//	
////	@Override
////	public void draw(Canvas c){
////		Log.d("GSTA", "DRAWING THE BLUE BOX");
////		Rect recObject = new Rect((int)((left - GameObject.offset.x) / 1000), 
////				                  (int)((top - GameObject.offset.y) / 1000), 
////				                  (int)((right - GameObject.offset.x)/ 1000), 
////		 		                 (int)((bottom - GameObject.offset.y) / 1000));
////
////		Paint paintObject = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
////		paintObject.setColor(color);
////		c.drawRect(recObject, paintObject);
////	}
//	
//	@Override
//	public void drawPreview(Canvas c){
//		Log.d("GSTA", "BUT IM DRAWING A PREVIEW");
//		Rect recObject = new Rect((int)((left - GameObject.offset.x) / 1000 / 4), 
//				                  (int)((top - GameObject.offset.y) / 1000 / 4), 
//				                  (int)((right - GameObject.offset.x)/ 1000 / 4), 
//		 		                 (int)((bottom - GameObject.offset.y) / 1000 / 4));
//
//		Paint paintObject = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
//		paintObject.setColor(color);
//		c.drawRect(recObject, paintObject);
//	}
//	


}
