/**
 * Week 3 Assignment: Sort
 *
 * Assignment url: https://coursera.cs.princeton.edu/algs4/assignments/collinear/specification.php
 *
 * Notice:
 * Comment the package statement for submission!
 *
 * Score:  / 100
 *
 * Defect:
 *
 * Possible Improvements:
 *
 * Key part of this assignment:
 *  For BruteCollinearPoints.java
 *    *) Since we don't know (in advance) that the length of `segments`.
 *       Use LinkedList<LineSegment> or ArrayList<LineSegment> instead and
 *         call .Array() to return LineSegment[]
 *    *) four nested loop: j = i + 1, instead of j = i, same for k and l
 *    *) use epsilon to determine whether slopes are the same, instead of `==`
 *    *) Should consider the case when slope is Double.POSITIVE_INFINITY
 *    *) Use HashSet to check duplicates is not correct,
 *       since two points may be not the same object, but could also be duplicates
 *    *) Set the epsilon to be as small as possible.
 *
 *  For Point.java
 *    *) `this` in the `BySlopeOrder` comparator
 *    *) In the slopeTo method, you have to specify 0.0 when that.x != this.x while that.y == this.y
 *
 *  For FastCollinearPoints.java
 *    *) HashSet is not allowed, thus we have to use Sort and the stability of sort
 *    *) Realize the importance of the fact that pointsCopy[p].slopeTo(pointsCopy[p]) == Double.NEGATIVE_INFINITY
 *    *) Realize the importance of sort stability
 *    *) A way to transform ArrayList/LinkedList to Array without any warning
 *
 */

package com.alanwang;

/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        // The slopeTo() method should return the slope between the invoking point (x0, y0)
        // and the argument point (x1, y1), which is given by the formula (y1 − y0) / (x1 − x0).
        // Treat the slope of a horizontal line segment as positive zero;
        // treat the slope of a vertical line segment as positive infinity;
        // treat the slope of a degenerate line segment (between a point and itself) as negative infinity.
        if (this.x != that.x) {
            if (this.y == that.y)
                return +0.0;
            return ((double) (that.y - this.y)) / (that.x - this.x);
        }

        else {
            // this.x == that.x
            if (this.y == that.y) return Double.NEGATIVE_INFINITY;
            else return Double.POSITIVE_INFINITY;
        }
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        // The compareTo() method should compare points by their y-coordinates,
        // breaking ties by their x-coordinates.
        // Formally, the invoking point (x0, y0) is less than the argument point (x1, y1)
        // if and only if either y0 < y1 or if y0 = y1 and x0 < x1.

        if (this.y - that.y < 0)
            return -1;
        else if (this.y - that.y > 0)
            return 1;
        else {
            if (this.x - that.x < 0)
                return -1;
            else if (this.x - that.x > 0)
                return 1;
            else return 0;
        }
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
      return new BySlopeOrder();
    }

    private class BySlopeOrder implements Comparator<Point> {
        // The slopeOrder() method should return a comparator that compares its two argument points by the slopes
        // they make with the invoking point (x0, y0).
        // Formally, the point (x1, y1) is less than the point (x2, y2) if and only if
        // the slope (y1 − y0) / (x1 − x0) is less than the slope (y2 − y0) / (x2 − x0).
        // Treat horizontal, vertical, and degenerate line segments as in the slopeTo() method.
        public int compare(Point q1, Point q2) {
            double qSlopeToQ1 = Point.this.slopeTo(q1); // or just slopeTo(q1)
            double qSlopeToQ2 = Point.this.slopeTo(q2); // or just slopeTo(q2)

            if (qSlopeToQ1 == qSlopeToQ2) return 0;
            if (qSlopeToQ2 == Double.POSITIVE_INFINITY) return -1;
            return qSlopeToQ1 - qSlopeToQ2 < 0.0 ? -1 : 1;
        }
    }

    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
    }
}