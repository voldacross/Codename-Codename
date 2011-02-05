package voldaran.com.Upright;


import voldaran.com.Upright.Game.GameThread;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;


public class upright extends Activity{
	private Game game;
	private GameThread thread;
	
	public static final String PREFS_NAME = "savedGameState";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        game = new Game(this);
//        setContentView(game);
    	game = new Game(this);
    	setContentView(game);

    }
    
    
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
    	Log.d("GSTA" ,"onSaveInstanceState is called");
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    	
    }

    @Override
    protected void onResume(){
    	super.onResume();
    	Log.d("GSTA","OnResume Activity Called");

    	game.createThread();
    	thread = game.getThread();
    	thread.resumeGameState(resumeGameState());
    	
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    	Log.d("GSTA","OnPause Activity Called");
    	
    	//Load SavedGameState into SharedPrederences
    	saveBundleToShared(thread.saveGameState());
    	
    	//EndThread
    	game.stopThread();
    }
    
    
    
    //Grab the Menu Button Key, and the Back Button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back.
        	Log.d("GSTA", "You pressed back");
            return true;
            
        } else if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0) {
        	Log.d("GSTA", "You pressed menu");
        	return true;
        	
        }
        
        return super.onKeyDown(keyCode, event);
    }
    
    private Bundle resumeGameState() {
    	Log.d("GSTA", "Setting up Settings");
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    		
    	Bundle ResumeState = new Bundle();
    	ResumeState.putLong("HERO_X", 600000);//settings.getLong("HERO_X", 600000));
     	ResumeState.putLong("HERO_Y", 300000);//settings.getLong("HERO_Y", 300000));
     	ResumeState.putBoolean("HERO_GRAVITY", settings.getBoolean("HERO_GRAVITY", true));
    	
    	
    	return ResumeState;
    }
    
    private void saveBundleToShared(Bundle SaveState) {
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        
    	editor.putLong("HERO_X", SaveState.getLong("HERO_X"));
    	editor.putLong("HERO_Y", SaveState.getLong("HERO_Y"));
    	editor.putBoolean("HERO_GRAVITY", SaveState.getBoolean("HERO_GRAVITY"));
    	
        //commit
        editor.commit();
    }

}