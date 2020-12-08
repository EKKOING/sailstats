package sailstats;

import java.util.ArrayList;

public class Races {
    private ArrayList<Race> raceList = new ArrayList<>(0);

    public Race addNewRace(int regattaID, ArrayList<Integer> results) {
        int raceID = raceList.size();
        Race newRace = new Race(raceID, regattaID, results);
        raceList.add(newRace);
        return newRace;
    }

    public Race getRace(int raceID) {
        return raceList.get(raceID);
    }
}
