package jstudio.chizmover.managers;

import jstudio.chizmover.runtime.MainActivity;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;

import android.graphics.Color;

import jstudio.chizmover.data.*;
public class ResourceManager {

	//constants
	private static final ResourceManager INSTANCE = new ResourceManager();
	
	//variables
	public MainActivity activity;
	public LimitedFPSEngine engine;
	public Camera camera;
	
	public float cameraWidth;
	public float cameraHeight;
	
	//variables - game resources
	public static final String SplashScreenImage = "SplashScreen_480x800.png";
	public static final String EpisodeScreenImage = "episodeScreen_480x800.png";
	public static final String GameBackgroundImage = "screen_480x800.png";
	
	public static final String WallImageName = "block40.png";
	public static final String BotImageName = "bot40.png";
	public static final String BoxImageName = "box40.png";
	public static final String TargetImageName = "spot40.png";
	public static final String CanmoveImage = "canMove40.png";
	public static final String CannotMoveImage = "cannotMove40.png";
	
	public static final String PauseImageName = "pause50.png";
	public static final int PauseID = 0;
	
	public static final String PrevImageName = "back_btn50.png";
	public static final int PrevID = 1;
	
	public static final String NextImageName = "next_btn50.png";
	public static final int NextID = 2;
	
	public static final String MenuBgImage = "menu.png";
	
	public static final String MenuImage = "menu_btn50.png";
	public static final int MenuID = 0;
	
	public static final String RestartImage = "restart_btn50.png";
	public static final int RestartID = 1;
	
	public static final String RadioOnImage = "radioon_btn50.png";
	public static final int RadioOnID = 2;
	
	public static final String RadioOffImage = "radiooff_btn50.png";
	public static final int RadioOffID = 3;
	
	public static final String HelpImage = "help50.png";
	public static final int HelpID = 4;
	
	
	public static final int FixSizeSpriteEdge = 40;
	public static final int FixSizeBtnEdge = 50;
	
	public static Font GameFont;
	
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
	
	public static void setup (MainActivity pActivity, LimitedFPSEngine pEngine, Camera camera, float pCameraWidth, float pCameraHeight) {
		getInstance().activity = pActivity;
		getInstance().engine = pEngine;
		getInstance().camera = camera;
		getInstance().cameraWidth = pCameraWidth;
		getInstance().cameraHeight = pCameraHeight;
		
		//load splashscene first
		//getInstance().loadSplashScreenResources();
		
		getInstance().createDatabase();
	}
	
	/*
	 * 	loading resources
	 */
	
	private void createDatabase() {
		try {
			DBHelper dbHelper = new DBHelper(getInstance().activity);
			dbHelper.createDB();
		}
		catch (Exception ex) {
			System.out.println("Error create DB: " + ex.toString());
		}
	}
	
	public Font getGameFont(int size) {
		Font tmpFont = FontFactory.createFromAsset(ResourceManager.getActivity().getFontManager(), ResourceManager.getActivity().getTextureManager(), 
				256, 256, ResourceManager.getActivity().getAssets(),
			    "font/angrybirds-regular.ttf", size, true, Color.WHITE);
		return tmpFont;
	}	
	
	
	private void loadSounds() {

	}
}
