package jstudio.chizmover.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;

public class PauseMenu extends MenuScene implements IOnMenuItemClickListener  {
	
	public PauseMenu(Camera camera) {
		super(camera);
	}
	
	public void onLoad() {
		
	}
	
	public void unLoad() {
		
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		// TODO Auto-generated method stub
		return false;
	}

}
