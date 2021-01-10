package com.alanwang;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Graph {
    private final int V;
    private Bag<Integer>[] adj;

    public Graph(int V) {
        this.V = V;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<Integer>();
    }

    public Graph(In in) {
        this.V = in.readInt();
        int n = in.readInt();
        for (int i = 0; i < n; i++) {
            int v = in.readInt();
            int w = in.readInt();
            addEdge(v, w);
        }
    }

    public void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
    }

    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    public int V() {
        return this.V;
    }

    public int E() {
        int edgeCount = 0;
        for (int v = 0; v < this.V; v++)
            for (int w: adj[v])
                edgeCount++;
        return edgeCount / 2;
    }

    public String toString() {
        return "";
    }

    // Static Methods

    /**
     *
     * @param G
     * @param v
     * @return the degree of vertex v
     */
    public static int degree(Graph G, int v) {
        int degree = 0;
        for (int w: G.adj(v)) degree++;
        return degree;
    }

    /**
     * @param G
     * @return maximum degree of vertex
     */
    public static int maxDegree(Graph G) {
        int max = 0;
        for (int v = 0; v < G.V(); v++){
            int degreeOfW = degree(G, v);
            if (degreeOfW > max)
                max = degreeOfW;
        }
        return max;
    }

    /**
     * @param G
     * @return average degree over all vertices
     */
    public static double averageDegree(Graph G) {
        return 2.0 * G.E() / G.V();
    }

    /**
     * @param G
     * @return self-loops vertex count
     */
    public static int numberOfSelfLoops(Graph G) {
        int count = 0;
        for (int v = 0; v < G.V(); v++)
            for (int w: G.adj(v))
                if (v == w) count++;
        return count/2; // each edge counted twice, does it?
    }

    public static void main(String[] args) {
        // Read graph from input stream.
        In in = new In(args[0]);
        Graph G = new Graph(in);

        // print out each edge (twice)
        for (int v = 0; v < G.V(); v++)
            for (int w : G.adj(v))
                StdOut.println(v + "_" + w);
    }
}