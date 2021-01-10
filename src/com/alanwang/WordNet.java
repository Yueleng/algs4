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

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class WordNet {
    private final Map<String, List<Integer>> nounToInts = new HashMap<>();
    private final Map<Integer, String> intToSynset = new HashMap<>();
    private final Digraph G;
    // Shortest Ancestral Path
    private final SAP sap;
    /**
     * Constructor takes the name of two input files.
     * @param synsets
     * @param hypernyms
     */
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();

        In in = new In(synsets);
        int maxID = 0;
        String inputLine;
        while (true) {
            inputLine = in.readLine();
            if (inputLine == null) break;
            // items = [0,'hood,(slang) a neighborhood] or items = [1,1530s,the decade from 1530 to 1539 ]
            String[] items = inputLine.split(",");

            // items[1] = "noun1 noun2 noun3" => nouns = ["noun1", "noun2", "noun3"]
            String[] nouns = items[1].split(" ");

            // in order to construct the dict
            // {noun1: noun1IdxList, noun2: noun2IdxList, ...},
            // where nounIdxList contains all the idx(s) that contain that noun,
            // for example two lines of synset look like this
            // 1, apple orange, fruits blablabla
            // 101 huawei apple, phonemaker blablabla
            // so dict = {..., apple: [1, 101], ...}
            List<Integer> nounIdxList;
            for (int i = 0; i < nouns.length; i++) {
                if (!nounToInts.containsKey(nouns[i]))
                    nounIdxList = new LinkedList<>();
                else
                    nounIdxList = nounToInts.get(nouns[i]);

                nounIdxList.add(Integer.parseInt(items[0]));
                nounToInts.put(nouns[i], nounIdxList);
            }

            // reverseDict = {..., 101: "huawei apple", ...}
            intToSynset.put(Integer.parseInt(items[0]), items[1]);

            // the maximum idx
            maxID = Math.max(maxID, Integer.parseInt(items[0]));
        }
        in.close();

        // Create new Digraph by reading hypernyms
        G = new Digraph(maxID + 1);

        in = new In(hypernyms);
        while (true) {
            inputLine = in.readLine();
            if (inputLine == null) break;
            // items = [hyponym, hypernym*]
            String[] items = inputLine.split(",");
            int v = Integer.parseInt(items[0]);
            for (int i = 1; i < items.length; i++)
                G.addEdge(v, Integer.parseInt(items[i]));
        }
        in.close();

        // Check for DAG property
        if (!isDAG(G)) throw new IllegalArgumentException("The Digraph is cyclic!");

        // initialize sap
        sap = new SAP(G);
    }

    /**
     * @param G
     * @return true if the Digraph is acyclic (directed acyclic graph);  false if cyclic.
     * IDEA: Try to sort the graph topologically, if you cannot then it has cycles.
     * Also: Don't forget to check the Digraph G is root digraph, i.e. with one root.
     */
    private boolean isDAG(Digraph G) {
        for (int i = 0; i < G.V(); i++) {
            Set<Integer> visited = new HashSet<>();
            Stack<Integer> reversePost = new Stack<>();
            if (!visited.contains(i))
                if (!isDagIter(visited, reversePost, i))
                    return false;
        }

        // Check if that the digraph has only one root
        int count = 0;
        for (int i = 0; i < G.V(); i++) {
            int n = 0;
            for (int w: G.adj(i)) n += 1;
            if (n == 0) count += 1; // last node in this dag.
        }

        if (count != 1) return false; // multiple last nodes.
        return true;
    }

    /**
     *
     * @param visited
     * @param reversePost
     * @param v
     * @return Auxiliary dfs for isDAG, follow the IDEA part in isDAG, standard DFS
     */
    private boolean isDagIter(Set<Integer> visited, Stack<Integer> reversePost, int v) {
        visited.add(v);
        reversePost.push(v);

        for (int w: G.adj(v)) {
            // if w in the stack reversPost, it forms a circle.
            // This is INGENIOUS idea and crucial!!
            // If the path is very deep. We can keep a parallel HashSet which is the copy the stack
            //    in order to take advantage of .contains call with O(1) complexity
            if (reversePost.contains(w))
                return false;

            if (!visited.contains(w))
                if (!isDagIter(visited, reversePost, w))
                    return false;
        }

        reversePost.pop();
        return true;
    }

    public Iterable<String> nouns() {
        return nounToInts.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounToInts.containsKey(word);
    }

    /**
     * Shortest common ancestor to subsets of vertices
     * A shortest ancestral path of two subsets of vertices A and B is a
     * shortest ancestral path over all pairs of vertices v and w, with v in A and w in B.
     * @param nounA
     * @param nounB
     * @return Shortest common ancestor to subsets of vertices
     */
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        List<Integer> idxAs = nounToInts.get(nounA);
        List<Integer> idxBs = nounToInts.get(nounB);

        return sap.length(idxAs, idxBs);
    }

    /**
     * @param nounA
     * @param nounB
     * @return a synset (second field of synsets.txt) that the common ancestor of nounA and nouB
     *         in a shortest ancestral path.
     */
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        List<Integer> idxAs = nounToInts.get(nounA);
        List<Integer> idxBs = nounToInts.get(nounB);

        return intToSynset.get(sap.ancestor(idxAs, idxBs));
    }

}
