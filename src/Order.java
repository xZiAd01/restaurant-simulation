/**
 * The Order class represents a customer's order in the restaurant simulation.
 * Each order includes details about the customer, the item ordered, and the preparation time.
 */
public class Order {
    // ID of the customer who placed the order
    private int customerID;
    // The item ordered by the customer
    private String orderItem;
    // Time required to prepare the order, in seconds
    private int orderTime;

    /**
     * Constructor to initialize an Order object with the customer ID, order item, and order time.
     * @param customerID The ID of the customer who placed the order.
     * @param orderItem The name of the item ordered.
     * @param orderTime The time needed to prepare the order, in seconds.
     */
    public Order(int customerID, String orderItem, int orderTime) {
        this.customerID = customerID;
        this.orderItem = orderItem;
        this.orderTime = orderTime;
    }

    /**
     * Getter method to retrieve the customer ID.
     * @return The ID of the customer who placed the order.
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Getter method to retrieve the order item.
     * @return The name of the item ordered.
     */
    public String getOrderItem() {
        return orderItem;
    }

    /**
     * Getter method to retrieve the order time.
     * @return The time needed to prepare the order, in seconds.
     */
    public int getOrderTime() {
        return orderTime;
    }
}
