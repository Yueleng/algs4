/**
 * Week 4 Assignment: 8 - puzzle problem
 *
 * Assignment url: https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/specification.php
 *
 * Notice:
 * Comment the package statement for submission!
 *
 * Score:  96 / 100
 *
 * Defect:
 *
 *
 * Possible Improvements:
 * Test 4b: count Board operations (that should get called),
 *          rejecting if doesn't adhere to stricter caching limits
 *
 *                filename    Board()            equals()         manhattan()
 * --------------------------------------------------------------------------
 * => FAILED  puzzle20.txt       1142                1140               10216   (2.5x)
 * => FAILED  puzzle22.txt       2774                2771               28473   (2.9x)
 * => FAILED  puzzle21.txt       2807                2805               29210   (3.0x)
 * => FAILED  puzzle23.txt       4228                4226               44322   (3.0x)
 * => FAILED  puzzle24.txt       4342                4340               47434   (3.1x)
 * => FAILED  puzzle25.txt       8193                8191               93124   (3.2x)
 * => FAILED  puzzle27.txt       8973                8971              102724   (3.3x)
 * => FAILED  puzzle29.txt       9295                9293              111576   (3.4x)
 * => FAILED  puzzle26.txt       9466                9464              111238   (3.3x)
 * => FAILED  puzzle28.txt      21611               21609              287354   (3.8x)
 * => FAILED  puzzle30.txt      34653               34650              470503   (3.9x)
 * => FAILED  puzzle31.txt      36742               36740              510116   (3.9x)
 * => FAILED  puzzle39.txt      53156               53154              681984   (3.7x)
 * => FAILED  puzzle41.txt      83358               83356             1166110   (4.0x)
 * => FAILED  puzzle34.txt     112233              112231             1669052   (4.2x)
 * => FAILED  puzzle37.txt     123560              123558             1776436   (4.1x)
 * => FAILED  puzzle44.txt     201184              201182             2983816   (4.3x)
 * => FAILED  puzzle32.txt     384459              384457             6371524   (4.7x)
 * => FAILED  puzzle35.txt     392450              392448             6191240   (4.5x)
 * => FAILED  puzzle33.txt     462383              462381             7867768   (4.9x)
 * => FAILED  puzzle43.txt     783776              783774            13212914   (4.8x)
 * => FAILED  puzzle46.txt     784294              784292            13149394   (4.9x)
 * => FAILED  puzzle40.txt     824520              824518            14292174   (4.9x)
 * => FAILED  puzzle36.txt    1544590             1544588            28433996   (5.2x)
 * => FAILED  puzzle45.txt    1813752             1813750            32119294   (5.1x)
 * ==> 0/25 tests passed
 *
 * Key part of this assignment:
 *  For Board.java
 *   *) Copy the tiles into our private attribute: this.tiles to avoid any mutability (exposure) to the private attribute.
 *   *) .equals() mimic the implementation of .equals() in lecture notes.
 *   *) use int[] dx = {-1, 1, 0, 0}; and int[] dy = {0, 0, -1, 1}; to represent the four directions in .neighbors() implementation.
 *   *) copyOfTiles method for copying two dimensional array.
 *
 *  For Solver.java
 *   *) Since Board class did not implement Comparable Interface. We need to create a wrapper class Node to be put into MinPQ
 *   *) The attribute move in node is used in return the min number of moves to solve initial board.
 *   *) The prev Node is kept in every Node. The purpose is to compare the nextNode.getBoard() and preNode.getBoard()
 *        In this way, we won't insert prevNode into MinPQ<Node>
 *   *) The brilliant part of this solver code is the following code:
 *      if (currBoard.manhattan() == 2 && currBoard.twin().isGoal()) {
 *          isSolvable = false;
 *          break;
 *      }
 *      Why?
 *      It uses only one line of code to determine if the board is solvable. Because unsolvable board will
 *      push the following board in the process of solving into the MinPQ:
 *        2 1 3
 *        5 6 7
 *        8 9
 *      thus satisfies manhattan distance 2 and currBoard.twin().isGoal() == True
 *   *) the solution is retrieved by recursively call the solutionNode.prev() and push to stack. Thus the stack will pop
 *        up the initial board first and step by step to solutionNode.
 */

package com.alanwang;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private boolean isSolvable;

    private Node solutionNode;

    private class Node implements Comparable<Node> {

        private final Node prev;
        private final Board board;
        private final int moves;

        Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }

        public int compareTo(Node that) {
            return this.priority() - that.priority();
        }

        public int priority() {
            return board.manhattan() + moves;
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        public Node prev() {
            return prev;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Initial board is null.");
        }

        solutionNode = null;
        MinPQ<Node> minPQ = new MinPQ<>();
        minPQ.insert(new Node(initial, 0, null));

        while (true) {
            Node currNode = minPQ.delMin();
            Board currBoard = currNode.getBoard();

            if (currBoard.isGoal()) {
                isSolvable = true;
                solutionNode = currNode;
                break;
            }

            // This is the brilliant code !!
            if (currBoard.manhattan() == 2 && currBoard.twin().isGoal()) {
                isSolvable = false;
                break;
            }

            // Insert each neighbor except the board of the previous search node
            int moves = currNode.getMoves();
            Board prevBoard = moves > 0 ? currNode.prev().getBoard() : null;

            for (Board nextBoard : currBoard.neighbors()) {
                if (nextBoard.equals(prevBoard)) {
                    continue;
                }
                minPQ.insert(new Node(nextBoard, moves + 1, currNode));
            }
        }


    }



    /**
     * Is the initial board solvable?
     */
    public boolean isSolvable() {
        return isSolvable;
    }

    /**
     * Min number of moves to solve initial board; -1 if unsolvable
     */
    public int moves() {
        return isSolvable() ? solutionNode.getMoves() : -1;
    }

    /**
     * Sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        if (!isSolvable) {
            return null;
        }
        Stack<Board> solution = new Stack<>();
        Node node = solutionNode;
        while (node != null) {
            solution.push(node.getBoard());
            node = node.prev();
        }
        return solution;
    }

    // test client see below
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
