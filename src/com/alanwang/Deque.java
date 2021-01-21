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

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    private Node first, last;
    private int len;

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        len = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null || last == null;
    }

    // return the number of items on the deque
    public int size() {
        return len;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("item null");
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.prev = null;
        if (oldFirst != null) oldFirst.prev = first;
        if (isEmpty()) last = first;
        len++;
    }


    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("item null");
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldLast;
        if (oldLast != null) oldLast.next = last;
        if (isEmpty()) first = last;
        len++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (len == 0) throw new NoSuchElementException("Cannot removeFirst from an empty deque");
        Node oldFirst = first;
        first = first.next;
        if (first != null) first.prev = null;
        else last = null;
        len--;
        return oldFirst.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (len == 0) throw new NoSuchElementException("Cannot removeLast from an empty deque");
        Node oldLast = last;
        last = last.prev;
        if (last != null) last.next = null;
        else first = null;
        len--;
        return oldLast.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() { return current != null; }
        public Item next() {
            if (current == null) throw new NoSuchElementException("Null Deque");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {

    }
}