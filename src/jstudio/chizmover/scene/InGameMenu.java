package jstudio.chizmover.scene;

import jstudio.chizmover.managers.CurrentMenu;
import jstudio.chizmover.managers.GameManager;
import jstudio.chizmover.managers.ResourceManager;
import jstudio.chizmover.managers.SceneManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.util.Log;

public class InGameMenu extends MenuScene implements IOnMenuItemClickListener {
	
	private final static String TAG = "InGameMenu";

	private BitmapTextureAtlas mPauseBMP;
	private ITextureRegion mPauseTR;
	
	private BitmapTextureAtlas mNextBMP;
	private ITextureRegion mNextTR;
	
	private BitmapTextureAtlas mPrevBMP;
	private ITextureRegion mPrevTR;
	
	private boolean mShowPrev = false;
	private boolean mShowNext = false;
	
	public InGameMenu(Camera pCamera, boolean showPrev, boolean showNext) {
		super(pCamera);
		// TODO Auto-generated constructor stub
		this.mShowPrev = showPrev;
		this.mShowNext = showNext;
		
		onLoad();
	}
	
	//load resource
	private void onLoad() {
		mPauseBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mPauseTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mPauseBMP, ResourceManager.getActivity(), ResourceManager.PauseImageName, 0, 0);
		
		mNextBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mNextTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mNextBMP, ResourceManager.getActivity(), ResourceManager.NextImageName, 0, 0);
		
		mPrevBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), ResourceManager.FixSizeBtnEdge, ResourceManager.FixSizeBtnEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mPrevTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mPrevBMP, ResourceManager.getActivity(), ResourceManager.PrevImageName, 0, 0);
	
		mPauseBMP.load();
		mNextBMP.load();
		mPrevBMP.load();
		
		float pad = 10;
		
		final IMenuItem pauseMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.PauseID, mPauseTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
		
		final IMenuItem nextMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.NextID, mNextTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
	 
		final IMenuItem prevMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(ResourceManager.PrevID, mPrevTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1.2f , 1f);
		
		this.addMenuItem(pauseMI);
		
		if (mShowPrev)
			this.addMenuItem(prevMI);
		
		if (mShowNext)
			this.addMenuItem(nextMI);

		this.buildAnimations();
		
		//only can set after build animations();
		pauseMI.setPosition(pauseMI.getWidth() / 2, ResourceManager.getInstance().cameraHeight - pad - pauseMI.getHeight() / 2);
		prevMI.setPosition(this.getCamera().getCameraSceneWidth() - prevMI.getWidth() / 2 - prevMI.getWidth(), pauseMI.getY());
		nextMI.setPosition(this.getCamera().getCameraSceneWidth() - nextMI.getWidth() / 2, pauseMI.getY());
		
	    this.setBackgroundEnabled(false);
	    
	    this.setOnMenuItemClickListener(this);
	}
	
	public void unLoad() {
		mPauseBMP.unload();
		mNextBMP.unload();
		mPrevBMP.unload();
		
		
		
		this.detachSelf();
	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
	{
		if (SceneManager.getInstance().getCurrentMenu() == CurrentMenu.NoMenu) {
			switch(pMenuItem.getID())
		    {
		        case ResourceManager.PauseID:
		        	Log.i(TAG, "click info");
		        	SceneManager.getInstance().handlePauseBtnClick();
		        	//GameManager.getInstance().handleGameWin();
		            return true;
		        case ResourceManager.PrevID:
		        	Log.i(TAG, "click prev");
		        	SceneManager.getInstance().handlePrevBtnClick();
		            //action
		            return true;
		        case ResourceManager.NextID:
		        	Log.i(TAG, "click next");
		        	SceneManager.getInstance().handleNextBtnClick();
		        	return true;
		        default:
		            return false;
		    }
		}
		return false;
	}
}
