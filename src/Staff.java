/**
 * A member of park staff, employed in a particular position and
 * responsible for operating attractions and carrying out inspections.
 */
public class Staff extends Person {

        private String position;

        /**
         * Creates a staff member.
         * @param id unique numeric identifier
         * @param name the staff member's full name
         * @param age the staff member's age in years
         * @param position the role they hold, e.g. "Ride Operator"
         */
        public Staff(int id, String name, int age, String position) {
            
            super(id, name, age);
            this.position = position;

        }

        /**
         * Carries out an inspection on any inspectable item in the park -
         * ride or facility alike - closing it, recording the outcome,
         * and reopening it.
         * @param item anything that honours the Inspectable contract
         * @param outcome the finding to record
         */
        public void performInspection(Inspectable item, String outcome) {
            System.out.println(getName() + " (" + position + ") is starting an inspection...");
            item.startInspection();
            item.completeInspection(outcome);
    }

        /**
         * @return the shared person details plus the staff member's position
         */
        @Override
        public String toString(){
    
            return super.toString() + " | Position: " + position;
        }

    
}
