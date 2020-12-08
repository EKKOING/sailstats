package sailstats;

import java.util.ArrayList;

public class Regattas {
    private ArrayList<Regatta> regattaList = new ArrayList<Regatta>(0);

    public Regatta addNewRegatta(String name) {
        Regatta newRegatta = new Regatta(name, regattaList.size());
        regattaList.add(newRegatta);
        return newRegatta;
    }

    public Regatta getRegatta(int id) {
        return regattaList.get(id);
    }

    @Override
    public String toString() {
        String tableHeader = "ID - Name of Event - Num Competitors - Num Races \n";
        String collumns = "";
        for (Regatta regatta : regattaList) {
            collumns += regatta.toString() + "\n";
        }
        return tableHeader + collumns;
    }

    public static void main(String[] args) {
        Regattas testRegattas = new Regattas();
        testRegattas.addNewRegatta("Buzzards Bay");
        testRegattas.addNewRegatta("Vendee Globe");
        Regatta buzzardsBay = testRegattas.getRegatta(0);
        buzzardsBay.addCompetitor(new Sailor("Bob", "Smith"), 1);
        System.out.print(testRegattas);
    }
}
