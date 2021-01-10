package com.alanwang;

/**
 * Connected Component Class
 */
public class CC {
    private boolean[] marked;
    private int[] id;
    private int count;

    public CC(Graph G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
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

    private void dfs(Graph G, int v) {
        marked[v] = true;
        id[v] = count; // all vertices discovered in same call of dfs have same id.
        for (int w: G.adj(v))
            if (!marked[w])
                dfs(G, w);
    }

    public boolean connected(int v, int w) {
        return id[v] == id[w];
    }

}
