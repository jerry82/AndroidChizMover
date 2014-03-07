package jstudio.chizmover.scene;

import java.util.List;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.util.Log;

import jstudio.chizmover.data.LevelDetailEntity;
import jstudio.chizmover.managers.GameManager;
import jstudio.chizmover.managers.ResourceManager;

public class InGameScreen extends ManagedScene {
	
	/*
	 *  variables
	 */
	private static final String TAG = "InGameScreen";
	private LevelDetailEntity mCurrentLevel; 
	private float mSpriteScaleFactor;
	private int mSpriteCurrentEdge;
	private List<String> mMazeChars; 
	
	/* 
	 * 	sprites
	 */
	//background
	protected BitmapTextureAtlas mWallBMP;
	protected ITextureRegion mWallTR;
	
	
	/*
	 * 	constructor
	 */
	public InGameScreen() {
		super("screen_480x800.png");
		
		mSpriteCurrentEdge = 40;
		//create resources
		mWallBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), mSpriteCurrentEdge, mSpriteCurrentEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mWallTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mWallBMP, ResourceManager.getActivity(), ResourceManager.WallImageName, 0, 0);
	
		LevelDetailEntity entity = GameManager.getInstance().getNextLevel(null);
		SetCurrentLevel(entity);
	}
	
	public void SetCurrentLevel(LevelDetailEntity entity) { 
		this.mCurrentLevel = entity;
		
		//calculate scale factore
		mMazeChars = GameManager.getInstance().getMazeChars(entity.getContent());
		if (mMazeChars == null) {
			Log.e(TAG, "Level content is null");
		}
		else {
			int width = mMazeChars.get(0).length();
			mSpriteScaleFactor = ResourceManager.getInstance().cameraWidth / (width * mSpriteCurrentEdge);
		}
	}
	
	public void createGameGUI() {
		if (mCurrentLevel == null || mMazeChars == null) {
			Log.e(TAG, "Current Level is NULL");
			return;
		}
		
		
		for (int row = 0; row < mMazeChars.size(); row++) {
			String line = mMazeChars.get(row);
			
			int revertR = mMazeChars.size() - row - 1;
			
			for (int col = 0; col < line.length(); col++) {
				char ch = line.charAt(col);
				
				Sprite tmpSprite = null; 
				switch (ch) {
					case '#':
						mWallBMP.load();
						tmpSprite = new Sprite(0, 0, mWallTR, ResourceManager.getEngine().getVertexBufferObjectManager());

						tmpSprite.setScale(mSpriteScaleFactor);
						
						float pX = (tmpSprite.getWidth() * 0.5f + tmpSprite.getWidth() * col) * mSpriteScaleFactor;
						float pY = (tmpSprite.getHeight() * 0.5f + tmpSprite.getHeight() * revertR) * mSpriteScaleFactor;
						
						
						tmpSprite.setPosition(pX, pY);
						
						this.attachChild(tmpSprite);
						
						break;
				}

			}
		}
		
	}
	
	public void createMenuGUI() {
		
	}
	
	@Override
	public void onLoadScene() {
		// TODO Auto-generated method stub
		super.onLoadScene();
		
		//build gui
		createGameGUI();
		
		createMenuGUI();
		
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
