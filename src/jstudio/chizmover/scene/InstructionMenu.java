package jstudio.chizmover.scene;

import jstudio.chizmover.managers.CurrentMenu;
import jstudio.chizmover.managers.ResourceManager;
import jstudio.chizmover.managers.SceneManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.util.Log;

public class InstructionMenu extends PauseMenu {

	private static final String TAG = "InstructionMenu";
	
	protected BitmapTextureAtlas mBgInstructionBMP;
	protected ITextureRegion mBgInstructionTR;
	
	public InstructionMenu(Camera camera) {
		super(camera);
		// TODO Auto-generated constructor stub
	}
	
	public void loadResource() {
		super.loadResource();
		
		//load extra
		mBgInstructionBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), 280, 120, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mBgInstructionTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBgInstructionBMP, ResourceManager.getActivity(), ResourceManager.InstructionImage, 0, 0);
		mBgInstructionBMP.load();
	}
	
	public void unLoad() {
		super.unLoad();
		
		mBgInstructionBMP.unload();
	}
	
	public void onLoad() {
		
		loadResource();
		
		final IMenuItem menuBgMI = new ScaleMenuItemDecorator(
				new SpriteMenuItem(-1, mBgInstructionTR, ResourceManager.getEngine().getVertexBufferObjectManager()), 
				1f , 1f);
	    this.addMenuItem(menuBgMI);
	    
	    this.buildAnimations();
	    this.setBackgroundEnabled(false);
	    
	    //set position
	    float width = this.getCamera().getWidth();
	    float height = this.getCamera().getHeight();
	    
	    menuBgMI.setPosition(width / 2, height / 2);
	    
	    this.setOnMenuItemClickListener(this);
	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		// TODO Auto-generated method stub
		if (SceneManager.getInstance().getCurrentMenu() == CurrentMenu.InstructionMenu) {
		    switch(pMenuItem.getID())
		    {
		        case ResourceManager.InstructionID:
		        	Log.i(TAG, "click ins truction banner");
		        	SceneManager.getInstance().hideInstructionMenu();
		        	return true;
		        default:
		        	//Log.i(TAG, "click outside item");
		            return false;
		    }
		}
		return false;
	}
	
}