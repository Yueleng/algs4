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

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private final int n; // dimension (width and height) parameter of the board.
    private final int hammingDistance; // cached hammingDistance
    private final int manhanttanDistance; // cached manhattanDistance
    private final int[][] tiles; // tiles information. using 0 to designate the blank square
    private int blankx, blanky;  // position of blank title.

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        int hd = 0;
        int md = 0;

        this.tiles = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                this.tiles[i][j] = tiles[i][j];
                // blank tile is not counted as mis position.
                if (tiles[i][j] == 0) {
                    blankx = i;
                    blanky = j;
                } else if (tiles[i][j] != i * n + j + 1) {
                    ++hd;

                    // tiles[i][j] - 1 means the supposed position, for example
                    //      8 should be at position 7.
                    md += Math.abs(i - (tiles[i][j] - 1) / n);
                    md += Math.abs(j - (tiles[i][j] - 1) % n);
                }
            }
        }

        hammingDistance = hd;
        manhanttanDistance = md;
    }

    // string representation of this board
    // The toString() method returns a string composed of n + 1 lines.
    // The first line contains the board size n;
    // the remaining n lines contains the n-by-n grid of tiles in row-major order,
    // using 0 to designate the blank square.
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // s.append(String.format("%2d ", tiles[i][j]));
                s.append(tiles[i][j] + " ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhanttanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhanttanDistance == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (n != that.dimension()) {
            return false;
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();

        // four directions
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        for (int i = 0; i < dx.length; ++i) {
            int x = blankx + dx[i];
            int y = blanky + dy[i];

            // should be inside the n * n square
            if (x >= 0 && x < n && y >= 0 && y < n) {
                int[][] matrix = copyOfTiles(tiles);
                swap(matrix, blankx, blanky, x, y);
                neighbors.add(new Board(matrix));
            }
        }

        return neighbors;
    }

    private void swap(int[][] matrix, int x1, int y1, int x2, int y2) {
        matrix[x1][y1] = matrix[x2][y2];
        matrix[x2][y2] = this.tiles[x1][y1];
    }

    // return a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] matrix = copyOfTiles(tiles);
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                // find the first tile such that
                // j < n-1 and tile[i][j] and tile[i][j+1] both not equal to 0
                // then return the first found such swap
                if (j < n - 1 && tiles[i][j] != 0 && tiles[i][j + 1] != 0) {
                    swap(matrix, i, j, i, j + 1);
                    return new Board(matrix);
                }
            }
        }
        return null;
    }

    private int[][] copyOfTiles(int[][] tiles) {
        int[][] matrix = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            matrix[i] = Arrays.copyOf(tiles[i], tiles[i].length);
        }
        return matrix;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] blocks = {
                {8, 1, 3},
                {4, 0, 2},
                {7, 6, 5},
        };

        Board b = new Board(blocks);
        StdOut.println(b.toString());
        StdOut.println("Dimension: " + b.dimension());
        StdOut.println("Hamming distance: " + b.hamming());
        StdOut.println("Manhattan distance: " + b.manhattan());

        Board t = b.twin();
        StdOut.println("Block twin: " + t.toString());
        StdOut.println(b.equals(t));

        for (Board it : b.neighbors()) {
            StdOut.println(it.toString());
            StdOut.println("Dimension: " + it.dimension());
            StdOut.println("Hamming distance: " + it.hamming());
            StdOut.println("Manhattan distance: " + it.manhattan());
        }
    }
}
