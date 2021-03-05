package com.alanwang;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.HashMap;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output

    /**
     * reading: "ABRACADABRA!"
     * write:
     * """
     * 3
     * ARD!RCAAAABB
     * """
     */
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);

        // i = 3 in the example.
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }

        for (int i = 0; i < csa.length(); i++) {
            int index = csa.index(i);
            if (index == 0) BinaryStdOut.write(s.charAt(s.length() - 1), 8);
            else BinaryStdOut.write(s.charAt(index-1), 8);
        }

        BinaryStdIn.close();
        BinaryStdOut.close();


    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    // meaning of next[]:
    //   if the jth original suffix(original string, shifted j characters to the left) is the ith row in the sorted order.
    //   next[i] to be the row in the sorted order where the (j+1)st original suffix appears.
    //
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        char[] t = s.toCharArray(); // t = ['A','R','D','!','R','C','A','A','A','A','B','B']

        // Stores the character and its positions
        HashMap<Character, Queue<Integer>> table = new HashMap<>();
        // table = {'A': [0,6,..], '!':[3], ...}
        for (int i = 0; i < t.length; i++) {
            if (!table.containsKey(t[i]))
                table.put(t[i], new Queue<Integer>());
            table.get(t[i]).enqueue(i);
        }
        Arrays.sort(t); // t = ['!', 'A', 'A', ..., 'R', 'R']

        // construct next array.
        int[] next = new int[t.length];
        for (int i = 0; i < next.length; i++)
            next[i] = table.get(t[i]).dequeue();

        // With first, next, we will reconstruct the original string
        for (int i = 0; i < next.length; i++) {
            BinaryStdOut.write(t[first], 8);
            first = next[first];
        }

        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[1] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No command Line argument");
        }

        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        } else
            throw new IllegalArgumentException("Command Line Argument neither '-' nor '+'.");
    }
}
