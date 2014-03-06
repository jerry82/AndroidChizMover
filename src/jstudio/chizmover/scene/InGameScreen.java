package jstudio.chizmover.scene;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import jstudio.chizmover.managers.ResourceManager;

public class InGameScreen extends ManagedScene {

	private float mWidth;
	private float mHeight;
	
	//background
	private BitmapTextureAtlas mBackgroundTextureAtlas;
	private ITextureRegion mBackgroundTextureRegion;
	private Sprite mBackgroundSprite;
	
	
	
	/*
	 * 	constructor
	 */
	public InGameScreen() {
		mWidth = ResourceManager.getInstance().cameraWidth;
		mHeight = ResourceManager.getInstance().cameraHeight;
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mBackgroundTextureAtlas = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), 380, 568, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTextureAtlas, ResourceManager.getActivity(), "screen.png", 0, 0);
	}
	
	@Override
	public void onLoadScene() {
		// TODO Auto-generated method stub

		//background
		mBackgroundTextureAtlas.load();
		mBackgroundSprite = new Sprite(0, 0, mBackgroundTextureRegion, ResourceManager.getEngine().getVertexBufferObjectManager());
		mBackgroundSprite.setWidth(mWidth);
		mBackgroundSprite.setHeight(mHeight);
		mBackgroundSprite.setPosition(mWidth * 0.5f, mHeight * 0.5f);
		this.attachChild(mBackgroundSprite);
		
		//bot
		
		//wall
	}

	@Override
	public void onUnloadScene() {
		// TODO Auto-generated method stub

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
