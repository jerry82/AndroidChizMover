package jstudio.chizmover.data;

/*
 * Data entity for pack table
 */
public class PackEntity {
	
	private int mId; 
	
	private String mDescription;
	
	private int mCurrentLevel;
	
	private boolean mLock;
	
	public PackEntity(int pId, String pDescription, int pCurrentLevel, boolean pLock) {
		this.mId = pId;
		this.mDescription = pDescription;
		this.mCurrentLevel = pCurrentLevel;
		this.mLock = pLock;
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
}
