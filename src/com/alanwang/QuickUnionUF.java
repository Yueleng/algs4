package com.alanwang;

/*
 * Lazy Approach
 * This implementation has quick find operation, and quick union operation.
 * Quick-union defect:
 *   Worst case for find and union are all O(N). If trees get too tall.
 *
 * */

public class QuickUnionUF {
    private int[] id;

    // set id of each object to itself (N array accesses)
    public QuickUnionUF(int N) {
        id = new int[N];
        for (int i = 0; i < id.length; i++)
            id[i] = i;
    }

    // chase parent pointers until reach root
    // (depth of i array accesses)
    private int root(int i) {
        while (i != id[i]) i = id[i];
        return i;
    }

    // check if p and q have same root
    // (depth of p and q array accesses)
    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    // change root of p to point to root of q
    // (depth of p and q array accesses)
    public void union(int p, int q) {
        int i = root(p);
        int j = root(q);
        id[i] = j;

    }
}
