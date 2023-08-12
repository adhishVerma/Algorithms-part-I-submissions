/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int sz;
    private int openSites = 0;
    private WeightedQuickUnionUF uf;
    private int top;
    private int bottom;

    // creates an n-by-n grid with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        grid = new boolean[n][n];
        sz = n;
        uf = new WeightedQuickUnionUF((int) Math.pow(sz, 2) + 2);
        top = 0;
        bottom = (int) (Math.pow(sz, 2) + 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // checking if the row and col are valid
        if (!validateIndex(row, col)) {
            throw new IllegalArgumentException();
        }

        // we open the site if not already open
        if (!isOpen(row, col)) {
            // since the row and col are 1 indexed we reduce them.
            int gridRow = row - 1;
            int gridCol = col - 1;
            //
            grid[gridRow][gridCol] = true;
            openSites++;
            //     we have to mark connected now
            if (row == 1) {
                uf.union(top, col);
                if (isOpen(row + 1, col)) {
                    uf.union(sz + col, sz * (row) + col);
                }
            }
            else if (row == sz) {
                // connect with above it after checking if above is open
                if (isOpen(row - 1, col)) {
                    uf.union(sz * (row - 1) + col, sz * (row - 2) + col);
                }
                uf.union(bottom, (row - 1) * sz + col);
            }
            else {
                // connect with above it after checking if above is open
                if (isOpen(row - 1, col)) {
                    uf.union(sz * (row - 1) + col, sz * (row - 2) + col);
                }

                // connect with below it.
                if (isOpen(row + 1, col)) {
                    uf.union(sz * (row - 1) + col, sz * (row) + col);
                }
            }

            if (col == 1) {
                //     only connect right
                if (isOpen(row, col + 1)) {
                    uf.union(sz * (row - 1) + col, sz * (row - 1) + col + 1);
                }
            }
            else if (col == sz) {
                // connect left
                if (isOpen(row, col - 1)) {
                    uf.union(sz * (row - 1) + col, sz * (row - 1) + col - 1);
                }
            }
            else {
                //     connect both left and right
                if (isOpen(row, col - 1)) {
                    uf.union(sz * (row - 1) + col, sz * (row - 1) + col - 1);
                }
                if (isOpen(row, col + 1)) {
                    uf.union(sz * (row - 1) + col, sz * (row - 1) + col + 1);
                }
            }
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!validateIndex(row, col)) {
            throw new IllegalArgumentException();
        }
        row--;
        col--;
        return (grid[row][col]);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!validateIndex(row, col)) {
            throw new IllegalArgumentException();
        }
        // full means connected to the top
        return uf.find(top) == uf.find(sz * (row - 1) + col);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        //     find bottom and top connection
        return uf.find(bottom) == uf.find(top);
    }

    private boolean validateIndex(int row, int col) {
        if (1 > row || row > sz || col > sz || 1 > col) {
            return false;
        }
        return true;
    }

}
