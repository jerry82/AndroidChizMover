package jstudio.chizmover.scene;

import jstudio.chizmover.managers.ResourceManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

public class WinLevelMenu extends PauseMenu {

	private static final String TAG = "WinLevelMenu";
	
	protected BitmapTextureAtlas mBgLevelWinBMP;
	protected ITextureRegion mBgLevelWinTR;
	
	public WinLevelMenu(Camera camera) {
		super(camera);
		// TODO Auto-generated constructor stub
	}
	
	public void loadResource() {
		super.loadResource();
		
		//load extra
		mBgLevelWinBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), 280, 120, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mBgLevelWinTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBgLevelWinBMP, ResourceManager.getActivity(), ResourceManager.WinLevelBgImage, 0, 0);
		mBgLevelWinBMP.load();
	}
	
	public void unLoad() {
		super.unLoad();
		
		mBgLevelWinBMP.unload();
	}
	
	public void onLoad() {
		
		loadResource();
		
		final IMenuItem menuBgMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(-1, mBgLevelWinTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1f , 1f);
		
		final IMenuItem menuMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.MenuID, mMenuTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
		
		final IMenuItem restartMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.RestartID, mRestartTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
		
		final IMenuItem nextMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.NextID, mNextTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
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