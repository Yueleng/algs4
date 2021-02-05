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

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> segmentsList = new ArrayList<>();
    public FastCollinearPoints(Point[] points) {
        checkPoints(points);

        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy); // Now pointsCopy are sorted by compareTo method.
        if (pointsCopy.length >= 4) {
            for (int p = 0; p < pointsCopy.length - 3; p++) {
                Point[] pointsBySlope = pointsCopy.clone();

                // now pointsBySlope are sorted by pointsCopy[p].slopeOrder()
                Arrays.sort(pointsBySlope, pointsCopy[p].slopeOrder());

                // candidates which may be put into `segmentsList`
                ArrayList<Point> lineSegPoints = new ArrayList<>(); // temp stored points

                // q starts from 1, since pointsCopy[p] will be positioned at the first one of pointsBySlope
                // by the fact that pointsCopy[p].slopeTo(pointsCopy[p]) == Double.NEGATIVE_INFINITY
                for (int q = 1; q < pointsBySlope.length;) { /* q++ was performed in do-while loop */
                    double curSlope = pointsCopy[p].slopeTo(pointsBySlope[q]);
                    do {
                        lineSegPoints.add(pointsBySlope[q]);
                        q++;
                    } while (q < pointsBySlope.length && pointsCopy[p].slopeTo(pointsBySlope[q]) == curSlope);

                    // Q: Why the lineSegPoints.get(0) will return the smallest point in the lineSegPoints
                    //      other than pointsCopy[p]?
                    // A: Because pointsCopy are sorted by compareTo method, and sort again by pointsCopy[p].slopeOrder()
                    //      The second sort will keep the first sort order in case of same order in second sort, i.e. sort stability!
                    if (lineSegPoints.size() >= 3 && pointsCopy[p].compareTo(lineSegPoints.get(0)) < 0) {
                        segmentsList.add(
                            new LineSegment(
                                pointsCopy[p],
                                lineSegPoints.get(lineSegPoints.size() - 1)
                            )
                        );
                    }

                    // clear lineSegPoints
                    lineSegPoints.clear();
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segmentsList.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segmentsList.toArray(new LineSegment[segmentsList.size()]);
        // return (LineSegment[]) segmentsList.toArray();
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
