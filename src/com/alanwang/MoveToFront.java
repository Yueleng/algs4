package com.alanwang;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {
    private static LinkedList<Character> createASCIIList() {
        LinkedList<Character> asciiList = new LinkedList<Character>();
        for (int i = 0; i <= 255; i++) {
            asciiList.add((char) i);
        }
        return asciiList;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> asciiList = createASCIIList();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int indexOfC = asciiList.indexOf(c);
            asciiList.remove(indexOfC);
            asciiList.addFirst(c);
            BinaryStdOut.write(indexOfC, 8);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    // Example List: [A, B, C, D, E, F]
    // Input: [2, 1, 0, 0, 2, ...] Output: [C, A, A, A, B, ...]
    public static void decode() {
        LinkedList<Character> asciiList = createASCIIList();
        while (!BinaryStdIn.isEmpty()) {
            int indexOfC = BinaryStdIn.readChar();
            char c = asciiList.get(indexOfC);
            asciiList.remove(indexOfC);
            asciiList.addFirst(c);
            BinaryStdOut.write(c, 8);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No command Line argument");
        }

        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        } else
            throw new IllegalArgumentException("Command Line Argument neither '-' nor '+'.");
    }
}
