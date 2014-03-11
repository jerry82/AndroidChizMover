package jstudio.chizmover.scene;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;

public class EpisodeSprite extends Sprite {
	
	private String mEpisodeNumber;
	private String mBodyText;
	private boolean mLock;
	
	//internal resource
	private BitmapTextureAtlas mLockBMP;
	private ITextureRegion mLockTR;
	
	public EpisodeSprite(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			ISpriteVertexBufferObject pSpriteVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pSpriteVertexBufferObject);
		// TODO Auto-generated constructor stub
		
		//create lock item
		
	}
	
	public void create(String pEpisodeNumber, String pBodyText, boolean pLock) {
		this.mEpisodeNumber = pEpisodeNumber;
		this.mBodyText = pBodyText;
		this.mLock = pLock;
		
		
	}
	
	/*
	 * 	getters and setters
	 */
	

}
