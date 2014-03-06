package jstudio.chizmover.managers;

import jstudio.chizmover.runtime.MainActivity;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;

public class ResourceManager {

	//constants
	private static final ResourceManager INSTANCE = new ResourceManager();
	
	//variables
	public MainActivity activity;
	public LimitedFPSEngine engine;
	
	public float cameraWidth;
	public float cameraHeight;
	
	//variables - game resources
	public static TextureRegion gameSplashScreenTR;
	public static BitmapTextureAtlas gameSplashScreenBMP;
	
	public static TextureRegion gameBotTR;
	public static BitmapTextureAtlas gameBotBMP;
	
	public static TextureRegion gameBackgroundTR;
	public static BitmapTextureAtlas gameBackgroundBMP;
	
	public static Font gameFont;
	
	/*
	 * 	constructor and setup
	 */
	public ResourceManager() {
	}
	
	//public instance
	public static ResourceManager getInstance() {
		return INSTANCE;
	}
	
	public static MainActivity getActivity() {
		return getInstance().activity;
	}
	
	public static Engine getEngine() {
		return getInstance().engine;
	}
	
	public static void setup (MainActivity pActivity, LimitedFPSEngine pEngine, float pCameraWidth, float pCameraHeight) {
		getInstance().activity = pActivity;
		getInstance().engine = pEngine;
		getInstance().cameraWidth = pCameraWidth;
		getInstance().cameraHeight = pCameraHeight;
		
		//load splashscene first
		getInstance().loadSplashScreenResources();
	}
	
	/*
	 * 	loading resources
	 */

	
	public static void loadGameResources() {
		getInstance().loadGameTextures();
		getInstance().loadFont();
		getInstance().loadSounds();
	}
	
	//privates
	private void loadSplashScreenResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		if (gameSplashScreenTR == null) {
			gameSplashScreenBMP = 
					new BitmapTextureAtlas(getInstance().activity.getTextureManager(), 320, 568, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
						
			gameSplashScreenTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameSplashScreenBMP, getInstance().activity, "SplashScreen.png", 0, 0);
			gameSplashScreenBMP.load();
		}
		
	}
	
	private void loadGameTextures() {
		//load graphics
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		//get bot
		if (gameBotTR == null) {
			gameBotBMP = 
					new BitmapTextureAtlas(activity.getTextureManager(), 40, 40, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			gameBotTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameBotBMP, activity, "bot40.png", 0, 0);
		}
		
		//get background
		if (gameBackgroundTR == null) {
			gameBackgroundBMP = 
					new BitmapTextureAtlas(activity.getTextureManager(), 360, 568, TextureOptions.DEFAULT);
			gameBackgroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameBackgroundBMP, activity, "screen.png", 0, 0);
		}
	}
	
	private void loadFont() {
		
	}
	
	private void loadSounds() {
		if (gameFont == null) {
			FontFactory.setAssetBasePath("font/");
		}
	}
}
