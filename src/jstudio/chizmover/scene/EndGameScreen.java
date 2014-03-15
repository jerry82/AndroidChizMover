package jstudio.chizmover.scene;

import jstudio.chizmover.managers.ResourceManager;
import jstudio.chizmover.managers.SceneManager;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import android.util.Log;

public class EndGameScreen extends ManagedScene implements IOnSceneTouchListener{
	
	private final String TAG = "EndGameScreen";
	
	public EndGameScreen() {
		super(ResourceManager.EndGameScreenImage);
		
		this.setOnSceneTouchListener(this);
	}
	
	@Override
	public void onLoadScene() {
		// TODO Auto-generated method stub
		super.onLoadScene();
	}

	@Override
	public void onUnloadScene() {
		// TODO Auto-generated method stub
		super.onUnloadScene();
		
		this.clearTouchAreas();
	}

	@Override
	public void onShowScene() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHideScene() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			SceneManager.getInstance().showScene(new SplashScreen());
			Log.i(TAG, "onSceneTouchEvent");
		}
		return true;
	}

}
