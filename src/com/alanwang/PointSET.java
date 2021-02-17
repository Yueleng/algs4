package com.alanwang;

import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> points;
    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // return the size of tree
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkNull(p);
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkNull(p);
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        List<Point2D> temp = new LinkedList<>();
        for (Point2D point : points)
            if (rect.contains(point))
                temp.add(point);
        return temp;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkNull(p);
        double nearestDist = Double.POSITIVE_INFINITY;
        Point2D nearest = null;
        for (Point2D point: points)
            if (nearestDist > p.distanceTo(point)) {
                nearestDist = p.distanceTo(point);
                nearest = point;
            }
        return nearest;
    }

    private void checkNull(Object p) {
        if (p == null)
            throw new IllegalArgumentException("point input null");
    }

//    // unit testing of the methods (optional)
//    public static void main(String[] args) {
//
//    }

}
