package com.alanwang;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrincetonWebCrawler {
    private Queue<String> queue = new Queue<>();
    SET<String> marked = new SET<>();

    public PrincetonWebCrawler() {
        String root = "http://www.princeton.edu";
        queue.enqueue(root);
        marked.add(root);

        while(!queue.isEmpty()) {
            String v = queue.dequeue();
            StdOut.println(v);
            In in = new In(v);
            String input = in.readAll();

            String regexp = "http://(\\w+\\.)*(\\w+)";
            Pattern pattern = Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                String w = matcher.group();
                if (!marked.contains(w)) {
                    marked.add(w);
                    queue.enqueue(w);
                }
            }
        }
    }

    public static void main(String[] args) {
        PrincetonWebCrawler crawler = new PrincetonWebCrawler();
        for (String s: crawler.marked)
            StdOut.println(s); // print all web urls.

    }
}
