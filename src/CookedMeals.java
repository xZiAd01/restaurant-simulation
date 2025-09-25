import java.util.ArrayList;

/**
 * The CookedMeals class manages a queue of cooked meal orders.
 * This class uses a CustomSemaphore to manage synchronization between chefs and waiters.
 */
public class CookedMeals {
    // List to store cooked meals
    private ArrayList<Order> cookedMeals;
    // Semaphore to manage the number of cooked meals available
    private CustomSemaphore mealsSemaphore;

    /**
     * Constructor to initialize the CookedMeals queue.
     * Initializes an empty list and a semaphore with zero permits.
     */
    public CookedMeals() {
        cookedMeals = new ArrayList<>();
        mealsSemaphore = new CustomSemaphore(0); // Initially, no meals are available
    }

    /**
     * Retrieves and removes a cooked meal from the queue.
     * Blocks if no meals are available until a chef adds one.
     * @return The cooked meal order.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public Order getMeal() throws InterruptedException {
        mealsSemaphore.acquire(); // Wait until a meal is available
        synchronized (this) {
            return cookedMeals.remove(0);
        }
    }

    /**
     * Adds a cooked meal to the queue and notifies waiters.
     * @param meal The cooked meal to be added to the queue.
     */
    public void addMeal(Order meal) {
        synchronized (this) {
            cookedMeals.add(meal);
        }
        
        mealsSemaphore.release(meal.getCustomerID()); // Signal that a meal is available
    }
}
