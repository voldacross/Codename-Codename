package voldaran.com.Upright;

import java.util.ArrayList;

import android.util.Log;

public class LevelInfo {
	
	public int level, optimal, first, second, highscore, state, world, id;
	
	public static ArrayList<LevelInfo> levels = new ArrayList<LevelInfo>();
	
	public static LevelInfo returnLevelInfo(int ii) {
		for (LevelInfo i: LevelInfo.levels) {
			if (i.id == ii) {
				return i;
			}
		}
		return null;
	}
	public static int levelToIntFromInt(int w, int l) {
		
		String temp = String.valueOf(l);
		
		StringBuilder sb = new StringBuilder();
		sb.append(w);
		
		if (temp.length()==1) {
			sb.append("00");
		}
		if (temp.length()==2) {
			sb.append("0");
		}
		sb.append(l);
		
		return Integer.parseInt(sb.toString());
	}
	
	
	public static String levelToString(int l) {
		//1004
		//1104
		
		String sL = String.valueOf(l); 
		
		String world = sL.substring(0, sL.length() - 3);
		String level = String.valueOf(Integer.parseInt(sL.substring(sL.length() - 3)));
		
//		Log.d("GSTA", "world " + world + " level " + level);
//		"world1/level104.txt"
		String r = "world" + world  +  "/level" + level + ".txt";
		
		return r;
	}
	
	public LevelInfo(int w, int l, int i) {
		level = l;
		world = w;
		id = i;
		LevelInfo.levels.add(this);
	}
	
	public LevelInfo(LevelInfo l) {
		level = l.level;
		world = l.world;
		id = l.id;
		first = l.first;
		second = l.second;
		highscore = l.highscore;
		optimal = l.optimal;
		state = l.state;
	}
	
	
	@Override
	public String toString() {
		String r = "id = " + id + 
		",world = " + world  +
		",level = " + level +
		",optimal = " + optimal +
		",first = " + first +
		",second = " + second +
		",highscore = " + highscore + 
		",state = " + state;
		return r;
	}
	
	public LevelInfo set() {
		return new LevelInfo(this);
	}
	
	public boolean UpdateWin(int score) {
		
		if (score>highscore) highscore = score;
		
		//Unlock other levels?
		
		return (score>highscore);
		
	}

}




//String temp = String.valueOf(l);
//
//StringBuilder sb = new StringBuilder();
//sb.append(w);
//
//if (temp.length()==1) {
//	sb.append("00");
//}
//if (temp.length()==2) {
//	sb.append("0");
//}
//sb.append(l);
//
//id = Integer.parseInt(sb.toString());
//
//Log.d("GSTA", "ID = " + id);