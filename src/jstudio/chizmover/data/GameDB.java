package jstudio.chizmover.data;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import jstudio.chizmover.managers.ResourceManager;

/*
 * 	singleton class
 */
public final class GameDB {
	
	private static final String TAG = "GameDB";
	
	private DBHelper dbHelper;
	private SQLiteDatabase mDB;
	
	//singleton implementation
	private static class GameDBLoader {
		private static final GameDB INSTANCE = new GameDB();
	}
	
	private GameDB() {
		if (GameDBLoader.INSTANCE != null) {
			throw new IllegalStateException("GameDB already instantiated");
		}
		
		try {
			dbHelper = new DBHelper(ResourceManager.getInstance().activity);
			dbHelper.createDB();
		}
		catch (Exception ex) {
			System.out.println("error creating database: " + ex.toString());
		}
	}
	
	public static GameDB getInstance() {
		return GameDBLoader.INSTANCE;
	}
	//end
	
	/*
	 * 	data access code goes here
	 */
	
	//tables' names
	private static final String TABLE_LEVELDETAIL = "level_detail";
	private static final String TABLE_PACK = "pack";
	
	//table level_detail fields
	private static final String LD_ID = "_id";
	private static final String LD_CONTENT = "content";
	private static final String LD_PACKID = "packId";
	private static final String LD_LEVELNUM = "levelnum";
	private static final String LD_DIFFICULTY = "difficulty";
	private static final String[] LD_COLUMNS = 
		{ LD_ID, LD_CONTENT, LD_PACKID, LD_LEVELNUM, LD_DIFFICULTY};
	
	
	public LevelDetailEntity getLevelDetail(int packId, int levelnum) {
		LevelDetailEntity entity = null;
		
		try {
			this.openDB();
			
			Cursor cursor = mDB.query(TABLE_LEVELDETAIL, LD_COLUMNS, "packId = ? and levelnum = ?", 
					new String[] { String.valueOf(packId), String.valueOf(levelnum) }, 
					null, null, null);
			
			cursor.moveToFirst();
			if (cursor != null) {
				entity = new LevelDetailEntity(cursor.getInt(0), 
						cursor.getString(1), 
						cursor.getInt(2), 
						cursor.getInt(3), 
						cursor.getInt(4));
				
				Log.v(TAG, "level: " + entity.getContent());
			}
		}
		catch (Exception ex) {
			Log.e(TAG, "error getLevelDetail: " + ex.toString());
		}
		finally {
			this.closeDB();
		}

		return entity;
	}
	
	/*
	 * 	helpers
	 */
	private void openDB() {
		try {
			dbHelper.openDB();
			dbHelper.close();
			
			mDB = dbHelper.getReadableDatabase();
		}
		catch (SQLException ex) {
			Log.e(TAG, "error openDB: " + ex.toString());
		}
	}
	
	private void closeDB() {
		try {
			dbHelper.close();
		}
		catch (SQLException ex) {
			Log.e(TAG, "error closeDB: " + ex.toString());
		}
	}
	
}
