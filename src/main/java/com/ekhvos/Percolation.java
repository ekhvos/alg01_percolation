package com.ekhvos;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Data structure for percolation simulation.
 *
 * Created by ekhvos.
 */
public class Percolation {
    private static final int CLOSED = 0;
    private static final int OPENED = 1;
    private int openedCount = 0;
    private final WeightedQuickUnionUF struct;
    private final int n;
    private final int[] statuses;
    private final int[] top;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N should be greater then 0");
        }
        // save n
        this.n = n;
        // init holder
        this.struct = new WeightedQuickUnionUF(n * n);
        // init closed grid
        this.statuses = new int[n * n];
        // init top holder
        this.top = new int[n];
        fillTop();
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);

        int position = getPosition(row, col);
        if (CLOSED == statuses[position]) {
            statuses[position] = OPENED;
            openedCount++;

            doUnion(row, col, position);
        }
    }

    private void validate(int row, int col) {
        if (row <= 0 || row > n
                || col <= 0 || col > n) {
            throw new IllegalArgumentException("Site is out of range");
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);

        return OPENED == statuses[getPosition(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            return false;
        }
        int position = getPosition(row, col);
        return position < n || isLinkedToTop(position);
    }

    private boolean isLinkedToTop(int position) {
        int rootParent = struct.find(position);
        for (int v : top) {
            if (v == rootParent) {
                return true;
            }
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openedCount;
    }

    // does the system percolate?
    public boolean percolates() {
        for (int i = n * (n - 1); i < n * n; i++) {
            if (CLOSED == statuses[i]) {
                continue;
            }
            boolean linkedToTop = isLinkedToTop(i);
            if (linkedToTop) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get actual position in array;
     *
     * @param row the row
     * @param col the column
     * @return the actual position
     */
    private int getPosition(int row, int col) {
        return n * (row - 1) + (col - 1);
    }

    private void doUnion(int row, int col, int currentPosition) {
        boolean linked = false;

        // union with top site if it's open
        if (row > 1 && isOpen(row - 1, col)) {
            int top = getPosition(row - 1, col);
            struct.union(currentPosition, top);
            linked = true;
        }

        // union with bottom site if it's open
        if (row < n && isOpen(row + 1, col)) {
            int bottom = getPosition(row + 1, col);
            struct.union(currentPosition, bottom);
            linked = true;
        }

        // union with left site if it's open
        if (col > 1 && isOpen(row, col - 1)) {
            int left = getPosition(row, col - 1);
            struct.union(currentPosition, left);
            linked = true;
        }

        // union with right site if it's open
        if (col < n && isOpen(row, col + 1)) {
            int right = getPosition(row, col + 1);
            struct.union(currentPosition, right);
            linked = true;
        }

        // fill
        if (linked) {
            fillTop();
        }
    }

    /**
     * Fill top line with actual parent sites.
     */
    private void fillTop() {
        for (int i = 0; i < n; i++) {
            if (CLOSED == statuses[i]) {
                top[i] = i;
            } else {
                top[i] = struct.find(i);
            }
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        // made for tests
    }
}
