package sailstats;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main App Class To Run The SailStats App
 * 
 * @author Nicholas Lorentzen
 * @version 20201208
 */
public class App {
    /** Toggles On Or Off The Creation Of Some Sample Data */
    public static final boolean IS_DEBUG_MODE = false;

    private ArrayList<Sailor> sailors = new ArrayList<Sailor>(0);

    public void startUp() {
        SailStatsManager mySailStatsManager = new SailStatsManager(false);
        Demo();
        new SailStatsManager(true);
    }

    public void Demo() {
        Sailor currentSailor = SailStatsManager.sailors.addNewSailor("Jamie", "Gilman");
        currentSailor.club = "Conroe Yacht Club";
        sailors.add(currentSailor);
        currentSailor = SailStatsManager.sailors.addNewSailor("Paul", "Gingras");
        currentSailor.club = "Sailfish Club of Florida";
        sailors.add(currentSailor);
        currentSailor = SailStatsManager.sailors.addNewSailor("Nicholas", "Lorentzen");
        currentSailor.club = "Sailfish Club of Florida";
        currentSailor.team = "Connecticut College Camels";
        sailors.add(currentSailor);
        currentSailor = SailStatsManager.sailors.addNewSailor("John", "Brim");
        currentSailor.club = "Sailfish Club of Florida";
        sailors.add(currentSailor);
        currentSailor = SailStatsManager.sailors.addNewSailor("Melissa", "Solnick");
        currentSailor.club = "Fragila Vela Riva";
        sailors.add(currentSailor);
        currentSailor = SailStatsManager.sailors.addNewSailor("Jeffrey", "Leach");
        currentSailor.club = "Sailfish Club of Florida";
        sailors.add(currentSailor);
        currentSailor = SailStatsManager.sailors.addNewSailor("Geoffrey", "Moehl");
        currentSailor.club = "Lake Eustis Sailing Club";
        sailors.add(currentSailor);
        currentSailor = SailStatsManager.sailors.addNewSailor("Gregory", "Popp");
        currentSailor.club = "Halifax Sailing Association";
        sailors.add(currentSailor);
        currentSailor = SailStatsManager.sailors.addNewSailor("Matthew", "Lorentzen");
        currentSailor.club = "Sailfish Club of Florida";
        sailors.add(currentSailor);

        Regatta currentRegatta = SailStatsManager.regattas.addNewRegatta("RS Aero North American Midwinters");
        currentRegatta.addCompetitor(sailors.get(0), 2119);
        currentRegatta.addCompetitor(sailors.get(1), 1378);
        currentRegatta.addCompetitor(sailors.get(3), 1764);
        currentRegatta.addCompetitor(sailors.get(4), 1);
        currentRegatta.addCompetitor(sailors.get(5), 1278);
        currentRegatta.addCompetitor(sailors.get(6), 2279);
        currentRegatta.addCompetitor(sailors.get(7), 1610);
        currentRegatta.addCompetitor(sailors.get(8), 1761);
        
    }

    public static void main(String[] args) {
        new App().startUp();
    }
}
