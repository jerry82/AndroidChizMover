package jstudio.chizmover.scene;

import jstudio.chizmover.managers.ResourceManager;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;

public class EpisodeEntity extends Entity {
	
	public EpisodeEntity(float pX, float pY) {
		super(pX, pY);
		// TODO Auto-generated constructor stub
	}
	
	public void create(String pEpisodeNumber, String pBodyText, Font font1, Font font2, ITextureRegion lockTR, boolean pLock) {
		
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
