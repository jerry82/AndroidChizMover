package jstudio.chizmover.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	
	private static String DB_NAME = "sodoku_android.sqlite";
	private static String DB_FULLNAME = "";
	private final Context mContext;
	private SQLiteDatabase mDB;
	private static String TAG = "DBHelper";
	
	public DBHelper(Context pContext) {
		super(pContext, DB_NAME, null, 1);	    
	    DB_FULLNAME = pContext.getDatabasePath(DB_NAME).getAbsolutePath();
		this.mContext = pContext;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public synchronized void close() {
		if (mDB != null)
			mDB.close();
		super.close();
	}
	
	public void openDB() throws SQLException {
		mDB = SQLiteDatabase.openDatabase(DB_FULLNAME, null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	public void createDB() throws IOException {
		boolean dbExist = checkDB();
		
		//override database anyway
		this.getReadableDatabase();
		this.close();
		
		//copyDB();
		
		if (!dbExist) {
			System.out.println("db does not exist");
			//this method create empty database 
			//so we can copy the file over
			this.getReadableDatabase();
			copyDB();
		}
		else {
			System.out.println("db exists");
		}
	}
	
	/*
	 * helpers
	 */
	private boolean checkDB() {
		File dbFile = new File(DB_FULLNAME);
		return dbFile.exists();
	}
	
	private void copyDB() throws IOException {
		InputStream input = mContext.getAssets().open(DB_NAME);
		OutputStream output = new FileOutputStream(DB_FULLNAME);
		
		byte[] buffer = new byte[1024];
		int length;
		while ((length = input.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}
		//close stream
		output.flush();
		output.close();
		input.close();

		Log.v(TAG, "done copying the database file");
	}
}
