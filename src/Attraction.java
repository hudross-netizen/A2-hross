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