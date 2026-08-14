/**
 * A show - an attraction that runs scheduled performances of a set
 * duration. Shows are not subject to safety inspections, so this
 * class deliberately does not implement Inspectable.
 */
public class Show extends Attraction {
    private int durationMinutes;

    /**
     * Creates a show.
     * @param id unique identifier, e.g. "S1"
     * @param name display name
     * @param operator staff member presenting the show, or null
     * @param maxPerCycle audience size per performance
     * @param durationMinutes length of one performance, in minutes
     */
    public Show(String id, String name, Staff operator, int maxPerCycle, int durationMinutes) {
        super(id, name, operator, maxPerCycle);
        this.durationMinutes = durationMinutes;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public String getKind() {
        return "Show";
    }

    /**
     * @return the shared attraction details plus the performance duration
     */
    @Override
    public String toString() {
        return super.toString() + " | Duration: " + durationMinutes + " minutes";
    }

    /**
     * Runs one performance. A show needs an operator, but goes ahead
     * whether or not anyone is waiting - an empty house still counts.
     */
    @Override
    public void runCycle() {
        if (getOperator() == null) {
            System.out.println("WARNING: " + getName() + " cannot run - no operator assigned.");
            return;
        }
        System.out.println(getName() + " is starting a performance...");
        serveCycle();
    }
}