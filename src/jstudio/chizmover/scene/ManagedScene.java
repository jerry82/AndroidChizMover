package jstudio.chizmover.scene;

import jstudio.chizmover.managers.ResourceManager;
import jstudio.chizmover.managers.SceneManager;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

public abstract class ManagedScene extends Scene {
	
	protected float mWidth;
	protected float mHeight;
	
	//background
	protected BitmapTextureAtlas mBackgroundTextureAtlas;
	protected ITextureRegion mBackgroundTextureRegion;
	protected Sprite mBackgroundSprite;
	
	protected ManagedScene(String backgroundImageName) {
		mWidth = ResourceManager.getInstance().cameraWidth;
		mHeight = ResourceManager.getInstance().cameraHeight;
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mBackgroundTextureAtlas = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), (int)Math.round(mWidth), (int)Math.round(mHeight), TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTextureAtlas, ResourceManager.getActivity(), backgroundImageName, 0, 0);
	}
	
	//to be called by SceneManager
	public void onLoadManagedScene() {
		onLoadScene();
	}
	
	public void onUnloadManagedScene() {
		onUnloadScene();
	}
	
	public void onShowManagedScene() {
		
	}
	
	public void onHideManagedScene() {
		
	}
	//end
	
	//methods to override in subclasses
	protected void onLoadScene() {
		mBackgroundTextureAtlas.load();
		
		if (this instanceof SplashScreen) {
			mBackgroundSprite = new Sprite(0, 0, mBackgroundTextureRegion, ResourceManager.getEngine().getVertexBufferObjectManager())
			{
			    @Override
			    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
			    {
			        if (pSceneTouchEvent.isActionUp())
			        {
			        	SceneManager.getInstance().showScene(new InGameScreen());
			        }
			        return true;
			    };
			};
			this.registerTouchArea(mBackgroundSprite);
		}
		
		else {
			mBackgroundSprite = new Sprite(0, 0, mBackgroundTextureRegion, ResourceManager.getEngine().getVertexBufferObjectManager());
		}
		
	    //splash.setScale(1f);
		mBackgroundSprite.setWidth(mWidth);
		mBackgroundSprite.setHeight(mHeight);
		mBackgroundSprite.setPosition(mWidth * 0.5f, mHeight * 0.5f);
		
		this.attachChild(mBackgroundSprite);
	}
	
	protected void onUnloadScene() {
		mBackgroundTextureAtlas.unload();
	}
	public abstract void onShowScene();
	public abstract void onHideScene();
	
}
