import java.util.LinkedList;
import java.util.Queue;

/**
 * The CustomSemaphore class is a custom implementation of a semaphore
 * used to manage the availability of shared resources, such as tables.
 */
public class CustomSemaphore {
    // Number of available permits (resources)
    private int availablePermits;
    // Queue to store resource identifiers (e.g., table numbers)
    private Queue<Integer> resourceQueue;

    /**
     * Constructor to initialize the semaphore with a specific number of permits.
     * Each permit represents a resource that can be acquired or released.
     * @param permits The number of available resources.
     */
    public CustomSemaphore(int permits) {
        this.availablePermits = permits;
        this.resourceQueue = new LinkedList<>();
        // Initialize the resource queue with identifiers (e.g., table numbers)
        for (int i = 1; i <= permits; i++) {
            resourceQueue.add(i);
        }
    }

    /**
     * Acquires a resource from the semaphore.
     * Blocks if no resources are available until another thread releases one.
     * @return The identifier of the acquired resource.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public synchronized int acquire() throws InterruptedException {
        // Wait until a resource becomes available
        while (availablePermits <= 0) {
            wait();
        }
        // Decrement the available permits and return the acquired resource
        availablePermits--;
        return resourceQueue.poll(); // Remove and return the resource
    }

    /**
     * Releases a resource back to the semaphore.
     * Increases the number of available permits and notifies waiting threads.
     * @param resource The identifier of the resource to be released.
     */
    public synchronized void release(int resource) {
        // Increment the available permits and add the resource back to the queue
        availablePermits++;
        resourceQueue.add(resource);
        // Notify one of the waiting threads
        notify();
    }

    /**
     * Gets the number of currently available permits (resources).
     * @return The number of available permits.
     */
    public synchronized int getAvailablePermits() {
        return availablePermits;
    }

    /**
     * Checks whether the semaphore has available resources.
     * @return True if there are available permits, false otherwise.
     */
    public synchronized boolean hasAvailablePermits() {
        return availablePermits > 0;
    }
}
