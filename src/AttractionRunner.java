/**
 * The work of running one attraction for a number of cycles, expressed
 * as a Runnable so it can be carried out on its own thread while other
 * attractions run at the same time. Each cycle's visitors are added to
 * the park-wide total.
 */
public class AttractionRunner implements Runnable {
    private Attraction attraction;
    private ThemePark park;
    private int cyclesToRun;

    /**
     * Creates the task.
     * @param attraction the attraction to operate
     * @param park the park whose total is updated as visitors are served
     * @param cyclesToRun how many cycles to attempt
     */
    public AttractionRunner(Attraction attraction, ThemePark park, int cyclesToRun) {
        this.attraction = attraction;
        this.park = park;
        this.cyclesToRun = cyclesToRun;
    }

    /**
     * Runs the attraction's cycles, adding each cycle's visitors to the
     * park-wide total. The visitors served are measured from the growth
     * of the attraction's own history, so an attraction that refuses to
     * run simply adds nothing.
     */
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println(attraction.getName() + " starting on " + threadName + ".");
        for (int i = 0; i < cyclesToRun; i++) {
            int before = attraction.getTotalVisits();
            attraction.runCycle();
            park.addServed(attraction.getTotalVisits() - before);
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        System.out.println(attraction.getName() + " finished on " + threadName + ".");
    }
}