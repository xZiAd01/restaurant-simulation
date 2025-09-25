
/**
 * The Waiter class represents a waiter in the restaurant simulation.
 * Each waiter runs in a separate thread, serving meals to customers.
 */
public class Waiter implements Runnable {
    // Unique ID for the waiter
    private int waiterID;

    /**
     * Constructor to initialize a Waiter object with a unique ID.
     * @param waiterID The unique ID for the waiter.
     */
    public Waiter(int waiterID) {
        this.waiterID = waiterID;
    }

    /**
     * The run method defines the behavior of the waiter thread.
     * The waiter retrieves cooked meals from the queue and serves them to customers.
     */
    public void run() {
        try {
            while (true) {
                // Retrieve a cooked meal from the queue
                Order meal = RestaurantSimulation.cookedMeals.getMeal();

                // Find the customer associated with the order
                Customer customer = RestaurantSimulation.customers.get(meal.getCustomerID());

                // Notify the customer that their meal has been served
                customer.receiveMeal();
                System.out.println("[" + RestaurantSimulation.minutesToTime(getCurrentTime()) + "] Waiter " + waiterID +
                        " serves " + meal.getOrderItem() + " to Customer " + customer.getCustomerID() + " at Table " + customer.getTableID());
            }
        } catch (Exception e) {
            System.out.println("An error occurred in Waiter " + waiterID + ": " + e.getMessage());
        }
    }

    /**
     * Retrieves the current simulation time.
     * @return The current time in the simulation, in minutes since midnight.
     */
    private int getCurrentTime() {
        return RestaurantSimulation.currentTime;
    }
}
