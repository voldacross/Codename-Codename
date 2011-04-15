package voldaran.com.Upright;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
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
	public int step = 30;
	private int currentStep = 1;
	
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
		
	}
	
	private Picture snapshot = new Picture();
	private Canvas tempC = null;
	private Rect snapshotRect = new Rect();
	
	public void reset() {
		snapshotRect.left = 0;
		snapshotRect.top = 0;
		snapshotRect.right = (int) Game.cameraSize.x;
		snapshotRect.bottom = (int) Game.cameraSize.y;
		currentStep = 1;
		inTransition = false;
		
	}
	
	public Picture setSnapshot() {
		boolean tempTransition = inTransition;
		inTransition = false;
		tempC = snapshot.beginRecording((int) Game.cameraSize.x, (int) Game.cameraSize.y);
		draw(tempC);
		inTransition = tempTransition;
		return snapshot;
	}
	
	public void fullscreenButton(Button b) {
		Log.d("GSTA", "fullscreenButton");
		currentStep = step;

		snapshotRect.left = (int) ((double) ((double) Game.cameraSize.x / (double) b.buttonSize.width())*(0-b.buttonSize.left));
		snapshotRect.top = (int) (((double) Game.cameraSize.y / (double) b.buttonSize.height())*(0-b.buttonSize.top));
		snapshotRect.right = (int) ((double) ((double) Game.cameraSize.x / (double) b.buttonSize.width())*(Game.cameraSize.x-b.buttonSize.right + b.buttonSize.width()));
		snapshotRect.bottom = (int) (((double) Game.cameraSize.y / (double) b.buttonSize.height())*(Game.cameraSize.y - b.buttonSize.bottom + b.buttonSize.height()));
		
		leftStep = ((double) (((double) Game.cameraSize.x / (double) b.buttonSize.width())*(0-b.buttonSize.left) - 0.5) / step);
		topStep = ((double) (((double) Game.cameraSize.y / (double) b.buttonSize.height())*(0-b.buttonSize.top) - 0.5) / step);
		rightStep = (((double) (((double) Game.cameraSize.x / (double) b.buttonSize.width())*(Game.cameraSize.x-b.buttonSize.right + b.buttonSize.width())) - 800 + 0.5) / step);
		bottomStep = (((((double) Game.cameraSize.y / (double) b.buttonSize.height())*(Game.cameraSize.y - b.buttonSize.bottom + b.buttonSize.height())) - 480 + 0.5) / step);
						
		Log.d("GSTA", "Steps " + leftStep + " " + topStep+ " " + rightStep  + " " + bottomStep);
		
	}
	
	public void startTransition(Button b) {
		Log.d("GSTA", "startTransition");
		setSnapshot();
		currentStep = 1;
		Log.d("GSTA", "snapshotRect " + snapshotRect.flattenToString());
		
		snapshotRect.left = 0;
		snapshotRect.top = 0;
		snapshotRect.right = (int) Game.cameraSize.x;
		snapshotRect.bottom = (int) Game.cameraSize.y;
		
//		if((int) >0) {}
		
		leftStep = ((double) (((double) Game.cameraSize.x / (double) b.buttonSize.width())*(0-b.buttonSize.left) - 0.5) / step);
		topStep = ((double) (((double) Game.cameraSize.y / (double) b.buttonSize.height())*(0-b.buttonSize.top) - 0.5) / step);
		rightStep = (((double) (((double) Game.cameraSize.x / (double) b.buttonSize.width())*(Game.cameraSize.x-b.buttonSize.right + b.buttonSize.width())) - 800 + 0.5) / step);
		bottomStep = (((((double) Game.cameraSize.y / (double) b.buttonSize.height())*(Game.cameraSize.y - b.buttonSize.bottom + b.buttonSize.height())) - 480 + 0.5) / step);

		Log.d("GSTA", "Steps " + leftStep + " " + topStep+ " " + rightStep  + " " + bottomStep);
		
		inTransition = true;
	}
	
	public boolean expand(Button b) {
		snapshotRect.left = (int) (0 + (leftStep*currentStep));
		snapshotRect.top = (int) (0 + (topStep*currentStep));
		snapshotRect.right = (int) (Game.cameraSize.x + (rightStep*currentStep));
		snapshotRect.bottom = (int) (Game.cameraSize.y + (bottomStep*currentStep));
		
		
		currentStep++;
		if (snapshotRect.left<(int) ((double) ((double) Game.cameraSize.x / (double) b.buttonSize.width())*(0-b.buttonSize.left))
				&& (snapshotRect.right>(int) ((double) ((double) Game.cameraSize.x / (double) b.buttonSize.width())*(Game.cameraSize.x-b.buttonSize.right + b.buttonSize.width())))
				&& (snapshotRect.top<(int) (((double) Game.cameraSize.y / (double) b.buttonSize.height())*(0-b.buttonSize.top)))
				&& (snapshotRect.bottom>(int) (((double) Game.cameraSize.y / (double) b.buttonSize.height())*(Game.cameraSize.y - b.buttonSize.bottom + b.buttonSize.height())))) {
			return true;
		} else
			return false;
	}
	
	public boolean contract(Button b) {
		snapshotRect.left = (int) (0 + (leftStep*currentStep));
		snapshotRect.top = (int) (0 + (topStep*currentStep));
		snapshotRect.right = (int) (Game.cameraSize.x + (rightStep*currentStep));
		snapshotRect.bottom = (int) (Game.cameraSize.y + (bottomStep*currentStep));
		
		currentStep--;
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
				Log.d("GSTA", "onClick!");
				testButton.onClick();
			} else {
				activeButton = testButton;
				deactiveButton = null;
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
	
	public static Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
	
	public void draw (Canvas c) {
		
		if (inTransition) {
			Log.d("GSTA", "draw snapshotRect " + snapshotRect.flattenToString());
			c.drawPicture(snapshot, snapshotRect);
		} else {
			testRect.left = 0;
			testRect.top = 0;
			testRect.right = (int) Game.cameraSize.x;
			testRect.bottom = (int) Game.cameraSize.y;
			
			c.drawBitmap(background, null, testRect, null);
			
//			MenuPanel.paint.setColor(Color.BLACK);
//			MenuPanel.paint.setAlpha((int) 185);
//			c.drawRect(0, 0, Game.cameraSize.x, Game.cameraSize.y, MenuPanel.paint);
			
			for (Button b: menuButtons) {
				if (b!=activeButton||b!=deactiveButton) b.draw(c);
			}
			if (activeButton!=null) activeButton.draw(c);
			if (deactiveButton!=null) deactiveButton.draw(c);
		}
	}

}


/*



		snapshotRect.left = (int) Math.round(((double) ((int) Game.cameraSize.x / b.buttonSize.width())*(0-b.buttonSize.left)));
		snapshotRect.top = (int) Math.round(((double) ((int) Game.cameraSize.y / b.buttonSize.height()) * (0-b.buttonSize.top)));
		snapshotRect.right = (int) Math.round(((double) ((int) Game.cameraSize.x / b.buttonSize.width())*(Game.cameraSize.x-b.buttonSize.right + b.buttonSize.width())));
		snapshotRect.bottom = (int) Math.round(((double) ((int) Game.cameraSize.y / b.buttonSize.height()) * (Game.cameraSize.y - b.buttonSize.bottom + b.buttonSize.height())));
		
		Log.d("GSTA", "snapshotRect1 " + snapshotRect.flattenToString());


		Log.d("GSTA", "contracting");
		snapshotRect.left = (int) (snapshotRect.left - leftStep);
		snapshotRect.right = (int) (snapshotRect.right - rightStep);
		snapshotRect.top = (int) (snapshotRect.top - topStep);
		snapshotRect.bottom = (int) (snapshotRect.bottom - bottomStep);



		Log.d("GSTA", "expanding");
		
		snapshotRect.left = (int) (snapshotRect.left + leftStep);
		snapshotRect.right = (int) (snapshotRect.right + rightStep);
		snapshotRect.top = (int) (snapshotRect.top + topStep);
		snapshotRect.bottom = (int) (snapshotRect.bottom + bottomStep);
		
		
//		Log.d("GSTA", "snapshot left " + snapshotRect.left + "   " + ((int) Game.cameraSize.x / b.buttonSize.width())*(0-b.buttonSize.left));
//		Log.d("GSTA", "snapshot right " + snapshotRect.right + "   " + (int) ((double) ((double) Game.cameraSize.x / (double) b.buttonSize.width())*(Game.cameraSize.x-b.buttonSize.right + b.buttonSize.width())));
//		Log.d("GSTA", "snapshot top " + snapshotRect.top + "   " + ((int) Game.cameraSize.y / b.buttonSize.height()) * (0-b.buttonSize.top));
//		Log.d("GSTA", "snapshot bottom " + snapshotRect.bottom + "   " + ((int) Game.cameraSize.y / b.buttonSize.height()) * (Game.cameraSize.y - b.buttonSize.bottom));


//		leftStep = Math.round(((double) ((int) Game.cameraSize.x / b.buttonSize.width())*(0-b.buttonSize.left) / step) - 0.5);
//		rightStep =Math.round(((double) ((int) Game.cameraSize.x / b.buttonSize.width())*(Game.cameraSize.x-b.buttonSize.right) / step) + 0.5);
//		topStep =Math.round(((double) ((int) Game.cameraSize.y / b.buttonSize.height()) * (0-b.buttonSize.top) / step) - 0.5);
//		bottomStep =Math.round(((double) ((int) Game.cameraSize.y / b.buttonSize.height()) * (Game.cameraSize.y - b.buttonSize.bottom) / step) + 0.5);


//		Log.d("GSTA", "" + (double) ((double) Game.cameraSize.x / (double) b.buttonSize.width()));
		Log.d("GSTA", "snapshotRect2 " + snapshotRect.flattenToString());
		
		
//		leftStep = Math.round(((double) ((int) Game.cameraSize.x / b.buttonSize.width())*(0-b.buttonSize.left) / step) - 0.5);
//		rightStep =Math.round(((double) ((int) Game.cameraSize.x / b.buttonSize.width())*(Game.cameraSize.x-b.buttonSize.right) / step) + 0.5);
//		topStep =Math.round(((double) ((int) Game.cameraSize.y / b.buttonSize.height()) * (0-b.buttonSize.top) / step) - 0.5);
//		bottomStep =Math.round(((double) ((int) Game.cameraSize.y / b.buttonSize.height()) * (Game.cameraSize.y - b.buttonSize.bottom) / step) + 0.5);

		leftStep = Math.round(((double) ((int) Game.cameraSize.x / b.buttonSize.width())*(0-b.buttonSize.left) / step) - 0.5);
		rightStep =Math.round(((double) ((int) Game.cameraSize.x / b.buttonSize.width())*(Game.cameraSize.x-b.buttonSize.right) / step) + 0.5);
		topStep =Math.round(((double) ((int) Game.cameraSize.y / b.buttonSize.height()) * (0-b.buttonSize.top) / step) - 0.5);
		bottomStep =Math.round(((double) ((int) Game.cameraSize.y / b.buttonSize.height()) * (Game.cameraSize.y - b.buttonSize.bottom) / step) + 0.5);

		leftStep = Math.round(((((double) ((double) Game.cameraSize.x / (double) b.buttonSize.width())*(0-b.buttonSize.left)) - b.buttonSize.left) / step) - 0.5);
		topStep =Math.round((((((double) Game.cameraSize.y / (double) b.buttonSize.height())*(0-b.buttonSize.top)) - b.buttonSize.top) / step) - 0.5);
		rightStep =Math.round(((((double) ((double) Game.cameraSize.x / (double) b.buttonSize.width())*(Game.cameraSize.x-b.buttonSize.right + b.buttonSize.width())) - Game.cameraSize.x + b.buttonSize.right) / step) + 0.5);
		bottomStep =Math.round((((((double) Game.cameraSize.y / (double) b.buttonSize.height())*(Game.cameraSize.y - b.buttonSize.bottom + b.buttonSize.height())) - Game.cameraSize.y + b.buttonSize.bottom) / step) + 0.5);


*/