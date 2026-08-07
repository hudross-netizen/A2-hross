public class Staff extends Person {

        private String position;

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

        @Override
        public String toString(){
    
            return super.toString() + " | Position: " + position;
        }

    
}
