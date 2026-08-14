/**
 * A ride - an attraction with a minimum height requirement that
 * must be inspected for safety, and is closed to visitors while
 * an inspection is underway.
 */
public class Ride extends Attraction implements Inspectable {
    private int minHeightCm;
    private boolean closedForInspection;
    private String lastInspectionOutcome;

    /**
     * Creates a ride, initially open with no inspections recorded.
     * @param id unique identifier, e.g. "R1"
     * @param name display name
     * @param operator staff member running the ride, or null
     * @param maxPerCycle riders per cycle
     * @param minHeightCm minimum rider height in centimetres
     */
    public Ride(String id, String name, Staff operator, int maxPerCycle, int minHeightCm) {
        super(id, name, operator, maxPerCycle);
        this.minHeightCm = minHeightCm;
        this.closedForInspection = false;
        this.lastInspectionOutcome = "Not yet inspected";
    }

    public int getMinHeightCm() {
        return minHeightCm;
    }

    @Override
    public String getKind() {
        return "Ride";
    }

    @Override
    public void startInspection() {
        closedForInspection = true;
        System.out.println(getName() + " is now CLOSED for inspection.");
    }

    @Override
    public void completeInspection(String outcome) {
        lastInspectionOutcome = outcome;
        closedForInspection = false;
        System.out.println(getName() + " inspection complete (" + outcome + ") - now OPEN.");
    }

    @Override
    public boolean isClosedForInspection() {
        return closedForInspection;
    }

    /**
     * @return the shared attraction details plus the ride's height rule
     *         and latest inspection outcome
     */
    @Override
    public String toString() {
        return super.toString() + " | Min height: " + minHeightCm + "cm | Last inspection: "
                + lastInspectionOutcome;
    }

    /**
     * Runs one dispatch. A ride refuses to run without an operator,
     * while closed for inspection, or with nobody waiting.
     */
    @Override
    public void runCycle() {
        if (getOperator() == null) {
            System.out.println("WARNING: " + getName() + " cannot run - no operator assigned.");
            return;
        }
        if (closedForInspection) {
            System.out.println("WARNING: " + getName() + " cannot run - closed for inspection.");
            return;
        }
        if (getQueueLength() == 0) {
            System.out.println("WARNING: " + getName() + " cannot run - nobody is waiting.");
            return;
        }
        System.out.println(getName() + " is dispatching a cycle...");
        serveCycle();
    }
}