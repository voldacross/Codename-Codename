package voldaran.com.Upright;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Rect;

public class MenuPanel {
	
	
	public ArrayList<Button> menuButtons = new ArrayList<Button>();
	public static Rect testRect = new Rect();
	public static Button testButton = new Button(new Rect());
	public void addButton(Button b) {
		menuButtons.add(b);
	}
	
	public void addBitmap(Rect r) {
		Button d = new Button(r);
		d.clickable = false;
		menuButtons.add(d);
	}
	
	public MenuPanel () {

		
	}
	
	public void update(Vec2d click) {
		
		//Process Input
		MenuPanel.testButton = returnButton(click);
		if (MenuPanel.testButton!=null) MenuPanel.testButton.onClick(); 
			

	}
	
	public Button returnButton(Vec2d click) {
		for (Button b: menuButtons) {
			MenuPanel.testRect = b.buttonSize;
			if (MenuPanel.testRect.intersects((int) click.x,(int) click.y,(int) click.x,(int) click.y)) {
//				Log.d("GSTA", "You have clicked on " + b.name);
				return b;
			}
		}
		return null;	
	}
	
	public void draw (Canvas c) {
		for (Button b: menuButtons) {
			b.draw(c);
		}
		
		//Draw background
		//Draw Text
	}

}
