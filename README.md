# restaurant-simulation
 Java multi-threaded restaurant simulation with waiters, chefs, customers, and synchronized order handling.
# Multi-threaded Restaurant Simulation

This project was developed as part of the **CPCS-361 Operating Systems course** at King Abdulaziz University.  
It simulates a restaurant environment with multiple customers, chefs, waiters, and tables — all running concurrently using **Java multi-threading and synchronization**.

## 🚀 Features
- Customers arrive at specific times and place orders.
- Waiters serve customers and deliver orders to the kitchen.
- Chefs prepare meals concurrently from the order queue.
- Custom semaphore manages table availability.
- Thread-safe order and meal queues.
- Time simulation for realistic restaurant flow.
- Tested with multiple input scenarios.

## 🛠️ Tech Stack
- **Language**: Java  
- **Concepts**: Multi-threading, Synchronization, Producer-Consumer, Custom Semaphores, Concurrency

## 📂 Project Structure
- `RestaurantSimulation.java` → Main driver class  
- `Customer.java` → Models customer behavior  
- `Waiter.java` → Waiter thread serving customers  
- `Chef.java` → Chef thread preparing meals  
- `Order.java` → Represents orders  
- `OrderQueue.java` → Thread-safe order queue  
- `CookedMeals.java` → Synchronized cooked meals queue  
- `CustomSemaphore.java` → Custom implementation of semaphore  
- `Table.java` → Table management  
- `TimeSimulation.java` → Advances simulation time  
- `restaurant_simulation_input1/2/3` → Sample input files  
- `CPCS361GroupG01POurReport.docx` → Final project report  

## ▶️ How to Run
1. Compile the code:
   ```bash
   javac *.java

2. Run the simulation with an input file:
   ```bash
   java RestaurantSimulation restaurant_simulation_input1
