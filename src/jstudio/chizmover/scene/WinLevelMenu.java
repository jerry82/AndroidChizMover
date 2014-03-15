package jstudio.chizmover.scene;

import jstudio.chizmover.managers.ResourceManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;

public class WinLevelMenu extends PauseMenu {

	private static final String TAG = "WinLevelMenu";
	
	public WinLevelMenu(Camera camera) {
		super(camera);
		// TODO Auto-generated constructor stub
	}
	
	public void unLoad() {
		super.unLoad();
	}
	
	public void onLoad() {

		final IMenuItem menuBgMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(-1, ResourceManager.BgLevelWinTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1f , 1f);
		
		final IMenuItem menuMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.MenuID, ResourceManager.MenuTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
		
		final IMenuItem restartMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.RestartID, ResourceManager.RestartTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
		
		final IMenuItem nextMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.NextID, ResourceManager.NextTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
		
	    this.attachChild(menuBgMI);
	    this.addMenuItem(menuMI);
	    this.addMenuItem(restartMI);
	    this.addMenuItem(nextMI);
	    
	    this.buildAnimations();
	    this.setBackgroundEnabled(false);
	    
	    //set position
	    float width = this.getCamera().getWidth();
	    float height = this.getCamera().getHeight();
	    float pad = 28f;
	    
	    menuBgMI.setPosition(width / 2, height / 2);
	    
	    restartMI.setPosition(menuBgMI.getX(), menuBgMI.getY() - 10);
	    menuMI.setPosition(menuBgMI.getX() - 3 * pad, restartMI.getY());
	    nextMI.setPosition(menuBgMI.getX() + 3 * pad, restartMI.getY());
	    
	    this.setOnMenuItemClickListener(this);
	}
}