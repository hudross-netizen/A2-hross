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

    }

}