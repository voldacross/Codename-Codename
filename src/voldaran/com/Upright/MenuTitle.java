package voldaran.com.Upright;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class MenuTitle extends MenuSystem{

	public int activeLeft;
	public int mainLeft;
	public int currentLeft;
	public int drawOrder;
	public int goneLeft;
	
	public static boolean inTransition;
	
//	public Rect menuRec;
	public Paint menuPaint;
	public int col;
	public static MenuTitle activePanel;
	public static ArrayList<MenuTitle> menuTitlePanels = new ArrayList<MenuTitle>();
	Bitmap bitmap;
	
	public static void drawMenus(Canvas c) {
		
		for(MenuTitle m : MenuTitle.menuTitlePanels){
			m.draw(c);
		}
		
	}
	public MenuTitle(Rect rMenu, int activeL, int mainL,int goneL, Bitmap bitM, int order) {
		super(rMenu);
		
		activeLeft = activeL;
		mainLeft = mainL;
		drawOrder = order;
		goneLeft = goneL;
		MenuTitle.menuTitlePanels.add(this);
		
	}


	@Override public void draw(Canvas c) {
		Log.d("GSTA", "Drawwing menu             STARTING " + drawOrder );
		currentLeft = super.r.left;
		
		if (MenuTitle.activePanel==this) {
			Log.d("GSTA", "Drawwing menu             this   ,   " + drawOrder );
			if (currentLeft!=activeLeft) {
				
				MenuTitle.inTransition = true;
				//not in the correct position
				//Transition to correct Active position
				if (currentLeft > activeLeft) {
					currentLeft -= 30;
					if (currentLeft < activeLeft) currentLeft = activeLeft;
				} else if (currentLeft < activeLeft) {
					currentLeft += 30;
					if (currentLeft > activeLeft) currentLeft = activeLeft;
				}
			} else MenuTitle.inTransition = false;
		} else if (MenuTitle.activePanel==null) {
			//Main Menu, return to main position
			Log.d("GSTA", "Drawwing menu             null   ,   " + drawOrder );
			if (currentLeft!=mainLeft) {
				MenuTitle.inTransition = true;
				if (currentLeft > mainLeft) {
					currentLeft -= 30;
					if (currentLeft < mainLeft) currentLeft = mainLeft;
				} else if (currentLeft < mainLeft) {
					currentLeft += 30;
					if (currentLeft > mainLeft) currentLeft = mainLeft;
				}
			} else MenuTitle.inTransition = false;
		} else {

			//Not main menu, Not Self, return to offScreen Position
			Log.d("GSTA", "Drawwing menu             else   ,   " + drawOrder);
			if (currentLeft!=goneLeft) {
				MenuTitle.inTransition = true;
				//Transition back 
				if (currentLeft > goneLeft) {
					currentLeft -= 30;
					if (currentLeft < goneLeft) currentLeft = goneLeft;
				} else if (currentLeft < goneLeft) {
					currentLeft += 30;
					if (currentLeft > goneLeft) currentLeft = goneLeft;
				}
			} else MenuTitle.inTransition = false;
		}
		super.r.offsetTo(currentLeft, 0);

		
//		if (!MenuTitle.inTransition) {
		
			Log.d("GSTA", "" + drawOrder + "   " + super.r.left + "," + goneLeft + "," + MenuTitle.inTransition);
//		}
			
//		Paint drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
//		drawPaint.setColor(col);
		Log.d("GSTA", "Drawing bitmap");
		c.drawBitmap(bitmap, null, super.r, null);
		Log.d("GSTA", "end");
//		c.drawRect(super.r, drawPaint);
//		Log.d("GSTA", "Drawwing menu             END" );
	}

}
