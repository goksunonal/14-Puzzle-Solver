package fifteenpuzzlesolver;


import java.awt.BorderLayout;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
	public static int threshold=10;
	public static int hardness(int [][]tiles){
		Board a=new Board(tiles);
		int b=a.getManhattanLinearConflictDistance();
		System.out.println("Zorluk:"+b);
		if (b<threshold)
			return 0;
		else
			return 1;
	}
	public static void main(String[] args) {
            try 
            {
                    int[][] tiles = {{6,8,11,4},{9,-1,14,3},{1,13,12,10},{0,5,7,2}}; //hard 2: 49 moves
                  //int[][] tiles = {{5,4,3,8},{9,2,6,1},{0,13,14,7},{-1,11,10,12}}; //hard 1: 38 moves
                    //int[][] tiles = {{1,6,0,4},{10,14,2,8},{5,9,13,3},{-1,12,11,7}}; //Medium Hard: 32 moves
               // int[][] tiles = {{3,1,6,4},{5,12,9,7},{10,2,11,8},{13,14,-1,0}}; //Medium: 28 moves
                    //int[][] tiles = {{2,3,4,8},{1,6,0,12},{5,10,7,-1},{9,13,14,11}}; //Easy: 13 moves
                   // int[][] tiles = {{0,1,2,3},{5,6,7,4},{9,10,11,8},{13,14,-1,12}}; //Very Easy:6 moves
                	//int[][] tiles={{-1, 9, 12, 10},{ 8, 11, 1, 4},{ 13, 5, 2, 7}, {6, 3, 0, 14}};
            		//int[][] tiles = {{2, 11, 3, 4},{1, 6, 5, 12},{-1, 7, 0, 13},{9, 8, 10, 14}}; //Very Easy:6 moves
                    //int[][] tiles={{9, 13, 7, 0}, {4, 10, 1, 12}, {3, -1, 8, 14}, {11, 2, 5, 6}};
                //int[][] tiles = {{-1,  2,  3, 13},{1 , 4 ,10  ,7},{9 , 5  ,8 ,11 },{0 ,14 , 6 ,12}}; //Very Easy:6 moves
                //int[][] tiles = {{-1 ,2 ,3 ,13},{7 ,9 ,10 ,12} ,{11 ,4 ,6 ,14 },{5 ,0 ,1 ,8}} ;

            	// Remove tiles parameter to generate random puzzle
                    FifteenPuzzle fp = new FifteenPuzzle(tiles);

                    // Display the puzzle on the front end
                    SwingUtilities.invokeLater(() -> {
                            JFrame f = new JFrame();
                            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            f.setTitle("Fifteen Puzzle");
                            f.setResizable(false);
                            f.add(fp, BorderLayout.CENTER);
                            f.pack();
                            f.setLocationRelativeTo(null);
                            f.setVisible(true);
                    });

                    long startTime = System.currentTimeMillis();

                    // Solving the Puzzle using A Star Algorithm/ IDA Star
                     AStarSolver solver = new AStarSolver(new Board(fp.tiles),hardness(tiles));
                     //IDAStarSolver solver=new IDAStarSolver(new Board(fp.tiles));

                    // Making the moves on the Front End
                    Stack<Board> sols = solver.getSolution();

                    System.out.println("No of Moves: "
                                    + sols.size() +", Time taken: " + (System.currentTimeMillis() - startTime) + "ms , No of nodes searched: " + solver.countOfNodesUsed);


                    for (int i = sols.size() - 1; i >= 0; i--) {
                            Thread.sleep(500);
                            fp.set(sols.get(i).getIntegerArray());
                            fp.repaint();
                    }
                    System.out.println();
            } 
            catch (Exception ex) 
            {
                    ex.printStackTrace();
            }
	}

}
