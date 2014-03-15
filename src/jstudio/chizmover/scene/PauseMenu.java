package jstudio.chizmover.scene;

import jstudio.chizmover.managers.CurrentMenu;
import jstudio.chizmover.managers.GameManager;
import jstudio.chizmover.managers.ResourceManager;
import jstudio.chizmover.managers.SceneManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;


import android.util.Log;

public class PauseMenu extends MenuScene implements IOnMenuItemClickListener  {
	
	private static final String TAG = "PauseMenu";
	

	
	public PauseMenu(Camera camera) {
		super(camera);
		onLoad();
	}
	
	protected void loadResource() {

	}
	
	public void onLoad() {
		loadResource();
		
		final IMenuItem menuBgMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(-1, ResourceManager.MenuBgTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1f , 1f);
		
		final IMenuItem menuMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.MenuID, ResourceManager.MenuTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
		
		final IMenuItem restartMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.RestartID, ResourceManager.RestartTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
		
		final IMenuItem radioOnMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.RadioOnID, ResourceManager.RadioOnTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
		
		final IMenuItem helpMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.HelpID, ResourceManager.HelpTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
		
		
	    this.attachChild(menuBgMI);
	    this.addMenuItem(menuMI);
	    this.addMenuItem(restartMI);
	    this.addMenuItem(radioOnMI);
	    this.addMenuItem(helpMI);
	    
	    this.buildAnimations();
	    this.setBackgroundEnabled(false);
	    
	    //set position
	    float width = this.getCamera().getWidth();
	    float height = this.getCamera().getHeight();
	    float pad = 33f;
	    
	    menuBgMI.setPosition(width / 2, height / 2);
	    
	    restartMI.setPosition(menuBgMI.getX() - pad, menuBgMI.getY());
	    menuMI.setPosition(menuBgMI.getX() - 3 * pad, menuBgMI.getY());
	    radioOnMI.setPosition(menuBgMI.getX() + pad, menuBgMI.getY());
	    helpMI.setPosition(menuBgMI.getX() + 3 * pad, menuBgMI.getY());
	    
	    this.setOnMenuItemClickListener(this);
	}
	
	public void unLoad() {
		this.detachChildren();
		this.detachSelf();
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		// TODO Auto-generated method stub
		if (SceneManager.getInstance().getCurrentMenu() != CurrentMenu.NoMenu) {
		    switch(pMenuItem.getID())
		    {
		        case ResourceManager.MenuID:
		        	Log.i(TAG, "click menu");
		            //action
		        	SceneManager.getInstance().showScene(new EpisodeScreen());
		            return true;
		            
		        case ResourceManager.RestartID:
		        	Log.i(TAG, "click restart");
		        	SceneManager.getInstance().showScene(new InGameScreen());
		            //action
		            return true;
		        
		        case ResourceManager.RadioOnID:
		        	Log.i(TAG, "click radio");
		        	GameManager.getInstance().toggleSound();
		        	return true;
		        
		        case ResourceManager.HelpID:
		        	Log.i(TAG, "click help");
		        	SceneManager.getInstance().showInstructionMenu();
		        	return true;
		        
		        case ResourceManager.NextID:
		        	Log.i(TAG, "click next");
		        	SceneManager.getInstance().handleNextBtnClick();
		        	return true;

		        default:
		        	//Log.i(TAG, "click outside item");
		            return false;
		    }
		}
		return false;
	}
}
