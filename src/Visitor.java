/**
 * A guest visiting the park, holding a ticket of a particular type.
 * Visitors carry a natural ordering by age and are identified by their
 * ID, so the same visitor is never counted twice in a collection.
 */
public class Visitor extends Person implements Comparable<Visitor> {

    private String ticketType;

    /**
     * Creates a visitor with a chosen ticket type.
     * @param id unique numeric identifier
     * @param name the visitor's full name
     * @param age the visitor's age in years
     * @param ticketType the ticket held, e.g. "Season Pass"
     */
    public Visitor(int id, String name, int age, String ticketType) {
            
        super(id, name, age);
        this.ticketType = ticketType;

    }

    /**
     * Creates a visitor holding the standard day pass.
     * @param id unique numeric identifier
     * @param name the visitor's full name
     * @param age the visitor's age in years
     */
    public Visitor(int id, String name, int age) {
        this (id, name, age, "Day Pass");
    }

    /** @return the type of ticket this visitor holds */
    public String getTicketType() {
        return ticketType;
    }

    /**
     * Two visitors are the same guest when they share an ID.
     * @param obj the object to compare against
     * @return true if the other object is a visitor with the same ID
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Visitor)) {
            return false;
        }
        Visitor other = (Visitor) obj;
        return this.getId() == other.getId();

    }

    /**
     * @return a hash code based on the ID, consistent with equals()
     */
    @Override
    public int hashCode() {
        return getId();
    }

    /**
     * Defines the natural ordering of visitors: by age, youngest first.
     * This single built-in order is what Collections.sort uses.
     * @param other the visitor to compare against
     * @return negative if this visitor is younger, positive if older, zero if the same age
     */
    @Override
    public int compareTo(Visitor other) {
        return this.getAge() - other.getAge();
    }

    /**
     * @return the visitor's shared details plus their ticket type
     */
    @Override
    public String toString() {
        return super.toString() + " | Ticket: " + ticketType;
    }

}
