package voldaran.com.Upright;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;


public class upright extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.d("GSTA","test");
        //Intent StartGameIntent = new Intent(upright.this,Game.class);
        //startActivity(StartGameIntent);
        
        setContentView(new Game(this));
    }
    
    
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
    	Log.d("GSTA" ,"onSaveInstanceState is called");
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    	
    }


}