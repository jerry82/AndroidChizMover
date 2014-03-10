package jstudio.chizmover.data;

/*
 * Data entity for pack table
 */
public class PackEntity {
	
	private int mId; 
	
	private String mDescription;
	
	private int mCurrentLevel;
	
	private boolean mLock;
	
	private int mNumberOfLevel;
	
	public PackEntity() {
		
	}
	
	public PackEntity(int pId, String pDescription, int pCurrentLevel, boolean pLock, int pNumberOfLevel) {
		this.mId = pId;
		this.mDescription = pDescription;
		this.mCurrentLevel = pCurrentLevel;
		this.mLock = pLock;
		this.mNumberOfLevel = pNumberOfLevel;
	}
	
	public int getId() {
		return mId;
	}
	
	public void setId(int pId) {
		this.mId = pId;
	}
	
	public String getDescription() {
		return mDescription;
	}
	
	public void setDescription(String pDescription) {
		this.mDescription = pDescription;
	}
	
	public int getCurrentLevel() {
		return mCurrentLevel;
	}
	
	public void setCurrentLevel(int pCurrentLevel) {
		this.mCurrentLevel = pCurrentLevel;
	}
	
	public boolean getLock() {
		return mLock;
	}
	
	public void setLock(boolean pLock) {
		this.mLock = pLock;
	}
	
	public int getNumberOfLevel() {
		return mNumberOfLevel;
	}
	
	public void setNumberOfLevel(int pNumberOfLevel) {
		this.mNumberOfLevel = pNumberOfLevel;
	}
}
