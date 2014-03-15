package jstudio.chizmover.managers;

import java.io.IOException;

import jstudio.chizmover.runtime.MainActivity;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.util.Log;

import jstudio.chizmover.data.*;
public class ResourceManager {

	private static final String TAG = "ResourceManager";
	//constants
	private static final ResourceManager INSTANCE = new ResourceManager();
	
	
	//variables
	public MainActivity activity;
	public LimitedFPSEngine engine;
	public Camera camera;
	
	public Music gameSound;
	
	public float cameraWidth;
	public float cameraHeight;
	
	//variables - game resources
	public static final String LeaderboardID = "CgkIsPfzg-UVEAIQAA";
	public static final String SplashScreenImage = "SplashScreen_480x800.png";
	public static final String EpisodeScreenImage = "episodeScreen_480x800.png";
	public static final String GameBackgroundImage = "screen_480x800.png";
	public static final String EndGameScreenImage = "wingame_screen_480x800.png";
	
	public static final String WallImageName = "block40.png";
	public static final String BotImageName = "bot40.png";
	public static final String BoxImageName = "box40.png";
	public static final String TargetImageName = "spot40.png";
	public static final String CanmoveImage = "canMove40.png";
	public static final String CannotMoveImage = "cannotMove40.png";
	public static final String LockImage = "lock.png";
	public static final String TransparentBar = "bar.png";
	
	public static final String PauseImageName = "pause50.png";
	public static final int PauseID = 0;
	
	public static final String PrevImageName = "back_btn50.png";
	public static final int PrevID = 1;
	
	public static final String NextImageName = "next_btn50.png";
	public static final int NextID = 2;
	
	public static final String MenuBgImage = "menu.png";
	public static final String WinLevelBgImage = "popup.png";
	public static final String WinEpisodeBgImage = "popup_adv_completed.png";
	
	public static final String InstructionImage = "popup_instruction.png";
	public static final int InstructionID = -1;
	
	public static final String MenuImage = "menu_btn50.png";
	public static final int MenuID = 3;
	
	public static final String RestartImage = "restart_btn50.png";
	public static final int RestartID = 4;
	
	public static final String RadioOnImage = "radioon_btn50.png";
	public static final int RadioOnID = 5;
	
	public static final String RadioOffImage = "radiooff_btn50.png";
	public static final int RadioOffID = 6;
	
	public static final String HelpImage = "help50.png";
	public static final int HelpID = 7;
	
	
	public static final int FixSizeSpriteEdge = 40;
	public static final int FixSizeBtnEdge = 50;
	
	public static Font GameFont;
	
	/*
	 * 	game resources
	 */
	
	public static ITextureRegion WallTR;
	public static ITextureRegion BotTR;
	public static ITextureRegion BoxTR;
	public static ITextureRegion TargetTR;
	public static ITextureRegion CanMoveTR;
	public static ITextureRegion CanNOTMoveTR;
	
	public static ITextureRegion PauseTR;
	public static ITextureRegion NextTR;
	public static ITextureRegion PrevTR;
	
	
	public static ITextureRegion MenuBgTR;
	public static ITextureRegion MenuTR;
	public static ITextureRegion RestartTR;
	public static ITextureRegion RadioOnTR;
	public static ITextureRegion HelpTR;
	//public static ITextureRegion mNextTR;
	
	
	public static ITextureRegion BgLevelWinTR;
	
	
	public static ITextureRegion BgEpisodeWinTR;
	
	
	
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
	
	public static void createGameResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		 
		getInstance().loadInGameResources();
		getInstance().loadMenuResources();
	}
	
	public static void setup (MainActivity pActivity, LimitedFPSEngine pEngine, Camera camera, float pCameraWidth, float pCameraHeight) {
		getInstance().activity = pActivity;
		getInstance().engine = pEngine;
		getInstance().camera = camera;
		getInstance().cameraWidth = pCameraWidth;
		getInstance().cameraHeight = pCameraHeight;
				
		createGameResources();
		
		getInstance().createDatabase();
		getInstance().loadSounds();
	}
	
	/*
	 * 	loading resources
	 */
	private void loadInGameResources() {
		BitmapTextureAtlas mWallBMP;
		BitmapTextureAtlas mBotBMP;
		BitmapTextureAtlas mBoxBMP;
		BitmapTextureAtlas mTargetBMP;
		BitmapTextureAtlas mCanMoveBMP;
		BitmapTextureAtlas mCanNOTMoveBMP;
		//create resources
		int tmpEdge = ResourceManager.FixSizeSpriteEdge;
		
		mWallBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), tmpEdge, tmpEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		WallTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mWallBMP, ResourceManager.getActivity(), ResourceManager.WallImageName, 0, 0);
	
		mBotBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), tmpEdge, tmpEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		BotTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBotBMP, ResourceManager.getActivity(), ResourceManager.BotImageName, 0, 0);
		
		mBoxBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), tmpEdge, tmpEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		BoxTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBoxBMP, ResourceManager.getActivity(), ResourceManager.BoxImageName, 0, 0);
		
		mTargetBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), tmpEdge, tmpEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TargetTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mTargetBMP, ResourceManager.getActivity(), ResourceManager.TargetImageName, 0, 0);
		
		mCanMoveBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), tmpEdge, tmpEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		CanMoveTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCanMoveBMP, ResourceManager.getActivity(), ResourceManager.CanmoveImage, 0, 0);
		
		mCanNOTMoveBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), tmpEdge, tmpEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		CanNOTMoveTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCanNOTMoveBMP, ResourceManager.getActivity(), ResourceManager.CannotMoveImage, 0, 0);

		mWallBMP.load();
		mBotBMP.load();
		mBoxBMP.load();
		mTargetBMP.load();
		mCanMoveBMP.load();
		mCanNOTMoveBMP.load();

	}
	
	private void loadMenuResources() {
		BitmapTextureAtlas mPauseBMP;
		BitmapTextureAtlas mNextBMP;
		BitmapTextureAtlas mPrevBMP;
		
		mPauseBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		PauseTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mPauseBMP, ResourceManager.getActivity(), ResourceManager.PauseImageName, 0, 0);
		
		mNextBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		NextTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mNextBMP, ResourceManager.getActivity(), ResourceManager.NextImageName, 0, 0);
		
		mPrevBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		PrevTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mPrevBMP, ResourceManager.getActivity(), ResourceManager.PrevImageName, 0, 0);
	
		mPauseBMP.load();
		mNextBMP.load();
		mPrevBMP.load();
		
		BitmapTextureAtlas mMenuBgBMP;
		BitmapTextureAtlas mMenuBMP;
		BitmapTextureAtlas mRestartBMP;
		BitmapTextureAtlas mRadioOnBMP;
		BitmapTextureAtlas mHelpBMP;
		//BitmapTextureAtlas mNextBMP;
		
		mMenuBgBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), 280, 90, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		MenuBgTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMenuBgBMP, ResourceManager.getActivity(), ResourceManager.MenuBgImage, 0, 0);
		
		mMenuBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		MenuTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMenuBMP, ResourceManager.getActivity(), ResourceManager.MenuImage, 0, 0);
		
		mRestartBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		RestartTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mRestartBMP, ResourceManager.getActivity(), ResourceManager.RestartImage, 0, 0);
		
		mRadioOnBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		RadioOnTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mRadioOnBMP, ResourceManager.getActivity(), ResourceManager.RadioOnImage, 0, 0);
		
		mHelpBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		HelpTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mHelpBMP, ResourceManager.getActivity(), ResourceManager.HelpImage, 0, 0);
		
		mNextBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		mMenuBgBMP.load();
		mMenuBMP.load();
		mRestartBMP.load();
		mRadioOnBMP.load();
		mHelpBMP.load();
		
		BitmapTextureAtlas mBgLevelWinBMP;
		BitmapTextureAtlas mBgEpisodeWinBMP;
		
		mBgLevelWinBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), 280, 120, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		BgLevelWinTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBgLevelWinBMP, ResourceManager.getActivity(), ResourceManager.WinLevelBgImage, 0, 0);
		
		mBgEpisodeWinBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), 280, 120, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		BgEpisodeWinTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBgEpisodeWinBMP, ResourceManager.getActivity(), ResourceManager.WinEpisodeBgImage, 0, 0);
		
		mBgLevelWinBMP.load();
		mBgEpisodeWinBMP.load();
	}
	
	private void createDatabase() {
		try {
			DBHelper dbHelper = new DBHelper(getInstance().activity);
			dbHelper.createDB();
		}
		catch (Exception ex) {
			System.out.println("Error create DB: " + ex.toString());
		}
	}
	
	public Font getGameFont(int size, int color) {
		Font tmpFont = FontFactory.createFromAsset(ResourceManager.getActivity().getFontManager(), ResourceManager.getActivity().getTextureManager(), 
				256, 256, ResourceManager.getActivity().getAssets(),
			    "font/angrybirds-regular.ttf", size, true, color);
		tmpFont.load();
		return tmpFont;
	}
	
	private void loadSounds() {
		MusicFactory.setAssetBasePath("sounds/");
		try {
			gameSound = MusicFactory.createMusicFromAsset(getEngine().getMusicManager(), getActivity(), "ingame.mp3");
			gameSound.setLooping(true);
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
