package voldaran.com.Upright;
import java.util.ArrayList;

import android.util.Log;



public class Overlay {
	
	ArrayList[][] overlay = new ArrayList[100][100];
	
	public ArrayList<GameObjects> getGameObjects(int row, int column) {
		if (!(overlay[row][column]==null)) {
			return overlay[row][column];
		}
		return null;
	}
	public ArrayList getTile(int row, int column) {
		return overlay[row][column];
	}
	
	public void updateGameObject (GameObjects gameO) {
		//Used if a wall or object moves
		int rowTL, columnTL, rowBR, columnBR;
		int row, column;	
		//Remove object from overlay
		
		for (int Row = 0; Row<=99;Row++) {
			for (int Col = 0; Col<=99;Col++) {
				if (!(overlay[Row][Col]==null)) {
					if (overlay[Row][Col].contains(gameO)) overlay[Row][Col].remove(gameO);
				}
			}
		}
		
		//Add GameObject

		rowTL = (int) Math.round(((int) gameO.y / 100) - 0.5);
		columnTL = (int) Math.round(((int) gameO.x / 100) - 0.5);

		//Bottom right point
		rowBR = (int) Math.round(((int) (gameO.y + gameO.height) / 100) - 0.5);
		columnBR = (int) Math.round(((int) (gameO.x + gameO.width) / 100) - 0.5);
		
		for (row = rowTL; row <= rowBR; row++) {
			for (column = columnTL; column <= columnBR; column++) {
				if (overlay[row][column]==null) {
					overlay[row][column] = new ArrayList<GameObjects>();
				}
				
				if (!overlay[row][column].contains(gameO)) {
					overlay[row][column].add(gameO);
					//Log.d("GSTA", "Adding wall " + " : " + wall.X + "," + wall.Y +  " to " + row + "," + column);
				} else {
					//Log.d("GSTA", "wall already added" + " : " + wall.X + "," + wall.Y +  " to " + row + "," + column);
				}
			}
		}

		
		
		
	}
	
	
	public void addGameObject (GameObjects gameO) {
		int rowTL, columnTL, rowBR, columnBR;
		int row, column;
		//Top left point
		rowTL = (int) Math.round(((int) gameO.y / 100) - 0.5);
		columnTL = (int) Math.round(((int) gameO.x / 100) - 0.5);

		//Bottom right point
		rowBR = (int) Math.round(((int) (gameO.y + gameO.height) / 100) - 0.5);
		columnBR = (int) Math.round(((int) (gameO.x + gameO.width) / 100) - 0.5);
		
		for (row = rowTL; row <= rowBR; row++) {
			for (column = columnTL; column <= columnBR; column++) {
				if (overlay[row][column]==null) {
					overlay[row][column] = new ArrayList<GameObjects>();
				}
				
				if (!overlay[row][column].contains(gameO)) {
					overlay[row][column].add(gameO);
					//Log.d("GSTA", "Adding wall " + " : " + wall.X + "," + wall.Y +  " to " + row + "," + column);
				} else {
					//Log.d("GSTA", "wall already added" + " : " + wall.X + "," + wall.Y +  " to " + row + "," + column);
				}
			}
		}
	}
	
	
//	public void addWall (Walls wall) {
//		int rowTL, columnTL, rowBR, columnBR;
//		int row, column;
//		//Top left point
//		rowTL = (int) Math.round(((int) wall.Y / 100) - 0.5);
//		columnTL = (int) Math.round(((int) wall.X / 100) - 0.5);
//
//		//Bottom right point
//		rowBR = (int) Math.round(((int) (wall.Y + wall.height) / 100) - 0.5);
//		columnBR = (int) Math.round(((int) (wall.X + wall.width) / 100) - 0.5);
//		
//		for (row = rowTL; row <= rowBR; row++) {
//			for (column = columnTL; column <= columnBR; column++) {
//				if (overlay[row][column]==null) {
//					overlay[row][column] = new ArrayList<Walls>();
//				}
//				
//				if (!overlay[row][column].contains(wall)) {
//					overlay[row][column].add(wall);
//					//Log.d("GSTA", "Adding wall " + " : " + wall.X + "," + wall.Y +  " to " + row + "," + column);
//				} else {
//					//Log.d("GSTA", "wall already added" + " : " + wall.X + "," + wall.Y +  " to " + row + "," + column);
//				}
//			}
//		}
//	}
	


}
