package voldaran.com.Upright;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Button {
	public int id;
	public Rect buttonSize;
	public int left, right, top, bottom;
	public Bitmap bitmap;
	public boolean clickable = true;
	
	public static Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
	
	public Button(Rect r) {
		this(r, null);
	}
	
	public Button(Rect r,  Bitmap b) {
		buttonSize = r;
		left = buttonSize.left;
		right = buttonSize.right;
		top = buttonSize.top;
		bottom = buttonSize.bottom;
		bitmap = b;
	}

	public void reset() {
		buttonSize.left = left;
		buttonSize.right = right;
		buttonSize.top = top;
		buttonSize.bottom = bottom;
	}
	
	public void onClick() {
		//do nothing
	}
	
	public void draw(Canvas c) {
		    if (bitmap!=null){
		    	c.drawBitmap(bitmap, null, buttonSize, null);
		    } else {
		    	paint.setColor(Color.GREEN);
		    	c.drawRect(buttonSize, paint);
		    }
	}
	
}
