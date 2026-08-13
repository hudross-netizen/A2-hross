/**
 * Abstract base class for everyone in the park, staff and visitors alike.
 * Holds the details they all share: a unique numeric ID, a name and an age.
 * Cannot be instantiated directly - every person is a specific kind.
 */
public abstract class Person {

    private int id;
    private String name;
    private int age;

    /**
     * Creates a person with the details common to everyone in the park.
     * @param id unique numeric identifier
     * @param name the person's full name
     * @param age the person's age in years
     */

    public Person(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    /** @return the unique numeric identifier */
    public int getId() {
        return id;
    }

    /** @return the person's full name */
    public String getName() {
        return name;
    }

    /** @return the person's age in years */
    public int getAge() {
        return age;
    }
    
    /**
     * @return the shared details of any person, as one line of text
     */
    @Override
    public String toString(){

        return "ID: " + id + " | Name: " + name + " | Age: " + age;

    }
}
