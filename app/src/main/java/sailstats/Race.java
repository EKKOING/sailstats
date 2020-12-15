package sailstats;

import java.util.ArrayList;

/**
 * Defines A Race Object
 * 
 * @author Nicholas Lorentzen
 * @version 20201208
 */
public class Race {
    public final int RACE_ID;
    public final int REGATTA_ID;
    public final ArrayList<Integer> RESULTS;

    public Race(int raceID, int regattaID, ArrayList<Integer> results) {
        this.RACE_ID = raceID;
        this.REGATTA_ID = regattaID;
        this.RESULTS = results;
    }
}
