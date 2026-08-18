import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * PROG2004 Assessment 2 - Theme park attraction and Visitor management system.
 * @author Hudson Ross
 */
public class AssignmentTwo {

    public static void main(String[] args) {

// ===== Part 1: Modelling the park's people =====
        System.out.println("--- Part 1: The park's people ---");

        Staff staff = new Staff(101, "John Cena", 20, "Ride Operator");
        System.out.println(staff);

        Visitor v1 = new Visitor(201, "Amy Wu", 34, "Season Pass");
        Visitor v2 = new Visitor(202, "Ben Okafor", 8);
        Visitor v3 = new Visitor(203, "Carla Reyes", 21, "Day Pass");

        ArrayList<Visitor> visitors = new ArrayList<Visitor>();
        visitors.add(v1);
        visitors.add(v2);
        visitors.add(v3);

        System.out.println("Visitors in arrival order:");
        for (int i = 0; i < visitors.size(); i++) {
            System.out.println(visitors.get(i));
        }

        Collections.sort(visitors);
        System.out.println("Visitors ordered by age:");
        for (int i = 0; i < visitors.size(); i++) {
            System.out.println(visitors.get(i));
        }

        // ===== Part 2: The attraction family and inspections =====
        System.out.println();
        System.out.println("--- Part 2: Attractions, facilities and inspections ---");

        Ride coaster = new Ride("R1", "Thunder Loop", staff, 4, 120);
        Show dolphinShow = new Show("S1", "Dolphin Splash", null, 50, 25);
        Toilet northToilets = new Toilet("T1", "North Gate");

        System.out.println(coaster);
        System.out.println(dolphinShow);
        System.out.println(northToilets);

        staff.performInspection(coaster, "Passed - brakes and restraints all clear");
        staff.performInspection(northToilets, "Passed - restocked and cleaned");
        System.out.println(coaster);
        System.out.println(northToilets);

        // ===== Part 3: The waiting line =====
        System.out.println();
        System.out.println("--- Part 3: The waiting line (FIFO) ---");

        Visitor v4 = new Visitor(204, "Dan Foster", 45, "Season Pass");
        Visitor v5 = new Visitor(205, "Evan Larsen", 16);

        coaster.addToQueue(v1);
        coaster.addToQueue(v2);
        coaster.addToQueue(v3);
        coaster.addToQueue(v4);
        coaster.addToQueue(v5);
        coaster.printQueue();

        System.out.println("Serving the next visitor:");
        coaster.removeFromQueue();
        coaster.printQueue();

        System.out.println("Emptying the line for the dolphin show:");
        dolphinShow.removeFromQueue();

        // ===== Part 4: Visit history =====
        System.out.println();
        System.out.println("--- Part 4: Visit history, membership and sorting ---");

        coaster.recordVisit(v1);
        coaster.recordVisit(v3);
        coaster.recordVisit(v4);
        coaster.recordVisit(v1);
        coaster.recordVisit(v5);
        coaster.printVisitHistory();

        Visitor amyAgain = new Visitor(201, "Amy Wu", 34, "Season Pass");
        System.out.println("Has visitor 201 been on Thunder Loop? " + coaster.hasVisited(amyAgain));
        System.out.println("How many times? " + coaster.countVisits(amyAgain));

        Visitor stranger = new Visitor(299, "Unknown Guest", 30);
        System.out.println("Has visitor 299 been on Thunder Loop? " + coaster.hasVisited(stranger));

        coaster.sortHistoryByAge();
        coaster.printVisitHistory();

        coaster.sortHistoryByTicketThenName();
        coaster.printVisitHistory();

        // ===== Part 5: Running cycles =====
        System.out.println();
        System.out.println("--- Part 5: Running cycles ---");

        System.out.println("Cycles so far - Thunder Loop: " + coaster.getCyclesRun()
                + ", Dolphin Splash: " + dolphinShow.getCyclesRun());

        System.out.println("Before the cycle:");
        coaster.printQueue();
        coaster.runCycle();
        System.out.println("After the cycle:");
        coaster.printQueue();
        coaster.printVisitHistory();

        System.out.println("Trying to run with an empty line:");
        coaster.runCycle();

        System.out.println("Trying to run while closed for inspection:");
        coaster.addToQueue(v2);
        coaster.startInspection();
        coaster.runCycle();
        coaster.completeInspection("Passed - routine mid-day check");

        System.out.println("The show runs to an empty house:");
        dolphinShow.setOperator(staff);
        dolphinShow.runCycle();

        System.out.println("An unstaffed show refuses to run:");
        dolphinShow.setOperator(null);
        dolphinShow.runCycle();

        System.out.println("Cycles at the end of the day - Thunder Loop: " + coaster.getCyclesRun()
                + ", Dolphin Splash: " + dolphinShow.getCyclesRun());

                // ===== Part 6: Managing the park =====
        System.out.println();
        System.out.println("--- Part 6: Managing the whole park ---");

        ThemePark park = new ThemePark("Sunshine Adventure Park");
        park.registerAttraction(coaster);
        park.registerAttraction(dolphinShow);

        Ride splashFalls = new Ride("R2", "Splash Falls", staff, 2, 100);
        Show magicShow = new Show("S2", "Magic Hour", staff, 30, 40);
        park.registerAttraction(splashFalls);
        park.registerAttraction(magicShow);
        park.registerAttraction(coaster);

        System.out.println("Attractions registered: " + park.getAttractionCount());

        Visitor v6 = new Visitor(206, "Fiona Ng", 29, "Season Pass");
        splashFalls.addToQueue(v1);
        splashFalls.addToQueue(v6);
        splashFalls.runCycle();

        System.out.println("Looking up attractions by ID:");
        Attraction lookedUp = park.findAttraction("R2");
        System.out.println("  " + lookedUp);
        park.findAttraction("R9");

        park.printSeatsServed();
        System.out.println("Total seats served across the park: " + park.countTotalSeats());
        System.out.println("Distinct visitors admitted today: " + park.countDistinctVisitors());

        // ===== Part 7: Backing up and restoring the park =====
        System.out.println();
        System.out.println("--- Part 7: Backing up and restoring ---");

        File backup = new File("park-backup.txt");
        try {
            ParkIO.save(park, backup);
            ThemePark restored = ParkIO.load(backup, "Sunshine Adventure Park (restored)");

            System.out.println("Original attractions: " + park.getAttractionCount()
                    + ", restored: " + restored.getAttractionCount());
            System.out.println("Original seats: " + park.countTotalSeats()
                    + ", restored: " + restored.countTotalSeats());
            System.out.println("Original distinct visitors: " + park.countDistinctVisitors()
                    + ", restored: " + restored.countDistinctVisitors());

            Attraction restoredCoaster = restored.findAttraction("R1");
            System.out.println("  " + restoredCoaster);
            restoredCoaster.printQueue();
        } catch (ParkStorageException e) {
            System.out.println("Backup or restore failed: " + e.getMessage());
        }

        System.out.println("Trying to load a file that does not exist:");
        try {
            ParkIO.load(new File("no-such-park.txt"), "Recovered park");
        } catch (ParkStorageException e) {
            System.out.println("  Handled: " + e.getMessage());
            System.out.println("  Carrying on with the park already in memory.");
        }

        System.out.println("Loading a corrupted file:");
        try {
            ArrayList<String> badLines = new ArrayList<String>();
            badLines.add("STAFF,101,John Cena,20,Ride Operator");
            badLines.add("VISITOR,201,Amy Wu,34,Season Pass");
            badLines.add("VISITOR,202,Ben Okafor");
            badLines.add("ATTRACTION,RIDE,R1,Thunder Loop,101,four,120,1");
            badLines.add("ATTRACTION,SHOW,S1,Dolphin Splash,NONE,50,25,1");
            badLines.add("ROLLERDISCO,X1,Unknown thing");
            badLines.add("QUEUE,S1,201");
            badLines.add("HISTORY,S1,999");
            File corrupt = new File("park-corrupt.txt");
            ParkIO.writeLines(corrupt, badLines);
            ThemePark partial = ParkIO.load(corrupt, "Park from corrupt file");
            System.out.println("  Attractions recovered despite the bad lines: "
                    + partial.getAttractionCount());
        } catch (ParkStorageException e) {
            System.out.println("  Handled: " + e.getMessage());
        }

    }

}