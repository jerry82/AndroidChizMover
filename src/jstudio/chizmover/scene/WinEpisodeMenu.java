package jstudio.chizmover.scene;

import jstudio.chizmover.managers.ResourceManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;

public class WinEpisodeMenu extends PauseMenu {

	private static final String TAG = "WinLevelMenu";

	public WinEpisodeMenu(Camera camera) {
		super(camera);
		// TODO Auto-generated constructor stub
	}
	
	public void unLoad() {
		super.unLoad();
	}
	
	public void onLoad() {
		
		final IMenuItem menuBgMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(-1, ResourceManager.BgEpisodeWinTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1f , 1f);
		
		final IMenuItem menuMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.MenuID, ResourceManager.MenuTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
		
	    this.attachChild(menuBgMI);
	    this.addMenuItem(menuMI);
	    
	    this.buildAnimations();
	    this.setBackgroundEnabled(false);
	    
	    //set position
	    float width = this.getCamera().getWidth();
	    float height = this.getCamera().getHeight();
	    
	    menuBgMI.setPosition(width / 2, height / 2);
	    
	    menuMI.setPosition(menuBgMI.getX(), menuBgMI.getY() - 15);
	    
	    this.setOnMenuItemClickListener(this);
	}
	
}