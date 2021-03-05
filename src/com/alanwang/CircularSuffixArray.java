package com.alanwang;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private final String s;
    // private final int[] indices;
    private final Integer[] indices;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("Input string is null");
        this.s = s;
        indices = new Integer[s.length()];
        for (int i = 0; i < s.length(); i++) indices[i] = i;
        Arrays.sort(indices, suffixOrder());
    }

    private Comparator<Integer> suffixOrder() {
        return new SuffixOrder();
    }

    private class SuffixOrder implements Comparator<Integer> {
        // Brilliant Implementation.
        public int compare(Integer i1, Integer i2) {
            for (int i = 0; i < s.length(); i++) {
                char a = s.charAt(i1);
                char b = s.charAt(i2);

                if (a < b) return -1;
                else if (a > b) return 1;

                // first char same
                i1++;
                i2++;
                if (i1 == s.length()) i1 = 0;
                if (i2 == s.length()) i2 = 0;
            }
            return 0;
        }
    }

    // length of s
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= s.length()) throw new IllegalArgumentException("Input index is invalid");
        return indices[i];
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < csa.length(); i++)
            StdOut.print(csa.index(i) + " ");
        StdOut.println();
    }
}
