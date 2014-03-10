package jstudio.chizmover.scene;

import jstudio.chizmover.managers.ResourceManager;

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
	private BitmapTextureAtlas mMenuBgBMP;
	private ITextureRegion mMenuBgTR;
	
	//items
	private BitmapTextureAtlas mMenuBMP;
	private ITextureRegion mMenuTR;
	
	private BitmapTextureAtlas mRestartBMP;
	private ITextureRegion mRestartTR;
	
	private BitmapTextureAtlas mRadioOnBMP;
	private ITextureRegion mRadioOnTR;
	
	private BitmapTextureAtlas mHelpBMP;
	private ITextureRegion mHelpTR;
	
	
	public PauseMenu(Camera camera) {
		super(camera);
		
		onLoad();
	}
	
	public void onLoad() {
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
	
		
		
		mMenuBgBMP.load();
		mMenuBMP.load();
		mRestartBMP.load();
		mRadioOnBMP.load();
		mHelpBMP.load();
		
		final IMenuItem menuBgMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(-1, mMenuBgTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1f , 1f);
		
		final IMenuItem menuMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.MenuID, mMenuTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.1f , 1f);
		
		final IMenuItem restartMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.RestartID, mRestartTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.1f , 1f);
		
		final IMenuItem radioOnMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.RadioOnID, mRadioOnTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.1f , 1f);
		
		final IMenuItem helpMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.HelpID, mHelpTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.1f , 1f);
		
		
	    this.addMenuItem(menuBgMI);
	    this.addMenuItem(menuMI);
	    this.addMenuItem(restartMI);
	    this.addMenuItem(radioOnMI);
	    this.addMenuItem(helpMI);
	    
	    this.buildAnimations();
	    this.setBackgroundEnabled(false);
	    
	    //set position
	    float width = this.getCamera().getWidth();
	    float height = this.getCamera().getHeight();
	    float pad = width / 10;
	    
	    menuBgMI.setPosition(width / 2, height / 2);
	    restartMI.setPosition(menuBgMI.getX() - pad / 2, menuBgMI.getY());
	    menuMI.setPosition(restartMI.getX() - restartMI.getWidth(), menuBgMI.getY());
	    radioOnMI.setPosition(menuBgMI.getX() + pad / 2, menuBgMI.getY());
	    helpMI.setPosition(radioOnMI.getX() + radioOnMI.getWidth(), menuBgMI.getY());
	    
	    this.setOnMenuItemClickListener(this);
	}
	
	public void unLoad() {
		mMenuBgBMP.unload();
		mMenuBMP.unload();
		mRestartBMP.unload();
		mRadioOnBMP.unload();
		mHelpBMP.unload();
		
		this.detachChildren();
		this.detachSelf();
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		// TODO Auto-generated method stub
	    switch(pMenuItem.getID())
	    {
	        case ResourceManager.MenuID:
	        	Log.i(TAG, "click menu");
	            //action
	            return true;
	            
	        case ResourceManager.RestartID:
	        	Log.i(TAG, "click restart");
	            //action
	            return true;
	        
	        case ResourceManager.RadioOnID:
	        	Log.i(TAG, "click radio");
	        	return true;
	        
	        case ResourceManager.HelpID:
	        	Log.i(TAG, "click help");
	        	return true;
	        
	        default:
	        	//Log.i(TAG, "click outside item");
	            return false;
	    }
	}

}
