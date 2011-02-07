package voldaran.com.Upright;

import java.util.ArrayList;

import android.graphics.Rect;

public class MenuSystem {
	
	public static MenuSystem activeMenu;
	public ArrayList<MenuButton> menuButtons = new ArrayList<MenuButton>();
	public static ArrayList<MenuSystem> menus = new ArrayList<MenuSystem>();
	
	
	public Rect r;
	
	int left, right, top, bottom;

	public void addButton(Rect b, String name) {
		MenuButton button = new MenuButton(b, name);
		menuButtons.add(button);
	}
	
	public void addButton(MenuButton button) {
		menuButtons.add(button);
	}
	
	public MenuSystem() {
		
	}
	
	public MenuSystem(Rect menu) {
		MenuSystem.menus.add(this);
		r = menu;
	}
	
	public MenuButton returnButton(Vec2d click) {
		for(MenuButton b : menuButtons){
			
			Rect test = b.clickableArea;
			
			if (test.intersects((int) click.x,(int) click.y,(int) click.x,(int) click.y)) {
//				Log.d("GSTA", "You have clicked on " + b.name);
				return b;
			}
			
		}
		return null;
	}
	
}

