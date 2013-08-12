package DataStructure;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MaximumPriorityQueue<Key> implements Iterable<Key> {

    private Key[] priorityQueue;
    private int size;
    private Comparator comparator;

    /**
     * Build a maximum priority queue, in which finding the
     * maximum key and deleting it are O(1).
     *
     * @param capacity
     */
    public MaximumPriorityQueue(int capacity) {
        priorityQueue = (Key[]) new Object[capacity + 1];
        size = 0;
    }

    /**
     * Build a maximum priority queue, in which finding the
     * max and deleting it are O(1).
     */
    public MaximumPriorityQueue() {
        this(1);
    }

    /**
     * Build a maximum priority queue, in which finding the
     * max and deleting it are O(1).
     *
     * @param capacity
     * @param c
     */
    public MaximumPriorityQueue(int capacity, Comparator c) {
        priorityQueue = (Key[]) new Object[capacity + 1];
        size = 0;
        comparator = c;
    }

    /**
     * Returns true if the priority queue is empty and
     * false otherwise.
     */
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Returns the size of the priority queue.
     */
    public int size() {
        return size;
    }

    /**
     * Inserts the given key in the priority queue.
     *
     * @param key
     */
    public void insert(Key key) {
        if (size >= priorityQueue.length - 1) resize(priorityQueue.length * 2);

        priorityQueue[++size] = key;

        swim(size);
    }

    /**
     * Deletes the maximum key and returns it.
     */
    public Key deleteMax() {

        if (isEmpty()) throw new NoSuchElementException("Empty priority queue.");

        Key key = priorityQueue[1];

        swap(1, size--);
        sinkMax();

        priorityQueue[size + 1] = null;
        if (size > 0 && (size == (priorityQueue.length - 1) / 4)) resize(priorityQueue.length / 2);

        return key;
    }

    /**
     * Returns the maximum key
     */
    public Key max() {
        return priorityQueue[1];
    }

    private void swim(int rank) {
        int parentRank = rank / 2;
        while (parentRank > 0 && greater(rank, parentRank)) {
            swap(rank, parentRank);
            rank = parentRank;
            parentRank = rank / 2;
        }
    }

    private void sinkMax() {

        int rank = 1;

        while (2 * rank <= size) {
            int j = 2 * rank;
            if (j < size - 1 && greater(j + 1, j)) j++;

            if (!greater(j, rank)) break;

            swap(j, rank);
            rank = j;

        }
    }

    private void resize(int newSize) {
        Key[] newPriorityQueue = (Key[]) new Object[newSize];
        System.arraycopy(priorityQueue, 1, newPriorityQueue, 1, size);
        priorityQueue = newPriorityQueue;
    }


    // Helper functions for keys

    private boolean greater(int rank1, int rank2) {
        if (comparator != null) {
            return comparator.compare(priorityQueue[rank1], priorityQueue[rank2]) > 0;
        } else {
            return ((Comparable<Key>) priorityQueue[rank1]).compareTo(priorityQueue[rank2]) > 0;
        }
    }

    private void swap(int rank1, int rank2) {
        Key key = priorityQueue[rank1];
        priorityQueue[rank1] = priorityQueue[rank2];
        priorityQueue[rank2] = key;
    }


    /**
     * Returns an iterator over the keys in the priority queue,
     * in decreasing order.
     *
     * @return
     */
    public Iterator<Key> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Key> {

        private MaximumPriorityQueue<Key> copy;

        public HeapIterator() {
            if (comparator == null) copy = new MaximumPriorityQueue(size);
            else copy = new MaximumPriorityQueue(size, comparator);

            for (int i = 0; i < size; i++) {
                copy.insert(priorityQueue[i]);
            }

        }

        public boolean hasNext() {
            return !copy.isEmpty();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Key next() {
            return copy.deleteMax();
        }

    }

    public static void main(String[] args) {
        MaximumPriorityQueue maxPQ = new MaximumPriorityQueue();
        for (int i = 0; i < 10; i++) {
            maxPQ.insert(i);
        }

        for (int i = 0; i < 10; i++) {
            maxPQ.deleteMax();
        }

        try {
            maxPQ.deleteMax();
        } catch (NoSuchElementException e) {
            System.out.println("Passed exception test.");
        }
    }

}
