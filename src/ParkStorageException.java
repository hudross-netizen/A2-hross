/**
 * Signals that the park could not be saved to or loaded from storage.
 * Checked (it extends Exception rather than RuntimeException) because
 * a missing or unreadable file is not a mistake the caller could have
 * avoided - so the compiler forces every caller to decide what to do
 * about it rather than discovering the problem in production.
 */
public class ParkStorageException extends Exception {

    /**
     * @param message a description of what went wrong
     */
    public ParkStorageException(String message) {
        super(message);
    }
}