package sailstats;

import java.util.ArrayList;

public class Sailor {
    public final String FIRST_NAME;
    public final String LAST_NAME;
    public final int FIRST_INDEX;
    public final int SECOND_INDEX;
    public final int NUM_INDEX;
    public String club = "(N/A)";
    public String team = "(N/A)";
    private ArrayList<Integer> regattas = new ArrayList<Integer>(0);

    public Sailor(String firstName, String lastName, int numIndex) {
        this.FIRST_NAME = firstName;
        this.LAST_NAME = lastName;
        this.FIRST_INDEX = getIndexFromName(firstName);
        this.SECOND_INDEX = getIndexFromName(lastName);
        this.NUM_INDEX = numIndex;
    }

    public Sailor(String firstName, String lastName) {
        this.FIRST_NAME = firstName;
        this.LAST_NAME = lastName;
        this.FIRST_INDEX = getIndexFromName(firstName);
        this.SECOND_INDEX = getIndexFromName(lastName);
        this.NUM_INDEX = 0;
    }

    public static int getIndexFromName(String name){
        return name.toLowerCase().charAt(0) - 'a';
    }

    public String getUsername() {
        char firstChar = (char)('a' + FIRST_INDEX);
        char secondChar = (char)('a' + SECOND_INDEX);
        return (String.valueOf(firstChar) + String.valueOf(secondChar) + NUM_INDEX).toUpperCase();
    }

    public void addRegatta(int eventID) {
        regattas.add(eventID);
    }

    @Override
    public String toString() {
        String sailorProfile = "";
        sailorProfile += "Username: " + getUsername() + "\n";
        sailorProfile += "Name: " + LAST_NAME + ", " + FIRST_NAME + "\n";
        sailorProfile += "Club: " + club + "\n";
        sailorProfile += "Team: " + team + "\n\n";
        sailorProfile += "RESULTS\n";
        sailorProfile += "ID - Event Name - Place - Num Competitors\n";
        for (Integer index : regattas) {
            Regatta currentRegatta = SailStatsManager.regattas.getRegatta(index);
            sailorProfile += currentRegatta.EVENT_ID + " - " + currentRegatta.EVENT_NAME + " - " + currentRegatta.getPlaceOfSailor(this) + " - " + currentRegatta.numOfCompetitors() + "\n";
        }
        return sailorProfile;
    }
}
