/**
 * The Table class represents a table in the restaurant.
 * Each table has a unique ID and is used to seat customers.
 */
public class Table {
    // Unique ID for the table
    private int tableID;

    /**
     * Constructor to initialize a Table object with a specific ID.
     * @param tableID The unique ID for the table.
     */
    public Table(int tableID) {
        this.tableID = tableID;
    }

    /**
     * Getter method to retrieve the table ID.
     * @return The ID of the table.
     */
    public int getTableID() {
        return tableID;
    }
}
