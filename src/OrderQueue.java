import java.util.ArrayList;

public class OrderQueue {
    private ArrayList<Order> orderQueue;
    private CustomSemaphore ordersSemaphore;

    public OrderQueue() {
        orderQueue = new ArrayList<>();
        ordersSemaphore = new CustomSemaphore(0); // Initially, no orders are available
    }

    /**
     * Adds an order to the queue in the correct position based on order time.
     * Uses binary insertion for efficiency.
     * @param order The order to be added.
     */
    public synchronized void addOrder(Order order) {
        // If the queue is empty, directly add the order
        if (orderQueue.isEmpty()) {
            orderQueue.add(order);
        } else {
            // Insert the order at the correct position to maintain the order based on preparation time
            int position = findInsertPosition(order.getOrderTime());
            orderQueue.add(position, order);
        }
        // Signal that a new order is available
        ordersSemaphore.release(order.getCustomerID());
    }

    /**
     * Retrieves and removes the first order in the queue.
     * Blocks if the queue is empty until an order is available.
     * @return The first order in the queue.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public Order takeOrder() throws InterruptedException {
        ordersSemaphore.acquire(); // Wait until an order is available
        synchronized (this) {
            return orderQueue.remove(0);
        }
    }

    /**
     * Checks if the queue is empty.
     * @return True if the queue is empty, false otherwise.
     */
    public synchronized boolean isEmpty() {
        return orderQueue.isEmpty();
    }

    /**
     * Helper method to find the correct position to insert an order based on its order time.
     * Performs a linear search to find the appropriate position.
     * @param orderTime The order time of the new order.
     * @return The position to insert the new order.
     */
    private int findInsertPosition(int orderTime) {
        for (int i = 0; i < orderQueue.size(); i++) {
            if (orderTime < orderQueue.get(i).getOrderTime()) {
                return i;
            }
        }
        return orderQueue.size(); // Insert at the end if no smaller orderTime is found
    }
}
