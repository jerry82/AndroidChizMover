package jstudio.chizmover.scene;

import jstudio.chizmover.managers.ResourceManager;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class TextSprite extends Sprite {

	private Font mFont; 
	
	public TextSprite(float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		// TODO Auto-generated constructor stub
	}
	
	public void create(String text, int size, int color) {
		//load font
		if (mFont == null) {
			mFont = ResourceManager.getInstance().getGameFont(size, color);
			mFont.load();
		}
		
		Text mainText = new Text(0, 0, 
				mFont, text, ResourceManager.getEngine().getVertexBufferObjectManager());
		
		this.attachChild(mainText);
		mainText.setPosition(this.getX(), this.getY()); 
	}

}
