package jstudio.chizmover.scene;

import java.util.ArrayList;
import java.util.List;

import org.andengine.audio.music.exception.MusicReleasedException;
import org.andengine.entity.IEntity;
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
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseLinear;

import android.graphics.Color;
import android.util.Log;

import jstudio.chizmover.data.LevelDetailEntity;
import jstudio.chizmover.managers.*;

public class InGameScreen extends ManagedScene implements IOnSceneTouchListener {
	
	/*
	 *  variables
	 */
	private static final String TAG = "InGameScreen";
	private LevelDetailEntity mCurrentLevel; 
	//private float mSpriteScaleFactor;
	//private int mSpriteCurrentEdge;
	//private List<String> mMazeChars; 
	
	
	private Sprite botSprite; 
	private Sprite canMoveSprite;
	private Sprite cannotMoveSprite;
	
	private List<Sprite> boxSprites;
	private boolean mBotIsMoving = false;
	private boolean clickSpriteShowing = false;
	
	//avoid clicking fast
	private boolean handlingTouch = false;
	private StringBuilder botFlipString = new StringBuilder();
	
	//store current in game menu child
	private MenuScene mMenu = null;
	
	/*
	 * 	sprites
	 */
	//background

		
	protected Text LevelText;
	
	private Font mFont;

	/*
	 * 	constructor
	 */
	public InGameScreen() {
		//decorate background
		super(ResourceManager.GameBackgroundImage);
		
		boxSprites = new ArrayList<Sprite>();
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
		GameManager.getInstance().getMazeChars(entity.getContent());
		if (GameManager.getInstance().getMaze() == null) {
			Log.e(TAG, "Level content is null");
		}
		else {
			int width = GameManager.getInstance().getMazeWidth();
			
			GameManager.getInstance().setupRatio(width);
			
			//create text
			createGameText(String.format("%1$s-%2$s", entity.getPackId(), entity.getLevelNum()));
		}
	}
	
	public void createGameText(String text) {
		if (mFont == null) {
			mFont = ResourceManager.getInstance().getGameFont(43, Color.WHITE);
		}

		LevelText = new Text(0, 0, 
				mFont, text, ResourceManager.getEngine().getVertexBufferObjectManager());
		LevelText.setPosition(ResourceManager.getInstance().cameraWidth / 2, 
				ResourceManager.getInstance().cameraHeight - 30);
		this.attachChild(LevelText);
		
	}
	
	public void createGameGUI() {
		List<String> tmpMazeChars = GameManager.getInstance().getMaze();
		if (mCurrentLevel == null || tmpMazeChars == null) {
			Log.e(TAG, "Current Level is NULL");
			return;
		}
		
		for (int row = 0; row < tmpMazeChars.size(); row++) {
			String line = tmpMazeChars.get(row);
			
			int revertR = tmpMazeChars.size() - row - 1;
			
			for (int col = 0; col < line.length(); col++) {
				char ch = line.charAt(col);
				
				Sprite tmpSprite = null; 
				Sprite tmpAnotherSprite = null;
				
				switch (ch) {
					case GameManager.LEVEL_WALL_CHAR:
						tmpSprite = new Sprite(0, 0, ResourceManager.WallTR, ResourceManager.getEngine().getVertexBufferObjectManager());
						break;
						
					case GameManager.LEVEL_BOT_CHAR:
						botSprite = new Sprite(0, 0, ResourceManager.BotTR, ResourceManager.getEngine().getVertexBufferObjectManager());
						botSprite.setZIndex(2);
						addSpriteToScene(botSprite, col, revertR);
						
						break;
						
					case GameManager.LEVEL_BOX_CHAR:
						tmpSprite = new Sprite(0, 0, ResourceManager.BoxTR, ResourceManager.getEngine().getVertexBufferObjectManager());
						tmpSprite.setZIndex(2);
						boxSprites.add(tmpSprite);
						break;
					
					case GameManager.LEVEL_TARGET:
						tmpSprite = new Sprite(0,  0,  ResourceManager.TargetTR, ResourceManager.getEngine().getVertexBufferObjectManager());
						tmpSprite.setZIndex(1);
						break;
						
					case GameManager.LEVEL_BOX_ON_TARGET:
						
						tmpSprite = new Sprite(0,  0,  ResourceManager.TargetTR, ResourceManager.getEngine().getVertexBufferObjectManager());
						tmpSprite.setZIndex(1);
						
						tmpAnotherSprite = new Sprite(0,  0,  ResourceManager.BoxTR, ResourceManager.getEngine().getVertexBufferObjectManager());
						tmpAnotherSprite.setZIndex(2);
						
						boxSprites.add(tmpAnotherSprite);
						addSpriteToScene(tmpAnotherSprite, col, revertR);
						break;
				}
				
				if (tmpSprite != null) {
					addSpriteToScene(tmpSprite, col, revertR);
				}
			}
		}
		
		this.sortChildren();
		this.setOnSceneTouchListener(this);
	}
	
	private void addSpriteToScene(IEntity sprite, int col, int row) {
		float scale = GameManager.getInstance().SpriteScaleFactor;
		sprite.setScale(scale);
		float pX = (sprite.getWidth() * 0.5f + sprite.getWidth() * col) * scale;
		float pY = (sprite.getHeight() * 0.5f + sprite.getHeight() * row) * scale;
		sprite.setPosition(pX, pY);
		this.attachChild(sprite);
	}
	
	@Override
	public void onLoadScene() {
		super.onLoadScene();
		
		//build gui
		setCurrentLevel(GameManager.getInstance().getCurrentLevel());
		
		createGameGUI();
		
		//create menu
		MenuScene menu = SceneManager.getInstance().createInGameMenu();
		this.setChildScene(menu);
		setMenu(menu);
		
		//play music
		try {
			GameManager.getInstance().playSound();
		} 
		catch (MusicReleasedException e) {
			e.printStackTrace();
		} 
		catch (IllegalStateException e) {
			e.printStackTrace();
		} 
	}

	@Override
	public void onUnloadScene() {
		// TODO Auto-generated method stub
		super.onUnloadScene();
		
		try {
			ResourceManager.getActivity().runOnUpdateThread(new Runnable(){
				@Override 
				public void run() {
					detachChildren();
				}
			});
		}
		catch (Exception ex){

		}
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
	
	private Sprite getTouchBox(int[] touchPos) {
		for (Sprite box : boxSprites) {
			float boxAtX = box.getX();
			float boxAtY = box.getY();
			
			int[] boxPos = GameManager.getInstance().getMatrixPos(new float[] {boxAtX, boxAtY});
			
			if (boxPos[0] == touchPos[0] && boxPos[1] == touchPos[1]) {
				return box;
			}
		}
		
		return null;
	}
	
	private void handleTouchOnBox(Sprite box) {
		if (box == null) return;
		
		char moveChar = 0;
		
		int[] boxPos = GameManager.getInstance().getMatrixPos(new float[] {box.getX(), box.getY()});
		int[] botSpritePos = GameManager.getInstance().getMatrixPos(new float[] {botSprite.getX(), botSprite.getY()});
		
		if (botSpritePos[0] == boxPos[0]) {
			//both move left
			if (botSpritePos[1] - boxPos[1] == 1) {
				if (!GameManager.getInstance().boxHitWall(new int[] {boxPos[0], boxPos[1] - 1})) {
					moveChar = GameManager.BOT_MOVE_LEFT;
				}
			}
			else if (botSpritePos[1] - boxPos[1] == -1) {
				if (!GameManager.getInstance().boxHitWall(new int[] {boxPos[0], boxPos[1] + 1})) {
					moveChar = GameManager.BOT_MOVE_RIGHT;
				}
			}
		}
		else if (botSpritePos[1] == boxPos[1]) {
			if (botSpritePos[0] - boxPos[0] == 1) {
				if (!GameManager.getInstance().boxHitWall(new int[] {boxPos[0] - 1, boxPos[1]})) {
					moveChar = GameManager.BOT_MOVE_UP;
				}
			}
			else if (botSpritePos[0] - boxPos[0] == -1) {
				if (!GameManager.getInstance().boxHitWall(new int[] {boxPos[0] + 1, boxPos[1]})) {
					moveChar = GameManager.BOT_MOVE_DOWN;
				}
			}
		}
		
		if (moveChar != 0) {
			moveTheBox(box, moveChar);
		}
	}
	
	/*
	 * handle touch
	 * @see org.andengine.entity.scene.IOnSceneTouchListener#onSceneTouchEvent(org.andengine.entity.scene.Scene, org.andengine.input.touch.TouchEvent)
	 */
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			//if there is popup menu return
			//Log.i(TAG, "on scene touch");
			
			if (SceneManager.getInstance().getCurrentMenu() == CurrentMenu.PauseMenu) {
				SceneManager.getInstance().hidePauseMenu();
				return false;
			}
			
			if (SceneManager.getInstance().getCurrentMenu() == CurrentMenu.CompleteLevelMenu ||
					SceneManager.getInstance().getCurrentMenu() == CurrentMenu.CompleteEpisodeMenu ||
					SceneManager.getInstance().getCurrentMenu() == CurrentMenu.InstructionMenu) {
				return false;
			}
			
			if (mBotIsMoving)  {
				//Log.i(TAG, "touch: bot is moving");
				return false;
			}
			
			if (handlingTouch)
				return false;
			
			handlingTouch = true;
			
	        final float touchX = pSceneTouchEvent.getX();
	        final float touchY = pSceneTouchEvent.getY();
	        
	        final float botX = botSprite.getX();
	        final float botY = botSprite.getY();

	        int[] touchPos = GameManager.getInstance().getMatrixPos(new float[]{ touchX, touchY});
	        int[] botPos = GameManager.getInstance().getMatrixPos(new float[] {botX, botY});
	        	        
	        Sprite box = getTouchBox(touchPos);
	        if (box != null) {
	        	handleTouchOnBox(box);
	        }
	        else {
		        GameManager.getInstance().initMaze();
		        String pathString = GameManager.getInstance().getShortestPath(touchPos, botPos);
		        
		        moveBot(pathString, new float[] {botX, botY});
		        if (pathString == null || pathString.length() == 0) {
		        	showMoveSprite(false, touchPos[1], touchPos[0]);
		        }
		        else if (pathString != GameManager.PATH_OUTBOUND) {
		        	showMoveSprite(true, touchPos[1], touchPos[0]);
		        }
	        }
	        
	        //done handling touch
	        handlingTouch = false;
	        
	        return true;
	    }
	    return false;
	}
	
	private void moveTheBox(final Sprite box, final char moveChar) {
		
		float dx = 0f, dy = 0f;
		
		final int[] prevPos = GameManager.getInstance().getMatrixPos(new float[] {box.getX(), box.getY()});
		
		switch (moveChar) {
			case GameManager.BOT_MOVE_LEFT:
				dx = -1 * GameManager.getInstance().SpriteCurrentEdge;
				break;
			case GameManager.BOT_MOVE_RIGHT:
				dx = GameManager.getInstance().SpriteCurrentEdge;
				break;
			case GameManager.BOT_MOVE_UP:
				dy = GameManager.getInstance().SpriteCurrentEdge;
				break;
			case GameManager.BOT_MOVE_DOWN:
				dy = -1 * GameManager.getInstance().SpriteCurrentEdge;
				break;
		}
		
		//botSprite.clearEntityModifiers();
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
						
						ResourceManager.getEngine().runOnUpdateThread(new Runnable(){
							@Override
							public void run() {
								botSprite.clearEntityModifiers();
							}
						});
					}
				});
		botSprite.registerEntityModifier(moveForBot);
		
		//box.clearEntityModifiers();
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
						int[] curPos = GameManager.getInstance().getMatrixPos(new float[] {pItem.getX(), pItem.getY()});
						GameManager.getInstance().updateMazeWithNewBoxPos(prevPos, curPos);
						
						if (GameManager.getInstance().checkGameWin()) {
							handleGameWin();
						}
						
						ResourceManager.getEngine().runOnUpdateThread(new Runnable(){
							@Override
							public void run() {
								box.clearEntityModifiers();
							}
						});
					}
				});
		box.registerEntityModifier(moveForTheBox);
	}
	
	private void handleGameWin() {
		Log.i(TAG, "game is won");
		GameManager.getInstance().handleGameWin();
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
			            //Log.i(TAG, "bot runs");
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
			            //Log.i(TAG, "bot stop");
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
		
		float tmpEdge = GameManager.getInstance().SpriteCurrentEdge;
		for (char ch : pathString.toCharArray()) {
			switch (ch) {
				case GameManager.BOT_MOVE_LEFT:
					botScenePos[0] -= tmpEdge;
					break;
				case GameManager.BOT_MOVE_RIGHT:
					botScenePos[0] += tmpEdge;
					break;
				case GameManager.BOT_MOVE_UP:
					botScenePos[1] += tmpEdge;
					break;
				case GameManager.BOT_MOVE_DOWN:
					botScenePos[1] -= tmpEdge;
					break;
			}
			
			path.to(botScenePos[0], botScenePos[1]);
			//Log.i(TAG, "move to: " + botScenePos[0] + " - " + botScenePos[1]);
		}
		return path;
	}
	
	private boolean showMoveSprite(boolean canMove, final int row, final int col) {
		Sprite sprite = null;
		//
		if (clickSpriteShowing)
			return false;
		
		if (canMove) {
			canMoveSprite = new Sprite(0, 0, ResourceManager.CanMoveTR, ResourceManager.getEngine().getVertexBufferObjectManager());
			sprite = canMoveSprite;
		}
		else {
			cannotMoveSprite = new Sprite(0, 0, ResourceManager.CanNOTMoveTR, ResourceManager.getEngine().getVertexBufferObjectManager());
			sprite = cannotMoveSprite;
		}
		sprite.setZIndex(2);
		addSpriteToScene(sprite, row, GameManager.getInstance().getMaze().size() - 1 - col);
		
		sprite.clearEntityModifiers();
		DelayModifier delay = new DelayModifier(0.3f, new IEntityModifierListener() {
			
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				// TODO Auto-generated method stub
				//addSpriteToScene(pItem, row, col);
				clickSpriteShowing = true;
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
						
						clickSpriteShowing = false;
					}
					
				});
			}
		});
		sprite.registerEntityModifier(delay);
		
		return false;
	}
}
