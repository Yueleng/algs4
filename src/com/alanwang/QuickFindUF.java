package com.alanwang;

/*
* Eager Approach
* This implementation has quick find operation, but the union operation has O(N) complexity.
* Quick-find defect: Union too expensive
* */

public class QuickFindUF {
    private int[] id;

    public QuickFindUF(int N) {
        id = new int[N];

        // set id of each object to itself (N array accesses)
        for (int i = 0; i < N; i++)
            id[i] = i;
    }

    // check whether p and q are in the same component. (2 array accesses)
    public boolean connected(int p, int q) {
        return id[p] == id[q];
    }

    public void union(int p, int q) {
        int pid = id[p];
        int qid = id[q];

        // change all entries with id[p] to id[q] (at most 2N + 2 array accesses)
        for (int i = 0; i < id.length; i++) {
            if (id[i] == pid) id[i] = qid;
        }
    }
}
