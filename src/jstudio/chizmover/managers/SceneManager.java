package jstudio.chizmover.managers;

import jstudio.chizmover.scene.ManagedScene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;

public class SceneManager extends Object{
	
	//global instance
	private static final SceneManager INSTANCE = new SceneManager();
	
	/*
	 *	variables
	 */
	public ManagedScene mCurrentScene;
	private ManagedScene mNextScene;
	private Engine mEngine;
	
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
	 * 	handle main menu
	 */
	public void handlePauseBtnClick() {
		//show another menu
	}
	
	public void handlePrevBtnClick() {
		
	}
	
	public void handleNextBtnClick() { 
		
	}
	
	/*
	 * 	handle popup menu
	 */
}
