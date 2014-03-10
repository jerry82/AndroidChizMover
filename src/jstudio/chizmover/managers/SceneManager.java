package jstudio.chizmover.managers;

import jstudio.chizmover.data.LevelDetailEntity;
import jstudio.chizmover.scene.InGameMenu;
import jstudio.chizmover.scene.InGameScreen;
import jstudio.chizmover.scene.ManagedScene;
import jstudio.chizmover.scene.PauseMenu;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
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
		
		createInGameMenu();
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
	
	//only create when there's no menu
	public void createInGameMenu() {
		if (mCurrentScene instanceof InGameScreen) {
			InGameMenu menu = new InGameMenu(ResourceManager.getInstance().camera);
			mCurrentScene.setChildScene(menu);
			((InGameScreen)mCurrentScene).setMenu(menu);
			mCurrentMenu = CurrentMenu.NoMenu;
		}
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
}
