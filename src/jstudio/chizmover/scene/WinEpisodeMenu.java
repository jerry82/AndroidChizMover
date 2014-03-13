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

public class WinEpisodeMenu extends PauseMenu {

	private static final String TAG = "WinLevelMenu";
	
	protected BitmapTextureAtlas mBgEpisodeWinBMP;
	protected ITextureRegion mBgEpisodeWinTR;
	
	public WinEpisodeMenu(Camera camera) {
		super(camera);
		// TODO Auto-generated constructor stub
	}
	
	public void loadResource() {
		super.loadResource();
		
		//load extra
		mBgEpisodeWinBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), 280, 120, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mBgEpisodeWinTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBgEpisodeWinBMP, ResourceManager.getActivity(), ResourceManager.WinEpisodeBgImage, 0, 0);
		mBgEpisodeWinBMP.load();
	}
	
	public void unLoad() {
		super.unLoad();
		
		mBgEpisodeWinBMP.unload();
	}
	
	public void onLoad() {
		
		loadResource();
		
		final IMenuItem menuBgMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(-1, mBgEpisodeWinTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1f , 1f);
		
		final IMenuItem menuMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.MenuID, mMenuTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
		
	    this.attachChild(menuBgMI);
	    this.addMenuItem(menuMI);
	    
	    this.buildAnimations();
	    this.setBackgroundEnabled(false);
	    
	    //set position
	    float width = this.getCamera().getWidth();
	    float height = this.getCamera().getHeight();
	    float pad = 28f;
	    
	    menuBgMI.setPosition(width / 2, height / 2);
	    
	    menuMI.setPosition(menuBgMI.getX(), menuBgMI.getY() - 15);
	    
	    this.setOnMenuItemClickListener(this);
	}
	
}