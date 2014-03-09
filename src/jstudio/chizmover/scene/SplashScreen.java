package jstudio.chizmover.scene;

import jstudio.chizmover.managers.ResourceManager;
import jstudio.chizmover.managers.SceneManager;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

public class SplashScreen extends ManagedScene {
	
	private float mAnimationDuration = 4f; //4s default
	
	public SplashScreen() {
		super(ResourceManager.SplashScreenImage);
	}
	
	@Override
	public void onLoadScene() {
		// TODO Auto-generated method stub
		super.onLoadScene();
	}

	@Override
	public void onUnloadScene() {
		// TODO Auto-generated method stub
		super.onUnloadScene();
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
