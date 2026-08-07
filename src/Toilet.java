/**
 * A toilet block - a park facility, not an attraction, so it stands
 * outside the Attraction hierarchy entirely. It shares only one
 * capability with rides: it can be inspected, so it implements
 * the Inspectable contract.
 */
public class Toilet implements Inspectable {
    private String id;
    private String location;
    private boolean closedForInspection;
    private String lastInspectionOutcome;

    /**
     * Creates a toilet block, initially open with no inspections recorded.
     * @param id unique identifier, e.g. "T1"
     * @param location where in the park the block is found
     */
    public Toilet(String id, String location) {
        this.id = id;
        this.location = location;
        this.closedForInspection = false;
        this.lastInspectionOutcome = "Not yet inspected";
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public void startInspection() {
        closedForInspection = true;
        System.out.println("Toilet block at " + location + " is now CLOSED for cleaning inspection.");
    }

    @Override
    public void completeInspection(String outcome) {
        lastInspectionOutcome = outcome;
        closedForInspection = false;
        System.out.println("Toilet block at " + location + " inspection complete (" + outcome + ") - now OPEN.");
    }

    @Override
    public boolean isClosedForInspection() {
        return closedForInspection;
    }

    /**
     * @return the facility's details and latest inspection outcome
     */
    @Override
    public String toString() {
        return "Toilet " + id + " at " + location + " | Last inspection: " + lastInspectionOutcome;
    }
}