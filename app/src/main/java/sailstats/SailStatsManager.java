package sailstats;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Responsible For The CLI of the SailStats App
 * 
 * @author Nicholas Lorentzen
 * @version 20201208
 */
public class SailStatsManager {
    public static Sailors sailors = new Sailors();
    public static Regattas regattas = new Regattas();
    public static Races races = new Races();

    /** Holds The Previously Used Commands For Use In Back Button */
    private ArrayDeque<Consumer<Integer>> previousCommand = new ArrayDeque<>(0);
    /** Holds The Command To Be Executed Next */
    private Consumer<Integer> nextCommand = this::mainMenu;


    private int steps = 0;
    private boolean endProgram = false;
    private Scanner scanner = new Scanner(System.in);

    private Regatta currentRegatta;
    private int currentRegattaID = -1;
    /**
     * Creates A Sails Stats Instance
     * 
     * @param cliMode Can Disable The CLI menus for use as a library for another App
     */
    public SailStatsManager(boolean cliMode) {
        System.out.println("Starting SailStats!");
        if (App.IS_DEBUG_MODE) {
            addDebugData();
        }
        if (cliMode) {
            while (!endProgram) {
                nextCommand.accept(steps);
            }
        }
    }

    public static void clearScreen() {
        // System.out.print("\033[H\033[2J");
        // System.out.flush();
        // TODO Figure Out Why This Doesn't Work
    }

    public static void addHorizontalBreak() {
        System.out.println("");
        System.out.println("==================================");
    }

    /**
     * Creates a GUI Menu
     * @param prompt Title of the menu
     * @param options Descriptiions of each subitem
     * @param actions Lambdas for each possible choice
     * @param auto Default choice (0 for no default)
     */
    public void createMenu(String prompt, String[] options, ArrayList<Consumer<Integer>> actions, Integer auto) {
        clearScreen();
        System.out.println(prompt);
        addHorizontalBreak();
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + " - " + options[i]);
        }
        if (!previousCommand.isEmpty()) {
            actions.add(previousCommand.getLast());
            System.out.println((options.length + 1) + " - Back");
        }

        addHorizontalBreak();

        if (auto != 0) {
            System.out.print("Default: " + auto + " ");
        }
        System.out.print("> ");
        String input = scanner.nextLine();
        if (input.equals("")) {
            nextCommand = actions.get(auto);
        } else {
            try {
                int selection = Integer.parseInt(input);
                if (!previousCommand.isEmpty() && selection == actions.size()) {
                    previousCommand.removeLast();
                } else {
                    previousCommand.add(nextCommand);
                }
                nextCommand = actions.get(selection - 1);
                steps++;
            } catch (Exception e) {
                System.out.println("Invalid Selection - Check That You Entered Only The Number Of Your Selection");
                createMenu(prompt, options, actions, auto);
            }
        }
    }

    public void endProgram(int steps) {
        System.out.println("Exiting SailStats!");
        endProgram = true;
    }

    public void mainMenu(int steps) {
        createMenu("What would you like to do?", new String[] { "Manage Regattas", "Manage Sailors", "Quit" },
                new ArrayList<Consumer<Integer>>(List.of(this::regattasMain, this::sailorsMain, this::endProgram)), 0);
    }

    public void regattasMain(int steps) {
        createMenu("What would you like to do?", new String[] { "View Regattas", "Open Regatta", "Create New Regatta" },
                new ArrayList<Consumer<Integer>>(List.of(this::viewRegattas, this::openRegatta, this::createRegatta)),
                0);
    }

    public void viewRegattas(int steps) {
        clearScreen();
        addHorizontalBreak();
        System.out.print(regattas);
        addHorizontalBreak();
        System.out.println("When Done Press Enter To Return To Menu");
        scanner.nextLine();
        previousCommand.removeLast().accept(steps);
    }

    public void openRegatta(int steps) {
        clearScreen();
        addHorizontalBreak();
        if (currentRegattaID != -1) {
            System.out.print("Enter Regatta Id - Default: 0 > ");
        } else {
            System.out.print("Enter Regatta Id > ");
        }
        String regattaIDString = scanner.nextLine();
        if (regattaIDString.equals("") && (currentRegattaID != -1)) {
            regattaIDString = String.valueOf(currentRegattaID);
        }
        int regattaId = 0;
        Regatta regatta;
        try {
            regattaId = Integer.parseInt(regattaIDString);
            regatta = regattas.getRegatta(regattaId);
        } catch (Exception e) {
            System.out.println("Invalid Regatta ID Try Again");
            return;
        }
        currentRegattaID = regattaId;
        clearScreen();
        addHorizontalBreak();
        System.out.println("REGATTA INFORMATION");
        System.out.println("ID - Name of Event - Num Competitors - Num Races");
        System.out.println(regatta);
        if (regatta.numOfRaces() != 0) {
            System.out.println("========== Scores ==========");
            System.out.println(regatta.printableResults());
        }
        addHorizontalBreak();
        System.out.println("PRESS ENTER TO MOVE TO REGATTA EDIT MENU");
        scanner.nextLine();
        currentRegatta = regatta;
        createMenu("What would you like to do?",
                new String[] { "Score A Race", "Add an existing sailor", "Add a new sailor" },
                new ArrayList<Consumer<Integer>>(
                        List.of(this::scoreARace, this::addSailorByID, this::addNewSailorToEvent)),
                0);
    }

    public void scoreARace(int steps) {
        clearScreen();
        addHorizontalBreak();
        System.out.println("Type Each Sail Number In Place");
        System.out.println("When You Are Done Type 'Done'");
        String lastInput = "";
        ArrayList<Integer> results = new ArrayList<Integer>(0);
        while (!lastInput.toLowerCase().equals("done")) {
            lastInput = scanner.nextLine();
            int sailNum = -1;
            try {
                sailNum = Integer.parseInt(lastInput);
                if (results.contains(sailNum)) {
                    System.out.println("That Boat Has Already Finished");
                } else if (!currentRegatta.sailNumberRegistered(sailNum)) {
                    System.out.println("That Boat Is Not In This Regatta");
                } else {
                    results.add(sailNum);
                }
            } catch (Exception e) {
                if (!lastInput.toLowerCase().equals("done")) {
                    System.out.println("Not a number!");
                }
            }
        }
        currentRegatta.addRace(results);
        clearScreen();
        addHorizontalBreak();
        System.out.println("Race Scored Successfully");
        System.out.println("PRESS ENTER TO RETURN TO REGATTA MENU");
        scanner.nextLine();
        nextCommand = previousCommand.removeLast();
    }

    public void addSailorByID(int steps) {
        clearScreen();
        addHorizontalBreak();
        Sailor currentSailor = null;
        while (currentSailor == null) {
            System.out.print("Enter Sailor Username > ");
            String possibleID = scanner.nextLine();
            try {
                Sailor tempSailor = sailors.getSailor(possibleID);
                if (currentRegatta.isRegistered(tempSailor)) {
                    System.out.println("Sailor Already Registered For Regatta");
                } else {
                    currentSailor = tempSailor;
                }
            } catch (Exception e) {
                System.out.println("Sailor Not Found With That Username");
            }
        }
        addHorizontalBreak();
        System.out.println("Adding " + currentSailor.LAST_NAME + ", " + currentSailor.FIRST_NAME + " to event");
        int sailNumber = -1;
        while (sailNumber == -1) {
            System.out.print("Enter Sailor Event Sail Number > ");
            String sailNumberString = scanner.nextLine();
            try {
                int sailTemp = Integer.parseInt(sailNumberString);
                if (currentRegatta.sailNumberRegistered(sailTemp)) {
                    System.out.println("Sail Number Already In Use For Event");
                } else if (sailTemp < 1) {
                    System.out.println("Sail Numbers Must Be Larger Than 0");
                } else {
                    sailNumber = sailTemp;
                }
            } catch (Exception e) {
                System.out.println("Not A Number");
            }
        }
        currentRegatta.addCompetitor(currentSailor, sailNumber);
        clearScreen();
        addHorizontalBreak();
        System.out.println("Sailor with username: '" + currentSailor.getUsername()
                + "' added to event with sail number: " + sailNumber);
        System.out.println("PRESS ENTER TO RETURN TO REGATTA MENU");
        scanner.nextLine();
        nextCommand = previousCommand.removeLast();
    }

    public void addNewSailorToEvent(int steps) {
        clearScreen();
        addHorizontalBreak();
        String firstName = "";
        while (firstName.equals("")) {
            System.out.print("Enter Sailor First Name > ");
            firstName = scanner.nextLine();
        }
        String lastName = "";
        while (lastName.equals("")) {
            System.out.print("Enter Sailor Last Name > ");
            lastName = scanner.nextLine();
        }
        Sailor newSailor = sailors.addNewSailor(firstName, lastName);
        System.out.println("Sailor with username: '" + newSailor.getUsername() + "' created!");
        int sailNumber = -1;
        while (sailNumber == -1) {
            System.out.print("Enter Sailor Event Sail Number > ");
            String sailNumberString = scanner.nextLine();
            try {
                int sailTemp = Integer.parseInt(sailNumberString);
                if (currentRegatta.sailNumberRegistered(sailTemp)) {
                    System.out.println("Sail Number Already In Use For Event");
                } else if (sailTemp < 1) {
                    System.out.println("Sail Numbers Must Be Larger Than 0");
                } else {
                    sailNumber = sailTemp;
                }
            } catch (Exception e) {
                System.out.println("Not A Number");
            }
        }
        currentRegatta.addCompetitor(newSailor, sailNumber);
        clearScreen();
        addHorizontalBreak();
        System.out.println("Sailor with username: '" + newSailor.getUsername() + "' added to event with sail number: "
                + sailNumber);
        System.out.println("PRESS ENTER TO RETURN TO REGATTA MENU");
        scanner.nextLine();
        nextCommand = previousCommand.removeLast();
    }

    public void createRegatta(int steps) {
        clearScreen();
        addHorizontalBreak();
        System.out.println("CREATE A NEW REGATTA");
        System.out.print("Enter Regatta Name > ");
        String regattaName = scanner.nextLine();
        currentRegatta = regattas.addNewRegatta(regattaName);
        addHorizontalBreak();
        System.out.println("Regatta Added!");
        System.out.println("PRESS ENTER TO RETURN TO MAIN REGATTA MENU");
        scanner.nextLine();
        nextCommand = previousCommand.removeLast();
    }

    public void sailorsMain(int steps) {
        createMenu("What would you like to do?",
                new String[] { "View Sailors", "Open Sailor Profile", "Create New Sailor" },
                new ArrayList<Consumer<Integer>>(List.of(this::viewSailors, this::openSailor, this::createNewSailor)),
                0);
    }

    public void viewSailors(int steps) {
        clearScreen();
        addHorizontalBreak();
        System.out.println(sailors.toString());
        addHorizontalBreak();
        System.out.println("PRESS ENTER TO RETURN TO SAILORS MENU");
        scanner.nextLine();
        nextCommand = previousCommand.removeLast();
    }

    public void openSailor(int steps) {
        clearScreen();
        addHorizontalBreak();
        Sailor currentSailor = null;
        while (currentSailor == null) {
            System.out.print("Enter Sailor Username > ");
            String possibleID = scanner.nextLine();
            try {
                Sailor tempSailor = sailors.getSailor(possibleID);
                currentSailor = tempSailor;

            } catch (Exception e) {
                System.out.println("Sailor Not Found With That Username");
            }
        }
        addHorizontalBreak();
        System.out.println(currentSailor);
        addHorizontalBreak();
        System.out.println("PRESS ENTER TO RETURN TO SAILORS MENU");
        scanner.nextLine();
        nextCommand = previousCommand.removeLast();
    }

    public void createNewSailor(int steps) {
        clearScreen();
        addHorizontalBreak();
        String firstName = "";
        while (firstName.equals("")) {
            System.out.print("Enter Sailor First Name > ");
            firstName = scanner.nextLine();
        }
        String lastName = "";
        while (lastName.equals("")) {
            System.out.print("Enter Sailor Last Name > ");
            lastName = scanner.nextLine();
        }
        Sailor newSailor = sailors.addNewSailor(firstName, lastName);
        System.out.println("Sailor with username: '" + newSailor.getUsername() + "' created!");
        System.out.print("Add Sailor Yacht Club (Optional) > ");
        String yachtClub = scanner.nextLine();
        if (!yachtClub.equals("")) {
            newSailor.club = yachtClub;
        }
        System.out.print("Add Sailor Team (Optional) > ");
        String team = scanner.nextLine();
        if (!team.equals("")) {
            newSailor.team = team;
        }
        addHorizontalBreak();
        System.out.println("PRESS ENTER TO RETURN TO SAILORS MENU");
        scanner.nextLine();
        nextCommand = previousCommand.removeLast();
    }

    public void addDebugData() {
        String[] firstNames = { "Adam", "Alex", "Aaron", "Ben", "Carl", "Dan", "David", "Edward", "Fred", "Frank",
                "George", "Hal", "Hank", "Ike", "John", "Jack", "Joe", "Larry", "Monte", "Matthew", "Mark", "Nathan",
                "Otto", "Paul", "Peter", "Roger", "Roger", "Steve", "Thomas", "Tim", "Ty", "Victor", "Walter" };

        String[] lastNames = { "Anderson", "Ashwoon", "Aikin", "Bateman", "Bongard", "Bowers", "Boyd", "Cannon", "Cast",
                "Deitz", "Dewalt", "Ebner", "Frick", "Hancock", "Haworth", "Hesch", "Hoffman", "Kassing", "Knutson",
                "Lawless", "Lawicki", "Mccord", "McCormack", "Miller", "Myers", "Nugent", "Ortiz", "Orwig", "Ory",
                "Paiser", "Pak", "Pettigrew", "Quinn", "Quizoz", "Ramachandran", "Resnick", "Sagar", "Schickowski",
                "Schiebel", "Sellon", "Severson", "Shaffer", "Solberg", "Soloman", "Sonderling", "Soukup", "Soulis",
                "Stahl", "Sweeney", "Tandy", "Trebil", "Trusela", "Trussel", "Turco", "Uddin", "Uflan", "Ulrich",
                "Upson", "Vader", "Vail", "Valente", "Van Zandt", "Vanderpoel", "Ventotla", "Vogal", "Wagle", "Wagner",
                "Wakefield", "Weinstein", "Weiss", "Woo", "Yang", "Yates", "Yocum", "Zeaser", "Zeller", "Ziegler",
                "Bauer", "Baxster", "Casal", "Cataldi", "Caswell", "Celedon", "Chambers", "Chapman", "Christensen",
                "Darnell", "Davidson", "Davis", "DeLorenzo", "Dinkins", "Doran", "Dugelman", "Dugan", "Duffman",
                "Eastman", "Ferro", "Ferry", "Fletcher", "Fietzer", "Hylan", "Hydinger", "Illingsworth", "Ingram",
                "Irwin", "Jagtap", "Jenson", "Johnson", "Johnsen", "Jones", "Jurgenson", "Kalleg", "Kaskel", "Keller",
                "Leisinger", "LePage", "Lewis", "Linde", "Lulloff", "Maki", "Martin", "McGinnis", "Mills", "Moody",
                "Moore", "Napier", "Nelson", "Norquist", "Nuttle", "Olson", "Ostrander", "Reamer", "Reardon", "Reyes",
                "Rice", "Ripka", "Roberts", "Rogers", "Root", "Sandstrom", "Sawyer", "Schlicht", "Schmitt", "Schwager",
                "Schutz", "Schuster", "Tapia", "Thompson", "Tiernan", "Tisler" };
        Random rand = new Random();
        for (String lastName : lastNames) {
            for (String firstName : firstNames) {
                if (rand.nextInt(100) > 60) {
                    sailors.addNewSailor(firstName, lastName);
                }
            }
        }

        String[] regattaNameComponents = { "AY", "JO", "US", "NZ", "TT" };
        for (String string : regattaNameComponents) {
            for (String string2 : regattaNameComponents) {
                Regatta newRegatta = regattas.addNewRegatta(string + string2);
            }
        }

    }
}
