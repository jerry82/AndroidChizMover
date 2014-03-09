package jstudio.chizmover.data;

/*
 * 	Data entity for level_detail table
 */
public class LevelDetailEntity {
	
	private int mId;
	private String mContent;
	private int mPackId;
	private int mLevelNum;
	private int mDifficulty;
	
	public LevelDetailEntity() {
		
	}
	
	public LevelDetailEntity(int pId, String pContent, int pPackId, int pLevelNum, int pDifficulty) {
		this.mId = pId;
		this.mContent = pContent;
		this.mPackId = pPackId;
		this.mLevelNum = pLevelNum;
		this.mDifficulty = pDifficulty;
	}
	
	public int getId() { return mId; }
	public void setId(int pId) { this.mId = pId; }
	
	public String getContent() { return mContent; }
	public void setContent(String pContent) { this.mContent = pContent; }
	
	public int getPackId() { return mPackId; } 
	public void setPackId(int pPackId) { this.mPackId = pPackId; }
	
	public int getLevelNum() { return mLevelNum; } 
	public void setLevelNum(int pLevelNum) { this.mLevelNum = pLevelNum; }
	
	public int getDifficulty() { return mDifficulty; } 
	public void setDifficulty(int pDifficulty) { this.mDifficulty = pDifficulty; }
	
}
