package com.alanwang;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/*
 * Weighted-Quick-Union with Path Compression (WQUPC)
 * This implementation has quick find operation, and quick union operation.
 * Worst-case time: N + M * (lg^*)N, where lg^*: iterative log function
 *
 * */

public class UF {
    private int[] id;
    private int[] size;

    // set id of each object to itself (N array accesses)
    public UF(int N) {
        id = new int[N];
        size = new int[N];
        for (int i = 0; i < id.length; i++) {
            id[i] = i;
            size[i] = 1;
        }
    }

    // chase parent pointers until reach root
    // (depth of i array accesses)
    private int root(int i) {
        while (i != id[i]) {
            id[i] = id[id[i]]; // only one extra line of code!
            i = id[i];
        }
        return i;
    }

    // check if p and q have same root
    // (depth of p and q array accesses)
    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    // change root of p to point to root of q
    // (depth of p and q array accesses)
    // Weighted Union
    public void union(int p, int q) {
        int i = root(p);
        int j = root(q);
        if (i == j) return;
        if (size[i] < size[j]) {
            id[i] = j;
            size[j] += size[i];
        } else {
            id[j] = i;
            size[i] += size[j];
        }
    }

    public static void main(String[] args) {
        int N = StdIn.readInt();
        UF uf = new UF(N);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (!uf.connected(p, q)) {
                uf.union(p, q);
                StdOut.println(p + " " + q);
            }
        }

    }
}
