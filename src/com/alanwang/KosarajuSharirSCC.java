package com.alanwang;

/**
 * Connected Component Class
 */
public class KosarajuSharirSCC {
    private boolean[] marked;
    private int[] id;
    private int count;

    public KosarajuSharirSCC(Digraph G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        DepthFirstOrder dfs = new DepthFirstOrder(G.reverse());
        for (int v: dfs.reversePost())
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
    }

    /**
     * @return number of components
     */
    public int count() {
        return count;
    }

    /**
     * @param v
     * @return id of component containing v
     */
    public int id(int v) {
        return id[v];
    }

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        id[v] = count; // all vertices discovered in same call of dfs have same id.
        for (int w: G.adj(v))
            if (!marked[w])
                dfs(G, w);
    }

    public boolean stronglyConnected(int v, int w) {
        return id[v] == id[w];
    }

}

