package jstudio.chizmover.managers;

import java.util.Arrays;
import java.util.List;

import jstudio.chizmover.data.DBHelper;
import jstudio.chizmover.data.GameDB;
import jstudio.chizmover.data.LevelDetailEntity;

public class GameManager {
	
	private static final String TAG = "GameManager";
	
	public static final String LEVEL_LINE_SEPARATOR = "0";
	public static final char LEVEL_WALL_CHAR = '#';
	public static final char LEVEL_BOT_CHAR = '@';
	public static final char LEVEL_TARGET = '.';
	public static final char LEVEL_BOX_CHAR = '$';
	public static final char LEVEL_SPACE = ' ';
	public static final char LEVEL_BOX_ON_TARGET = '*';
	
	public static final char BOT_MOVE_LEFT = 'L';
	public static final char BOT_MOVE_RIGHT = 'R';
	public static final char BOT_MOVE_UP = 'U';
	public static final char BOT_MOVE_DOWN = 'D';
	
	public static final String PATH_OUTBOUND = "OUTBOUND";
	public static final float MOVE_UNIT_DURATION = 0.15F;
	
	
	private int[][] mIntegerMaze;
	private PathFinder mPathFinder;
	
	//singleton implementation
	private static class GameManagerLoader {
		private static final GameManager INSTANCE = new GameManager();
	}
	
	private GameManager() {
		if (GameManagerLoader.INSTANCE != null) {
			throw new IllegalStateException("GameManager already instantiated");
		}
		
		mPathFinder = new PathFinder();
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
	
	public void initMaze(List<String> mazeChars) {
		if (mazeChars == null || mazeChars.size() == 0)
			return;
		
		int row = mazeChars.size();
		int col = mazeChars.get(0).length();
		
		mIntegerMaze = new int[row][col];
		
		for (int i = 0; i < row; i++) {
			String line = mazeChars.get(i);
			for (int j = 0; j < line.length(); j++) {
				char tmpChar = line.charAt(j);
				int tmpNum = -1;
				
				if (tmpChar == LEVEL_WALL_CHAR || tmpChar == LEVEL_BOX_CHAR || tmpChar == LEVEL_BOX_ON_TARGET) {
					tmpNum = -1;
				}
				else {
					tmpNum = 0;
				}
				
				mIntegerMaze[i][j] = tmpNum;
			}
		}
	}

	public String getShortestPath(int[] touchPos, int[] botPos) {
		String path = mPathFinder.getShortestPathString(mIntegerMaze, touchPos, botPos);
		return path;
	}
	
	public boolean checkGameWin(List<String> mazeChars) {
		for (String line : mazeChars) {
			//there is still box out there
			if (line.indexOf(LEVEL_BOX_CHAR) > -1)
				return false;
		}
		
		return true;
	}
}
