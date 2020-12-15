package sailstats;

/**
 * Main App Class To Run The SailStats App
 * 
 * @author Nicholas Lorentzen
 * @version 20201208
 */
public class App {
    /** Toggles On Or Off The Creation Of Some Sample Data */
    public static final boolean IS_DEBUG_MODE = true;

    public void startUp() {
        SailStatsManager sailStatsManager = new SailStatsManager(true);
    }

    public static void main(String[] args) {
        new App().startUp();
    }
}
