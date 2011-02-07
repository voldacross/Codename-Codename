package voldaran.com.Upright;

import android.graphics.Rect;

public class MenuButton {

	public String name;
	public int id;
	public Rect clickableArea;
	public Game game;
	
	public MenuButton(Rect r, String Name) {
		clickableArea = r;
		name = Name;

	}
	
	public MenuButton(Rect r, String Name, Game GAME) {
		clickableArea = r;
		name = Name;
		game = GAME;
	}
	
	public void onClick() {
		
		
	}
	
}