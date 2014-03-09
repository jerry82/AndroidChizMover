package jstudio.chizmover.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

/*
 * 	implement A* algorithm to find path
 */
public class PathFinder {
	
	private int mStartNodeId;
	private int mEndNodeId;
	private int[][] mMaze;
	private HashMap<Integer, Node> mNodeDict;
	private List<Integer> mOpenList;
	private List<Integer> mCloseList;
	
	private final int  MOVECOST = 5;
	private final String TAG = "PathFinder";
	
	public PathFinder() {
		
	}
	
	public String getShortestPathString (int[][] maze, int[] touchPos, int[] botPos) {
		String path = null;
		
		if (touchPos[0] == botPos[0] && touchPos[1] == botPos[1]) 
			return "";
		
		mMaze = maze;
		
		if (!withinBound(touchPos)) {
			return "OUTBOUND";
		}
		
		mStartNodeId = getNodeId(botPos);
		mEndNodeId = getNodeId(touchPos);
		
		recreateList();
		
		buildNodeDict();
		
		path = route();
		
		return path;
	}
	
	private String route() {
		
		int curId = mStartNodeId;
		
		while (curId != mEndNodeId) {
			
			mCloseList.add(curId);
			
			List<Integer> neighbors = getNeightbors(curId);
			if (neighbors.size() > 0) 
				mOpenList.addAll(neighbors);
			
			if (mOpenList.size() > 0) 
				curId = popMinFValueFromOpenList();
			else 
				//no path
				return "";
		}
		
		List<Integer> pathNodes = new ArrayList<Integer>();
		int tmpId = mEndNodeId;
		
		while (tmpId != mStartNodeId) {
			pathNodes.add(tmpId);
			Node tmpNode = mNodeDict.get(tmpId);
			tmpId = tmpNode.ParentID;
			if (tmpId == -1) {
				break;
			}
		}
		pathNodes.add(tmpId);
		
		return convertPathToString(pathNodes);
	}
	
	private String convertPathToString(List<Integer> nodes) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = nodes.size() - 1; i > 0; i--) {
			int cur = nodes.get(i);
			int next = nodes.get(i - 1);
			
			sb.append(getMove(cur, next));
		}
		
		return sb.toString();
	}
	
	private String getMove (int from, int to) {
		char move = GameManager.BOT_MOVE_DOWN;
		
		int[] curPos = getPosOfNodeId(from);
		int[] toPos = getPosOfNodeId(to);
		
		if (curPos[1] == toPos[1]) {
			if (toPos[0] > curPos[0])
				move = GameManager.BOT_MOVE_DOWN;
			else 
				move = GameManager.BOT_MOVE_UP;
		}
		else if (curPos[0] == toPos[0]) {
			if (toPos[1] > curPos[1])
				move = GameManager.BOT_MOVE_RIGHT;
			else 
				move = GameManager.BOT_MOVE_LEFT;
		}
		
		return move + "";
	}
	
	/*
	 * 	aStar algorithm
	 */
	private void buildNodeDict() {
		mNodeDict = new HashMap<Integer, Node>();
		
		if (mStartNodeId < 1 || mEndNodeId < 1) {
			Log.e(TAG, "Error: invalid startNodeId | endNodeId!");
			return;
		}
		
		int cnt = 1;
		
		for (int i = 0; i < mMaze.length; i++) {
			for (int j = 0; j < mMaze[0].length; j++) {
				
				Node node = new Node();
				node.ID = cnt;
				node.MatrixPos = new int[]{0, 0};
				node.MatrixPos[0] = i;
				node.MatrixPos[1] = j;
				node.GValue = 0;
				node.FValue = 0;
				node.ParentID = -1;
				node.IsWall = false; 
				
				
				//calculate hvalue
				int[] tmpPos = getPosOfNodeId(mEndNodeId);
				node.HValue = Math.abs(tmpPos[0] - i) + Math.abs(tmpPos[1] - j);
								
				if (mMaze[i][j] == -1) {
					node.IsWall = true; 
					node.HValue = -1;
				}
				
				mNodeDict.put(cnt, node);
				
				cnt++;
			}
		}
	}

	//return list of neighbors
	//also calculate FValue of those neighbors
	private List<Integer> getNeightbors(int curId) {
		List<Integer> neighbors = new ArrayList<Integer>();
		
		int[] curPos = getPosOfNodeId(curId);
		Node curNode = mNodeDict.get(curId);
		
		for (int i = 1; i <= 4; i++) {
			int[] nbPos = new int[2];
			switch (i) {
				case 1:
					nbPos[0] = curPos[0] - 1;
					nbPos[1] = curPos[1];
					break;
				case 2:
					nbPos[0] = curPos[0] + 1;
					nbPos[1] = curPos[1];
					break;
				case 3:
					nbPos[0] = curPos[0];
					nbPos[1] = curPos[1] - 1;
					break;
				case 4:
					nbPos[0] = curPos[0];
					nbPos[1] = curPos[1] + 1;
					break;
			}
			
			if (withinBound(nbPos)) {
				//set parent
				Node nbNode = mNodeDict.get(getNodeId(nbPos));
				
				//ignore wall
				if (nbNode.IsWall) continue;
				
				//ignore node in close list
				if (mCloseList.contains(nbNode.ID)) continue;
				
				//not visited node
				if (nbNode.GValue == 0) {
					//calculate gcost and fcost of nb
					nbNode.ParentID = curId;
					nbNode.GValue = curNode.GValue + MOVECOST;
					nbNode.FValue = nbNode.GValue + nbNode.HValue;
				}
				//already visited
				else {
					if (curNode.GValue + MOVECOST < nbNode.GValue) {
						nbNode.ParentID = curId;
						nbNode.GValue = curNode.GValue + MOVECOST;
						nbNode.FValue = nbNode.GValue + nbNode.HValue;
					}
				}
				
				neighbors.add(getNodeId(nbPos));
			}
		}
		
		return neighbors;
	}
	
	/*
	 * helpers
	 * 
	 */
	private boolean withinBound(int[] pos) {
		if (pos[0] < 0 || pos[1] < 0 || pos[0] >= mMaze.length || pos[1] >= mMaze[0].length)
			return false;
		
		return true;
	}
	
	/*
	 * 	translate position to nodeid: 
	 * 	nodeid start at 1
	 */
	private int getNodeId(int[] pos) {
		return pos[0] * mMaze[0].length + pos[1] + 1;
	}
	
	/*
	 * 	translate nodeId to position in matrix
	 * 
	 */
	private int[] getPosOfNodeId(int id) {
		int[] pos = new int[2];
		
		int width = mMaze[0].length;
		
		//row
		if (id % width == 0)
			pos[0] = id / width - 1;
		else
			pos[0] = id / width;
		//col
		pos[1] = (id - 1) % width;
		
		return pos;
	}
	
	private int popMinFValueFromOpenList() {
		int returnId = -1;
		int min = Integer.MAX_VALUE;		
		if (mOpenList.size() > 0) {
			for (int id : mOpenList) {
				Node tmp = mNodeDict.get(id);
				if (min > tmp.FValue) {
					returnId = id;
					min = tmp.FValue;
				}
			}
		}
		//remove 
		mOpenList.remove(mOpenList.indexOf(returnId));
		
		return returnId;
	}
	
	private void recreateList() {
		if (mOpenList == null)
			mOpenList = new ArrayList<Integer>();
		else 
			mOpenList.clear();
		
		if (mCloseList == null)
			mCloseList = new ArrayList<Integer>();
		else 
			mCloseList.clear();
	}
	

	/*
	 * 	for debuggin
	 */
	private void showOpenCloseList() {
		Log.d(TAG, "openlist:");
		for (int id : mOpenList) {
			int parentID = mNodeDict.get(id).ParentID;
			Log.d(TAG, String.format("%1$d|%2$d", id, parentID));
		}
		
		Log.d(TAG, "closelist:");
		for (int id : mCloseList) {
			int parentID = mNodeDict.get(id).ParentID;
			Log.d(TAG, String.format("%1$d|%2$d", id, parentID));
		}
		
	}
	
	private void displayHGValues() {
		for (int i = 0; i <= mNodeDict.size(); i++) {
			Node node = mNodeDict.get(i + 1);
			Log.i(TAG, String.format("%1$d %2$d", node.HValue, node.GValue));
		}
	}
}

/*
 * 	to use within PathFinder only
 */
class Node {
	public int ID; 
	public int ParentID;
	public int HValue;
	public int GValue;
	public int FValue;
	public boolean IsWall;
	public int[] MatrixPos;
}
