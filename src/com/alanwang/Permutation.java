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

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> q = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            q.enqueue(s);
        }

        Iterator<String> iterator = q.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            i++;
            if (i > k) break;
            StdOut.println(iterator.next());
        }
    }
}
