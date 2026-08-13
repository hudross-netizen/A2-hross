import java.util.Comparator;

/**
 * An alternative ordering for visitors: by ticket type first, then by
 * name within each ticket type. Kept in its own class so that Visitor
 * can offer this ordering without changing its natural ordering by age.
 */
public class VisitorTicketComparator implements Comparator<Visitor> {

    /**
     * Compares two visitors on two attributes in turn.
     * @param a the first visitor
     * @param b the second visitor
     * @return negative if a comes first, positive if b comes first,
     *         zero if they match on both attributes
     */
    @Override
    public int compare(Visitor a, Visitor b) {
        int ticketResult = a.getTicketType().compareTo(b.getTicketType());
        if (ticketResult != 0) {
            return ticketResult;
        }
        return a.getName().compareTo(b.getName());
    }
}