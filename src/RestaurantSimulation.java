import java.io.*;
import java.util.*;

/**
 * Names: Zeyad Alghamdi 2237000 - Eyas Majeed   2236567
 * Project: Multi-threaded Restaurant Simulation
 * Compiler: JDK 18.0.1.1
 * Hardware Configuration:
 *   Processor: AMD Ryzen 5 4600H
 *   RAM: 8 GB
 *   System Type: 64-bit
 * Operating System: Windows 11 Home
 */



/**
 * The RestaurantSimulation class simulates the operations of a restaurant, managing chefs,
 * waiters, tables, and customers, and executing a time-based simulation for order preparation and serving.
 */


public class RestaurantSimulation {
    // Current time in the simulation, represented in minutes since midnight
    public static int currentTime;
    // Queue to manage incoming orders
    public static OrderQueue orderQueue = new OrderQueue();
    // Object to manage prepared meals
    public static CookedMeals cookedMeals = new CookedMeals();
    // Map to keep track of customer information with customer ID as the key
    public static HashMap<Integer, Customer> customers = new HashMap<>();
    // Menu map with meal names as keys and preparation times (in minutes) as values
    public static HashMap<String, Integer> menuMap = new HashMap<>();
    // Counter for the number of active customers currently in the simulation
    public static int activeCustomerCount = 0;
    // Total number of customers served during the simulation
    public static int totalCustomersServed = 0;
    // Total wait time accumulated by all customers
    public static double totalWaitTime = 0;
    // Total time taken to prepare all orders
    public static double totalOrderPreparationTime = 0;
    // Total duration of the simulation in minutes
    public static int simulationTime;
    // Start time of the simulation in minutes since midnight
    public static int simulationStartTime;

    public static void main(String[] args) throws Exception {
        
        // File name containing simulation configuration and data
        String inputFile1 = "restaurant_simulation_input3";

        // Create a BufferedReader to read the input file
        BufferedReader read = new BufferedReader(new FileReader(inputFile1));

        // Read the first line (header) and parse the configuration values
        String configLine = read.readLine();
        String[] configArray = configLine.split(" ");
        HashMap<String, Integer> configMap = new HashMap<>();
        for (String part : configArray) {
            String[] keyAndValue = part.split("=");
            configMap.put(keyAndValue[0], Integer.parseInt(keyAndValue[1]));
        }

        // Extract the number of chefs, waiters, and tables from the configuration
        int numChefs = configMap.get("NC");
        int numWaiters = configMap.get("NW");
        int numTables = configMap.get("NT");

        // Initialize and create threads for chefs
        ArrayList<Thread> chefThreads = new ArrayList<>();
        Chef[] chefs = new Chef[numChefs];
        for (int i = 0; i < numChefs; i++) {
            chefs[i] = new Chef(i + 1);
            chefThreads.add(new Thread(chefs[i]));
        }

        // Initialize and create threads for waiters
        ArrayList<Thread> waiterThreads = new ArrayList<>();
        Waiter[] waiters = new Waiter[numWaiters];
        for (int i = 0; i < numWaiters; i++) {
            waiters[i] = new Waiter(i + 1);
            waiterThreads.add(new Thread(waiters[i]));
        }

        // Initialize tables for customers
        Table[] tables = new Table[numTables];
        for (int i = 0; i < numTables; i++) {
            tables[i] = new Table(i + 1);
        }

        // Create a custom semaphore to manage table availability
        CustomSemaphore tableSemaphore = new CustomSemaphore(numTables);

        // Read and parse the menu items and their preparation times
        String menuLine = read.readLine();
        String[] menuArray = menuLine.split(" ");
        for (String meal : menuArray) {
            String[] nameAndTime = meal.split("=");
            menuMap.put(nameAndTime[0], timeToMinutes(nameAndTime[1]));
        }

        // Read customer details and schedule their arrival
        ArrayList<Thread> customerThreads = new ArrayList<>();
        int leastArrivalTime = Integer.MAX_VALUE;  // Variable to keep track of the earliest arrival time
        String line;
        while ((line = read.readLine()) != null) {
            String[] parts = line.split(" ");
            int customerID = Integer.parseInt(parts[0].split("=")[1]);
            int arrivalTime = timeToMinutes(parts[1].split("=")[1]);
            String orderItem = parts[2].split("=")[1];

            // Create a new customer and store in the map
            Customer customer = new Customer(customerID, orderItem, arrivalTime, tableSemaphore);
            customers.put(customerID, customer);
            customerThreads.add(new Thread(customer));

            // Update the earliest arrival time
            if (arrivalTime < leastArrivalTime) {
                leastArrivalTime = arrivalTime;
            }
        }
        read.close();

        // Set the current simulation time to the earliest customer arrival time
        currentTime = leastArrivalTime;
        simulationStartTime = currentTime;

        // Start the time simulation thread
        Thread timeSimulationThread = new Thread(new TimeSimulation());
        timeSimulationThread.start();
        System.out.println("Simulation Started with " + numChefs + " Chefs, " + numWaiters + " Waiters, and " + numTables + " Tables.");

        // Start all customer, chef, and waiter threads
        for (Thread thread : customerThreads) {
            activeCustomerCount++;
            thread.start();
        }
        for (Thread thread : chefThreads) {
            thread.start();
        }
        for (Thread thread : waiterThreads) {
            thread.start();
        }

        // Wait for all customer, chef, and waiter threads to finish
        for (Thread thread : customerThreads) {
            thread.join();
        }
        for (Thread thread : chefThreads) {
            thread.join();
        }
        for (Thread thread : waiterThreads) {
            thread.join();
        }

        // Wait for the time simulation thread to complete
        timeSimulationThread.join();
    }

    /**
     * Converts time in "HH:MM" format to total minutes since midnight.
     * @param time The time string in "HH:MM" format.
     * @return Total minutes since midnight.
     */
    public static int timeToMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    /**
     * Converts total minutes since midnight to "HH:MM" format.
     * @param minutes The total minutes since midnight.
     * @return The time string in "HH:MM" format.
     */
    public static String minutesToTime(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }

    /**
     * Called when a customer has finished their visit, updating the active customer count.
     * If no more active customers, the simulation ends.
     */
    public static synchronized void customerFinished() {
        activeCustomerCount--;
        totalCustomersServed++;
        if (activeCustomerCount == 0) {
            System.out.println("[End of Simulation]");
            simulationTime = currentTime - simulationStartTime;
            displaySummary();
            System.exit(0);
        }
    }

    /**
     * Displays a summary of the simulation including total customers served,
     * average wait time, and total simulation duration.
     */
    public static void displaySummary() {
        System.out.println("\nSummary:");
        System.out.println("Total Customers Served: " + totalCustomersServed);
        System.out.println("Average Wait Time for Table: " + (totalWaitTime / totalCustomersServed) + " Minutes");
        System.out.println("Average Order Preparation Time: " + (totalOrderPreparationTime / totalCustomersServed) + " Minutes");
        System.out.println("Total Simulation Time: " + simulationTime + " Minutes");
    }
}
