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

// import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordnet;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcastNoun = "";
        int maxDistance = Integer.MIN_VALUE;
        
        // calculate dist sum for every noun
        // get the nouns[i] s.t.
        // d_i is the max of all dist sum
        for (int i = 0; i < nouns.length; i++) {
            int currentDist = 0;
            for (int j = 0; j < nouns.length; j++) {
                currentDist += this.wordnet.distance(nouns[i], nouns[j]);
            }

            if (maxDistance < currentDist) {
                maxDistance = currentDist;
                outcastNoun = nouns[i];
            }
        }
        return outcastNoun;
    }



    public static void main(String[] args) {
        // WordNet wordnet = new WordNet(args[0], args[1]);
        // Outcast outcast = new Outcast(wordnet);
        // for (int t = 2; t < args.length; t++) {
        //     In in = new In(args[t]);
        //     String[] nouns = in.readAllStrings();
        //     StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        // }
    }
}