package week1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double mean;
    private double stdDev;
    private final int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        double[] percolationThresholds = new double[trials];
        this.trials = trials;
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            int sitesToOpen = 0;
            while (!p.percolates()) {
                int row = (int) Math.ceil(StdRandom.uniform(n) + 1);
                int col = (int) Math.ceil(StdRandom.uniform(n) + 1);
                if (p.isOpen(row, col)) {
                    continue;
                }
                sitesToOpen++;
                p.open(row, col);
            }

            percolationThresholds[i] = sitesToOpen * 1.0 / (n * n);
        }

        mean = StdStats.mean(percolationThresholds); // Arrays.stream(percolationThresholds).sum() * 1.0 / trials;
        double stdSum = StdStats.stddev(percolationThresholds); // Arrays.stream(percolationThresholds).map((xi) -> Math.pow(xi - mean, 2)).sum();
        stdDev = stdSum * 1.0 / (trials - 1);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stdDev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - (1.96) * stdDev / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + (1.96) * stdDev / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(20, 400);
        System.out.println(String.valueOf(ps.mean()) + " " + String.valueOf(ps.stddev()));
    }

}