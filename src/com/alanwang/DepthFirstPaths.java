package com.alanwang;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class DepthFirstPaths {
    private boolean[] marked;
    private int[] edgeTo;
    private int s;

    public DepthFirstPaths(Graph G, int s) {
        // Initialize data structures
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;

        dfs(G, s);
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        for (int w: G.adj(v))
            if (!marked[w]) {
                dfs(G, w);
                edgeTo[w] = v; // v -> w
            }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        // Q: Why Stack?
        // A: Print out the vertex from s to v, and s was last to be pushed to the Iterable.
        Stack<Integer> path = new Stack<>();
        for (int x = v; x != s; x = edgeTo[v]) {
            path.push(x);
        }
        path.push(s);
        return path;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        int s = Integer.parseInt(args[1]);
        DepthFirstPaths paths = new DepthFirstPaths(G, s);
        for (int v = 0; v < G.V(); v++)
            if (paths.hasPathTo(v))
                StdOut.println(v); // print all vertices connected to s

    }

}
