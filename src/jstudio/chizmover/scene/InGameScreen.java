package jstudio.chizmover.scene;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseLinear;

import android.util.Log;

import jstudio.chizmover.data.LevelDetailEntity;
import jstudio.chizmover.managers.*;

public class InGameScreen extends ManagedScene implements IOnSceneTouchListener {
	
	/*
	 *  variables
	 */
	private static final String TAG = "InGameScreen";
	private LevelDetailEntity mCurrentLevel; 
	private float mSpriteScaleFactor;
	private int mSpriteCurrentEdge;
	private List<String> mMazeChars; 
	
	
	private Sprite botSprite; 
	private Sprite canMoveSprite;
	private Sprite cannotMoveSprite;
	
	private List<Sprite> boxSprites;
	private boolean mBotIsMoving = false;
	private StringBuilder botFlipString = new StringBuilder();
	
	//store current in game menu child
	private MenuScene mMenu = null;
	
	/*
	 * 	sprites
	 */
	//background
	protected BitmapTextureAtlas mWallBMP;
	protected ITextureRegion mWallTR;
	
	protected BitmapTextureAtlas mBotBMP;
	protected ITextureRegion mBotTR;
	
	protected BitmapTextureAtlas mBoxBMP;
	protected ITextureRegion mBoxTR;
	
	protected BitmapTextureAtlas mTargetBMP;
	protected ITextureRegion mTargetTR;
	
	protected BitmapTextureAtlas mCanMoveBMP;
	protected ITextureRegion mCanMoveTR;
	
	protected BitmapTextureAtlas mCanNOTMoveBMP;
	protected ITextureRegion mCanNOTMoveTR;
	

	/*
	 * 	constructor
	 */
	public InGameScreen() {
		//decorate background
		super(ResourceManager.GameBackgroundImage);
		
		mSpriteCurrentEdge = ResourceManager.FixSizeSpriteEdge;
		boxSprites = new ArrayList<Sprite>();
		
		createScreenResource();
	}
	
	private void createScreenResource() {
		//create resources
		mWallBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), mSpriteCurrentEdge, mSpriteCurrentEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mWallTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mWallBMP, ResourceManager.getActivity(), ResourceManager.WallImageName, 0, 0);
	
		mBotBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), mSpriteCurrentEdge, mSpriteCurrentEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mBotTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBotBMP, ResourceManager.getActivity(), ResourceManager.BotImageName, 0, 0);
		
		mBoxBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), mSpriteCurrentEdge, mSpriteCurrentEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mBoxTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBoxBMP, ResourceManager.getActivity(), ResourceManager.BoxImageName, 0, 0);
		
		mTargetBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), mSpriteCurrentEdge, mSpriteCurrentEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTargetTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mTargetBMP, ResourceManager.getActivity(), ResourceManager.TargetImageName, 0, 0);
		
		mCanMoveBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), mSpriteCurrentEdge, mSpriteCurrentEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mCanMoveTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCanMoveBMP, ResourceManager.getActivity(), ResourceManager.CanmoveImage, 0, 0);
		
		mCanNOTMoveBMP = new BitmapTextureAtlas(ResourceManager.getActivity().getTextureManager(), mSpriteCurrentEdge, mSpriteCurrentEdge, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mCanNOTMoveTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCanNOTMoveBMP, ResourceManager.getActivity(), ResourceManager.CannotMoveImage, 0, 0);
	
		mWallBMP.load();
		mBotBMP.load();
		mBoxBMP.load();
		mTargetBMP.load();
		mCanMoveBMP.load();
		mCanNOTMoveBMP.load();
	}
	
	/*
	 * 	getters and setters
	 */
	public void setMenu(MenuScene pMenu) {
		this.mMenu = pMenu;
	}
	
	public MenuScene getMenu() {
		return this.mMenu;
	}
	//end
	
	public void setCurrentLevel(LevelDetailEntity entity) { 
		this.mCurrentLevel = entity;
		if (entity == null) 
			return;
		
		//calculate scale factor
		mMazeChars = GameManager.getInstance().getMazeChars(entity.getContent());
		if (mMazeChars == null) {
			Log.e(TAG, "Level content is null");
		}
		else {
			int width = GameManager.getInstance().getMazeWidth(mMazeChars);
			mSpriteScaleFactor = ResourceManager.getInstance().cameraWidth / (width * mSpriteCurrentEdge);
			
			//update resize currentEdge
			mSpriteCurrentEdge *= mSpriteScaleFactor;
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
				Sprite tmpAnotherSprite = null;
				
				switch (ch) {
					case GameManager.LEVEL_WALL_CHAR:
						//mWallBMP.load();
						tmpSprite = new Sprite(0, 0, mWallTR, ResourceManager.getEngine().getVertexBufferObjectManager());
						break;
						
					case GameManager.LEVEL_BOT_CHAR:
						//mBotBMP.load();
						botSprite = new Sprite(0, 0, mBotTR, ResourceManager.getEngine().getVertexBufferObjectManager());
						botSprite.setZIndex(2);
						addSpriteToScene(botSprite, col, revertR);
						
						break;
						
					case GameManager.LEVEL_BOX_CHAR:
						//mBoxBMP.load();
						tmpSprite = new Sprite(0, 0, mBoxTR, ResourceManager.getEngine().getVertexBufferObjectManager());
						tmpSprite.setZIndex(2);
						boxSprites.add(tmpSprite);
						break;
					
					case GameManager.LEVEL_TARGET:
						mTargetBMP.load();
						tmpSprite = new Sprite(0,  0,  mTargetTR, ResourceManager.getEngine().getVertexBufferObjectManager());
						tmpSprite.setZIndex(1);
						break;
						
					case GameManager.LEVEL_BOX_ON_TARGET:
						//mBoxBMP.load();
						mTargetBMP.load();
						
						tmpSprite = new Sprite(0,  0,  mTargetTR, ResourceManager.getEngine().getVertexBufferObjectManager());
						tmpSprite.setZIndex(1);
						
						tmpAnotherSprite = new Sprite(0,  0,  mBoxTR, ResourceManager.getEngine().getVertexBufferObjectManager());
						tmpAnotherSprite.setZIndex(2);
						
						boxSprites.add(tmpAnotherSprite);
						addSpriteToScene(tmpAnotherSprite, col, revertR);
						break;
				}
				
				if (tmpSprite != null) {
					addSpriteToScene(tmpSprite, col, revertR);
				}
				
				//to be ontop
				/*
				if (tmpAnotherSprite != null) {
					addSpriteToScene(tmpAnotherSprite, col, revertR);
				}*/
			}
		}
		
		this.sortChildren();
		
		
		this.setOnSceneTouchListener(this);
	}
	
	private void addSpriteToScene(IEntity sprite, int col, int row) {
		sprite.setScale(mSpriteScaleFactor);
		float pX = (sprite.getWidth() * 0.5f + sprite.getWidth() * col) * mSpriteScaleFactor;
		float pY = (sprite.getHeight() * 0.5f + sprite.getHeight() * row) * mSpriteScaleFactor;
		sprite.setPosition(pX, pY);
		this.attachChild(sprite);
	}
	
	@Override
	public void onLoadScene() {
		// TODO Auto-generated method stub
		super.onLoadScene();
		
		//build gui
		setCurrentLevel(GameManager.getInstance().getCurrentLevel());
		
		createGameGUI();
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
	
	/*
	 * 	game helpers
	 */
	private void displayBotPosition() {
		String display = String.format("bot position: %1$f%2$f", botSprite.getX(), botSprite.getY());
		Log.i(TAG, display);
	}
	
	private void displayMazeChars() {
		for (String str: mMazeChars) {
			Log.i(TAG, str);
		}
	}
	
	private Sprite getTouchBox(int[] touchPos) {
		for (Sprite box : boxSprites) {
			float boxAtX = box.getX();
			float boxAtY = box.getY();
			
			int[] boxPos = getMatrixPos(new float[] {boxAtX, boxAtY});
			
			if (boxPos[0] == touchPos[0] && boxPos[1] == touchPos[1]) {
				return box;
			}
		}
		
		return null;
	}
	
	private void handleTouchOnBox(Sprite box) {
		if (box == null) return;
		
		char moveChar = 0;
		
		int[] boxPos = getMatrixPos(new float[] {box.getX(), box.getY()});
		int[] botSpritePos = getMatrixPos(new float[] {botSprite.getX(), botSprite.getY()});
		
		if (botSpritePos[0] == boxPos[0]) {
			//both move left
			if (botSpritePos[1] - boxPos[1] == 1) {
				if (!boxHitWall(new int[] {boxPos[0], boxPos[1] - 1})) {
					moveChar = GameManager.BOT_MOVE_LEFT;
				}
			}
			else if (botSpritePos[1] - boxPos[1] == -1) {
				if (!boxHitWall(new int[] {boxPos[0], boxPos[1] + 1})) {
					moveChar = GameManager.BOT_MOVE_RIGHT;
				}
			}
		}
		else if (botSpritePos[1] == boxPos[1]) {
			if (botSpritePos[0] - boxPos[0] == 1) {
				if (!boxHitWall(new int[] {boxPos[0] - 1, boxPos[1]})) {
					moveChar = GameManager.BOT_MOVE_UP;
				}
			}
			else if (botSpritePos[0] - boxPos[0] == -1) {
				if (!boxHitWall(new int[] {boxPos[0] + 1, boxPos[1]})) {
					moveChar = GameManager.BOT_MOVE_DOWN;
				}
			}
		}
		
		if (moveChar != 0) {
			moveTheBox(box, moveChar);
		}
	}
	
	private boolean boxHitWall(int[] nextPos) {
		int row = nextPos[0];
		int col = nextPos[1];
		
		return ((mMazeChars.get(row).charAt(col) == GameManager.LEVEL_WALL_CHAR) || 
				(mMazeChars.get(row).charAt(col) == GameManager.LEVEL_BOX_CHAR) || 
				(mMazeChars.get(row).charAt(col) == GameManager.LEVEL_BOX_ON_TARGET));
	}
	
	private int[] getMatrixPos(float[] scenePos) {
		int[] result = new int[2];
		
		result[1] = (int) (scenePos[0] / this.mSpriteCurrentEdge);
		result[0] = (int) Math.floor(scenePos[1] / this.mSpriteCurrentEdge);
		
		result[0] = mMazeChars.size() - result[0] - 1;
		
		return result;
	}
	
	//idx0: row, idx1: col
	
	private float[] getScenePos(int[] matrixPos) {
		float[] result = new float[2];
		
		result[0] = matrixPos[1] * this.mSpriteCurrentEdge + this.mSpriteCurrentEdge / 2;
		result[1] = (this.mMazeChars.size() - matrixPos[0]) * this.mSpriteCurrentEdge - 
				this.mSpriteCurrentEdge / 2;
		
		return result;
	}

	/*
	 * handle touch
	 * @see org.andengine.entity.scene.IOnSceneTouchListener#onSceneTouchEvent(org.andengine.entity.scene.Scene, org.andengine.input.touch.TouchEvent)
	 */
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			//if there is popup menu return
			Log.i(TAG, "on scene touch");
			
			if (SceneManager.getInstance().getCurrentMenu() == CurrentMenu.PauseMenu) {
				SceneManager.getInstance().hidePauseMenu();
				return false;
			}
			
			if (mBotIsMoving)  {
				Log.i(TAG, "touch: bot is moving");
				return false;
			}
			
	        final float touchX = pSceneTouchEvent.getX();
	        final float touchY = pSceneTouchEvent.getY();
	        
	        final float botX = botSprite.getX();
	        final float botY = botSprite.getY();

	        int[] touchPos = getMatrixPos(new float[]{ touchX, touchY});
	        int[] botPos = getMatrixPos(new float[] {botX, botY});
	        	        
	        Log.i(TAG, "row: " + touchPos[0]);
	        Log.i(TAG, "col: " + touchPos[1]);
	        
	        Sprite box = getTouchBox(touchPos);
	        if (box != null) {
	        	handleTouchOnBox(box);
	        }
	        else {
		        GameManager.getInstance().initMaze(mMazeChars);
		        String pathString = GameManager.getInstance().getShortestPath(touchPos, botPos);
		        
		        Log.i(TAG, "move: " + pathString);
		        moveBot(pathString, new float[] {botX, botY});
		        if (pathString == null || pathString.length() == 0) {
		        	showMoveSprite(false, touchPos[1], mMazeChars.size() - 1 - touchPos[0]);
		        }
		        else if (pathString != GameManager.PATH_OUTBOUND) {
		        	showMoveSprite(true, touchPos[1], mMazeChars.size() - 1 - touchPos[0]);
		        }
	        }
	        
	        return true;
	    }
	    return false;
	}
	
	private void updateMazeWithNewBoxPos(int[] prevPos, int[] curPos){
		
		Log.i(TAG, String.format("prev: %1$d-%2$d", prevPos[0], prevPos[1]));
		Log.i(TAG, String.format("cur: %1$d-%2$d", curPos[0], curPos[1]));
		
		char prevChar = mMazeChars.get(prevPos[0]).charAt(prevPos[1]);
		char curChar = mMazeChars.get(curPos[0]).charAt(curPos[1]);
		
		if (prevChar == GameManager.LEVEL_BOX_ON_TARGET) 
			prevChar = GameManager.LEVEL_TARGET;
		else 
			prevChar = GameManager.LEVEL_SPACE;

		if (curChar == GameManager.LEVEL_TARGET) 
			curChar = GameManager.LEVEL_BOX_ON_TARGET;
		else 
			curChar = GameManager.LEVEL_BOX_CHAR;
		
		//replace mutable string
		updateMazeChars(prevPos, prevChar);
		updateMazeChars(curPos, curChar);
		//displayMazeChars();
		
	}
	
	private void updateMazeChars(int[] pos, char newChar) {
		String tmp = mMazeChars.get(pos[0]);
		StringBuilder tmpBuilder = new StringBuilder(tmp);
		tmpBuilder.setCharAt(pos[1], newChar);
		mMazeChars.set(pos[0], tmpBuilder.toString());
	}
	
	private void moveTheBox(final Sprite box, final char moveChar) {
		
		float dx = 0f, dy = 0f;
		
		final int[] prevPos = getMatrixPos(new float[] {box.getX(), box.getY()});
		
		switch (moveChar) {
			case GameManager.BOT_MOVE_LEFT:
				dx = -1 * mSpriteCurrentEdge;
				break;
			case GameManager.BOT_MOVE_RIGHT:
				dx = mSpriteCurrentEdge;
				break;
			case GameManager.BOT_MOVE_UP:
				dy = mSpriteCurrentEdge;
				break;
			case GameManager.BOT_MOVE_DOWN:
				dy = -1 * mSpriteCurrentEdge;
				break;
		}
		
		botSprite.clearEntityModifiers();
		MoveModifier moveForBot = new MoveModifier(GameManager.MOVE_UNIT_DURATION, 
				botSprite.getX(), botSprite.getY(), botSprite.getX() + dx, botSprite.getY() + dy, 
				new IEntityModifierListener() {
					
					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
						// TODO Auto-generated method stub
						switch (moveChar) {
						case GameManager.BOT_MOVE_LEFT:
							botSprite.setFlippedHorizontal(true);
							break;
						case GameManager.BOT_MOVE_RIGHT:
							botSprite.setFlippedHorizontal(false);
							break;
						}
						mBotIsMoving = true;
					}
					
					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						// TODO Auto-generated method stub
						mBotIsMoving = false;
						/*
						ResourceManager.getEngine().runOnUpdateThread(new Runnable(){
							@Override
							public void run() {
								botSprite.clearEntityModifiers();
							}
						});*/
					}
				});
		botSprite.registerEntityModifier(moveForBot);
		
		box.clearEntityModifiers();
		MoveModifier moveForTheBox = new MoveModifier(GameManager.MOVE_UNIT_DURATION, 
				box.getX(), box.getY(), box.getX() + dx, box.getY() + dy, 
				new IEntityModifierListener() {
					
					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						// TODO Auto-generated method stub
						int[] curPos = getMatrixPos(new float[] {pItem.getX(), pItem.getY()});
						updateMazeWithNewBoxPos(prevPos, curPos);
						
						if (GameManager.getInstance().checkGameWin(mMazeChars)) {
							handleGameWin();
						}
						/*
						ResourceManager.getEngine().runOnUpdateThread(new Runnable(){
							@Override
							public void run() {
								box.clearEntityModifiers();
							}
						});*/
					}
				});
		box.registerEntityModifier(moveForTheBox);
	}
	
	private void handleGameWin() {
		Log.i(TAG, "game is won");
	}
	
	private boolean moveBot(String pathString, float[] botScenePos) {
		if (pathString == null || pathString.length() == 0 || pathString == GameManager.PATH_OUTBOUND)
			return false;
		
		Path path = buildPath(pathString, botScenePos);
		
		clearBotEntityModifier();
		PathModifier move =  new PathModifier(pathString.length() * GameManager.MOVE_UNIT_DURATION, path, null, 
				new IPathModifierListener(){

					@Override
					public void onPathStarted(PathModifier pPathModifier,
							IEntity pEntity) {
			            mBotIsMoving = true;
			            Log.i(TAG, "bot runs");
					}

					@Override
					public void onPathWaypointStarted(
							PathModifier pPathModifier, IEntity pEntity,
							int pWaypointIndex) {
						
						char tmp = botFlipString.toString().charAt(pWaypointIndex);
						switch (tmp) {
						case GameManager.BOT_MOVE_LEFT:
							botSprite.setFlippedHorizontal(true);
							break;
						case GameManager.BOT_MOVE_RIGHT:
							botSprite.setFlippedHorizontal(false);
							break;
						}
					}

					@Override
					public void onPathWaypointFinished(
							PathModifier pPathModifier, IEntity pEntity,
							int pWaypointIndex) {
						
					}

					@Override
					public void onPathFinished(PathModifier pPathModifier,
							IEntity pEntity) {
			            mBotIsMoving = false;
			            Log.i(TAG, "bot stop");
					}
		}, EaseLinear.getInstance());

		botSprite.registerEntityModifier(move);
		
		return true;
	}
	
	private void clearBotEntityModifier() {
		if (botSprite.getEntityModifierCount() > 0)
			botSprite.clearEntityModifiers();
	}
	
	private Path buildPath(String pathString, float[] botScenePos) {
		//need at least 2 way points
		Path path = new Path(pathString.length() + 1).to(botScenePos[0], botScenePos[1]);
		botFlipString = new StringBuilder(pathString);
			
		for (char ch : pathString.toCharArray()) {
			switch (ch) {
				case GameManager.BOT_MOVE_LEFT:
					botScenePos[0] -= mSpriteCurrentEdge;
					break;
				case GameManager.BOT_MOVE_RIGHT:
					botScenePos[0] += mSpriteCurrentEdge;
					break;
				case GameManager.BOT_MOVE_UP:
					botScenePos[1] += mSpriteCurrentEdge;
					break;
				case GameManager.BOT_MOVE_DOWN:
					botScenePos[1] -= mSpriteCurrentEdge;
					break;
			}
			
			path.to(botScenePos[0], botScenePos[1]);
			Log.i(TAG, "move to: " + botScenePos[0] + " - " + botScenePos[1]);
		}
		return path;
	}
	
	private boolean showMoveSprite(boolean canMove, final int row, final int col) {
		Sprite sprite = null;
		
		if (canMove) {
			canMoveSprite = new Sprite(0, 0, mCanMoveTR, ResourceManager.getEngine().getVertexBufferObjectManager());
			canMoveSprite.setZIndex(2);
			addSpriteToScene(canMoveSprite, row, col);
			sprite = canMoveSprite;
		}
		else {
			cannotMoveSprite = new Sprite(0, 0, mCanNOTMoveTR, ResourceManager.getEngine().getVertexBufferObjectManager());
			cannotMoveSprite.setZIndex(2);
			addSpriteToScene(cannotMoveSprite, row, col);
			sprite = cannotMoveSprite;
		}
		
		sprite.clearEntityModifiers();
		DelayModifier delay = new DelayModifier(0.5f, new IEntityModifierListener() {
			
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				// TODO Auto-generated method stub
				//addSpriteToScene(pItem, row, col);
			}
			
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, final IEntity pItem) {
				ResourceManager.getEngine().runOnUpdateThread(new Runnable(){
					
					@Override
					public void run() {
						if (cannotMoveSprite != null) {
							cannotMoveSprite.detachSelf();
							cannotMoveSprite = null;
						}
						
						if (canMoveSprite != null) {
							canMoveSprite.detachSelf();
							canMoveSprite = null;
						}

					}
				});
			}
		});
		sprite.registerEntityModifier(delay);
		
		
		return false;
	}
}
