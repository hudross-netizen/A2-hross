public class Visitor extends Person implements Comparable<Visitor> {

    private String ticketType;

    public Visitor(int id, String name, int age, String ticketType) {
            
        super(id, name, age);
        this.ticketType = ticketType;

    }

    public Visitor(int id, String name, int age) {
        this (id, name, age, "Day Pass");
    }

    public String getTicketType() {
        return ticketType;
    }

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
