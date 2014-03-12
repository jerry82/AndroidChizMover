package jstudio.chizmover.scene;

import jstudio.chizmover.managers.ResourceManager;

import org.andengine.entity.Entity;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.util.adt.color.Color;

public class EpisodeEntity extends Entity {
	
	private String mEpisodeNumber;
	private String mBodyText;
	private boolean mLock;
	
	public EpisodeEntity(float pX, float pY) {
		super(pX, pY);
		// TODO Auto-generated constructor stub
	}
	
	public void create(String pEpisodeNumber, String pBodyText, Font font1, Font font2, ITextureRegion lockTR, boolean pLock) {
		this.mEpisodeNumber = pEpisodeNumber;
		this.mBodyText = pBodyText;
		this.mLock = pLock;
		
		Text episodeNumberText = new Text(0, 0, 
				font1, pEpisodeNumber, ResourceManager.getEngine().getVertexBufferObjectManager());
		episodeNumberText.setPosition(ResourceManager.getInstance().cameraWidth / 8, 0); 
		this.attachChild(episodeNumberText);
		
		Text bodyText = new Text(0, 0, 
				font2, pBodyText, ResourceManager.getEngine().getVertexBufferObjectManager());
		bodyText.setPosition(ResourceManager.getInstance().cameraWidth / 2, 0);
		this.attachChild(bodyText);
		 
		if (pLock) {
			Sprite lockSprite = new Sprite(0, 0, lockTR, ResourceManager.getEngine().getVertexBufferObjectManager());
			lockSprite.setPosition(ResourceManager.getInstance().cameraWidth * 7 / 8, 0);
			this.attachChild(lockSprite);
		}
	}
	
	/*
	 * 	getters and setters
	 */
	

}
