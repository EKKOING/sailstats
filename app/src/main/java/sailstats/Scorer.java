package main.java.sailstats;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Arrays;

public class Scorer {
    private String[][] results;
    private int numDrops = 0;

    public Scorer(String[][] results) {
        this.results = results;
    }

    public Hashtable<Integer, String[]> calculatePointTotals() {
        Hashtable<Integer, String[]> totals = new Hashtable<>(0);
        for (String[] line : results) {
            String[] scores = Arrays.copyOfRange(line, 1, line.length - 1);
            int[] drops = new int[numDrops];
            Arrays.fill(drops, 0);
            int total = 0;
            for (String score : scores) {
                int scoreNum = Integer.parseInt(score);
                total += scoreNum;
                if (scoreNum > drops[0]) {
                    drops[0] = scoreNum;
                    Arrays.sort(drops);
                }
            }
            for (int drop : drops) {
                total -= drop;
            }
            totals.put(total, line);
        }
        return totals;
    }

    private int numOfRaces() {
        return results[0].length - 1;
    }

    public String[][] getPlacements() {
        Hashtable<Integer, String[]> totals = calculatePointTotals();
        int[] finishes = new int[totals.size()];
        int i = 0;
        for (int total : totals.keySet()) {
            finishes[i] = total;
            i++;
        }
        Arrays.sort(finishes);
        String[][] table = new String[finishes.length][numOfRaces() + 3];
        i = 0;
        for (int finish : finishes) {
            table[i][1] = String.valueOf(i + 1);
            table[i][2] = String.valueOf(finish);
            table[i][0] = totals.get(finish)[0];
            int j = 3;
            for (String score : Arrays.copyOfRange(totals.get(finish), 1, numOfRaces() - 1)) {
                table[i][j] = score;
            }
        }
        return table;
    }
}
