package jstudio.chizmover.managers;

import java.util.Arrays;
import java.util.List;

import android.util.Log;

import jstudio.chizmover.data.GameDB;
import jstudio.chizmover.data.LevelDetailEntity;
import jstudio.chizmover.data.PackEntity;

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
	
	public int SpriteCurrentEdge;
	public float SpriteScaleFactor;
	
	public boolean mSoundEnable = true;
	
	private List<String> mMazeChars;
	private LevelDetailEntity mCurrentLevel = null;
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
		
		SpriteCurrentEdge = ResourceManager.FixSizeSpriteEdge;
	}
	
	public static GameManager getInstance() {
		return GameManagerLoader.INSTANCE;
	}
	//end
	
	/*	
	 * 	handle game data
	 */
	public LevelDetailEntity getPrevLevel() {
		LevelDetailEntity entity = null;
		
		if (mCurrentLevel == null) 
			return entity;
		
		entity = GameDB.getInstance().getLevelDetail(mCurrentLevel.getPackId(), mCurrentLevel.getLevelNum() - 1);
		
		return entity;
	}
	
	public LevelDetailEntity getNextLevel() {
		
		LevelDetailEntity entity = null;
		int packId = -1;
		int levelnum = -1;
		
		if (mCurrentLevel == null) {
			packId = 1;
			levelnum = 1;
		}
		else {
			packId = mCurrentLevel.getPackId();
			levelnum = mCurrentLevel.getLevelNum() + 1;
		}
		
		entity = GameDB.getInstance().getLevelDetail(packId, levelnum);
		
		return entity;
	}
	
	public LevelDetailEntity getLevelDetail(int packId, int levelnum) {
		return GameDB.getInstance().getLevelDetail(packId, levelnum);
	}
	
	public void getMazeChars(String content) {
		if (content != null && content.length() > 0) {
			this.mMazeChars = Arrays.asList(content.split(LEVEL_LINE_SEPARATOR));
		}
	}
	
	public List<PackEntity> getAllEpisodes() {
		return GameDB.getInstance().getAllEpisodes();
	}
	
	public PackEntity getEpisode(int packId) {
		return GameDB.getInstance().getEpisode(packId);
	}
		
	/*
	 * 	this will handle update pack table, 
	 *  unlock new episode, show endgame screen...
	 */
	public void handleGameWin() {
		int packId = mCurrentLevel.getPackId();
		int levelNum = mCurrentLevel.getLevelNum();
		
		List<PackEntity> allEpisode = GameDB.getInstance().getAllEpisodes();
		
		if (allEpisode == null || allEpisode.size() == 0) {
			Log.e(TAG, "allepisode is null");
			return;
		}
		
		PackEntity episode = allEpisode.get(packId - 1);
		
		GameDB.getInstance().updateCompleteLevel(packId, levelNum);
		
		if (levelNum < episode.getNumberOfLevel()) {
			SceneManager.getInstance().showWinLevelMenu();
		}
		else {
			//unlock next episode
			if (packId < allEpisode.size()) {
				GameDB.getInstance().unlockEpisode(packId + 1);
				SceneManager.getInstance().showWinEpisodeMenu();
			}
			//game win
			else {
				SceneManager.getInstance().showWinGameScreen();
			}
		}
		
		//play win sound
		GameManager.getInstance().playGameWinSound();
	}
	
	/*
	 * 	handle episode selection
	 */
	public void loadLevel(PackEntity pack) {
		int curLevel = pack.getCurrentLevel();
		int totalLevel = pack.getNumberOfLevel();
		
		Log.i(TAG, "current level: " + pack.getCurrentLevel());
		//if complete all 
		if (curLevel >= totalLevel) {
			curLevel--;
		}
		
		SceneManager.getInstance().handleEpisodeSelection(pack.getId(), curLevel);
	}
	
	/*
	 * 	handle sprites' sizes
	 */
	public void setupRatio(int width) {
		//reset SpriteCurrentEdge to fix value every level
		SpriteCurrentEdge = ResourceManager.FixSizeSpriteEdge;
		
		SpriteScaleFactor = ResourceManager.getInstance().cameraWidth / (width * SpriteCurrentEdge);
		
		//update resize currentEdge
		SpriteCurrentEdge = (int)Math.ceil(SpriteCurrentEdge * SpriteScaleFactor);
		
		/*
		Log.i(TAG, String.format("width:%1$d edge:%2$d scale:%3$f", width, 
				GameManager.getInstance().SpriteCurrentEdge,
				GameManager.getInstance().SpriteScaleFactor));
		*/
	}
	
	
	/*
	 * 	handle game logic
	 */
	public void initMaze() {
		if (mMazeChars == null || mMazeChars.size() == 0)
			return;
		
		int row = mMazeChars.size();
		int col = getMazeWidth();
		
		
		mIntegerMaze = new int[row][col];
		
		for (int i = 0; i < row; i++) {
			String line = mMazeChars.get(i);
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

	//return the max width
	public int getMazeWidth() {
		int result = 0;
		
		for (String line : this.mMazeChars) {
			if (result < line.length()) 
				result = line.length();
		}
		
		return result;
	}
	
	public String getShortestPath(int[] touchPos, int[] botPos) {
		String path = mPathFinder.getShortestPathString(mIntegerMaze, touchPos, botPos);
		return path;
	}
	
	public boolean checkGameWin() {
		for (String line : mMazeChars) {
			//there is still box out there
			if (line.indexOf(LEVEL_BOX_CHAR) > -1)
				return false;
		}
		
		return true;
	}

	public void setCurrentLevel(LevelDetailEntity pCurrentLevel) {
		this.mCurrentLevel = pCurrentLevel;
	}
	
	public LevelDetailEntity getCurrentLevel() {
		
		//first time load
		if (mCurrentLevel == null) {
			this.mCurrentLevel = getNextLevel();
		}
		 
		return this.mCurrentLevel;
	}

	public int[] getMatrixPos(float[] scenePos) {
		int[] result = new int[2];
		
		result[1] = (int) (scenePos[0] / SpriteCurrentEdge);
		result[0] = (int) Math.floor(scenePos[1] / SpriteCurrentEdge);
		
		result[0] = mMazeChars.size() - result[0] - 1;
		
		return result;
	}

	//idx0: row, idx1: col
	public float[] getScenePos(int[] matrixPos) {
		float[] result = new float[2];
		
		result[0] = matrixPos[1] * SpriteCurrentEdge + SpriteCurrentEdge / 2;
		result[1] = (this.mMazeChars.size() - matrixPos[0]) * SpriteCurrentEdge - 
				SpriteCurrentEdge / 2;
		
		return result;
	}
	
	public boolean boxHitWall(int[] nextPos) {
		int row = nextPos[0];
		int col = nextPos[1];
		
		return ((mMazeChars.get(row).charAt(col) == GameManager.LEVEL_WALL_CHAR) || 
				(mMazeChars.get(row).charAt(col) == GameManager.LEVEL_BOX_CHAR) || 
				(mMazeChars.get(row).charAt(col) == GameManager.LEVEL_BOX_ON_TARGET));
	}
	
	public void updateMazeWithNewBoxPos(int[] prevPos, int[] curPos){
		
		//Log.i(TAG, String.format("prev: %1$d-%2$d", prevPos[0], prevPos[1]));
		//Log.i(TAG, String.format("cur: %1$d-%2$d", curPos[0], curPos[1]));
		
		char prevChar = mMazeChars.get(prevPos[0]).charAt(prevPos[1]);
		char curChar = mMazeChars.get(curPos[0]).charAt(curPos[1]);
		
		if (prevChar == GameManager.LEVEL_BOX_ON_TARGET) 
			prevChar = GameManager.LEVEL_TARGET;
		else 
			prevChar = GameManager.LEVEL_SPACE;

		if (curChar == GameManager.LEVEL_TARGET) 
			curChar = GameManager.LEVEL_BOX_ON_TARGET;
		else 
			curChar = GameManager.LEVEL_BOX_CHAR;
		
		//replace mutable string
		updateMazeChars(prevPos, prevChar);
		updateMazeChars(curPos, curChar);
	}
	
	//after the boxes moved, the mazechars is updated accordingly
	//string within mazeChars is replaced by this function
	public void updateMazeChars(int[] pos, char newChar) {
		String tmp = mMazeChars.get(pos[0]);
		StringBuilder tmpBuilder = new StringBuilder(tmp);
		tmpBuilder.setCharAt(pos[1], newChar);
		mMazeChars.set(pos[0], tmpBuilder.toString());
	}
	
	/*
	 * 	for sound control
	 */
	public void toggleSound() { 
		if (mSoundEnable) {
			mSoundEnable = false; 
			if (ResourceManager.getInstance().gameMusic.isPlaying())
				ResourceManager.getInstance().gameMusic.pause();
		}
		else {
			mSoundEnable = true;
			if (!ResourceManager.getInstance().gameMusic.isPlaying()) {
				ResourceManager.getInstance().gameMusic.play();
			}
		}
	}
	
	public void playMusic() {
		if (mSoundEnable) {
			if (!ResourceManager.getInstance().gameMusic.isPlaying()) {
				ResourceManager.getInstance().gameMusic.play();
			}
		}
	}
	
	public void stopMusic() {
		if (ResourceManager.getInstance().gameMusic.isPlaying())
			ResourceManager.getInstance().gameMusic.pause();
	}
	
	public void playGameWinSound() {
		if (mSoundEnable) {
			ResourceManager.getInstance().gameWinSound.play();
		}
	}
	
	public void playInHoleSound() {
		if (mSoundEnable) {
			ResourceManager.getInstance().gameInHoleSound.play();
		}
	}
	
	public void playPushSound() {
		if (mSoundEnable) {
			ResourceManager.getInstance().gamePushSound.play();
		}
	}
	
	public void playRunSound() {
		if (mSoundEnable) {
			ResourceManager.getInstance().gameRunSound.play();
		}
	}
	
	/*	
	 * 	getters and setters
	 */
	public void setMaze(List<String> mazeChars) {
		this.mMazeChars = mazeChars;
	}
	
	public List<String> getMaze() {
		return this.mMazeChars;
	}
}
