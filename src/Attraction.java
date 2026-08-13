import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
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