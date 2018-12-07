package fifteenpuzzlesolver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;
import java.util.concurrent.TimeUnit;

public class Board {
	private int[][] board;
	final int[][] win = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 14, -1, 0 } };
	final int[][] win2 = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 14, 0, -1 } };

	public Board(int[][] blocks) {
		board = new int[blocks.length][blocks.length];
		for (int i = 0; i < blocks.length; i++)
			for (int j = 0; j < blocks.length; j++) {
				board[i][j] = blocks[i][j];
			}
	}

	public int getHammingDistancePattern(int[][] pattern) {
		int hamming = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				int value = board[i][j];

				if ((value != pattern[i][j]) & (value != 0) & value!=-1)
					hamming++;
			}
		}
		return hamming;
	}

	public int findIfExistingBoardsMatch(int[][] pattern, int ActualCost) {

		// Check if Arrays are equal
		int cost = 0;
		int hamming = 0;

		hamming = getHammingDistancePattern(pattern);

		if ((hamming == 0))
			cost = ActualCost + getManhattanLinearConflictDistance();
		// System.out.printf("%d and %d\n", cost, hamming);
		return cost;

	}

	public int getPatternMatchingHeuristic(ArrayList<int[][]> pattern, ArrayList<Integer> Cost) {
		int count = 0;
		/*
		 * To Do move the learned heuristics into a file and call that file here
		 * instead of initialising
		 */
		/* to do move the costs learnt into the above file */
		for (int i = 0; i < 4; i++) {
			count = findIfExistingBoardsMatch(pattern.get(i), Cost.get(i));
			if (count != 0)
				return count;
		}
		// if pattern not found return manhattan
		count = getManhattanLinearConflictDistance();
		return count;
	}

	public int getCombinedManhattanHammingDistance() {

		int threshold =6;
		int a=getManhattanDistance();
		if (a < threshold){
			return getManhattanLinearConflictDistance();		
		}else
			return a;
	}
	public int getRow(int value){
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (value==win[i][j] && value!=0 && value!=-1)
					return i;
			}
		}
		return -1;
	}
	public int getColumn(int value){
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (value==win[i][j] && value!=0 && value!=-1)
					return j;
			}
		}
		return -1;
	}
	// Calculate the Manhattan Distance of the current board
	public int getManhattanDistance() {
		int count = 0;
		int expected = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				int value = board[i][j];
				expected++;
				if (value!=-1 && value != 0 && value != expected) {
					count = count + Math.abs(i - (value-1)/(board.length))
							+ Math.abs(j - (value-1)%board.length);
				}
			}
		}
		return count;
	}
	public int getWalkingDistance() {
		int count = 0;
		int expected = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				int value = board[i][j];
				expected++;
				if (value!=-1 && value != 0 && value != expected) {
					count = count+(int) (Math.sqrt((Math.abs(i - (value-1)/(board.length)))*(Math.abs(i - (value-1)/(board.length)))
							+ Math.abs(j - getColumn(value))*Math.abs(j - getColumn(value))));
				}
			}
		}
		count+=getManhattanLinearConflictDistance();
		return count;
	}
	public int getRowDistance(){
		int count = 0;

		for (int i = 0; i < board.length; i++) {
			int sum=0;
			int sum2=0;
			for (int j = 0; j < board.length; j++) {
				int value = board[i][j];
				sum+=Math.abs(board[i][j]-win[i][j]);
				sum2+=Math.abs(board[j][i]-win[j][i]);
			}
			count+=(sum+sum2)/2;
		}
		return count;
		
	}
	public int getHammingDistance() {
		int hamming = 0;
		int i = 1;
		for (int[] block : board)
			for (int b : block)
				if (i++ != b && b != 0 && b!=-1) {
					hamming++;
				}
		return hamming;
	}

	public int getManhattanLinearConflictDistance() {
		long startTime = System.nanoTime();

		int count = 0;
		int expected = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				int value = board[i][j];
				expected++;
				if (value!=-1 && value != 0 && value != expected) {
					count = count + Math.abs(i - (value-1)/(board.length))
					+ Math.abs(j - (value-1)%(board.length));
				}
			}
		}
		// check for linear conflict if 2 elements are in their rows but swapped
		// in postions
		for (int i = 0; i < board.length; i++) {
			ArrayList<Integer> Araay = new ArrayList<Integer>();
			// find and store those common elements
			for (int j = 0; j < board.length; j++) {
				int value = board[i][j];
				boolean contains = IntStream.of(win[i]).anyMatch(x -> x == value) || IntStream.of(win2[i]).anyMatch(x -> x == value);
				if (contains == true)
					Araay.add(value);
			}
			// now compare and increase the manhattan distance by 2
			for (int j = 0; j < Araay.size(); j++) {
				for (int k = j; k < Araay.size(); k++) {
					if ((Araay.get(j) > Araay.get(k)) && (Araay.get(k) != 0 && Araay.get(k)!=-1))
						count = count + 1;
				}
			}
		}
		long endTime = System.nanoTime();

		// get difference of two nanoTime values
		long timeElapsed = endTime - startTime;
	
		return count;
	}

	// Check if we have reached the goal state
	public boolean isGoal() {

		if (getManhattanDistance() == 0)
			return true;
		return false;
	}

	// Get all possible neighbors of the current board
	public Iterable<Board> getNeighbors() {
		Queue<Board> neighbors = new LinkedList<Board>();
		int[] zeroIndex = new int[2];
		int[] zeroIndex1 = new int[2];

int safecount=0;
		// Find the Zero Index
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == 0 ) {
					zeroIndex[0] = i;
					zeroIndex[1] = j;
					safecount++;
				}if ( board[i][j] ==-1){
					zeroIndex1[0] = i;
					zeroIndex1[1] = j;
					safecount++;
				}if (safecount==2){
					break;
				}
			}
		}

		// Getting the Neighbors
		if (zeroIndex[0] > 0)// Move Zero Up
		{
			Board b = new Board(board);
			int temp = b.board[zeroIndex[0] - 1][zeroIndex[1]];
			b.board[zeroIndex[0] - 1][zeroIndex[1]] = b.board[zeroIndex[0]][zeroIndex[1]];
			b.board[zeroIndex[0]][zeroIndex[1]] = temp;
			neighbors.add(b);
		}
		if (zeroIndex[0] < board.length - 1)// Move Zero Down
		{
			Board b = new Board(board);
			int temp = b.board[zeroIndex[0] + 1][zeroIndex[1]];
			b.board[zeroIndex[0] + 1][zeroIndex[1]] = b.board[zeroIndex[0]][zeroIndex[1]];
			b.board[zeroIndex[0]][zeroIndex[1]] = temp;
			neighbors.add(b);
		}
		if (zeroIndex[1] > 0)// Move Zero Left
		{
			Board b = new Board(board);
			int temp = b.board[zeroIndex[0]][zeroIndex[1] - 1];
			b.board[zeroIndex[0]][zeroIndex[1] - 1] = b.board[zeroIndex[0]][zeroIndex[1]];
			b.board[zeroIndex[0]][zeroIndex[1]] = temp;
			neighbors.add(b);
		}
		if (zeroIndex[1] < board.length - 1)// Move Zero Right
		{
			Board b = new Board(board);
			int temp = b.board[zeroIndex[0]][zeroIndex[1] + 1];
			b.board[zeroIndex[0]][zeroIndex[1] + 1] = b.board[zeroIndex[0]][zeroIndex[1]];
			b.board[zeroIndex[0]][zeroIndex[1]] = temp;
			neighbors.add(b);
		}
		if (zeroIndex1[0] > 0)// Move Zero Up
		{
			Board b = new Board(board);
			int temp = b.board[zeroIndex1[0] - 1][zeroIndex1[1]];
			b.board[zeroIndex1[0] - 1][zeroIndex1[1]] = b.board[zeroIndex1[0]][zeroIndex1[1]];
			b.board[zeroIndex1[0]][zeroIndex1[1]] = temp;
			neighbors.add(b);
		}
		if (zeroIndex1[0] < board.length - 1)// Move Zero Down
		{
			Board b = new Board(board);
			int temp = b.board[zeroIndex1[0] + 1][zeroIndex1[1]];
			b.board[zeroIndex1[0] + 1][zeroIndex1[1]] = b.board[zeroIndex1[0]][zeroIndex1[1]];
			b.board[zeroIndex1[0]][zeroIndex1[1]] = temp;
			neighbors.add(b);
		}
		if (zeroIndex1[1] > 0)// Move Zero Left
		{
			Board b = new Board(board);
			int temp = b.board[zeroIndex1[0]][zeroIndex1[1] - 1];
			b.board[zeroIndex1[0]][zeroIndex1[1] - 1] = b.board[zeroIndex1[0]][zeroIndex1[1]];
			b.board[zeroIndex1[0]][zeroIndex1[1]] = temp;
			neighbors.add(b);
		}
		if (zeroIndex1[1] < board.length - 1)// Move Zero Right
		{
			Board b = new Board(board);
			int temp = b.board[zeroIndex1[0]][zeroIndex1[1] + 1];
			b.board[zeroIndex1[0]][zeroIndex1[1] + 1] = b.board[zeroIndex1[0]][zeroIndex1[1]];
			b.board[zeroIndex1[0]][zeroIndex1[1]] = temp;
			neighbors.add(b);
		}
		return neighbors;
	}

	// Convert the board array into a string
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board.length; j++)
				str.append(board[i][j]);
		return str.toString();
	}

	public int[][] getIntegerArray() {
		return board;
	}
}
