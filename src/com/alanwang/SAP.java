/**
 * Week 7 Assignment: WordNet
 *
 * Assingment url: https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php
 *
 * Notice:
 * Comment the package statement for submission!
 *
 * Score: 100 / 100
 *
 * Defect:
 * Test 3: count Digraph operations during WordNet constructor
 *    - the constructor makes more than 2 calls to adj() per vertex
 * ==> FAILED
 *
 * Possible Improvements: Try not to call adj() twice per vertex in the isDAG (and isDAGIter) func.
 *
 * Key part of this assignment:
 *   - In order for the SAP to be immutable. "this.G = new Digraph(G)" should be called in SAP constructor
 *   - In the WordNet constructor, create two Maps, which store information about transformations between
 *       index and synset (or nouns)
 *   - The core algorithm in WordNet.java is the implementation of isDAG:
 *       We maintain a Stack called reversePost to determine if the Digranph G contains a circle.
 *       We also maintain a HashSet called visited, to reduce the isDagIter func calls.
 *       Finally, we also checked if the Digraph have multiple roots.
 *         If we have one independent root, We will have one node that has no adj(), i.e. this is the root
 *   - The core algorithm in SAP.java is the implementation of getAncestors(int v) which returns
 *       the HashMap<w: Integer, distance:Integer> meaning the distance of v to w for all w that is reachable for v.
 *     I tried to compute the LCA of two nodes in a simple way for tree (commented codes), but realized that
 *       a node can jump to multiple nodes upwards. (see the first image in assignment url)
 *     All other methods are easily implemented after the getAncestors is implemented in SAP.java
 *
 */

package com.alanwang;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;
// import java.util.HashSet;
import java.util.Map;

// Shortest Ancestral Path
public class SAP {
    private final Digraph G;
    // private boolean[] marked;
    // private int[] parents;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        this.G = new Digraph(G);
        // set parent of every node -1 in initialization.
//        for (int i = 0; i < G.V(); i++)
//            parents[i] = -1;
    }

    // Helper function: checkIndex
    private void checkIndex(int index) {
        if (index < 0 || index >= G.V()) throw new IllegalArgumentException("index out of range");
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkIndex(v);
        checkIndex(w);

        // This method does not hold for the case where node have multiple parents.
        // Only hold for tree where node has one parent.
//        findParents(v);
//        findParents(w);

//        HashSet<Integer> upwardNodes = new HashSet<>();
//        int lca = -1; // lowest common ancestor
//        while ((parents[v] != -1 || parents[w] != -1) /* one of the search not finished */) {
//            if (upwardNodes.contains(parents[v])) {
//                lca = parents[v];
//                break;
//            } else {
//                upwardNodes.add(parents[v]);
//                v = parents[v];
//            }
//
//            if (upwardNodes.contains(parents[w])) {
//                lca = parents[w];
//                break;
//            } else {
//                upwardNodes.add(parents[w]);
//                w = parents[w];
//            }
//        }
//
//        return lca;

        Map<Integer, Integer> ancestorsTo_v = getAncestors(v);
        Map<Integer, Integer> ancestorsTo_w = getAncestors(w);

        int minDist = Integer.MAX_VALUE; // set initial minimum distance to be max integer.
        int lca = -1; // lowest common ancestor
        for (int ancestorTo_v: ancestorsTo_v.keySet()) {
            if (ancestorsTo_w.containsKey(ancestorTo_v)) {
                int currentDist = ancestorsTo_w.get(ancestorTo_v) + ancestorsTo_v.get(ancestorTo_v);
                if (currentDist < minDist) {
                    minDist = currentDist;
                    lca = ancestorTo_v;
                }
            }
        }
        return lca;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkIndex(v);
        checkIndex(w);
        // This method does not hold for the case where node have multiple parents.
        // Only hold for tree where node has one parent.
//        int lca = ancestor(v, w);
//        if (lca != -1) {
//            int dist1 = 0;
//            int dist2 = 0;
//
//            while (v != lca) {
//                dist1 += 1;
//                v = parents[v];
//            }
//
//            while (w != lca) {
//                dist2 += 1;
//                w = parents[w];
//            }
//
//            return dist1 + dist2;
//        } else return -1;
        Map<Integer, Integer> ancestorsTo_v = getAncestors(v);
        Map<Integer, Integer> ancestorsTo_w = getAncestors(w);

        int minDist = Integer.MAX_VALUE; // set initial minimum distance to be max integer.
        for (int ancestorTo_v : ancestorsTo_v.keySet()) {
            if (ancestorsTo_w.containsKey(ancestorTo_v)) {
                int currentDist = ancestorsTo_w.get(ancestorTo_v) + ancestorsTo_v.get(ancestorTo_v);
                if (currentDist < minDist) {
                    minDist = currentDist;
                }
            }
        }
        return minDist == Integer.MAX_VALUE ? -1 : minDist;
    }

    // Helper function: findParents
    // Standard DFS.
    // Is not used here. Works with tree's lowest common ancestor
//    private void findParents(int v) {
//        marked[v] = true;
//        for (int w: G.adj(v)) {
//            if (!marked[w]) {
//                findParents(w);
//                parents[w] = v;
//            }
//        }
//    }

    // Helper function: get ancestors
    // Standard BFS.
    private Map<Integer, Integer> getAncestors(int v) {
        Queue<Integer> q = new Queue<>();
        Map<Integer, Integer> distT_v = new HashMap<>();
        q.enqueue(v);
        distT_v.put(v, 0);
        while (!q.isEmpty()) {
            int head = q.dequeue();
            int currentDist = distT_v.get(head);
            for (int w: G.adj(head)) {
                // if w is new in distT_v
                // We don't need to check if this length is the shortest
                // Since BFS search always from close to far.
                if (!distT_v.containsKey(w)) {
                    q.enqueue(w);
                    distT_v.put(w, currentDist + 1);
                }
            }
        }
        return distT_v;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w;
    // -1 if no such path;
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        int min = Integer.MAX_VALUE;
        for (Integer i: v) {
            if (i == null) throw new IllegalArgumentException();
            for (Integer j: w) {
                if (j == null) throw new IllegalArgumentException();
                int minLength = length(i, j);
                if (minLength < min && minLength != -1)
                    min = minLength;
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        int min = Integer.MAX_VALUE;
        int node1 = -1;
        int node2 = -1;
        for (Integer i: v) {
            if (i == null) throw new IllegalArgumentException();
            for (Integer j: w) {
                if (j == null) throw new IllegalArgumentException();
                int minLength = length(i, j);
                if (minLength < min && minLength != -1) {
                    min = minLength;
                    node1 = i;
                    node2 = j;
                }

            }
        }
        return (node1 == -1 || node2 == -1) ? -1 : ancestor(node1, node2);
    }

    // do unit testing of this class
//    public static void main(String[] args) {
//
//    }

}
