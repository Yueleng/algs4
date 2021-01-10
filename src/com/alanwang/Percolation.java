/**
 * Week 1 Assignment: Percolation
 *
 * Assingment url: https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php
 *
 * Notice:
 * Comment the package statement for submission!
 *
 * Score: 98 / 100
 *
 * Defect:
 * Test 2 (bonus): check that total memory <= 11 n^2 + 128 n + 1024 bytes
 *    -  failed memory test for n = 64
 * ==> FAILED
 *
 * Possible Improvements: Can we construct only one WeightedQuickUnionUF?
 */

package com.alanwang;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private int numOpenSites;
    private final WeightedQuickUnionUF uf, uf2;
    private boolean[] blocked;


    public Percolation(int num) {
        // Throws error if num less or equal to 0.
        if (num <= 0) throw new IllegalArgumentException();
        n = num;

        // Create a WeightedQuickUnionUF with size n*n+2
        // index 0 means top virtual site
        // index n*n+1 means bottom virtual site
        uf = new WeightedQuickUnionUF(n * n + 2);

        // This is only for isFull() call, this data structure
        // does not include the bottom virtual point
        uf2 = new WeightedQuickUnionUF(n * n + 1);

        // create n-by-n grid, with all sites blocked
        // note that 0 and n*n+1 are not blocked (i.e. open) by default.
        blocked = new boolean[n*n + 2];
        for (int i = 1; i <= n*n; i++) blocked[i] = true;

        // Initialize number of open sites to be 0.
        numOpenSites = 0;
    }

    /**
     *
     * @param row
     * @param col
     * @return index in array from (row, col)
     */
    private int index(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) throw new IllegalArgumentException();
        return (row-1) * n + col;
    }

    /**
     *
     * @param row
     * @param col
     * Open site (row, col) if is not open yet.
     */
    public void open(int row, int col) {
        // Fetch the index
        int id = index(row, col);
        if (!isOpen(row, col)) {
            // Number of open sites +1
            numOpenSites++;

            // Open this site
            blocked[id] = false;

            // union adjacent four sites if possible
            if (row >= 2 && isOpen(row-1, col)) {
                uf.union(id, index(row-1, col));
                uf2.union(id, index(row-1, col));
            }

            if (row <= n-1 && isOpen(row+1, col)) {
                uf.union(id, index(row+1, col));
                uf2.union(id, index(row+1, col));
            }

            if (col >= 2 && isOpen(row, col-1)) {
                uf.union(id, index(row, col-1));
                uf2.union(id, index(row, col-1));
            }

            if (col <= n-1 && isOpen(row, col+1)) {
                uf.union(id, index(row, col+1));
                uf2.union(id, index(row, col+1));
            }

            // Special treatment for row 1: connect this to top virtual site
            if (row == 1) {
                uf.union(id, 0);
                uf2.union(id, 0);
            }

            // Special treatment for row n: connect this to bottom virtual site
            if (row == n) uf.union(id, n*n+1);
        }
    }

    /**
     *
     * @param row
     * @param col
     * @return Is (row, col) site open or not
     */
    public boolean isOpen(int row, int col) {
        // is site (row, col) open?
        return !blocked[index(row, col)];
    }

    /**
     *
     * @param row
     * @param col
     * @return Is (row, col) site full or not (i.e. connected to top virtual site)
     *         This public method seems never used in PercolationStats.java
     */
    public boolean isFull(int row, int col) {
        return uf2.find(0) == uf2.find(index(row, col));
    }

    /**
     *
     * @return number of open sites in this Percolation system
     */
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    /**
     *
     * @return If the Percolation system percolates or not
     *         (i.e. whether top virtual site connected to bottom virtual site)
     */
    public boolean percolates() {
        return uf.find(0) == uf.find(n*n+1);
    }

    // test client (optional)
    // comment for submission
//    public static void main(String[] args) {
//
//    }
}