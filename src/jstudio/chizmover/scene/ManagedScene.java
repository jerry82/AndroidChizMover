package jstudio.chizmover.scene;

import org.andengine.entity.scene.Scene;

public abstract class ManagedScene extends Scene {
	
	public void onLoadManagedScene() {
		onLoadScene();
	}
	
	public void onUnloadManagedScene() {
		
	}
	
	public void onShowManagedScene() {
		
	}
	
	public void onHideManagedScene() {
		
	}
	
	//methods to override in subclasses
	public abstract void onLoadScene();
	public abstract void onUnloadScene();
	public abstract void onShowScene();
	public abstract void onHideScene();
	
}
