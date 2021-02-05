package com.alanwang;

import edu.princeton.cs.algs4.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BaseballElimination {
//    private final String[] teams;
//    private final Map<String, Integer> teamPos = new HashMap<>();
//    private final Map<String, Integer> wins = new HashMap<>();
//    private final Map<String, Integer> losses = new HashMap<>();
//    private final Map<String, Integer> remaining = new HashMap<>();
//    private final Map<String, int[]> schedule = new HashMap<>();
//    private final boolean[] isEliminated;
//    private final Map<String, String[]> certificateOfElimination = new HashMap<>();
//
//
//
//    public BaseballElimination(String filename) {
////        5
////        New_York    75 59 28   0 3 8 7 3
////        Baltimore   71 63 28   3 0 2 7 7
////        Boston      69 66 27   8 2 0 0 3
////        Toronto     63 72 27   7 7 0 0 3
////        Detroit     49 86 27   3 7 3 3 0
//
//        // Problem Initialization
//        In in = new In(filename);
//        int n = Integer.parseInt(in.readLine());
//        teams = new String[n];
//        for (int i = 0; i < n; i++) {
//            String[] input = in.readLine().split(" ");
//            teams[i] = input[0];
//            teamPos.put(teams[i], i);
//            wins.put(teams[i], Integer.parseInt(input[1]));
//            losses.put(teams[i], Integer.parseInt(input[2]));
//            remaining.put(teams[i], Integer.parseInt(input[3]));
//            int[] scheduleOfI = new int[n];
//            for (int j = 0; j < n; j++) {
//                scheduleOfI[j] = Integer.parseInt(input[4 + j]);
//            }
//            schedule.put(teams[i], scheduleOfI);
//        }
//
//
//        // Compute the elimination status for every team
//        isEliminated = new boolean[n];
//        for (int x = 0; x < n; x++) {
//            // Trivial Elimination
//            // w[x] + r[x] < w[i]
//            // w[x]: wins[teams[x]]; r[x]: remaining[teams[x]];
//            for (int i = 0; i < n; i++) {
//                if (wins.get(teams[x]) + remaining.get(teams[x]) < wins.get(teams[i])) {
//                    isEliminated[x] = true;
//                    certificateOfElimination.put(teams[x], new String[] {teams[i]});
//                    break;
//                }
//            }
//
//            // If trivial elimination passed, the following is Nontrivial Elimination
//
//            FlowNetwork virtualNetWork = new FlowNetwork(1 + *(n-1) );
//            for (int i = 0; i < n; i++) {
//                if (x != i) {
//
//                }
//            }
//            FordFulkerson
//        }
//
//
//
//    }
//
//    public int numberOfTeams() {
//        return teams.length;
//    }
//
//    public Iterable<String> teams() {
//        return Arrays.asList(teams);
//    }
//
//    public int losses(String team) {
//        return losses.get(team);
//    }
//
//    public int remaining(String team) {
//        return remaining.get(team);
//    }
//
//    public int against(String team1, String team2) {
//        return schedule.get(team1)[teamPos.get(team2)];
//    }
//
//    public boolean isEliminated(String team) {
//        return isEliminated[teamPos.get(team)];
//    }
//
//    public Iterable<String> certificateOfElimination(String team) {
//        if (certificateOfElimination.get(team).length > 0)
//            return Arrays.asList(certificateOfElimination.get(team));
//        return null;
//    }
//
//    public static void main(String[] args) {
//        BaseballElimination division = new BaseballElimination(args[0]);
//        for (String team: division.teams()){
//            if (division.isEliminated(team)) {
//                StdOut.print(team + " is eliminated by the subset R = { ");
//                for (String t: division.certificateOfElimination(team)) {
//                    StdOut.print(t + " ");
//                }
//                StdOut.println("}");
//            } else {
//                StdOut.print(team + " is not eliminated");
//            }
//
//        }
//    }
}
