/**
 * Notice:
 * Comment the package statement for submission!
 *
 * Defect:
 * Test 2 (bonus): check that total memory <= 11 n^2 + 128 n + 1024 bytes
 *    -  failed memory test for n = 64
 * ==> FAILED
 */

package com.alanwang;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] ratio;
    private final int trials;

    public PercolationStats(int n, int trials) {
        // throw exception when input arguments not valid
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();

        // Initialize num of trials, ratio array, grid size or num of sites (n * n)
        this.trials = trials;
        this.ratio = new double[trials];
        final int numSites = n*n;

        // perform independent trials experiments on an n-by n grid
        for (int t = 0; t < this.trials; t++) {
            Percolation p = new Percolation(n);

            // random permutations return one of the permutation of (0, 1, ..., n-1)
            int[] perm = StdRandom.permutation(numSites);

            // open sites one by one until percolates
            int ptr = 0;
            while (!p.percolates()) {
                int index = perm[ptr];
                int row = index / n + 1;
                int col = index % n + 1;
                p.open(row, col);
                ptr++;
            }

            // Calculate the percolates ratio for this single experiment t
            this.ratio[t] = p.numberOfOpenSites() * 1.0 / numSites;
        }
    }

    public double mean() {
        // sample mean of percolation threshold
        return StdStats.mean(this.ratio);
    }

    public double stddev() {
        return StdStats.stddev(this.ratio);
    }

    public double confidenceLo() {
        // low endpoint of 95% confidence interval
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(this.trials);
    }

    public double confidenceHi() {
        // high endpoint of 95% confidence interval
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(this.trials);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int n = Integer.parseInt(args[0]);
        int testCases = Integer.parseInt(args[1]);
        PercolationStats tester = new PercolationStats(n, testCases);

        StdOut.println("mean                    = " + tester.mean());
        StdOut.println("stddev                  = " + tester.stddev());
        StdOut.println("95% confidence interval = [" + tester.confidenceLo() + ", " + tester.confidenceHi() + "]");
    }
}
