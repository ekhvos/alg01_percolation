package com.ekhvos;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Data structure for percolation simulation statistics.
 * <p>
 * Created by ekhvos.
 */
public class PercolationStats {
    private static final double COEFFICIENT = 1.96;
    private final int n;
    private final int trials;
    private final double[] fractions;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Out of range");
        }
        this.n = n;
        this.trials = trials;
        fractions = new double[trials];

        runSimulatons();
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(fractions);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(fractions);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (COEFFICIENT * stddev() / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(trials));
    }

    private void runSimulatons() {
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            simulate(percolation);

            fractions[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
    }

    private void simulate(Percolation percolation) {
        while (!percolation.percolates()) {
            percolation.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
        }
    }

    /**
     * Produce statistics output.
     *
     * @param args the args
     */
    public static void main(String[] args) {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("Not enough data to compute");
        }

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);

        System.out.println("mean\t\t\t\t\t\t= " + stats.mean());
        System.out.println("stddev\t\t\t\t\t\t= " + stats.stddev());
        System.out.println("95% confidence interval\t\t= [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
