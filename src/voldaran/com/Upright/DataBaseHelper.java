package voldaran.com.Upright;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper{
	 
    private static String DB_PATH = "/data/data/voldaran.com.Upright/databases/";
    private static String DB_NAME = "masterDB.db";
 
    private SQLiteDatabase mDB; 
 
    private final Context mContext;
 
    public DataBaseHelper(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.mContext = context;
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method an empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = mContext.getAssets().open("db/masterDB.db");
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
    
    
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
        mDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(mDB != null)
    	    	mDB.close();
 
    	    super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
	
	public Cursor returnAllRecords() {
		//Returns all records
		Cursor c = mDB.rawQuery("SELECT * FROM " + "levelinfo" , null);
		
		return c;
	}
	
	public Cursor readRecord(int id) {
		//Returns selected record
		Cursor c = mDB.rawQuery("SELECT * FROM " + "levelinfo" + " where id = " + id, null);
		return c;
		
		
//		Log.d("GSTA", "ReadRecord      " + c.getCount() + " " + c.getColumnCount() );
//		if (c != null) {
//			if (c.moveToFirst()) {
//				Log.d("GSTA", "Read Record " + c.getInt(c.getColumnIndex("highscore")));
//			}
//		}
		
	}
 
	public void updateRecord(LevelInfo level) {
		//updates LevelInfo record in DB
		
		ContentValues cv = new ContentValues();
		cv.put("highscore", level.highscore);
		cv.put("state", level.state);

		mDB.update("levelinfo", cv, "id = " + level.id, null);
		
	}
 
}