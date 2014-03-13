package jstudio.chizmover.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
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
	
	//table pack fields
	private static final String P_ID = "_id";
	private static final String P_DESCRIPTION = "description";
	private static final String P_CURRENTLEVEL = "current_level";
	private static final String P_LOCK = "lock";
	private static final String[] PACK_COLUMNS = 
		{ P_ID, P_DESCRIPTION, P_CURRENTLEVEL, P_LOCK };
	
	
	/*
	 * 	interface methods
	 */
	public LevelDetailEntity getLevelDetail(int packId, int levelnum) {
		LevelDetailEntity entity = null;
		
		try {
			this.openDB();
			
			Cursor cursor = mDB.query(TABLE_LEVELDETAIL, LD_COLUMNS, "packId = ? and levelnum = ?", 
					new String[] { String.valueOf(packId), String.valueOf(levelnum) }, 
					null, null, null);
			
			cursor.moveToFirst();
			
			if (cursor.getCount() > 0) {
				entity = new LevelDetailEntity(cursor.getInt(0), 
						cursor.getString(1), 
						cursor.getInt(2), 
						cursor.getInt(3), 
						cursor.getInt(4));
				
				Log.i(TAG, "level: " + entity.getContent());
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
	
	public PackEntity getEpisode(int packId) {
		PackEntity entity = null;
		
		try {
			this.openDB();
			
			Cursor cursor = mDB.query(TABLE_PACK, PACK_COLUMNS, "_id = ?", 
					new String[] { String.valueOf(packId) }, 
					null, null, null);
			
			cursor.moveToFirst();
			
			if (cursor.getCount() > 0) {
				entity = new PackEntity(cursor.getInt(0), 
						cursor.getString(1), 
						cursor.getInt(2),  
						cursor.getInt(3) == 1);
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
	
	public List<PackEntity> getAllEpisodes() {
		List<PackEntity> packs = new ArrayList<PackEntity>();
		
		try {
			this.openDB();
			
			String sql = "SELECT _id, description, current_level, lock, custom.total AS total FROM pack AS p join " +
			"(SELECT packId, COUNT(packId) AS total FROM level_detail GROUP BY packid) AS " +
			"custom ON p._id = custom.packId";
			
			Cursor cursor = mDB.rawQuery(sql, null);
			PackEntity pack = null;
			if (cursor.moveToFirst()) {
				do {
					pack = new PackEntity();
					pack.setId(cursor.getInt(0));
					pack.setDescription(cursor.getString(1));
					pack.setCurrentLevel(cursor.getInt(2));
					//0: unlock-false - 1: lock-true
					pack.setLock(cursor.getInt(3) != 0);
					pack.setNumberOfLevel(cursor.getInt(4));
					
					packs.add(pack);
				}
				while (cursor.moveToNext());
			}
			
			
		}
		catch (Exception ex) {
			Log.e(TAG, "error getAllEpisodes: " + ex.toString());
		}
		finally {
			this.closeDB();
		}
		
		return packs;
	}
	
	//update currentlevel + 1
	public void updateCompleteLevel(int packId, int curLevel) {
		//update completed current level;
		try {
			this.openDB();
			
			ContentValues values = new ContentValues();
			values.put(P_CURRENTLEVEL, curLevel + 1);
			
			mDB.update(TABLE_PACK, values, "_id = ?", new String[] { String.valueOf(packId)});
			
		}
		catch (Exception ex) {
			Log.e(TAG, "error unlockEpisode: " + ex.toString());
		}
		finally {
			this.closeDB();
		}
	}
	
	public void unlockEpisode(int packId) {
		try {
			this.openDB();
			
			ContentValues values = new ContentValues();
			values.put(P_LOCK, 0);
			
			mDB.update(TABLE_PACK, values, "_id = ?", new String[] { String.valueOf(packId)});
			
		}
		catch (Exception ex) {
			Log.e(TAG, "error unlockEpisode: " + ex.toString());
		}
		finally {
			this.closeDB();
		}
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
