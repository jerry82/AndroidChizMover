package jstudio.chizmover.scene;

import jstudio.chizmover.managers.CurrentMenu;
import jstudio.chizmover.managers.ResourceManager;
import jstudio.chizmover.managers.SceneManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException;

import android.graphics.Color;
import android.util.Log;

public class PauseMenu extends MenuScene implements IOnMenuItemClickListener  {
	
	private static final String TAG = "PauseMenu";
	
	//background img
	protected BitmapTextureAtlas mMenuBgBMP;
	protected ITextureRegion mMenuBgTR;
	
	//items
	protected BitmapTextureAtlas mMenuBMP;
	protected ITextureRegion mMenuTR;
	
	protected BitmapTextureAtlas mRestartBMP;
	protected ITextureRegion mRestartTR;
	
	protected BitmapTextureAtlas mRadioOnBMP;
	protected ITextureRegion mRadioOnTR;
	
	protected BitmapTextureAtlas mHelpBMP;
	protected ITextureRegion mHelpTR;
	
	protected BitmapTextureAtlas mNextBMP;
	protected ITextureRegion mNextTR;
	
	public PauseMenu(Camera camera) {
		super(camera);
		onLoad();
	}
	
	protected void loadResource() {
		mMenuBgBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), 280, 90, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mMenuBgTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMenuBgBMP, ResourceManager.getActivity(), ResourceManager.MenuBgImage, 0, 0);
		
		mMenuBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mMenuTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMenuBMP, ResourceManager.getActivity(), ResourceManager.MenuImage, 0, 0);
		
		mRestartBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mRestartTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mRestartBMP, ResourceManager.getActivity(), ResourceManager.RestartImage, 0, 0);
		
		mRadioOnBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mRadioOnTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mRadioOnBMP, ResourceManager.getActivity(), ResourceManager.RadioOnImage, 0, 0);
		
		mHelpBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mHelpTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mHelpBMP, ResourceManager.getActivity(), ResourceManager.HelpImage, 0, 0);
		
		mNextBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mNextTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mNextBMP, ResourceManager.getActivity(), ResourceManager.NextImageName, 0, 0);
		
		mMenuBgBMP.load();
		mMenuBMP.load();
		mRestartBMP.load();
		mRadioOnBMP.load();
		mHelpBMP.load();
		mNextBMP.load();
	}
	
	public void onLoad() {
		loadResource();
		
		final IMenuItem menuBgMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(-1, mMenuBgTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1f , 1f);
		
		final IMenuItem menuMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.MenuID, mMenuTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
		
		final IMenuItem restartMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.RestartID, mRestartTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
		
		final IMenuItem radioOnMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.RadioOnID, mRadioOnTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
		
		final IMenuItem helpMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.HelpID, mHelpTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
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
		mMenuBgBMP.unload();
		mMenuBMP.unload();
		mRestartBMP.unload();
		mRadioOnBMP.unload();
		mHelpBMP.unload();
		mNextBMP.unload();
		
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
		        	if (ResourceManager.getInstance().gameSound.isPlaying())
		        		ResourceManager.getInstance().gameSound.pause();
		        	else 
		        		ResourceManager.getInstance().gameSound.play();
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
