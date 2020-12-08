package sailstats;
import main.java.sailstats.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class Regatta {
    public final int EVENT_ID;
    public final String EVENT_NAME;
    private ArrayList<Integer> races = new ArrayList<Integer>(0);
    private Hashtable<Integer, String> competitors = new Hashtable<Integer, String>(0);
    
    public Regatta(String name, int id) {
        this.EVENT_NAME = name;
        this.EVENT_ID = id;
    }
    
    public boolean addCompetitor(Sailor sailor, int sailNumber) {
        if (competitors.containsKey(sailNumber)) {
            return false;
        }
        sailor.addRegatta(EVENT_ID);
        competitors.put(sailNumber, sailor.getUsername());
        return true;
    }

    public Race addRace(ArrayList<Integer> results) {
        Race newRace = SailStatsManager.races.addNewRace(EVENT_ID, results);
        races.add(newRace.RACE_ID);
        return newRace;
    }

    public int numOfCompetitors() {
        return competitors.size();
    }

    public int numOfRaces() {
        return races.size();
    }

    public boolean sailNumberRegistered(int num) {
        return competitors.keySet().contains(num);
    }

    public boolean isRegistered(Sailor sailor) {
        return competitors.containsValue(sailor.getUsername());
    }

    public String[][] getResults() {
        String[][] results = new String[numOfCompetitors()][numOfRaces() + 1];
        int competitorNum = 0;
        for (Map.Entry<Integer, String> competitor : competitors.entrySet()) {
            results[competitorNum][0] = competitor.getValue();
            int raceNum = 1;
            for (Integer raceID : races) {
                Race race = SailStatsManager.races.getRace(raceID);
                int score = 0;
                if (race.RESULTS.contains(competitor.getKey())) {
                    score = race.RESULTS.indexOf(competitor.getKey()) + 1;
                } else {
                    score = race.RESULTS.size() + 1;
                }
                results[competitorNum][raceNum] = String.valueOf(score);
                raceNum++;
            }
            competitorNum++;
        }
        return results;
    }

    public String[][] getTotals() {
        String[][] results = getResults();
        String[][] totaledResults = new String[results.length][results[0].length + 1];
        int lineNum = 0;
        for (String[] line : results) {
            String username = line[0];
            String[] scores = Arrays.copyOfRange(line, 1, line.length);
            int total = 0;
            int raceNum = 1;
            for (String score : scores) {
                int scorenum = Integer.parseInt(score);
                total += scorenum;
                totaledResults[lineNum][raceNum + 1] = score;
                raceNum++;
            }
            totaledResults[lineNum][0] = username;
            totaledResults[lineNum][1] = String.valueOf(total);
            lineNum++;
        }
        Arrays.sort(totaledResults, (a, b) -> Integer.compare(Integer.parseInt(a[1]), Integer.parseInt(b[1])));
        return totaledResults;
    }

    public int getPlaceOfSailor(Sailor sailor) {
        String[][] totaledResults = getTotals();
        String sailorUsername = sailor.getUsername();
        int i = 1;
        for (String[] line : totaledResults) {
            if (line[0].equals(sailorUsername)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public String printableResults() {
        String divider = " | ";
        String newLine = "\n";
        String startLine = "| ";
        String resultsString = "| ";
        String[] headers = {"Place", "Username", "Total"};
        int[] stringLengths = new int[3];
        int i = 0;
        for (String header : headers) {
            resultsString += header + divider;
            stringLengths[i] = header.length();
            i++;
        }
        resultsString += newLine;
        String[][] totaledResults = getTotals();
        i = 1;
        for (String[] line : totaledResults) {
            resultsString += startLine;
            resultsString += padString(String.valueOf(i), stringLengths[0]) + divider;
            resultsString += padString(line[0], stringLengths[1]) + divider;
            resultsString += padString(line[1], stringLengths[2]) + divider;
            resultsString += newLine;
            i++;
        }
        return resultsString;
    }

    private String padString(String string, int length) {
        boolean left = true;
        while (string.length() < length) {
            if (left) {
                string = " " + string;
            } else {
                string += " ";
            }
            left = !left;
        }
        return string;
    }

    @Override
    public String toString() {
        return EVENT_ID + " - " + EVENT_NAME + " - " + numOfCompetitors() + " - " + numOfRaces();
    }

    public static void main(String[] args) {
        Regatta testRegatta = SailStatsManager.regattas.addNewRegatta("Buzzards Bay");
        testRegatta.addCompetitor(SailStatsManager.sailors.addNewSailor("Bob", "Smith"), 7777);
        testRegatta.addCompetitor(SailStatsManager.sailors.addNewSailor("John", "Robertson"), 1234);
        testRegatta.addCompetitor(SailStatsManager.sailors.addNewSailor("Jacob", "Reese"), 1003);
        testRegatta.addCompetitor(SailStatsManager.sailors.addNewSailor("Amanda", "Clarke"), 1003);
        System.out.println(testRegatta);
        testRegatta.addRace(new ArrayList<Integer>(List.of(7777, 1003, 1234)));
        testRegatta.addRace(new ArrayList<>(List.of(1003, 1234, 7777)));
        testRegatta.addRace(new ArrayList<>(List.of(7777, 1234)));
        testRegatta.addRace(new ArrayList<>(List.of(1234)));
        testRegatta.addRace(new ArrayList<>(List.of(7777, 1234)));
        testRegatta.addRace(new ArrayList<>(List.of(7777, 1234)));
        System.out.println(testRegatta);
        String[][] results = testRegatta.getResults();
        for (String[] strings : results) {
            for (String string : strings) {
                System.out.print(string + " | ");
            }
            System.out.print("\n");
        }
        System.out.println("======================");
        results = testRegatta.getTotals();
        for (String[] strings : results) {
            for (String string : strings) {
                System.out.print(string + " | ");
            }
            System.out.print("\n");
        }
        System.out.println("======================");
        System.out.println(testRegatta.printableResults());
    }
}
