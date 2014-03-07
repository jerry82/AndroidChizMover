package jstudio.chizmover.managers;

import java.util.Arrays;
import java.util.List;

import jstudio.chizmover.data.DBHelper;
import jstudio.chizmover.data.GameDB;
import jstudio.chizmover.data.LevelDetailEntity;

public class GameManager {
	
	private static final String TAG = "GameManager";
	
	private static final String LEVEL_LINE_SEPARATOR = "0";
	
	//singleton implementation
	private static class GameManagerLoader {
		private static final GameManager INSTANCE = new GameManager();
	}
	
	private GameManager() {
		if (GameManagerLoader.INSTANCE != null) {
			throw new IllegalStateException("GameManager already instantiated");
		}
	}
	
	public static GameManager getInstance() {
		return GameManagerLoader.INSTANCE;
	}
	//end
	
	
	public LevelDetailEntity getNextLevel(LevelDetailEntity currentLevel) {
		
		LevelDetailEntity entity = null;
		int packId = -1;
		int levelnum = -1;
		
		if (currentLevel == null) {
			packId = 1;
			levelnum = 1;
		}
		else {
			//TODO change to check last levelnum
			packId = currentLevel.getPackId();
			levelnum = currentLevel.getLevelNum() + 1;
		}
		
		entity = GameDB.getInstance().getLevelDetail(packId, levelnum);
		
		return entity;
	}
	
	public List<String> getMazeChars(String content) {
		
		if (content != null && content.length() > 0) {
			List<String> mazeChars = Arrays.asList(content.split(LEVEL_LINE_SEPARATOR));
			return mazeChars;
		}
		
		return null;
	}
}
