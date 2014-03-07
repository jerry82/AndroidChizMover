package jstudio.chizmover.runtime;

import java.io.IOException;

import jstudio.chizmover.data.GameDB;
import jstudio.chizmover.managers.ResourceManager;
import jstudio.chizmover.managers.SceneManager;
import jstudio.chizmover.scene.SplashScreen;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;


public class MainActivity extends BaseGameActivity {

	private static final float HEIGHT = 800;
	private static final float WIDTH = 480;
	
	private Scene mScene;
	private Camera mCamera;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		
		mCamera = new Camera(0, 0, WIDTH, HEIGHT);
		
		/*
		 * resolution policy includes:
		 * . FillResolutionPolicy()
		 * . FixedResolutionPolicy()
		 * . RatioResolutionPolicy(float: width/height)
		 * . RelativeResolutionPolicy() //careful to use...ignore
		 */
		
		EngineOptions engineOptions = new EngineOptions
				(true, ScreenOrientation.PORTRAIT_FIXED, 
				new FillResolutionPolicy(), mCamera);
		
		//prevent turning off while playing
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		
		return engineOptions;
	}
	
	@Override
	public Engine onCreateEngine(final EngineOptions pEngineOptions) {
		return new LimitedFPSEngine(pEngineOptions, 60);
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		
		ResourceManager.setup(this, (LimitedFPSEngine) this.getEngine(), WIDTH, HEIGHT);
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		
		SceneManager.getInstance().showScene(new SplashScreen());
		
		GameDB.getInstance().getLevelDetail(1, 1);
		
		pOnCreateSceneCallback.onCreateSceneFinished(getEngine().getScene());
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
				
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}
}
