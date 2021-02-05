/**
 * Week 3 Assignment: Sort/Collinear Points
 *
 * Assignment url: https://coursera.cs.princeton.edu/algs4/assignments/collinear/specification.php
 *
 * Notice:
 * Comment the package statement for submission!
 *
 * Score: 100 / 100
 *
 * Defect:
 *   Test 2a-2e: Find collinear points among n/4 arbitrary line segments
 *
 *
 *                                                       slopeTo()
 *              n    time     slopeTo()   compare()  + 2*compare()        compareTo()
 * -----------------------------------------------------------------------------------------------
 * => passed    16   0.00        5460           0           5460                 9791
 * => passed    32   0.02      107880           0         107880               195248
 * => passed    64   0.05     1906128           0        1906128              3392212
 * => passed   128   0.68    32004000           0       32004000             56541776
 * => passed   256  11.27   524377920           0      524377920            928616975
 * Aborting: time limit of 10 seconds exceeded
 * Total: 9/10 tests passed!
 *
 * Possible Improvements:
 *   *) Why the small value of epsilon will avoid duplicate segments in BruteCollinearPoints.java?
 *   *) Why use (int i = 0; i < points.length; p++) to check points null is not correct?
 *      Instead we should use `for (Point p: points)`
 *
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
 */


package com.alanwang;

import java.util.Arrays;
import java.util.LinkedList;



public class BruteCollinearPoints {
    // finds all line segments containing 4 points
    private final LineSegment[] segments;
    private final static double epsilon = 0.00000001;
    public BruteCollinearPoints(Point[] points) {
        checkPoints(points);
        LinkedList<LineSegment> segmentsList = new LinkedList<>();
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        double slopeITOJ = points[i].slopeTo(points[j]);
                        double slopeJTOK = points[j].slopeTo(points[k]);
                        double slopeKTOL = points[k].slopeTo(points[l]);
                        Point[] fourPoints = new Point[4];
                        fourPoints[0] = points[i];
                        fourPoints[1] = points[j];
                        fourPoints[2] = points[k];
                        fourPoints[3] = points[l];
                        Arrays.sort(fourPoints);
                        LineSegment segment = new LineSegment(fourPoints[0], fourPoints[3]);
                        if (Math.abs(slopeITOJ - slopeJTOK) < epsilon && Math.abs(slopeJTOK - slopeKTOL) < epsilon) {
                            segmentsList.add(segment);
                        } else if (slopeITOJ == Double.POSITIVE_INFINITY
                                && slopeJTOK == Double.POSITIVE_INFINITY
                                && slopeKTOL == Double.POSITIVE_INFINITY) {
                            segmentsList.add(segment);
                        }
                    }
                }
            }
        }
        segments = segmentsList.toArray(new LineSegment[segmentsList.size()]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }

    private void checkPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("points null");

        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("point null");
            }

        }
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("Duplicate points found.");
            }
        }
    }
}
