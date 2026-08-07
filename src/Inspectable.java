/**
 * A contract for anything in the park that can undergo an inspection -
 * attractions and facilities alike. Implementing classes are closed to
 * the public while an inspection is underway and reopened afterwards,
 * keeping a record of the most recent outcome.
 */
public interface Inspectable {

    /** Begins an inspection, closing this item to the public. */
    void startInspection();

    /**
     * Ends the inspection, recording its outcome and reopening the item.
     * @param outcome the inspector's finding, e.g. "Passed - all clear"
     */
    void completeInspection(String outcome);

    /**
     * @return true while an inspection is underway
     */
    boolean isClosedForInspection();
}