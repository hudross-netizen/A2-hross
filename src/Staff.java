public class Staff extends Person {

        private String position;

        public Staff(int id, String name, int age, String position) {
            
            super(id, name, age);
            this.position = position;

        }

        @Override
        public String toString(){
    
            return super.toString() + " | Position: " + position;
        }

    
}
