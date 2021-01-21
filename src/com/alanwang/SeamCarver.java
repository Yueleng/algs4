package com.alanwang;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Topological;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Week 8 Assignment: Seam Carving
 *
 * Assignment url: https://coursera.cs.princeton.edu/algs4/assignments/seam/specification.php
 *
 * Notice:
 * Comment the package statement for submission!
 *
 * Score: 100 / 100
 *
 * Defect:
 *
 * Possible Improvements:
 *
 * Key part of this assignment:
 *    *) Math.pow(a, power), don't use ()^2
 *
 *
 */

public class SeamCarver {
    private final static Double BORDER_ENERGY = 1000.0;
    private Picture picture;
    private EdgeWeightedDigraph G;
    private DirectedEdge[] edgeTo;
    private double[] distTo;
    private double[][] energy;
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("constructor parameter: picture is null");
        this.picture = picture;
        this.energy = new double[this.picture.height()][this.picture.width()];
    }

    // current picture
    public Picture picture() {
        Picture pic = new Picture(width(), height());
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                pic.set(j, i, new Color(picture.get(j, i).getRGB()));
            }
        }
        return new Picture(pic);
    }

    // width of current picture
    public int width() {
        return this.picture.width();
    }

    // height of current picture
    public int height() {
        return this.picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        // x: col, y: row
        if (x < 0 || x > width() -1 || y < 0 || y > height() - 1)
            throw new IllegalArgumentException("(x,y) out of range");

        // Edge case: first row, last row, first column, last column;
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return BORDER_ENERGY;

        // General case: sqrt(delta^2_x(x,y) + delta^2_y(x,y))
        //   where delta^2_x(x,y) = R^2_x(x,y) + G^2_x(x,y) + B^2_x(x,y)
        // Color pixel = picture.get(x, y);
        Color pixelLeft = picture.get(x-1,y);
        Color pixelRight = picture.get(x+1,y);

        double R2x = Math.pow(pixelLeft.getRed() - pixelRight.getRed(), 2);
        double G2x = Math.pow(pixelLeft.getGreen() - pixelRight.getGreen(), 2);
        double B2x = Math.pow(pixelLeft.getBlue() - pixelRight.getBlue(), 2);

        Color pixelAbove = picture.get(x, y - 1);
        Color pixelDown = picture.get(x, y + 1);

        double R2y = Math.pow(pixelAbove.getRed() - pixelDown.getRed(), 2);
        double G2y = Math.pow(pixelAbove.getGreen() - pixelDown.getGreen(), 2);
        double B2y = Math.pow(pixelAbove.getBlue() - pixelDown.getBlue(), 2);

        return Math.sqrt(R2x + G2x + B2x + R2y + G2y + B2y);
    }

    // sequence of indices for horizontal seam;
    public int[] findHorizontalSeam() {
        constructLeftToRightEdgeWeightedDigraph();
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];

        // Initialize distance
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[0] = 0.0;

        Topological topological = new Topological(G);
        for (int v: topological.order())
            for (DirectedEdge e: G.adj(v))
                relax(e);


        // return path
        int v = width() * height() + 1;
        int[] horizontalSeam = new int[width()];
        while (edgeTo[v] != null) {
            DirectedEdge e = edgeTo[v];
            if (e.from() == 0) break;
            int[] from2DPoint = toRowCol(e.from());
            horizontalSeam[from2DPoint[1]] = from2DPoint[0];
            v = e.from();
        }
        return horizontalSeam;
    }

    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
        }
    }

    public int[] findVerticalSeam() {
        constructTopToBottomEdgeWeightedDigraph();
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];

        // Initialize distance
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[0] = 0.0;

        Topological topological = new Topological(G);
        for (int v: topological.order())
            for (DirectedEdge e: G.adj(v))
                relax(e);

        // return path
        int v = width() * height() + 1;
        int[] verticalSeam = new int[height()];
        while (edgeTo[v] != null) {
            DirectedEdge e = edgeTo[v];
            if (e.from() == 0) break;
            int[] from2DPoint = toRowCol(e.from());
            verticalSeam[from2DPoint[0]] = from2DPoint[1];
            v = e.from();
        }
        return verticalSeam;
    }

    private int index(int row, int col) {
        return row * width() + col + 1;
    }

    private int[] toRowCol(int idx) {
        int row = (idx - 1) / width();
        int col = (idx - 1) % width();
        return new int[] {row, col};
    }

    private void constructTopToBottomEdgeWeightedDigraph() {
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(picture.width() * picture.height() + 2);
        calEnergy();
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                if (row == 0) {
                    DirectedEdge e = new DirectedEdge(0, index(row, col), 0);
                    G.addEdge(e);
                }

                if (row == height() - 1) {
                    DirectedEdge e = new DirectedEdge(index(row, col), width() * height() + 1, energy[row][col]);
                    G.addEdge(e);
                } else if (col == 0) {
                    DirectedEdge e1 = new DirectedEdge(index(row, col), index(row+1, col), energy[row][col]);
                    G.addEdge(e1);

                    if (col+1 <= width() - 1) {
                        DirectedEdge e2 = new DirectedEdge(index(row, col), index(row+1, col+1), energy[row][col]);
                        G.addEdge(e2);
                    }
                } else if (col == width() - 1) {
                    DirectedEdge e1 = new DirectedEdge(index(row, col), index(row+1, col-1), energy[row][col]);
                    G.addEdge(e1);
                    DirectedEdge e2 = new DirectedEdge(index(row, col), index(row+1, col), energy[row][col]);
                    G.addEdge(e2);
                } else {
                    // inside with row 0 to row height - 2(inclusive), column 1 to width() - 2(inclusive)
                    DirectedEdge e1 = new DirectedEdge(index(row, col), index(row+1, col-1), energy[row][col]);
                    G.addEdge(e1);
                    DirectedEdge e2 = new DirectedEdge(index(row, col), index(row+1, col), energy[row][col]);
                    G.addEdge(e2);
                    DirectedEdge e3 = new DirectedEdge(index(row, col), index(row+1, col+1), energy[row][col]);
                    G.addEdge(e3);
                }
            }
        }
        this.G = G;
    }

    private void constructLeftToRightEdgeWeightedDigraph() {
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(picture.width() * picture.height() + 2);
        calEnergy();
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                if (col == 0) {
                    DirectedEdge e = new DirectedEdge(0, index(row, col), 0);
                    G.addEdge(e);
                }

                if (col == width() - 1) {
                    DirectedEdge e = new DirectedEdge(index(row, col), width() * height() + 1, energy[row][col]);
                    G.addEdge(e);
                } else if (row == 0) {
                    DirectedEdge e1 = new DirectedEdge(index(row, col), index(row, col+1), energy[row][col]);
                    G.addEdge(e1);

                    if (row+1 <= height() - 1) {
                        DirectedEdge e2 = new DirectedEdge(index(row, col), index(row+1, col+1), energy[row][col]);
                        G.addEdge(e2);
                    }
                } else if (row == height() - 1) {
                    DirectedEdge e1 = new DirectedEdge(index(row, col), index(row-1, col+1), energy[row][col]);
                    G.addEdge(e1);
                    DirectedEdge e2 = new DirectedEdge(index(row, col), index(row, col+1), energy[row][col]);
                    G.addEdge(e2);
                } else {
                    // inside with col 0 to col weihgt-2(inclusive), row 1 to height-2(inclusive)
                    DirectedEdge e1 = new DirectedEdge(index(row, col), index(row-1, col+1), energy[row][col]);
                    G.addEdge(e1);
                    DirectedEdge e2 = new DirectedEdge(index(row, col), index(row, col+1), energy[row][col]);
                    G.addEdge(e2);
                    DirectedEdge e3 = new DirectedEdge(index(row, col), index(row+1, col+1), energy[row][col]);
                    G.addEdge(e3);
                }
            }
        }
        this.G = G;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (height() <= 1) throw new IllegalArgumentException("height() less or equal to 1, no row to remove any more");

        checkSeam(seam, 2);
        Picture newPicture = new Picture(picture.width(), picture.height()-1);
        for (int col = 0; col < width(); col++) {
            int verticalIndex = seam[col];
            for (int row = 0; row < height() - 1; row++) {
                if (verticalIndex > row) newPicture.setRGB(col, row, picture.getRGB(col, row));
                else if (row >= verticalIndex) newPicture.setRGB(col, row, picture.getRGB(col, row+1));
            }
        }
        this.picture = newPicture;
        this.energy = new double[this.picture.height()][this.picture.width()];
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (width() <= 1) throw new IllegalArgumentException("width() less or equal to 1, no column to remove any more");

        checkSeam(seam, 1);
        Picture newPicture = new Picture(picture.width()-1, picture.height());
        for (int row = 0; row < height(); row++) {
            int horizontalIndex = seam[row];
            for (int col = 0; col < width() - 1; col++) {
                if (horizontalIndex > col) newPicture.setRGB(col, row, picture.getRGB(col, row));
                else if (col >= horizontalIndex) newPicture.setRGB(col, row, picture.getRGB(col+1, row));
            }
        }
        this.picture = newPicture;
        this.energy = new double[this.picture.height()][this.picture.width()];
    }

    private void checkSeam(int[] seam, int direction) {
        if (seam == null) throw new IllegalArgumentException("seam is null");

        if (direction == 1) {
            // vertical
            if (seam.length != height()) throw new IllegalArgumentException("Vertical seam length not equal to height()");
            List<Integer> ints = Arrays.asList(convert(seam));
            if (Collections.max(ints) >= width() || Collections.min(ints) <= -1)
                throw new IllegalArgumentException("Max or min out of range");

        } else if (direction == 2) {
            // horizontal
            if (seam.length != width()) throw new IllegalArgumentException("horizontal seam length not equal to width()");

            List<Integer> ints = Arrays.asList(convert(seam));
            if (Collections.max(ints) >= height() || Collections.min(ints) <= -1)
                throw new IllegalArgumentException("Max or min out of range");
        }

        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i+1]) > 1) throw new IllegalArgumentException("two adjacent entries differ by more than 1");
        }
    }

    private Integer[] convert(int[] ints) {
        Integer[] copy = new Integer[ints.length];
        for(int i = 0; i < copy.length; i++) {
            copy[i] = ints[i];
        }
        return copy;
    }

    private void calEnergy() {
        for (int j = 0; j < this.picture.height(); j++) {
            for (int i = 0; i < this.picture.width(); i++) {
                energy[j][i] = this.energy(i, j);
            }
        }
    }

    // unit testing (optional)
//    public static void main(String[] args) {
//
//    }
}
