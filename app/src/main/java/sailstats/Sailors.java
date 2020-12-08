package sailstats;

import java.util.ArrayList;

public class Sailors {
    private ArrayList<Sailor>[][] sailorDirectory = new ArrayList[26][26];

    public Sailors() {
        for (ArrayList<Sailor>[] arrayLists : sailorDirectory) {
            for (ArrayList<Sailor> arrayList : arrayLists) {
                arrayList = new ArrayList<Sailor>(0);
            }
        }
    }

    public Sailor getSailor(int firstIndex, int secondIndex, int indexNum) {
        return sailorDirectory[firstIndex][secondIndex].get(indexNum);
    }

    public Sailor getSailor(String username) {
        String firstInitial = username.substring(0, 1);
        String lastInitial = username.substring(1, 2);
        int idNum = Integer.parseInt(username.substring(2));
        return getSailor(Sailor.getIndexFromName(firstInitial), Sailor.getIndexFromName(lastInitial), idNum);
    }

    public Sailor addNewSailor(String firstName, String lastName) {
        ArrayList<Sailor> newSailorLocation = sailorDirectory[Sailor.getIndexFromName(firstName)][Sailor
                .getIndexFromName(lastName)];
        if (newSailorLocation == null) {
            sailorDirectory[Sailor.getIndexFromName(firstName)][Sailor
                    .getIndexFromName(lastName)] = new ArrayList<Sailor>(0);
            newSailorLocation = sailorDirectory[Sailor.getIndexFromName(firstName)][Sailor.getIndexFromName(lastName)];
        }
        int newSailorIndexNum = newSailorLocation.size();
        Sailor newSailor = new Sailor(firstName, lastName, newSailorIndexNum);
        newSailorLocation.add(newSailor);
        return newSailor;
    }

    @Override
    public String toString() {
        String sailorDirectoryString = "";
        sailorDirectoryString += "Username - Name - Club/Team\n";
        for (ArrayList<Sailor>[] arrayLists : sailorDirectory) {
            for (ArrayList<Sailor> arrayList : arrayLists) {
                if (arrayList != null) {
                    for (Sailor sailor : arrayList) {
                        sailorDirectoryString += sailor.getUsername() + " - " + sailor.LAST_NAME + ", "
                                + sailor.FIRST_NAME + " - " + sailor.club + "/" + sailor.team + "\n";
                    }
                }
            }
        }
        return sailorDirectoryString;
    }
}
