package jstudio.chizmover.managers;

import jstudio.chizmover.data.LevelDetailEntity;
import jstudio.chizmover.data.PackEntity;
import jstudio.chizmover.scene.*;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;


import android.util.Log;

public class SceneManager extends Object{
	
	//global instance
	private static final SceneManager INSTANCE = new SceneManager();
	
	private static final String TAG = "SceneManager";
	/*
	 *	variables
	 */
	public ManagedScene mCurrentScene;
	private ManagedScene mNextScene;
	private Engine mEngine;
	
	private CurrentMenu mCurrentMenu;

	private PauseMenu mPauseMenu;
	private WinLevelMenu mWinLevelMenu;
	private WinEpisodeMenu mWinEpisodeMenu;
	private InstructionMenu mInstructionMenu;
	
	public SceneManager() {
		mEngine = ResourceManager.getEngine();
	}
	
	public static SceneManager getInstance() {
		return INSTANCE;
	}
	
	public void showScene(ManagedScene pManagedScene) {
		
		mNextScene = pManagedScene;
		mEngine.setScene(mNextScene);
		
		// If a previous managed scene exists, hide and unload it.
		if(mCurrentScene!=null)
		{
			mCurrentScene.onHideManagedScene();
			mCurrentScene.onUnloadManagedScene();
		}
		// Load and show the new managed scene, and set it as the current scene.
		mNextScene.onLoadManagedScene();
		mNextScene.onShowManagedScene();
		mCurrentScene = mNextScene;
	}

	/*
	 *	win dialog 
	 */
	public void showWinLevelMenu() {
		//show another menu
		if (mCurrentScene instanceof InGameScreen) {
			if (mCurrentMenu == CurrentMenu.NoMenu) {
				mWinLevelMenu = new WinLevelMenu(ResourceManager.getInstance().camera);
				((InGameScreen)mCurrentScene).getMenu().setChildScene(mWinLevelMenu);
				mCurrentMenu = CurrentMenu.CompleteLevelMenu;
			}
		}
	}
	
	public void showWinEpisodeMenu() {
		//show another menu
		if (mCurrentScene instanceof InGameScreen) {
			if (mCurrentMenu == CurrentMenu.NoMenu) {
				mWinEpisodeMenu = new WinEpisodeMenu(ResourceManager.getInstance().camera);
				((InGameScreen)mCurrentScene).getMenu().setChildScene(mWinEpisodeMenu);
				mCurrentMenu = CurrentMenu.CompleteEpisodeMenu;
			}
		}
	}
	
	public void showInstructionMenu() {
		if (mCurrentScene instanceof InGameScreen) {
			hidePauseMenu();
			
			mInstructionMenu = new InstructionMenu(ResourceManager.getInstance().camera);
			((InGameScreen)mCurrentScene).getMenu().setChildScene(mInstructionMenu);
			mCurrentMenu = CurrentMenu.InstructionMenu;
		}
	}
	
	public void hideInstructionMenu() {
		if (mInstructionMenu == null) return;
		
		mInstructionMenu.unLoad();
		mCurrentMenu = CurrentMenu.NoMenu;
	}
	
	public void showWinGameScreen() {
		showScene(new EndGameScreen());
	}
	
	/*
	 * 	handle main menu
	 */
	public void handlePauseBtnClick() {
		//show another menu
		if (mCurrentScene != null) {
			if (mCurrentMenu == CurrentMenu.NoMenu) {
				mPauseMenu = new PauseMenu(ResourceManager.getInstance().camera);
				((InGameScreen)mCurrentScene).getMenu().setChildScene(mPauseMenu);
				mCurrentMenu = CurrentMenu.PauseMenu;
			}
		}
	}
	
	public void hidePauseMenu() {
		if (mPauseMenu == null) return;
		
		mPauseMenu.unLoad();
		mCurrentMenu = CurrentMenu.NoMenu;
	}
	
	
	
	public void handlePrevBtnClick() {
		LevelDetailEntity prevLevel = GameManager.getInstance().getPrevLevel();
		
		if (prevLevel == null) {
			Log.i(TAG, " no more prev level ");
			return;
		}
		
		GameManager.getInstance().setCurrentLevel(prevLevel);
		showScene(new InGameScreen());
		
	}
	
	public void handleNextBtnClick() { 
		LevelDetailEntity nextLevel = GameManager.getInstance().getNextLevel();
		
		if (nextLevel == null) {
			Log.i(TAG, " no more next level ");
			return;
		}
		
		GameManager.getInstance().setCurrentLevel(nextLevel);
		showScene(new InGameScreen());
	}
	
	public void handleEpisodeSelection(int packId, int levelNum) {
		LevelDetailEntity curLevel = GameManager.getInstance().getLevelDetail(packId, levelNum);
		
		if (curLevel == null) {
			Log.e(TAG, "error get curLevel");
			return;
		}
		
		GameManager.getInstance().setCurrentLevel(curLevel);
		showScene(new InGameScreen());
	}
	
	//only create when there's no menu
	public MenuScene createInGameMenu() {
		boolean showPrev = false;
		boolean showNext = false;
		
		LevelDetailEntity entity = GameManager.getInstance().getCurrentLevel();
		showPrev = entity.getLevelNum() > 1;
		
		PackEntity pack = GameManager.getInstance().getEpisode(entity.getPackId());
		showNext = entity.getLevelNum() < pack.getCurrentLevel();
		
		InGameMenu menu = new InGameMenu(ResourceManager.getInstance().camera, showPrev, showNext);
		//mCurrentScene.setChildScene(menu);
		mCurrentMenu = CurrentMenu.NoMenu;
		
		return menu;
	}
	
	public CurrentMenu getCurrentMenu() {
		return this.mCurrentMenu;
	}

	public ManagedScene getCurrentScene() {
		return mCurrentScene;
	}
	
	/*
	 * 	handle popup menu
	 */
	public void onBackKeyPressed() {
		if (mCurrentScene == null) 
			return;
		
		System.exit(0);
		
		/*
		if (mCurrentScene instanceof SplashScreen) {
			System.exit(0);
		}
		else if (mCurrentScene instanceof EpisodeScreen) {
			showScene(new SplashScreen());
		}
		else if (mCurrentScene instanceof InGameScreen) {
			showScene(new EpisodeScreen());
		}*/
	}
}
