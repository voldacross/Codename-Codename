package voldaran.com.Upright;

import java.io.FileOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class test extends Activity{
 
	        WebView w = null; 
	    /** Called when the activity is first created. */ 
	    @Override 
	    public void onCreate(Bundle savedInstanceState) 
	    { 
	        super.onCreate(savedInstanceState); 
	        w = new WebView(this); 
	        w.setWebViewClient(new WebViewClient() 
	        { 
	                public void onPageFinished(WebView view, String url) 
	                { 
	                        Picture picture = view.capturePicture(); 
	                Bitmap  b = Bitmap.createBitmap( picture.getWidth(), 
	picture.getHeight(), Bitmap.Config.ARGB_8888); 
	                Canvas c = new Canvas( b ); 
	                picture.draw( c ); 
	                FileOutputStream fos = null; 
	                try { 
	                        fos = new FileOutputStream( "/sdcard/yahoo_" + 
	System.currentTimeMillis() + ".jpg" ); 
	                        if ( fos != null ) 
	                        { 
	                                b.compress(Bitmap.CompressFormat.JPEG, 90, fos ); 
	                                fos.close(); 
	                        } 
	                } catch( Exception e ) 
	                        { 
	                        //... 
	                        } 
	                } 
	          }); 
	        setContentView( w ); 
	        w.loadUrl( "http://www.yahoo.com"); 
	    } 
	} 