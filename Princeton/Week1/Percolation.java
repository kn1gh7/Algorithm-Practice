package week1;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF connectedPts;
    private final int gridDimen;
    private boolean[] openSites;
    private int openSitesCount = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        connectedPts = new WeightedQuickUnionUF(n * n + 2);
        gridDimen = n;
        openSites = new boolean[n * n + 2];
        openSitesCount = 0;

        for (int i = 0; i < gridDimen; i++) {
            // Connect first row with index 0
            connectedPts.union(0, i + 1);

            // Connect last row with index n * n + 1, aka last index;
            connectedPts.union(n * n + 1, n * n - i);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isValidRowCol(row, col)) {
            throw new IllegalArgumentException();
        }

        int currentIndex = getIndex(row, col);
        openSites[currentIndex] = true;
        openSitesCount++;

        if (isValidRowCol(row - 1, col) && isOpen(row - 1, col)) {
            connectedPts.union(getIndex(row - 1, col), currentIndex);
        }

        if (isValidRowCol(row + 1, col) && isOpen(row + 1, col)) {
            connectedPts.union(getIndex(row + 1, col), currentIndex);
        }

        if (isValidRowCol(row, col - 1) && isOpen(row, col - 1)) {
            connectedPts.union(getIndex(row, col - 1), currentIndex);
        }

        if (isValidRowCol(row, col + 1) && isOpen(row, col + 1)) {
            connectedPts.union(getIndex(row, col + 1), currentIndex);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isValidRowCol(row, col)) {
            throw new IllegalArgumentException();
        }

        return openSites[getIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isValidRowCol(row, col)) {
            throw new IllegalArgumentException();
        }

        int currentParent = connectedPts.find(getIndex(row, col));
        int rootParent = connectedPts.find(0);
        return isOpen(row, col) && currentParent == rootParent;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    // does the system percolate?
    public boolean percolates() {
        int rootParent = connectedPts.find(0);
        int leafParent = connectedPts.find(gridDimen * gridDimen + 1);
        return rootParent == leafParent;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(2);

        p.checkOutput(2, 1);
        p.open(2, 1);
        p.checkOutput(2, 1);
        System.out.println("openSitesCount " + p.numberOfOpenSites());

        p.checkOutput(1, 1);
        p.open(1, 1);
        p.checkOutput(1, 1);
        System.out.println("openSitesCount " + p.numberOfOpenSites());
    }

    private void checkOutput(int row, int col) {
        System.out.println(String.format("isOpen: %b, isFull: %b", isOpen(row, col), isFull(row, col)));
    }

    private boolean isValidRowCol(int row, int col) {
        return row > 0 && col > 0 && row <= gridDimen && col <= gridDimen;
    }

    private int getIndex(int row, int col) {
        if (!isValidRowCol(row, col)) {
            throw new IllegalArgumentException(String.format("Could not getIndex for row %d and col: %d", row, col));
        }

        return (row - 1) * gridDimen + col;
    }
}