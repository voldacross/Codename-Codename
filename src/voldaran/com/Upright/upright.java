package voldaran.com.Upright;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;


public class upright extends Activity{
	private Game game;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        game = new Game(this);
        setContentView(game);
    }
    

    @Override
    protected void onResume(){
    	super.onResume();
    	game.createThread();
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    	game.stopThread();
    }

}