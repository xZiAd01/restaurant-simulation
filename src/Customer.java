import java.time.LocalTime;

/**
 * The Customer class represents a customer in the restaurant simulation who places an order,
 * waits for a table, and eats the meal. This class implements the Runnable interface for
 * running in a separate thread.
 */
public class Customer implements Runnable {
    // Unique ID for the customer
    private int customerID;
    // Item the customer orders from the menu
    private String orderItem;
    // Time (in minutes since midnight) the customer arrives at the restaurant
    private int arrivalTime;
    // Semaphore to manage table availability
    private CustomSemaphore tableSemaphore;
    // Table number assigned to the customer
    private int tableNumber;

    /**
     * Constructor to initialize a Customer object.
     * @param customerID The unique ID for the customer.
     * @param orderItem The item the customer orders.
     * @param arrivalTime The time the customer arrives, in minutes since midnight.
     * @param tableSemaphore The custom semaphore managing table availability.
     * @throws Exception If there is an issue during initialization.
     */
    public Customer(int customerID, String orderItem, int arrivalTime, CustomSemaphore tableSemaphore) throws Exception {
        this.customerID = customerID;
        this.orderItem = orderItem;
        this.arrivalTime = arrivalTime;
        this.tableSemaphore = tableSemaphore;
    }

    /**
     * The run method defines the behavior of the customer thread.
     * The customer waits until their arrival time, gets seated at a table,
     * places an order, waits for the meal, eats the meal, and then leaves the restaurant.
     */
    public synchronized void run() {
        try {
            // Wait until the current simulation time matches the customer's arrival time
            while (getCurrentTime() < arrivalTime) {
                Thread.sleep(500); // Sleep for a short duration and then recheck
            }

            // Print a message when the customer arrives
            System.out.println("[" + RestaurantSimulation.minutesToTime(getCurrentTime()) + "] Customer " + this.customerID + " arrives.");

            // Acquire a table using the semaphore and print a message
            this.tableNumber = tableSemaphore.acquire();
            Thread.sleep(250); // Short pause to simulate time taken to get seated
            System.out.println("[" + RestaurantSimulation.minutesToTime(getCurrentTime()) + "] Customer " + this.customerID + " is seated at Table " + this.tableNumber);

            // Calculate and add the wait time to the total wait time in the simulation
            RestaurantSimulation.totalWaitTime += getCurrentTime() - getArrivalTime();

            // Place the order and add it to the order queue
            RestaurantSimulation.orderQueue.addOrder(new Order(customerID, orderItem, RestaurantSimulation.menuMap.get(orderItem)));
            System.out.println("[" + RestaurantSimulation.minutesToTime(getCurrentTime()) + "] Customer " + this.customerID + " places an order: " + this.orderItem);

            // Wait until the meal is served
            synchronized (this) {
                wait();
            }

            // Simulate eating the meal for a random duration between 10 and 20 seconds
            int randomTime = 10 + (int) (Math.random() * 11);
            Thread.sleep(randomTime * 1000);

            // Release the table and print messages when the customer finishes eating and leaves
            tableSemaphore.release(this.tableNumber);
            System.out.println("[" + RestaurantSimulation.minutesToTime(getCurrentTime()) + "] Customer " + this.customerID + " finishes eating and leaves the restaurant.");
            System.out.println("[" + RestaurantSimulation.minutesToTime(getCurrentTime()) + "] Table " + this.tableNumber + " is now available.");

            // Notify the simulation that the customer has finished
            RestaurantSimulation.customerFinished();

        } catch (Exception e) {
            // Print an error message if something goes wrong
            System.out.println("Something is wrong: " + e.getMessage());
        }
    }

    /**
     * Method to notify the customer when their meal is ready.
     * Wakes up the customer thread from waiting.
     */
    public synchronized void receiveMeal() {
        synchronized (this) {
            notify();
        }
    }

    // Getter for the customer ID
    public int getCustomerID() {
        return customerID;
    }

    // Getter for the order item
    public String getOrderItem() {
        return orderItem;
    }

    // Getter for the arrival time
    public int getArrivalTime() {
        return arrivalTime;
    }

    // Getter for the table number
    public int getTableID() {
        return tableNumber;
    }

    /**
     * Method to get the current simulation time.
     * @return Current time in the simulation, in minutes since midnight.
     */
    private int getCurrentTime() {
        return RestaurantSimulation.currentTime;
    }
}
