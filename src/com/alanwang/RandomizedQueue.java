/**
 * Week 2 Assignment: Stack/Queue/Deque
 *
 * Assignment url: https://coursera.cs.princeton.edu/algs4/assignments/queues/specification.php
 *
 * Notice:
 * Comment the package statement for submission!
 *
 * Score: 100 / 100
 *
 * Defect:
 * Test 3 (bonus): check that maximum size of any or Deque or RandomizedQueue object
 *                 created is equal to k
 *   * filename = tale.txt, n = 138653, k = 5
 *     - max size of RandomizedQueue object = 138653
 *
 *   * filename = tale.txt, n = 138653, k = 50
 *     - max size of RandomizedQueue object = 138653
 *
 *   * filename = tale.txt, n = 138653, k = 500
 *     - max size of RandomizedQueue object = 138653
 *
 *   * filename = tale.txt, n = 138653, k = 5000
 *     - max size of RandomizedQueue object = 138653
 *
 *   * filename = tale.txt, n = 138653, k = 50000
 *     - max size of RandomizedQueue object = 138653
 *
 * ==> FAILED
 *
 * Possible Improvements: Can some one explain to me what the above failed test case mean?
 *
 * Key part of this assignment:
 *  For Deque.java
 *    *) We should keep both `prev` and `next` in the Node class for double ended queue,
 *       while in queue or stack in lecture, `next` is enough.
 *    *) Be careful in the addLast(), addFirst(), removeLast(), removeFirst() method
 *       we should refer to null check before calling method for that Node.
 *
 *  For RandomizedQueue.java
 *    *) s = (Item[]) new Object[1] instead of Objects[1];
 *
 *  For Permutation.java
 *    *) int i = 0;
 *      while (iterator.hasNext() && i++ <= k) {...}
 *      Is NOT correct, this will execute k + 1 times.
 *
 */


package com.alanwang;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] s;
    private int N = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        s = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return N;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("item null");
        if (N == s.length) resize(2 * s.length);
        s[N++] = item;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++)
            copy[i] = s[i];
        s = copy;
    }

    // remove and return a random item
    public Item dequeue() {
        if (N == 0) throw new NoSuchElementException("dequeue empty randomized queue");
        int index = (int) (Math.random() * N);
        Item item = s[index];
        s[index] = s[--N];
        s[N] = null;

        if (N > 0 && N == s.length / 4)
            resize(s.length / 2);

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (N == 0) throw new NoSuchElementException("dequeue empty randomized queue");
        int index = (int) (Math.random() * N);
        Item item = s[index];
        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }


    // inner class
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int idx = N;
        private final int[] shuffleIndex = new int[N];
        public RandomizedQueueIterator() {
            // Knuth Shuffle Algorithm
            for (int i = 0; i < N; i++) {
                shuffleIndex[i] = i;
            }
            for (int i = 0; i < N; i++) {
                int j = i + (int) (Math.random() * (N - i));
                int temp = shuffleIndex[i];
                shuffleIndex[i] = shuffleIndex[j];
                shuffleIndex[j] = temp;
            }
        }
        public boolean hasNext() { return idx > 0; }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("Null");
            // if (i == 0) throw new NoSuchElementException("Null");
            return s[shuffleIndex[--idx]];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {

    }

}
