/**
 * The TimeSimulation class is responsible for advancing the simulation time.
 * It runs in a separate thread and increments the simulation time every second.
 */
public class TimeSimulation implements Runnable {

    /**
     * The run method defines the behavior of the time simulation thread.
     * It continuously increments the current time in the simulation every second.
     */
    public void run() {
        try {
            while (true) {
                // Sleep for 1000 milliseconds (1 second) to simulate time passing
                Thread.sleep(1000);
                // Increment the current time in the simulation
                RestaurantSimulation.currentTime++;
            }
        } catch (InterruptedException e) {
            // Handle interruptions by printing the stack trace
            e.printStackTrace();
        }
    }
}
