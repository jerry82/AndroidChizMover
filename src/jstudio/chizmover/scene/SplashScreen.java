package jstudio.chizmover.scene;

import jstudio.chizmover.managers.ResourceManager;
import jstudio.chizmover.managers.SceneManager;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;

public class SplashScreen extends ManagedScene {
	
	private float mAnimationDuration = 4f; //4s default
	private BitmapTextureAtlas mSplashTextureAtlas;
	private ITextureRegion mSplashTextureRegion;
	private Sprite mSplash;
	
	private float mWidth; 
	private float mHeight;
	
	public SplashScreen(float pAnimationDuration) {
		mAnimationDuration = pAnimationDuration;
		
		mWidth = ResourceManager.getInstance().cameraWidth;
		mHeight = ResourceManager.getInstance().cameraHeight;
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mSplashTextureAtlas = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), 380, 568, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mSplashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mSplashTextureAtlas, ResourceManager.getActivity(), "SplashScreen.png", 0, 0);
	}
	
	@Override
	public void onLoadScene() {
		// TODO Auto-generated method stub
		mSplashTextureAtlas.load();
		
		mSplash = new Sprite(0, 0, mSplashTextureRegion, ResourceManager.getEngine().getVertexBufferObjectManager())
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
	    //splash.setScale(1f);
		mSplash.setWidth(mWidth);
		mSplash.setHeight(mHeight);
		mSplash.setPosition(mWidth * 0.5f, mHeight * 0.5f);
		
		this.registerTouchArea(mSplash);
		this.attachChild(mSplash);
	}

	@Override
	public void onUnloadScene() {
		// TODO Auto-generated method stub
		mSplashTextureAtlas.unload();
	}

	@Override
	public void onShowScene() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHideScene() {
		// TODO Auto-generated method stub

	}

}
