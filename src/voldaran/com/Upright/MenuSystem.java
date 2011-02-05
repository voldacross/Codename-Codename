package voldaran.com.Upright;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Rect;

public class MenuSystem {
	
	public static MenuSystem activeMenu;
	public static ArrayList<MenuButton> menuButtons = new ArrayList<MenuButton>();
	public static ArrayList<MenuSystem> menus = new ArrayList<MenuSystem>();
	
	
	public Rect r;
	
	int left, right, top, bottom;

	public void addButton(Rect b, String name) {
		MenuButton button = new MenuButton(b, name);
		MenuSystem.menuButtons.add(button);
	}
	
	public MenuSystem() {
		
	}
	
	public MenuSystem(Rect menu) {
		MenuSystem.menus.add(this);
		r = menu;
	}
	
	
	public static void updateActive() {
		
	}	
	
	public void Transition() {
		MenuSystem.activeMenu = this;
	}
	
	
	public void draw(Canvas c) {
		
		
	}
	
}


