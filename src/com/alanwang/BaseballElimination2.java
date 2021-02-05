package com.alanwang;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;
import java.util.Arrays;

public class BaseballElimination2 {
    private static final double EPSILON = 0.00001;
    private final int num; // number of teams
    private final String[] teams; // team name array
    private final int[] wins;  // current wins of each team by index
    private final int[] losses;  // current losses of each team by index
    private final int[] remainings; // current remaining games of each team by index
    private final int[][] g;  // schedule matrix
    private final HashMap<String, Integer> teamToIndex; // Team String => Team Index
    private final HashMap<Integer, String> indexToTeam;  // Team Index => Team String
    private final HashMap<String, Bag<String>> certificates; // Certificates of each team, if not eliminated then null
    private int maxWin; // current max wins from file input
    private String maxWinTeam; // current max wins team from file input

    // create a baseball division from given filename in format specified below
    public BaseballElimination2(String filename) {
        // check file name, if null throw exception.
        if (filename == null) {
            throw new IllegalArgumentException("Filename is null.");
        }

        // private parameters initialization:
        //   num, teams, wins, losses, remainings, g,
        //   teamToIndex, indexToTeam, certificates, maxWin, maxWinTeam
        In in = new In(filename);
        num = in.readInt();
        teams = new String[num];
        wins = new int[num];
        losses = new int[num];
        remainings = new int[num];
        g = new int[num][num];
        teamToIndex = new HashMap<String, Integer>();
        indexToTeam = new HashMap<Integer, String>();
        certificates = new HashMap<String, Bag<String>>();
        maxWin = 0;
        maxWinTeam = null;

        // fill in values into teamToIndex, indexToTeam, wins, maxWin, maxWinTeam,
        //   losses, remainings, and g (schedule matrix)
        for (int i = 0; i < num; ++i) {
            teams[i] = in.readString();
            teamToIndex.put(teams[i], i);
            indexToTeam.put(i, teams[i]);
            wins[i] = in.readInt();
            if (wins[i] > maxWin) {
                maxWin = wins[i];
                maxWinTeam = teams[i];
            }
            losses[i] = in.readInt();
            remainings[i] = in.readInt();
            for (int j = 0; j < num; ++j) {
                g[i][j] = in.readInt();
            }
        }
    }

    // return number of teams
    public int numberOfTeams() {
        return num;
    }

    // return teams as Iterable<String>
    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }

    // return number of wins for given team
    // transform team to index, then get win from wins[]
    public int wins(String team) {
        if (team == null || teamToIndex.get(team) == null) {
            throw new IllegalArgumentException("team is invalid.");
        }
        return wins[teamToIndex.get(team)];
    }

    // number of losses for given team
    // transform team to index, then get losses from losses[]
    public int losses(String team) {
        if (team == null || teamToIndex.get(team) == null) {
            throw new IllegalArgumentException("team is invalid.");
        }
        return losses[teamToIndex.get(team)];
    }

    // number of remaining games for given team
    // transform team to index, then get remainings from remainings[]
    public int remaining(String team) {
        if (team == null || teamToIndex.get(team) == null) {
            throw new IllegalArgumentException("team is invalid.");
        }
        return remainings[teamToIndex.get(team)];
    }

    // number of remaining games between team1 and team2
    // transform team1 to index1, team2 to index2. then get g[index1][index2] from g
    public int against(String team1, String team2) {
        if (team1 == null || team2 == null ||
                teamToIndex.get(team1) == null || teamToIndex.get(team2) == null) {
            throw new IllegalArgumentException("team is invalid.");
        }
        return g[teamToIndex.get(team1)][teamToIndex.get(team2)];
    }

    // is given team eliminated?
    // if certificates Map does not contain the input team
    //   do the calculation and save to certificates map
    // return the certificates.get(team) != null to determine eliminated!
    // avoid creating redundant isEliminated[] array. Good idea.
    public boolean isEliminated(String team) {
        if (team == null || teamToIndex.get(team) == null) {
            throw new IllegalArgumentException("team is invalid.");
        }

        if (!certificates.containsKey(team)) {
            checkEliminated(team);
        }

        return certificates.get(team) != null;
    }

    // [Core Codes]
    // Create FlowNetwork by the index parameter.
    // the key here is to place source index and sink index as last two indices
    private FlowNetwork createFlowNetwork(int index) {
        int numGames = num * (num - 1) / 2; // number of games: num chooses 2
        int numV = numGames + num + 2;  // number of Vertices in the FlowNetwork: numGames + num of teams + 1 Source + 1 Sink
        int s = numV - 2; // Source index
        int t = numV - 1; // Sink index

        int sum = wins[index] + remainings[index]; // Get possible wins for Team with index
        FlowNetwork fn = new FlowNetwork(numV); // Create empty FlowNetwork of num of vertices: numV

        // Create FlowEdge connecting sink and team vertices
        // the capacity = wins[Index] + remainings[Index] - wins[i]
        for (int i = 0; i < num; ++i) {
            fn.addEdge(new FlowEdge(i, t, sum - wins[i]));
        }

        // Create FlowEdge connection source to game vertices,
        //             also connects each game to corresponding two teams
        for (int i = 0, v = num; i < num; ++i) {
            for (int j = i + 1; j < num; ++j) {
                fn.addEdge(new FlowEdge(s, v, g[i][j]));
                fn.addEdge(new FlowEdge(v, i, Double.POSITIVE_INFINITY));
                fn.addEdge(new FlowEdge(v, j, Double.POSITIVE_INFINITY));
                ++v;
            }
        }

        return fn;
    }

    // [Core Codes]
    //   Check any FlowEdge from s is full
    //   if any edge is not full then return false
    //   if all edges are full, return true
    private boolean isFullFromSource(FlowNetwork fn, int s) {
        for (FlowEdge fe : fn.adj(s)) {
            if (Math.abs(fe.flow() - fe.capacity()) > EPSILON) {
                return false;
            }
        }
        return true;
    }

    // [Core Codes]
    // check team is eliminated mathematically
    // put the result into certificates
    private void checkEliminated(String team) {
        int index = teamToIndex.get(team);
        // Trivial Elimination
        if (wins[index] + remainings[index] < maxWin) {
            Bag<String> b = new Bag<String>();
            b.add(maxWinTeam);
            certificates.put(team, b);
        }
        // Non Trivial Elimination
        else {
            FlowNetwork fn = createFlowNetwork(index);
            int s = fn.V() - 2; // source index: V() - 2
            int t = fn.V() - 1; // source index: V() - 1
            FordFulkerson ff = new FordFulkerson(fn, s, t);

            // if all FlowEdges from source s all full after FordFulkerson Algorithm
            //   certificate for this team is null
            if (isFullFromSource(fn, s)) {
                certificates.put(team, null);
            }

            // if any of the FlowEdges are not full
            //   team is mathematically eliminated
            //   use inCut to create the cut bag and put this info into certificate as certificates.put(team, b)
            else {
                Bag<String> b = new Bag<>();
                for (int i = 0; i < num; ++i) {
                    if (ff.inCut(i)) {
                        b.add(indexToTeam.get(i));
                    }
                }
                certificates.put(team, b);
            }
        }
    }


    // subset R of teams that eliminates given team; null if not eliminated
    // the same as isEliminated method except the return expression.
    public Iterable<String> certificateOfElimination(String team) {
        if (team == null || teamToIndex.get(team) == null) {
            throw new IllegalArgumentException("team is invalid.");
        }

        if (!certificates.containsKey(team)) {
            checkEliminated(team);
        }

        return certificates.get(team);
    }

    public static void main(String[] args) {
        BaseballElimination2 division = new BaseballElimination2(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
