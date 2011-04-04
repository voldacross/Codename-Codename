package voldaran.com.Upright;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Rect;
import android.util.Log;

public class MenuPanel {
	
	
	public ArrayList<Button> menuButtons = new ArrayList<Button>();
	public Rect testRect = new Rect();
	public Button testButton = new Button(new Rect());
	public boolean inTransition = false;
	public Button activeButton, deactiveButton;
	private Bitmap background;
	
	
	private double leftStep, rightStep, topStep, bottomStep;
	
	public void addButton(Button b) {
		menuButtons.add(b);
	}
	
	public void addBitmap(Rect r) {
		Button d = new Button(r);
		d.clickable = false;
		menuButtons.add(d);
	}
	
	public MenuPanel (Bitmap Background) {
		activeButton = null;
		deactiveButton = null;
		background = Background;
		
		double test;
		
		test = 10 / 3;
		
		Log.d("GSTA", "test " + test);
		
		
		
	}
	
	private Picture snapshot = new Picture();
	private Canvas tempC = null;
	private Rect snapshotRect = new Rect();
	
	public Picture setSnapshot() {

		tempC = snapshot.beginRecording((int) Game.cameraSize.x, (int) Game.cameraSize.y);
		draw(tempC);
		return snapshot;
	}
	
	public void startTransition(Button b) {
		setSnapshot();
		
		snapshotRect.left = 0;
		snapshotRect.top = 0;
		snapshotRect.right = (int) Game.cameraSize.x;
		snapshotRect.bottom = (int) Game.cameraSize.y;
		
//		if((int) >0) {}
		int step = 10;
		leftStep = Math.round(((double) ((int) Game.cameraSize.x / b.buttonSize.width())*(0-b.buttonSize.left) / step) - 0.5);
		rightStep =Math.round(((double) ((int) Game.cameraSize.x / b.buttonSize.width())*(Game.cameraSize.x-b.buttonSize.right) / step) + 0.5);
		topStep =Math.round(((double) ((int) Game.cameraSize.y / b.buttonSize.height()) * (0-b.buttonSize.top) / step) - 0.5);
		bottomStep =Math.round(((double) ((int) Game.cameraSize.y / b.buttonSize.height()) * (Game.cameraSize.y - b.buttonSize.bottom) / step) + 0.5);

		Log.d("GSTA", "Steps " + leftStep + " " + rightStep + " " + topStep + " " + bottomStep);
		
		inTransition = true;
	}
	
	public boolean expand(Button b) {
		snapshotRect.left = (int) (snapshotRect.left + leftStep);
		snapshotRect.right = (int) (snapshotRect.right + rightStep);
		snapshotRect.top = (int) (snapshotRect.top + topStep);
		snapshotRect.bottom = (int) (snapshotRect.bottom + bottomStep);
		
//		Log.d("GSTA", "snapshot left " + snapshotRect.left + "   " + ((int) Game.cameraSize.x / b.buttonSize.width())*(0-b.buttonSize.left));
//		Log.d("GSTA", "snapshot right " + snapshotRect.right + "   " + ((int) Game.cameraSize.x / b.buttonSize.width())*(Game.cameraSize.x-b.buttonSize.right));
//		Log.d("GSTA", "snapshot top " + snapshotRect.top + "   " + ((int) Game.cameraSize.y / b.buttonSize.height()) * (0-b.buttonSize.top));
//		Log.d("GSTA", "snapshot bottom " + snapshotRect.bottom + "   " + ((int) Game.cameraSize.y / b.buttonSize.height()) * (Game.cameraSize.y - b.buttonSize.bottom));
		
		if (snapshotRect.left<((int) Game.cameraSize.x / b.buttonSize.width())*(0-b.buttonSize.left)
				&& (snapshotRect.right - Game.cameraSize.x>((int) Game.cameraSize.x / b.buttonSize.width())*(Game.cameraSize.x-b.buttonSize.right))) {
			return true;
		} else
			return false;
	}
	
	public boolean contract(Button b) {
		snapshotRect.left = (int) (snapshotRect.left - leftStep);
		snapshotRect.right = (int) (snapshotRect.right - rightStep);
		snapshotRect.top = (int) (snapshotRect.top - topStep);
		snapshotRect.bottom = (int) (snapshotRect.bottom - bottomStep);
		
		if (snapshotRect.top >= 0 && snapshotRect.left>=0) {
			return true;
		} else 
			return false;
		
	}
	
	public void processInput(Vec2d click) {
		
		//Process Input
		testButton = returnButton(click);
		
		if (testButton!=null&&!inTransition) {
			if (testButton.Back) {
//				deactiveButton = testButton;
//				startTransition(deactiveButton);
				testButton.onClick();
			} else {
				activeButton = testButton;
				startTransition(activeButton);
			}
			
		}
		
		testButton = null; 
	}
	
	public void update() {
		
		if (inTransition) {
			if (deactiveButton!=null) {
				if (contract(activeButton)) {
					inTransition = false;
					deactiveButton = null;
				}
			} else if (activeButton!=null) {
				if (expand(activeButton)) {
					deactiveButton = activeButton;
					activeButton.onClick();
					activeButton = null;
				}
			} 
		}
		
	}
	
	public Button returnButton(Vec2d click) {
		for (Button b: menuButtons) {
			testRect.left = b.buttonSize.left;
			testRect.top = b.buttonSize.top;
			testRect.right = b.buttonSize.right;
			testRect.bottom = b.buttonSize.bottom;
			if (testRect.intersects((int) click.x,(int) click.y,(int) click.x,(int) click.y)) {
				return b;
			}
		}
		return null;	
	}
	
	public void draw (Canvas c) {
		if (inTransition) {
			c.drawPicture(snapshot, snapshotRect);
		} else {
			testRect.left = 0;
			testRect.top = 0;
			testRect.right = (int) Game.cameraSize.x;
			testRect.bottom = (int) Game.cameraSize.y;
			
			c.drawBitmap(background, null, testRect, null);
			
			for (Button b: menuButtons) {
				if (b!=activeButton||b!=deactiveButton) b.draw(c);
			}
			if (activeButton!=null) activeButton.draw(c);
			if (deactiveButton!=null) deactiveButton.draw(c);
		}
	}

}
