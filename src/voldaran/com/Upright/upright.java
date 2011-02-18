package voldaran.com.Upright;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;


public class upright extends Activity{
	private Game game;
	public String test;
	
	public static final String PREFS_NAME = "savedGameState";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        game = new Game(this);
        
        setContentView(game);
    	game.setDrawingCacheEnabled(true); 
    	

    }
    

    @Override
    protected void onResume(){
    	super.onResume();
    	
    	//start thread
    	game.createThread();
    	
    	
    	
    	//resume previous gamestate
    	game.resumeGameBundle(resumeGameState());
    	
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    	Log.d("GSTA", "onPause called");
    	//Load SavedBundle to SharedPreferences
    	saveBundletoShared(game.saveGameBundle());
    	
    	//Kill thread;
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
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	
    	Bundle resumeState = new Bundle();
    	
    	
    	resumeState.putInt("GAME_STATE", settings.getInt("GAME_STATE", 0));
    	
    	return resumeState;
    }
    
    public void saveBundletoShared(Bundle SaveState) {
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	SharedPreferences.Editor editor = settings.edit();
    	
    	editor.putInt("GAME_STATE", SaveState.getInt("GAME_STATE"));
    	
//    	commit
    	editor.commit();
    }
}