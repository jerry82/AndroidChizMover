package jstudio.chizmover.scene;

import java.util.ArrayList;
import java.util.List;

import jstudio.chizmover.data.PackEntity;
import jstudio.chizmover.managers.GameManager;
import jstudio.chizmover.managers.ResourceManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveByModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.modifier.IModifier;

import android.graphics.Color;
import android.util.Log;

public class EpisodeScreen extends ManagedScene implements IScrollDetectorListener, IOnSceneTouchListener,
	IClickDetectorListener {
	
	private static final String TAG = "EpisodeScreen";
	private float itemHeight; 
	private Font mFont;
	
	private Font mFont1;
	private Font mFont2;
	
	//internal resource
	private BitmapTextureAtlas mLockBMP;
	private ITextureRegion mLockTR;
	
	private Text mTitle; 
	private SurfaceScrollDetector mScrollDetector = null;
	private ClickDetector mClickDetector = null;
	private Rectangle mClickBar;
	
	private float mMinY;
	private float mMaxY;
	
	private List<PackEntity> mAllEpisodes;
	
	public EpisodeScreen() {
		super(ResourceManager.EpisodeScreenImage);
		
		itemHeight = ResourceManager.getInstance().cameraHeight / 7;
	}

	@Override
	public void onLoadScene() {
		// TODO Auto-generated method stub
		super.onLoadScene();
		
		createResource();
		
		createTitle();
		
		createEpisodeText();
		
		this.mScrollDetector = new SurfaceScrollDetector(this);
		this.mClickDetector = new ClickDetector(this);
		
		this.setOnSceneTouchListener(this);
		this.setTouchAreaBindingOnActionDownEnabled(true);
		this.setTouchAreaBindingOnActionMoveEnabled(true);            
		this.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
	}

	@Override
	public void onUnloadScene() {
		// TODO Auto-generated method stub
		super.onUnloadScene();
		
		ResourceManager.getActivity().runOnUpdateThread(new Runnable(){
			@Override
			public void run() {
				mFont.unload();
				mFont1.unload();
				mFont2.unload();
				mLockBMP.unload();
				
				detachChildren();
			}
		});

		
		//scroll camera to default pos
		Camera camera = ResourceManager.getInstance().camera;
		camera.setCenter(camera.getWidth() / 2, camera.getHeight() / 2);
	}

	@Override
	public void onShowScene() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHideScene() {
		// TODO Auto-generated method stub
	}
	
	/*
	 * 	main methods
	 */
	private void createTitle() {
		if (mFont == null) {
			mFont = ResourceManager.getInstance().getGameFont(45, Color.WHITE);
			mFont.load();
		}
		
		mTitle = new Text(0, 0, 
				mFont, "SELECT ADVENTURE", ResourceManager.getEngine().getVertexBufferObjectManager());	
		mTitle.setPosition(ResourceManager.getInstance().cameraWidth / 2, 
							ResourceManager.getInstance().cameraHeight - itemHeight / 2);
		this.attachChild(mTitle);
	}
	
	private void createResource() {
		//create parallax background
		ParallaxBackground background = new ParallaxBackground(0, 0, 0);
	    background.attachParallaxEntity(new ParallaxEntity(0, mBackgroundSprite));
	    this.setBackground(background);
	    
		if (mFont1 == null) {
			mFont1 = ResourceManager.getInstance().getGameFont(45, android.graphics.Color.WHITE);
			mFont1.load();
		}
		
		//load font
		if (mFont2 == null) {
			mFont2 = ResourceManager.getInstance().getGameFont(30, android.graphics.Color.WHITE);
			mFont2.load();
		}
		
		//create lock item
		mLockBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mLockTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLockBMP, ResourceManager.getActivity(), ResourceManager.LockImage, 0, 0);
		mLockBMP.load();
	}
	
	private void createEpisodeText() {
		//get list of episode from dB
		mAllEpisodes = GameManager.getInstance().getAllEpisodes();
		
		if (mAllEpisodes.size() == 0) return;
		float height = ResourceManager.getInstance().cameraHeight;
		
		for (PackEntity entity : mAllEpisodes) {
			String id = Integer.toString(entity.getId());
			String content = String.format("Completed: %1$d/%2$d", 
					entity.getCurrentLevel() - 1, entity.getNumberOfLevel());
			
			EpisodeEntity sprite = new EpisodeEntity(0, 0) ;
			
			sprite.create(id, content, mFont1, mFont2, mLockTR, entity.getLock());
			sprite.setPosition(0, height - entity.getId() * itemHeight - itemHeight/2);
			this.attachChild(sprite);
			//this.registerTouchArea(sprite);
		}
		
		//set scroll camera minY, maxY
		mMaxY = ResourceManager.getInstance().cameraHeight / 2 + 2;
		float delta = (mAllEpisodes.size() + 1) * itemHeight - ResourceManager.getInstance().cameraHeight/2;
		mMinY = (float) Math.ceil(mMaxY - delta / 2 - 100);
	}
	
	private void handleLevelClick(final int levelId) {
		
		if (mAllEpisodes == null) 
			return;
		
		if (levelId <= 0 || levelId > mAllEpisodes.size())
			return;
		
		int idx = levelId - 1;		
		final PackEntity pack = mAllEpisodes.get(idx);
		
		if (pack == null) 
			return;

		//take out comment in production
		
		if (pack.getId() > 1 && pack.getLock() == true)
			return;
		
		float pY = ResourceManager.getInstance().cameraHeight - levelId * itemHeight - itemHeight/2;
				
		mClickBar = new Rectangle(ResourceManager.getInstance().cameraWidth / 2, pY, 
				ResourceManager.getInstance().cameraWidth, 
				itemHeight,
				ResourceManager.getEngine().getVertexBufferObjectManager());
		mClickBar.setColor(1f, 1f, 1f, 0.2f);
		
		DelayModifier delay = new DelayModifier(0.3f, new IEntityModifierListener() {
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				// TODO Auto-generated method stub
				ResourceManager.getEngine().runOnUpdateThread(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						removeClickBar();
						loadLevel(pack);
					}
				});
			}
		});
		
		mClickBar.registerEntityModifier(delay);
		this.attachChild(mClickBar);	
	}
	
	private void removeClickBar() {
		mClickBar.clearEntityModifiers();
		this.detachChild(mClickBar);
	}
	
	private void loadLevel(PackEntity pack) {
		GameManager.getInstance().loadLevel(pack);
	}

	/*
	 * (non-Javadoc)
	 * @see org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener#onScrollStarted(org.andengine.input.touch.detector.ScrollDetector, int, float, float)
	 */
	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		// TODO Auto-generated method stub
		//moveAllSprites(pDistanceY);
		Camera camera = ResourceManager.getEngine().getCamera();
		
		pDistanceY *= 1.5f;
		
		if (camera.getCenterY() >= mMinY && camera.getCenterY() <= mMaxY ) {
			if (pDistanceY < 0) {
				if (pDistanceY + camera.getCenterY() < mMinY) {
					pDistanceY = (mMinY - camera.getCenterY());
				}
				camera.offsetCenter(0, pDistanceY);
			}
			else {
				if (pDistanceY + camera.getCenterY() > mMaxY) {
					pDistanceY = mMaxY - camera.getCenterY();
				}
				camera.offsetCenter(0, pDistanceY);
			}
		}
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		//Log.i(TAG, "on scene touched");
		mScrollDetector.onManagedTouchEvent(pSceneTouchEvent);
		mClickDetector.onManagedTouchEvent(pSceneTouchEvent);
		return false;
	}

	@Override
	public void onClick(ClickDetector pClickDetector, int pPointerID,
			float pSceneX, float pSceneY) {
		
		//get episode item from screen
		float dFromTopScreen = ResourceManager.getInstance().cameraHeight - pSceneY;
		int levelId = (int)Math.round((dFromTopScreen - itemHeight/2) / itemHeight);
		Log.i(TAG, "you seleted: " + levelId);
		
		handleLevelClick(levelId);
	}
}
