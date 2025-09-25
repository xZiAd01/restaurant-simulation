import java.util.*;

/**
 * The Chef class represents a chef in the restaurant simulation.
 * Each chef runs in a separate thread, processing orders and preparing meals.
 */
public class Chef implements Runnable {
    // Unique ID for the chef
    private int chefID;

    /**
     * Constructor to initialize a Chef object with a unique ID.
     * @param chefID The unique ID for the chef.
     */
    public Chef(int chefID) {
        this.chefID = chefID;
    }

    /**
     * The run method defines the behavior of the chef thread.
     * The chef retrieves orders from the queue, prepares meals, and adds them to the cooked meals queue.
     */
    public void run() {
        try {
            while (true) {
                // Retrieve an order from the queue
                Order order = RestaurantSimulation.orderQueue.takeOrder();
                System.out.println("[" + RestaurantSimulation.minutesToTime(getCurrentTime()) + "] Chef " + chefID +
                        " starts preparing " + order.getOrderItem() + " for Customer " + order.getCustomerID());

                // Record the preparation start time
                int startTime = getCurrentTime();

                // Simulate preparation time
                Thread.sleep(order.getOrderTime() * 1000);

                // Update the total preparation time
                RestaurantSimulation.totalOrderPreparationTime += getCurrentTime() - startTime;
                System.out.println("[" + RestaurantSimulation.minutesToTime(getCurrentTime()) + "] Chef " + chefID +
                        " finishes preparing " + order.getOrderItem() + " for Customer " + order.getCustomerID());

                // Add the prepared meal to the cooked meals queue
                RestaurantSimulation.cookedMeals.addMeal(order);
            }
        } catch (Exception e) {
            System.out.println("An error occurred in Chef " + chefID + ": " + e.getMessage());
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
