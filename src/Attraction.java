import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Collections;

/**
 * Abstract base class for every attraction in the park.
 * Holds what all attractions share: an ID, a name, the staff member
 * operating it, and the number of visitors served per cycle.
 * Cannot be instantiated - every attraction is a specific kind.
 */
public abstract class Attraction {
    private String id;
    private String name;
    private Staff operator;
    private int maxPerCycle;
    private Queue<Visitor> waitingLine = new LinkedList<Visitor>();
    private ArrayList<Visitor> visitHistory = new ArrayList<Visitor>();
    private int cyclesRun = 0;

    /**
     * Creates an attraction with its shared details.
     * @param id unique identifier, e.g. "R1"
     * @param name display name of the attraction
     * @param operator the staff member running it (may be null if unstaffed)
     * @param maxPerCycle visitors served in one cycle
     */
    public Attraction(String id, String name, Staff operator, int maxPerCycle) {
        this.id = id;
        this.name = name;
        this.operator = operator;
        this.maxPerCycle = maxPerCycle;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Staff getOperator() {
        return operator;
    }

    /**
     * Assigns or replaces the operator - attractions can be unstaffed
     * at quiet times, so this can also be set to null.
     * @param operator the staff member taking over, or null
     */
    public void setOperator(Staff operator) {
        this.operator = operator;
    }

    public int getMaxPerCycle() {
        return maxPerCycle;
    }

    /**
     * @return how many cycles this attraction has run today
     */
    public int getCyclesRun() {
        return cyclesRun;
    }

    /**
     * Runs a single cycle if this attraction's own rules allow it.
     * Each kind decides differently, so each subclass supplies its rule.
     */
    public abstract void runCycle();

    /**
     * The shared serving machinery every kind uses once it has decided
     * to run: takes up to its capacity from the front of the waiting
     * line, moves them into the visit history, and counts the cycle.
     */
    public void serveCycle() {
        int served = 0;
        while (served < maxPerCycle && waitingLine.size() > 0) {
            Visitor next = waitingLine.poll();
            visitHistory.add(next);
            System.out.println("  " + next.getName() + " was taken from the line and served.");
            served++;
        }
        cyclesRun++;
        if (served == 0) {
            System.out.println("  Nobody was waiting - the cycle ran to an empty house.");
        }
        System.out.println(name + " completed cycle " + cyclesRun + " (" + served + " visitors served).");
    }

    /**
     * Each kind of attraction states what it is.
     * @return the kind, e.g. "Ride"
     */
    public abstract String getKind();

    /**
     * Adds a visitor to the back of this attraction's waiting line.
     * @param visitor the visitor joining the line
     */
    public void addToQueue(Visitor visitor) {
        if (visitor == null) {
            System.out.println("Warning: cannot add an unknown visitor to the line for " + name + ".");
            return;
        }
        waitingLine.offer(visitor);
        System.out.println(visitor.getName() + " joined the line for " + name + " (position " + waitingLine.size() + ").");
    }

    /**
     * Serves the visitor who has waited longest, removing them from
     * the front of the line.
     * @return the visitor served, or null if nobody was waiting
     */
    public Visitor removeFromQueue() {
        Visitor next = waitingLine.poll();
        if (next == null) {
            System.out.println("WARNING: nobody is waiting for " + name + ".");
            return null;
        }
        System.out.println(next.getName() + " left the line for " + name + " and was served.");
        return next;
    }

    /**
     * @return the number of visitors currently waiting
     */
    public int getQueueLength() {
        return waitingLine.size();
    }

    /**
     * Prints everyone currently waiting, from the front of the line
     * to the back, using an iterator to traverse the queue.
     */
    public void printQueue() {
        System.out.println("Waiting line for " + name + " (" + waitingLine.size() + " waiting):");
        if (waitingLine.isEmpty()) {
            System.out.println(" (nobody is waiting)");
            return;
        }
        Iterator<Visitor> iterator = waitingLine.iterator();
        while (iterator.hasNext()) {
            System.out.println(" " + iterator.next());
        }
    }

    /**
     * Records that a visitor has been on this attraction. The same
     * visitor may appear more than once - repeat visits are kept.
     * @param visitor the visitor who has just been served
     */
    public void recordVisit(Visitor visitor) {
        if (visitor == null) {
            return;
        }
        visitHistory.add(visitor);
    }

    /**
     * Checks whether a visitor has ever been on this attraction.
     * The match is made by Visitor's equals, so any visitor object
     * carrying the same ID counts as the same guest.
     * @param visitor the visitor to look for
     * @return true if they appear in the visit history
     */
    public boolean hasVisited(Visitor visitor) {
        return visitHistory.contains(visitor);
    }

    /**
     * Counts how many times one visitor has been on this attraction.
     * @param visitor the visitor to count
     * @return the number of recorded visits by that guest
     */
    public int countVisits(Visitor visitor) {
        int count = 0;
        Iterator<Visitor> iterator = visitHistory.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(visitor)) {
                count++;
            }
        }
        return count;
    }

    /**
     * @return the total number of visits recorded, including repeats
     */
    public int getTotalVisits() {
        return visitHistory.size();
    }

    /**
     * Prints the visit history in its current order.
     */
    public void printVisitHistory() {
        System.out.println("Visit history for " + name + " (" + visitHistory.size() + " visits):");
        if (visitHistory.isEmpty()) {
            System.out.println("  (no visits recorded)");
            return;
        }
        Iterator<Visitor> iterator = visitHistory.iterator();
        while (iterator.hasNext()) {
            System.out.println("  " + iterator.next());
        }
    }

    /**
     * Sorts the visit history by the visitors' natural ordering: age,
     * youngest first.
     */
    public void sortHistoryByAge() {
        Collections.sort(visitHistory);
        System.out.println("Visit history for " + name + " sorted by age (natural ordering).");
    }

    /**
     * Sorts the visit history by ticket type and then name, using a
     * comparator rather than the visitors' natural ordering.
     */
    public void sortHistoryByTicketThenName() {
        visitHistory.sort(new VisitorTicketComparator());
        System.out.println("Visit history for " + name + " sorted by ticket type, then name.");
    }


    /**
     * @return the attraction's shared details, including its kind
     */
    @Override
    public String toString() {
        String operatorName = "UNSTAFFED";
        if (operator != null) {
            operatorName = operator.getName();
        }
        return getKind() + " " + id + ": " + name + " | Operator: " + operatorName
                + " | Capacity per cycle: " + maxPerCycle;
    }
}