package com.alanwang;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.LinkedList;
import java.util.List;

public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;

    private Node root;
    private int size;

    private static class Node {
        private final boolean separateDir;
        private final RectHV rect; // why we need this one?
        private final Point2D p;
        private Node left; // left or below
        private Node right; // right or above

        Node(Point2D p, boolean separateDir, RectHV rect) {
            this.p = p;
            this.separateDir = separateDir;
            this.rect = rect;
        }

        public boolean nextSeparateDir() {
            return !this.separateDir;
        }

        // left or below rectangle
        public RectHV rectLB() {
            return separateDir == VERTICAL
                    ? new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax())
                    : new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
        }

        // right or above rectangle
        public RectHV rectRT() {
            return separateDir == VERTICAL
                    ? new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax())
                    : new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
        }

        // determine the new point is on the left or below of current node
        // right node must be strictly greater w.r.t. current separator
        public boolean isChildOnTheLeft(Point2D q) {
            return (this.separateDir == HORIZONTAL) ? p.y() > q.y()
                    : p.x() > q.x();
        }

        // only for get full points
        public boolean isChildOnTheLeft(double x, double y) {
            return (this.separateDir == HORIZONTAL) ? p.y() > y
                    : p.x() > x;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the KdTree empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the size of tree
    public int size() {
        return this.size;
    }

    // add the point to the KdTree (if it is not already in the set)
    public void insert(Point2D point) {
        checkNull(point);
        if (root == null) {
            root = new Node(point, VERTICAL, new RectHV(0, 0, 1,1));
            size++;
            return;
        }

        // find node position for insertion
        Node prev = null;
        Node curr = root;

        while (curr != null) {
            if (curr.p.equals(point))
                return;
            prev = curr;
            curr = curr.isChildOnTheLeft(point) ? curr.left : curr.right;
        }

        // curr == null, assign new Node
        boolean isCurrLeft = prev.isChildOnTheLeft(point);
        curr = new Node(point, prev.nextSeparateDir(), isCurrLeft ? prev.rectLB(): prev.rectRT());
        if (isCurrLeft)
            prev.left = curr;
        else
            prev.right = curr;

        size++;
    }

    // does the set contain point p?
    public boolean contains(Point2D point) {
        checkNull(point);
        Node node = root;
        while (node != null) {
            if (node.p.equals(point))
                return true;
            node = node.isChildOnTheLeft(point) ? node.left : node.right;
        }
        return false;
    }



    // draw all points to standard draw
    public void draw() {
        LinkedList<Node> pointsCollect = new LinkedList<>();
        pointsCollect.add(root);
        while (pointsCollect.size() > 0) {
            Node node = pointsCollect.removeFirst();
            node.p.draw();
            if (node.left != null) pointsCollect.add(node.left);
            if (node.right != null) pointsCollect.add(node.right);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        List<Point2D> results = new LinkedList<>();
        addAll(root, rect, results);
        return results;
    }

    private void addAll(Node node, RectHV rect, List<Point2D> results) {
        if (node == null) {
            return;
        }

        if (rect.contains(node.p)) {
            results.add(node.p);
            addAll(node.left, rect, results);
            addAll(node.right, rect, results);
            return;
        }

        if (node.isChildOnTheLeft(rect.xmin(), rect.ymin()))
            addAll(node.left, rect, results);


        if (!node.isChildOnTheLeft(rect.xmax(),rect.ymax()))
            addAll(node.right, rect, results);

    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkNull(p);
        return isEmpty() ? null : nearest(p, root.p, root);
    }

    private Point2D nearest(Point2D target, Point2D closest, Node node) {
        if (node == null) {
            return closest;
        }

        // recursively search left/bottom or right/top: DFS
        // every call of this function includes the current closest point to the target.
        double closestDist = closest.distanceTo(target);

        // if the closest point in the rect (i.e. node.rect.distanceTo(target))
        //    is greater or equal to the current closestDist, just return
        if (node.rect.distanceTo(target) < closestDist) {
            double nodeDist = node.p.distanceTo(target);
            if (nodeDist < closestDist)
                closest = node.p;

            // if the target is the current node's left, search for left
            //    else search for right.
            //    i.e. search for the child node on the same side with target by the cutting of current node.
            if (node.isChildOnTheLeft(target)) {
                closest = nearest(target, closest, node.left);
                closest = nearest(target, closest, node.right);
            } else {
                closest = nearest(target, closest, node.right);
                closest = nearest(target, closest, node.left);
            }
        }
        return closest;
    }

    private void checkNull(Object p) {
        if (p == null)
            throw new IllegalArgumentException("point input null");
    }

    /**
     * Unit testing of the methods (optional)
     */
//    public static void main(String[] args) {
//    }
}
