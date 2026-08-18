import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Manages the whole collection of attractions in one park.
 * Attractions are held in a map keyed by their identifier so that any
 * one can be retrieved in a single step, and the park can report both
 * how busy each attraction has been and how many different visitors
 * it has admitted across the day.
 */
public class ThemePark {
    private String name;
    private Map<String, Attraction> attractions = new HashMap<String, Attraction>();
    private int parkWideTotal = 0;

    /**
     * Adds to the single park-wide tally of visitors served.
     * Synchronized because several attractions update this value at the
     * same time: reading, adding and writing must happen as one step,
     * or two threads can overwrite each other's update and the total
     * comes out too low.
     * @param served how many visitors an attraction just served
     */
    public synchronized void addServed(int served) {
        parkWideTotal = parkWideTotal + served;
    }

    /**
     * @return the park-wide total of visitors served
     */
    public int getParkWideTotal() {
        return parkWideTotal;
    }

    /**
     * Creates an empty park.
     * @param name the park's name
     */
    public ThemePark(String name) {
        this.name = name;
    }

    /** @return the park's name */
    public String getName() {
        return name;
    }

    /**
     * Registers an attraction under its own identifier. An attraction
     * is rejected if its identifier is already in use, so one entry can
     * never silently replace another.
     * @param attraction the attraction to register
     */
    public void registerAttraction(Attraction attraction) {
        if (attraction == null) {
            System.out.println("WARNING: cannot register an unknown attraction.");
            return;
        }
        if (attractions.containsKey(attraction.getId())) {
            System.out.println("WARNING: ID " + attraction.getId()
                    + " is already registered - " + attraction.getName() + " was not added.");
            return;
        }
        attractions.put(attraction.getId(), attraction);
        System.out.println("Registered " + attraction.getKind() + " " + attraction.getId()
                + " (" + attraction.getName() + ") at " + name + ".");
    }

    /**
     * Retrieves an attraction directly by its identifier.
     * @param id the identifier to look up, e.g. "R1"
     * @return the matching attraction, or null if none is registered
     */
    public Attraction findAttraction(String id) {
        Attraction found = attractions.get(id);
        if (found == null) {
            System.out.println("WARNING: no attraction is registered with ID " + id + ".");
            return null;
        }
        System.out.println("Found " + found.getName() + " for ID " + id + ".");
        return found;
    }

    /**
     * @return the number of attractions registered
     */
    public int getAttractionCount() {
        return attractions.size();
    }

    /**
     * @return a list of every registered attraction
     */
    public ArrayList<Attraction> getAllAttractions() {
        return new ArrayList<Attraction>(attractions.values());
    }

    /**
     * Prints how many seats each attraction has served, where the same
     * visitor attending twice counts twice.
     */
    public void printSeatsServed() {
        System.out.println("Seats served at " + name + ":");
        Iterator<String> keys = attractions.keySet().iterator();
        while (keys.hasNext()) {
            Attraction attraction = attractions.get(keys.next());
            System.out.println("  " + attraction.getId() + " " + attraction.getName()
                    + ": " + attraction.getTotalVisits() + " seats over "
                    + attraction.getCyclesRun() + " cycles");
        }
    }

    /**
     * Counts how many different visitors the park has admitted across
     * all attractions. A visitor served several times, or served at
     * several attractions, is counted only once - the set decides
     * sameness using Visitor's equals and hashCode.
     * @return the number of distinct visitors
     */
    public int countDistinctVisitors() {
        Set<Visitor> distinct = new HashSet<Visitor>();
        Iterator<String> keys = attractions.keySet().iterator();
        while (keys.hasNext()) {
            Attraction attraction = attractions.get(keys.next());
            Iterator<Visitor> visitors = attraction.getVisitHistory().iterator();
            while (visitors.hasNext()) {
                distinct.add(visitors.next());
            }
        }
        return distinct.size();
    }

    /**
     * @return the total seats served across every attraction, counting
     *         repeat visits separately
     */
    public int countTotalSeats() {
        int total = 0;
        Iterator<String> keys = attractions.keySet().iterator();
        while (keys.hasNext()) {
            total = total + attractions.get(keys.next()).getTotalVisits();
        }
        return total;
    }

    /**
     * Looks up an attraction without printing anything, for internal
     * use where a missing entry is handled by the caller.
     * @param id the identifier to look up
     * @return the attraction, or null if not registered
     */
    public Attraction getAttraction(String id) {
        return attractions.get(id);
    }
}